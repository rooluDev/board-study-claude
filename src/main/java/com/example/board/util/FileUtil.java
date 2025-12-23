package com.example.board.util;

import com.example.board.constants.BoardConstants;
import com.example.board.exception.FileUploadException;
import com.example.board.exception.ValidationException;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

/**
 * 파일 처리 유틸리티 클래스.
 */
public class FileUtil {

  /**
   * 파일 확장자 추출.
   *
   * @param fileName 파일명
   * @return 확장자 (소문자)
   */
  public static String getExtension(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      return "";
    }
    return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
  }

  /**
   * 파일 크기 검증.
   *
   * @param size 파일 크기 (bytes)
   * @throws ValidationException 파일 크기가 초과된 경우
   */
  public static void validateFileSize(long size) throws ValidationException {
    if (size > BoardConstants.MAX_FILE_SIZE) {
      throw new ValidationException(
          String.format("파일 크기는 %dMB를 초과할 수 없습니다.",
              BoardConstants.MAX_FILE_SIZE / 1024 / 1024));
    }
  }

  /**
   * 파일 확장자 검증.
   *
   * @param extension 확장자
   * @throws ValidationException 허용되지 않는 확장자인 경우
   */
  public static void validateExtension(String extension) throws ValidationException {
    if (!Arrays.asList(BoardConstants.ALLOWED_EXTENSIONS).contains(extension)) {
      throw new ValidationException(
          String.format("허용되는 파일 형식: %s",
              String.join(", ", BoardConstants.ALLOWED_EXTENSIONS)));
    }
  }

  /**
   * Part 검증.
   *
   * @param part 파일 Part
   * @throws ValidationException 파일이 유효하지 않은 경우
   */
  public static void validatePart(Part part) throws ValidationException {
    if (part == null || part.getSubmittedFileName() == null
        || part.getSubmittedFileName().isEmpty()) {
      return; // 파일이 없는 경우 검증하지 않음
    }

    String fileName = part.getSubmittedFileName();
    String extension = getExtension(fileName);

    validateFileSize(part.getSize());
    validateExtension(extension);
  }

  /**
   * UUID 기반 물리적 파일명 생성.
   *
   * @param originalFileName 원본 파일명
   * @return UUID + 확장자
   */
  public static String generatePhysicalName(String originalFileName) {
    String extension = getExtension(originalFileName);
    return UUID.randomUUID().toString() + "." + extension;
  }

  /**
   * 날짜 기반 저장 경로 생성 (YYYY/MM/DD).
   *
   * @return 저장 경로
   */
  public static String generateFilePath() {
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern(BoardConstants.FILE_PATH_DATE_FORMAT);
    return LocalDateTime.now().format(formatter);
  }

  /**
   * 파일 저장 디렉토리 생성.
   *
   * @param basePath 기본 경로
   * @param filePath 파일 경로 (YYYY/MM/DD)
   * @return 전체 디렉토리 경로
   * @throws FileUploadException 디렉토리 생성 실패 시
   */
  public static String createDirectory(String basePath, String filePath)
      throws FileUploadException {
    Path directoryPath = Paths.get(basePath, filePath);
    File directory = directoryPath.toFile();

    if (!directory.exists()) {
      try {
        Files.createDirectories(directoryPath);
      } catch (IOException e) {
        throw new FileUploadException("디렉토리 생성 실패: " + directoryPath, e);
      }
    }

    return directoryPath.toString();
  }

  /**
   * 파일 저장.
   *
   * @param part 파일 Part
   * @param directoryPath 저장 디렉토리 경로
   * @param physicalName 물리적 파일명
   * @throws FileUploadException 파일 저장 실패 시
   */
  public static void saveFile(Part part, String directoryPath, String physicalName)
      throws FileUploadException {
    try {
      Path filePath = Paths.get(directoryPath, physicalName);
      part.write(filePath.toString());
    } catch (IOException e) {
      throw new FileUploadException("파일 저장 실패: " + physicalName, e);
    }
  }

  /**
   * 파일 삭제.
   *
   * @param basePath 기본 경로
   * @param filePath 파일 경로 (YYYY/MM/DD)
   * @param physicalName 물리적 파일명
   */
  public static void deleteFile(String basePath, String filePath, String physicalName) {
    try {
      Path fullPath = Paths.get(basePath, filePath, physicalName);
      Files.deleteIfExists(fullPath);
    } catch (IOException e) {
      // 파일 삭제 실패 시 로그만 남기고 예외를 던지지 않음
      e.printStackTrace();
    }
  }

  /**
   * 인스턴스화 방지를 위한 private 생성자.
   */
  private FileUtil() {
    throw new AssertionError("Cannot instantiate utility class");
  }
}
