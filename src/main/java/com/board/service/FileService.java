package com.board.service;

import com.board.dao.FileDAO;
import com.board.dto.File;
import com.board.exception.FileUploadException;
import com.board.util.Constants;
import com.board.util.FileUtil;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 첨부파일 Service (비즈니스 로직 계층)
 *
 * 첨부파일 관련 비즈니스 로직을 처리
 * DAO를 호출하여 데이터베이스 작업 수행
 */
public class FileService {

  private static final Logger logger = LoggerFactory.getLogger(FileService.class);

  /**
   * FileDAO 인스턴스
   */
  private final FileDAO fileDAO;

  /**
   * 기본 생성자
   * FileDAO 인스턴스를 생성합니다.
   */
  public FileService() {
    this.fileDAO = new FileDAO();
  }

  /**
   * 특정 게시글의 첨부파일 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 목록
   */
  public List<File> getFilesByBoardId(Long boardId) {
    logger.info("첨부파일 목록 조회 요청: boardId={}", boardId);

    // DAO를 통해 첨부파일 목록 조회
    List<File> files = fileDAO.selectFilesByBoardId(boardId);

    logger.info("첨부파일 목록 조회 완료: {} 건", files.size());
    return files;
  }

  /**
   * 첨부파일을 업로드하고 데이터베이스에 저장합니다.
   * - 파일 개수, 확장자, 크기 검증
   * - UUID로 물리 파일명 생성
   * - 물리 파일 저장
   * - 데이터베이스 삽입
   * - 실패 시 이미 저장된 파일 삭제 (롤백)
   *
   * @param fileParts multipart/form-data에서 추출한 Part 목록
   * @param boardId 게시글 ID
   * @throws FileUploadException 파일 업로드 중 오류 발생 시
   */
  public void uploadFiles(List<Part> fileParts, Long boardId) {
    if (fileParts == null || fileParts.isEmpty()) {
      logger.debug("업로드할 파일이 없음");
      return;
    }

    logger.info("파일 업로드 시작: boardId={}, 파일 개수={}", boardId, fileParts.size());

    // 빈 파일 필터링 (파일명이 없거나 크기가 0인 경우)
    List<Part> validParts = new ArrayList<>();
    for (Part part : fileParts) {
      String submittedFileName = part.getSubmittedFileName();
      if (submittedFileName != null && !submittedFileName.trim().isEmpty() && part.getSize() > 0) {
        validParts.add(part);
      }
    }

    if (validParts.isEmpty()) {
      logger.debug("유효한 파일이 없음");
      return;
    }

    // 파일 개수 검증 (최대 3개)
    if (validParts.size() > Constants.MAX_FILE_COUNT) {
      throw new FileUploadException(
          String.format("최대 %d개의 파일만 업로드할 수 있습니다.", Constants.MAX_FILE_COUNT));
    }

    List<File> fileList = new ArrayList<>();
    List<String> savedPhysicalPaths = new ArrayList<>();

    try {
      // 각 파일 처리
      for (Part part : validParts) {
        String originalName = part.getSubmittedFileName();
        long fileSize = part.getSize();

        logger.debug("파일 처리: originalName={}, size={} bytes", originalName, fileSize);

        // 파일 크기 검증 (최대 2MB)
        if (fileSize > Constants.MAX_FILE_SIZE) {
          throw new FileUploadException(
              String.format("파일 크기는 최대 %d MB입니다. (파일: %s)",
                  Constants.MAX_FILE_SIZE / 1048576, originalName));
        }

        // 확장자 추출 및 검증
        String extension = FileUtil.getFileExtension(originalName);
        FileUtil.validateFileExtension(extension);

        // UUID로 물리 파일명 생성
        String physicalName = FileUtil.generateUUID() + "." + extension;
        String filePath = Constants.FILE_UPLOAD_PATH + physicalName;

        // 물리 파일 저장
        try (InputStream inputStream = part.getInputStream()) {
          Files.copy(inputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
          savedPhysicalPaths.add(filePath);
          logger.debug("파일 저장 완료: {}", filePath);
        }

        // File DTO 생성
        File file = new File();
        file.setBoardId(boardId);
        file.setOriginalName(originalName);
        file.setPhysicalName(physicalName);
        file.setFilePath(filePath);
        file.setExtension(extension);
        file.setSize(fileSize);

        fileList.add(file);
      }

      // 데이터베이스에 파일 정보 삽입
      if (!fileList.isEmpty()) {
        fileDAO.insertFiles(fileList);
        logger.info("파일 업로드 완료: {} 건", fileList.size());
      }

    } catch (Exception e) {
      // 실패 시 이미 저장된 물리 파일 삭제 (롤백)
      logger.error("파일 업로드 실패, 롤백 시작: {}", e.getMessage());
      for (String savedPath : savedPhysicalPaths) {
        try {
          Files.deleteIfExists(Paths.get(savedPath));
          logger.debug("물리 파일 삭제: {}", savedPath);
        } catch (Exception deleteEx) {
          logger.warn("물리 파일 삭제 실패: {}", savedPath, deleteEx);
        }
      }

      if (e instanceof FileUploadException) {
        throw (FileUploadException) e;
      } else {
        throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.", e);
      }
    }
  }

  /**
   * 여러 파일을 삭제합니다.
   * - DB에서 파일 정보 조회
   * - 물리 파일 삭제
   * - DB에서 파일 정보 삭제
   *
   * @param fileIds 삭제할 파일 ID 목록
   * @throws FileUploadException 파일 삭제 중 오류 발생 시
   */
  public void deleteFiles(List<Long> fileIds) {
    if (fileIds == null || fileIds.isEmpty()) {
      logger.debug("삭제할 파일이 없음");
      return;
    }

    logger.info("파일 삭제 시작: {} 건", fileIds.size());

    List<String> deletedPaths = new ArrayList<>();

    try {
      // 각 파일 삭제
      for (Long fileId : fileIds) {
        // DB에서 파일 정보 조회
        File file = fileDAO.selectFileById(fileId);

        if (file != null) {
          // 물리 파일 삭제
          try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
            deletedPaths.add(file.getFilePath());
            logger.debug("물리 파일 삭제 완료: {}", file.getFilePath());
          } catch (Exception e) {
            logger.warn("물리 파일 삭제 실패: {}", file.getFilePath(), e);
            // 물리 파일 삭제 실패해도 계속 진행
          }
        }
      }

      // DB에서 파일 정보 삭제
      fileDAO.deleteFilesByIds(fileIds);

      logger.info("파일 삭제 완료: {} 건", fileIds.size());

    } catch (Exception e) {
      logger.error("파일 삭제 실패: {}", e.getMessage(), e);
      throw new FileUploadException("파일 삭제 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 게시글의 모든 파일을 삭제합니다.
   * 게시글 삭제 시 사용
   * - DB에서 파일 목록 조회
   * - 물리 파일 삭제
   * - DB에서 파일 정보 삭제
   *
   * @param boardId 게시글 ID
   * @throws FileUploadException 파일 삭제 중 오류 발생 시
   */
  public void deleteAllFilesByBoardId(Long boardId) {
    logger.info("게시글 파일 전체 삭제 시작: boardId={}", boardId);

    try {
      // DB에서 파일 목록 조회
      List<File> files = fileDAO.selectFilesByBoardId(boardId);

      if (files.isEmpty()) {
        logger.debug("삭제할 파일이 없음: boardId={}", boardId);
        return;
      }

      logger.debug("삭제할 파일 개수: {} 건", files.size());

      // 각 파일의 물리적 파일 삭제
      for (File file : files) {
        try {
          Files.deleteIfExists(Paths.get(file.getFilePath()));
          logger.debug("물리 파일 삭제 완료: {}", file.getFilePath());
        } catch (Exception e) {
          logger.warn("물리 파일 삭제 실패: {}", file.getFilePath(), e);
          // 물리 파일 삭제 실패해도 계속 진행
        }
      }

      // DB에서 파일 정보 삭제
      fileDAO.deleteFilesByBoardId(boardId);

      logger.info("게시글 파일 전체 삭제 완료: boardId={}, 삭제된 파일 {} 건", boardId, files.size());

    } catch (Exception e) {
      logger.error("게시글 파일 삭제 실패: {}", e.getMessage(), e);
      throw new FileUploadException("파일 삭제 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 게시글 수정 시 파일을 업데이트합니다.
   * - 기존 파일 삭제
   * - 새 파일 업로드
   * - 총 파일 개수 검증 (최대 3개)
   *
   * @param boardId 게시글 ID
   * @param newFiles 새로 추가할 파일 Part 목록
   * @param deletedFileIds 삭제할 기존 파일 ID 목록
   * @throws FileUploadException 파일 업데이트 중 오류 발생 시
   */
  public void updateFiles(Long boardId, List<Part> newFiles, List<Long> deletedFileIds) {
    logger.info("파일 업데이트 시작: boardId={}", boardId);

    try {
      // 현재 게시글의 파일 개수 조회
      List<File> currentFiles = fileDAO.selectFilesByBoardId(boardId);
      int currentFileCount = currentFiles.size();

      logger.debug("현재 파일 개수: {}", currentFileCount);

      // 삭제할 파일 개수 계산
      int deleteCount = (deletedFileIds != null) ? deletedFileIds.size() : 0;

      // 새로 추가할 파일 개수 계산 (빈 파일 제외)
      int newFileCount = 0;
      if (newFiles != null && !newFiles.isEmpty()) {
        for (Part part : newFiles) {
          String submittedFileName = part.getSubmittedFileName();
          if (submittedFileName != null && !submittedFileName.trim().isEmpty()
              && part.getSize() > 0) {
            newFileCount++;
          }
        }
      }

      // 최종 파일 개수 계산: 현재 - 삭제 + 새 파일
      int finalFileCount = currentFileCount - deleteCount + newFileCount;

      logger.debug("파일 개수 계산: 현재={}, 삭제={}, 추가={}, 최종={}",
          currentFileCount, deleteCount, newFileCount, finalFileCount);

      // 총 파일 개수 검증 (최대 3개)
      if (finalFileCount > Constants.MAX_FILE_COUNT) {
        throw new FileUploadException(
            String.format("파일은 최대 %d개까지 첨부할 수 있습니다. (현재: %d개, 삭제: %d개, 추가: %d개)",
                Constants.MAX_FILE_COUNT, currentFileCount, deleteCount, newFileCount));
      }

      // 기존 파일 삭제
      if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
        deleteFiles(deletedFileIds);
        logger.info("기존 파일 삭제 완료: {} 건", deletedFileIds.size());
      }

      // 새 파일 업로드
      if (newFiles != null && !newFiles.isEmpty()) {
        uploadFiles(newFiles, boardId);
        logger.info("새 파일 업로드 완료");
      }

      logger.info("파일 업데이트 완료: boardId={}", boardId);

    } catch (FileUploadException e) {
      throw e;
    } catch (Exception e) {
      logger.error("파일 업데이트 실패: {}", e.getMessage(), e);
      throw new FileUploadException("파일 업데이트 중 오류가 발생했습니다.", e);
    }
  }
}
