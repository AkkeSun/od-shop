# <font color="#de7802">About Account</font>

Account 는 사용자 및 인증 관리를 위한 도메인 입니다.
이는 사용자 요청을 처리하는 API 서비스와 비동기 처리 및 스케쥴링을 위한 Agent 서비스로 구분 됩니다.
![Image](https://github.com/user-attachments/assets/d42927bb-948b-4cc3-afef-8c4628db0d3a)

| Service Name  | Host |
|---------------|------|
| Account       |      |
| Account Agent |      |

<br /><br />

### <font color="#92d050">Account Service</font>

Account Service 는 사용자 및 인증 관리를 처리하는 RESTFul API Service 입니다.
사용자는 이름, 이메일, 전화번호, 비밀번호를 포함한 정보를 등록/조회/수정/삭제 할 수 있으며 이메일 및 비밀번호를 통해 인증 토큰을 발급받을 수 있습니다.
발급받은 토큰은 권한이 필요한 리스소에 접근할 때 사용됩니다.
인증토큰 유효기간은 10분 이며 만료시 리프레시 토큰을 통해 인증 토큰을 갱신 할 수 있습니다.
리프레시 토큰의 유효기간은 3일 입니다.

API 명세서는 다음과 같으며 자세한 내용은 API Docs 를 참고해주세요.

| Method | URI       | 기능                                   |
|--------|-----------|--------------------------------------|
| GET    | /accounts | 사용자 정보를 조회합니다.                       |
| POST   | /accounts | 사용자 정보를 등록합니다.                       |
| PUT    | /accounts | 사용자 정보를 수정합니다.                       |
| DELETE | /accounts | 사용자 정보를 삭제합니다.                       |
| POST   | /auth     | 아이디 비밀번호로 로그인을 하여 <br>인증 토큰을 발급받습니다. |
| PUT    | /auth     | 리프레시 토큰으로 <br>인증 토큰을 갱신 합니다.         |
| DELETE | /auth     | 토큰 정보를 삭제합니다.                        |

<br /><br />

### <font color="#92d050">Account Agent Service</font>

Account Agent Service 는 외부 서비스에서 전송되는 Account 관련 메시지를 처리하는 컨슈머와 데이터 보존기간 정책에 따른 데이터 삭제를 수행하는 스케쥴러로
이루어진 서비스 입니다.
컨슈머는 내부 비즈니스 로직 예외 처리를 위한 DLQ (Dead Letter Queue) 패턴이 적용되어 있으며
스케쥴러는 동시에 하나의 프로세스만 실행 하도록 ShedLock 이 적용되어 있습니다.

| Agent Type           | 기능                                 |
|----------------------|------------------------------------|
| Consumer / Scheduler | 사용자 히스토리 (수정, 삭제)를 NoSql 에 저장 합니다. |
| Consumer             | 토큰 생성 내역을 NoSql 에 저장 합니다.          |
| Scheduler            | 3개월이 지난 로그 데이터를 삭제합니다.             |
| Scheduler            | 3일이 지난 리프레시 토큰 정보를 삭제합니다.          |

<br /> <br />

---

<br />

# <font color="#de7802">데이터 베이스 설계</font>

사용자 및 토큰 정보는 엄격한 스키마 관리가 유리하여 RDB를 사용하고 사용자 히스토리와 토큰 생성 내역은 읽기 작업보다 쓰기 작업이 빈번하므로 NoSQL을 사용하고 있습니다.
또한 토큰 갱신 시 사용되는 리프레시 토큰은 빠른 응답을 위해 Redis를 통해 캐싱하고 있습니다.

### <font color="#92d050">RDB Schema</font>

``` sql fold title:'ACCOUNT'
-- 사용자 정보가 저장되는 테이블 입니다.
create table ACCOUNT  
(  
    ID            bigint auto_increment  
        primary key,  
    EMAIL         varchar(50)  not null comment '이메일',  
    PASSWORD      varchar(100) not null comment '비밀번호',  
    USER_NAME     varchar(10)  null comment '이름',  
    USER_TEL      varchar(11)  null comment '전화번호',  
    ADDRESS       varchar(100) null comment '주소',  
    ROLE          varchar(20)  not null comment '권한',  
    REG_DATE      varchar(8)   not null comment '등록일',  
    REG_DATE_TIME timestamp    not null comment '등록일시'  
);  
  
create index ACCOUNT_IDX_1  
    on ACCOUNT (EMAIL, PASSWORD);
```

``` sql fold title:'TOKEN'
-- 리프레시 토큰 검증을 위한 테이블 입니다.
create table TOKEN  
(  
    ID            bigint auto_increment  
        primary key,  
    ACCOUNT_ID    bigint       null comment '사용자 아이디',  
    EMAIL         varchar(20)  null comment '사용자 이메일',  
    USER_AGENT    varchar(200) null comment '사용자 에이전트',  
    REFRESH_TOKEN varchar(150) null comment '리프레시 토큰',  
    ROLE          varchar(15)  null comment '사용자 권한',  
    REG_DATE_TIME varchar(20)  null comment '등록 일시'  
);  
  
create index TOKEN_idx1  
    on TOKEN (EMAIL, USER_AGENT);  
  
create index TOKEN_idx2  
    on TOKEN (REG_DATE_TIME);
```

<br />

### <font color="#92d050">NoSQL Document</font>

``` json fold title:'login_yyyyMMdd'
// 로그인(토 큰생성) 로그 
{
	"accountId": 12345,
	"email": "Bearer Token",
	"loginDateTime": "yyyyMMdd hh:mm:ss
}
```

``` json fold title:'history_yyyyMMdd'
// 사용자 정보 변경 내역
{
	"accountId":12345,
	"historyType": "update or delete",
	"detailInfo": "주소 변경",
	"regDateTime": "yyyyMMdd hh:mm:ss"	
}
```

``` json fold title:'dlq_yyyyMMdd'
{
	"topic": "토픽명",
	"payload": "요청받은 메시지",
	"regDateTime": "yyyyMMdd hh:mm:ss"
}
```

<br /> <br />

---

<br />

# <font color="#de7802">사용자 인증 프로세스</font>

![Image](https://github.com/user-attachments/assets/b95964a2-9580-4fb4-aec0-d262b5d4f6bb)

Account Service는 JWT(Json Web Token) 기반 인증 방식과 세션 기반 인증 방식을 결합한 하이브리드 인증 방식을 사용합니다.  
하이브리드 인증 방식은 리프레시 토큰(Refresh Token)을 세션 저장소(데이터베이스)에 저장하고 관리하는 방식으로 빠른 응답 속도와 높은 보안성을 동시에 제공합니다.

<br />

### <font color="#92d050">인증 토큰 발급 순서</font>

- 사용자가 입력한 아이디와 비밀번호를 검증합니다.
- 검증에 성공하면 인증토큰과 리프레시 토큰을 생성합니다.
- 리프레시 토큰을 Redis(1차 캐시) 와 RDB(2차 캐시) 에 각각 저장 합니다.
- 만약 1차 캐시 서버(Redis)에 문제가 발생하는 경우 서킷브레이커가 작동하여 1차 캐시에 접근하지 않도록 차단 되며 RDB 2차 캐시를 통해 리프레시 토큰을 검증
  합니다. (서킷브레이커에 대한 자세한 내용은 [다음](Circuit%20Breaker) 을 참고하세요)

<br /> 

### <font color="#92d050">인증 토큰 재발급 순서</font>

- 사용자가 인증토큰 재발급을 위해 리프레시 토큰을 전송합니다.
- 리프레시 토큰 유효성을 검증합니다.
- 리프레시 토큰에서 이메일을 추출합니다.
- API 를 호출한 사용자의 에이전트를 추출합니다.
- 이메일과 사용자 에이전트로 저장된 리프레시 토큰 정보를 정보를 조회합니다.
- 조회된 정보가 있다면 사용자가 요청한 리프레시 토큰과 동일한지 검증합니다.
- 리프레시 토큰과 인증 토큰을 갱신하여 응답합니다.

