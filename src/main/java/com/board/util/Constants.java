package com.board.util;

/**
 * 게시판 시스템 상수 정의 클래스
 *
 * 애플리케이션 전역에서 사용되는 상수 값들을 정의
 * 모든 필드는 public static final로 선언
 */
public final class Constants {

  /**
   * 생성자를 private으로 선언하여 인스턴스화 방지
   */
  private Constants() {
    throw new AssertionError("Constants 클래스는 인스턴스화할 수 없습니다.");
  }

  // ============================================================
  // 페이징 관련 상수
  // ============================================================

  /**
   * 한 페이지당 표시할 게시글 수
   */
  public static final int PAGE_SIZE = 10;

  // ============================================================
  // 파일 업로드 관련 상수
  // ============================================================

  /**
   * 단일 파일 최대 크기 (2MB = 2,097,152 bytes)
   */
  public static final long MAX_FILE_SIZE = 2097152L;

  /**
   * 최대 파일 업로드 개수
   */
  public static final int MAX_FILE_COUNT = 3;

  /**
   * 파일 업로드 디렉토리 경로 (웹 경로)
   */
  public static final String UPLOAD_DIR = "/uploads";

  /**
   * 파일 업로드 물리적 경로 (절대 경로)
   */
  public static final String FILE_UPLOAD_PATH = "/Users/user/workspace/claude/board-model2/src/main/webapp/uploads/";

  /**
   * 허용되는 파일 확장자 배열
   */
  public static final String[] ALLOWED_EXTENSIONS = {"jpg", "pdf", "png"};

  // ============================================================
  // 입력값 검증 관련 상수
  // ============================================================

  /**
   * 제목 최소 길이
   */
  public static final int TITLE_MIN_LENGTH = 4;

  /**
   * 제목 최대 길이
   */
  public static final int TITLE_MAX_LENGTH = 1000;

  /**
   * 내용 최소 길이
   */
  public static final int CONTENT_MIN_LENGTH = 4;

  /**
   * 내용 최대 길이
   */
  public static final int CONTENT_MAX_LENGTH = 4000;

  /**
   * 작성자 최소 길이
   */
  public static final int WRITER_MIN_LENGTH = 4;

  /**
   * 작성자 최대 길이
   */
  public static final int WRITER_MAX_LENGTH = 10;

  /**
   * 비밀번호 최소 길이
   */
  public static final int PASSWORD_MIN_LENGTH = 8;

  /**
   * 비밀번호 최대 길이
   */
  public static final int PASSWORD_MAX_LENGTH = 12;

  /**
   * 댓글 최소 길이
   */
  public static final int COMMENT_MIN_LENGTH = 1;

  /**
   * 댓글 최대 길이
   */
  public static final int COMMENT_MAX_LENGTH = 300;

  // ============================================================
  // 비밀번호 검증 정규식
  // ============================================================

  /**
   * 비밀번호 검증 정규식 (영문 + 숫자 조합)
   */
  public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";

  // ============================================================
  // 에러 메시지
  // ============================================================

  /**
   * 제목 유효성 검증 실패 메시지
   */
  public static final String ERROR_TITLE_LENGTH =
      "제목은 " + TITLE_MIN_LENGTH + "자 이상 " + TITLE_MAX_LENGTH + "자 이하로 입력해주세요.";

  /**
   * 내용 유효성 검증 실패 메시지
   */
  public static final String ERROR_CONTENT_LENGTH =
      "내용은 " + CONTENT_MIN_LENGTH + "자 이상 " + CONTENT_MAX_LENGTH + "자 이하로 입력해주세요.";

  /**
   * 작성자 유효성 검증 실패 메시지
   */
  public static final String ERROR_WRITER_LENGTH =
      "작성자는 " + WRITER_MIN_LENGTH + "자 이상 " + WRITER_MAX_LENGTH + "자 이하로 입력해주세요.";

  /**
   * 비밀번호 유효성 검증 실패 메시지
   */
  public static final String ERROR_PASSWORD_FORMAT =
      "비밀번호는 " + PASSWORD_MIN_LENGTH + "자 이상 " + PASSWORD_MAX_LENGTH
          + "자 이하의 영문과 숫자 조합이어야 합니다.";

  /**
   * 댓글 유효성 검증 실패 메시지
   */
  public static final String ERROR_COMMENT_LENGTH =
      "댓글은 " + COMMENT_MIN_LENGTH + "자 이상 " + COMMENT_MAX_LENGTH + "자 이하로 입력해주세요.";

  /**
   * 파일 크기 초과 메시지
   */
  public static final String ERROR_FILE_SIZE =
      "파일 크기는 " + (MAX_FILE_SIZE / 1024 / 1024) + "MB 이하여야 합니다.";

  /**
   * 파일 개수 초과 메시지
   */
  public static final String ERROR_FILE_COUNT =
      "파일은 최대 " + MAX_FILE_COUNT + "개까지 첨부 가능합니다.";

  /**
   * 허용되지 않는 파일 확장자 메시지
   */
  public static final String ERROR_FILE_EXTENSION =
      "허용되지 않는 파일 확장자입니다. (jpg, pdf, png만 가능)";
}
