package com.board.exception;

/**
 * 인증 실패 시 발생하는 예외
 *
 * 비밀번호 불일치 등 인증 관련 오류가 발생할 때 사용됩니다.
 */
public class AuthenticationException extends BoardException {
  /**
   * 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 인증 실패 메시지
   */
  public AuthenticationException(String message) {
    super(message);
  }

  /**
   * 메시지와 사용자 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지 (로그용)
   * @param userMessage 사용자에게 표시할 메시지
   */
  public AuthenticationException(String message, String userMessage) {
    super(message, userMessage);
  }

  /**
   * 메시지와 원인 예외를 포함하는 예외를 생성합니다.
   *
   * @param message 인증 실패 메시지
   * @param cause 원인 예외
   */
  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
