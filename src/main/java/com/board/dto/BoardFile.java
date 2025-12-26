package com.board.dto;

import java.time.LocalDateTime;

/**
 * 첨부파일 정보를 담는 DTO 클래스
 *
 * DB 테이블: file
 * 참고: java.io.File과 구분하기 위해 BoardFile로 명명
 */
public class BoardFile {
  /** 파일 ID */
  private Long fileId;

  /** 게시글 ID */
  private Long boardId;

  /** 원본 파일명 */
  private String originalName;

  /** 서버 저장 파일명 (UUID) */
  private String physicalName;

  /** 파일 저장 경로 */
  private String filePath;

  /** 파일 확장자 (jpg, pdf, png) */
  private String extension;

  /** 파일 크기 (bytes) */
  private Long size;

  /** 등록일시 */
  private LocalDateTime createdAt;

  /** 수정일시 */
  private LocalDateTime editedAt;

  /**
   * 기본 생성자
   */
  public BoardFile() {
  }

  /**
   * 모든 필드를 포함하는 생성자
   *
   * @param fileId 파일 ID
   * @param boardId 게시글 ID
   * @param originalName 원본 파일명
   * @param physicalName 서버 저장 파일명
   * @param filePath 파일 저장 경로
   * @param extension 파일 확장자
   * @param size 파일 크기
   * @param createdAt 등록일시
   * @param editedAt 수정일시
   */
  public BoardFile(Long fileId, Long boardId, String originalName, String physicalName,
                   String filePath, String extension, Long size,
                   LocalDateTime createdAt, LocalDateTime editedAt) {
    this.fileId = fileId;
    this.boardId = boardId;
    this.originalName = originalName;
    this.physicalName = physicalName;
    this.filePath = filePath;
    this.extension = extension;
    this.size = size;
    this.createdAt = createdAt;
    this.editedAt = editedAt;
  }

  /**
   * 파일 ID를 반환합니다.
   *
   * @return 파일 ID
   */
  public Long getFileId() {
    return fileId;
  }

  /**
   * 파일 ID를 설정합니다.
   *
   * @param fileId 파일 ID
   */
  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  /**
   * 게시글 ID를 반환합니다.
   *
   * @return 게시글 ID
   */
  public Long getBoardId() {
    return boardId;
  }

  /**
   * 게시글 ID를 설정합니다.
   *
   * @param boardId 게시글 ID
   */
  public void setBoardId(Long boardId) {
    this.boardId = boardId;
  }

  /**
   * 원본 파일명을 반환합니다.
   *
   * @return 원본 파일명
   */
  public String getOriginalName() {
    return originalName;
  }

  /**
   * 원본 파일명을 설정합니다.
   *
   * @param originalName 원본 파일명
   */
  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  /**
   * 서버 저장 파일명을 반환합니다.
   *
   * @return 서버 저장 파일명 (UUID)
   */
  public String getPhysicalName() {
    return physicalName;
  }

  /**
   * 서버 저장 파일명을 설정합니다.
   *
   * @param physicalName 서버 저장 파일명 (UUID)
   */
  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  /**
   * 파일 저장 경로를 반환합니다.
   *
   * @return 파일 저장 경로
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * 파일 저장 경로를 설정합니다.
   *
   * @param filePath 파일 저장 경로
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * 파일 확장자를 반환합니다.
   *
   * @return 파일 확장자
   */
  public String getExtension() {
    return extension;
  }

  /**
   * 파일 확장자를 설정합니다.
   *
   * @param extension 파일 확장자
   */
  public void setExtension(String extension) {
    this.extension = extension;
  }

  /**
   * 파일 크기를 반환합니다.
   *
   * @return 파일 크기 (bytes)
   */
  public Long getSize() {
    return size;
  }

  /**
   * 파일 크기를 설정합니다.
   *
   * @param size 파일 크기 (bytes)
   */
  public void setSize(Long size) {
    this.size = size;
  }

  /**
   * 등록일시를 반환합니다.
   *
   * @return 등록일시
   */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * 등록일시를 설정합니다.
   *
   * @param createdAt 등록일시
   */
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * 수정일시를 반환합니다.
   *
   * @return 수정일시
   */
  public LocalDateTime getEditedAt() {
    return editedAt;
  }

  /**
   * 수정일시를 설정합니다.
   *
   * @param editedAt 수정일시
   */
  public void setEditedAt(LocalDateTime editedAt) {
    this.editedAt = editedAt;
  }

  @Override
  public String toString() {
    return "BoardFile{" +
        "fileId=" + fileId +
        ", boardId=" + boardId +
        ", originalName='" + originalName + '\'' +
        ", physicalName='" + physicalName + '\'' +
        ", filePath='" + filePath + '\'' +
        ", extension='" + extension + '\'' +
        ", size=" + size +
        ", createdAt=" + createdAt +
        ", editedAt=" + editedAt +
        '}';
  }
}
