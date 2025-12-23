package com.example.board.dto;

import java.time.LocalDateTime;
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
