package com.example.board.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 댓글 DTO.
 */
public class CommentDTO {

  private Long commentId;
  private Long boardId;
  private String comment;
  private LocalDateTime createdAt;
  private LocalDateTime editedAt;

  public CommentDTO() {
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public Long getBoardId() {
    return boardId;
  }

  public void setBoardId(Long boardId) {
    this.boardId = boardId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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

  /**
   * 등록일을 포맷팅하여 반환 (yyyy-MM-dd HH:mm).
   */
  public String getFormattedCreatedAt() {
    if (createdAt == null) {
      return "";
    }
    return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  /**
   * 수정일을 포맷팅하여 반환 (yyyy-MM-dd HH:mm).
   */
  public String getFormattedEditedAt() {
    if (editedAt == null) {
      return "";
    }
    return editedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  @Override
  public String toString() {
    return "CommentDTO{"
        + "commentId=" + commentId
        + ", boardId=" + boardId
        + ", comment='" + comment + '\''
        + ", createdAt=" + createdAt
        + '}';
  }
}
