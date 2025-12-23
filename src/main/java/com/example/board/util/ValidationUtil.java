package com.example.board.util;

import com.example.board.constants.BoardConstants;
import com.example.board.exception.ValidationException;

/**
 * 입력값 검증 유틸리티 클래스.
 *
 * <p>게시글, 댓글, 파일 등의 입력값을 검증하는 정적 메서드를 제공합니다.
 */
public class ValidationUtil {

  /**
   * 제목 검증.
   *
   * @param title 제목
   * @throws ValidationException 제목이 유효하지 않은 경우
   */
  public static void validateTitle(String title) throws ValidationException {
    if (title == null || title.trim().isEmpty()) {
      throw new ValidationException("제목을 입력해주세요.");
    }
    if (title.length() < BoardConstants.MIN_TITLE_LENGTH
        || title.length() > BoardConstants.MAX_TITLE_LENGTH) {
      throw new ValidationException(
          String.format("제목은 %d~%d자 사이여야 합니다.",
              BoardConstants.MIN_TITLE_LENGTH,
              BoardConstants.MAX_TITLE_LENGTH));
    }
  }

  /**
   * 내용 검증.
   *
   * @param content 내용
   * @throws ValidationException 내용이 유효하지 않은 경우
   */
  public static void validateContent(String content) throws ValidationException {
    if (content == null || content.trim().isEmpty()) {
      throw new ValidationException("내용을 입력해주세요.");
    }
    if (content.length() < BoardConstants.MIN_CONTENT_LENGTH
        || content.length() > BoardConstants.MAX_CONTENT_LENGTH) {
      throw new ValidationException(
          String.format("내용은 %d~%d자 사이여야 합니다.",
              BoardConstants.MIN_CONTENT_LENGTH,
              BoardConstants.MAX_CONTENT_LENGTH));
    }
  }

  /**
   * 작성자 이름 검증.
   *
   * @param userName 작성자 이름
   * @throws ValidationException 작성자 이름이 유효하지 않은 경우
   */
  public static void validateUserName(String userName) throws ValidationException {
    if (userName == null || userName.trim().isEmpty()) {
      throw new ValidationException("작성자를 입력해주세요.");
    }
    if (userName.length() < BoardConstants.MIN_USER_NAME_LENGTH
        || userName.length() > BoardConstants.MAX_USER_NAME_LENGTH) {
      throw new ValidationException(
          String.format("작성자는 %d~%d자 사이여야 합니다.",
              BoardConstants.MIN_USER_NAME_LENGTH,
              BoardConstants.MAX_USER_NAME_LENGTH));
    }
  }

  /**
   * 비밀번호 검증.
   *
   * @param password 비밀번호
   * @throws ValidationException 비밀번호가 유효하지 않은 경우
   */
  public static void validatePassword(String password) throws ValidationException {
    if (password == null || password.isEmpty()) {
      throw new ValidationException("비밀번호를 입력해주세요.");
    }
    if (password.length() < BoardConstants.MIN_PASSWORD_LENGTH
        || password.length() > BoardConstants.MAX_PASSWORD_LENGTH) {
      throw new ValidationException(
          String.format("비밀번호는 %d~%d자 사이여야 합니다.",
              BoardConstants.MIN_PASSWORD_LENGTH,
              BoardConstants.MAX_PASSWORD_LENGTH));
    }
    if (!password.matches(BoardConstants.PASSWORD_PATTERN)) {
      throw new ValidationException("비밀번호는 영문과 숫자를 포함해야 합니다.");
    }
  }

  /**
   * 비밀번호 확인 검증.
   *
   * @param password 비밀번호
   * @param passwordConfirm 비밀번호 확인
   * @throws ValidationException 비밀번호가 일치하지 않는 경우
   */
  public static void validatePasswordConfirm(String password, String passwordConfirm)
      throws ValidationException {
    if (passwordConfirm == null || passwordConfirm.isEmpty()) {
      throw new ValidationException("비밀번호 확인을 입력해주세요.");
    }
    if (!password.equals(passwordConfirm)) {
      throw new ValidationException("비밀번호가 일치하지 않습니다.");
    }
  }

  /**
   * 댓글 내용 검증.
   *
   * @param comment 댓글 내용
   * @throws ValidationException 댓글 내용이 유효하지 않은 경우
   */
  public static void validateComment(String comment) throws ValidationException {
    if (comment == null || comment.trim().isEmpty()) {
      throw new ValidationException("댓글 내용을 입력해주세요.");
    }
    if (comment.length() < BoardConstants.MIN_COMMENT_LENGTH
        || comment.length() > BoardConstants.MAX_COMMENT_LENGTH) {
      throw new ValidationException(
          String.format("댓글은 %d~%d자 사이여야 합니다.",
              BoardConstants.MIN_COMMENT_LENGTH,
              BoardConstants.MAX_COMMENT_LENGTH));
    }
  }

  /**
   * 카테고리 ID 검증.
   *
   * @param categoryId 카테고리 ID
   * @throws ValidationException 카테고리 ID가 유효하지 않은 경우
   */
  public static void validateCategoryId(Long categoryId) throws ValidationException {
    if (categoryId == null || categoryId <= 0) {
      throw new ValidationException("카테고리를 선택해주세요.");
    }
  }

  /**
   * 인스턴스화 방지를 위한 private 생성자.
   */
  private ValidationUtil() {
    throw new AssertionError("Cannot instantiate utility class");
  }
}
