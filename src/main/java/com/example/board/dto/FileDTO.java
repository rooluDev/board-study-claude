package com.example.board.dto;

import java.time.LocalDateTime;

/**
 * 첨부파일 DTO.
 */
public class FileDTO {

  private Long fileId;
  private Long boardId;
  private String originalName;
  private String physicalName;
  private String filePath;
  private String extension;
  private Long size;
  private LocalDateTime createdAt;
  private LocalDateTime editedAt;

  public FileDTO() {
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public Long getBoardId() {
    return boardId;
  }

  public void setBoardId(Long boardId) {
    this.boardId = boardId;
  }

  public String getOriginalName() {
    return originalName;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  public String getPhysicalName() {
    return physicalName;
  }

  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getEditedAt() {
    return editedAt;
  }

  public void setEditedAt(LocalDateTime editedAt) {
    this.editedAt = editedAt;
  }

  @Override
  public String toString() {
    return "FileDTO{"
        + "fileId=" + fileId
        + ", boardId=" + boardId
        + ", originalName='" + originalName + '\''
        + ", physicalName='" + physicalName + '\''
        + ", size=" + size
        + '}';
  }
}
