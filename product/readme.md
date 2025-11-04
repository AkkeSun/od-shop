# <font color="#de7802">About Product</font>

Product는 상품 관리를 위한 도메인입니다. <br />
이는 상품 요청을 처리하는 API 서비스와 비동기 처리 및 스케줄링을 위한 Agent 서비스로 구분됩니다. <br />
![Image](https://github.com/user-attachments/assets/0176b069-29b9-4845-a4e9-2fc2d14abc60)

| Service Name | Host                 |
|--------------|----------------------|
| Product      | https://api.odlab.kr |

<br /><br />

### <font color="#92d050">Product Service</font>

Product Service는 상품 관리를 처리하는 RESTful API Service 및 gRPC Service 입니다. <br />
사용자는 상품 정보를 등록/조회/수정/삭제 할 수 있으며 <br />
상품 리뷰를 작성하고 조회할 수 있습니다. <br />
또한 추천 상품 조회 기능을 통해 개인화된 상품 추천을 받을 수 있습니다. <br />
내부 서비스 간 통신을 위한 gRPC API를 제공하여 상품 예약/확정/취소 등의 기능을 처리합니다. <br />

#### REST API

API 명세서는 다음과 같으며 자세한 내용은 [API Docs](https://api.odlab.kr/od-shop?urls.primaryName=product) 를 참고해주세요.

| Method | URI                            | 기능                                   |
|--------|--------------------------------|--------------------------------------|
| GET    | /products                      | 상품 목록을 조회합니다. (검색, 카테고리, 정렬 지원)     |
| POST   | /products                      | 상품을 등록합니다. <br>(판매 권한 필요)           |
| GET    | /products/{productId}          | 특정 상품을 조회합니다.                       |
| PUT    | /products/{productId}          | 상품 정보를 수정합니다. <br>(판매자만 가능)         |
| DELETE | /products/{productId}          | 상품을 삭제합니다. <br>(판매자만 가능)            |
| PUT    | /products/{productId}/quantity | 상품 수량을 수정합니다. <br>(판매자 또는 관리자만 가능)  |
| GET    | /products/recommendations      | 추천 상품 목록을 조회합니다. <br>(개인화 추천 포함)    |
| GET    | /products/{productId}/reviews  | 상품 리뷰를 조회합니다.                       |
| POST   | /products/{productId}/reviews  | 상품 리뷰를 등록합니다. <br>(구매자만 가능)         |

<br />

#### gRPC API

내부 서비스 간 통신을 위한 gRPC API를 제공합니다.

| Service                       | Method           | 기능                     |
|-------------------------------|------------------|------------------------|
| CreateProductReservation      | createReserve    | 상품 예약을 생성합니다.          |
| ConfirmProductReservation     | confirmReserve   | 상품 예약을 확정합니다.          |
| CancelProductReservation      | cancelReserve    | 상품 예약을 취소합니다.          |
| FindProduct                   | findProduct      | 상품 정보를 조회합니다.          |
| ExistsCustomerProduct         | exists           | 고객의 상품 구매 여부를 확인합니다.   |
| FindOrderProductIds           | findProductIds   | 주문의 상품 ID 목록을 조회합니다.   |

<br /><br />

### <font color="#92d050">Product Agent Service</font>

Product Agent Service는 외부 서비스에서 전송되는 Product 관련 메시지를 처리하는 컨슈머와 <br/>
데이터 보존기간 정책에 따른 데이터 삭제 및 메트릭 수집을 수행하는 스케줄러로 이루어진 서비스입니다. <br/>
컨슈머는 내부 비즈니스 로직 예외 처리를 위한 [DLQ(Dead Letter Queue)](https://velog.io/@akkessun/%EB%B8%8C%EB%A1%9C%EC%BB%A4-%EC%98%A4%EB%A5%98%EC%B2%98%EB%A6%AC-%EC%A0%84%EB%9E%B5) 패턴이 적용되어 있으며 <br />
스케줄러는 동시에 하나의 프로세스만 실행하도록 ShedLock 이 적용되어 있습니다. <br />

| Agent Type           | 기능                                      |
|----------------------|-----------------------------------------|
| Consumer             | 상품 수량 업데이트 (주문, 취소, 환불)를 처리합니다.         |
| Consumer             | 상품 삭제 이벤트를 처리하여 NoSQL에 히스토리를 저장합니다.    |
| Scheduler            | 상품 메트릭 (조회수, 판매량, 리뷰 점수 등)을 수집하여 저장합니다. |
| Scheduler            | ElasticSearch에 상품 정보를 동기화합니다.           |
| Scheduler            | 상품 검색 로그를 수집하여 NoSQL에 저장합니다.            |
| Scheduler            | 상품 히스토리를 NoSQL에 저장합니다.                  |
| Scheduler            | 상품 클릭 로그를 수집하여 NoSQL에 저장합니다.            |
| Scheduler            | 3개월이 지난 로그 데이터를 삭제합니다.                  |

<br />

#### 배치 메시지 처리 방식 (Kafka Batch Processing)

Product Agent는 로그 데이터(클릭 로그, 검색 로그, 히스토리)를 효율적으로 처리하기 위해 <br />
스케줄러 기반 배치 처리 방식을 사용합니다.

##### 처리 흐름
1. **메시지 수집**: Kafka 브로커에서 메시지가 토픽별로 누적됩니다.
2. **주기적 로드**: 스케줄러가 1분마다 실행되어 Kafka Consumer를 통해 메시지를 가져옵니다.
   - `consumer.poll(Duration.ofSeconds(5))`: 최대 5초 동안 대기하며 메시지 수집
   - `MAX_POLL_RECORDS_CONFIG`: 한 번에 최대 500개의 메시지를 가져옴
3. **일괄 저장**: 수집된 메시지를 파싱하여 MongoDB에 일괄 저장합니다.
4. **커밋**: 모든 메시지 처리가 완료되면 `consumer.commitSync()`로 오프셋을 커밋합니다.

##### 에러 처리
- **재시도 메커니즘**: 처리 실패 시 최대 3회까지 재시도합니다.
- **DLQ 전송**: 3회 재시도 실패 시 Dead Letter Queue에 메시지를 저장하여 추후 분석합니다.
- **트랜잭션 보장**: 수동 커밋을 통해 메시지 유실 방지

##### 배치 처리 대상
- **상품 클릭 로그**: Product 서비스에서 발행 → 1분마다 배치 처리 → MongoDB 저장
- **상품 검색 로그**: Product 서비스에서 발행 → 1분마다 배치 처리 → MongoDB 저장
- **상품 히스토리**: Product 서비스에서 발행 → 1분마다 배치 처리 → MongoDB 저장

<br />

#### 상품 메트릭 업데이트 방식

상품의 인기도와 트렌드를 파악하기 위해 메트릭을 주기적으로 수집하고 집계합니다.

##### 처리 흐름
1. **스케줄링**: 10분마다 `UpdateProductMetricScheduler`가 실행됩니다.
2. **데이터 수집**: 마지막 업데이트 시간부터 현재까지의 데이터를 수집합니다.
   - **리뷰 데이터**: RDB의 REVIEW 테이블에서 조회 (리뷰 개수, 평점)
   - **주문 데이터**: gRPC로 Order 서비스에서 조회 (판매량)
   - **클릭 로그**: MongoDB에서 조회 (조회수)
3. **메트릭 집계**: Product 도메인 객체에 데이터를 집계합니다.
   - `reviewCount`: 리뷰 개수 집계
   - `reviewScore`: 리뷰 평점 평균 계산
   - `salesCount`: 판매량 집계
   - `hitCount`: 조회수 집계
   - `totalScore`: 종합 점수 계산 (판매량, 리뷰 점수, 조회수 가중 평균)
4. **일괄 업데이트**: RDB의 PRODUCT_METRIC 테이블에 일괄 업데이트합니다.
5. **시간 기록**: 마지막 업데이트 시간을 MongoDB에 저장하여 다음 실행 시 참조합니다.

##### 메트릭 활용
- **상품 추천 시스템**: 인기 상품(POPULAR)과 트렌드 상품(TREND) 선정에 사용
- **상품 랭킹**: 검색 결과 정렬 시 메트릭 점수 활용
- **ElasticSearch 동기화**: 메트릭 업데이트 시 `NEEDS_ES_UPDATE` 플래그 설정

<br />
<br />
<br />

---

<br />
<br />

# <font color="#de7802">데이터 베이스 설계</font>

상품 정보는 엄격한 스키마 관리에 유리한 RDB 를 사용하며, 대용량 텍스트 기반 검색과 벡터 검색을 위해 Elasticsearch를 함께 사용합니다. <br>
상품 검색 로그, 클릭 로그, 히스토리 데이터는 읽기보다 쓰기 작업이 빈번하므로 NoSQL을 사용하며 
상품 조회 시 빠른 응답을 위해 Redis 로 캐싱 합니다. <br>
데이터베이스 선택에 대한 자세한
내용은 [od-lab](https://velog.io/@akkessun/%EC%96%B4%EB%96%A4-%EC%A0%80%EC%9E%A5%EC%86%8C%EB%A5%BC-%EC%84%A0%ED%83%9D%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94%EA%B0%80)
을 참고하세요.

<br />

### <font color="#92d050">RDB 샤딩 (Database Sharding)</font>

대용량 트래픽 처리를 위해 데이터베이스를 [샤딩](https://velog.io/@akkessun/%EB%8D%B0%EC%9D%B4%ED%84%B0-%EB%B6%84%EC%82%B0%EC%B2%98%EB%A6%AC)하여 사용합니다. <br>
샤드 키는 Product 테이블의 ID 이며 Snowflake 알고리즘으로 생성합니다. <br>
```
(timestamp XOR workerId) % 2 == 0 → Shard1
(timestamp XOR workerId) % 2 == 1 → Shard2
```

<br />

### <font color="#92d050">RDB Schema</font>

``` sql
-- 상품 정보가 저장되는 테이블입니다.
create table PRODUCT
(
    ID                bigint        not null
        primary key,
    SELLER_ID         bigint        not null comment '판매자 아이디',
    SELLER_EMAIL      varchar(50)   not null comment '판매자 이메일',
    PRODUCT_NAME      varchar(50)   not null comment '상품명',
    PRODUCT_IMG       varchar(50)   not null comment '상품 이미지 주소',
    DESCRIPTION       varchar(50)   not null comment '상품 설명 이미지',
    KEYWORD           varchar(255)  null comment '상품 키워드',
    PRICE             int           not null comment '상품 금액',
    QUANTITY          int           not null comment '상품 수량',
    RESERVED_QUANTITY int default 0 not null comment '예약 상품 수량',
    CATEGORY          varchar(10)   null comment '상품 카테고리 (DIGITAL, FASHION, SPORTS, FOOD, LIFE, TOTAL)',
    DELETE_YN         varchar(1)    null comment '삭제 유무',
    REG_DATE          date          not null comment '등록일',
    REG_DATE_TIME     datetime      not null comment '등록 일시',
    UPDATE_DATE_TIME  datetime      null comment '수정 일시'
)
    comment '상품 데이터';

create index PRODUCT_IDX_1
    on PRODUCT (SELLER_ID, DELETE_YN);

create index PRODUCT_IDX_2
    on PRODUCT (DELETE_YN, CATEGORY);
```

``` sql
-- 상품 리뷰가 저장되는 테이블입니다.
create table REVIEW
(
    ID             bigint      not null
        primary key,
    SCORE          double      not null comment '점수 (0.5 ~ 5.0)',
    PRODUCT_ID     bigint      not null comment '상품 아이디',
    CUSTOMER_ID    bigint      not null comment '구매자 아이디',
    CUSTOMER_EMAIL varchar(50) not null comment '구매자 이메일',
    REVIEW         varchar(50) not null comment '리뷰',
    REG_DATE       date        not null comment '등록일',
    REG_DATE_TIME  datetime    not null comment '등록일시'
)
    comment '리뷰';

create index REVIEW_idx1
    on REVIEW (PRODUCT_ID);

create index REVIEW_idx2
    on REVIEW (REG_DATE_TIME);
```

``` sql
create table PRODUCT_METRIC
(
    ID               bigint   not null
        primary key,
    PRODUCT_ID       bigint   not null comment '상품 아이디',
    SALES_COUNT      int      not null comment '판매수',
    REVIEW_COUNT     int      not null comment '리뷰수',
    HIT_COUNT        int      not null comment '조회수',
    REVIEW_SCORE     double   not null comment '리뷰점수',
    TOTAL_SCORE      double   not null comment '총점',
    NEEDS_ES_UPDATE  bit      not null comment '엘라스틱서치 업데이트 필요유무',
    REG_DATE         date     not null comment '등록일',
    REG_DATE_TIME    datetime not null comment '등록일시',
    UPDATE_DATE_TIME datetime null comment '수정일시',
    constraint PRODUCT_METRIC_fk
        foreign key (PRODUCT_ID) references PRODUCT (ID)
)
    comment '상품 메트릭';
```

``` sql
-- 상품 예약 히스토리가 저장되는 테이블입니다.
create table PRODUCT_RESERVE_HISTORY
(
    ID                bigint                              not null
        primary key,
    PRODUCT_ID        bigint                              not null comment '상품 아이디',
    CUSTOMER_ID       int                                 not null comment '구매자 아이디',
    RESERVED_QUANTITY int                                 not null comment '예약 상품 수량',
    REG_DATE_TIME     timestamp default CURRENT_TIMESTAMP not null comment '등록일시'
)
    comment '상품 예약 정보';

create index PRODUCT_RESERVE_HISTORY_PRODUCT_ID_CUSTOMER_ID_index
    on PRODUCT_RESERVE_HISTORY (PRODUCT_ID, CUSTOMER_ID);
```

``` sql
-- 추천 상품 정보가 저장되는 테이블입니다.
create table PRODUCT_RECOMMEND
(
    ID            bigint      not null
        primary key,
    PRODUCT_ID    bigint      not null comment '상품 아이디',
    TYPE          varchar(10) not null comment '추천 타입 (POPULAR, TREND)',
    CHECK_DATE    date        not null comment '조회 타겟 날짜',
    REG_DATE      date        not null comment '등록일',
    REG_DATE_TIME datetime    not null comment '등록일시'
)
    comment '추천상품 데이터';

create index PRODUCT_RECOMMEND_idx1
    on PRODUCT_RECOMMEND (CHECK_DATE);
```

<br />

### <font color="#92d050">NoSQL Document</font>

``` json
// 상품 검색 로그
{
  "customerId": 12345,
  "searchKeyword": "노트북",
  "category": "ELECTRONICS",
  "searchDateTime": "yyyyMMdd hh:mm:ss"
}
```

``` json
// 상품 클릭 로그
{
  "customerId": 12345,
  "productId": 67890,
  "clickDateTime": "yyyyMMdd hh:mm:ss"
}
```

``` json
// 상품 변경 히스토리
{
  "productId": 67890,
  "type": "update or delete",
  "detailInfo": "상세 정보",
  "regDateTime": "yyyyMMdd hh:mm:ss"
}
```

``` json
// dlq 로그 (컨슈머 비즈니스로직 3회 실패시 기록되는 로그)
{
  "topic": "토픽명",
  "payload": "요청받은 메시지",
  "regDateTime": "yyyyMMdd hh:mm:ss"
}
```

<br />
<br />
<br />

---

<br />
<br />

# <font color="#de7802">상품 예약 프로세스</font>

![Image](https://github.com/user-attachments/assets/84aa05fa-91c0-475d-9bfa-64266baddd0f)

Product Service는 주문 서비스와의 통신을 위해 gRPC 기반 상품 예약 시스템을 제공합니다. <br/>
상품 예약은 분산 트랜잭션 처리를 위한 Saga 패턴의 일부로 동작하며 <br />
동시성 제어를 위해 [Redisson 분산 락](https://velog.io/@akkessun/%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88%EC%99%80-%ED%95%B4%EA%B2%B0)을 사용합니다.
<br/>

<br />

### <font color="#92d050">상품 예약 생성 순서</font>

- Order 서비스에서 gRPC로 상품 예약 생성 요청을 받습니다.
- 분산 락을 획득하여 동시성 문제를 방지합니다.
- 상품 재고(QUANTITY)를 확인하고 충분한 재고가 있는지 검증합니다.
- 예약 수량만큼 RESERVED_QUANTITY를 증가시킵니다.
- 예약 히스토리를 PRODUCT_RESERVE_HISTORY에 저장합니다.
- 예약 ID를 응답하고 분산 락을 해제합니다.

<br />

### <font color="#92d050">상품 예약 확정 순서</font>

- Order 서비스에서 gRPC로 상품 예약 확정 요청을 받습니다.
- 분산 락을 획득합니다.
- 예약 ID로 예약 히스토리를 조회합니다.
- RESERVED_QUANTITY를 감소시키고 실제 QUANTITY를 감소시킵니다.
- 예약 히스토리 상태를 'CONFIRMED'로 업데이트합니다.
- Kafka로 상품 수량 업데이트 이벤트를 발행합니다.
- 분산 락을 해제합니다.

<br />

### <font color="#92d050">상품 예약 취소 순서 (보상 트랜잭션)</font>

- Order 서비스에서 gRPC로 상품 예약 취소 요청을 받습니다.
- 분산 락을 획득합니다.
- 예약 ID로 예약 히스토리를 조회합니다.
- RESERVED_QUANTITY를 감소시킵니다. (재고 복구)
- 예약 히스토리 상태를 'CANCELLED'로 업데이트합니다.
- 분산 락을 해제합니다.

<br />
<br />
<br />

---

<br />
<br />

# <font color="#de7802">상품 추천 알고리즘</font>
Product Service는 사용자 맞춤형 상품 추천 시스템을 제공합니다. <br/>
추천 시스템은 개인 맞춤형, 인기 상품, 트렌드 상품 세 가지 타입으로 구분되며, <br />
AI 임베딩 기반 검색과 ElasticSearch를 활용하여 고도화된 추천을 제공합니다.

<br />

### <font color="#92d050">추천 타입</font>

| 추천 타입    | 설명                       | 데이터 소스                    |
|----------|--------------------------|---------------------------|
| PERSONAL | 사용자의 구매 이력 기반 맞춤형 추천     | Gemini AI + ElasticSearch |
| POPULAR  | 판매량 기반 인기 상품 추천          | RDB (메트릭 데이터)             |
| TREND    | 대형 쇼핑몰에서 수집한 인기 상품 기반 추천 | RDB (메트릭 데이터)             |

<br />

### <font color="#92d050">개인 맞춤형 추천 (PERSONAL) 알고리즘</font>

개인 맞춤형 추천은 Gemini AI의 임베딩 기술과 ElasticSearch를 결합하여 구현되었습니다.

#### 추천 순서

- 사용자의 최근 주문 이력을 조회합니다. (최대 15개)
- 각 주문 상품의 정보를 조회합니다.
- Gemini API를 사용하여 각 상품의 임베딩 벡터를 생성합니다.
  - 상품명, 카테고리, 키워드 등을 결합한 문서를 임베딩합니다.
- 생성된 임베딩 벡터들의 평균을 계산합니다.
  - 사용자의 전반적인 선호도를 나타내는 벡터를 생성합니다.
- ElasticSearch에서 유사도 검색을 수행합니다.
  - 평균 임베딩과 코사인 유사도가 높은 상품을 검색합니다.
- 이미 구매한 상품은 추천 목록에서 제외합니다.
- Redis에 결과를 캐싱하여 성능을 최적화합니다.

<br />

### <font color="#92d050">인기 상품 추천 (POPULAR) 알고리즘</font>

인기 상품 추천은 판매 데이터를 기반으로 합니다.

#### 추천 순서

- 매주 월요일 기준으로 주간 판매량을 집계합니다.
- PRODUCT_METRIC 테이블에서 SALES_COUNT가 높은 상품을 조회합니다.
- RECOMMEND 테이블에 저장된 인기 상품 목록을 조회합니다.
- Redis에 캐싱하여 반복 조회 시 성능을 향상시킵니다.

<br />

### <font color="#92d050">트렌드 상품 추천 (TREND) 알고리즘</font>

트렌드 상품 추천 알고리즘은 [od-trend](https://github.com/AkkeSun/od-trend) 프로젝트를 통해 수집된 상품을 기반으로 처리됩니다.

#### 추천 순서

- 매주 월요일 기준으로 주간 메트릭을 분석합니다.
- 조회수, 판매량, 리뷰 점수의 증가율을 계산합니다.
- 증가율이 높은 상품을 트렌드 상품으로 선정합니다.
- RECOMMEND 테이블에 저장된 트렌드 상품 목록을 조회합니다.
- Redis에 캐싱하여 성능을 최적화합니다.

<br />

### <font color="#92d050">추천 결과 최적화</font>

추천 결과는 다음과 같이 최적화됩니다:

#### 중복 제거
- 개인 맞춤형, 인기 상품, 트렌드 상품 간 중복되는 상품을 제거합니다.
- 우선순위: 개인 맞춤형 > 인기 상품 > 트렌드 상품

#### 랜덤화
- 각 추천 타입별로 결과를 셔플하여 다양성을 제공합니다.
- 매번 다른 순서로 노출되어 사용자 경험을 향상시킵니다.

#### 개수 제한
- 각 추천 타입별로 최대 10개의 상품만 반환합니다.

#### 캐싱 전략
- **개인 맞춤형 추천**: 개별 사용자별로 캐싱 (TTL: 1일)
- **인기/트렌드 추천**: 전체 사용자 공통으로 캐싱 (TTL: 1주)
- Redis 캐시를 활용하여 응답 속도를 대폭 향상
