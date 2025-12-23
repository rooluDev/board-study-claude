package com.example.board.exception;

/**
 * 입력값 검증 실패 시 발생하는 예외.
 *
 * <p>사용자 입력값이 유효성 검사를 통과하지 못했을 때 발생합니다.
 * 제목, 내용, 작성자, 비밀번호 등의 입력값 검증에 사용됩니다.
 */
public class ValidationException extends RuntimeException {

  /**
   * 메시지를 포함하는 ValidationException을 생성합니다.
   *
   * @param message 예외 메시지
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 ValidationException을 생성합니다.
   *
   * @param message 예외 메시지
   * @param cause 예외 원인
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
