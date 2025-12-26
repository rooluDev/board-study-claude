# 게시판 시스템 (Board System)

Servlet과 JSP를 활용한 기본 CRUD 기능을 갖춘 게시판 시스템

## 프로젝트 소개

이 프로젝트는 Spring Framework 없이 순수 Servlet/JSP와 MyBatis를 사용하여 구현한 게시판 시스템입니다.
Model 2 (MVC) 아키텍처를 기반으로 계층 분리, 예외 처리, 보안, 파일 업로드 등 웹 애플리케이션의 핵심 기능을 구현했습니다.

### 주요 특징

- ✅ **순수 Servlet/JSP**: Spring Framework 없이 Jakarta EE 기술 스택 사용
- ✅ **계층 분리 아키텍처**: Servlet → Service → DAO → MyBatis 계층 구조
- ✅ **동적 SQL**: MyBatis 동적 SQL로 검색 기능 구현
- ✅ **파일 관리**: 업로드/다운로드, 검증, 트랜잭션 처리
- ✅ **보안**: SQL Injection 방지, XSS 방지, 비밀번호 해시 저장
- ✅ **이중 검증**: 프론트엔드(JavaScript) + 백엔드(Java) 검증

## 기술 스택

### 백엔드
- **Java**: JDK 17
- **Servlet**: Jakarta Servlet 6.0.0
- **JSP**: Jakarta JSP 3.1.0
- **MyBatis**: 3.5.13
- **Connection Pool**: HikariCP 5.0.1
- **Logging**: SLF4J 2.0.9 + Logback 1.4.11

### 데이터베이스
- **MySQL**: 8.0.31
- **JDBC**: MySQL Connector 8.0.33

### 프론트엔드
- **HTML5**
- **CSS3**
- **JavaScript ES6** (Fetch API)

### 개발 환경
- **WAS**: Apache Tomcat 10.1.50
- **IDE**: IntelliJ IDEA
- **빌드 툴**: Gradle 8.x
- **버전 관리**: Git

## 디렉토리 구조

```
board-model2/
├── src/
│   ├── main/
│   │   ├── java/com/board/
│   │   │   ├── servlet/          # Servlet 클래스 (Controller)
│   │   │   │   ├── BoardListServlet.java
│   │   │   │   ├── BoardViewServlet.java
│   │   │   │   ├── BoardPostServlet.java
│   │   │   │   ├── BoardEditServlet.java
│   │   │   │   ├── BoardDeleteServlet.java
│   │   │   │   ├── CommentServlet.java
│   │   │   │   ├── FileDownloadServlet.java
│   │   │   │   └── AuthServlet.java
│   │   │   ├── service/          # 비즈니스 로직
│   │   │   │   ├── BoardService.java
│   │   │   │   ├── FileService.java
│   │   │   │   └── CommentService.java
│   │   │   ├── dao/              # 데이터 접근 계층
│   │   │   │   ├── BoardDAO.java
│   │   │   │   ├── FileDAO.java
│   │   │   │   └── CommentDAO.java
│   │   │   ├── dto/              # 데이터 전송 객체
│   │   │   │   ├── Board.java
│   │   │   │   ├── BoardFile.java
│   │   │   │   ├── Comment.java
│   │   │   │   └── Category.java
│   │   │   ├── exception/        # 예외 클래스
│   │   │   │   ├── BoardException.java
│   │   │   │   ├── ValidationException.java
│   │   │   │   ├── FileUploadException.java
│   │   │   │   └── AuthenticationException.java
│   │   │   └── util/             # 유틸리티 클래스
│   │   │       ├── MyBatisUtil.java
│   │   │       ├── ValidationUtil.java
│   │   │       ├── FileUtil.java
│   │   │       └── Constants.java
│   │   ├── resources/
│   │   │   ├── mapper/           # MyBatis Mapper XML
│   │   │   │   ├── BoardMapper.xml
│   │   │   │   ├── FileMapper.xml
│   │   │   │   └── CommentMapper.xml
│   │   │   ├── db/               # 데이터베이스 스크립트
│   │   │   │   └── init.sql
│   │   │   ├── mybatis-config.xml
│   │   │   └── logback.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── views/        # JSP 파일
│   │       │   │   ├── list.jsp
│   │       │   │   ├── view.jsp
│   │       │   │   ├── post.jsp
│   │       │   │   ├── edit.jsp
│   │       │   │   └── error.jsp
│   │       │   └── web.xml
│   │       └── uploads/          # 업로드 파일 저장
│   └── test/
│       └── java/                 # 테스트 코드
├── build.gradle
├── README.md
├── CLAUDE.md                     # AI Assistant 협업 가이드
├── convention.md                 # 코딩 컨벤션
├── prd.md                        # 제품 요구사항 명세서
└── TEST_SCENARIOS.md             # 통합 테스트 시나리오
```

## 시작하기

### 1. 사전 준비

- JDK 17 이상 설치
- MySQL 8.0.31 설치
- Apache Tomcat 10.1.50 설치
- Git 설치

### 2. 데이터베이스 설정

#### 2.1 데이터베이스 및 사용자 생성

```sql
-- MySQL에 root로 접속
mysql -u root -p

-- 데이터베이스 생성
CREATE DATABASE board_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여
CREATE USER 'board_user'@'localhost' IDENTIFIED BY 'board_pass';
GRANT ALL PRIVILEGES ON board_db.* TO 'board_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 2.2 테이블 생성

```bash
# 프로젝트 루트에서 실행
mysql -u board_user -p board_db < src/main/resources/db/init.sql
```

또는 MySQL Workbench에서 `src/main/resources/db/init.sql` 파일을 실행합니다.

#### 2.3 데이터베이스 연결 정보 확인

`src/main/resources/mybatis-config.xml`에서 데이터베이스 연결 정보를 확인하고 필요시 수정합니다:

```xml
<property name="driver" value="com.mysql.cj.jdbc.Driver"/>
<property name="url" value="jdbc:mysql://localhost:3306/board_db?useUnicode=true&amp;characterEncoding=UTF-8"/>
<property name="username" value="board_user"/>
<property name="password" value="board_pass"/>
```

### 3. 프로젝트 빌드

```bash
# 프로젝트 클론
git clone <repository-url>
cd board-model2

# 의존성 다운로드 및 빌드
./gradlew clean build

# WAR 파일 생성
./gradlew war
```

빌드가 완료되면 `build/libs/board-model2-1.0-SNAPSHOT.war` 파일이 생성됩니다.

### 4. Tomcat 배포

#### 4.1 WAR 파일 배포

```bash
# WAR 파일을 Tomcat webapps 디렉토리에 복사
cp build/libs/board-model2-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
```

#### 4.2 Tomcat 시작

```bash
# Tomcat 시작
$CATALINA_HOME/bin/startup.sh  # Linux/Mac
$CATALINA_HOME/bin/startup.bat  # Windows
```

### 5. 접속

브라우저에서 다음 URL로 접속:

```
http://localhost:8080/board-model2-1.0-SNAPSHOT/boards
```

또는 WAR 파일명을 `ROOT.war`로 변경하여 루트 경로로 접속:

```
http://localhost:8080/boards
```

## 주요 기능

### 1. 게시글 관리

- **목록 조회**: 페이징 처리 (10개/페이지), 조회수, 첨부파일 개수 표시
- **상세 조회**: 게시글 내용, 댓글 목록, 첨부파일 목록 표시, 조회수 자동 증가
- **등록**: 카테고리, 제목, 내용, 작성자, 비밀번호, 첨부파일(최대 3개) 입력
- **수정**: 비밀번호 확인 후 제목, 내용, 첨부파일 수정
- **삭제**: 비밀번호 확인 후 게시글, 댓글, 첨부파일 일괄 삭제

### 2. 검색 기능

- **카테고리 검색**: Java, Spring, Database
- **날짜 범위 검색**: 등록일시 시작일 ~ 종료일
- **키워드 검색**: 제목, 내용, 작성자 OR 검색
- **복합 검색**: 카테고리 + 날짜 + 키워드 AND 검색
- **검색 조건 유지**: 페이지 이동, 상세 조회, 글쓰기 시 검색 조건 유지

### 3. 파일 관리

- **업로드**: jpg, pdf, png 확장자만 허용, 파일당 2MB 이하, 최대 3개
- **다운로드**: 원본 파일명 유지, 한글 파일명 지원
- **검증**: 확장자, 크기, 개수 검증 (프론트엔드 + 백엔드)
- **트랜잭션**: 게시글 등록/수정/삭제 시 파일도 함께 처리, 실패 시 롤백

### 4. 댓글 기능

- **등록**: AJAX 방식, 1~300자 입력, 실시간 글자 수 표시
- **목록 조회**: 등록일시 표시
- **삭제**: 게시글 삭제 시 댓글도 함께 삭제

### 5. 인증 및 보안

- **비밀번호 확인**: 수정/삭제 전 비밀번호 확인 (AJAX 모달)
- **SHA-256 해시**: 비밀번호를 SHA-256으로 해시하여 DB 저장
- **SQL Injection 방지**: MyBatis 파라미터 바인딩 (`#{}`) 사용
- **XSS 방지**: 출력 시 이스케이프 처리

## 아키텍처

### MVC Pattern (Model 2)

```
┌──────────────┐
│   Browser    │
└──────┬───────┘
       │ HTTP Request
       ▼
┌──────────────┐
│   Servlet    │ ← Controller (요청 처리, 응답)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Service    │ ← Business Logic (비즈니스 로직, 트랜잭션)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│     DAO      │ ← Data Access (데이터 조회/저장)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   MyBatis    │ ← SQL Mapper
└──────┬───────┘
       │
       ▼
┌──────────────┐
│    MySQL     │ ← Database
└──────────────┘
```

### 계층별 책임

| 계층 | 클래스 | 책임 |
|-----|--------|------|
| **Presentation** | Servlet, JSP | HTTP 요청/응답 처리, 파라미터 추출, View 선택 |
| **Business** | Service | 비즈니스 로직, 트랜잭션 관리, 입력값 검증 |
| **Persistence** | DAO | 데이터 조회/저장, MyBatis 호출 |
| **Database** | MyBatis Mapper | SQL 쿼리 정의, 동적 SQL |

## 데이터베이스 설계

### ERD

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│  category   │       │    board    │       │    file     │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ category_id │◄──┐   │  board_id   │◄──┐   │   file_id   │
│ category_name│   └───│ category_id │   └───│  board_id   │
└─────────────┘       │    title    │       │ original_name│
                      │   content   │       │physical_name│
                      │  user_name  │       │  file_path  │
                      │  password   │       │  extension  │
                      │    views    │       │    size     │
                      │ created_at  │       │ created_at  │
                      │  edited_at  │       └─────────────┘
                      └──────┬──────┘
                             │
                             │
                      ┌──────▼──────┐
                      │   comment   │
                      ├─────────────┤
                      │ comment_id  │
                      │  board_id   │
                      │   comment   │
                      │ created_at  │
                      └─────────────┘
```

### 테이블 설명

- **category**: 게시글 카테고리 (Java, Spring, Database)
- **board**: 게시글 정보 (제목, 내용, 작성자, 비밀번호, 조회수)
- **file**: 첨부파일 정보 (원본명, 물리명, 경로, 확장자, 크기)
- **comment**: 댓글 정보 (내용, 작성일시)

## 보안

### SQL Injection 방지

- MyBatis 파라미터 바인딩 (`#{}`) 사용
- 동적 SQL에서도 `${}` 사용 금지

```xml
<!-- ✅ 안전 -->
<select id="selectBoardById">
  SELECT * FROM board WHERE board_id = #{boardId}
</select>

<!-- ❌ 위험 -->
<select id="selectBoardById">
  SELECT * FROM board WHERE board_id = ${boardId}
</select>
```

### XSS 방지

- JSP 출력 시 `<c:out>` 태그 사용 또는 수동 이스케이프
- JSON 응답에서 `escapeJson()` 메서드 사용

```jsp
<!-- ✅ 안전 -->
<c:out value="${board.title}" />

<!-- ❌ 위험 -->
<%= board.getTitle() %>
```

### 비밀번호 보안

- 평문 저장 금지
- MySQL `SHA2(password, 256)` 함수로 해시 저장
- 비교 시에도 해시값 비교

```sql
-- 저장
INSERT INTO board (password, ...) VALUES (SHA2('password123', 256), ...)

-- 비교
SELECT COUNT(*) FROM board
WHERE board_id = ? AND password = SHA2('password123', 256)
```

### 파일 업로드 보안

- 확장자 화이트리스트 검증 (jpg, pdf, png)
- 파일 크기 제한 (2MB)
- UUID 파일명 사용 (덮어쓰기 방지)
- 업로드 디렉토리 실행 권한 제거

## 테스트

### 통합 테스트

자세한 테스트 시나리오는 `TEST_SCENARIOS.md` 파일을 참조하세요.

**주요 시나리오**:
1. 게시글 등록 → 조회 → 댓글 작성 → 수정 → 삭제
2. 파일 업로드 → 다운로드
3. 검색 → 페이징

### 단위 테스트

```bash
./gradlew test
```

## 개선 사항 및 알려진 이슈

### 현재 상태

- ✅ 기본 CRUD 기능 완성
- ✅ 파일 업로드/다운로드 기능 완성
- ✅ 검색 기능 완성
- ✅ 동적 SQL 구현 완성
- ✅ 예외 처리 구조 완성
- ⚠️ 로깅 기능 부분 미적용 (logback.xml 작성 완료)

### 개선 필요 사항

1. **로깅 추가**: 모든 Servlet과 Service에 Logger 추가 필요
    - System.out.println, e.printStackTrace() → logger.info/error 교체
    - 주요 작업(등록/수정/삭제) 시작/완료/실패 로깅

2. **JSON 유틸리티 분리**: JSON 파싱 메서드 중복 코드 제거
    - JsonUtil 클래스로 추출하여 재사용

3. **트랜잭션 관리 개선**: Connection 기반 명시적 트랜잭션 관리

4. **JSON 라이브러리 도입**: Gson 또는 Jackson 사용 고려

### 알려진 제한사항

- 사용자 인증 기능 없음 (비밀번호만으로 수정/삭제 제어)
- 댓글 수정/삭제 기능 없음
- 대댓글 기능 없음
- 검색 조건 저장 기능 없음 (쿠키/세션)

## 참고 문서

- [CLAUDE.md](CLAUDE.md): AI Assistant 협업 가이드
- [convention.md](convention.md): 코딩 컨벤션 (Google Java Style Guide 기반)
- [prd.md](prd.md): 제품 요구사항 명세서

## 라이선스

이 프로젝트는 교육 목적으로 작성되었습니다.

## 문의

프로젝트 관련 문의사항이나 버그 리포트는 이슈를 등록해주세요.
