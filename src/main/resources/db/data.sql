-- ============================================================
-- Initial Data for Board System
-- ============================================================
-- 게시판 시스템 초기 데이터
-- schema.sql을 먼저 실행한 후 이 파일을 실행하세요.
-- ============================================================

USE board_system;

-- ============================================================
-- 1. Category 초기 데이터
-- ============================================================
INSERT INTO category (category_name) VALUES
  ('Java'),
  ('JavaScript'),
  ('Python'),
  ('Database'),
  ('DevOps'),
  ('기타');

-- ============================================================
-- 2. 테스트용 게시글 데이터
-- ============================================================
-- 비밀번호: 'password123' (SHA2 해시로 저장)

-- 게시글 1: Java 게시글
INSERT INTO board (
  category_id,
  title,
  content,
  user_name,
  password,
  views
) VALUES (
  1,
  'Java 17의 새로운 기능 소개',
  'Java 17은 LTS(Long Term Support) 버전으로, 다양한 새로운 기능이 추가되었습니다. Pattern Matching, Sealed Classes, Text Blocks 등이 대표적입니다. 이번 게시글에서는 Java 17의 주요 기능들을 자세히 살펴보겠습니다.',
  '김개발',
  SHA2('password123', 256),
  15
);

-- 게시글 2: JavaScript 게시글
INSERT INTO board (
  category_id,
  title,
  content,
  user_name,
  password,
  views
) VALUES (
  2,
  'ES6 화살표 함수 완벽 가이드',
  'ES6에서 도입된 화살표 함수는 간결한 문법과 this 바인딩의 변화로 많은 개발자들이 사용하고 있습니다. 기본 문법부터 활용 방법까지 상세히 설명합니다.',
  '이프론트',
  SHA2('password123', 256),
  23
);

-- 게시글 3: Database 게시글
INSERT INTO board (
  category_id,
  title,
  content,
  user_name,
  password,
  views
) VALUES (
  4,
  'MySQL 인덱스 최적화 전략',
  'MySQL 데이터베이스의 성능을 향상시키기 위한 인덱스 전략을 소개합니다. B-Tree 인덱스의 동작 원리와 적절한 인덱스 설계 방법을 다룹니다.',
  '박데이터',
  SHA2('password123', 256),
  42
);

-- ============================================================
-- 3. 테스트용 댓글 데이터
-- ============================================================

-- 게시글 1번에 대한 댓글
INSERT INTO comment (board_id, comment) VALUES
  (1, '좋은 정보 감사합니다!'),
  (1, 'Java 17 사용 중인데 정말 편리하네요.'),
  (1, 'Pattern Matching 부분이 특히 유용했습니다.');

-- 게시글 2번에 대한 댓글
INSERT INTO comment (board_id, comment) VALUES
  (2, '화살표 함수 사용 시 주의사항도 알려주시면 좋겠어요.'),
  (2, '잘 보고 갑니다!');

-- 게시글 3번에 대한 댓글
INSERT INTO comment (board_id, comment) VALUES
  (3, '인덱스 설계 시 카디널리티를 고려하는 것도 중요하죠.'),
  (3, '실무에서 바로 적용해볼게요. 감사합니다!');

-- ============================================================
-- 데이터 확인
-- ============================================================

-- 카테고리 확인
SELECT * FROM category;

-- 게시글 확인
SELECT
  b.board_id,
  c.category_name,
  b.title,
  b.user_name,
  b.views,
  b.created_at
FROM board b
LEFT JOIN category c ON b.category_id = c.category_id
ORDER BY b.created_at DESC;

-- 댓글 확인
SELECT
  cm.comment_id,
  cm.board_id,
  cm.comment,
  cm.created_at
FROM comment cm
ORDER BY cm.board_id, cm.created_at;

-- ============================================================
-- 참고사항
-- ============================================================
-- 테스트용 게시글 비밀번호: password123
-- 게시글 수정/삭제 테스트 시 사용하세요.
-- ============================================================
