package com.example.board.exception;

/**
 * 파일 업로드 실패 시 발생하는 예외.
 *
 * <p>파일 크기 초과, 허용되지 않는 확장자, 파일 저장 실패 등
 * 파일 업로드 과정에서 오류가 발생했을 때 사용됩니다.
 */
public class FileUploadException extends RuntimeException {

  /**
   * 메시지를 포함하는 FileUploadException을 생성합니다.
   *
   * @param message 예외 메시지
   */
  public FileUploadException(String message) {
    super(message);
  }

  /**
   * 메시지와 원인을 포함하는 FileUploadException을 생성합니다.
   *
   * @param message 예외 메시지
   * @param cause 예외 원인
   */
  public FileUploadException(String message, Throwable cause) {
    super(message, cause);
  }
}
