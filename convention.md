# Java 코딩 컨벤션

본 문서는 Google Java Style Guide를 기반으로 작성되었으며, 프로젝트 특성에 맞게 일부 수정되었습니다.

## 1. 소스 파일 기본사항

### 1.1 파일 인코딩
- **UTF-8** 사용

### 1.2 특수 문자
- **공백 문자**: 공백(space)만 사용, 탭(tab) 사용 금지
- **이스케이프 시퀀스**: `\b`, `\t`, `\n`, `\f`, `\r`, `\"`, `\'`, `\\` 사용
- **비 ASCII 문자**: 유니코드 이스케이프(`\u000a`) 대신 실제 문자 사용

## 2. 소스 파일 구조

소스 파일은 다음 순서로 구성:
1. 라이선스 또는 저작권 정보 (있는 경우)
2. Package 문
3. Import 문
4. 클래스 선언

**각 섹션 사이에 정확히 한 줄의 빈 줄 삽입**

### 2.1 Package 문
- 줄바꿈하지 않음
- 컬럼 제한(100자)에 영향받지 않음

### 2.2 Import 문
- 와일드카드 import 사용 금지 (❌ `import java.util.*`)
- 줄바꿈하지 않음
- 정적 import와 비정적 import 구분
- 알파벳 순으로 정렬

**순서**:
1. 정적 import (알파벳순)
2. 빈 줄
3. 비정적 import (알파벳순)

```java
import static com.board.util.Constants.MAX_FILE_SIZE;
import static com.board.util.Constants.UPLOAD_DIR;

import com.board.dto.Board;
import com.board.exception.BoardException;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
```

### 2.3 클래스 선언

#### 클래스 구성 순서
1. Static 변수 (public → protected → private)
2. Instance 변수 (public → protected → private)
3. 생성자
4. 메서드
    - Public 메서드
    - Protected 메서드
    - Private 메서드
5. Inner 클래스 또는 Interface

**오버로드된 메서드는 연속해서 배치**

```java
public class BoardService {
    // Static 변수
    private static final int PAGE_SIZE = 10;
    
    // Instance 변수
    private final BoardDAO boardDAO;
    private final FileService fileService;
    
    // 생성자
    public BoardService() {
        this.boardDAO = new BoardDAO();
        this.fileService = new FileService();
    }
    
    // Public 메서드
    public List<Board> getBoardList(int page) { }
    public Board getBoardById(Long boardId) { }
    
    // Private 메서드
    private void validateBoard(Board board) { }
}
```

## 3. 포매팅

### 3.1 중괄호

#### K&R 스타일 사용
```java
// ✅ 올바른 예
if (condition) {
    doSomething();
}

// ❌ 잘못된 예
if (condition)
{
    doSomething();
}
```

#### 빈 블록
```java
// ✅ 간결한 스타일 허용
void doNothing() {}

// ✅ K&R 스타일도 가능
void doNothingElse() {
}

// ❌ 잘못된 예
void invalid() {
    // 주석만 있는 경우
}
```

### 3.2 블록 들여쓰기
- **2칸 공백** 사용 (탭 금지)
- 새 블록이 시작될 때마다 2칸씩 증가

```java
public void example() {
  if (condition) {
    for (int i = 0; i < 10; i++) {
      doSomething(i);
    }
  }
}
```

### 3.3 줄당 하나의 문장
```java
// ✅ 올바른 예
int a = 1;
int b = 2;

// ❌ 잘못된 예
int a = 1, b = 2;
```

### 3.4 컬럼 제한
- **100자** 제한
- 예외:
    - Package 및 import 문
    - 주석의 URL
    - 긴 문자열 리터럴

### 3.5 줄바꿈
컬럼 제한을 초과하는 경우 줄바꿈 수행

#### 줄바꿈 위치
1. **비 할당 연산자** 앞에서 끊김
   ```java
   // ✅ 올바른 예
   String longString = "This is a very long string that needs "
       + "to be broken into multiple lines";
   ```

2. **할당 연산자** 뒤에서 끊김
   ```java
   // ✅ 올바른 예
   String veryLongVariableName =
       someMethodThatReturnsAString();
   ```

3. **메서드 또는 생성자 이름** 뒤의 여는 괄호 `(`에 붙임
   ```java
   // ✅ 올바른 예
   public void someLongMethodName(
       String parameter1, String parameter2) {
   ```

4. **쉼표** 뒤에서 끊김
   ```java
   // ✅ 올바른 예
   doSomething(parameter1,
       parameter2,
       parameter3);
   ```

#### 들여쓰기
- 계속되는 줄은 **최소 +4칸** 들여쓰기

```java
// ✅ 올바른 예
String result = someObject.someMethod()
    .anotherMethod()
    .yetAnotherMethod();

if (veryLongCondition1 && veryLongCondition2
    && veryLongCondition3) {
  doSomething();
}
```

### 3.6 공백

#### 수직 공백
다음 경우에 빈 줄 삽입:
- 클래스 멤버 사이 (필드, 생성자, 메서드 등)
- 메서드 내에서 논리적 그룹을 구분할 때
- Import 그룹 사이

```java
public class Example {
  private int field1;
  private int field2;
  
  public Example() {
    // 생성자
  }
  
  public void method1() {
    // 메서드 1
  }
  
  public void method2() {
    // 메서드 2
  }
}
```

#### 수평 공백
1. **예약어와 괄호 사이**
   ```java
   if (condition) { }
   for (int i = 0; i < 10; i++) { }
   while (condition) { }
   ```

2. **이항/삼항 연산자 양쪽**
   ```java
   int sum = a + b;
   boolean result = (a > b) ? true : false;
   ```

3. **쉼표, 세미콜론, 콜론 뒤**
   ```java
   doSomething(a, b, c);
   for (int i = 0; i < 10; i++) { }
   ```

4. **주석 시작 문자 뒤**
   ```java
   // 주석
   /* 주석 */
   ```

5. **중괄호 양쪽** (특정 상황)
   ```java
   new int[] {1, 2, 3}
   ```

## 4. 네이밍

### 4.1 공통 규칙
- 영문 알파벳, 숫자만 사용
- 언더스코어(_)는 상수에만 사용
- 의미 있고 발음 가능한 이름 사용

### 4.2 패키지명
- **소문자** + **숫자** (언더스코어 사용 안 함)
- 단어 연결 시 그냥 연결 (구분자 없음)

```java
com.board.servlet
com.board.service
com.board.dao
com.board.dto
com.board.exception
com.board.util
```

### 4.3 클래스명
- **UpperCamelCase** (PascalCase)
- 명사 또는 명사구
- 테스트 클래스: `{테스트 대상 클래스명}Test`

```java
// ✅ 올바른 예
BoardListServlet
BoardService
BoardDAO
BoardException
FileUploadUtil

// 테스트 클래스
BoardServiceTest
```

### 4.4 메서드명
- **lowerCamelCase**
- 동사 또는 동사구

```java
// ✅ 올바른 예
getBoardList()
createBoard()
deleteBoard()
validateInput()
isValidPassword()
hasPermission()
```

**규칙**:
- **get**: 값 반환
- **set**: 값 설정
- **is/has**: boolean 반환
- **create/add**: 생성
- **update/modify**: 수정
- **delete/remove**: 삭제
- **validate**: 유효성 검증

### 4.5 변수명
- **lowerCamelCase**
- 명사 또는 명사구

```java
// ✅ 올바른 예
String userName;
int pageNumber;
List<Board> boardList;
```

#### 한 글자 변수명
루프 인덱스 또는 예외 변수에만 사용:
```java
for (int i = 0; i < 10; i++) { }
try { } catch (Exception e) { }
```

### 4.6 상수명
- **UPPER_SNAKE_CASE**
- `static final` 필드

```java
// ✅ 올바른 예
private static final int MAX_FILE_SIZE = 2097152; // 2MB
private static final String UPLOAD_DIR = "/uploads";
private static final int PAGE_SIZE = 10;
```

### 4.7 프로젝트별 네이밍 규칙

#### DTO 클래스
```java
Board.java          // 게시글
File.java           // 파일 (BoardFile로 충돌 방지 시 사용)
Comment.java        // 댓글
Category.java       // 카테고리
```

#### DAO 클래스
```java
BoardDAO.java
FileDAO.java
CommentDAO.java
CategoryDAO.java
```

#### Service 클래스
```java
BoardService.java
FileService.java
CommentService.java
```

#### Servlet 클래스
```java
BoardListServlet.java       // GET /boards
BoardViewServlet.java       // GET /board/view
BoardPostServlet.java       // GET/POST /board/post
BoardEditServlet.java       // GET/POST /board/edit
BoardDeleteServlet.java     // POST /board/delete
CommentServlet.java         // POST /comment
FileDownloadServlet.java    // GET /download
AuthServlet.java            // POST /auth/confirm
```

#### Exception 클래스
```java
BoardException.java         // 기본 예외
ValidationException.java    // 검증 예외
FileUploadException.java    // 파일 업로드 예외
AuthenticationException.java // 인증 예외
```

#### Util 클래스
```java
FileUtil.java              // 파일 처리
ValidationUtil.java        // 유효성 검증
PasswordUtil.java          // 비밀번호 해싱 (필요 시)
```

## 5. 주석

### 5.1 Javadoc

#### 필수 대상
- Public 클래스
- Public/Protected 메서드

#### 형식
```java
/**
 * 게시글 목록을 조회합니다.
 *
 * @param page 페이지 번호 (1부터 시작)
 * @param keyword 검색어 (선택)
 * @return 게시글 목록
 * @throws BoardException 조회 중 오류 발생 시
 */
public List<Board> getBoardList(int page, String keyword) throws BoardException {
  // 구현
}
```

#### 요약 설명
- 첫 문장은 완전한 문장으로 작성
- 마침표로 끝남
- 메서드의 경우 3인칭 현재형 동사로 시작 ("Returns the...", "Sets the...")

### 5.2 구현 주석

#### 블록 주석
```java
/*
 * 여러 줄에 걸친
 * 주석입니다.
 */
```

#### 한 줄 주석
```java
// 짧은 주석
```

#### 사용 시점
- 복잡한 로직 설명
- TODO 표시
- 중요한 결정 사항 기록

```java
// TODO: 파일 업로드 검증 로직 추가 필요
// FIXME: 트랜잭션 롤백 처리 개선

// 비밀번호는 SHA-256으로 해싱하여 저장
String hashedPassword = hashPassword(password);
```

## 6. 프로그래밍 관행

### 6.1 @Override 애노테이션
항상 사용:
```java
@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) {
  // 구현
}
```

### 6.2 예외 처리
- 빈 catch 블록 금지
- 구체적인 예외 타입 사용
- 예외를 무시하는 경우 주석으로 이유 설명

```java
// ✅ 올바른 예
try {
  doSomething();
} catch (IOException e) {
  logger.error("파일 읽기 실패", e);
  throw new BoardException("파일 처리 중 오류가 발생했습니다.", e);
}

// ❌ 잘못된 예
try {
  doSomething();
} catch (Exception e) {
  // 무시
}
```

### 6.3 Static 멤버
클래스명으로 접근:
```java
// ✅ 올바른 예
String result = MyClass.staticMethod();

// ❌ 잘못된 예
MyClass obj = new MyClass();
String result = obj.staticMethod();
```

### 6.4 파이널라이저 사용 금지
`finalize()` 메서드 오버라이드 금지

## 7. 프로젝트별 추가 규칙

### 7.1 SQL 쿼리
- MyBatis Mapper XML에 작성
- 동적 SQL은 `<if>`, `<choose>` 등 MyBatis 태그 사용
- 파라미터 바인딩은 `#{}` 사용 (SQL Injection 방지)

```xml
<!-- ✅ 올바른 예 -->
<select id="selectBoardById" resultType="Board">
  SELECT * FROM board WHERE board_id = #{boardId}
</select>

<!-- ❌ 잘못된 예 (SQL Injection 위험) -->
<select id="selectBoardById" resultType="Board">
  SELECT * FROM board WHERE board_id = ${boardId}
</select>
```

### 7.2 JSP 스크립틀릿
- 필요한 경우에만 사용
- 복잡한 로직은 Servlet/Service로 분리
- EL 표현식 우선 사용

```jsp
<!-- ✅ 가능한 예 -->
<%
  List<Board> boards = (List<Board>) request.getAttribute("boards");
  for (Board board : boards) {
%>
  <tr>
    <td><%= board.getTitle() %></td>
  </tr>
<%
  }
%>

<!-- ✅ 더 나은 예 (EL 사용) -->
<c:forEach items="${boards}" var="board">
  <tr>
    <td>${board.title}</td>
  </tr>
</c:forEach>
```

### 7.3 리소스 관리
- try-with-resources 사용
- Connection, Statement, ResultSet 등은 반드시 닫기

```java
// ✅ 올바른 예
try (Connection conn = dataSource.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
  // 사용
} catch (SQLException e) {
  // 예외 처리
}
```

### 7.4 로깅
- System.out.println() 대신 로거 사용
- 민감 정보(비밀번호 등) 로깅 금지

```java
// ✅ 올바른 예
private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

logger.info("게시글 조회: boardId={}", boardId);
logger.error("게시글 삭제 실패: boardId={}", boardId, e);

// ❌ 잘못된 예
System.out.println("게시글 조회: " + boardId);
logger.info("사용자 비밀번호: " + password); // 민감 정보 로깅 금지
```

## 8. 체크리스트

코드 작성 후 다음 사항을 확인:

- [ ] 들여쓰기는 2칸 공백인가?
- [ ] 한 줄은 100자를 넘지 않는가?
- [ ] 모든 Public 클래스와 메서드에 Javadoc이 있는가?
- [ ] 네이밍 규칙을 준수했는가?
- [ ] Import 문은 정렬되어 있고 와일드카드가 없는가?
- [ ] 예외는 적절히 처리되고 있는가?
- [ ] 리소스는 확실히 닫히는가?
- [ ] SQL Injection 취약점은 없는가?
- [ ] 민감 정보가 로그에 남지 않는가?

## 참고 자료

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Oracle Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)