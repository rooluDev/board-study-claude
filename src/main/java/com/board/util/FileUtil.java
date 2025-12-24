package com.board.util;

import com.board.exception.FileUploadException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 파일 처리 유틸리티 클래스
 *
 * 파일 업로드, 검증, 이름 생성 등의 파일 관련 기능 제공
 */
public final class FileUtil {

  /**
   * 생성자를 private으로 선언하여 인스턴스화 방지
   */
  private FileUtil() {
    throw new AssertionError("FileUtil 클래스는 인스턴스화할 수 없습니다.");
  }

  /**
   * UUID를 사용하여 고유한 파일명을 생성합니다.
   *
   * @return UUID 기반의 고유한 파일명
   */
  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  /**
   * 파일명에서 확장자를 추출합니다.
   *
   * @param filename 파일명
   * @return 확장자 (소문자, 점 제외). 확장자가 없으면 빈 문자열 반환
   */
  public static String getFileExtension(String filename) {
    if (filename == null || filename.trim().isEmpty()) {
      return "";
    }

    int lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
      return "";
    }

    return filename.substring(lastDotIndex + 1).toLowerCase();
  }

  /**
   * 파일 확장자가 허용된 확장자인지 검증합니다.
   *
   * @param extension 확장자 (소문자)
   * @return 허용된 확장자면 true, 그렇지 않으면 false
   */
  public static boolean isAllowedExtension(String extension) {
    if (extension == null || extension.trim().isEmpty()) {
      return false;
    }

    return Arrays.asList(Constants.ALLOWED_EXTENSIONS)
        .contains(extension.toLowerCase());
  }

  /**
   * 파일 확장자의 유효성을 검증합니다.
   *
   * @param extension 검증할 확장자
   * @throws FileUploadException 허용되지 않는 확장자인 경우
   */
  public static void validateFileExtension(String extension) {
    if (!isAllowedExtension(extension)) {
      throw new FileUploadException(Constants.ERROR_FILE_EXTENSION);
    }
  }

  /**
   * 파일 크기가 허용된 크기 이내인지 검증합니다.
   *
   * @param size 파일 크기 (bytes)
   * @return 허용된 크기면 true, 그렇지 않으면 false
   */
  public static boolean isValidFileSize(long size) {
    return size > 0 && size <= Constants.MAX_FILE_SIZE;
  }

  /**
   * 파일 크기의 유효성을 검증합니다.
   *
   * @param size 검증할 파일 크기 (bytes)
   * @throws FileUploadException 파일 크기가 0 이하이거나 최대 크기를 초과한 경우
   */
  public static void validateFileSize(long size) {
    if (size <= 0) {
      throw new FileUploadException("파일 크기가 올바르지 않습니다.");
    }

    if (size > Constants.MAX_FILE_SIZE) {
      throw new FileUploadException(Constants.ERROR_FILE_SIZE);
    }
  }

  /**
   * 파일 개수가 허용된 개수 이내인지 검증합니다.
   *
   * @param count 파일 개수
   * @throws FileUploadException 파일 개수가 최대 개수를 초과한 경우
   */
  public static void validateFileCount(int count) {
    if (count < 0) {
      throw new FileUploadException("파일 개수가 올바르지 않습니다.");
    }

    if (count > Constants.MAX_FILE_COUNT) {
      throw new FileUploadException(Constants.ERROR_FILE_COUNT);
    }
  }

  /**
   * 원본 파일명과 확장자를 결합하여 물리적 파일명을 생성합니다.
   *
   * @param extension 파일 확장자
   * @return UUID + 확장자 형식의 물리적 파일명
   */
  public static String generatePhysicalFileName(String extension) {
    if (extension == null || extension.trim().isEmpty()) {
      return generateUUID();
    }

    return generateUUID() + "." + extension.toLowerCase();
  }

  /**
   * 파일 크기를 읽기 쉬운 형식으로 변환합니다.
   *
   * @param size 파일 크기 (bytes)
   * @return 읽기 쉬운 형식의 파일 크기 (예: "1.5 MB", "256 KB")
   */
  public static String formatFileSize(long size) {
    if (size < 1024) {
      return size + " B";
    } else if (size < 1024 * 1024) {
      return String.format("%.2f KB", size / 1024.0);
    } else {
      return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }
  }
}
