package com.example.board.constants;

/**
 * 게시판 시스템 상수 정의.
 *
 * <p>이 클래스는 게시판 시스템 전반에서 사용되는 상수값을 정의합니다.
 * 인스턴스화를 방지하기 위해 private 생성자를 제공합니다.
 */
public class BoardConstants {

  // ========================================
  // 페이징 관련 상수
  // ========================================
  /** 기본 페이지 크기 (한 페이지에 표시할 게시글 수) */
  public static final int DEFAULT_PAGE_SIZE = 10;

  /** 기본 페이지 번호 */
  public static final int DEFAULT_PAGE_NUMBER = 1;

  /** 한 번에 표시할 페이지 번호 개수 */
  public static final int PAGE_BLOCK_SIZE = 10;

  // ========================================
  // 파일 업로드 관련 상수
  // ========================================
  /** 최대 파일 크기 (2MB) */
  public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

  /** 최대 파일 개수 */
  public static final int MAX_FILE_COUNT = 3;

  /** 허용되는 파일 확장자 */
  public static final String[] ALLOWED_EXTENSIONS = {"jpg", "png", "pdf"};

  /** 파일 저장 경로 */
  public static final String UPLOAD_PATH = "/uploads";

  // ========================================
  // 게시글 입력값 검증 상수
  // ========================================
  /** 제목 최소 길이 */
  public static final int MIN_TITLE_LENGTH = 4;

  /** 제목 최대 길이 */
  public static final int MAX_TITLE_LENGTH = 1000;

  /** 내용 최소 길이 */
  public static final int MIN_CONTENT_LENGTH = 4;

  /** 내용 최대 길이 */
  public static final int MAX_CONTENT_LENGTH = 4000;

  /** 작성자 이름 최소 길이 */
  public static final int MIN_USER_NAME_LENGTH = 4;

  /** 작성자 이름 최대 길이 */
  public static final int MAX_USER_NAME_LENGTH = 10;

  /** 비밀번호 최소 길이 */
  public static final int MIN_PASSWORD_LENGTH = 8;

  /** 비밀번호 최대 길이 */
  public static final int MAX_PASSWORD_LENGTH = 12;

  // ========================================
  // 댓글 입력값 검증 상수
  // ========================================
  /** 댓글 최소 길이 */
  public static final int MIN_COMMENT_LENGTH = 1;

  /** 댓글 최대 길이 */
  public static final int MAX_COMMENT_LENGTH = 300;

  // ========================================
  // 날짜 형식 상수
  // ========================================
  /** 날짜 시간 표시 형식 */
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /** 날짜 입력 형식 */
  public static final String DATE_FORMAT = "yyyy-MM-dd";

  /** 파일 저장 경로 날짜 형식 */
  public static final String FILE_PATH_DATE_FORMAT = "yyyy/MM/dd";

  // ========================================
  // 비밀번호 정규식
  // ========================================
  /** 비밀번호 패턴 (영문 + 숫자 조합) */
  public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$";

  // ========================================
  // 인코딩 상수
  // ========================================
  /** 기본 인코딩 */
  public static final String DEFAULT_ENCODING = "UTF-8";

  // ========================================
  // HTTP 상태 코드 메시지
  // ========================================
  /** 잘못된 요청 메시지 */
  public static final String MSG_BAD_REQUEST = "잘못된 요청입니다.";

  /** 권한 없음 메시지 */
  public static final String MSG_UNAUTHORIZED = "비밀번호가 일치하지 않습니다.";

  /** 찾을 수 없음 메시지 */
  public static final String MSG_NOT_FOUND = "요청한 리소스를 찾을 수 없습니다.";

  /** 서버 오류 메시지 */
  public static final String MSG_INTERNAL_SERVER_ERROR = "서버 오류가 발생했습니다.";

  /**
   * 인스턴스화 방지를 위한 private 생성자.
   *
   * @throws AssertionError 항상 예외 발생
   */
  private BoardConstants() {
    throw new AssertionError("Cannot instantiate constants class");
  }
}
