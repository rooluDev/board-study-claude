package com.board.exception;

/**
 * 인증 예외 클래스
 *
 * 비밀번호 검증 실패 등 인증 과정에서 발생하는 예외
 * 게시글 수정/삭제 시 비밀번호 불일치에 사용
 */
public class AuthenticationException extends BoardException {

  /**
   * 기본 생성자
   */
  public AuthenticationException() {
    super();
  }

  /**
   * 메시지를 포함하는 생성자
   *
   * @param message 예외 메시지
   */
  public AuthenticationException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 생성자
   *
   * @param message 예외 메시지
   * @param cause 원인 예외
   */
  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 원인만 포함하는 생성자
   *
   * @param cause 원인 예외
   */
  public AuthenticationException(Throwable cause) {
    super(cause);
  }
}
