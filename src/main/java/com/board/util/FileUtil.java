package com.board.util;

import com.board.exception.FileUploadException;

import java.util.UUID;

import static com.board.util.Constants.*;

/**
 * 파일 처리를 담당하는 유틸리티 클래스
 *
 * 파일명 생성, 확장자 추출, 유효성 검증 등을 제공합니다.
 */
public final class FileUtil {
  /**
   * private 생성자로 인스턴스화 방지
   */
  private FileUtil() {
    throw new AssertionError("Cannot instantiate FileUtil class");
  }

  /**
   * UUID를 사용하여 고유한 파일명을 생성합니다.
   *
   * @return UUID 문자열
   */
  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  /**
   * 파일명에서 확장자를 추출합니다.
   *
   * @param filename 파일명
   * @return 확장자 (소문자, 점 제외)
   * @throws FileUploadException 확장자가 없는 경우
   */
  public static String getFileExtension(String filename) {
    if (filename == null || filename.isEmpty()) {
      throw new FileUploadException("파일명이 유효하지 않습니다.");
    }

    int lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
      throw new FileUploadException("파일 확장자가 없습니다.");
    }

    return filename.substring(lastDotIndex + 1).toLowerCase();
  }

  /**
   * 파일 확장자가 허용된 확장자인지 확인합니다.
   *
   * @param extension 확장자 (소문자)
   * @return 허용되면 true, 아니면 false
   */
  public static boolean isAllowedExtension(String extension) {
    if (extension == null || extension.isEmpty()) {
      return false;
    }
    return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
  }

  /**
   * 파일 확장자 유효성을 검증합니다.
   *
   * @param extension 확장자
   * @throws FileUploadException 허용되지 않은 확장자인 경우
   */
  public static void validateFileExtension(String extension) {
    if (!isAllowedExtension(extension)) {
      throw new FileUploadException(ERR_FILE_EXTENSION_NOT_ALLOWED);
    }
  }

  /**
   * 파일 크기가 제한 범위 내인지 확인합니다.
   *
   * @param size 파일 크기 (bytes)
   * @return 범위 내이면 true, 아니면 false
   */
  public static boolean isValidFileSize(long size) {
    return size > 0 && size <= MAX_FILE_SIZE;
  }

  /**
   * 파일 크기 유효성을 검증합니다.
   *
   * @param size 파일 크기 (bytes)
   * @throws FileUploadException 파일 크기가 제한을 초과한 경우
   */
  public static void validateFileSize(long size) {
    if (size <= 0) {
      throw new FileUploadException("파일 크기가 유효하지 않습니다.");
    }
    if (size > MAX_FILE_SIZE) {
      throw new FileUploadException(ERR_FILE_SIZE_EXCEEDED);
    }
  }

  /**
   * 파일 개수가 제한 범위 내인지 확인합니다.
   *
   * @param count 파일 개수
   * @return 범위 내이면 true, 아니면 false
   */
  public static boolean isValidFileCount(int count) {
    return count >= 0 && count <= MAX_FILE_COUNT;
  }

  /**
   * 파일 개수 유효성을 검증합니다.
   *
   * @param count 파일 개수
   * @throws FileUploadException 파일 개수가 제한을 초과한 경우
   */
  public static void validateFileCount(int count) {
    if (count > MAX_FILE_COUNT) {
      throw new FileUploadException(ERR_FILE_COUNT_EXCEEDED);
    }
  }

  /**
   * 확장자를 포함한 물리적 파일명을 생성합니다.
   *
   * @param extension 파일 확장자
   * @return UUID.확장자 형식의 파일명
   */
  public static String generatePhysicalName(String extension) {
    if (extension == null || extension.isEmpty()) {
      throw new FileUploadException("파일 확장자가 없습니다.");
    }
    return generateUUID() + "." + extension.toLowerCase();
  }

  /**
   * 파일 크기를 사람이 읽기 쉬운 형식으로 변환합니다.
   *
   * @param size 파일 크기 (bytes)
   * @return 변환된 문자열 (예: 1.5 MB)
   */
  public static String formatFileSize(long size) {
    if (size < 1024) {
      return size + " B";
    } else if (size < 1024 * 1024) {
      return String.format("%.1f KB", size / 1024.0);
    } else {
      return String.format("%.1f MB", size / (1024.0 * 1024.0));
    }
  }
}
