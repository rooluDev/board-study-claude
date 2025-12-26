package com.board.util;

import com.board.exception.ValidationException;

import java.util.regex.Pattern;

import static com.board.util.Constants.*;

/**
 * 입력값 검증을 담당하는 유틸리티 클래스
 *
 * 모든 메서드는 static으로 선언되며, 검증 실패 시 ValidationException을 발생시킵니다.
 */
public final class ValidationUtil {
  /** 비밀번호 패턴 (영문 + 숫자 조합) */
  private static final Pattern PASSWORD_REGEX = Pattern.compile(PASSWORD_PATTERN);

  /**
   * private 생성자로 인스턴스화 방지
   */
  private ValidationUtil() {
    throw new AssertionError("Cannot instantiate ValidationUtil class");
  }

  /**
   * 제목 유효성을 검증합니다.
   *
   * @param title 검증할 제목
   * @throws ValidationException 검증 실패 시
   */
  public static void validateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new ValidationException("제목을 입력해주세요.");
    }

    int length = title.trim().length();
    if (length < TITLE_MIN_LENGTH || length > TITLE_MAX_LENGTH) {
      throw new ValidationException(ERR_TITLE_INVALID);
    }
  }

  /**
   * 내용 유효성을 검증합니다.
   *
   * @param content 검증할 내용
   * @throws ValidationException 검증 실패 시
   */
  public static void validateContent(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new ValidationException("내용을 입력해주세요.");
    }

    int length = content.trim().length();
    if (length < CONTENT_MIN_LENGTH || length > CONTENT_MAX_LENGTH) {
      throw new ValidationException(ERR_CONTENT_INVALID);
    }
  }

  /**
   * 작성자 유효성을 검증합니다.
   *
   * @param writer 검증할 작성자
   * @throws ValidationException 검증 실패 시
   */
  public static void validateWriter(String writer) {
    if (writer == null || writer.trim().isEmpty()) {
      throw new ValidationException("작성자를 입력해주세요.");
    }

    int length = writer.trim().length();
    if (length < WRITER_MIN_LENGTH || length > WRITER_MAX_LENGTH) {
      throw new ValidationException(ERR_WRITER_INVALID);
    }
  }

  /**
   * 비밀번호 유효성을 검증합니다.
   * 8~12자, 영문+숫자 조합이어야 합니다.
   *
   * @param password 검증할 비밀번호
   * @throws ValidationException 검증 실패 시
   */
  public static void validatePassword(String password) {
    if (password == null || password.isEmpty()) {
      throw new ValidationException("비밀번호를 입력해주세요.");
    }

    if (!PASSWORD_REGEX.matcher(password).matches()) {
      throw new ValidationException(ERR_PASSWORD_INVALID);
    }
  }

  /**
   * 비밀번호 확인 일치 여부를 검증합니다.
   *
   * @param password 비밀번호
   * @param passwordConfirm 비밀번호 확인
   * @throws ValidationException 검증 실패 시
   */
  public static void validatePasswordConfirm(String password, String passwordConfirm) {
    if (passwordConfirm == null || passwordConfirm.isEmpty()) {
      throw new ValidationException("비밀번호 확인을 입력해주세요.");
    }

    if (!password.equals(passwordConfirm)) {
      throw new ValidationException("비밀번호가 일치하지 않습니다.");
    }
  }

  /**
   * 댓글 내용 유효성을 검증합니다.
   *
   * @param content 검증할 댓글 내용
   * @throws ValidationException 검증 실패 시
   */
  public static void validateCommentContent(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new ValidationException("댓글 내용을 입력해주세요.");
    }

    int length = content.trim().length();
    if (length < COMMENT_MIN_LENGTH || length > COMMENT_MAX_LENGTH) {
      throw new ValidationException(ERR_COMMENT_INVALID);
    }
  }

  /**
   * 카테고리 ID 유효성을 검증합니다.
   *
   * @param categoryId 검증할 카테고리 ID
   * @throws ValidationException 검증 실패 시
   */
  public static void validateCategoryId(Long categoryId) {
    if (categoryId == null || categoryId <= 0) {
      throw new ValidationException("카테고리를 선택해주세요.");
    }
  }

  /**
   * 게시글 ID 유효성을 검증합니다.
   *
   * @param boardId 검증할 게시글 ID
   * @throws ValidationException 검증 실패 시
   */
  public static void validateBoardId(Long boardId) {
    if (boardId == null || boardId <= 0) {
      throw new ValidationException("유효하지 않은 게시글입니다.");
    }
  }

  /**
   * 페이지 번호 유효성을 검증합니다.
   *
   * @param page 검증할 페이지 번호
   * @return 검증된 페이지 번호 (기본값: 1)
   */
  public static int validatePage(Integer page) {
    if (page == null || page < 1) {
      return 1;
    }
    return page;
  }

  /**
   * 문자열이 비어있는지 확인합니다.
   *
   * @param str 확인할 문자열
   * @return 비어있으면 true, 아니면 false
   */
  public static boolean isEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * 문자열이 비어있지 않은지 확인합니다.
   *
   * @param str 확인할 문자열
   * @return 비어있지 않으면 true, 아니면 false
   */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }
}
