# 게시판 프로젝트 개발 가이드

## 프로젝트 개요

이 프로젝트는 Servlet/JSP를 활용한 기본적인 CRUD 게시판 시스템입니다.
**Spring Framework를 사용하지 않고** 순수 Servlet/JSP로 구현합니다.

## 기술 스택

- **Backend**: Java 17, Servlet 6.0.0, JSP 3.1.0, MyBatis 3.5.13
- **Database**: MySQL 8.0.31, JDBC 8.0.33
- **Frontend**: HTML5, CSS3, JavaScript (ES6)
- **WAS**: Apache Tomcat 10.1.50
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA

## 프로젝트 구조

```
board-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── board/
│   │   │           ├── servlet/        # Servlet 계층
│   │   │           ├── service/        # 비즈니스 로직 계층
│   │   │           ├── dao/            # 데이터 액세스 계층
│   │   │           ├── dto/            # 데이터 전송 객체
│   │   │           ├── exception/      # 커스텀 예외
│   │   │           └── util/           # 유틸리티 클래스
│   │   ├── resources/
│   │   │   ├── mybatis-config.xml      # MyBatis 설정
│   │   │   ├── mapper/                 # MyBatis Mapper XML
│   │   │   └── db/
│   │   │       └── schema.sql          # DDL 스크립트
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml             # 웹 애플리케이션 설정
│   │       │   └── views/              # JSP 파일
│   │       │       ├── list.jsp
│   │       │       ├── view.jsp
│   │       │       ├── post.jsp
│   │       │       └── edit.jsp
│   │       ├── css/                    # CSS 파일
│   │       ├── js/                     # JavaScript 파일
│   │       └── uploads/                # 업로드 파일 저장
│   └── test/
│       └── java/                       # 테스트 코드
├── build.gradle
├── settings.gradle
├── prd.md                              # 요구사항 문서 (필독)
├── convention.md                       # 코딩 컨벤션 (필독)
└── CLAUDE.md                           # 본 파일
```

## 개발 단계

### Phase 1: 프로젝트 초기 설정
1. Gradle 프로젝트 생성 및 의존성 설정
2. 디렉토리 구조 생성
3. 데이터베이스 스키마 생성 (DDL)
4. MyBatis 설정 및 연결 테스트

### Phase 2: 기본 인프라 구현
1. 공통 예외 클래스 작성
2. 유틸리티 클래스 작성 (파일 처리, 유효성 검증 등)
3. DTO 클래스 작성 (Board, File, Comment, Category)
4. MyBatis SqlSessionFactory 설정

### Phase 3: 게시글 목록 기능
1. BoardListServlet (GET /boards)
2. BoardService.getBoardList()
3. BoardDAO.selectBoardList()
4. BoardMapper.xml (목록 조회 쿼리)
5. list.jsp (목록 화면)

### Phase 4: 게시글 조회 기능
1. BoardViewServlet (GET /board/view)
2. BoardService.getBoardById()
3. BoardDAO.selectBoardById(), updateViewCount()
4. BoardMapper.xml (상세 조회, 조회수 증가 쿼리)
5. view.jsp (상세 화면)

### Phase 5: 게시글 등록 기능
1. BoardPostServlet (GET/POST /board/post)
2. BoardService.createBoard()
3. BoardDAO.insertBoard()
4. FileService.uploadFiles()
5. BoardMapper.xml, FileMapper.xml
6. post.jsp (등록 화면)
7. 입력값 검증 (프론트/백엔드)

### Phase 6: 게시글 수정 기능
1. BoardEditServlet (GET/POST /board/edit)
2. AuthServlet (POST /auth/confirm) - 비밀번호 확인
3. BoardService.updateBoard()
4. BoardDAO.updateBoard()
5. FileService.updateFiles()
6. BoardMapper.xml (수정 쿼리)
7. edit.jsp (수정 화면)

### Phase 7: 게시글 삭제 기능
1. BoardDeleteServlet (POST /board/delete)
2. BoardService.deleteBoard()
3. BoardDAO.deleteBoard()
4. FileService.deleteFiles()
5. CommentDAO.deleteCommentsByBoardId()
6. 트랜잭션 처리

### Phase 8: 댓글 기능
1. CommentServlet (POST /comment)
2. CommentService.createComment()
3. CommentDAO.insertComment()
4. CommentMapper.xml
5. AJAX 처리

### Phase 9: 파일 다운로드
1. FileDownloadServlet (GET /download)
2. FileService.downloadFile()
3. FileDAO.selectFileById()

### Phase 10: 검색 기능
1. list.jsp에 검색 폼 추가
2. BoardDAO.selectBoardList() 동적 쿼리 추가
3. 검색 조건 유지 로직

## 중요 구현 지침

### 1. 계층 분리 원칙
- **Servlet**: HTTP 요청/응답 처리, JSP 포워딩/리다이렉트
- **Service**: 비즈니스 로직, 트랜잭션 관리
- **DAO**: 데이터베이스 CRUD 작업
- **Mapper**: SQL 쿼리 정의

❌ **안티패턴**: Servlet에서 직접 DAO 호출 또는 비즈니스 로직 작성

### 2. 비밀번호 보안
```java
// MySQL SHA2() 함수 사용
// Mapper XML에서:
// INSERT INTO board (password) VALUES (SHA2(#{password}, 256))
// SELECT * FROM board WHERE board_id = #{boardId} AND password = SHA2(#{password}, 256)
```

### 3. 파일 업로드
- 파일명: UUID로 변환하여 저장
- 원본 파일명: DB에 별도 저장
- 허용 확장자: jpg, pdf, png
- 최대 크기: 2MB
- 최대 개수: 3개

### 4. 페이징
- DB 레벨에서 LIMIT, OFFSET 사용
- 메모리에서 전체 데이터 로드 후 페이징 ❌

### 5. 트랜잭션
- 게시글 삭제 시: 댓글 → 파일(DB) → 파일(물리) → 게시글 순서로 삭제
- 실패 시 롤백
- CASCADE 사용 안 함 (명시적 삭제)

### 6. 검색 조건 유지
- 모든 링크와 폼에 검색 파라미터 포함
- GET 파라미터: page, category, from, to, keyword

### 7. 입력값 검증
- 프론트엔드: 사용자 편의성 (즉시 피드백)
- 백엔드: 보안 (필수 검증)
- 검증 실패 시: 오류 메시지 + 기존 입력값 유지

### 8. 예외 처리
```java
try {
    // 비즈니스 로직
} catch (CustomException e) {
    // 사용자 친화적 오류 메시지
    request.setAttribute("errorMessage", e.getMessage());
    request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
} catch (Exception e) {
    // 시스템 오류 로깅
    logger.error("Unexpected error", e);
    // 일반적인 오류 메시지
    request.setAttribute("errorMessage", "시스템 오류가 발생했습니다.");
    request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
}
```

## MyBatis 설정 가이드

### mybatis-config.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
    <setting name="jdbcTypeForNull" value="NULL"/>
  </settings>
  
  <typeAliases>
    <package name="com.board.dto"/>
  </typeAliases>
  
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/board_system"/>
        <property name="username" value="root"/>
        <property name="password" value="asd@1252370"/>
      </dataSource>
    </environment>
  </environments>
  
  <mappers>
    <mapper resource="mapper/BoardMapper.xml"/>
    <mapper resource="mapper/FileMapper.xml"/>
    <mapper resource="mapper/CommentMapper.xml"/>
    <mapper resource="mapper/CategoryMapper.xml"/>
  </mappers>
</configuration>
```

## SQL Injection 방지

### ✅ 올바른 방법 (파라미터 바인딩)
```xml
<select id="selectBoardById" resultType="Board">
  SELECT * FROM board WHERE board_id = #{boardId}
</select>
```

### ❌ 잘못된 방법 (문자열 결합)
```xml
<select id="selectBoardById" resultType="Board">
  SELECT * FROM board WHERE board_id = ${boardId}
</select>
```

**예외**: 동적 ORDER BY 절에서만 ${} 사용 가능 (단, 화이트리스트 검증 필수)

## 테스트 가이드

각 기능 구현 후 다음 사항을 테스트:

1. **정상 케이스**: 올바른 입력으로 기능이 정상 동작하는지
2. **유효성 검증**: 잘못된 입력에 대해 적절한 오류 메시지 표시
3. **경계값**: 최소/최대 길이, 파일 크기 등
4. **보안**: SQL Injection, 파일 업로드 취약점 등
5. **예외 상황**: DB 연결 실패, 파일 I/O 오류 등

## 디버깅 팁

1. **MyBatis 로그 활성화**
   ```xml
   <settings>
     <setting name="logImpl" value="STDOUT_LOGGING"/>
   </settings>
   ```

2. **Tomcat 로그 확인**
    - `catalina.out` 파일에서 에러 확인

3. **브라우저 개발자 도구**
    - Network 탭에서 요청/응답 확인
    - Console 탭에서 JavaScript 오류 확인

## 참고 문서

- **prd.md**: 모든 요구사항과 API 명세 (필독)
- **convention.md**: 코딩 컨벤션 및 네이밍 규칙
- [MyBatis 공식 문서](https://mybatis.org/mybatis-3/)
- [Servlet 6.0 Javadoc](https://jakarta.ee/specifications/servlet/6.0/apidocs/)

## 주의사항

1. **Spring 사용 금지**: @Controller, @Service, @Autowired 등 사용 불가
2. **JSTL 사용 금지**: JSP에서 Java 코드 직접 사용
3. **JPA/Hibernate 사용 금지**: MyBatis만 사용
4. **CASCADE 사용 금지**: 삭제는 코드에서 명시적으로 처리

## 완료 체크리스트

각 단계 완료 후 체크:

- [ ] Phase 1: 프로젝트 초기 설정
- [ ] Phase 2: 기본 인프라
- [ ] Phase 3: 게시글 목록
- [ ] Phase 4: 게시글 조회
- [ ] Phase 5: 게시글 등록
- [ ] Phase 6: 게시글 수정
- [ ] Phase 7: 게시글 삭제
- [ ] Phase 8: 댓글 기능
- [ ] Phase 9: 파일 다운로드
- [ ] Phase 10: 검색 기능
- [ ] 통합 테스트
- [ ] 코드 리뷰 및 리팩토링