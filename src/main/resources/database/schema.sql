-- ================================================
-- 게시판 시스템 데이터베이스 스키마
-- ================================================

-- 데이터베이스 생성 (필요시)
CREATE DATABASE IF NOT EXISTS board_system
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE board_system;

-- ================================================
-- 1. Category 테이블
-- ================================================
CREATE TABLE IF NOT EXISTS category (
  category_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '카테고리 ID',
  category_name VARCHAR(20) NOT NULL COMMENT '카테고리 이름',
  PRIMARY KEY (category_id),
  UNIQUE KEY uk_category_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='카테고리';

-- ================================================
-- 2. Board 테이블
-- ================================================
CREATE TABLE IF NOT EXISTS board (
  board_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게시글 ID',
  category_id BIGINT NOT NULL COMMENT '카테고리 ID',
  title VARCHAR(99) NOT NULL COMMENT '제목',
  content VARCHAR(3999) NOT NULL COMMENT '내용',
  user_name VARCHAR(10) NOT NULL COMMENT '작성자',
  password VARCHAR(64) NOT NULL COMMENT '비밀번호 (SHA-256 해시)',
  views BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  edited_at TIMESTAMP NULL DEFAULT NULL COMMENT '수정일시',
  PRIMARY KEY (board_id),
  KEY idx_category_id (category_id),
  KEY idx_created_at (created_at),
  CONSTRAINT fk_board_category FOREIGN KEY (category_id) REFERENCES category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시글';

-- ================================================
-- 3. Comment 테이블
-- ================================================
CREATE TABLE IF NOT EXISTS comment (
  comment_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
  board_id BIGINT NOT NULL COMMENT '게시글 ID',
  comment VARCHAR(300) NOT NULL COMMENT '댓글 내용',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  edited_at TIMESTAMP NULL DEFAULT NULL COMMENT '수정일시',
  PRIMARY KEY (comment_id),
  KEY idx_board_id (board_id),
  CONSTRAINT fk_comment_board FOREIGN KEY (board_id) REFERENCES board (board_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='댓글';

-- ================================================
-- 4. File 테이블
-- ================================================
CREATE TABLE IF NOT EXISTS file (
  file_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '파일 ID',
  board_id BIGINT NOT NULL COMMENT '게시글 ID',
  original_name VARCHAR(100) NOT NULL COMMENT '원본 파일명',
  physical_name VARCHAR(100) NOT NULL COMMENT '서버 저장 파일명 (UUID)',
  file_path VARCHAR(15) NOT NULL COMMENT '저장 경로 (YYYY/MM/DD)',
  extension VARCHAR(10) NOT NULL COMMENT '확장자',
  size BIGINT NOT NULL COMMENT '파일 크기 (bytes)',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  edited_at TIMESTAMP NULL DEFAULT NULL COMMENT '수정일시',
  PRIMARY KEY (file_id),
  KEY idx_board_id (board_id),
  CONSTRAINT fk_file_board FOREIGN KEY (board_id) REFERENCES board (board_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='첨부파일';

-- ================================================
-- 초기 데이터 삽입
-- ================================================

-- Category 초기 데이터
INSERT INTO category (category_name) VALUES
('Java'),
('JavaScript'),
('Python'),
('Database'),
('기타');

-- ================================================
-- 인덱스 설명
-- ================================================
-- board.idx_category_id: 카테고리별 게시글 검색 성능 향상
-- board.idx_created_at: 최신순 정렬 및 날짜 범위 검색 성능 향상
-- comment.idx_board_id: 게시글별 댓글 조회 성능 향상
-- file.idx_board_id: 게시글별 첨부파일 조회 성능 향상

-- ================================================
-- 제약조건 설명
-- ================================================
-- fk_board_category: 게시글은 반드시 유효한 카테고리를 참조해야 함
-- fk_comment_board: 게시글 삭제 시 해당 댓글도 함께 삭제 (CASCADE)
-- fk_file_board: 게시글 삭제 시 해당 파일 정보도 함께 삭제 (CASCADE)
