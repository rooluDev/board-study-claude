package com.example.board.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 게시글 DTO.
 */
public class BoardDTO {

  private Long boardId;
  private Long categoryId;
  private String title;
  private String content;
  private String userName;
  private String password;
  private Long views;
  private LocalDateTime createdAt;
  private LocalDateTime editedAt;

  // 연관 데이터
  private String categoryName;
  private boolean hasAttachment;
  private List<FileDTO> files;
  private List<CommentDTO> comments;

  public BoardDTO() {
  }

  public Long getBoardId() {
    return boardId;
  }

  public void setBoardId(Long boardId) {
    this.boardId = boardId;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getViews() {
    return views;
  }

  public void setViews(Long views) {
    this.views = views;
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

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public boolean isHasAttachment() {
    return hasAttachment;
  }

  public void setHasAttachment(boolean hasAttachment) {
    this.hasAttachment = hasAttachment;
  }

  public List<FileDTO> getFiles() {
    return files;
  }

  public void setFiles(List<FileDTO> files) {
    this.files = files;
  }

  public List<CommentDTO> getComments() {
    return comments;
  }

  public void setComments(List<CommentDTO> comments) {
    this.comments = comments;
  }

  /**
   * 등록일을 포맷팅하여 반환 (yyyy-MM-dd HH:mm:ss).
   */
  public String getFormattedCreatedAt() {
    if (createdAt == null) {
      return "";
    }
    return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  /**
   * 등록일을 짧은 포맷으로 반환 (yyyy-MM-dd HH:mm).
   */
  public String getFormattedCreatedAtShort() {
    if (createdAt == null) {
      return "";
    }
    return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  /**
   * 수정일을 포맷팅하여 반환 (yyyy-MM-dd HH:mm:ss).
   */
  public String getFormattedEditedAt() {
    if (editedAt == null) {
      return "";
    }
    return editedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  /**
   * 수정일을 짧은 포맷으로 반환 (yyyy-MM-dd HH:mm).
   */
  public String getFormattedEditedAtShort() {
    if (editedAt == null) {
      return "";
    }
    return editedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  @Override
  public String toString() {
    return "BoardDTO{"
        + "boardId=" + boardId
        + ", categoryId=" + categoryId
        + ", title='" + title + '\''
        + ", userName='" + userName + '\''
        + ", views=" + views
        + ", createdAt=" + createdAt
        + '}';
  }
}
