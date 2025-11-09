# MicroLotto

---

Lotto-MSA는 로또 구매부터 당첨 결과 조회까지의 전 과정을
Microservice Architecture (MSA) 기반으로 분리하여 구현한 애플리케이션입니다.

각 서비스는 독립적으로 배포되어, 서비스 간 통신을 통해 작동합니다.
각 서비스가 독립적으로 작동하기 때문에 확장성 및 안정성을 확보할 수 있습니다.

## 목차

- [서비스 구성](#서비스-구성)
- [서비스 상세](#서비스-상세)
- [빠른 시작](#빠른-시작)

## 서비스 구성

---

### 마이크로서비스

| 서비스 | 포트 | 설명 | DB 포트 |
|--------|------|------|-------|
| **lotto-purchase-service** | 7001 | 로또 구매 및 구매 내역 관리 | 3316  |
| **lotto-draw-service** | 7002 | 회차 관리 및 당첨 번호 등록 | 3317  |
| **lotto-result-service** | 7003 | 당첨 결과 및 통계 계산 | 3318  |

## 서비스 상세

---

### 1. lotto-purchase-service (로또 구매 서비스)

사용자가 로또를 구매하고 구매 내역을 조회하는 기능 담당한다.

#### 주요 기능

- **로또 구매**
  - 현재 진행 중인 회차 정보를 draw-service로부터 조회
  - 1~45 사이의 랜덤 숫자 6개로 구성된 로또 자동 생성
  - 회차 번호, 구매 시간과 함께 저장
  
- **구매 내역 조회**
  - 특정 회차별 구매 내역 조회
  - 전체 구매 내역 조회


---

### 2. lotto-draw-service (회차 및 당첨 번호 서비스)

로또 회차를 생성하고, 해당 회차의 당첨 번호를 관리한다.

#### 주요 기능

- **회차 생성**
  - 신규 회차 번호 자동 생성
  - 회차 시작일/종료일 및 상태 관리 (OPEN, CLOSED)

- **현재 회차 조회**
  - 구매 가능한 최신 회차 번호 제공

- **당첨 번호 등록**
  - 6개의 당첨 번호 + 보너스 번호 등록
  - 등록 시 해당 회차를 종료 처리 (isClosed = true)
  - result-service에 결과 계산 요청 가능

- **예외**
  - 현재 진행중인 회차가 존재하지 않는 경우
---

### 3. lotto-result-service (당첨 결과 및 통계 서비스)

구매 내역과 당첨 번호를 비교하여 당첨 결과와 통계 계산한다.

#### 주요 기능

- **당첨 결과 계산**
  - 특정 회차(drawNo)의 모든 구매 내역을 조회
  - 당첨 번호와 비교 후 등수 및 상금 계산
  - 결과를 DB에 저장 또는 응답으로 반환

- **등수별 상금 기준**
  - 1등: 6개 일치 → 2,000,000,000원
  - 2등: 5개 + 보너스 일치 → 30,000,000원
  - 3등: 5개 일치 → 1,500,000원
  - 4등: 4개 일치 → 50,000원
  - 5등: 3개 일치 → 5,000원

- **통계 조회**
  - 회차별 당첨 결과 요약 조회
  - 누적 당첨 횟수 및 총 당첨 금액 계산

  

## 빠른 시작

---

### 사전 요구사항

- Docker Desktop 설치
- Docker Compose v2.0 이상

### 실행 방법

```bash
# 1. 프로젝트 클론
git clone <repository-url>
cd lotto-msa

# 2. 전체 서비스 실행 (DB 포함)
docker-compose up -d --build

# 3. 서비스 상태 확인
docker-compose ps

# 4. 로그 확인
docker-compose logs -f
```

### 헬스체크

각 서비스가 정상적으로 실행되었는지 확인

```bash
curl http://localhost:7001/actuator/health  # Purchase Service
curl http://localhost:7002/actuator/health  # Draw Service
curl http://localhost:7003/actuator/health  # Result Service
```

### 종료

```bash
# 서비스 중지 (데이터 유지)
docker-compose stop

# 컨테이너 삭제 (데이터 유지)
docker-compose down

# 데이터까지 모두 삭제
docker-compose down -v
```
