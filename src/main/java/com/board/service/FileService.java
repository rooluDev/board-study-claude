package com.board.service;

import com.board.dao.FileDAO;
import com.board.dto.BoardFile;
import com.board.exception.BoardException;
import com.board.exception.FileUploadException;
import com.board.exception.ValidationException;
import com.board.util.FileUtil;
import com.board.util.ValidationUtil;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.board.util.Constants.*;

/**
 * 첨부파일 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * DAO를 통해 데이터를 조회하고, 비즈니스 규칙을 적용합니다.
 */
public class FileService {
  /** FileDAO 인스턴스 */
  private final FileDAO fileDAO;

  /**
   * 기본 생성자
   * FileDAO를 초기화합니다.
   */
  public FileService() {
    this.fileDAO = new FileDAO();
  }

  /**
   * 의존성 주입을 위한 생성자
   *
   * @param fileDAO FileDAO 인스턴스
   */
  public FileService(FileDAO fileDAO) {
    this.fileDAO = fileDAO;
  }

  /**
   * 게시글 ID로 첨부파일 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 목록
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 조회 실패 시
   */
  public List<BoardFile> getFilesByBoardId(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 첨부파일 목록 조회
    return fileDAO.selectFilesByBoardId(boardId);
  }

  /**
   * 파일 ID로 첨부파일을 조회합니다.
   *
   * @param fileId 파일 ID
   * @return 첨부파일 정보
   * @throws BoardException 파일이 존재하지 않거나 조회 실패 시
   */
  public BoardFile getFileById(Long fileId) {
    if (fileId == null || fileId <= 0) {
      throw new BoardException("유효하지 않은 파일입니다.");
    }

    BoardFile file = fileDAO.selectFileById(fileId);

    if (file == null) {
      throw new BoardException("파일을 찾을 수 없습니다.");
    }

    return file;
  }

  /**
   * 첨부파일을 등록합니다.
   *
   * @param file 등록할 첨부파일 정보
   * @return 생성된 파일 ID
   * @throws BoardException 등록 실패 시
   */
  public Long createFile(BoardFile file) {
    // 필수 필드 검증
    if (file.getBoardId() == null || file.getBoardId() <= 0) {
      throw new BoardException("유효하지 않은 게시글입니다.");
    }
    if (file.getOriginalName() == null || file.getOriginalName().isEmpty()) {
      throw new BoardException("파일명이 유효하지 않습니다.");
    }

    // 첨부파일 등록
    return fileDAO.insertFile(file);
  }

  /**
   * 첨부파일을 삭제합니다.
   *
   * @param fileId 삭제할 파일 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteFile(Long fileId) {
    if (fileId == null || fileId <= 0) {
      throw new BoardException("유효하지 않은 파일입니다.");
    }

    fileDAO.deleteFile(fileId);
  }

  /**
   * 게시글의 모든 첨부파일을 삭제합니다.
   *
   * @param boardId 게시글 ID
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 삭제 실패 시
   */
  public void deleteFilesByBoardId(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 모든 첨부파일 삭제
    fileDAO.deleteFilesByBoardId(boardId);
  }

  /**
   * 게시글의 첨부파일 개수를 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 개수
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 조회 실패 시
   */
  public int getFileCountByBoardId(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 첨부파일 개수 조회
    return fileDAO.countFilesByBoardId(boardId);
  }

  /**
   * 여러 파일을 업로드하고 DB에 저장합니다.
   *
   * @param fileParts 업로드할 파일 Part 목록
   * @param boardId 게시글 ID
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @return 업로드된 파일 목록
   * @throws FileUploadException 파일 업로드 실패 시
   * @throws ValidationException 파일 검증 실패 시
   */
  public List<BoardFile> uploadFiles(Collection<Part> fileParts, Long boardId,
                                     String uploadPath) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // null이거나 비어있으면 빈 리스트 반환
    if (fileParts == null || fileParts.isEmpty()) {
      return new ArrayList<>();
    }

    // 실제 파일이 있는 Part만 필터링 (파일명이 있는 것만)
    List<Part> actualFileParts = fileParts.stream()
        .filter(part -> part.getSubmittedFileName() != null &&
            !part.getSubmittedFileName().isEmpty())
        .toList();

    // 파일 개수 검증
    FileUtil.validateFileCount(actualFileParts.size());

    // 업로드 디렉토리 생성
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }

    List<BoardFile> uploadedFiles = new ArrayList<>();

    try {
      for (Part filePart : actualFileParts) {
        // 원본 파일명
        String originalName = filePart.getSubmittedFileName();

        // 파일 크기 검증
        long fileSize = filePart.getSize();
        FileUtil.validateFileSize(fileSize);

        // 확장자 추출 및 검증
        String extension = FileUtil.getFileExtension(originalName);
        FileUtil.validateFileExtension(extension);

        // UUID 파일명 생성
        String physicalName = FileUtil.generatePhysicalName(extension);

        // 물리적 파일 저장
        File destFile = new File(uploadDir, physicalName);
        try (InputStream input = filePart.getInputStream()) {
          Files.copy(input, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // BoardFile 객체 생성
        BoardFile boardFile = new BoardFile();
        boardFile.setBoardId(boardId);
        boardFile.setOriginalName(originalName);
        boardFile.setPhysicalName(physicalName);
        boardFile.setFilePath(UPLOAD_DIR);
        boardFile.setExtension(extension);
        boardFile.setSize(fileSize);

        // DB에 파일 정보 저장
        Long fileId = fileDAO.insertFile(boardFile);
        boardFile.setFileId(fileId);

        uploadedFiles.add(boardFile);
      }

      return uploadedFiles;

    } catch (IOException e) {
      // 업로드 실패 시 이미 저장된 물리적 파일 삭제
      rollbackUploadedFiles(uploadedFiles, uploadPath);

      throw new FileUploadException(
          "File upload failed: " + e.getMessage(),
          "파일 업로드에 실패했습니다.",
          e
      );
    } catch (FileUploadException | ValidationException e) {
      // 검증 실패 시 이미 저장된 물리적 파일 삭제
      rollbackUploadedFiles(uploadedFiles, uploadPath);
      throw e;
    } catch (Exception e) {
      // 예상치 못한 오류 시 이미 저장된 물리적 파일 삭제
      rollbackUploadedFiles(uploadedFiles, uploadPath);

      throw new FileUploadException(
          "Unexpected error during file upload: " + e.getMessage(),
          "파일 업로드 중 오류가 발생했습니다.",
          e
      );
    }
  }

  /**
   * 업로드 실패 시 물리적 파일을 삭제합니다.
   *
   * @param uploadedFiles 업로드된 파일 목록
   * @param uploadPath 업로드 디렉토리 경로
   */
  private void rollbackUploadedFiles(List<BoardFile> uploadedFiles, String uploadPath) {
    for (BoardFile file : uploadedFiles) {
      try {
        File physicalFile = new File(uploadPath, file.getPhysicalName());
        if (physicalFile.exists()) {
          physicalFile.delete();
        }
      } catch (Exception e) {
        // 삭제 실패는 무시 (로그만 출력)
        System.err.println("Failed to delete uploaded file: " +
            file.getPhysicalName());
      }
    }
  }

  /**
   * 여러 파일을 삭제합니다. (DB + 물리 파일)
   *
   * @param fileIds 삭제할 파일 ID 리스트
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @throws BoardException 삭제 실패 시
   */
  public void deleteFiles(List<Long> fileIds, String uploadPath) {
    if (fileIds == null || fileIds.isEmpty()) {
      return;
    }

    // DB에서 파일 정보 조회 (물리 파일명 확인용)
    List<BoardFile> files = new ArrayList<>();
    for (Long fileId : fileIds) {
      BoardFile file = fileDAO.selectFileById(fileId);
      if (file != null) {
        files.add(file);
      }
    }

    // DB에서 파일 정보 삭제
    fileDAO.deleteFilesByIds(fileIds);

    // 물리적 파일 삭제
    for (BoardFile file : files) {
      try {
        File physicalFile = new File(uploadPath, file.getPhysicalName());
        if (physicalFile.exists()) {
          physicalFile.delete();
        }
      } catch (Exception e) {
        // 물리 파일 삭제 실패는 로그만 출력
        System.err.println("Failed to delete physical file: " +
            file.getPhysicalName());
      }
    }
  }

  /**
   * 게시글의 파일을 수정합니다. (기존 파일 삭제 + 새 파일 추가)
   *
   * @param boardId 게시글 ID
   * @param newFileParts 새로 추가할 파일 Part 목록
   * @param deletedFileIds 삭제할 파일 ID 리스트
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @return 새로 업로드된 파일 목록
   * @throws ValidationException 파일 개수 초과 시
   * @throws FileUploadException 파일 업로드 실패 시
   * @throws BoardException 파일 삭제 실패 시
   */
  public List<BoardFile> updateFiles(Long boardId, Collection<Part> newFileParts,
                                      List<Long> deletedFileIds, String uploadPath) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 현재 파일 개수 조회
    int currentFileCount = fileDAO.countFilesByBoardId(boardId);

    // 삭제할 파일 개수
    int deleteCount = (deletedFileIds != null) ? deletedFileIds.size() : 0;

    // 실제 파일이 있는 Part만 필터링
    List<Part> actualNewFileParts = new ArrayList<>();
    if (newFileParts != null) {
      actualNewFileParts = newFileParts.stream()
          .filter(part -> part.getSubmittedFileName() != null &&
              !part.getSubmittedFileName().isEmpty())
          .toList();
    }

    int newFileCount = actualNewFileParts.size();

    // 총 파일 개수 검증: 현재 - 삭제 + 새 파일 <= 3
    int totalFileCount = currentFileCount - deleteCount + newFileCount;
    FileUtil.validateFileCount(totalFileCount);

    // 기존 파일 삭제
    if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
      deleteFiles(deletedFileIds, uploadPath);
    }

    // 새 파일 업로드
    if (!actualNewFileParts.isEmpty()) {
      return uploadFiles(actualNewFileParts, boardId, uploadPath);
    }

    return new ArrayList<>();
  }

  /**
   * 게시글의 모든 파일을 삭제합니다. (DB + 물리 파일)
   *
   * @param boardId 게시글 ID
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 삭제 실패 시
   */
  public void deleteAllFilesByBoardId(Long boardId, String uploadPath) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // DB에서 파일 정보 조회 (물리 파일명 확인용)
    List<BoardFile> files = fileDAO.selectFilesByBoardId(boardId);

    if (files == null || files.isEmpty()) {
      return;
    }

    // DB에서 파일 정보 삭제
    fileDAO.deleteFilesByBoardId(boardId);

    // 물리적 파일 삭제
    for (BoardFile file : files) {
      try {
        File physicalFile = new File(uploadPath, file.getPhysicalName());
        if (physicalFile.exists()) {
          physicalFile.delete();
        }
      } catch (Exception e) {
        // 물리 파일 삭제 실패는 로그만 출력
        System.err.println("Failed to delete physical file: " +
            file.getPhysicalName());
      }
    }
  }

  /**
   * 파일을 다운로드합니다.
   *
   * @param fileId 파일 ID
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @return 다운로드할 파일의 BoardFile 정보
   * @throws BoardException 파일이 존재하지 않거나 조회 실패 시
   * @throws FileUploadException 물리적 파일이 존재하지 않는 경우
   */
  public BoardFile downloadFile(Long fileId, String uploadPath) {
    // 파일 ID 유효성 검증
    if (fileId == null || fileId <= 0) {
      throw new BoardException("유효하지 않은 파일입니다.");
    }

    // 파일 정보 조회
    BoardFile boardFile = fileDAO.selectFileById(fileId);

    if (boardFile == null) {
      throw new BoardException("파일을 찾을 수 없습니다.");
    }

    // 물리적 파일 존재 여부 확인
    File physicalFile = new File(uploadPath, boardFile.getPhysicalName());

    if (!physicalFile.exists()) {
      throw new FileUploadException(
          "Physical file not found: " + boardFile.getPhysicalName(),
          "파일이 존재하지 않습니다."
      );
    }

    if (!physicalFile.isFile()) {
      throw new FileUploadException(
          "Not a file: " + boardFile.getPhysicalName(),
          "유효하지 않은 파일입니다."
      );
    }

    return boardFile;
  }
}
