# CLAUDE.md

> 이 문서는 Claude AI가 본 프로젝트에서 효과적으로 작업할 수 있도록 프로젝트의 구조, 규칙, 컨텍스트를 제공합니다.

## 프로젝트 개요

### 기본 정보
- **프로젝트명**: 게시판 시스템 (Board System)
- **기술 스택**: Java 17, Servlet + JSP, MySQL 8.0.31, MyBatis, Tomcat 10.1.50
- **아키텍처**: MVC 패턴 (Servlet-Service-DAO)
- **목적**: Servlet과 JSP를 사용한 전통적인 웹 애플리케이션 게시판 CRUD 구현

### 핵심 문서
프로젝트 작업 전 반드시 다음 문서들을 참조하세요:
- **`prd.md`**: 제품 요구사항 명세서 - 모든 기능 요구사항과 API 명세
- **`convention.md`**: Java 코딩 컨벤션 - Google Java Style Guide 기반 코딩 규칙
- **`CLAUDE.md`**: 본 문서 - Claude를 위한 작업 가이드

## 프로젝트 구조

```
board-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/board/
│   │   │       ├── controller/          # Servlet 클래스
│   │   │       │   ├── BoardListServlet.java
│   │   │       │   ├── BoardViewServlet.java
│   │   │       │   ├── BoardPostServlet.java
│   │   │       │   ├── BoardEditServlet.java
│   │   │       │   ├── BoardDeleteServlet.java
│   │   │       │   ├── CommentServlet.java
│   │   │       │   ├── FileDownloadServlet.java
│   │   │       │   └── AuthConfirmServlet.java
│   │   │       ├── service/             # 비즈니스 로직
│   │   │       │   ├── BoardService.java
│   │   │       │   ├── CommentService.java
│   │   │       │   ├── FileService.java
│   │   │       │   └── CategoryService.java
│   │   │       ├── dao/                 # 데이터 접근 계층
│   │   │       │   ├── BoardDAO.java
│   │   │       │   ├── CommentDAO.java
│   │   │       │   ├── FileDAO.java
│   │   │       │   └── CategoryDAO.java
│   │   │       ├── dto/                 # 데이터 전송 객체
│   │   │       │   ├── BoardDTO.java
│   │   │       │   ├── CommentDTO.java
│   │   │       │   ├── FileDTO.java
│   │   │       │   ├── CategoryDTO.java
│   │   │       │   └── SearchCondition.java
│   │   │       ├── util/                # 유틸리티
│   │   │       │   ├── ValidationUtil.java
│   │   │       │   ├── DateUtil.java
│   │   │       │   ├── FileUtil.java
│   │   │       │   └── PasswordUtil.java
│   │   │       ├── exception/           # 사용자 정의 예외
│   │   │       │   ├── BoardNotFoundException.java
│   │   │       │   ├── ValidationException.java
│   │   │       │   ├── PasswordMismatchException.java
│   │   │       │   └── FileUploadException.java
│   │   │       └── constants/           # 상수 정의
│   │   │           └── BoardConstants.java
│   │   ├── resources/
│   │   │   ├── mybatis/
│   │   │   │   ├── mybatis-config.xml   # MyBatis 설정
│   │   │   │   └── mapper/              # SQL Mapper
│   │   │   │       ├── BoardMapper.xml
│   │   │   │       ├── CommentMapper.xml
│   │   │   │       ├── FileMapper.xml
│   │   │   │       └── CategoryMapper.xml
│   │   │   ├── database/
│   │   │   │   └── schema.sql           # DB 스키마
│   │   │   └── logback.xml              # 로깅 설정
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml              # 웹 애플리케이션 설정
│   │       │   └── views/               # JSP 파일
│   │       │       ├── board/
│   │       │       │   ├── list.jsp
│   │       │       │   ├── view.jsp
│   │       │       │   ├── post.jsp
│   │       │       │   └── edit.jsp
│   │       │       ├── common/
│   │       │       │   ├── header.jsp
│   │       │       │   └── footer.jsp
│   │       │       └── error/
│   │       │           ├── 404.jsp
│   │       │           └── 500.jsp
│   │       ├── resources/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   ├── js/
│   │       │   │   ├── board.js
│   │       │   │   └── validation.js
│   │       │   └── images/
│   │       └── uploads/                 # 업로드 파일 저장
│   └── test/
│       └── java/
│           └── com/example/board/
│               ├── service/
│               └── dao/
├── pom.xml                              # Maven 의존성
└── README.md
```

## Claude 작업 가이드

### 1. 작업 시작 전 체크리스트

작업을 시작하기 전에 반드시 확인하세요:

- [ ] `prd.md`를 읽고 요구사항을 정확히 이해했는가?
- [ ] `convention.md`를 읽고 코딩 스타일을 숙지했는가?
- [ ] 작업할 기능이 어느 계층(Controller/Service/DAO)에 속하는가?
- [ ] 관련된 DTO와 예외 클래스가 필요한가?

### 2. 코드 작성 원칙

#### 2.1 계층별 책임

**Controller (Servlet)**
```java
// ✅ 해야 할 일
- HTTP 요청 파라미터 추출
- Service 계층 호출
- 결과를 JSP로 전달 또는 리다이렉트
- 예외를 HTTP 에러로 변환

// ❌ 하지 말아야 할 일
- 비즈니스 로직 작성
- 직접 DAO 호출
- SQL 작성
```

**Service**
```java
// ✅ 해야 할 일
- 비즈니스 로직 구현
- 트랜잭션 관리
- DAO 계층 조합
- 비즈니스 예외 발생

// ❌ 하지 말아야 할 일
- HTTP 관련 처리 (request, response)
- JSP forward/redirect
- SQL 작성
```

**DAO**
```java
// ✅ 해야 할 일
- MyBatis 호출
- 데이터베이스 CRUD 수행
- ResultSet을 DTO로 변환 (MyBatis가 자동으로 수행)

// ❌ 하지 말아야 할 일
- 비즈니스 로직
- HTTP 처리
- 트랜잭션 관리 (Service에서 수행)
```

#### 2.2 명명 규칙 준수

```java
// Servlet: ~Servlet
public class BoardListServlet extends HttpServlet { }

// Service: ~Service  
public class BoardService { }

// DAO: ~DAO
public class BoardDAO { }

// DTO: ~DTO 또는 없음
public class BoardDTO { }

// 메서드: 동사 + 명사 (lowerCamelCase)
public List<BoardDTO> getBoardList() { }
public void createBoard(BoardDTO board) { }
public void updateBoard(BoardDTO board) { }
public void deleteBoard(int boardId) { }

// 상수: UPPER_SNAKE_CASE
private static final int MAX_FILE_SIZE = 2097152; // 2MB
```

#### 2.3 예외 처리 패턴

```java
// Service 계층
public BoardDTO getBoard(int boardId) throws BoardNotFoundException {
    BoardDTO board = boardDAO.selectById(boardId);
    if (board == null) {
        throw new BoardNotFoundException("게시글을 찾을 수 없습니다. ID: " + boardId);
    }
    return board;
}

// Controller 계층
try {
    BoardDTO board = boardService.getBoard(boardId);
    request.setAttribute("board", board);
    request.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
           .forward(request, response);
} catch (BoardNotFoundException e) {
    logger.error("게시글 조회 실패", e);
    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
} catch (Exception e) {
    logger.error("예상치 못한 오류", e);
    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}
```

### 3. 코드 생성 가이드

#### 3.1 새로운 기능 추가 시 순서

1. **DTO 작성** (필요한 경우)
    - 필드 정의
    - Getter/Setter 생성
    - toString() 구현

2. **예외 클래스 작성** (필요한 경우)
    - RuntimeException 또는 Exception 상속
    - 생성자 정의

3. **DAO 작성**
    - 인터페이스 메서드 정의
    - MyBatis Mapper XML 작성

4. **Service 작성**
    - 비즈니스 로직 구현
    - DAO 호출 및 조합
    - 예외 처리

5. **Controller (Servlet) 작성**
    - doGet/doPost 구현
    - 파라미터 추출 및 검증
    - Service 호출
    - 결과 처리 (forward/redirect)

6. **JSP 작성**
    - JSTL 사용
    - EL 표현식으로 데이터 표시
    - form 검증 스크립트

#### 3.2 MyBatis Mapper 작성 시 주의사항

```xml
<!-- ✅ Good: 파라미터 바인딩 사용 (SQL Injection 방지) -->
<select id="selectById" parameterType="int" resultType="BoardDTO">
    SELECT * FROM board WHERE board_id = #{boardId}
</select>

<!-- ❌ Bad: 문자열 결합 (SQL Injection 위험) -->
<select id="selectById" parameterType="int" resultType="BoardDTO">
    SELECT * FROM board WHERE board_id = ${boardId}
</select>

<!-- ✅ Good: 동적 SQL -->
<select id="searchBoards" parameterType="SearchCondition" resultType="BoardDTO">
    SELECT * FROM board
    WHERE 1=1
    <if test="categoryId != null">
        AND category_id = #{categoryId}
    </if>
    <if test="keyword != null and keyword != ''">
        AND (title LIKE CONCAT('%', #{keyword}, '%')
             OR content LIKE CONCAT('%', #{keyword}, '%')
             OR user_name LIKE CONCAT('%', #{keyword}, '%'))
    </if>
    <if test="from != null">
        AND created_at &gt;= #{from}
    </if>
    <if test="to != null">
        AND created_at &lt;= #{to}
    </if>
    ORDER BY created_at DESC
    LIMIT #{limit} OFFSET #{offset}
</select>
```

### 4. 보안 요구사항

반드시 준수해야 하는 보안 규칙:

```java
// ✅ 1. 비밀번호 해싱 (SHA-256)
// Mapper XML에서 처리
<insert id="insert">
    INSERT INTO board (password, ...) 
    VALUES (SHA2(#{password}, 256), ...)
</insert>

// ✅ 2. 파일 업로드 검증
public void validateFile(Part filePart) throws ValidationException {
    // 크기 검증
    if (filePart.getSize() > BoardConstants.MAX_FILE_SIZE) {
        throw new ValidationException("파일 크기는 2MB를 초과할 수 없습니다.");
    }
    
    // 확장자 검증
    String fileName = filePart.getSubmittedFileName();
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    if (!Arrays.asList(BoardConstants.ALLOWED_EXTENSIONS).contains(extension)) {
        throw new ValidationException("허용되지 않는 파일 형식입니다.");
    }
}

// ✅ 3. UUID로 파일명 변경
String physicalName = UUID.randomUUID().toString() + "." + extension;

// ✅ 4. XSS 방지 (JSP에서 c:out 사용)
<c:out value="${board.title}" />

// ❌ Bad
${board.title}  <!-- XSS 위험 -->
```

### 5. 페이징 처리 패턴

```java
// Controller에서 페이지 번호 추출
int page = 1;
String pageParam = request.getParameter("page");
if (pageParam != null && !pageParam.isEmpty()) {
    try {
        page = Integer.parseInt(pageParam);
        if (page < 1) page = 1;
    } catch (NumberFormatException e) {
        page = 1;
    }
}

// Service에서 offset 계산
int pageSize = BoardConstants.DEFAULT_PAGE_SIZE;
int offset = (page - 1) * pageSize;

// DAO에서 LIMIT, OFFSET 사용
List<BoardDTO> boards = boardDAO.selectList(offset, pageSize);
int totalCount = boardDAO.countAll();

// 전체 페이지 수 계산
int totalPages = (int) Math.ceil((double) totalCount / pageSize);

// JSP에 전달
request.setAttribute("boardList", boards);
request.setAttribute("currentPage", page);
request.setAttribute("totalPages", totalPages);
```

### 6. 트랜잭션 처리

```java
// ✅ 게시글 등록 + 파일 업로드 (트랜잭션 필요)
public void createBoard(BoardDTO board, List<Part> files) throws Exception {
    Connection conn = null;
    try {
        conn = dataSource.getConnection();
        conn.setAutoCommit(false);  // 트랜잭션 시작
        
        // 1. 게시글 등록
        boardDAO.insert(conn, board);
        int boardId = board.getBoardId();  // auto_increment로 생성된 ID
        
        // 2. 파일 저장
        for (Part file : files) {
            FileDTO fileDTO = fileService.saveFile(file, boardId);
            fileDAO.insert(conn, fileDTO);
        }
        
        conn.commit();  // 커밋
    } catch (Exception e) {
        if (conn != null) {
            conn.rollback();  // 롤백
        }
        throw e;
    } finally {
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
```

### 7. 검증 규칙

모든 입력값은 프론트엔드와 백엔드 양쪽에서 검증합니다.

```java
// ValidationUtil.java
public class ValidationUtil {
    public static void validateTitle(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("제목을 입력해주세요.");
        }
        if (title.length() < 4 || title.length() > 1000) {
            throw new ValidationException("제목은 4~1000자 사이여야 합니다.");
        }
    }
    
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("비밀번호를 입력해주세요.");
        }
        if (password.length() < 8 || password.length() > 12) {
            throw new ValidationException("비밀번호는 8~12자 사이여야 합니다.");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            throw new ValidationException("비밀번호는 영문과 숫자를 포함해야 합니다.");
        }
    }
}
```

### 8. JSP 작성 가이드

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 목록</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <!-- ✅ XSS 방지: c:out 사용 -->
        <h1><c:out value="${title}" /></h1>
        
        <!-- ✅ 조건부 렌더링 -->
        <c:if test="${not empty message}">
            <div class="alert alert-success">
                <c:out value="${message}" />
            </div>
        </c:if>
        
        <!-- ✅ 반복문 -->
        <c:forEach var="board" items="${boardList}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td><c:out value="${board.title}" /></td>
                <td>
                    <!-- ✅ 날짜 포맷팅 -->
                    <fmt:formatDate value="${board.createdAt}" 
                                    pattern="yyyy-MM-dd HH:mm:ss" />
                </td>
            </tr>
        </c:forEach>
        
        <!-- ✅ URL 생성 -->
        <a href="${pageContext.request.contextPath}/boards?page=${currentPage + 1}">
            다음 페이지
        </a>
    </div>
    
    <script src="${pageContext.request.contextPath}/resources/js/board.js"></script>
</body>
</html>
```

### 9. 로깅 가이드

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
    
    public BoardDTO getBoard(int boardId) throws BoardNotFoundException {
        // DEBUG: 상세한 실행 흐름
        logger.debug("게시글 조회 시작: boardId={}", boardId);
        
        try {
            BoardDTO board = boardDAO.selectById(boardId);
            
            if (board == null) {
                // WARN: 예상 가능한 문제
                logger.warn("게시글을 찾을 수 없음: boardId={}", boardId);
                throw new BoardNotFoundException("게시글을 찾을 수 없습니다.");
            }
            
            // INFO: 주요 비즈니스 이벤트
            logger.info("게시글 조회 성공: boardId={}, title={}", 
                       boardId, board.getTitle());
            
            return board;
        } catch (BoardNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // ERROR: 예상치 못한 오류
            logger.error("게시글 조회 중 오류 발생: boardId={}", boardId, e);
            throw new RuntimeException("게시글 조회 실패", e);
        }
    }
}
```

### 10. 테스트 작성 (선택사항)

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {
    
    @Test
    void testGetBoard_Success() throws Exception {
        // Given
        BoardService boardService = new BoardService();
        int boardId = 1;
        
        // When
        BoardDTO board = boardService.getBoard(boardId);
        
        // Then
        assertNotNull(board);
        assertEquals(boardId, board.getBoardId());
    }
    
    @Test
    void testGetBoard_NotFound() {
        // Given
        BoardService boardService = new BoardService();
        int boardId = 999999;
        
        // When & Then
        assertThrows(BoardNotFoundException.class, () -> {
            boardService.getBoard(boardId);
        });
    }
}
```

## 일반적인 실수 방지

### ❌ 하지 말아야 할 것들

```java
// 1. Servlet에 비즈니스 로직 작성
public class BoardListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // ❌ Bad: Servlet에서 직접 DAO 호출
        BoardDAO boardDAO = new BoardDAO();
        List<BoardDTO> boards = boardDAO.selectAll();
    }
}

// 2. 하드코딩된 값
// ❌ Bad
if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {

// ✅ Good
if (Arrays.asList(BoardConstants.ALLOWED_EXTENSIONS).contains(extension)) {

// 3. 예외 무시
try {
    // ...
} catch (Exception e) {
    // ❌ Bad: 아무것도 하지 않음
}

// 4. SQL Injection 위험
// ❌ Bad
String sql = "SELECT * FROM board WHERE title LIKE '%" + keyword + "%'";

// ✅ Good: MyBatis 파라미터 바인딩
<select id="search">
    SELECT * FROM board WHERE title LIKE CONCAT('%', #{keyword}, '%')
</select>

// 5. 리소스 누수
// ❌ Bad
Connection conn = dataSource.getConnection();
// ... 사용
// conn.close() 없음

// ✅ Good: try-with-resources
try (Connection conn = dataSource.getConnection()) {
    // ...
} // 자동으로 close됨
```

## 질문할 때 포함할 정보

Claude에게 질문할 때는 다음 정보를 포함하세요:

1. **어떤 계층의 코드인가?** (Controller/Service/DAO/DTO/Util)
2. **어떤 기능을 구현하는가?** (게시글 등록, 조회, 수정, 삭제 등)
3. **현재 코드** (있다면)
4. **에러 메시지** (있다면)
5. **참조한 PRD 섹션** (예: "3.4 게시글 등록")

### 좋은 질문 예시

```
"게시글 수정 기능을 구현하려고 합니다.
- PRD 3.5절의 게시글 수정 요구사항을 참조했습니다.
- BoardEditServlet에서 비밀번호 검증 후 수정 페이지로 이동하는 부분을 구현해주세요.
- BoardService의 updateBoard 메서드도 필요합니다.
- 비밀번호 검증 실패 시 401 에러를 반환해야 합니다."
```

## 참고 자료

- **PRD**: 모든 기능 요구사항과 API 명세
- **Convention**: Google Java Style Guide 기반 코딩 규칙
- **Servlet 3.1 Spec**: https://javaee.github.io/servlet-spec/
- **MyBatis 문서**: https://mybatis.org/mybatis-3/
- **JSTL 문서**: https://docs.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/

## 버전 정보

- **문서 버전**: 1.0.0
- **최종 수정일**: 2024-12-23
- **작성자**: Development Team

---

**이 문서는 프로젝트가 진행됨에 따라 지속적으로 업데이트됩니다.**