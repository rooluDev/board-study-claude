package com.board.exception;

/**
 * 입력값 검증 예외 클래스
 *
 * 사용자 입력값 검증 실패 시 발생하는 예외
 * 제목, 내용, 작성자, 비밀번호 등의 유효성 검증에 사용
 */
public class ValidationException extends BoardException {

  /**
   * 기본 생성자
   */
  public ValidationException() {
    super();
  }

  /**
   * 메시지를 포함하는 생성자
   *
   * @param message 예외 메시지
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 생성자
   *
   * @param message 예외 메시지
   * @param cause 원인 예외
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 원인만 포함하는 생성자
   *
   * @param cause 원인 예외
   */
  public ValidationException(Throwable cause) {
    super(cause);
  }
}
