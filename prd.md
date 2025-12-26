# 게시판 프로젝트 PRD

## 1. 프로젝트 개요

### 1.1 프로젝트 목적

Servlet과 JSP를 활용한 기본적인 CRUD 기능을 갖춘 게시판 시스템 구현

### 1.2 프로젝트 범위

- 게시글 목록 조회 및 검색
- 게시글 등록, 조회, 수정, 삭제
- 첨부파일 관리 (업로드 / 다운로드)
- 댓글 등록
- 작성자 비밀번호 인증

### 1.3 주요 목표

- Servlet / JSP 기본 아키텍처 학습
- MyBatis를 활용한 데이터베이스 연동
- 파일 업로드 / 다운로드 기능 구현

## 2. 기술 스택

### 2.1 백엔드

- Java: JDK 17
- Servlet: 6.0.0
- JSP: 3.1.0
- MyBatis: 3.5.13

### 2.2 DB

- MySQL: 8.0.31
- JDBC: 8.0.33

### 2.3 프론트엔드

- HTML5
- CSS3
- JavaScript (ES6)

### 2.4 개발 환경

- WAS: Apache Tomcat 10.1.50
- IDE: IntelliJ
- 빌드 툴: Gradle

## 3. 기능 요구사항

### 3.1 게시글 목록 조회

**기능 설명**: 등록된 게시글 목록을 페이지 단위로 조회

**상세 요구사항**:
- 한 페이지당 10개의 게시글 표시
- 카테고리, 첨부파일 여부(존재 시 파일 아이콘 표시), 제목, 작성자, 조회수, 등록일시, 수정일시
- 최신 글이 상단에 표시 (작성일 기준 내림차순)
- 페이징 네비게이션 제공 (이전, 다음, 페이지 번호)

**입력**:
- `page`: 페이지 번호 (기본값: 1)
- `category`: 카테고리 ID
- `from`: 등록일시 시작
- `to`: 등록일시 종료
- `keyword`: 검색어 (제목 or 내용 or 작성자)

**출력**:
- 게시글 목록 (List<Board>)
- 전체 게시글 수
- 현재 페이지 정보
- 페이지네이션 정보

### 3.2 게시글 검색

**기능 설명**: 다양한 조건으로 게시글 검색

**상세 요구사항**:

**검색 조건**:
- 등록일시 범위 (선택)
    - `form`: 시작일 (미입력 시: 1년전)
    - `to`: 종료일 (미입력 시: 오늘)
- 카테고리
    - 미입력 시: 전체
    - 입력 시: 일치하는 카테고리만 선택
- 검색어 (선택)
    - 미입력 시: 날짜 범위와 카테고리로만 검색
    - 입력 시: 날짜 범위, 카테고리와 제목/내용/작성자 중 일치하는 검색
- 검색 결과는 목록 조회 규칙과 동일하게 페이징 및 정렬
- 검색어가 없는 경우 alert("검색어를 입력하세요!") 표시

**입력**:
- `category`: 카테고리 ID
- `from`: 등록일시 시작 (기본값: 금일로부터 1년전)
- `to`: 등록일시 종료 (기본값: 금일)
- `keyword`: 검색어 (필수)

**출력**:
- 검색된 게시글 목록
- 검색 결과 개수

**유효성 검증**:
- 검색어가 비어있으면 alert 표시 후 검색 수행하지 않음

### 3.3 게시글 조회

**기능 설명**: 특정 게시글의 상세 내용 조회

**상세 요구사항**:
- 목록에서 제목 클릭 시 상세 페이지로 이동
- 게시글 조회 시 조회수 1 증가 (페이지 진입 시마다 증가)

**표시 정보**:
- 작성자
- 등록일시
- 수정일시
- 조회수
- 카테고리
- 제목
- 내용
- 첨부파일 목록 (다운로드 링크 포함)
- 댓글 리스트 (등록일시, 내용)

**제공 기능**:
- 목록으로 돌아가기 (검색 조건 유지)
- 수정 버튼 (비밀번호 인증 필요)
- 삭제 버튼 (비밀번호 인증 필요)
- 댓글 등록

**입력**:
- `boardId`: 게시글 ID
- `page`: 페이지 번호
- `category`: 카테고리 ID
- `from`: 등록일시 시작
- `to`: 등록일시 종료
- `keyword`: 검색어

**출력**:
- 게시글 상세 정보 (Board 객체)
- 첨부파일 목록 (List<File>)
- 댓글 목록 (List<Comment>)

### 3.4 게시글 등록

**기능 설명**: 새로운 게시글 작성 및 저장

**상세 요구사항**:
- 입력값 검증은 프론트엔드와 백엔드 모두에서 수행
- 검증 실패 시 오류 메시지 표시 및 기존 입력값 유지
- 등록 성공 시 게시글 목록 페이지로 이동

**입력 항목**:
- `categoryId`: 카테고리 (필수)
- `writer`: 작성자 (필수, 2~10자)
- `password`: 비밀번호 (필수, 8~12자, 영문 + 숫자 조합)
- `passwordConfirm`: 비밀번호 확인 (필수, password와 동일)
- `title`: 제목 (필수, 4~1000자)
- `content`: 내용 (필수, 4~4000자)
- `files`: 첨부파일 (선택, 최대 3개)

**첨부파일 제약사항**:
- 최대 개수: 3개
- 최소 개수: 0개
- 단일 파일 최대 크기: 2MB
- 허용 확장자: jpg, pdf, png

**유효성 검증**:
- 카테고리: 선택 필수
- 작성자: 2~10자
- 비밀번호: 8~12자, 영문 + 숫자 조합
- 비밀번호 확인: password와 일치
- 제목: 4~1000자
- 내용: 4~4000자
- 파일 개수: 최대 3개
- 파일 크기: 각 파일당 2MB 이하
- 파일 확장자: jpg, pdf, png만 허용

**출력**:
- 등록 성공: 게시글 ID 반환 및 상세 페이지로 이동
- 등록 실패: 오류 메시지 표시 및 입력 화면 유지

### 3.5 게시글 수정

**기능 설명**: 기존 게시글의 제목, 내용, 첨부파일 수정

**상세 요구사항**:
- 비밀번호 검증 후 수정 가능
- 비밀번호 불일치 시 수정 불가
- 수정 시 수정일시 자동 갱신
- 수정 완료 후 검색조건 상세 페이지로 이동

**수정 가능 항목**:
- `title`: 제목 (필수, 4~1000자)
- `content`: 내용 (필수, 4~4000자)
- `files`: 첨부파일 추가 (선택, 기존 파일과 합쳐서 최대 3개)
- `deletedFileIdList`: 삭제할 첨부파일 ID 목록 (선택)

**수정 불가 항목**:
- 카테고리
- 작성자
- 비밀번호
- 등록일시
- 조회수

**입력**:
- `boardId`: 게시글 ID (필수)
- `password`: 비밀번호 (필수)
- `title`: 수정할 제목 (필수, 4~1000자)
- `content`: 수정할 내용 (필수, 4~4000자)
- `files`: 추가할 첨부파일 (선택)
- `deletedFileIdList`: 삭제할 파일 ID 배열 (선택)
- `page`, `category`, `from`, `to`, `keyword`: 검색 조건

**출력**:
- 수정 성공: 상세 페이지로 이동
- 수정 실패: 오류 메시지 표시

### 3.6 게시글 삭제

**기능 설명**: 게시글 및 관련 데이터 삭제

**상세 요구사항**:
- 비밀번호 검증 후 삭제 가능
- 비밀번호 불일치 시 삭제 불가
- 게시글과 함께 첨부파일, 댓글도 삭제
- 삭제 성공 시 목록 페이지로 이동 (검색 조건 유지 X)

**입력**:
- `boardId`: 게시글 ID (필수)
- `password`: 비밀번호 (필수)

**출력**:
- 삭제 성공: 목록 페이지로 이동
- 삭제 실패: 오류 메시지 표시 및 상세 페이지 유지

**삭제 대상**:
- board 테이블 레코드
- file 테이블 레코드 (연관된 첨부파일)
- comment 테이블 레코드 (연관된 댓글)
- 물리적 파일 (서버에 저장된 실제 파일)

### 3.7 첨부파일 다운로드

**기능 설명**: 게시글에 첨부된 파일 다운로드

**상세 요구사항**:
- 게시글 조회 페이지에서 첨부파일 클릭 시 다운로드
- 원본 파일명으로 다운로드
- 브라우저 다운로드 기능 활용

**입력**:
- `fileId`: 파일 ID (필수)

**출력**:
- 파일 바이너리 데이터
- Content-Disposition 헤더에 원본 파일명 포함

#### 3.7.1 파일 관리 전략

**기본 경로**: `./src/main/webapp/uploads`

**파일명 규칙**:
- 저장 파일명: `{UUID}.extension`

### 3.8 댓글 등록

**기능 설명**: 게시글에 댓글 작성

**상세 요구사항**:
- 게시글 조회 페이지에서 댓글 등록 가능
- 입력값 검증은 프론트엔드와 백엔드 모두에서 수행
- 검증 실패 시 오류 메시지 표시 및 기존 입력값 유지
- 등록 성공 시 댓글 목록에 즉시 반영

**입력 항목**:
- `boardId`: 게시글 ID (필수)
- `content`: 댓글 내용 (필수, 1~300자)

**유효성 검증**:
- 내용: 1~300자

**출력**:
- 등록 성공: 댓글 목록 갱신
- 등록 실패: 오류 메시지 표시

### 3.9 게시글 작성자 확인

**기능 설명**: 게시글 수정/삭제 전 작성자 비밀번호 확인

**상세 요구사항**:
- 수정 또는 삭제 버튼 클릭 시 비밀번호 입력 모달창 표시
- 비밀번호 일치 시
    - 수정 요청: 수정 페이지로 이동
    - 삭제 요청: 게시글 삭제 후 목록으로 이동
- 비밀번호 불일치 시
    - "비밀번호가 일치하지 않습니다" 메시지 표시
    - 게시글 상세 화면에 그대로 머무름

**입력**:
- `boardId`: 게시글 ID (필수)
- `password`: 입력한 비밀번호 (필수)

**출력**:
- 성공: true/false
- 실패: 오류 메시지

**보안 처리**:
- 비밀번호는 SHA2() 함수로 해시하여 비교
- 평문 비밀번호는 서버에 저장하지 않음

### 3.10 공통 요구사항

#### 3.10.1 검색 조건 유지

**적용 범위**: 모든 게시판 관련 화면 이동 시 검색 조건을 유지해야 합니다.

**유지되어야 할 파라미터**:
- `page`: 페이지 번호 (기본값: 1)
- `category`: 카테고리 ID (선택)
- `from`: 등록일시 시작 (선택, YYYY-MM-DD 형식)
- `to`: 등록일시 종료 (선택, YYYY-MM-DD 형식)
- `keyword`: 검색어 (선택)

**적용 시나리오**:

| 출발 화면 | 도착 화면 | 검색 조건 유지 |
|---------|---------|--------------|
| 게시글 목록 | 게시글 조회 | ✅ 유지 |
| 게시글 조회 | 게시글 목록 | ✅ 유지 |
| 게시글 조회 | 게시글 수정 | ✅ 유지 |
| 게시글 수정 | 게시글 조회 | ✅ 유지 |
| 게시글 목록 | 게시글 등록 | ✅ 유지 |
| 게시글 등록 완료 | 게시글 조회 | ❌ 유지 안 함 (새 글) |
| 게시글 삭제 완료 | 게시글 목록 | ❌ 유지 안 함 |

## 4. 비기능 요구사항

### 4.1 성능

**요구사항**:
- 게시글 목록은 반드시 DB 레벨에서 페이징 처리 (LIMIT, OFFSET 사용)
- 단일 첨부파일 최대 크기는 2MB 이하로 제한

**성능 목표**:
- 게시글 목록 조회 응답 시간: 2초 이내
- 게시글 상세 조회 응답 시간: 1초 이내

### 4.2 보안

**필수 보안 요구사항**:

**1. 비밀번호 보안**
- 게시글 비밀번호는 평문으로 저장하지 않음
- MySQL SHA2() 함수를 사용하여 해시 저장
- 비밀번호 비교 시에도 해시 값으로 비교

**2. 입력값 검증**
- 모든 사용자 입력값은 서버에서 검증
- 프론트엔드 검증은 사용자 편의를 위한 보조 수단

**3. 파일 업로드 보안**
- 업로드 파일명은 서버에서 UUID로 변환하여 저장
- 원본 파일명은 DB에 별도 저장
- 허용 확장자: jpg, pdf, png만 업로드 가능
- 단일 파일 최대 크기: 2MB

**4. SQL Injection 방지**
- MyBatis 파라미터 바인딩 사용 (#{} 문법)
- 문자열 결합을 통한 동적 SQL 생성 지양
- ${} 문법 사용 최소화

### 4.3 안정성/신뢰성

**요구사항**:
- 파일 업로드 실패 시 게시글 저장 롤백
- 트랜잭션 처리로 데이터 일관성 보장
- 게시글 삭제 시 연관 데이터(첨부파일, 댓글) 함께 삭제. 단, CASCADE 사용안함. 직접 제거

**예외 처리**:
- 모든 예외는 적절히 처리하여 사용자에게 의미 있는 메시지 제공
- 시스템 오류는 로그에 기록

### 4.4 사용성

**요구사항**:
- 입력 오류 발생 시 사용자가 이해할 수 있는 오류 메시지 제공
- 오류 메시지는 화면에 명확히 표시
- 오류 발생 시 기존 입력값 유지

**오류 메시지 예시**:
- "검색어를 입력하세요!"
- "비밀번호가 일치하지 않습니다"
- "제목은 4자 이상 1000자 이하로 입력해주세요"
- "파일은 최대 3개까지 첨부 가능합니다"
- "허용되지 않는 파일 확장자입니다 (jpg, pdf, png만 가능)"

### 4.5 유지보수성

**아키텍처 요구사항**:
- Servlet, Service, DAO, Mapper 계층 분리
- 비즈니스 로직은 Servlet에 직접 작성하지 않음 (Service 계층에서 처리)
- 공통 예외 클래스 별도 관리
- 공통 유틸리티 클래스 별도 관리
- SQL은 MyBatis Mapper XML로 관리

**코드 품질**:
- 일관된 네이밍 컨벤션
- 적절한 주석 작성
- 메서드 단일 책임 원칙 준수

### 4.6 확장성

**요구사항**:
- 게시판은 카테고리, 댓글, 파일 첨부 기능 확장 가능하도록 설계

**향후 추가 가능한 기능**:
- 댓글 수정/삭제
- 대댓글 (답글)
- 게시글 좋아요/싫어요
- 사용자 인증 시스템
- 게시글 알림

### 4.7 제약사항

**기술 스택 제약**:
- Java 17
- Tomcat 10.1.50
- Servlet
- JSP
- MySQL 8.0.31
- MyBatis

**사용 불가 기술**:
- Spring Framework (Spring Boot 포함)
- JPA/Hibernate
- JSTL

## 5. Servlet API 명세

### 5.1 게시물 목록 조회

**URL**: `/boards`  
**Method**: GET  
**Query Parameters**:
- `page`: Integer (optional, default: 1)
- `category`: Integer (optional)
- `from`: String (optional, YYYY-MM-DD)
- `to`: String (optional, YYYY-MM-DD)
- `keyword`: String (optional)

**Description**:  
검색 조건에 맞는 게시물 목록을 최신순으로 조회 후 목록 페이지 반환

**Response**:
- 목록 JSP 페이지 렌더링 (list.jsp)
- request attribute로 게시글 목록, 페이지 정보 전달

### 5.2 게시물 상세 조회

**URL**: `/board/view`  
**Method**: GET  
**Query Parameters**:
- `boardId`: Long (required)
- `page`: Integer (optional)
- `category`: Integer (optional)
- `from`: String (optional)
- `to`: String (optional)
- `keyword`: String (optional)

**Description**:  
검색 조건 유지하며 게시물 상세 조회 페이지 반환  
조회수 1 증가

**Response**:
- 상세 JSP 페이지 렌더링 (view.jsp)
- request attribute로 게시글 정보, 첨부파일, 댓글 목록 전달

### 5.3 게시물 작성 페이지

**URL**: `/board/post`  
**Method**: GET  
**Query Parameters**:
- `page`: Integer (optional)
- `category`: Integer (optional)
- `from`: String (optional)
- `to`: String (optional)
- `keyword`: String (optional)

**Description**:  
검색 조건 유지하며 게시물 작성 페이지 반환

**Response**:
- 작성 JSP 페이지 렌더링 (post.jsp)

### 5.4 게시물 수정 페이지

**URL**: `/board/edit`  
**Method**: GET  
**Query Parameters**:
- `boardId`: Long (required)
- `page`: Integer (optional)
- `category`: Integer (optional)
- `from`: String (optional)
- `to`: String (optional)
- `keyword`: String (optional)

**Description**:  
검색 조건 유지하며 게시물 수정 페이지 반환  
기존 게시글 데이터를 폼에 미리 채움

**Response**:
- 수정 JSP 페이지 렌더링 (edit.jsp)
- request attribute로 게시글 정보, 첨부파일 목록 전달

### 5.5 첨부파일 다운로드

**URL**: `/download`  
**Method**: GET  
**Query Parameters**:
- `fileId`: Long (required)

**Description**:  
첨부파일 다운로드

**Response**:
- Content-Type: application/octet-stream
- Content-Disposition: attachment; filename="원본파일명"
- 파일 바이너리 데이터

### 5.6 게시물 작성

**URL**: `/board/post`  
**Method**: POST  
**Content-Type**: multipart/form-data  
**Request Body**:
- `categoryId`: Integer (required)
- `writer`: String (required, 2~10자)
- `password`: String (required, 8~12자, 영문+숫자)
- `passwordConfirm`: String (required)
- `title`: String (required, 4~1000자)
- `content`: String (required, 4~4000자)
- `files`: File[] (optional, 최대 3개)

**Description**:  
게시물 작성  
입력값 검증 후 DB 저장  
파일 업로드 처리

**Response**:
- 성공: 상세 페이지로 redirect (`/board/view?boardId={생성된ID}`)
- 실패: 작성 페이지로 forward, 오류 메시지 전달

### 5.7 게시물 수정

**URL**: `/board/edit`  
**Method**: POST  
**Content-Type**: multipart/form-data  
**Request Body**:
- `boardId`: Long (required)
- `password`: String (required)
- `title`: String (required, 4~1000자)
- `content`: String (required, 4~4000자)
- `files`: File[] (optional)
- `deletedFileIdList`: Long[] (optional)

**Description**:  
게시물 수정  
비밀번호 검증 후 수정 처리  
첨부파일 추가/삭제 처리

**Response**:
- 성공: 상세 페이지로 redirect (`/board/view?boardId={ID}`)
- 실패: 수정 페이지로 forward, 오류 메시지 전달

### 5.8 게시물 삭제

**URL**: `/board/delete`  
**Method**: POST  
**Content-Type**: application/json  
**Request Body**:
```json
{
  "boardId": Long (required),
  "password": String (required)
}
```

**Description**:  
게시물 삭제  
비밀번호 검증 후 게시글, 첨부파일, 댓글 삭제

**Response**:
```json
{
  "success": Boolean,
  "message": String,
  "redirectUrl": String (optional)
}
```

### 5.9 댓글 작성

**URL**: `/comment`  
**Method**: POST  
**Content-Type**: application/json  
**Request Body**:
```json
{
  "boardId": Long (required),
  "content": String (required, 1~300자)
}
```

**Description**:  
댓글 등록  
입력값 검증 후 DB 저장

**Response**:
```json
{
  "success": Boolean,
  "message": String,
  "comment": {
    "commentId": Long,
    "content": String,
    "createdAt": String
  }
}
```

### 5.10 작성자 확인

**URL**: `/auth/confirm`  
**Method**: POST  
**Content-Type**: application/json  
**Request Body**:
```json
{
  "boardId": Long (required),
  "password": String (required)
}
```

**Description**:  
작성자 비밀번호 확인  
SHA2() 해시로 비밀번호 검증

**Response**:
```json
{
  "success": Boolean,
  "message": String
}
```

## 6. DB 설계

### 6.1 테이블 구조

#### 6.1.1 Category (카테고리)

| Column | Type | Null | Key | Default | Description |
|--------|------|------|-----|---------|-------------|
| category_id | BIGINT | NO | PK | AUTO_INCREMENT | 카테고리 ID |
| category_name | VARCHAR(20) | NO | | | 카테고리 이름 |

**제약조건**:
- PRIMARY KEY (category_id)
- UNIQUE KEY (category_name)

#### 6.1.2 Board (게시글)

| Column | Type | Null | Key | Default | Description |
|--------|------|------|-----|---------|-------------|
| board_id | BIGINT | NO | PK | AUTO_INCREMENT | 게시글 ID |
| category_id | BIGINT | NO | FK | | 카테고리 ID |
| title | VARCHAR(99) | NO | | | 제목 |
| content | VARCHAR(3999) | NO | | | 내용 |
| user_name | VARCHAR(10) | NO | | | 작성자 |
| password | VARCHAR(64) | NO | | | 비밀번호(해시) |
| views | BIGINT | NO | | 0 | 조회수 |
| created_at | TIMESTAMP | NO | | CURRENT_TIMESTAMP | 등록일시 |
| edited_at | TIMESTAMP | YES | | NULL | 수정일시 |

**제약조건**:
- PRIMARY KEY (board_id)
- FOREIGN KEY (category_id) REFERENCES category(category_id)
- INDEX on created_at (내림차순 조회용)
- INDEX on user_name (검색용)
- INDEX on title (검색용)

#### 6.1.3 Comment (댓글)

| Column | Type | Null | Key | Default | Description |
|--------|------|------|-----|---------|-------------|
| comment_id | BIGINT | NO | PK | AUTO_INCREMENT | 댓글 ID |
| board_id | BIGINT | NO | FK | | 게시글 ID |
| comment | VARCHAR(300) | NO | | | 댓글 내용 |
| created_at | TIMESTAMP | NO | | CURRENT_TIMESTAMP | 등록일시 |
| edited_at | TIMESTAMP | YES | | NULL | 수정일시 |

**제약조건**:
- PRIMARY KEY (comment_id)
- FOREIGN KEY (board_id) REFERENCES board(board_id)
- INDEX on board_id

#### 6.1.4 File (첨부파일)

| Column | Type | Null | Key | Default | Description |
|--------|------|------|-----|---------|-------------|
| file_id | BIGINT | NO | PK | AUTO_INCREMENT | 파일 ID |
| board_id | BIGINT | NO | FK | | 게시글 ID |
| original_name | VARCHAR(100) | NO | | | 원본 파일명 |
| physical_name | VARCHAR(100) | NO | | | 서버 저장 파일명 (UUID) |
| file_path | VARCHAR(100) | NO | | | 저장 경로 |
| extension | VARCHAR(10) | NO | | | 확장자 |
| size | BIGINT | NO | | | 파일 크기(bytes) |
| created_at | TIMESTAMP | NO | | CURRENT_TIMESTAMP | 등록일시 |
| edited_at | TIMESTAMP | YES | | NULL | 수정일시 |

**제약조건**:
- PRIMARY KEY (file_id)
- FOREIGN KEY (board_id) REFERENCES board(board_id)
- INDEX on board_id

### 6.2 데이터베이스 관계

1. **Category : Board = 1 : N**
    - 하나의 카테고리는 여러 게시글을 가질 수 있음

2. **Board : Comment = 1 : N**
    - 하나의 게시글은 여러 댓글을 가질 수 있음
    - 게시글 삭제 시 댓글도 함께 삭제 (CASCADE 사용X)

3. **Board : File = 1 : N**
    - 하나의 게시글은 여러 첨부파일을 가질 수 있음 (최대 3개)
    - 게시글 삭제 시 첨부파일도 함께 삭제 (CASCADE 사용X)

### 6.3 DB 설정

- **DB URL**: jdbc:mysql://localhost:3306/board_system
- **DB username**: root
- **DB password**: asd@1252370

## 7. 화면 설계 및 흐름

### 7.1 전체 화면 흐름도

```
[게시판 목록] (/boards)
├─ (검색)
├─ (페이지 이동)
├─ 제목 클릭 ──────▶ [게시글 조회] (/board/view)
│                    ├─ 댓글 등록 (/comment)
│                    ├─ 첨부파일 다운로드 (/download)
│                    ├─ 수정 버튼 ──▶ [비밀번호 확인] (/auth/confirm)
│                    │                └─ 성공 ──▶ [게시글 수정] (/board/edit)
│                    └─ 삭제 버튼 ──▶ [비밀번호 확인] (/auth/confirm)
│                                     └─ 성공 ──▶ [게시글 목록]
└─ 글쓰기 버튼 ──────▶ [게시글 등록] (/board/post)
                      └─ 등록 완료 ──▶ [게시글 조회]
```

### 7.2 화면별 상세 설계

#### 7.2.1 게시판 목록 화면 (list.jsp)

**URL**: `/boards`

**사용자 액션**:
1. 검색 조건 입력 후 [검색] 버튼 클릭 → 검색 결과 표시
2. 페이지 번호 클릭 → 해당 페이지 게시글 표시
3. 게시글 제목 클릭 → 상세 페이지로 이동
4. [글쓰기] 버튼 클릭 → 게시글 등록 페이지로 이동

**검색 조건 유지**:
- 모든 링크에 검색 조건(page, category, from, to, keyword) 포함

#### 7.2.2 게시글 조회 화면 (view.jsp)

**URL**: `/board/view?boardId={ID}`

**사용자 액션**:
1. [목록] 버튼 클릭 → 목록 페이지로 이동 (검색 조건 유지)
2. 첨부파일 [다운로드] 클릭 → 파일 다운로드
3. 댓글 입력 후 [등록] 클릭 → 댓글 등록, 페이지 새로고침
4. [수정] 버튼 클릭 → 비밀번호 입력 모달 표시
5. [삭제] 버튼 클릭 → 비밀번호 입력 모달 표시

**비밀번호 확인 모달**:
```
┌─────────────┐
│ 비밀번호 확인 │
├─────────────┤
│ 비밀번호: [________] │
│ [확인] [취소] │
└─────────────┘
```

#### 7.2.3 게시글 등록 화면 (post.jsp)

**URL**: `/board/post`

**사용자 액션**:
1. 모든 필수 항목 입력
2. [등록] 버튼 클릭 → 입력값 검증 → 저장 → 상세 페이지로 이동
3. [취소] 버튼 클릭 → 목록 페이지로 이동
4. 검증 실패 시 → 오류 메시지 표시, 입력값 유지

**입력값 검증 (프론트엔드)**:
- 실시간 검증: 입력 중 문자 수 표시
- submit 이벤트에서 검증 수행
- 검증 실패 시 해당 필드에 빨간 테두리 표시 및 오류 메시지 표시

#### 7.2.4 게시글 수정 화면 (edit.jsp)

**URL**: `/board/edit?boardId={ID}`

**사용자 액션**:
1. 제목, 내용 수정
2. 기존 파일 [삭제] 체크
3. 새 파일 추가
4. [수정] 버튼 클릭 → 저장 → 상세 페이지로 이동
5. [취소] 버튼 클릭 → 상세 페이지로 이동

### 7.3 화면 이동 흐름 시나리오

#### 시나리오 1: 게시글 작성 및 조회

1. 사용자가 `/boards` 접속
2. [글쓰기] 버튼 클릭 → `/board/post` 이동
3. 게시글 정보 입력 후 [등록] 클릭
4. POST `/board/post` 요청
5. 성공 시 `/board/view?boardId={id}` 으로 redirect
6. 새로 작성한 게시글 조회 페이지 표시

#### 시나리오 2: 게시글 검색 및 조회

1. 사용자가 `/boards` 접속
2. 카테고리: Java, 검색어: "스프링" 입력 후 [검색] 클릭
3. `/boards?category=1&keyword=스프링` 으로 이동
4. 검색 결과 표시
5. 게시글 제목 클릭 → `/board/view?boardId={id}&검색조건`
6. 상세 페이지에서도 검색 조건 유지
7. [목록] 클릭 → `/boards?검색조건` 으로 돌아감

#### 시나리오 3: 게시글 수정

1. 게시글 조회 페이지 (`/board/view?boardId={id}&검색조건`)
2. [수정] 버튼 클릭
3. 비밀번호 입력 모달 표시
4. 비밀번호 입력 후 [확인] 클릭
5. POST `/auth/confirm` 요청 (AJAX)
6. 비밀번호 일치 시 `/board/edit?boardId=123` 으로 이동
7. 수정 화면에서 내용 변경 후 [수정] 클릭
8. POST `/board/edit` 요청
9. 성공 시 `/board/view?boardId={id}` 으로 redirect

#### 시나리오 4: 게시글 삭제

1. 게시글 조회 페이지 (`/board/view?boardId={id}&검색조건`)
2. [삭제] 버튼 클릭
3. 비밀번호 입력 모달 표시
4. 비밀번호 입력 후 [확인] 클릭
5. POST `/auth/confirm` 요청 (AJAX)
6. 비밀번호 일치 시 POST `/board/delete` 요청 (AJAX)
7. 삭제 성공 시 `/boards?검색조건`으로 redirect

## 8. 사용자 시나리오

### 8.1 게시글 목록 조회 시나리오

**주요 흐름**:
1. 사용자는 브라우저에서 `/boards` 로 접속한다
2. 시스템은 첫 페이지의 게시글 목록을 최신순으로 조회한다
3. 10개의 게시글과 페이지네이션이 표시된다
4. 사용자는 페이지 번호를 클릭하여 다른 페이지를 조회할 수 있다

**대체 흐름**:
- 페이지 번호가 범위를 벗어난 경우: 첫 페이지로 이동
- 게시글이 없는 경우: "등록된 게시글이 없습니다" 메시지 표시

### 8.2 게시글 검색 시나리오

**주요 흐름**:
1. 사용자는 검색 조건을 입력한다
    - 카테고리 선택 (선택사항)
    - 등록일시 범위 입력 (선택사항)
    - 검색어 입력 (필수)
2. [검색] 버튼을 클릭한다
3. 시스템은 입력값을 검증한다
4. 검색 조건에 맞는 게시글 목록을 조회하여 표시한다

**대체 흐름**:
- 검색어가 비어있는 경우: alert("검색어를 입력하세요!") 표시, 검색 수행하지 않음
- 검색 결과가 없는 경우: "검색 결과가 없습니다" 메시지 표시

**검색 조건 유지**:
- 검색 후 상세 페이지로 이동해도 검색 조건 유지
- 목록으로 돌아올 때 동일한 검색 결과 표시

### 8.3 게시글 상세 조회 시나리오

**주요 흐름**:
1. 사용자는 게시판 목록에서 게시글 제목을 클릭한다
2. 시스템은 해당 게시글의 상세 정보를 조회한다
3. 시스템은 조회수를 1 증가시킨다
4. 게시글 상세 정보, 첨부파일, 댓글이 표시된다

**대체 흐름**:
- 존재하지 않는 게시글 ID: "게시글을 찾을 수 없습니다" 오류 메시지 표시
- 첨부파일이 없는 경우: "첨부파일이 없습니다" 표시

### 8.4 게시글 등록 시나리오

**주요 흐름**:
1. 사용자는 게시판 목록 화면에서 [글쓰기] 버튼을 클릭한다
2. 게시글 등록 화면으로 이동한다
3. 사용자는 다음 정보를 입력한다:
    - 카테고리 선택
    - 작성자 (2~10자)
    - 비밀번호 (8~12자, 영문+숫자)
    - 비밀번호 확인
    - 제목 (4~1000자)
    - 내용 (4~4000자)
    - 첨부파일 (선택, 최대 3개)
4. [등록] 버튼을 클릭한다
5. 시스템은 프론트엔드에서 입력값을 검증한다
6. 시스템은 백엔드에서 입력값을 재검증한다
7. 검증 성공 시 게시글을 저장한다
8. 업로드된 파일을 서버(`./src/webapp/uploads`)에 저장한다
9. 게시글 조회 화면으로 이동한다

**대체 흐름**:
- **입력값 검증 실패 (프론트엔드)**:
    - 해당 필드에 빨간 테두리 표시
    - 오류 메시지 표시
    - 등록 수행하지 않음
- **입력값 검증 실패 (백엔드)**:
    - 등록 화면에 머무름
    - 오류 메시지 표시
    - 기존 입력값 유지
- **파일 업로드 실패**:
    - 게시글 저장 롤백
    - "파일 업로드에 실패했습니다" 오류 메시지 표시

### 8.5 게시글 수정 시나리오

**주요 흐름**:
1. 사용자는 게시글 조회 화면에서 [수정] 버튼을 클릭한다
2. 비밀번호 입력 모달이 표시된다
3. 사용자는 비밀번호를 입력한다
4. [확인] 버튼을 클릭한다
5. 시스템은 비밀번호를 검증한다 (AJAX 요청)
6. 비밀번호가 일치하면 게시글 수정 화면으로 이동한다
7. 기존 게시글 데이터가 폼에 미리 채워진다
8. 사용자는 제목, 내용, 첨부파일을 수정한다
    - 기존 파일 삭제 가능
    - 새 파일 추가 가능 (기존 파일 포함 최대 3개)
9. [수정] 버튼을 클릭한다
10. 시스템은 입력값을 검증한다
11. 검증 성공 시 게시글을 수정한다
12. 수정일시가 현재 시각으로 갱신된다
13. 게시글 조회 화면으로 이동한다

**대체 흐름**:
- **비밀번호 불일치**:
    - "비밀번호가 일치하지 않습니다" 메시지 표시
    - 게시글 조회 화면에 머무름
- **입력값 검증 실패**:
    - 수정 화면에 머무름
    - 오류 메시지 표시
    - 기존 입력값 유지
- **파일 개수 초과 (기존 + 새 파일 > 3개)**:
    - "첨부파일은 최대 3개까지 등록할 수 있습니다" 오류 메시지 표시

### 8.6 게시글 삭제 시나리오

**주요 흐름**:
1. 사용자는 게시글 조회 화면에서 [삭제] 버튼을 클릭한다
2. 비밀번호 입력 모달이 표시된다
3. 사용자는 비밀번호를 입력한다
4. [확인] 버튼을 클릭한다
5. 시스템은 비밀번호를 검증한다 (AJAX 요청)
6. 비밀번호가 일치하면 삭제를 수행한다
7. 시스템은 다음을 순서대로 삭제한다:
    - 댓글 (comment 테이블)
    - 첨부파일 정보 (file 테이블)
    - 물리적 파일 (서버 디스크)
    - 게시글 (board 테이블)
8. 게시글 목록 화면으로 이동한다 (검색 조건 유지)

**대체 흐름**:
- **비밀번호 불일치**:
    - "비밀번호가 일치하지 않습니다" 메시지 표시
    - 게시글 조회 화면에 머무름
- **삭제 중 오류 발생**:
    - 트랜잭션 롤백
    - "게시글 삭제에 실패했습니다" 오류 메시지 표시
    - 게시글 조회 화면에 머무름

### 8.7 댓글 등록 시나리오

**주요 흐름**:
1. 사용자는 게시글 조회 화면에서 댓글 입력 영역에 댓글을 작성한다
2. 댓글 내용을 입력한다 (1~300자)
3. [등록] 버튼을 클릭한다
4. 시스템은 프론트엔드에서 입력값을 검증한다
5. 시스템은 AJAX로 댓글 등록 요청을 전송한다
6. 백엔드에서 입력값을 재검증한다
7. 검증 성공 시 댓글을 저장한다
8. 댓글 목록이 갱신되어 표시된다

**대체 흐름**:
- **입력값 검증 실패 (프론트엔드)**:
    - 오류 메시지 표시
    - 등록 수행하지 않음
- **입력값 검증 실패 (백엔드)**:
    - AJAX 응답으로 오류 메시지 반환
    - 오류 메시지 표시
    - 기존 입력값 유지

### 8.8 첨부파일 다운로드 시나리오

**주요 흐름**:
1. 사용자는 게시글 조회 화면에서 첨부파일 링크를 클릭한다
2. 시스템은 file_id로 파일 정보를 조회한다
3. 서버에서 물리적 파일을 읽는다
4. Content-Disposition 헤더에 원본 파일명을 설정한다
5. 파일 데이터를 응답으로 전송한다
6. 브라우저는 파일 다운로드를 시작한다

**대체 흐름**:
- **파일이 존재하지 않는 경우**:
    - "파일을 찾을 수 없습니다" 오류 메시지 표시
- **파일 읽기 오류**:
    - "파일 다운로드에 실패했습니다" 오류 메시지 표시

---
