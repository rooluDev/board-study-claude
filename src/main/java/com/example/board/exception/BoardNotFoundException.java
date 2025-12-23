package com.example.board.exception;

/**
 * 게시글을 찾을 수 없을 때 발생하는 예외.
 *
 * <p>요청한 게시글 ID에 해당하는 게시글이 데이터베이스에 존재하지 않을 때 발생합니다.
 */
public class BoardNotFoundException extends Exception {

  /**
   * 메시지를 포함하는 BoardNotFoundException을 생성합니다.
   *
   * @param message 예외 메시지
   */
  public BoardNotFoundException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 BoardNotFoundException을 생성합니다.
   *
   * @param message 예외 메시지
   * @param cause 예외 원인
   */
  public BoardNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
