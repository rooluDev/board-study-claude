package com.board.exception;

/**
 * 파일 업로드 실패 시 발생하는 예외
 *
 * 파일 크기 초과, 허용되지 않은 확장자, I/O 오류 등이 발생할 때 사용됩니다.
 */
public class FileUploadException extends BoardException {
  /**
   * 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 파일 업로드 실패 메시지
   */
  public FileUploadException(String message) {
    super(message);
  }

  /**
   * 메시지와 사용자 메시지를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지 (로그용)
   * @param userMessage 사용자에게 표시할 메시지
   */
  public FileUploadException(String message, String userMessage) {
    super(message, userMessage);
  }

  /**
   * 메시지와 원인 예외를 포함하는 예외를 생성합니다.
   *
   * @param message 파일 업로드 실패 메시지
   * @param cause 원인 예외
   */
  public FileUploadException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 메시지, 사용자 메시지, 원인 예외를 포함하는 예외를 생성합니다.
   *
   * @param message 개발자용 메시지
   * @param userMessage 사용자에게 표시할 메시지
   * @param cause 원인 예외
   */
  public FileUploadException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, cause);
  }
}
