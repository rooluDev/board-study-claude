package com.example.board.dto;

import java.time.LocalDateTime;

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
