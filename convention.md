# Java Coding Convention

> 본 프로젝트는 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 기반으로 합니다.

## 목차

1. [소스 파일 기본사항](#1-소스-파일-기본사항)
2. [소스 파일 구조](#2-소스-파일-구조)
3. [포매팅](#3-포매팅)
4. [명명 규칙](#4-명명-규칙)
5. [프로그래밍 관례](#5-프로그래밍-관례)
6. [Javadoc](#6-javadoc)
7. [프로젝트 특화 규칙](#7-프로젝트-특화-규칙)

---

## 1. 소스 파일 기본사항

### 1.1 파일 이름

- 소스 파일 이름은 최상위 클래스 이름 + `.java` 확장자로 구성됩니다.
- 대소문자를 구분합니다.

```java
// 파일명: BoardController.java
public class BoardController {
    // ...
}
```

### 1.2 파일 인코딩

- **UTF-8** 인코딩을 사용합니다.

### 1.3 특수 문자

#### 1.3.1 공백 문자

- 줄바꿈 문자를 제외하고, **ASCII 공백 문자(0x20)**만 사용합니다.
- **탭 문자는 들여쓰기에 사용하지 않습니다.**

#### 1.3.2 이스케이프 시퀀스

- 특수 이스케이프 시퀀스가 있는 문자(`\b`, `\t`, `\n`, `\f`, `\r`, `\"`, `\'`, `\\`)는 8진수나 유니코드 대신 이스케이프 시퀀스를 사용합니다.

```java
// Good
String newline = "first line\nsecond line";

// Bad
String newline = "first line\u000Asecond line";
```

---

## 2. 소스 파일 구조

소스 파일은 다음 순서로 구성됩니다:

1. 라이선스 또는 저작권 정보 (있는 경우)
2. Package 문
3. Import 문
4. 정확히 하나의 최상위 클래스

각 섹션은 **한 줄의 빈 줄**로 구분합니다.

### 2.1 Package 문

- Package 문은 **줄바꿈하지 않습니다**.
- 열 제한(100자)이 적용되지 않습니다.

```java
package com.example.board.controller;
```

### 2.2 Import 문

#### 2.2.1 와일드카드 import 금지

- 와일드카드 import(static 또는 일반)를 사용하지 않습니다.

```java
// Good
import java.util.List;
import java.util.ArrayList;

// Bad
import java.util.*;
```

#### 2.2.2 줄바꿈 금지

- Import 문은 줄바꿈하지 않습니다.
- 열 제한이 적용되지 않습니다.

#### 2.2.3 순서 및 간격

Import 문은 다음 순서로 그룹화하고, 각 그룹 사이에 빈 줄을 넣습니다:

1. 모든 static import (한 블록)
2. 모든 non-static import (한 블록)

각 블록 내에서는 알파벳 순으로 정렬합니다.

```java
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
```

### 2.3 클래스 선언

#### 2.3.1 정확히 하나의 최상위 클래스

- 각 소스 파일에는 정확히 하나의 최상위 클래스가 있습니다.

#### 2.3.2 클래스 멤버 순서

클래스 멤버의 순서는 학습 가능성에 큰 영향을 미칩니다. 권장 순서:

1. 상수 (static final 필드)
2. 정적 필드 (static 필드)
3. 인스턴스 필드
4. 생성자
5. 메서드
    - public 메서드
    - protected 메서드
    - private 메서드
6. 내부 클래스/인터페이스

**오버로드된 메서드는 연속적으로 배치합니다.**

```java
public class BoardService {
    // 1. 상수
    private static final int MAX_TITLE_LENGTH = 100;
    
    // 2. 정적 필드
    private static int instanceCount = 0;
    
    // 3. 인스턴스 필드
    private BoardDAO boardDAO;
    private FileService fileService;
    
    // 4. 생성자
    public BoardService() {
        instanceCount++;
    }
    
    public BoardService(BoardDAO boardDAO) {
        this.boardDAO = boardDAO;
        instanceCount++;
    }
    
    // 5. public 메서드
    public List<BoardDTO> getBoardList(int page) {
        // ...
    }
    
    public BoardDTO getBoard(int boardId) {
        // ...
    }
    
    // 6. private 메서드
    private void validateBoard(BoardDTO board) {
        // ...
    }
}
```

---

## 3. 포매팅

### 3.1 중괄호

#### 3.1.1 중괄호 사용 (K&R 스타일)

- 중괄호는 `if`, `else`, `for`, `do`, `while` 문에서 본문이 비어 있거나 단일 문인 경우에도 사용합니다.

```java
// Good
if (condition) {
    doSomething();
}

// Bad
if (condition)
    doSomething();
```

#### 3.1.2 비어있지 않은 블록: K&R 스타일

- 여는 중괄호 앞에 줄바꿈 없음
- 여는 중괄호 뒤에 줄바꿈
- 닫는 중괄호 앞에 줄바꿈
- 닫는 중괄호 뒤에 줄바꿈 (메서드, 생성자, 클래스 종료 등)

```java
public void example() {
    if (condition) {
        try {
            something();
        } catch (Exception e) {
            recover();
        }
    } else {
        somethingElse();
    }
}
```

#### 3.1.3 빈 블록

- 빈 블록은 `{}`로 간결하게 작성할 수 있습니다.

```java
// Good
public void doNothing() {}

// Also acceptable
public void doNothing() {
}
```

### 3.2 블록 들여쓰기: +2 스페이스

- 새 블록이나 블록과 유사한 구조가 열릴 때마다 들여쓰기는 **2 스페이스** 증가합니다.
- 블록이 종료되면 들여쓰기는 이전 수준으로 돌아갑니다.

```java
public class Example {
  private int value;
  
  public void method() {
    if (condition) {
      doSomething();
    }
  }
}
```

### 3.3 한 줄에 한 문장

- 각 문장 뒤에는 줄바꿈이 옵니다.

```java
// Good
int a = 1;
int b = 2;

// Bad
int a = 1; int b = 2;
```

### 3.4 열 제한: 100

- 프로젝트는 **100자** 열 제한을 사용합니다.
- 예외:
    - 열 제한을 준수할 수 없는 줄 (예: Javadoc의 긴 URL)
    - package 및 import 문
    - 주석의 명령줄

### 3.5 줄바꿈

#### 3.5.1 언제 줄바꿈하는가

- 주요 원칙: **더 높은 구문 수준에서 줄바꿈**하는 것을 선호합니다.

```java
// Good - 메서드 체인
someObject.someMethod()
    .anotherMethod()
    .yetAnotherMethod();

// Good - 연산자 앞에서 줄바꿈
int result = veryLongVariableName
    + anotherLongVariableName
    + yetAnotherVariable;
```

#### 3.5.2 들여쓰기

- 줄바꿈 시 원래 줄에서 최소 **+4 스페이스** 들여씁니다.

```java
public void longMethodName(String firstParameter,
    String secondParameter, String thirdParameter) {
    // 메서드 본문
}
```

### 3.6 공백

#### 3.6.1 수직 공백

다음 경우에 빈 줄을 사용합니다:

- 클래스의 멤버(필드, 생성자, 메서드, 내부 클래스 등) 사이
- 메서드 내에서 논리적 그룹을 구분하기 위해
- 첫 번째 멤버 앞이나 마지막 멤버 뒤 (선택 사항)

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

#### 3.6.2 수평 공백

다음 경우에 공백을 사용합니다:

```java
// if, for, while, switch 등의 키워드와 여는 괄호 사이
if (condition) {

// else, catch 등의 키워드와 앞의 닫는 중괄호 사이
} else {

// 여는 중괄호 앞
void method() {

// 이항/삼항 연산자 양쪽
a + b
condition ? a : b

// 타입 경계 앞뒤 : <T extends Foo & Bar>

// 주석 시작 문자 뒤
// This is a comment

// 콤마, 콜론, 세미콜론 뒤
for (int i = 0; i < 10; i++) {
```

### 3.7 괄호 그룹화

- 선택적 괄호는 작성자와 검토자가 괄호 없이도 코드를 잘못 해석할 가능성이 없고, 코드를 더 읽기 쉽게 만들지 않는다고 동의할 때만 생략합니다.

```java
// Good - 명확함
if ((a && b) || c) {
    
// Also acceptable if clear
if (a && b || c) {
```

---

## 4. 명명 규칙

### 4.1 모든 식별자에 공통적인 규칙

- 식별자는 ASCII 문자와 숫자만 사용합니다.
- 밑줄은 상수 이름과 일부 예외적인 경우에만 사용합니다.

### 4.2 식별자 유형별 규칙

#### 4.2.1 Package 이름

- 모두 소문자
- 연속된 단어는 단순히 연결 (밑줄 없음)

```java
com.example.board
com.example.board.controller
```

#### 4.2.2 Class 이름

- **UpperCamelCase** (PascalCase)
- 명사 또는 명사구

```java
public class BoardController { }
public class BoardService { }
public class BoardDTO { }
```

#### 4.2.3 Method 이름

- **lowerCamelCase**
- 동사 또는 동사구

```java
public void getBoardList() { }
public void createBoard() { }
public boolean isValid() { }
```

#### 4.2.4 Constant 이름

- **UPPER_SNAKE_CASE**
- 모두 대문자, 단어는 밑줄로 구분
- `static final` 필드로서 내용이 불변인 경우

```java
private static final int MAX_BOARD_COUNT = 100;
private static final String DEFAULT_CATEGORY = "Java";
```

#### 4.2.5 Non-constant 필드 이름

- **lowerCamelCase**
- 명사 또는 명사구

```java
private BoardDAO boardDAO;
private String userName;
private int pageNumber;
```

#### 4.2.6 Parameter 이름

- **lowerCamelCase**
- public 메서드에서 한 글자 매개변수 이름을 피합니다.

```java
// Good
public void setTitle(String title) { }

// Bad (한 글자 변수명)
public void setTitle(String t) { }

// Acceptable (for loop의 경우)
for (int i = 0; i < list.size(); i++) {
```

#### 4.2.7 Local 변수 이름

- **lowerCamelCase**
- final이고 불변인 경우에도 상수로 간주되지 않으며, 상수 스타일로 작성되지 않습니다.

```java
String userName = "John";
int totalCount = 100;
```

#### 4.2.8 Type 변수 이름

두 가지 스타일 중 하나:

1. 단일 대문자, 선택적으로 단일 숫자: `E`, `T`, `K`, `V`
2. 클래스에 사용되는 형식 + 대문자 `T`: `RequestT`, `FooBarT`

```java
public class Box<T> { }
public interface List<E> { }
public class HashMap<K, V> { }
```

### 4.3 Camel Case 정의

약어나 특이한 구조가 있는 경우:

1. 구문을 일반 ASCII로 변환하고 아포스트로피 제거
2. 공백과 구두점으로 단어 분리
3. 모든 것을 소문자로 변환
4. 다음의 첫 글자만 대문자로:
    - UpperCamelCase: 각 단어
    - lowerCamelCase: 첫 단어를 제외한 각 단어

```java
// "XML HTTP request" → XmlHttpRequest (UpperCamelCase)
// "supports IPv6 on iOS" → supportsIpv6OnIos (lowerCamelCase)
```

---

## 5. 프로그래밍 관례

### 5.1 @Override 항상 사용

- 메서드가 상위 클래스 메서드를 재정의하거나, 인터페이스 메서드를 구현하거나, 상위 인터페이스 메서드를 재지정하는 경우 `@Override` 어노테이션을 사용합니다.

```java
@Override
public String toString() {
    return "BoardDTO";
}
```

### 5.2 예외 처리

#### 5.2.1 예외를 무시하지 않음

- catch 블록에서 예외를 무시하는 것은 매우 드물게 정당화됩니다.
- 무시하는 것이 정당한 경우, 주석으로 설명합니다.

```java
// Good
try {
    int result = someMethod();
} catch (NumberFormatException expected) {
    // 예외 발생이 예상되는 경우, 무시하는 것이 정당함
}

// Bad
try {
    int result = someMethod();
} catch (Exception e) {
    // 아무것도 하지 않음
}
```

### 5.3 Static 멤버: 클래스로 접근

- static 멤버를 참조할 때는 해당 클래스의 이름으로 접근합니다.

```java
// Good
Foo.aStaticMethod();

// Bad
someInstance.aStaticMethod();
```

### 5.4 Finalizers 사용 금지

- `Object.finalize`를 재정의하지 않습니다.

### 5.5 Switch 문

#### 5.5.1 들여쓰기

- switch 블록의 내용은 +2 들여쓰기
- 각 switch 레이블 뒤에는 +2 들여쓰기

```java
switch (value) {
  case 1:
    doSomething();
    break;
  case 2:
    doSomethingElse();
    break;
  default:
    doDefault();
}
```

#### 5.5.2 Fall-through

- 각 switch 레이블 그룹은 `break`, `return`, 또는 예외를 던지는 것으로 종료하거나, fall-through를 나타내는 주석을 포함해야 합니다.

```java
switch (value) {
  case 1:
    doSomething();
    // fall through
  case 2:
    doSomethingElse();
    break;
  default:
    doDefault();
}
```

#### 5.5.3 Default case

- 각 switch 문은 `default` 문 그룹을 포함해야 합니다 (아무 코드도 없더라도).

```java
switch (value) {
  case 1:
    doSomething();
    break;
  default:
    // 처리할 것이 없음
    break;
}
```

---

## 6. Javadoc

### 6.1 형식

#### 6.1.1 일반 형식

```java
/**
 * 여러 줄의 Javadoc 텍스트는 여기에 작성되며,
 * 일반적으로 줄바꿈됩니다...
 */
public void method(String parameter) {
```

또는 단일 줄:

```java
/** 특히 짧은 Javadoc. */
public void method() {
```

#### 6.1.2 단락

- 빈 줄(정렬된 선행 별표(`*`)만 포함하는 줄)은 단락 사이에 나타납니다.

```java
/**
 * 첫 번째 단락.
 *
 * 두 번째 단락.
 */
```

#### 6.1.3 블록 태그

- 블록 태그는 `@param`, `@return`, `@throws`, `@deprecated` 순서로 나타납니다.
- 4개 이상의 블록 태그가 있는 경우, 설명을 들여쓸 수 있습니다.

```java
/**
 * 게시글을 조회합니다.
 *
 * @param boardId 게시글 ID
 * @return 게시글 정보
 * @throws BoardNotFoundException 게시글이 존재하지 않는 경우
 */
public BoardDTO getBoard(int boardId) throws BoardNotFoundException {
    // ...
}
```

### 6.2 요약 문구

- 각 Javadoc 블록은 간단한 요약 문구로 시작합니다.
- 이 문구는 매우 중요합니다: 클래스 및 메서드 인덱스와 같은 특정 컨텍스트에 나타나는 텍스트의 유일한 부분입니다.

```java
/**
 * 게시글 목록을 페이지 단위로 조회합니다.
 */
public List<BoardDTO> getBoardList(int page) {
```

### 6.3 어디에 Javadoc을 사용하는가

최소한 모든 `public` 클래스와 해당 클래스의 모든 `public` 또는 `protected` 멤버에는 Javadoc이 있어야 합니다.

#### 6.3.1 예외: 자명한 메서드

- `getFoo`와 같은 "간단하고 명백한" 메서드의 경우 Javadoc은 선택 사항입니다.

```java
// Javadoc 선택 사항
public String getTitle() {
    return title;
}

// Javadoc 필요
/**
 * 게시글의 조회수를 1 증가시킵니다.
 */
public void incrementViewCount() {
    this.views++;
}
```

#### 6.3.2 예외: 재정의

- `@Override` 메서드는 항상 Javadoc이 필요한 것은 아닙니다.

---

## 7. 프로젝트 특화 규칙

### 7.1 계층 구조

```
controller  - Servlet 클래스 (요청/응답 처리)
service     - 비즈니스 로직
dao         - 데이터베이스 접근
dto         - 데이터 전송 객체
util        - 유틸리티 클래스
exception   - 사용자 정의 예외
```

### 7.2 클래스 명명 규칙

| 계층 | 접미사 | 예시 |
|------|--------|------|
| Controller | `Servlet` | `BoardListServlet`, `BoardViewServlet` |
| Service | `Service` | `BoardService`, `FileService` |
| DAO | `DAO` | `BoardDAO`, `CommentDAO` |
| DTO | `DTO` 또는 없음 | `BoardDTO`, `CommentDTO` |
| Exception | `Exception` | `BoardNotFoundException` |
| Util | `Util` 또는 `Helper` | `ValidationUtil`, `DateUtil` |

### 7.3 메서드 명명 규칙

#### 7.3.1 CRUD 작업

| 작업 | 접두사 | 예시 |
|------|--------|------|
| 조회 (단건) | `get` | `getBoard(int id)` |
| 조회 (목록) | `get` + `List` | `getBoardList(SearchCondition condition)` |
| 생성 | `create` 또는 `insert` | `createBoard(BoardDTO board)` |
| 수정 | `update` | `updateBoard(BoardDTO board)` |
| 삭제 | `delete` | `deleteBoard(int id)` |
| 개수 조회 | `count` | `countBoards(SearchCondition condition)` |

#### 7.3.2 검증 메서드

```java
// boolean 반환
public boolean isValidPassword(String password) {
    return password != null && password.length() >= 8;
}

// 예외 발생
public void validateBoard(BoardDTO board) throws ValidationException {
    if (board == null) {
        throw new ValidationException("Board cannot be null");
    }
}
```

### 7.4 상수 정의

```java
public class BoardConstants {
    // 페이징
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    
    // 파일
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    public static final int MAX_FILE_COUNT = 3;
    public static final String[] ALLOWED_EXTENSIONS = {"jpg", "pdf", "png"};
    
    // 게시글
    public static final int MIN_TITLE_LENGTH = 4;
    public static final int MAX_TITLE_LENGTH = 1000;
    public static final int MIN_CONTENT_LENGTH = 4;
    public static final int MAX_CONTENT_LENGTH = 4000;
    
    private BoardConstants() {
        // 인스턴스화 방지
        throw new AssertionError();
    }
}
```

### 7.5 DTO 작성 규칙

```java
public class BoardDTO {
    private Long boardId;
    private Long categoryId;
    private String title;
    private String content;
    private String userName;
    private Long views;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    
    // 연관 데이터
    private String categoryName;
    private boolean hasAttachment;
    private List<FileDTO> files;
    private List<CommentDTO> comments;
    
    // 기본 생성자
    public BoardDTO() {
    }
    
    // Getter/Setter
    public Long getBoardId() {
        return boardId;
    }
    
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
    
    // ... 나머지 getter/setter
    
    @Override
    public String toString() {
        return "BoardDTO{" +
            "boardId=" + boardId +
            ", title='" + title + '\'' +
            ", userName='" + userName + '\'' +
            '}';
    }
}
```

### 7.6 예외 처리

```java
// Service 계층
public BoardDTO getBoard(int boardId) throws BoardNotFoundException {
    BoardDTO board = boardDAO.selectById(boardId);
    if (board == null) {
        throw new BoardNotFoundException("게시글을 찾을 수 없습니다. ID: " + boardId);
    }
    return board;
}

// Servlet 계층
try {
    BoardDTO board = boardService.getBoard(boardId);
    request.setAttribute("board", board);
    forward(request, response, "/WEB-INF/views/board/view.jsp");
} catch (BoardNotFoundException e) {
    logger.error("게시글 조회 실패", e);
    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
} catch (Exception e) {
    logger.error("예상치 못한 오류", e);
    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
}
```

### 7.7 로깅

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
    
    public BoardDTO getBoard(int boardId) {
        logger.debug("게시글 조회 시작: boardId={}", boardId);
        
        try {
            BoardDTO board = boardDAO.selectById(boardId);
            logger.info("게시글 조회 성공: boardId={}", boardId);
            return board;
        } catch (Exception e) {
            logger.error("게시글 조회 실패: boardId={}", boardId, e);
            throw e;
        }
    }
}
```

### 7.8 SQL Mapper (MyBatis) 규칙

```xml
<!-- BoardMapper.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.board.dao.BoardDAO">
    
    <!-- Result Map -->
    <resultMap id="boardResultMap" type="com.example.board.dto.BoardDTO">
        <id property="boardId" column="board_id"/>
        <result property="categoryId" column="category_id"/>
        <result property="title" column="title"/>
        <result property="content" column="content"/>
        <result property="userName" column="user_name"/>
        <result property="views" column="views"/>
        <result property="createdAt" column="created_at"/>
        <result property="editedAt" column="edited_at"/>
    </resultMap>
    
    <!-- Select 쿼리 -->
    <select id="selectById" parameterType="int" resultMap="boardResultMap">
        SELECT *
        FROM board
        WHERE board_id = #{boardId}
    </select>
    
    <!-- Insert 쿼리 -->
    <insert id="insert" parameterType="com.example.board.dto.BoardDTO"
            useGeneratedKeys="true" keyProperty="boardId">
        INSERT INTO board (
            category_id, title, content, user_name, password, views
        ) VALUES (
            #{categoryId}, #{title}, #{content}, #{userName}, 
            SHA2(#{password}, 256), 0
        )
    </insert>
    
    <!-- Update 쿼리 -->
    <update id="update" parameterType="com.example.board.dto.BoardDTO">
        UPDATE board
        SET title = #{title},
            content = #{content},
            edited_at = CURRENT_TIMESTAMP
        WHERE board_id = #{boardId}
    </update>
    
    <!-- Delete 쿼리 -->
    <delete id="delete" parameterType="int">
        DELETE FROM board
        WHERE board_id = #{boardId}
    </delete>
    
</mapper>
```

### 7.9 JSP 작성 규칙

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>게시글 목록</h1>
        
        <table class="board-table">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>카테고리</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>조회수</th>
                    <th>등록일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="board" items="${boardList}">
                    <tr>
                        <td>${board.boardId}</td>
                        <td>${board.categoryName}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/board/view?boardId=${board.boardId}">
                                ${board.title}
                            </a>
                        </td>
                        <td>${board.userName}</td>
                        <td>${board.views}</td>
                        <td>
                            <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="${pageContext.request.contextPath}/resources/js/board.js"></script>
</body>
</html>
```

---

## 참고 자료

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Oracle Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [Clean Code by Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

---

## IDE 설정

### IntelliJ IDEA

1. `Settings` → `Editor` → `Code Style` → `Java`
2. `Scheme` 옆의 톱니바퀴 아이콘 클릭
3. `Import Scheme` → `IntelliJ IDEA code style XML`
4. [intellij-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml) 파일 선택

### Eclipse

1. `Window` → `Preferences` → `Java` → `Code Style` → `Formatter`
2. `Import` 클릭
3. [eclipse-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml) 파일 선택

### 자동 포매팅

코드 작성 후 다음 단축키로 자동 포매팅:
- IntelliJ: `Ctrl + Alt + L` (Windows/Linux), `Cmd + Option + L` (Mac)
- Eclipse: `Ctrl + Shift + F` (Windows/Linux), `Cmd + Shift + F` (Mac)