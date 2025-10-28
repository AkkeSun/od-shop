# <font color="#de7802">About Order</font>

Order Service는 주문 관리를 처리하는 RESTful API Service 입니다. <br />
사용자는 상품 예약을 생성하고 주문을 등록할 수 있으며 <br />
주문 내역을 조회하거나 주문을 취소할 수 있습니다. <br />
판매자는 자신의 판매 상품 목록을 조회할 수 있습니다. <br />
Product 서비스와 gRPC 통신을 통해 상품 예약/확정/취소를 처리합니다. <br />

![Image](https://github.com/user-attachments/assets/86fdb553-3722-4235-a94e-3f85e7c40009)

| Service Name | Host                 |
|--------------|----------------------|
| Order        | https://api.odlab.kr |

<br /><br />


#### REST API

API 명세서는 다음과 같으며 자세한 내용은 [API Docs](https://api.odlab.kr/od-shop?urls.primaryName=order) 를 참고해주세요.

| Method | URI                          | 기능                                 |
|--------|------------------------------|-------------------------------------|
| POST   | /orders/reserve              | 상품 예약을 생성합니다. <br>(Product 서비스 gRPC 호출) |
| POST   | /orders                      | 주문을 등록합니다. <br>(예약 확정)               |
| GET    | /orders                      | 고객의 주문 내역을 조회합니다.                   |
| DELETE | /orders/{orderNumber}        | 주문을 취소합니다. <br>(보상 트랜잭션)             |
| GET    | /orders/sold-products        | 판매자의 판매 상품을 조회합니다. <br>(판매자만 가능)     |

<br />

#### gRPC Client

Product 서비스와 통신하기 위한 gRPC Client를 사용합니다.

| Service                   | Method         | 기능                    |
|---------------------------|----------------|------------------------|
| CreateProductReservation  | createReserve  | Product 서비스에 상품 예약 요청  |
| ConfirmProductReservation | confirmReserve | Product 서비스에 상품 예약 확정 요청 |
| CancelProductReservation  | cancelReserve  | Product 서비스에 상품 예약 취소 요청 (보상 트랜잭션) |
| FindProduct               | findProduct    | Product 서비스에서 상품 정보 조회  |
| ExistsCustomerProduct     | exists         | 고객의 상품 구매 여부 확인       |
| FindOrderProductIds       | findProductIds | 주문의 상품 ID 목록 조회       |

<br />
<br />
<br />

---

<br />
<br />

# <font color="#de7802">데이터 베이스 설계</font>

주문 정보는 엄격한 스키마 관리에 유리한 RDB를 사용합니다. <br />
주문 내역 조회 시 빠른 응답을 위해 Redis를 통해 캐싱하고 있습니다. <br/>
데이터베이스 선택에 대한 자세한
내용은 [od-lab](https://velog.io/@akkessun/%EC%96%B4%EB%96%A4-%EC%A0%80%EC%9E%A5%EC%86%8C%EB%A5%BC-%EC%84%A0%ED%83%9D%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94%EA%B0%80)
을 참고하세요.

<br />

### <font color="#92d050">RDB Schema</font>

``` sql
-- 주문 정보가 저장되는 테이블입니다.
create table `ORDER`
(
    ORDER_NUMBER     bigint auto_increment
        primary key,
    CUSTOMER_ID      bigint       not null comment '구매자 아이디',
    TOTAL_PRICE      int          not null comment '총 결제 금액',
    RECEIVER_NAME    varchar(100) not null comment '받는사람 이름',
    RECEIVER_ADDRESS varchar(150) not null comment '받는사람 주소',
    RECEIVER_TEL     varchar(100) not null comment '받는사람 전화번호',
    REG_DATE_TIME    timestamp    not null comment '등록일시'
)
    comment '주문 정보';

create index ORDER_idx1
    on `ORDER` (CUSTOMER_ID);
```

``` sql
-- 주문 상품 정보가 저장되는 테이블입니다.
create table ORDER_PRODUCT
(
    ID            bigint auto_increment
        primary key,
    ORDER_NUMBER  bigint      not null comment '주문번호',
    PRODUCT_ID    bigint      not null comment '상품 아이디',
    SELLER_ID     bigint      null comment '판매자 아이디',
    BUY_QUANTITY  int         null comment '구매 수량',
    BUY_STATUS    varchar(20) null comment '구매 상태',
    REG_DATE_TIME timestamp   not null comment '등록일시',
    UPD_DATE_TIME timestamp   null comment '수정일시',
    constraint ORDER_PRODUCT_fk
        foreign key (ORDER_NUMBER) references `ORDER` (ORDER_NUMBER)
)
    comment '구매 상품 정보';
```

``` sql
-- 주문 히스토리가 저장되는 테이블입니다.
create table ORDER_HISTORY
(
    ID            bigint auto_increment
        primary key,
    ORDER_NUMBER  bigint       not null,
    DESCRIPTION   varchar(100) not null,
    REG_DATE_TIME timestamp    not null
)
    comment '주문 상태 변경 내역';

create index ORDER_HISTORY_idx1
    on ORDER_HISTORY (ORDER_NUMBER);
```

<br />
<br />
<br />

---

<br />
<br />

# <font color="#de7802">주문 프로세스</font>

![Image](https://github.com/user-attachments/assets/84aa05fa-91c0-475d-9bfa-64266baddd0f)

Order Service는 Product 서비스와 gRPC 통신을 통해 주문을 처리합니다. <br/>
주문 프로세스는 Saga 패턴을 기반으로 분산 트랜잭션을 처리하며 <br />
실패 시 보상 트랜잭션을 통해 데이터 일관성을 보장합니다.

<br />

### <font color="#92d050">주문 생성 순서</font>

#### 1. 상품 예약 (Reserve Product)

- 사용자가 장바구니에서 구매할 상품을 선택합니다.
- Order 서비스가 Product 서비스에 gRPC로 상품 예약을 요청합니다.
- Product 서비스는 상품 재고를 확인하고 예약 가능 여부를 검증합니다.
- 예약이 성공하면 RESERVED_QUANTITY를 증가시키고 예약 ID를 반환합니다.
- 예약 ID와 상품 정보를 사용자에게 응답합니다.

<br />

#### 2. 주문 등록 (Register Order)

- 사용자가 배송 정보(받는 사람, 전화번호, 주소)를 입력하고 주문을 확정합니다.
- Order 서비스가 예약 정보를 검증합니다.
- Product 서비스에 gRPC로 예약 확정을 요청합니다.
  - Product 서비스는 RESERVED_QUANTITY를 감소시키고 실제 QUANTITY를 차감합니다.
  - 예약 상태를 'CONFIRMED'로 변경합니다.
- 주문 정보를 ORDER 테이블에 저장합니다.
- 주문 상품 정보를 ORDER_PRODUCT 테이블에 저장합니다.
- 주문 번호를 사용자에게 응답합니다.

<br />

### <font color="#92d050">주문 취소 순서 (보상 트랜잭션)</font>

- 사용자가 주문 취소를 요청합니다.
- Order 서비스가 주문 정보를 조회합니다.
- ORDER_PRODUCT의 BUY_STATUS를 'CANCEL'로 변경합니다.
- Product 서비스에 예약 취소를 위한 메시지를 전송합니다.
- 예약 상태를 'CANCELLED'로 변경합니다.
- ORDER_HISTORY에 취소 이력을 저장합니다.
- 취소 완료 응답을 사용자에게 반환합니다.