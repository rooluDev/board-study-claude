package com.board.exception;

/**
 * 입력값 검증 실패 시 발생하는 예외
 *
 * 사용자 입력값이 유효성 검증을 통과하지 못했을 때 발생합니다.
 */
public class ValidationException extends BoardException {
  /**
   * 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 검증 실패 메시지
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * 메시지와 사용자 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지 (로그용)
   * @param userMessage 사용자에게 표시할 메시지
   */
  public ValidationException(String message, String userMessage) {
    super(message, userMessage);
  }

  /**
   * 메시지와 원인 예외를 포함하는 예외를 생성합니다.
   *
   * @param message 검증 실패 메시지
   * @param cause 원인 예외
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
