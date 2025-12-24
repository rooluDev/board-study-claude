package com.board.util;

import com.board.exception.ValidationException;

/**
 * 입력값 검증 유틸리티 클래스
 *
 * 사용자 입력값의 유효성을 검증하는 메서드들을 제공
 * 검증 실패 시 ValidationException 발생
 */
public final class ValidationUtil {

  /**
   * 생성자를 private으로 선언하여 인스턴스화 방지
   */
  private ValidationUtil() {
    throw new AssertionError("ValidationUtil 클래스는 인스턴스화할 수 없습니다.");
  }

  /**
   * 제목의 유효성을 검증합니다.
   *
   * @param title 검증할 제목
   * @throws ValidationException 제목이 null이거나 길이가 범위를 벗어난 경우
   */
  public static void validateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new ValidationException("제목을 입력해주세요.");
    }

    int length = title.trim().length();
    if (length < Constants.TITLE_MIN_LENGTH || length > Constants.TITLE_MAX_LENGTH) {
      throw new ValidationException(Constants.ERROR_TITLE_LENGTH);
    }
  }

  /**
   * 내용의 유효성을 검증합니다.
   *
   * @param content 검증할 내용
   * @throws ValidationException 내용이 null이거나 길이가 범위를 벗어난 경우
   */
  public static void validateContent(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new ValidationException("내용을 입력해주세요.");
    }

    int length = content.trim().length();
    if (length < Constants.CONTENT_MIN_LENGTH || length > Constants.CONTENT_MAX_LENGTH) {
      throw new ValidationException(Constants.ERROR_CONTENT_LENGTH);
    }
  }

  /**
   * 작성자의 유효성을 검증합니다.
   *
   * @param writer 검증할 작성자
   * @throws ValidationException 작성자가 null이거나 길이가 범위를 벗어난 경우
   */
  public static void validateWriter(String writer) {
    if (writer == null || writer.trim().isEmpty()) {
      throw new ValidationException("작성자를 입력해주세요.");
    }

    int length = writer.trim().length();
    if (length < Constants.WRITER_MIN_LENGTH || length > Constants.WRITER_MAX_LENGTH) {
      throw new ValidationException(Constants.ERROR_WRITER_LENGTH);
    }
  }

  /**
   * 비밀번호의 유효성을 검증합니다.
   * 8~12자의 영문과 숫자 조합이어야 합니다.
   *
   * @param password 검증할 비밀번호
   * @throws ValidationException 비밀번호가 null이거나 형식이 올바르지 않은 경우
   */
  public static void validatePassword(String password) {
    if (password == null || password.trim().isEmpty()) {
      throw new ValidationException("비밀번호를 입력해주세요.");
    }

    if (!password.matches(Constants.PASSWORD_REGEX)) {
      throw new ValidationException(Constants.ERROR_PASSWORD_FORMAT);
    }
  }

  /**
   * 비밀번호와 비밀번호 확인의 일치 여부를 검증합니다.
   *
   * @param password 비밀번호
   * @param passwordConfirm 비밀번호 확인
   * @throws ValidationException 두 비밀번호가 일치하지 않는 경우
   */
  public static void validatePasswordConfirm(String password, String passwordConfirm) {
    if (password == null || passwordConfirm == null) {
      throw new ValidationException("비밀번호를 입력해주세요.");
    }

    if (!password.equals(passwordConfirm)) {
      throw new ValidationException("비밀번호가 일치하지 않습니다.");
    }
  }

  /**
   * 댓글 내용의 유효성을 검증합니다.
   *
   * @param content 검증할 댓글 내용
   * @throws ValidationException 댓글이 null이거나 길이가 범위를 벗어난 경우
   */
  public static void validateCommentContent(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new ValidationException("댓글 내용을 입력해주세요.");
    }

    int length = content.trim().length();
    if (length < Constants.COMMENT_MIN_LENGTH || length > Constants.COMMENT_MAX_LENGTH) {
      throw new ValidationException(Constants.ERROR_COMMENT_LENGTH);
    }
  }

  /**
   * 카테고리 ID의 유효성을 검증합니다.
   *
   * @param categoryId 검증할 카테고리 ID
   * @throws ValidationException 카테고리 ID가 null이거나 0 이하인 경우
   */
  public static void validateCategoryId(Long categoryId) {
    if (categoryId == null || categoryId <= 0) {
      throw new ValidationException("카테고리를 선택해주세요.");
    }
  }

  /**
   * 게시글 ID의 유효성을 검증합니다.
   *
   * @param boardId 검증할 게시글 ID
   * @throws ValidationException 게시글 ID가 null이거나 0 이하인 경우
   */
  public static void validateBoardId(Long boardId) {
    if (boardId == null || boardId <= 0) {
      throw new ValidationException("유효하지 않은 게시글입니다.");
    }
  }

  /**
   * 문자열이 null이 아니고 비어있지 않은지 검증합니다.
   *
   * @param value 검증할 문자열
   * @return 유효하면 true, 그렇지 않으면 false
   */
  public static boolean isNotEmpty(String value) {
    return value != null && !value.trim().isEmpty();
  }

  /**
   * 문자열이 null이거나 비어있는지 검증합니다.
   *
   * @param value 검증할 문자열
   * @return 비어있으면 true, 그렇지 않으면 false
   */
  public static boolean isEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }
}
