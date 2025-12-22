# PRD

## 1. 제품 개요

- **제품 이름**: 기본적인 게시판
- **주요 기능**: 게시판 CRUD

## 2. 목표

- Tomcat에서 동작하는 **Servlet + JSP**으로 게시글 CRUD를 구현한다.
- 페이지는 총 4개
    - 게시판 목록
    - 게시판 쓰기
    - 게시판 보기
    - 게시판 수정

## 3. 기능 요구사항

### 기능 개요

- 게시글 목록 조회
- 게시글 검색
- 게시글 등록
- 게시글 조회
- 게시글 수정
- 게시글 삭제
- 첨부파일 다운로드
- 댓글 등록
- 게시글 작성자 확인

### 3.1 게시글 목록 조회

- 사용자는 게시글 목록을 조회할 수 있다.
- 게시글은 최신순으로 정렬된다.
- 한 페이지에 최대 10개의 게시글을 표시한다.
- 목록 다음 정보를 표시한다.
    - 카테고리
    - 첨부파일 여부 (존재 시 파일 아이콘 표시)
    - 제목
    - 작성자
    - 조회수
    - 등록일시
    - 수정일시

#### 페이지네이션

- 사용자는 페이지 단위로 게시글을 조회할 수 있다.
- 페이지 번호가 없거나 유효하지 않은 경우 첫 페이지로 처리한다.

### 3.2 게시글 검색

- 사용자는 다음 조건으로 게시글을 검색할 수 있다.
    - 등록일시 범위 (선택)
    - 카테고리 (선택)
    - 검색어(제목/내용/작성자 중 일치하는 검색) **required**
- 검색 조건을 적용한 결과는 목록 조회 규칙과 동일하게 페이징 및 정렬된다.
- 검색어가 입력되지 않은 경우 `alert(검색어를 입력하세요!)` 표시
- 검색 조건은 게시글 조회, 등록, 수정, 목록 이동 시 유지된다.

### 3.3 게시글 조회

- 사용자는 특정 게시글을 조회할 수 있다.
- 사용자는 조회 페이지에서 제목을 클릭하여 조회할 수 있다.
- 상세 화면에는 다음 정보를 표시한다
    - 작성자
    - 등록일시
    - 수정일시
    - 조회수
    - 카테고리
    - 제목
    - 내용
    - 첨부파일 목록
    - 댓글 리스트 (등록일시와 내용)
- 게시글 조회 시 조회수를 1 증가시킨다.
- 조회수는 상세 페이지 진입 시마다 증가한다.

### 3.4 게시글 등록

- 사용자는 게시글을 등록할 수 있다.
- 게시글 입력 항목
    - 카테고리 (필수)
    - 작성자 (4~10자)
    - 비밀번호 (8~12자, 영문 + 숫자 조합)
    - 비밀번호 확인 (비밀번호와 동일)
    - 제목 (4~1000자)
    - 내용 (4 ~ 4000자)
    - 첨부파일 (최대 3개, 최소 0개)

#### 입력값 검증

- 입력값이 유효하지 않은 경우 저장하지 않는다.
- 검증 실패 시 오류 메시지를 표시하고 기존 입력값을 유지한다.
- 검증은 front, back 둘 다 진행한다.

### 3.5 게시글 수정

- 사용자는 기존 게시글을 수정할 수 있다.
- 게시글 수정 시 비밀번호 검증을 수행한다.
- 비밀번호가 일치하지 않으면 수정할 수 없다.
- 수정 항목
    - 제목
    - 내용
    - 첨부파일
    - 그 외의 항목들은 변경 불가능
- 게시글 수정 시 수정일시를 현재 시각으로 갱신한다.

### 3.6 게시글 삭제

- 사용자는 게시글을 삭제할 수 있다.
- 게시글 삭제 시 비밀번호 검증을 수행한다.
- 비밀번호가 일치하지 않으면 삭제할 수 없다.
- 게시글 삭제 시 게시글과 연관된 첨부파일도 함께 삭제한다.

### 3.7 첨부파일 다운로드

- 사용자는 게시물 조회 페이지에서 게시물에 있는 첨부파일을 클릭으로 다운로드할 수 있다.

### 3.8 댓글 등록

- 사용자는 게시물 조회 페이지에서 댓글을 등록할 수 있다.
- 댓글 입력 항목
    - 내용 (1 ~ 300자)

#### 입력값 검증

- 입력값이 유효하지 않은 경우 저장하지 않는다.
- 검증 실패 시 오류 메시지를 표시하고 기존 입력값을 유지한다.
- 검증은 front, back 둘 다 진행한다.

### 3.9 게시글 작성자 확인

- 사용자는 게시글 상세 조회 페이지에서 수정 또는 삭제를 시도할 수 있다.
- 수정 또는 삭제 클릭 시 비밀번호 입력 모달창을 제공한다.
- 사용자는 비밀번호를 입력하고 확인할 수 있다.
- 입력한 비밀번호가 게시글에 저장된 비밀번호와 일치하면:
    - 수정 요청 시: 수정 페이지로 이동
    - 삭제 요청 시: 게시글 삭제를 수행
- 입력한 비밀번호가 일치하지 않으면 수정/삭제 처리를 수행하지 않는다.
    - 사용자에게 "비밀번호가 일치하지 않습니다" 메시지를 표시한다.
    - 게시글 상세 화면에 그대로 머무른다.

## 4. 비기능 요구사항

### 4.1 성능

- 게시글 목록은 반드시 DB레벨에서 페이징 처리한다. (LIMIT, OFFSET)
- 단일 첨부파일 최대 크기는 2MB 이하로 제한한다.

### 4.2 보안

- 게시글 비밀번호는 평문으로 저장하지 않는다.
- 비밀번호는 MySQL SHA2() 함수를 사용해 저장한다.
- 모든 사용자 입력값은 서버에서 검증한다.
- 업로드 파일명은 서버에서 UUID로 변환하여 저장한다.
- 저장 가능한 파일의 확장자는 jpg, pdf, png로 제한한다.
- MyBatis 파라미터 바인딩을 사용하여 SQL Injection을 방지한다.
- 문자열 결합을 통한 동적 SQL 생성을 지양한다.

### 4.3 안정성/신뢰성

- 파일 업로드 실패 시 게시글 저장을 롤백한다.

### 4.4 사용성

- 입력 오류 발생 시 사용자가 이해할 수 있는 오류 메시지를 제공한다.
- 오류 메시지는 화면에 명확히 표시한다.

### 4.5 유지보수성

- Servlet, Service, DAO, Mapper 계층을 분리하여 구현한다.
- 비지니스 로직은 Servlet에 직접 작성하지 않는다.
- 공통 예외 및 공통 유틸리티는 별도의 클래스로 관리한다.
- SQL은 MyBatis Mapper를 통해 관리한다.

### 4.6 확장성

- 게시판은 카테고리, 댓글, 파일 첨부 기능 확장이 가능하도록 설계한다.

### 4.7 제약사항

#### 기술스택

- Java 17
- Tomcat 10.1.50
- Servlet + JSP
- MySQL 8.0.31
- MyBatis

### 4.8 프로젝트 구조

```
project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.board/
│   │   │       ├── controller/     # Servlet 클래스
│   │   │       ├── service/        # 비즈니스 로직
│   │   │       ├── dao/            # 데이터 접근 계층
│   │   │       ├── dto/            # 데이터 전송 객체
│   │   │       ├── util/           # 유틸리티 클래스
│   │   │       └── exception/      # 예외 클래스
│   │   ├── resources/
│   │   │   └── mybatis/
│   │   │       ├── config/         # MyBatis 설정
│   │   │       └── mapper/         # SQL Mapper XML
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml         # 웹 애플리케이션 설정
│   │       │   └── views/          # JSP 파일
│   │       ├── resources/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── uploads/            # 업로드 파일 저장
└── pom.xml / build.gradle          # 의존성 관리
```

### 4.9 구현 시 주의사항

#### 날짜 형식
- 등록일시/수정일시 표시 형식: `yyyy-MM-dd HH:mm:ss`
- 검색 시 날짜 입력 형식: `yyyy-MM-dd`

#### 페이징 처리
- 페이지 번호는 1부터 시작
- 한 번에 표시할 페이지 번호 개수: 10개
- 이전/다음 버튼 제공

#### 검색 조건 유지
- 모든 query parameter를 세션이나 hidden input으로 유지
- 페이지 이동 시에도 검색 조건이 URL에 포함되어야 함

#### 파일 업로드
- 파일 저장 경로: `/uploads/YYYY/MM/DD/` 형식으로 날짜별 폴더 생성
- UUID로 변환된 파일명 + 원본 확장자로 저장
- 파일 크기 체크는 클라이언트와 서버 양쪽에서 수행

#### 에러 처리
- 유효성 검증 실패: 400 Bad Request
- 비밀번호 불일치: 401 Unauthorized
- 게시글 없음: 404 Not Found
- 서버 오류: 500 Internal Server Error

#### 트랜잭션 처리
- 게시글 등록 시 첨부파일도 함께 등록되어야 함 (원자성 보장)
- 파일 업로드 실패 시 전체 롤백
- 게시글 삭제 시 연관된 댓글과 파일도 함께 삭제

## 5. Servlet 명세

### 5.1 게시물 목록 조회

- **URL**: `/boards`
- **Method**: GET
- **Query Params**
    - page : int (optional, default: 1) - 페이지 번호
    - category : int (optional) - 카테고리 ID
    - from : String (optional, format: yyyy-MM-dd) - 검색 시작일
    - to : String (optional, format: yyyy-MM-dd) - 검색 종료일
    - keyword : String (optional) - 검색어 (제목/내용/작성자)
- **Description**: 검색조건에 맞는 게시물 목록을 최신순으로 조회 후 목록 페이지 반환

### 5.2 게시물 상세 조회

- **URL**: `/board/view`
- **Method**: GET
- **Query Params**
    - boardId : int (required) - 게시글 ID
    - page : int (optional, default: 1) - 목록 페이지 번호 (뒤로가기용)
    - category : int (optional) - 카테고리 ID (검색조건 유지)
    - from : String (optional, format: yyyy-MM-dd) - 검색 시작일 (검색조건 유지)
    - to : String (optional, format: yyyy-MM-dd) - 검색 종료일 (검색조건 유지)
    - keyword : String (optional) - 검색어 (검색조건 유지)
- **Description**: 검색조건 유지 후 게시물 상세 조회 페이지 반환

### 5.3 게시물 작성 페이지

- **URL**: `/board/post`
- **Method**: GET
- **Query Params**
    - page : int (optional, default: 1) - 목록 페이지 번호 (뒤로가기용)
    - category : int (optional) - 카테고리 ID (검색조건 유지)
    - from : String (optional, format: yyyy-MM-dd) - 검색 시작일 (검색조건 유지)
    - to : String (optional, format: yyyy-MM-dd) - 검색 종료일 (검색조건 유지)
    - keyword : String (optional) - 검색어 (검색조건 유지)
- **Description**: 검색조건 유지 후 게시물 작성 페이지 반환

### 5.4 게시물 수정 페이지

- **URL**: `/board/edit`
- **Method**: GET
- **Query Params**
    - boardId : int (required) - 게시글 ID
    - page : int (optional, default: 1) - 목록 페이지 번호 (뒤로가기용)
    - category : int (optional) - 카테고리 ID (검색조건 유지)
    - from : String (optional, format: yyyy-MM-dd) - 검색 시작일 (검색조건 유지)
    - to : String (optional, format: yyyy-MM-dd) - 검색 종료일 (검색조건 유지)
    - keyword : String (optional) - 검색어 (검색조건 유지)
- **Description**: 검색조건 유지 후 게시물 수정 페이지 반환

### 5.5 첨부파일 다운로드

- **URL**: `/download`
- **Method**: GET
- **Query Params**
    - fileId : int (required) - 파일 ID
- **Description**: 첨부파일 다운로드

### 5.6 게시물 작성

- **URL**: `/board/post`
- **Method**: POST
- **Content-Type**: multipart/form-data
- **Request Body**
    - categoryId : int : required
    - writer : String : required
    - password : String : required
    - passwordConfirm : String : required
    - title : String : required
    - content: String : required
    - files : File[] : not required
- **Description**: 게시물 작성

### 5.7 게시물 수정

- **URL**: `/board/edit`
- **Method**: POST
- **Content-Type**: multipart/form-data
- **Request Body**
    - boardId : int : required
    - password : String : required
    - title : String : required
    - content: String : required
    - files : File[] : not required
    - deletedFileIdList : Integer[] : not required
- **Description**: 게시물 수정

### 5.8 게시물 삭제

- **URL**: `/board/delete`
- **Method**: POST
- **Content-Type**: application/json
- **Request Body**
    - boardId : int : required
    - password : String : required
- **Description**: 게시물 삭제

### 5.9 댓글 작성

- **URL**: `/comment`
- **Method**: POST
- **Content-Type**: application/json
- **Request Body**
    - boardId : int : required
    - content : String : required
- **Description**: 댓글 작성

### 5.10 작성자 확인

- **URL**: `/auth/confirm`
- **Method**: POST
- **Content-Type**: application/json
- **Request Body**
    - boardId : int : required
    - password : String : required
- **Description**: 작성자 확인

## 6. DB 설계

### 6.1 Category

| Column        | Type         | Null | Key | Default          | Description  |
|---------------|--------------|------|-----|------------------|--------------|
| category_id   | BIGINT       | NO   | PK  | AUTO_INCREMENT   | 카테고리 ID      |
| category_name | VARCHAR(20)  | NO   |     |                  | 카테고리 이름      |

**제약조건:**
- PRIMARY KEY: category_id
- UNIQUE: category_name

### 6.2 Board

| Column      | Type          | Null | Key | Default            | Description    |
|-------------|---------------|------|-----|--------------------|----------------|
| board_id    | BIGINT        | NO   | PK  | AUTO_INCREMENT     | 게시글 ID         |
| category_id | BIGINT        | NO   | FK  |                    | 카테고리 ID        |
| title       | VARCHAR(99)   | NO   |     |                    | 제목             |
| content     | VARCHAR(3999) | NO   |     |                    | 내용             |
| user_name   | VARCHAR(10)   | NO   |     |                    | 작성자            |
| password    | VARCHAR(64)   | NO   |     |                    | 비밀번호(해시)       |
| views       | BIGINT        | NO   |     | 0                  | 조회수            |
| created_at  | TIMESTAMP     | NO   |     | CURRENT_TIMESTAMP  | 등록일시           |
| edited_at   | TIMESTAMP     | YES  |     | NULL               | 수정일시           |

**제약조건:**
- PRIMARY KEY: board_id
- FOREIGN KEY: category_id REFERENCES Category(category_id)
- INDEX: category_id, created_at (검색 성능 향상)

### 6.3 Comment

| Column      | Type          | Null | Key | Default            | Description |
|-------------|---------------|------|-----|--------------------|-------------|
| comment_id  | BIGINT        | NO   | PK  | AUTO_INCREMENT     | 댓글 ID        |
| board_id    | BIGINT        | NO   | FK  |                    | 게시글 ID       |
| comment     | VARCHAR(300)  | NO   |     |                    | 댓글 내용        |
| created_at  | TIMESTAMP     | NO   |     | CURRENT_TIMESTAMP  | 등록일시         |
| edited_at   | TIMESTAMP     | YES  |     | NULL               | 수정일시         |

**제약조건:**
- PRIMARY KEY: comment_id
- FOREIGN KEY: board_id REFERENCES Board(board_id) ON DELETE CASCADE
- INDEX: board_id (조회 성능 향상)

### 6.4 File

| Column         | Type          | Null | Key | Default            | Description           |
|----------------|---------------|------|-----|--------------------|------------------------|
| file_id        | BIGINT        | NO   | PK  | AUTO_INCREMENT     | 파일 ID                  |
| board_id       | BIGINT        | NO   | FK  |                    | 게시글 ID                 |
| original_name  | VARCHAR(100)  | NO   |     |                    | 원본 파일명                 |
| physical_name  | VARCHAR(100)  | NO   |     |                    | 서버 저장 파일명 (UUID)      |
| file_path      | VARCHAR(15)   | NO   |     |                    | 저장 경로                  |
| extension      | VARCHAR(10)   | NO   |     |                    | 확장자                    |
| size           | BIGINT        | NO   |     |                    | 파일 크기(bytes)          |
| created_at     | TIMESTAMP     | NO   |     | CURRENT_TIMESTAMP  | 등록일시                   |
| edited_at      | TIMESTAMP     | YES  |     | NULL               | 수정일시                   |

**제약조건:**
- PRIMARY KEY: file_id
- FOREIGN KEY: board_id REFERENCES Board(board_id) ON DELETE CASCADE
- INDEX: board_id (조회 성능 향상)

### 6.5 초기 데이터

**Category 테이블 초기 데이터:**
```sql
INSERT INTO Category (category_name) VALUES 
('Java'),
('JavaScript'),
('Python'),
('Database'),
('기타');
```

## 7. 화면 흐름

### 7.1 전체 화면 흐름 개요

```
[게시판 목록]
├─ (검색)
├─ (페이지 이동)
├─ 제목 클릭 ─────▶ [게시글 조회]
│                   ├─ 댓글 등록
│                   ├─ 첨부파일 다운로드
│                   ├─ 수정 ──▶ [비밀번호 확인] ──▶ [게시글 수정]
│                   └─ 삭제 ──▶ [비밀번호 확인] ──▶ [게시글 목록]
└─ 글쓰기 ─────────▶ [게시글 등록] ──▶ [게시글 조회]
```

### 7.2 화면별 이동 흐름

#### 7.2.1 게시판 목록 화면

- 사용자가 `/boards`로 진입하면 게시판 목록 화면이 표시된다.
- 사용자는 다음 행동을 수행할 수 있다.
    - 검색 조건 입력 후 검색
    - 페이지 번호 클릭을 통한 페이지 이동
    - 게시글 제목 클릭을 통한 게시글 조회 화면 이동
    - 글쓰기 버튼 클릭을 통한 게시글 등록 화면 이동

#### 7.2.2 게시글 조회 화면

- 사용자는 게시판 목록에서 제목을 클릭하여 게시글 조회 화면으로 이동한다.
- 게시글 조회 화면에서는 다음 기능을 사용할 수 있다.
    - 게시글 상세 정보 확인
    - 첨부파일 다운로드
    - 댓글 등록
    - 수정 버튼 클릭
    - 삭제 버튼 클릭
- 수정 또는 삭제 버튼 클릭 시 작성자 확인 절차가 수행된다.

#### 7.2.3 게시글 등록 화면

- 사용자는 게시판 목록 화면에서 글쓰기 버튼을 클릭하여 게시글 등록 화면으로 이동한다.
- 게시글 등록 완료 후 게시글 조회 화면으로 이동한다.
- 입력값 검증 실패 시 등록 화면에 머무르며 오류 메시지를 표시한다.

#### 7.2.4 게시글 수정 화면

- 게시글 조회 화면에서 수정 버튼 클릭 후 비밀번호 검증이 성공하면 게시글 수정 화면으로 이동한다.
- 수정 완료 후 게시글 조회 화면으로 이동한다.
- 비밀번호 검증 실패 시 게시글 조회 화면에 머무른다.

## 8. 사용자 시나리오

### 8.1 게시글 목록 조회 시나리오

1. 사용자는 게시판 목록 페이지에 접속한다.
2. 시스템은 게시글 목록을 최신순으로 조회하여 표시한다.
3. 사용자는 페이지 번호를 클릭하여 다른 페이지의 게시글을 조회할 수 있다.

### 8.2 게시글 검색 시나리오

1. 사용자는 검색 조건(카테고리, 등록일시, 검색어)을 입력한다.
2. 검색 버튼을 클릭한다.
3. 시스템은 검색 조건에 맞는 게시글 목록을 조회하여 표시한다.
4. 검색어가 입력되지 않은 경우 오류 메시지를 표시한다.

### 8.3 게시글 상세 조회 시나리오

1. 사용자는 게시판 목록에서 게시글 제목을 클릭한다.
2. 시스템은 게시글 상세 정보를 조회하여 표시한다.
3. 시스템은 해당 게시글의 조회수를 1 증가시킨다.

### 8.4 게시글 등록 시나리오

1. 사용자는 게시판 목록 화면에서 글쓰기 버튼을 클릭한다.
2. 게시글 등록 화면에서 게시글 정보를 입력한다.
3. 저장 버튼을 클릭한다.
4. 입력값 검증이 성공하면 게시글을 저장한다.
5. 게시글 조회 화면으로 이동한다.
6. 입력값 검증 실패 시 등록 화면에 머무르며 오류 메시지를 표시한다.

### 8.5 게시글 수정 시나리오

1. 사용자는 게시글 조회 화면에서 수정 버튼을 클릭한다.
2. 비밀번호 입력 화면이 표시된다.
3. 사용자는 비밀번호를 입력한다.
4. 비밀번호 검증 성공 시 게시글 수정 화면으로 이동한다.
5. 수정 완료 후 게시글 조회 화면으로 이동한다.
6. 비밀번호 검증 실패 시 오류 메시지를 표시하고 게시글 조회 화면에 머무른다.

### 8.6 게시글 삭제 시나리오

1. 사용자는 게시글 조회 화면에서 삭제 버튼을 클릭한다.
2. 비밀번호 입력 화면이 표시된다.
3. 비밀번호 검증 성공 시 게시글을 삭제한다.
4. 게시글 목록 화면으로 이동한다.
5. 비밀번호 검증 실패 시 오류 메시지를 표시하고 게시글 조회 화면에 머무른다.

### 8.7 댓글 등록 시나리오

1. 사용자는 게시글 조회 화면에서 댓글 내용을 입력한다.
2. 등록 버튼을 클릭한다.
3. 입력값 검증 성공 시 댓글을 저장한다.
4. 댓글 목록에 신규 댓글이 표시된다.
5. 입력값 검증 실패 시 오류 메시지를 표시한다.