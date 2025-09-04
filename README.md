# 미니 그룹웨어 Full-Stack 프로젝트

## 📖 프로젝트 소개

Full-Stack 개발자로서의 역량을 종합적으로 증명하기 위해 기획하고 개발한 개인 프로젝트입니다.

기업의 필수 협업 도구인 그룹웨어의 핵심 기능, **전자결재**와 **업무관리**를 Spring Boot와 React를 기반으로 구현했습니다. 이 프로젝트를 통해 백엔드의 안정적인 API 설계 및 구축 능력, 그리고 React를 이용한 동적인 프론트엔드 개발 및 상태 관리 능력을 보여주고자 했습니다.

---

## 배운 방식 VS 새로운 방식

* **배운 방식 (MyBatis 기반, Database-First)**
   *  DB 테이블 생성 (in SQL Developer): 데이터 구조를 먼저 정의한다 논리ERD / 물리ERD. 데이터베이스가 중심.
   *  VO(Value Object) 클래스 작성: DB 테이블의 컬럼과 1:1로 대응되는 Java 클래스를 만든다. 이 클래스는 순수하게 데이터를 담는 역할만 한다.
   *  Mapper(XML/Interface) 작성: 실행할 SQL 쿼리문을 SELECT * FROM ... 처럼 개발자가 직접 작성한다.
   *  Service, Controller 개발: 비즈니스 로직을 처리한다.

   *  이 방식은 개발자가 SQL을 완벽하게 제어할 수 있다는 큰 장점이 있다.

* **새로운 방식 (JPA 기반, Code-First)**
   * Entity 클래스 작성 (in Java): Java 객체 모델을 먼저 설계한다. Java 코드가 중심!
   * (JPA가 알아서) DB 테이블 생성: @Entity 클래스의 정보를 바탕으로 JPA가 CREATE TABLE 구문을 자동으로 생성하여 실행한다.
   * Repository(Interface) 작성: 기본적인 CRUD SQL은 JPA가 자동으로 생성해 주기 때문에. 개발자는 메소드 이름만 규칙에 맞게 만들면 됩니다.
   * Service, Controller 개발: 비즈니스 로직을 처리한다.

결론적으로 MVC 패턴(Controller-Service) 구조와 거의 동일하다.
단지, 데이터에 접근하고 처리하는 계층(Mapper vs Repository)만 최신 기술 트렌드 중 하나인 JPA로 바꾼 것이다.

---

## ✨ 주요 기능

* **사용자 인증/인가 (Spring Security)**
    * BCrypt를 이용한 비밀번호 암호화 및 안전한 회원가입
    * 세션 및 쿠키 기반의 로그인/로그아웃 기능
    * 인증된 사용자만 접근 가능한 보호된 라우트(Protected Route) 구현

* **전자결재 시스템**
    * 새 결재 문서 작성 및 결재자 지정 기능
    * 문서 목록 조회 (내가 올린 문서 / 내가 결재할 문서)
    * 문서 상세 조회
    * 사용자 권한 및 문서 상태에 따른 버튼(상신/승인/반려) 조건부 렌더링
    * 문서 상태 변경(DRAFT → PENDING → APPROVED/REJECTED) 워크플로우 구현

* **업무관리 시스템**
    * 칸반(Kanban) 보드 스타일의 직관적인 UI
    * 업무 목록 상태별(TODO, IN_PROGRESS, DONE) 실시간 필터링
    * 드롭다운 메뉴를 통한 간편한 업무 상태 변경 기능

---

## 🏗️ 시스템 아키텍처



```
  Client (React @ Port 3000)
           |
           | (HTTP/HTTPS Requests)
           |
  Proxy Server (React Dev Server)  <-- CORS 및 쿠키 문제 해결
           |
           | (Forwarded API Calls)
           |
  API Server (Spring Boot @ Port 8080)
           |
           | (JPA/Hibernate)
           |
  Database (Oracle DB in Docker)
```

---

## 🛠️ 기술 스택

| 구분       | 기술                                                          |
| ---------- | ------------------------------------------------------------- |
| **Backend** | Java, Spring Boot, Spring Security, Spring Data JPA, Lombok   |
| **Frontend** | React, JavaScript(ES6+), Axios, React Router DOM, React Context API |
| **Database** | Oracle DB (Docker)                                            |
| **Tools** | Git, GitHub, STS4, VS Code, Postman, Oracle SQL Developer     |

---

## 겪었던 문제 및 해결 과정 (Troubleshooting)

이 프로젝트를 진행하며 마주쳤던 주요 기술적 문제들과 해결 과정을 통해 얻은 교훈을 정리했습니다.

### 1. CORS 정책 및 `SameSite` 쿠키 문제로 인한 인증 실패

* **문제 현상:**
    * 프론트엔드(`localhost:3000`)에서 로그인 성공 후, 다른 API 요청 시 `401 Unauthorized` 에러가 발생했습니다. `withCredentials: true` 옵션을 추가했음에도 문제가 해결되지 않았습니다.
* **원인 분석:**
    * 최신 브라우저의 `SameSite=Lax` 기본 쿠키 정책 때문이었습니다. 다른 출처(3000 -> 8080)로 요청 시, 브라우저가 보안상의 이유로 인증 쿠키(`JSESSIONID`) 전송을 차단하고 있었습니다. 백엔드와 프론트엔드 양쪽 코드 자체는 문제가 없었지만, 브라우저의 보안 정책으로 인해 통신이 막히는 상황이었습니다.
* **해결 과정:**
    * 백엔드 서버의 `CookieSerializer`를 수정하여 `SameSite=None; Secure` 설정을 시도했으나, 이는 HTTPS 환경에서만 유효하여 로컬 HTTP 환경에서는 적용할 수 없었습니다.
    * 최종적으로 React 개발 서버의 **Proxy 기능**을 설정하여 이 문제를 해결했습니다. 프론트엔드에서 `/api`로 시작하는 모든 요청을 백엔드(`localhost:8080`)로 대신 전달하도록 설정했습니다. 이를 통해 브라우저 입장에서는 모든 요청이 동일 출처(`localhost:3000`)에서 나가는 것처럼 보여 쿠키 전송이 정상적으로 이루어졌습니다.
    * 이 과정을 통해 최신 웹 보안 정책에 대한 깊은 이해와 개발 환경에서의 표준적인 CORS 해결책을 체득했습니다.

### 2. Oracle DB Sequence 불일치로 인한 PK 중복 에러

* **문제 현상:**
    * 결재 문서 생성 시 `ORA-00001: 고유한 제약 조건이 위반되었습니다` 라는 DB 에러가 발생하며 `500 Internal Server Error`가 발생했습니다.
* **원인 분석:**
    * JPA의 ID 자동 생성 전략으로 `GenerationType.SEQUENCE`를 사용하고 있었습니다. 테스트 중 데이터를 수동으로 추가/삭제하는 과정에서, DB의 Sequence 객체가 다음으로 발급해야 할 숫자와 테이블에 이미 존재하는 `DOC_ID`가 충돌을 일으킨 것이 원인이었습니다.
* **해결 과정:**
    * 먼저 `SELECT MAX(DOC_ID) FROM APPROVAL_DOC;` 쿼리로 현재 테이블에 있는 가장 큰 ID 값을 확인했습니다.
    * `DROP SEQUENCE APPROVAL_DOC_SEQ;`로 기존 시퀀스를 삭제하고, `CREATE SEQUENCE APPROVAL_DOC_SEQ START WITH [가장 큰 ID + 1] INCREMENT BY 1;` 쿼리로 시퀀스를 재설정하여 문제를 해결했습니다.
    * ID 자동 생성 전략의 내부 동작 원리와 데이터 정합성의 중요성을 이해하는 계기가 되었습니다.

### 3. JPA Entity 필드 값 누락 문제

* **문제 현상:**
    * 결재 문서 생성 시 `content` 필드에 내용을 입력했음에도, DB에는 `null`로 저장되었습니다.
* **원인 분석:**
    * `System.out.println`을 통해 Service 계층까지는 `content` 값이 정상적으로 전달되는 것을 확인했습니다. 이를 통해 문제는 JPA가 DB에 `INSERT` 쿼리를 실행하는 마지막 단계에 있다고 특정할 수 있었습니다.
    * 최종 원인은 `@Builder`와 `@NoArgsConstructor`를 함께 사용할 때 Lombok과 JPA 사이에 드물게 발생하는 모호함 때문이었습니다.
* **해결 과정:**
    * `ApprovalDoc` Entity 클래스에 `@AllArgsConstructor`를 추가하여 모든 필드를 갖는 생성자를 명시적으로 제공했습니다. 이를 통해 JPA가 객체를 생성하고 필드를 매핑하는 과정의 모호함을 제거하여 문제를 해결했습니다.
    * 단순한 코드 오타가 아닌, 라이브러리 간의 상호작용으로 인해 발생할 수 있는 미묘한 문제에 대한 디버깅 경험을 쌓았습니다.
