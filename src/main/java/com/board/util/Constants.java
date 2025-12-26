package com.board.util;

import java.util.Arrays;
import java.util.List;

/**
 * 게시판 시스템의 상수를 정의하는 클래스
 *
 * 모든 필드는 static final로 선언됩니다.
 */
public final class Constants {
  /**
   * private 생성자로 인스턴스화 방지
   */
  private Constants() {
    throw new AssertionError("Cannot instantiate Constants class");
  }

  // ============================================================
  // 페이징 관련 상수
  // ============================================================

  /** 페이지당 게시글 수 */
  public static final int PAGE_SIZE = 10;

  /** 페이지 네비게이션에 표시할 페이지 번호 개수 */
  public static final int PAGE_BLOCK_SIZE = 5;

  // ============================================================
  // 파일 업로드 관련 상수
  // ============================================================

  /** 단일 파일 최대 크기 (2MB) */
  public static final long MAX_FILE_SIZE = 2097152L;

  /** 전체 요청 최대 크기 (10MB) */
  public static final long MAX_REQUEST_SIZE = 10485760L;

  /** 첨부파일 최대 개수 */
  public static final int MAX_FILE_COUNT = 3;

  /** 파일 저장 경로 (webapp 기준 상대 경로) */
  public static final String UPLOAD_DIR = "/uploads";

  /** 허용 파일 확장자 목록 */
  public static final List<String> ALLOWED_EXTENSIONS =
      Arrays.asList("jpg", "pdf", "png");

  // ============================================================
  // 입력값 검증 관련 상수
  // ============================================================

  /** 작성자 최소 길이 */
  public static final int WRITER_MIN_LENGTH = 2;

  /** 작성자 최대 길이 */
  public static final int WRITER_MAX_LENGTH = 10;

  /** 비밀번호 최소 길이 */
  public static final int PASSWORD_MIN_LENGTH = 8;

  /** 비밀번호 최대 길이 */
  public static final int PASSWORD_MAX_LENGTH = 12;

  /** 제목 최소 길이 */
  public static final int TITLE_MIN_LENGTH = 4;

  /** 제목 최대 길이 */
  public static final int TITLE_MAX_LENGTH = 1000;

  /** 내용 최소 길이 */
  public static final int CONTENT_MIN_LENGTH = 4;

  /** 내용 최대 길이 */
  public static final int CONTENT_MAX_LENGTH = 4000;

  /** 댓글 최소 길이 */
  public static final int COMMENT_MIN_LENGTH = 1;

  /** 댓글 최대 길이 */
  public static final int COMMENT_MAX_LENGTH = 300;

  // ============================================================
  // 검색 조건 관련 상수
  // ============================================================

  /** 검색 기본 기간 (년) */
  public static final int DEFAULT_SEARCH_YEARS = 1;

  // ============================================================
  // 정규 표현식 패턴
  // ============================================================

  /** 비밀번호 패턴 (영문 + 숫자 조합) */
  public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";

  // ============================================================
  // 에러 메시지
  // ============================================================

  /** 작성자 유효성 검증 실패 메시지 */
  public static final String ERR_WRITER_INVALID =
      "작성자는 " + WRITER_MIN_LENGTH + "자 이상 "
          + WRITER_MAX_LENGTH + "자 이하로 입력해주세요.";

  /** 비밀번호 유효성 검증 실패 메시지 */
  public static final String ERR_PASSWORD_INVALID =
      "비밀번호는 " + PASSWORD_MIN_LENGTH + "~"
          + PASSWORD_MAX_LENGTH + "자 영문+숫자 조합으로 입력해주세요.";

  /** 제목 유효성 검증 실패 메시지 */
  public static final String ERR_TITLE_INVALID =
      "제목은 " + TITLE_MIN_LENGTH + "자 이상 "
          + TITLE_MAX_LENGTH + "자 이하로 입력해주세요.";

  /** 내용 유효성 검증 실패 메시지 */
  public static final String ERR_CONTENT_INVALID =
      "내용은 " + CONTENT_MIN_LENGTH + "자 이상 "
          + CONTENT_MAX_LENGTH + "자 이하로 입력해주세요.";

  /** 댓글 유효성 검증 실패 메시지 */
  public static final String ERR_COMMENT_INVALID =
      "댓글은 " + COMMENT_MIN_LENGTH + "자 이상 "
          + COMMENT_MAX_LENGTH + "자 이하로 입력해주세요.";

  /** 파일 크기 초과 메시지 */
  public static final String ERR_FILE_SIZE_EXCEEDED =
      "파일 크기는 " + (MAX_FILE_SIZE / 1024 / 1024) + "MB 이하로 업로드해주세요.";

  /** 파일 개수 초과 메시지 */
  public static final String ERR_FILE_COUNT_EXCEEDED =
      "파일은 최대 " + MAX_FILE_COUNT + "개까지 첨부 가능합니다.";

  /** 파일 확장자 불허 메시지 */
  public static final String ERR_FILE_EXTENSION_NOT_ALLOWED =
      "허용되지 않는 파일 확장자입니다. (jpg, pdf, png만 가능)";
}
