# <font color="#de7802">OD-SHOP</font>

---

od-shop은 헥사고날 아키텍처 기반의 마이크로서비스 쇼핑몰 플랫폼입니다. 

Account, Product, Order 도메인을 중심으로 각각의 비즈니스 서비스와 이벤트 처리를 위한 Agent 서비스로 구성되어 있습니다.

### 핵심 특징

- **헥사고날 아키텍처** (Ports & Adapters Pattern)
- **마이크로서비스 아키텍처** (Domain-Driven Design)
- **이벤트 기반 비동기 통신** (Kafka)
- **고성능 동기 통신** (gRPC)
- **분산 시스템 패턴** (Circuit Breaker, Distributed Lock, Saga Pattern)
- **JWT 기반 인증/인가** (Hybrid Authentication)
- **CI/CD 구축** (Docker Image Based Github Action)



<br> 

### 서비스 링크
- API 문서 : https://api.odlab.kr/od-shop
- 옵저버빌리티 : 작성예정

<br>

---

<br>


## <font color="#92d050">주요 기능</font>

### [Account Service](https://github.com/AkkeSun/od-shop/blob/master/account/readme.md)
- 회원가입/로그인
- JWT 토큰 발급 및 검증 (Access Token + Refresh Token)
- 사용자 정보 관리 (CRUD)
- 하이브리드 인증 방식 (JWT + Session)
- Account Agent 서비스를 통한 비동기 이벤트 처리 및 스케쥴링 작업

### [Product Service](https://github.com/AkkeSun/od-shop/tree/master/product)
- 상품 관리 (CRUD)
- 재고 관리
- 상품 예약/확정/취소 (gRPC)
- 분산 락을 통한 동시성 제어
- Product Agent 서비스를 통한 상품이벤트 처리 및 재고 업데이트

### [Order Service](https://github.com/AkkeSun/od-shop/tree/master/order)
- 주문 생성/취소
- 고객 주문 조회
- 판매 상품 조회
- 상품 예약 프로세스

<br>

---

<br>


## <font color="#92d050">기술 스택</font>

### Core
- **Java**: 21
- **Spring Boot**: 3.3.4
- **Gradle**: Multi-module Project

### Database
- **MySQL**: 서비스별 독립 데이터베이스 (DB per Service)
- **MongoDB**: Agent 서비스 이벤트 로그
- **Redis**: 캐시, 세션, 분산 락

### Communication
- **Kafka**: 비동기 이벤트 기반 통신
- **gRPC**: 동기 RPC 통신 (protobuf 4.27.2, grpc 1.65.1)
- **REST API**: 외부 노출 API

### Security
- **Spring Security**: 인증/인가
- **JWT**: 토큰 기반 인증 (jjwt 0.9.1)
- **Jasypt**: 설정 파일 암호화

### Infrastructure
- **Resilience4j**: Circuit Breaker
- **Redisson**: 분산 락
- **ShedLock**: 분산 스케줄링


### Testing
- **JUnit 5**: 테스트 프레임워크
- **Spring REST Docs**: API 문서 자동 생성
- **H2**: 테스트용 인메모리 DB
- **Embedded Redis**: 테스트용 Redis
- **Kafka Test Container**: 테스트용 Kafka

<br>

---

<br>

## <font color="#92d050">아키텍쳐</font>

### 마이크로서비스 구성

```
od-shop/
├── account/           # 사용자 인증/인가 서비스 
├── account-agent/     # 사용자 이벤트 처리 Agent
├── product/           # 상품 관리 서비스 
├── product-agent/     # 상품 이벤트 처리 Agent
├── order/             # 주문 관리 서비스
└── common/            # 공통 모듈
```

<br> 

### 헥사고날 아키텍처

모든 서비스는 [헥사고날 아키텍처](https://velog.io/@akkessun/DDD-%EC%99%80-%ED%97%A5%EC%82%AC%EA%B3%A0%EB%82%A0-%EC%95%84%ED%82%A4%ED%85%8D%EC%B3%90)를 따릅니다.


```
com.{service}/
├── adapter/              # 어댑터 계층
│   ├── in/               # Inbound Adapters (Controller, gRPC, Consumer)
│   └── out/              # Outbound Adapters (Persistence, Client, Producer)
├── application/          # 애플리케이션 계층
│   ├── port/             # 포트 인터페이스
│   │   ├── in/           # UseCase (입력 포트)
│   │   └── out/          # 외부 의존성 (출력 포트)
│   └── service/          # 비즈니스 로직 구현
├── domain/               # 도메인 계층
│   └── model/            # 도메인 모델 (엔티티, VO)
└── infrastructure/       # 인프라 계층
    ├── config/           # 설정
    ├── filter/           # Servlet Filter
    ├── exception/        # 예외 처리
    └── util/             # 유틸리티
```

<br> 


### 서비스 간 통신

#### 동기 통신: [gRPC](https://velog.io/@akkessun/gRPC-vs-REST-API)
- **Product ↔ Order**: 상품 예약, 상품 조회
- **Product ↔ Product-Agent**: 재고 업데이트

#### 비동기 통신: Kafka
- **이벤트 발행**: 주문 생성, 상품 변경, 계정 생성
- **이벤트 처리**: Agent 서비스에서 수신 및 처리

#### REST API
- **Account**: 로그인, 회원가입, 토큰 관리
- **Product**: 상품 조회, 상품 등록
- **Order**: 주문 생성, 주문 조회


<br>

---

<br>

## <font color="#92d050">API 문서</font>


Spring Rest Docs Test 를 통해 openapi3 문서를 생성하고 이를 Swagger UI 로 호스팅하여 API 문서를 배포 합니다.

| 구분 | 링크 |
|-----|-----|
| Swagger UI 프로젝트|  https://github.com/AkkeSun/od-api-doc
| API 문서 | https://api.odlab.kr/od-shop|

<br>

---

<br>

## <font color="#92d050">분산 시스템 패턴</font>

| 패턴                                                                                                                      | 기술           | 설명                                    | 적용 위치                     |
|-------------------------------------------------------------------------------------------------------------------------|---------------|----------------------------------------|------------------------------|
| [Circuit Breaker](https://velog.io/@akkessun/%EC%84%9C%ED%82%B7%EB%B8%8C%EB%A0%88%EC%9D%B4%EC%BB%A4-%ED%8C%A8%ED%84%B4) | Resilience4j  | 외부 서비스 호출 시 장애 전파 방지 및 Fallback 메서드 제공 | REST/gRPC Client             |
| [분산 락](https://velog.io/@akkessun/%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88%EC%99%80-%ED%95%B4%EA%B2%B0)           | Redisson      | 동시성 제어가 필요한 작업에 Redis 기반 분산 락 사용    | Product 서비스 (재고 차감 등)   |
| 분산 스케줄링                                                                                                                 | ShedLock      | MongoDB 기반 분산 락으로 중복 실행 방지              | Agent 서비스                  |
| Saga Pattern                                                                                                            | Kafka         | 분산 트랜잭션을 이벤트 기반으로 처리 및 보상 트랜잭션 구현 | 주문/상품 서비스 간 트랜잭션     |

<br>

---

<br>

## <font color="#92d050">보안</font>

### JWT 인증

- **Access Token**: 10분 유효기간
- **Refresh Token**: 3일 유효기간

### Spring Security

- 경로별 권한 설정
- JWT 필터 체인
- 인증/인가 예외 처리

### Jasypt 암호화

- application.yml의 민감 정보(DB 비밀번호, API 키 등)를 암호화합니다.

<br>

---

<br>

## <font color="#92d050">옵저버빌리티 (SigNoz)</font>

### 개요

모든 마이크로서비스는 **SigNoz**를 통해 통합 옵저버빌리티를 제공합니다.

[SigNoz](https://velog.io/@akkessun/Signoz)는 오픈소스 APM(Application Performance Monitoring) 플랫폼으로, 분산 추적(Distributed Tracing), 메트릭(Metrics), 로그(Logs)를 통합하여 시스템의 성능과 동작을 모니터링합니다.

<br>

### OpenTelemetry 통합

모든 서비스는 **OpenTelemetry Java Agent**를 사용하여 자동으로 텔레메트리 데이터를 수집합니다.

**수집 데이터**:
- **Traces**: HTTP 요청, gRPC 호출, Kafka 메시지, DB 쿼리 등의 분산 추적
- **Metrics**: JVM 메트릭, HTTP 메트릭, 커스텀 비즈니스 메트릭
- **Logs**: 애플리케이션 로그 (JSON 포맷)

<br>

### 주요 기능

#### 1. 분산 추적 (Distributed Tracing)
- 서비스 간 요청 흐름 추적 (REST API, gRPC, Kafka)
- 각 구간별 응답 시간 측정
- 병목 지점 및 성능 이슈 식별

#### 2. 메트릭 모니터링
- **JVM 메트릭**: 힙 메모리, GC, 쓰레드 수
- **HTTP 메트릭**: 요청 수, 응답 시간, 에러율
- **DB 메트릭**: 쿼리 실행 시간, 커넥션 풀 상태
- **Kafka 메트릭**: 메시지 처리 시간, Consumer Lag

#### 3. 로그 분석
- 구조화된 로그 수집 (JSON)
- 트레이스 ID 기반 로그 연결
- 실시간 로그 검색 및 필터링

<br>

---

<br>



## <font color="#92d050">서비스 배포</font>

### GitHub Actions 기반 CI/CD

**워크플로우 구조**

```yaml
Trigger: master 브랜치 Push (특정 모듈 변경 시)
├── Module Check: 변경된 모듈 감지
├── CI (Build & Push)
│   ├── Gradle 빌드 (prod 프로파일, 테스트 제외)
│   ├── Docker 이미지 빌드 (linux/arm64)
│   └── Docker Hub 푸시 (이미지 태그: yyyyMMddHHmm)
└── CD (Deploy)
    ├── Docker Hub에서 이미지 Pull
    ├── 기존 컨테이너 중지 및 제거
    ├── 오래된 이미지 정리
    └── 새 컨테이너 실행
```

**주요 특징**

- **선택적 배포**: 변경된 모듈만 빌드 및 배포
- **병렬 처리**: 각 서비스별 독립적인 CI/CD 파이프라인
- **무중단 배포**: 컨테이너 기반 Blue-Green 방식
- **자동화**: master 브랜치 push 시 자동 배포

<br>

### Docker Hub 이미지

| 서비스              | Docker Hub Repository           | 포트 매핑              |
|--------------------|--------------------------------|----------------------|
| Account API        | akkessun/account-api           | 8081:8080           |
| Product API        | akkessun/product-api           | 8082:8080, 18082:8081 (gRPC) |
| Order API          | akkessun/order-api             | 8083:8080, 18083:8081 (gRPC) |
| Account Agent      | akkessun/account-agent         | 8081:8080           |
| Product Agent      | akkessun/product-agent         | 8082:8080           |

<br >

### 서버 구성
- **PROD-01**: Account, Product, Order API 서비스
- **PROD-02**: Account Agent, Product Agent 서비스

<br >


### Nginx 프록시 설정
- **Reverse Proxy**: 각 마이크로서비스로 요청 라우팅
- **SSL/TLS**: Let's Encrypt 무료 인증서 사용
- **HTTP/2**: 활성화하여 성능 최적화

<br>

---

<br>

## <font color="#92d050">작성 문서</font>
- [어떻게 테스트 할 것인가](https://velog.io/@akkessun/%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%95%A0-%EA%B2%83%EC%9D%B8%EA%B0%80)
- [gRPC vs REST API](https://velog.io/@akkessun/gRPC-vs-REST-API)
- [서킷브레이커 패턴](https://velog.io/@akkessun/%EC%84%9C%ED%82%B7%EB%B8%8C%EB%A0%88%EC%9D%B4%EC%BB%A4-%ED%8C%A8%ED%84%B4)
- [브로커 오류처리 전략](https://velog.io/@akkessun/%EB%B8%8C%EB%A1%9C%EC%BB%A4-%EC%98%A4%EB%A5%98%EC%B2%98%EB%A6%AC-%EC%A0%84%EB%9E%B5)
- [동시성 이슈와 해결방법](https://velog.io/@akkessun/%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88%EC%99%80-%ED%95%B4%EA%B2%B0)
- [어떤 저장소를 선택해야 하는가](https://velog.io/@akkessun/%EC%96%B4%EB%96%A4-%EC%A0%80%EC%9E%A5%EC%86%8C%EB%A5%BC-%EC%84%A0%ED%83%9D%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94%EA%B0%80)
- [어떻게 인증할 것인가](https://velog.io/@akkessun/%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%9D%B8%EC%A6%9D%ED%95%A0-%EA%B2%83%EC%9D%B8%EA%B0%80)
- [데이터 분산처리 전략](https://velog.io/@akkessun/%EB%8D%B0%EC%9D%B4%ED%84%B0-%EB%B6%84%EC%82%B0%EC%B2%98%EB%A6%AC)
- [Signoz 옵저버빌리티](https://velog.io/@akkessun/Signoz)
