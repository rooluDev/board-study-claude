# 게시판 프로젝트 전체 개발 프로세스

> **중요**: 각 단계를 완료하고 결과를 확인한 후 다음 단계로 진행하세요.
> 한 번에 여러 단계를 건너뛰지 마세요.

---

## 📋 목차
1. [Phase 0: 프로젝트 초기 설정](#phase-0-프로젝트-초기-설정)
2. [Phase 1: 데이터베이스 설정](#phase-1-데이터베이스-설정)
3. [Phase 2: 기본 인프라 구축](#phase-2-기본-인프라-구축)
4. [Phase 3: 게시글 목록 조회](#phase-3-게시글-목록-조회)
5. [Phase 4: 게시글 상세 조회](#phase-4-게시글-상세-조회)
6. [Phase 5: 게시글 등록](#phase-5-게시글-등록)
7. [Phase 6: 게시글 수정](#phase-6-게시글-수정)
8. [Phase 7: 게시글 삭제](#phase-7-게시글-삭제)
9. [Phase 8: 댓글 기능](#phase-8-댓글-기능)
10. [Phase 9: 파일 다운로드](#phase-9-파일-다운로드)
11. [Phase 10: 검색 기능 강화](#phase-10-검색-기능-강화)
12. [Phase 11: 최종 검증](#phase-11-최종-검증)

---

## Phase 0: 프로젝트 초기 설정

### 프롬프트
```
Servlet/JSP 기반의 게시판 시스템을 구현하려고 합니다.

먼저 프로젝트 루트에 있는 다음 문서들을 읽어주세요:
1. prd.md - 전체 요구사항
2. CLAUDE.md - 개발 가이드
3. convention.md - 코딩 컨벤션

문서를 읽은 후, 다음 작업을 수행해주세요:

1. Gradle 프로젝트 생성
   - build.gradle 작성
   - settings.gradle 작성
   - Java 17, Servlet 6.0.0, JSP 3.1.0, MyBatis 3.5.13 의존성 추가

2. 프로젝트 디렉토리 구조 생성
   - CLAUDE.md의 "프로젝트 구조" 섹션을 참고
   - src/main/java, src/main/resources, src/main/webapp 등

3. web.xml 기본 설정 파일 생성

convention.md의 네이밍 규칙을 준수하고,
각 파일의 역할을 주석으로 설명해주세요.

작업 완료 후:
- 생성된 파일 목록 제공
- 다음 단계 진행 전 확인이 필요한 사항 알려주기
```

### ✅ 확인사항
- [ ] build.gradle에 모든 필수 의존성이 포함되었는가?
- [ ] 디렉토리 구조가 CLAUDE.md와 일치하는가?
- [ ] web.xml이 생성되었는가?

---

## Phase 1: 데이터베이스 설정

### 프롬프트
```
이제 데이터베이스 설정을 진행하겠습니다.
prd.md의 "6. DB 설계" 섹션을 참고하여 작업해주세요.

다음 작업을 수행해주세요:

1. DDL 스크립트 작성 (src/main/resources/db/schema.sql)
   - Category 테이블 생성
   - Board 테이블 생성
   - Comment 테이블 생성
   - File 테이블 생성
   - 모든 제약조건(PK, FK, INDEX) 포함
   - prd.md의 테이블 구조를 정확히 따를 것

2. 초기 데이터 스크립트 작성 (src/main/resources/db/data.sql)
   - Category 테이블에 기본 카테고리 3-5개 삽입
   - 테스트용 게시글 2-3개 삽입

3. MyBatis 설정 파일 작성 (src/main/resources/mybatis-config.xml)
   - CLAUDE.md의 "MyBatis 설정 가이드" 참고
   - 데이터베이스 연결 정보 설정
   - typeAliases 설정
   - mappers 경로 설정

주의사항:
- prd.md의 DB 연결 정보 사용 (localhost:3306/board_system, root/asd@1252370)
- CASCADE 사용 금지 (prd.md 4.3 참고)
- 비밀번호는 SHA2(256)로 해싱하여 저장

작업 완료 후 생성된 SQL 파일들을 제공해주세요.
```

### ✅ 확인사항
- [ ] 4개 테이블이 모두 생성되는가?
- [ ] FK 제약조건이 올바르게 설정되었는가?
- [ ] INDEX가 prd.md와 일치하는가?
- [ ] MyBatis 설정이 올바른가?

---

## Phase 2: 기본 인프라 구축

### 프롬프트
```
기본 인프라를 구축하겠습니다.
convention.md의 네이밍 규칙을 준수해주세요.

1. DTO 클래스 작성 (com.board.dto 패키지)
   - Board.java
   - Category.java
   - Comment.java
   - File.java (또는 BoardFile.java)
   - prd.md의 테이블 구조와 일치하는 필드 작성
   - Getter/Setter 메서드 포함
   - 각 필드에 Javadoc 주석 추가

2. 공통 예외 클래스 작성 (com.board.exception 패키지)
   - BoardException.java (기본 예외)
   - ValidationException.java (검증 예외)
   - FileUploadException.java (파일 업로드 예외)
   - AuthenticationException.java (인증 예외)
   - convention.md의 예외 처리 규칙 준수

3. 유틸리티 클래스 작성 (com.board.util 패키지)
   - Constants.java (상수 정의)
     - PAGE_SIZE = 10
     - MAX_FILE_SIZE = 2097152 (2MB)
     - MAX_FILE_COUNT = 3
     - ALLOWED_EXTENSIONS = jpg, pdf, png
     - UPLOAD_DIR 경로
   - ValidationUtil.java (입력값 검증)
     - validateTitle(String title) - 4~1000자
     - validateContent(String content) - 4~4000자
     - validateWriter(String writer) - 4~10자
     - validatePassword(String password) - 8~12자, 영문+숫자
     - validateCommentContent(String content) - 1~300자
   - FileUtil.java (파일 처리)
     - generateUUID() - 파일명 생성
     - getFileExtension(String filename)
     - isAllowedExtension(String extension)
     - validateFileSize(long size)

4. MyBatis SqlSessionFactory 유틸리티 작성
   - MyBatisUtil.java
   - SqlSession 생성/반환 메서드

주의사항:
- convention.md의 "4. 네이밍" 섹션 준수
- 모든 Public 클래스와 메서드에 Javadoc 작성
- ValidationUtil에서 검증 실패 시 ValidationException 던지기
- Constants는 static final 필드만 포함

작업 완료 후:
- 생성된 클래스 목록과 주요 메서드 요약 제공
```

### ✅ 확인사항
- [ ] DTO 필드가 DB 컬럼과 일치하는가?
- [ ] 검증 로직이 prd.md의 유효성 검증 규칙과 일치하는가?
- [ ] 예외 클래스가 적절히 상속 구조를 가지는가?
- [ ] 상수명이 UPPER_SNAKE_CASE인가?

---

## Phase 3: 게시글 목록 조회

### 프롬프트
```
게시글 목록 조회 기능을 구현하겠습니다.
prd.md의 "3.1 게시글 목록 조회" 섹션을 정확히 따라주세요.

다음 순서로 작업해주세요:

1. BoardMapper.xml 작성 (src/main/resources/mapper/)
   - selectBoardList 쿼리
     - 페이징 처리 (LIMIT, OFFSET)
     - 최신순 정렬 (created_at DESC)
     - 카테고리 조인하여 카테고리명 조회
     - 첨부파일 존재 여부 확인 (EXISTS 서브쿼리 또는 LEFT JOIN)
   - countBoards 쿼리
     - 전체 게시글 수 조회
   - convention.md의 SQL 규칙 준수 (#{} 파라미터 바인딩)

2. BoardDAO.java 작성 (com.board.dao 패키지)
   - selectBoardList(int page) 메서드
   - countBoards() 메서드
   - try-with-resources로 리소스 관리
   - 예외 발생 시 BoardException으로 래핑

3. BoardService.java 작성 (com.board.service 패키지)
   - getBoardList(int page) 메서드
   - getTotalPages() 메서드
   - 비즈니스 로직 처리
   - 페이지 번호 유효성 검증 (1 이상)

4. BoardListServlet.java 작성 (com.board.servlet 패키지)
   - GET /boards 처리
   - 쿼리 파라미터에서 page 추출 (기본값 1)
   - BoardService 호출
   - request attribute에 데이터 설정
   - list.jsp로 forward
   - 예외 처리 및 에러 페이지 전달

5. list.jsp 작성 (src/main/webapp/WEB-INF/views/)
   - 게시글 목록 테이블
   - 페이지네이션 (이전, 1, 2, 3..., 다음)
   - 글쓰기 버튼
   - 첨부파일 존재 시 아이콘 표시
   - convention.md의 JSP 규칙 준수

주의사항:
- CLAUDE.md의 "계층 분리 원칙" 준수
- Servlet에서 DAO 직접 호출 금지 (Service를 통해서만)
- DB 레벨 페이징 (LIMIT, OFFSET) 필수
- 10개씩 페이징 (Constants.PAGE_SIZE 사용)
- convention.md의 네이밍 규칙 준수

작업 완료 후:
- 각 계층별로 작성된 주요 메서드 설명
- 테스트 방법 안내
```

### ✅ 확인사항
- [ ] 페이징이 DB 레벨에서 처리되는가?
- [ ] Servlet → Service → DAO 계층이 분리되었는가?
- [ ] 예외 처리가 적절한가?
- [ ] 리소스가 확실히 닫히는가?

### 테스트
```
브라우저에서 http://localhost:8080/boards 접속
- 게시글 목록이 표시되는가?
- 페이징이 동작하는가?
```

---

## Phase 4: 게시글 상세 조회

### 프롬프트
```
게시글 상세 조회 기능을 구현하겠습니다.
prd.md의 "3.3 게시글 조회" 섹션을 참고해주세요.

다음 순서로 작업해주세요:

1. BoardMapper.xml에 쿼리 추가
   - selectBoardById(Long boardId)
     - 게시글 정보 조회
     - 카테고리 조인
   - updateViewCount(Long boardId)
     - 조회수 1 증가
     - views = views + 1

2. FileMapper.xml 작성
   - selectFilesByBoardId(Long boardId)
     - 해당 게시글의 첨부파일 목록 조회

3. CommentMapper.xml 작성
   - selectCommentsByBoardId(Long boardId)
     - 해당 게시글의 댓글 목록 조회
     - 등록일시 오름차순 정렬

4. FileDAO.java 작성
   - selectFilesByBoardId(Long boardId) 메서드

5. CommentDAO.java 작성
   - selectCommentsByBoardId(Long boardId) 메서드

6. BoardDAO.java에 메서드 추가
   - selectBoardById(Long boardId)
   - updateViewCount(Long boardId)

7. BoardService.java에 메서드 추가
   - getBoardById(Long boardId)
     - 게시글 조회
     - 조회수 증가
     - 첨부파일 목록 조회
     - 댓글 목록 조회
     - 게시글이 없으면 BoardException 던지기
   - FileService, CommentService와 협력

8. FileService.java 작성
   - getFilesByBoardId(Long boardId) 메서드

9. CommentService.java 작성
   - getCommentsByBoardId(Long boardId) 메서드

10. BoardViewServlet.java 작성
    - GET /board/view 처리
    - boardId 파라미터 필수 검증
    - 검색 조건 파라미터 받기 (page, category, from, to, keyword)
    - BoardService 호출
    - request attribute 설정
    - view.jsp로 forward

11. view.jsp 작성
    - 게시글 상세 정보 표시
    - 첨부파일 목록 (다운로드 링크)
    - 댓글 목록
    - 댓글 등록 폼
    - 목록/수정/삭제 버튼
    - 모든 링크에 검색 조건 파라미터 포함

주의사항:
- 조회수는 페이지 진입 시마다 증가 (prd.md 3.3 참고)
- 검색 조건 유지 (모든 링크에 파라미터 포함)
- boardId가 없거나 유효하지 않으면 예외 처리
- 게시글이 존재하지 않으면 적절한 오류 메시지

작업 완료 후:
- 각 Service의 역할 설명
- 검색 조건 유지 로직 설명
```

### ✅ 확인사항
- [ ] 조회수가 증가하는가?
- [ ] 첨부파일 목록이 표시되는가?
- [ ] 댓글 목록이 표시되는가?
- [ ] 검색 조건이 유지되는가?
- [ ] 존재하지 않는 게시글 접근 시 적절한 처리가 되는가?

### 테스트
```
1. 목록에서 게시글 클릭
2. 상세 페이지 표시 확인
3. 조회수 증가 확인
4. [목록] 버튼 클릭 시 이전 페이지로 이동 확인
```

---

## Phase 5: 게시글 등록

### 프롬프트
```
게시글 등록 기능을 구현하겠습니다.
prd.md의 "3.4 게시글 등록" 섹션을 정확히 따라주세요.

다음 순서로 작업해주세요:

1. CategoryMapper.xml 작성
   - selectAllCategories()
     - 전체 카테고리 목록 조회

2. CategoryDAO.java 작성
   - selectAllCategories() 메서드

3. CategoryService.java 작성
   - getAllCategories() 메서드

4. BoardMapper.xml에 쿼리 추가
   - insertBoard(Board board)
     - 게시글 삽입
     - useGeneratedKeys="true"로 생성된 ID 반환
     - password는 SHA2(#{password}, 256)로 해싱

5. FileMapper.xml에 쿼리 추가
   - insertFile(File file)
     - 파일 정보 삽입
   - insertFiles(List<File> files)
     - 여러 파일 배치 삽입

6. BoardDAO.java에 메서드 추가
   - insertBoard(Board board)

7. FileDAO.java에 메서드 추가
   - insertFile(File file)
   - insertFiles(List<File> files)

8. FileService.java에 메서드 추가
   - uploadFiles(List<Part> parts, Long boardId)
     - 파일 유효성 검증
       - 확장자 검증 (jpg, pdf, png만)
       - 파일 크기 검증 (2MB 이하)
       - 파일 개수 검증 (최대 3개)
     - UUID 파일명 생성
     - 물리적 파일 저장 (/uploads 디렉토리)
     - DB에 파일 정보 저장
     - 실패 시 FileUploadException 던지기

9. BoardService.java에 메서드 추가
   - createBoard(Board board, List<Part> fileParts)
     - 입력값 검증 (ValidationUtil 사용)
       - 제목: 4~1000자
       - 내용: 4~4000자
       - 작성자: 4~10자
       - 비밀번호: 8~12자, 영문+숫자 조합
     - 게시글 저장
     - 파일 업로드 (FileService 호출)
     - 트랜잭션 처리 (파일 업로드 실패 시 게시글 롤백)

10. BoardPostServlet.java 작성
    - GET /board/post
      - 카테고리 목록 조회
      - 검색 조건 파라미터 받기
      - post.jsp로 forward
    - POST /board/post
      - multipart/form-data 파싱
      - 입력값 받기 (categoryId, writer, password, passwordConfirm, title, content, files)
      - 비밀번호 확인 검증 (password == passwordConfirm)
      - BoardService.createBoard() 호출
      - 성공 시 상세 페이지로 redirect
      - 실패 시 오류 메시지와 함께 post.jsp로 forward

11. post.jsp 작성
    - 게시글 등록 폼
      - 카테고리 선택 (드롭다운)
      - 작성자 입력
      - 비밀번호 입력
      - 비밀번호 확인 입력
      - 제목 입력
      - 내용 입력 (textarea)
      - 파일 첨부 (multiple, 최대 3개)
    - JavaScript 검증
      - 실시간 글자 수 표시
      - submit 전 검증
      - 비밀번호 일치 확인
      - 파일 개수/크기/확장자 검증
    - 오류 메시지 표시
    - 검증 실패 시 입력값 유지

주의사항:
- 프론트엔드와 백엔드 양쪽에서 검증 (prd.md 3.4 참고)
- 비밀번호는 SHA2(256)로 해싱 저장
- 파일 업로드 실패 시 게시글 저장 롤백 (트랜잭션)
- 파일명은 UUID로 변환, 원본 파일명은 DB에 별도 저장
- multipart/form-data 처리 (@MultipartConfig 어노테이션)

작업 완료 후:
- 파일 업로드 로직 설명
- 트랜잭션 처리 방식 설명
- 검증 로직 설명
```

### ✅ 확인사항
- [ ] 카테고리 선택이 가능한가?
- [ ] 입력값 검증이 프론트/백엔드 모두에서 동작하는가?
- [ ] 비밀번호가 해싱되어 저장되는가?
- [ ] 파일이 업로드되는가?
- [ ] 파일 업로드 실패 시 롤백되는가?
- [ ] 검증 실패 시 입력값이 유지되는가?

### 테스트
```
1. [글쓰기] 버튼 클릭
2. 모든 필드 입력 (파일 첨부 포함)
3. 등록 성공 확인
4. 상세 페이지에서 첨부파일 확인
5. DB에서 비밀번호 해싱 확인
```

---

## Phase 6: 게시글 수정

### 프롬프트
```
게시글 수정 기능을 구현하겠습니다.
prd.md의 "3.5 게시글 수정"과 "3.9 게시글 작성자 확인" 섹션을 참고해주세요.

다음 순서로 작업해주세요:

1. BoardMapper.xml에 쿼리 추가
   - checkPassword(Long boardId, String password)
     - 비밀번호 확인 (SHA2로 해싱하여 비교)
     - SELECT COUNT(*) FROM board WHERE board_id = #{boardId} AND password = SHA2(#{password}, 256)
   - updateBoard(Board board)
     - 제목, 내용 수정
     - edited_at 현재 시각으로 갱신

2. FileMapper.xml에 쿼리 추가
   - deleteFile(Long fileId)
     - 파일 정보 삭제
   - deleteFilesByIds(List<Long> fileIds)
     - 여러 파일 정보 삭제
   - selectFileById(Long fileId)
     - 파일 정보 조회 (삭제 전 물리 파일명 확인용)

3. BoardDAO.java에 메서드 추가
   - checkPassword(Long boardId, String password)
     - 비밀번호 일치 여부 반환 (boolean)
   - updateBoard(Board board)

4. FileDAO.java에 메서드 추가
   - deleteFile(Long fileId)
   - deleteFilesByIds(List<Long> fileIds)
   - selectFileById(Long fileId)

5. FileService.java에 메서드 추가
   - deleteFiles(List<Long> fileIds)
     - DB에서 파일 정보 조회
     - 물리적 파일 삭제
     - DB에서 파일 정보 삭제
   - updateFiles(Long boardId, List<Part> newFiles, List<Long> deletedFileIds)
     - 기존 파일 삭제
     - 새 파일 업로드
     - 총 파일 개수 검증 (최대 3개)

6. BoardService.java에 메서드 추가
   - checkPassword(Long boardId, String password)
     - 비밀번호 확인
     - 불일치 시 AuthenticationException 던지기
   - updateBoard(Board board, List<Part> newFiles, List<Long> deletedFileIds)
     - 입력값 검증
     - 비밀번호 확인
     - 게시글 수정
     - 파일 업데이트 (FileService 호출)
     - 트랜잭션 처리

7. AuthServlet.java 작성
   - POST /auth/confirm
     - Content-Type: application/json
     - boardId, password 받기
     - BoardService.checkPassword() 호출
     - JSON 응답 반환
       - {"success": true/false, "message": "..."}

8. BoardEditServlet.java 작성
   - GET /board/edit
     - boardId 파라미터 받기
     - 검색 조건 파라미터 받기
     - 게시글 정보 조회
     - 첨부파일 목록 조회
     - edit.jsp로 forward
   - POST /board/edit
     - multipart/form-data 파싱
     - boardId, password, title, content, files, deletedFileIdList 받기
     - BoardService.updateBoard() 호출
     - 성공 시 상세 페이지로 redirect (검색 조건 유지)
     - 실패 시 오류 메시지와 함께 edit.jsp로 forward

9. edit.jsp 작성
    - 게시글 수정 폼
      - 제목 입력 (기존 값 표시)
      - 내용 입력 (기존 값 표시)
      - 기존 첨부파일 목록 (삭제 체크박스)
      - 새 파일 추가 (multiple)
    - JavaScript 검증
      - 총 파일 개수 검증 (기존 - 삭제 + 새 파일 <= 3)
      - 제목/내용 검증
    - 수정 불가 필드 표시 (카테고리, 작성자, 등록일시)

10. view.jsp 수정
    - [수정] 버튼에 비밀번호 확인 모달 추가
    - JavaScript로 모달 처리
      - 비밀번호 입력 모달 표시
      - AJAX로 /auth/confirm 호출
      - 성공 시 /board/edit로 이동
      - 실패 시 오류 메시지 표시

주의사항:
- 비밀번호 검증은 AJAX로 처리 (prd.md 3.9 참고)
- 검색 조건 유지
- 파일 개수는 기존 + 새 파일 - 삭제 파일 <= 3
- 카테고리, 작성자, 비밀번호, 등록일시, 조회수는 수정 불가
- edited_at 자동 갱신
- 트랜잭션 처리

작업 완료 후:
- 비밀번호 확인 프로세스 설명
- 파일 수정 로직 설명
- 검색 조건 유지 방식 설명
```

### ✅ 확인사항
- [ ] 비밀번호 확인 모달이 동작하는가?
- [ ] 비밀번호 불일치 시 수정이 불가능한가?
- [ ] 기존 파일 삭제가 되는가?
- [ ] 새 파일 추가가 되는가?
- [ ] 파일 개수 제한(3개)이 지켜지는가?
- [ ] 수정일시가 갱신되는가?
- [ ] 검색 조건이 유지되는가?

### 테스트
```
1. 게시글 상세 페이지에서 [수정] 클릭
2. 비밀번호 입력 (정확한 비밀번호)
3. 제목/내용 수정
4. 기존 파일 삭제, 새 파일 추가
5. 수정 성공 확인
6. 상세 페이지에서 변경사항 확인
```

---

## Phase 7: 게시글 삭제

### 프롬프트
```
게시글 삭제 기능을 구현하겠습니다.
prd.md의 "3.6 게시글 삭제" 섹션을 참고해주세요.

다음 순서로 작업해주세요:

1. CommentMapper.xml에 쿼리 추가
   - deleteCommentsByBoardId(Long boardId)
     - 해당 게시글의 모든 댓글 삭제

2. FileMapper.xml에 쿼리 추가
   - deleteFilesByBoardId(Long boardId)
     - 해당 게시글의 모든 파일 정보 삭제

3. BoardMapper.xml에 쿼리 추가
   - deleteBoard(Long boardId)
     - 게시글 삭제

4. CommentDAO.java에 메서드 추가
   - deleteCommentsByBoardId(Long boardId)

5. FileDAO.java에 메서드 추가
   - deleteFilesByBoardId(Long boardId)

6. BoardDAO.java에 메서드 추가
   - deleteBoard(Long boardId)

7. FileService.java에 메서드 추가
   - deleteAllFilesByBoardId(Long boardId)
     - 게시글의 모든 파일 정보 조회
     - 물리적 파일 삭제
     - DB에서 파일 정보 삭제

8. BoardService.java에 메서드 추가
   - deleteBoard(Long boardId, String password)
     - 비밀번호 확인 (checkPassword 재사용)
     - 삭제 순서 (prd.md 3.6 참고):
       1. 댓글 삭제 (CommentDAO)
       2. 첨부파일 정보 삭제 (FileDAO)
       3. 물리적 파일 삭제 (FileService)
       4. 게시글 삭제 (BoardDAO)
     - 트랜잭션 처리 (하나라도 실패하면 전체 롤백)
     - CASCADE 사용 금지

9. BoardDeleteServlet.java 작성
   - POST /board/delete
     - Content-Type: application/json
     - boardId, password 받기
     - BoardService.deleteBoard() 호출
     - JSON 응답 반환
       - {"success": true/false, "message": "...", "redirectUrl": "/boards"}

10. view.jsp 수정
    - [삭제] 버튼에 비밀번호 확인 모달 추가
    - JavaScript로 모달 처리
      - 비밀번호 입력 모달 표시
      - AJAX로 /auth/confirm 호출
      - 성공 시 AJAX로 /board/delete 호출
      - 삭제 성공 시 목록 페이지로 이동 (검색 조건 유지 X)
      - 실패 시 오류 메시지 표시

주의사항:
- 삭제 순서 엄수: 댓글 → 파일(DB) → 파일(물리) → 게시글
- CASCADE 사용 금지 (명시적 삭제)
- 비밀번호 검증 필수
- 트랜잭션 처리 (하나라도 실패하면 전체 롤백)
- 삭제 성공 시 검색 조건 유지하지 않음 (prd.md 3.6 참고)

작업 완료 후:
- 삭제 프로세스 설명
- 트랜잭션 처리 방식 설명
```

### ✅ 확인사항
- [ ] 비밀번호 확인이 되는가?
- [ ] 댓글이 함께 삭제되는가?
- [ ] 첨부파일이 함께 삭제되는가?
- [ ] 물리적 파일이 삭제되는가?
- [ ] 게시글이 삭제되는가?
- [ ] 삭제 후 목록으로 이동하는가?

### 테스트
```
1. 게시글 상세 페이지에서 [삭제] 클릭
2. 비밀번호 입력
3. 삭제 확인
4. 목록 페이지로 이동 확인
5. 해당 게시글이 목록에 없는지 확인
6. DB에서 댓글, 파일도 삭제되었는지 확인
7. 서버의 uploads 폴더에서 물리 파일 삭제 확인
```

---

## Phase 8: 댓글 기능

### 프롬프트
```
댓글 등록 기능을 구현하겠습니다.
prd.md의 "3.8 댓글 등록" 섹션을 참고해주세요.

다음 순서로 작업해주세요:

1. CommentMapper.xml에 쿼리 추가
   - insertComment(Comment comment)
     - 댓글 삽입
     - useGeneratedKeys="true"로 생성된 ID 반환

2. CommentDAO.java에 메서드 추가
   - insertComment(Comment comment)

3. CommentService.java에 메서드 추가
   - createComment(Comment comment)
     - 입력값 검증 (ValidationUtil.validateCommentContent)
       - 1~300자
     - 댓글 저장
     - 검증 실패 시 ValidationException 던지기

4. CommentServlet.java 작성
   - POST /comment
     - Content-Type: application/json
     - boardId, content 받기
     - CommentService.createComment() 호출
     - JSON 응답 반환
       - {"success": true/false, "message": "...", "comment": {...}}

5. view.jsp 수정
   - 댓글 등록 폼 추가
     - textarea (1~300자)
     - 글자 수 표시
     - [등록] 버튼
   - JavaScript로 댓글 등록 처리
     - AJAX로 /comment POST 요청
     - 성공 시 댓글 목록에 새 댓글 추가 (페이지 새로고침 또는 동적 추가)
     - 실패 시 오류 메시지 표시
   - 댓글 목록 영역
     - 등록일시, 내용 표시
     - 최신 댓글이 하단에 표시

주의사항:
- 프론트엔드와 백엔드 양쪽에서 검증
- AJAX로 처리
- 등록 성공 시 댓글 목록 갱신 (페이지 새로고침 또는 DOM 조작)
- 등록일시 포맷팅 (YYYY-MM-DD HH:mm:ss)

작업 완료 후:
- AJAX 처리 방식 설명
- 댓글 목록 갱신 방법 설명
```

### ✅ 확인사항
- [ ] 댓글이 등록되는가?
- [ ] 입력값 검증이 동작하는가?
- [ ] 등록 후 댓글 목록이 갱신되는가?
- [ ] 글자 수 제한이 지켜지는가?

### 테스트
```
1. 게시글 상세 페이지에서 댓글 입력
2. [등록] 버튼 클릭
3. 댓글 목록에 새 댓글 표시 확인
4. DB에서 댓글 저장 확인
```

---

## Phase 9: 파일 다운로드

### 프롬프트
```
첨부파일 다운로드 기능을 구현하겠습니다.
prd.md의 "3.7 첨부파일 다운로드" 섹션을 참고해주세요.

다음 순서로 작업해주세요:

1. FileService.java에 메서드 추가
   - downloadFile(Long fileId)
     - 파일 정보 조회 (FileDAO 호출)
     - 물리적 파일 존재 여부 확인
     - 파일이 없으면 FileUploadException 던지기
     - File 객체 반환

2. FileDownloadServlet.java 작성
   - GET /download
     - fileId 쿼리 파라미터 받기 (필수)
     - FileService.downloadFile() 호출
     - 파일 읽기 (InputStream)
     - Content-Type: application/octet-stream 설정
     - Content-Disposition 헤더 설정
       - attachment; filename*=UTF-8''인코딩된파일명
       - 한글 파일명 처리 (URLEncoder 사용)
     - 파일 바이너리 데이터 스트리밍
     - 예외 처리

3. view.jsp 확인
   - 첨부파일 링크가 /download?fileId={fileId} 형태인지 확인
   - 원본 파일명 표시

주의사항:
- 한글 파일명 처리 (URLEncoder)
- Content-Disposition 헤더 올바르게 설정
- 파일 스트리밍 (한 번에 메모리에 로드하지 않기)
- 리소스 확실히 닫기 (try-with-resources)

작업 완료 후:
- 파일 다운로드 처리 방식 설명
- 한글 파일명 인코딩 방법 설명
```

### ✅ 확인사항
- [ ] 파일 다운로드가 되는가?
- [ ] 원본 파일명으로 다운로드되는가?
- [ ] 한글 파일명이 깨지지 않는가?
- [ ] 파일이 없을 때 적절한 오류 메시지가 표시되는가?

### 테스트
```
1. 게시글 상세 페이지에서 첨부파일 클릭
2. 파일 다운로드 시작 확인
3. 원본 파일명으로 저장되는지 확인
4. 한글 파일명 테스트
```

---

## Phase 10: 검색 기능 강화

### 프롬프트
```
검색 기능을 강화하겠습니다.
prd.md의 "3.2 게시글 검색" 섹션을 참고해주세요.

다음 순서로 작업해주세요:

1. BoardMapper.xml 수정
   - selectBoardList 쿼리에 동적 SQL 추가
     - <where> 태그 사용
     - category 조건 (<if test="category != null">)
     - keyword 조건 (<if test="keyword != null">)
       - title LIKE CONCAT('%', #{keyword}, '%')
       - OR content LIKE CONCAT('%', #{keyword}, '%')
       - OR user_name LIKE CONCAT('%', #{keyword}, '%')
     - from/to 날짜 범위 조건
       - created_at >= #{from}
       - AND created_at <= #{to}
   - countBoards 쿼리도 동일한 조건 추가

2. BoardDAO.java 수정
   - selectBoardList 메서드 파라미터 추가
     - int page, Integer category, String from, String to, String keyword
   - countBoards 메서드 파라미터 추가
     - Integer category, String from, String to, String keyword

3. BoardService.java 수정
   - getBoardList 메서드 파라미터 추가
   - getTotalPages 메서드 파라미터 추가

4. BoardListServlet.java 수정
   - 검색 파라미터 받기
     - category, from, to, keyword
   - keyword가 비어있으면 alert 표시 (프론트엔드에서 처리)
   - 검색 조건을 request attribute에 설정 (JSP에서 사용)

5. list.jsp 수정
   - 검색 폼 추가
     - 카테고리 선택 (전체/Java/Spring 등)
     - 등록일시 범위 (from ~ to, input type="date")
     - 검색어 입력 (필수)
     - [검색] 버튼
   - JavaScript 검증
     - 검색어 없으면 alert("검색어를 입력하세요!")
   - 검색 조건 표시 (현재 검색 중인 조건)
   - 페이지네이션 링크에 검색 조건 포함
   - 게시글 제목 링크에 검색 조건 포함
   - [글쓰기] 버튼 링크에 검색 조건 포함

6. post.jsp 수정
   - [목록] 버튼에 검색 조건 포함

7. view.jsp 수정
   - [목록] 버튼에 검색 조건 포함

주의사항:
- 검색어가 없으면 검색 수행하지 않음 (alert 표시)
- 검색 조건은 모든 페이지 이동 시 유지
- MyBatis 동적 SQL (<where>, <if>) 사용
- SQL Injection 방지 (#{} 바인딩)

작업 완료 후:
- 동적 SQL 작성 방법 설명
- 검색 조건 유지 메커니즘 설명
```

### ✅ 확인사항
- [ ] 카테고리별 검색이 되는가?
- [ ] 날짜 범위 검색이 되는가?
- [ ] 키워드 검색이 되는가? (제목/내용/작성자)
- [ ] 검색어 없이 검색 시 alert가 표시되는가?
- [ ] 검색 조건이 모든 페이지 이동 시 유지되는가?

### 테스트
```
1. 검색 조건 입력 (카테고리: Java, 검색어: "테스트")
2. 검색 결과 확인
3. 게시글 클릭 → 상세 페이지
4. [목록] 클릭 → 검색 조건 유지 확인
5. 페이지 이동 → 검색 조건 유지 확인
```

---

## Phase 11: 최종 검증

### 프롬프트
```
전체 기능을 검증하고 마무리하겠습니다.

다음 작업을 수행해주세요:

1. 에러 페이지 작성 (error.jsp)
   - 에러 메시지 표시
   - [목록으로] 버튼

2. 전체 예외 처리 점검
   - 모든 Servlet에서 예외 처리 확인
   - 적절한 에러 메시지 표시
   - 리소스 누수 확인 (try-with-resources)

3. 로깅 추가
   - 주요 작업에 로그 추가
   - logger.info(), logger.error() 사용
   - 민감 정보(비밀번호) 로깅 금지

4. 코드 리뷰
   - convention.md 준수 여부 확인
   - 네이밍 규칙 확인
   - Javadoc 누락 확인
   - 주석 확인

5. 통합 테스트 시나리오 작성
   - 게시글 등록 → 조회 → 댓글 작성 → 수정 → 삭제
   - 파일 업로드 → 다운로드
   - 검색 → 페이징

6. README.md 작성
   - 프로젝트 소개
   - 기술 스택
   - 실행 방법
   - 데이터베이스 설정
   - 주요 기능

작업 완료 후:
- 테스트 시나리오 결과 보고
- 개선이 필요한 부분 제안
- 완료된 기능 목록 제공
```

### ✅ 최종 체크리스트
- [ ] 모든 기능이 동작하는가?
- [ ] 예외 처리가 적절한가?
- [ ] 보안 요구사항이 충족되는가?
- [ ] 코딩 컨벤션이 준수되었는가?
- [ ] 문서화가 완료되었는가?

---

## 📝 각 단계별 진행 방법

### 1. 단계 시작 전
- 해당 Phase의 프롬프트를 Claude에게 전달
- prd.md의 관련 섹션 확인
- convention.md의 관련 규칙 확인

### 2. 단계 진행 중
- Claude의 응답 확인
- 생성된 코드 검토
- 의문점이 있으면 즉시 질문

### 3. 단계 완료 후
- ✅ 확인사항 체크
- 테스트 수행
- 문제가 있으면 수정 요청
- 문제 없으면 다음 단계로 진행

### 4. 문제 발생 시
```
[오류 메시지 또는 현상]

prd.md의 [관련 섹션]과 convention.md의 [관련 규칙]을 
참고하여 문제를 해결해주세요.

기대 동작: ...
실제 동작: ...
```

---

## 🎯 주요 원칙 (항상 기억하기)

1. **한 번에 하나씩**: 각 Phase를 완료하고 검증 후 다음 단계
2. **문서 우선**: prd.md와 convention.md를 항상 참고
3. **계층 분리**: Servlet → Service → DAO 계층 엄수
4. **검증 철저**: 프론트엔드와 백엔드 양쪽에서 검증
5. **예외 처리**: 모든 예외를 적절히 처리
6. **리소스 관리**: try-with-resources로 확실히 닫기
7. **보안 준수**: SQL Injection, 비밀번호 해싱 등
8. **검색 조건 유지**: 모든 페이지 이동 시 파라미터 포함

---

## 🚀 시작하기

첫 번째 Phase 0의 프롬프트를 복사하여 Claude에게 전달하세요!

```
Servlet/JSP 기반의 게시판 시스템을 구현하려고 합니다.

먼저 프로젝트 루트에 있는 다음 문서들을 읽어주세요:
1. prd.md - 전체 요구사항
2. CLAUDE.md - 개발 가이드
3. convention.md - 코딩 컨벤션

문서를 읽은 후, 다음 작업을 수행해주세요:
...
```