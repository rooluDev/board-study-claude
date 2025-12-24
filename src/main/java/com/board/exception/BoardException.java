package com.board.exception;

/**
 * 게시판 시스템 기본 예외 클래스
 *
 * 게시판 애플리케이션에서 발생하는 모든 예외의 최상위 클래스
 * RuntimeException을 상속하여 Unchecked Exception으로 구현
 */
public class BoardException extends RuntimeException {

  /**
   * 기본 생성자
   */
  public BoardException() {
    super();
  }

  /**
   * 메시지를 포함하는 생성자
   *
   * @param message 예외 메시지
   */
  public BoardException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 생성자
   *
   * @param message 예외 메시지
   * @param cause 원인 예외
   */
  public BoardException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 원인만 포함하는 생성자
   *
   * @param cause 원인 예외
   */
  public BoardException(Throwable cause) {
    super(cause);
  }
}
