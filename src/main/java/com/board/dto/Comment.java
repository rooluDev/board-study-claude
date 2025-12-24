package com.board.dto;

import java.sql.Timestamp;

/**
 * 댓글 DTO (Data Transfer Object)
 *
 * comment 테이블과 매핑되는 데이터 전송 객체
 */
public class Comment {

  /**
   * 댓글 ID (Primary Key)
   */
  private Long commentId;

  /**
   * 게시글 ID (Foreign Key)
   */
  private Long boardId;

  /**
   * 댓글 내용 (1~300자)
   */
  private String comment;

  /**
   * 등록일시
   */
  private Timestamp createdAt;

  /**
   * 수정일시
   */
  private Timestamp editedAt;

  /**
   * 기본 생성자
   */
  public Comment() {
  }

  /**
   * 모든 필드를 포함하는 생성자
   *
   * @param commentId 댓글 ID
   * @param boardId 게시글 ID
   * @param comment 댓글 내용
   * @param createdAt 등록일시
   * @param editedAt 수정일시
   */
  public Comment(Long commentId, Long boardId, String comment,
                 Timestamp createdAt, Timestamp editedAt) {
    this.commentId = commentId;
    this.boardId = boardId;
    this.comment = comment;
    this.createdAt = createdAt;
    this.editedAt = editedAt;
  }

  /**
   * 댓글 ID를 반환합니다.
   *
   * @return 댓글 ID
   */
  public Long getCommentId() {
    return commentId;
  }

  /**
   * 댓글 ID를 설정합니다.
   *
   * @param commentId 댓글 ID
   */
  public void setCommentId(Long commentId) {
    this.commentId = commentId;
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
   * 댓글 내용을 반환합니다.
   *
   * @return 댓글 내용
   */
  public String getComment() {
    return comment;
  }

  /**
   * 댓글 내용을 설정합니다.
   *
   * @param comment 댓글 내용
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * 등록일시를 반환합니다.
   *
   * @return 등록일시
   */
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  /**
   * 등록일시를 설정합니다.
   *
   * @param createdAt 등록일시
   */
  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * 수정일시를 반환합니다.
   *
   * @return 수정일시
   */
  public Timestamp getEditedAt() {
    return editedAt;
  }

  /**
   * 수정일시를 설정합니다.
   *
   * @param editedAt 수정일시
   */
  public void setEditedAt(Timestamp editedAt) {
    this.editedAt = editedAt;
  }

  @Override
  public String toString() {
    return "Comment{" +
        "commentId=" + commentId +
        ", boardId=" + boardId +
        ", comment='" + comment + '\'' +
        ", createdAt=" + createdAt +
        ", editedAt=" + editedAt +
        '}';
  }
}
