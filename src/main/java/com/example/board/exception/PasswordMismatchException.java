package com.example.board.exception;

/**
 * 비밀번호가 일치하지 않을 때 발생하는 예외.
 *
 * <p>게시글 수정 또는 삭제 시 입력한 비밀번호가 저장된 비밀번호와 일치하지 않을 때 발생합니다.
 */
public class PasswordMismatchException extends RuntimeException {

  /**
   * 메시지를 포함하는 PasswordMismatchException을 생성합니다.
   *
   * @param message 예외 메시지
   */
  public PasswordMismatchException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 PasswordMismatchException을 생성합니다.
   *
   * @param message 예외 메시지
   * @param cause 예외 원인
   */
  public PasswordMismatchException(String message, Throwable cause) {
    super(message, cause);
  }
}
