package com.board.exception;

/**
 * 파일 업로드 예외 클래스
 *
 * 파일 업로드 및 처리 과정에서 발생하는 예외
 * 파일 크기 초과, 허용되지 않는 확장자, 파일 저장 실패 등에 사용
 */
public class FileUploadException extends BoardException {

  /**
   * 기본 생성자
   */
  public FileUploadException() {
    super();
  }

  /**
   * 메시지를 포함하는 생성자
   *
   * @param message 예외 메시지
   */
  public FileUploadException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 생성자
   *
   * @param message 예외 메시지
   * @param cause 원인 예외
   */
  public FileUploadException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 원인만 포함하는 생성자
   *
   * @param cause 원인 예외
   */
  public FileUploadException(Throwable cause) {
    super(cause);
  }
}
