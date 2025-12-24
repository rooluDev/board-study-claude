-- ============================================================
-- 게시판 시스템 데이터베이스 스키마 (DDL)
-- ============================================================
-- 데이터베이스: board_system
-- MySQL 8.0.31
-- 문자 집합: UTF8MB4
-- ============================================================

-- 데이터베이스 생성 (실행 시 주석 해제)
-- CREATE DATABASE IF NOT EXISTS board_system
--   CHARACTER SET utf8mb4
--   COLLATE utf8mb4_unicode_ci;

-- USE board_system;

-- 기존 테이블 삭제 (역순으로 삭제)
DROP TABLE IF EXISTS file;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS category;

-- ============================================================
-- 1. Category 테이블 (카테고리)
-- ============================================================
CREATE TABLE category (
  category_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '카테고리 ID',
  category_name VARCHAR(20) NOT NULL COMMENT '카테고리 이름',

  PRIMARY KEY (category_id),
  UNIQUE KEY uk_category_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='카테고리';

-- ============================================================
-- 2. Board 테이블 (게시글)
-- ============================================================
CREATE TABLE board (
  board_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게시글 ID',
  category_id BIGINT NOT NULL COMMENT '카테고리 ID',
  title VARCHAR(99) NOT NULL COMMENT '제목',
  content VARCHAR(3999) NOT NULL COMMENT '내용',
  user_name VARCHAR(10) NOT NULL COMMENT '작성자',
  password VARCHAR(64) NOT NULL COMMENT '비밀번호 (SHA2-256 해시)',
  views BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  edited_at TIMESTAMP NULL DEFAULT NULL COMMENT '수정일시',

  PRIMARY KEY (board_id),
  CONSTRAINT fk_board_category FOREIGN KEY (category_id)
    REFERENCES category (category_id),
  INDEX idx_board_created_at (created_at DESC) COMMENT '목록 조회용 인덱스',
  INDEX idx_board_user_name (user_name) COMMENT '작성자 검색용 인덱스',
  INDEX idx_board_title (title) COMMENT '제목 검색용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시글';

-- ============================================================
-- 3. Comment 테이블 (댓글)
-- ============================================================
CREATE TABLE comment (
  comment_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
  board_id BIGINT NOT NULL COMMENT '게시글 ID',
  comment VARCHAR(300) NOT NULL COMMENT '댓글 내용',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  edited_at TIMESTAMP NULL DEFAULT NULL COMMENT '수정일시',

  PRIMARY KEY (comment_id),
  CONSTRAINT fk_comment_board FOREIGN KEY (board_id)
    REFERENCES board (board_id),
  INDEX idx_comment_board_id (board_id) COMMENT '게시글별 댓글 조회용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='댓글';

-- ============================================================
-- 4. File 테이블 (첨부파일)
-- ============================================================
CREATE TABLE file (
  file_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '파일 ID',
  board_id BIGINT NOT NULL COMMENT '게시글 ID',
  original_name VARCHAR(100) NOT NULL COMMENT '원본 파일명',
  physical_name VARCHAR(100) NOT NULL COMMENT '서버 저장 파일명 (UUID)',
  file_path VARCHAR(15) NOT NULL COMMENT '저장 경로',
  extension VARCHAR(10) NOT NULL COMMENT '확장자',
  size BIGINT NOT NULL COMMENT '파일 크기 (bytes)',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  edited_at TIMESTAMP NULL DEFAULT NULL COMMENT '수정일시',

  PRIMARY KEY (file_id),
  CONSTRAINT fk_file_board FOREIGN KEY (board_id)
    REFERENCES board (board_id),
  INDEX idx_file_board_id (board_id) COMMENT '게시글별 파일 조회용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='첨부파일';

-- ============================================================
-- 테이블 생성 완료
-- ============================================================