package com.example.board.service;

import com.example.board.constants.BoardConstants;
import com.example.board.dao.FileDAO;
import com.example.board.dto.FileDTO;
import com.example.board.exception.FileUploadException;
import com.example.board.util.FileUtil;
import jakarta.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 파일 Service.
 */
public class FileService {

  private static final Logger logger = LoggerFactory.getLogger(FileService.class);
  private final FileDAO fileDAO;

  public FileService() {
    this.fileDAO = new FileDAO();
  }

  /**
   * 파일 정보 조회.
   *
   * @param fileId 파일 ID
   * @return 파일 DTO
   */
  public FileDTO getFile(Long fileId) {
    return fileDAO.selectById(fileId);
  }

  /**
   * 게시글의 파일 목록 조회.
   *
   * @param boardId 게시글 ID
   * @return 파일 목록
   */
  public List<FileDTO> getFilesByBoardId(Long boardId) {
    return fileDAO.selectByBoardId(boardId);
  }

  /**
   * 파일 업로드 및 등록.
   *
   * @param parts 파일 Part 목록
   * @param boardId 게시글 ID
   * @param uploadBasePath 업로드 기본 경로
   * @return 등록된 파일 DTO 목록
   * @throws Exception 업로드 실패 시
   */
  public List<FileDTO> uploadFiles(List<Part> parts, Long boardId, String uploadBasePath)
      throws Exception {
    logger.debug("파일 업로드 시작: boardId={}, 파일 개수={}", boardId, parts.size());

    List<FileDTO> uploadedFiles = new ArrayList<>();

    if (parts == null || parts.isEmpty()) {
      return uploadedFiles;
    }

    // 파일 개수 검증
    int validFileCount = 0;
    for (Part part : parts) {
      if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
        validFileCount++;
      }
    }

    if (validFileCount > BoardConstants.MAX_FILE_COUNT) {
      throw new FileUploadException(
          String.format("파일은 최대 %d개까지 업로드 가능합니다.", BoardConstants.MAX_FILE_COUNT));
    }

    // 날짜 기반 저장 경로 생성
    String filePath = FileUtil.generateFilePath();
    String fullPath = FileUtil.createDirectory(uploadBasePath, filePath);

    for (Part part : parts) {
      if (part.getSubmittedFileName() == null || part.getSubmittedFileName().isEmpty()) {
        continue;
      }

      // 파일 검증
      FileUtil.validatePart(part);

      String originalName = part.getSubmittedFileName();
      String physicalName = FileUtil.generatePhysicalName(originalName);
      String extension = FileUtil.getExtension(originalName);

      // 파일 저장
      FileUtil.saveFile(part, fullPath, physicalName);

      // 파일 정보 DB 저장
      FileDTO fileDTO = new FileDTO();
      fileDTO.setBoardId(boardId);
      fileDTO.setOriginalName(originalName);
      fileDTO.setPhysicalName(physicalName);
      fileDTO.setFilePath(filePath);
      fileDTO.setExtension(extension);
      fileDTO.setSize(part.getSize());

      fileDAO.insert(fileDTO);
      uploadedFiles.add(fileDTO);

      logger.info("파일 업로드 성공: fileId={}, originalName={}",
          fileDTO.getFileId(), originalName);
    }

    return uploadedFiles;
  }

  /**
   * 파일 삭제.
   *
   * @param fileId 파일 ID
   * @param uploadBasePath 업로드 기본 경로
   * @throws Exception 삭제 실패 시
   */
  public void deleteFile(Long fileId, String uploadBasePath) throws Exception {
    FileDTO file = fileDAO.selectById(fileId);
    if (file != null) {
      // 물리적 파일 삭제
      FileUtil.deleteFile(uploadBasePath, file.getFilePath(), file.getPhysicalName());

      // DB에서 삭제
      fileDAO.deleteById(fileId);
      logger.info("파일 삭제 성공: fileId={}", fileId);
    }
  }

  /**
   * 파일 목록 삭제.
   *
   * @param fileIds 파일 ID 목록
   * @param uploadBasePath 업로드 기본 경로
   * @throws Exception 삭제 실패 시
   */
  public void deleteFiles(List<Long> fileIds, String uploadBasePath) throws Exception {
    if (fileIds == null || fileIds.isEmpty()) {
      return;
    }

    for (Long fileId : fileIds) {
      deleteFile(fileId, uploadBasePath);
    }
  }
}