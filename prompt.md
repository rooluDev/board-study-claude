# Claude 프로젝트 진행 프롬프트 가이드

이 문서는 Servlet/JSP 게시판 프로젝트를 Claude와 함께 효과적으로 진행하기 위한 프롬프트 모음입니다.

---

## 🎯 프로젝트 시작 시 필수 컨텍스트 제공 프롬프트

```
나는 Servlet/JSP 기반 게시판 시스템을 개발하고 있습니다.

프로젝트 문서:
- prd.md: 전체 요구사항 및 API 명세
- convention.md: 코딩 컨벤션 및 네이밍 규칙
- CLAUDE.md: 개발 가이드 및 단계별 진행 방법

기술 스택:
- Java 17, Servlet 6.0, JSP 3.1.0, MyBatis 3.5.13
- MySQL 8.0.31, Tomcat 10.1.50
- Spring Framework 사용 금지

주요 제약사항:
1. Spring, JPA, JSTL 사용 불가
2. 계층 분리 필수: Servlet → Service → DAO → Mapper
3. 비밀번호는 SHA2()로 해시화
4. SQL Injection 방지 (#{} 사용)
5. CASCADE 사용 금지 (명시적 삭제)

지금부터 [구체적인 작업 요청]을 진행하겠습니다.
```

---

## 📋 단계별 개발 프롬프트

### Phase 1: 프로젝트 초기 설정

```
Phase 1 - 프로젝트 초기 설정을 시작합니다.

작업 목록:
1. Gradle 프로젝트 생성 (build.gradle, settings.gradle)
2. 디렉토리 구조 생성 (prd.md의 프로젝트 구조 참고)
3. MySQL 데이터베이스 스키마 생성 (DDL)
4. MyBatis 설정 파일 작성 (mybatis-config.xml)

요구사항:
- convention.md의 코딩 컨벤션 준수
- CLAUDE.md의 MyBatis 설정 가이드 참조
- prd.md 6장의 DB 설계 참조

먼저 build.gradle부터 작성해주세요.
```

### Phase 2: 기본 인프라 구현

```
Phase 2 - 기본 인프라를 구현합니다.

작업 목록:
1. 공통 예외 클래스 작성
   - BoardException, ValidationException, FileUploadException
2. 유틸리티 클래스 작성
   - FileUtil (파일 처리), ValidationUtil (검증)
3. DTO 클래스 작성
   - Board, File, Comment, Category
4. MyBatis SqlSessionFactory 설정

각 클래스는 convention.md의 네이밍 규칙을 따르고,
CLAUDE.md의 계층 분리 원칙을 준수해주세요.

먼저 예외 클래스부터 작성해주세요.
```

### Phase 3: 게시글 목록 기능

```
Phase 3 - 게시글 목록 조회 기능을 구현합니다.

API 명세 (prd.md 5.1 참조):
- URL: GET /boards
- Parameters: page, category, from, to, keyword

구현 순서:
1. BoardMapper.xml - selectBoardList 쿼리 작성
2. BoardDAO - selectBoardList 메서드
3. BoardService - getBoardList 메서드
4. BoardListServlet - doGet 메서드
5. list.jsp - 목록 화면

중요 사항:
- 페이징은 DB 레벨에서 LIMIT, OFFSET 사용
- convention.md의 네이밍 규칙 준수
- SQL Injection 방지 (#{} 사용)

먼저 BoardMapper.xml의 selectBoardList 쿼리부터 작성해주세요.
```

### Phase 4: 게시글 조회 기능

```
Phase 4 - 게시글 상세 조회 기능을 구현합니다.

API 명세 (prd.md 5.2 참조):
- URL: GET /board/view
- Parameters: boardId, page, category, from, to, keyword

구현 순서:
1. BoardMapper.xml - selectBoardById, updateViewCount 쿼리
2. FileMapper.xml - selectFilesByBoardId 쿼리
3. CommentMapper.xml - selectCommentsByBoardId 쿼리
4. BoardDAO, FileDAO, CommentDAO 메서드 구현
5. BoardService - getBoardById 메서드 (트랜잭션 관리)
6. BoardViewServlet - doGet 메서드
7. view.jsp - 상세 화면

중요 사항:
- 조회 시 조회수 1 증가
- 검색 조건 유지 (모든 링크에 파라미터 포함)
- 첨부파일, 댓글 함께 조회

먼저 BoardMapper.xml부터 작성해주세요.
```

### Phase 5: 게시글 등록 기능

```
Phase 5 - 게시글 등록 기능을 구현합니다.

API 명세 (prd.md 5.4 참조):
- URL: GET/POST /board/post
- Parameters: categoryId, writer, password, title, content, files

구현 순서:
1. BoardMapper.xml - insertBoard 쿼리
2. FileMapper.xml - insertFile 쿼리
3. BoardDAO, FileDAO 메서드 구현
4. FileService - uploadFiles 메서드 (파일 저장 로직)
5. BoardService - createBoard 메서드 (트랜잭션)
6. BoardPostServlet - doGet (폼), doPost (저장)
7. post.jsp - 등록 화면

유효성 검증 (prd.md 3.4 참조):
- 프론트엔드: JavaScript로 실시간 검증
- 백엔드: ValidationUtil로 재검증
- 실패 시: 오류 메시지 + 입력값 유지

파일 업로드 제약:
- 최대 3개, 각 2MB 이하
- 확장자: jpg, pdf, png만 허용
- UUID로 저장, 원본명은 DB에 저장

먼저 ValidationUtil부터 작성해주세요.
```

### Phase 6: 게시글 수정 기능

```
Phase 6 - 게시글 수정 기능을 구현합니다.

API 명세 (prd.md 5.5 참조):
- URL: GET/POST /board/edit
- Parameters: boardId, password, title, content, files, deletedFileIdList

구현 순서:
1. AuthServlet - POST /auth/confirm (비밀번호 확인)
2. BoardMapper.xml - updateBoard, verifyPassword 쿼리
3. FileMapper.xml - deleteFile 쿼리
4. BoardDAO, FileDAO 메서드 구현
5. FileService - updateFiles, deleteFiles 메서드
6. BoardService - updateBoard 메서드 (트랜잭션)
7. BoardEditServlet - doGet (수정 폼), doPost (저장)
8. edit.jsp - 수정 화면

중요 사항:
- 비밀번호 검증: SHA2()로 해시 비교
- 파일 수정: 기존 + 신규 최대 3개
- 수정일시 자동 갱신
- 검색 조건 유지

먼저 AuthServlet의 비밀번호 확인 기능부터 구현해주세요.
```

### Phase 7: 게시글 삭제 기능

```
Phase 7 - 게시글 삭제 기능을 구현합니다.

API 명세 (prd.md 5.6 참조):
- URL: POST /board/delete
- Parameters: boardId, password

구현 순서:
1. BoardMapper.xml - deleteBoard 쿼리
2. FileMapper.xml - deleteFilesByBoardId 쿼리
3. CommentMapper.xml - deleteCommentsByBoardId 쿼리
4. BoardDAO, FileDAO, CommentDAO 메서드 구현
5. FileService - deletePhysicalFiles 메서드
6. BoardService - deleteBoard 메서드 (트랜잭션)
7. BoardDeleteServlet - doPost 메서드

삭제 순서 (CLAUDE.md 참조):
1. 댓글 삭제 (comment 테이블)
2. 파일 정보 삭제 (file 테이블)
3. 물리 파일 삭제 (디스크)
4. 게시글 삭제 (board 테이블)

중요 사항:
- CASCADE 사용 금지
- 트랜잭션으로 일관성 보장
- 실패 시 롤백

먼저 BoardService의 deleteBoard 메서드 구현해주세요.
```

### Phase 8: 댓글 기능

```
Phase 8 - 댓글 등록 기능을 구현합니다.

API 명세 (prd.md 5.9 참조):
- URL: POST /comment
- Parameters: boardId, content

구현 순서:
1. CommentMapper.xml - insertComment 쿼리
2. CommentDAO - insertComment 메서드
3. CommentService - createComment 메서드
4. CommentServlet - doPost 메서드 (AJAX 처리)
5. view.jsp에 댓글 등록 폼 및 AJAX 스크립트 추가

유효성 검증:
- 내용: 1~300자
- 프론트엔드 + 백엔드 모두 검증

AJAX 응답 형식:
{
  "success": true,
  "comment": {
    "commentId": 1,
    "content": "댓글 내용",
    "createdAt": "2025-01-10 10:30"
  }
}

먼저 CommentServlet부터 작성해주세요.
```

### Phase 9: 파일 다운로드

```
Phase 9 - 파일 다운로드 기능을 구현합니다.

API 명세 (prd.md 5.8 참조):
- URL: GET /download
- Parameters: fileId

구현 순서:
1. FileMapper.xml - selectFileById 쿼리 (이미 있으면 생략)
2. FileDAO - selectFileById 메서드 (이미 있으면 생략)
3. FileService - downloadFile 메서드
4. FileDownloadServlet - doGet 메서드

구현 세부사항:
- Content-Type 설정 (확장자별)
- Content-Disposition: attachment; filename="원본파일명"
- 파일 스트림 읽기 및 응답 출력
- 예외 처리 (파일 없음, 읽기 오류)

먼저 FileDownloadServlet을 작성해주세요.
```

### Phase 10: 검색 기능

```
Phase 10 - 검색 기능을 구현합니다.

요구사항 (prd.md 3.2 참조):
- 검색 조건: category, from, to, keyword
- keyword는 필수 (없으면 alert)
- 제목, 내용, 작성자 중 검색

구현 내용:
1. list.jsp에 검색 폼 추가
2. BoardMapper.xml의 selectBoardList에 동적 쿼리 추가
   - <if> 태그로 조건부 WHERE 절
3. 검색 조건 유지 로직
   - 모든 링크와 폼에 파라미터 포함

MyBatis 동적 SQL 예시:
<select id="selectBoardList">
  SELECT * FROM board
  <where>
    <if test="category != null">
      AND category_id = #{category}
    </if>
    <if test="keyword != null">
      AND (title LIKE CONCAT('%', #{keyword}, '%')
           OR content LIKE CONCAT('%', #{keyword}, '%')
           OR user_name LIKE CONCAT('%', #{keyword}, '%'))
    </if>
  </where>
  ORDER BY created_at DESC
  LIMIT #{limit} OFFSET #{offset}
</select>

먼저 BoardMapper.xml을 수정해주세요.
```

---

## 🐛 디버깅 및 오류 해결 프롬프트

### 빌드 오류

```
프로젝트를 빌드하는 중 다음 오류가 발생했습니다:

[오류 메시지 붙여넣기]

문제 해결을 위해 다음을 확인해주세요:
1. build.gradle의 의존성 설정
2. Java 버전 (17 사용 중)
3. Gradle 버전 호환성
```

### MyBatis 오류

```
MyBatis에서 다음 오류가 발생했습니다:

[오류 메시지 붙여넣기]

확인 사항:
1. mybatis-config.xml 설정
2. Mapper XML 문법
3. DTO 필드명과 DB 컬럼명 매핑
4. SQL 쿼리 문법

CLAUDE.md의 MyBatis 설정 가이드를 참고하여 해결해주세요.
```

### 파일 업로드 오류

```
파일 업로드 시 다음 문제가 발생합니다:

[문제 설명]

확인 사항:
1. 업로드 디렉토리 경로 (./src/webapp/uploads)
2. 파일 권한 설정
3. 파일 크기 제한 (2MB)
4. 확장자 검증 로직
5. UUID 파일명 변환

FileService 코드를 검토하고 수정해주세요.
```

### SQL Injection 검토

```
다음 코드에서 SQL Injection 취약점이 있는지 검토해주세요:

[코드 붙여넣기]

확인 사항:
- #{} vs ${} 사용
- 동적 ORDER BY 절 처리
- LIKE 절 처리
- IN 절 처리

CLAUDE.md의 SQL Injection 방지 섹션 참고
```

---

## 🧪 테스트 프롬프트

### 단위 테스트

```
[클래스명]의 단위 테스트를 작성해주세요.

테스트 케이스:
1. 정상 케이스: [설명]
2. 예외 케이스: [설명]
3. 경계값 케이스: [설명]

사용 라이브러리:
- JUnit 5
- Mockito (필요 시)

CLAUDE.md의 테스트 가이드 참조
```

### 통합 테스트 시나리오

```
다음 시나리오를 테스트해주세요:

시나리오: [prd.md 8장의 시나리오]

테스트 단계:
1. [단계 1]
2. [단계 2]
3. [단계 3]

확인 사항:
- 각 단계의 예상 결과
- 오류 발생 시 처리
- 데이터 일관성
```

---

## 🔄 리팩토링 프롬프트

### 코드 리뷰

```
다음 코드를 리뷰하고 개선점을 제안해주세요:

[코드 붙여넣기]

리뷰 관점:
1. convention.md의 코딩 컨벤션 준수
2. CLAUDE.md의 계층 분리 원칙
3. 예외 처리 적절성
4. 성능 최적화 가능성
5. 보안 취약점
```

### 성능 개선

```
게시글 목록 조회 성능을 개선하고 싶습니다.

현재 상황:
- 응답 시간: [현재 시간]
- 목표: 2초 이내

확인 사항:
1. DB 인덱스 사용
2. 페이징 쿼리 최적화
3. N+1 쿼리 문제
4. Connection Pool 설정

prd.md 4.1의 성능 요구사항 참조
```

---

## 💡 일반적인 질문 프롬프트

### 기술 선택 질문

```
[구체적인 상황 설명]

다음 중 어떤 방식이 더 적합할까요?
1. [방법 1]
2. [방법 2]

고려 사항:
- prd.md의 요구사항
- CLAUDE.md의 설계 원칙
- convention.md의 코딩 컨벤션
```

### 설계 검증

```
다음과 같이 설계하려고 하는데 적절한지 검토해주세요:

[설계 내용]

확인 사항:
1. 계층 분리 적절성
2. 책임 분담 명확성
3. 확장 가능성
4. prd.md 요구사항 충족 여부
```

---

## 📝 문서화 프롬프트

### API 문서 생성

```
현재까지 구현된 API에 대한 문서를 작성해주세요.

포함 내용:
- URL 및 HTTP 메서드
- 요청 파라미터
- 응답 형식
- 오류 코드
- 사용 예시

prd.md 5장의 형식 참조
```

### 트러블슈팅 가이드

```
개발 중 자주 발생하는 문제와 해결 방법을 정리해주세요.

카테고리:
1. 빌드 및 배포 문제
2. MyBatis 설정 문제
3. 파일 업로드 문제
4. 트랜잭션 문제
5. 성능 문제
```

---

## 🎨 프론트엔드 프롬프트

### JSP 화면 작성

```
[화면명] (예: list.jsp, view.jsp)를 작성해주세요.

요구사항 (prd.md 7장 참조):
- 표시 항목: [항목 나열]
- 사용자 액션: [액션 나열]
- 검색 조건 유지

주의사항:
- JSTL 사용 금지 (순수 Java 코드)
- CSS는 별도 파일로 분리
- JavaScript는 별도 파일로 분리
```

### JavaScript 유효성 검증

```
[폼명]의 프론트엔드 유효성 검증 JavaScript를 작성해주세요.

검증 항목 (prd.md 3장 참조):
- [항목 1]: [검증 규칙]
- [항목 2]: [검증 규칙]

요구사항:
- 실시간 검증 (입력 중 피드백)
- submit 이벤트 시 최종 검증
- 오류 시 빨간 테두리 + 메시지 표시
```

---

## ✅ 체크리스트 프롬프트

```
Phase [번호] 완료 후 다음 사항을 체크해주세요:

코드 품질:
- [ ] convention.md의 네이밍 규칙 준수
- [ ] 들여쓰기 2칸 공백
- [ ] 한 줄 100자 이내
- [ ] Public 메서드에 Javadoc 작성

기능 요구사항:
- [ ] prd.md의 요구사항 충족
- [ ] 유효성 검증 (프론트/백엔드)
- [ ] 예외 처리 적절
- [ ] 검색 조건 유지 (해당 시)

보안:
- [ ] SQL Injection 방지 (#{} 사용)
- [ ] 비밀번호 해시화 (SHA2)
- [ ] 파일 업로드 검증
- [ ] 입력값 서버 검증

테스트:
- [ ] 정상 케이스 동작 확인
- [ ] 오류 케이스 처리 확인
- [ ] 경계값 테스트
- [ ] 통합 테스트

체크 완료 후 다음 Phase로 진행하겠습니다.
```

---

## 🚀 프로젝트 완료 후 프롬프트

```
프로젝트가 완료되었습니다. 최종 점검을 진행해주세요.

점검 항목:
1. 모든 Phase 완료 여부 (CLAUDE.md 체크리스트)
2. prd.md의 모든 요구사항 구현 확인
3. convention.md 준수 여부
4. 통합 테스트 (prd.md 8장 시나리오)
5. 보안 취약점 최종 검토
6. 성능 목표 달성 확인
7. 문서화 완료 (API 문서, README)

각 항목별로 상태를 보고해주세요.
```
