package com.board.exception;

/**
 * 게시판 시스템의 기본 예외 클래스
 *
 * 모든 커스텀 예외의 부모 클래스
 */
public class BoardException extends RuntimeException {
  /** 사용자에게 표시할 메시지 */
  private final String userMessage;

  /**
   * 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지 (로그용)
   */
  public BoardException(String message) {
    super(message);
    this.userMessage = message;
  }

  /**
   * 메시지와 사용자 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지 (로그용)
   * @param userMessage 사용자에게 표시할 메시지
   */
  public BoardException(String message, String userMessage) {
    super(message);
    this.userMessage = userMessage;
  }

  /**
   * 메시지와 원인 예외를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지
   * @param cause 원인 예외
   */
  public BoardException(String message, Throwable cause) {
    super(message, cause);
    this.userMessage = message;
  }

  /**
   * 메시지, 사용자 메시지, 원인 예외를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지
   * @param userMessage 사용자에게 표시할 메시지
   * @param cause 원인 예외
   */
  public BoardException(String message, String userMessage, Throwable cause) {
    super(message, cause);
    this.userMessage = userMessage;
  }

  /**
   * 사용자에게 표시할 메시지를 반환합니다.
   *
   * @return 사용자 메시지
   */
  public String getUserMessage() {
    return userMessage;
  }
}
