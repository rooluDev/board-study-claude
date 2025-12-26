package com.board.service;

import com.board.dao.CommentDAO;
import com.board.dto.Comment;
import com.board.exception.BoardException;
import com.board.exception.ValidationException;
import com.board.util.ValidationUtil;

import java.util.List;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * DAO를 통해 데이터를 조회하고, 비즈니스 규칙을 적용합니다.
 */
public class CommentService {
  /** CommentDAO 인스턴스 */
  private final CommentDAO commentDAO;

  /**
   * 기본 생성자
   * CommentDAO를 초기화합니다.
   */
  public CommentService() {
    this.commentDAO = new CommentDAO();
  }

  /**
   * 의존성 주입을 위한 생성자
   *
   * @param commentDAO CommentDAO 인스턴스
   */
  public CommentService(CommentDAO commentDAO) {
    this.commentDAO = commentDAO;
  }

  /**
   * 게시글 ID로 댓글 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 댓글 목록 (등록일시 오름차순)
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 조회 실패 시
   */
  public List<Comment> getCommentsByBoardId(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 댓글 목록 조회
    return commentDAO.selectCommentsByBoardId(boardId);
  }

  /**
   * 댓글 ID로 댓글을 조회합니다.
   *
   * @param commentId 댓글 ID
   * @return 댓글 정보
   * @throws BoardException 댓글이 존재하지 않거나 조회 실패 시
   */
  public Comment getCommentById(Long commentId) {
    if (commentId == null || commentId <= 0) {
      throw new BoardException("유효하지 않은 댓글입니다.");
    }

    Comment comment = commentDAO.selectCommentById(commentId);

    if (comment == null) {
      throw new BoardException("댓글을 찾을 수 없습니다.");
    }

    return comment;
  }

  /**
   * 댓글을 등록합니다.
   *
   * @param boardId 게시글 ID
   * @param content 댓글 내용
   * @return 생성된 댓글 ID
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 등록 실패 시
   */
  public Long createComment(Long boardId, String content) {
    // 입력값 검증
    ValidationUtil.validateBoardId(boardId);
    ValidationUtil.validateCommentContent(content);

    // 댓글 객체 생성
    Comment comment = new Comment();
    comment.setBoardId(boardId);
    comment.setComment(content);

    // 댓글 등록
    return commentDAO.insertComment(comment);
  }

  /**
   * 댓글을 수정합니다.
   *
   * @param commentId 댓글 ID
   * @param content 수정할 댓글 내용
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 수정 실패 시
   */
  public void updateComment(Long commentId, String content) {
    // 입력값 검증
    if (commentId == null || commentId <= 0) {
      throw new BoardException("유효하지 않은 댓글입니다.");
    }
    ValidationUtil.validateCommentContent(content);

    // 댓글 객체 생성
    Comment comment = new Comment();
    comment.setCommentId(commentId);
    comment.setComment(content);

    // 댓글 수정
    commentDAO.updateComment(comment);
  }

  /**
   * 댓글을 삭제합니다.
   *
   * @param commentId 삭제할 댓글 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteComment(Long commentId) {
    if (commentId == null || commentId <= 0) {
      throw new BoardException("유효하지 않은 댓글입니다.");
    }

    commentDAO.deleteComment(commentId);
  }

  /**
   * 게시글의 모든 댓글을 삭제합니다.
   *
   * @param boardId 게시글 ID
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 삭제 실패 시
   */
  public void deleteCommentsByBoardId(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 모든 댓글 삭제
    commentDAO.deleteCommentsByBoardId(boardId);
  }

  /**
   * 게시글의 댓글 개수를 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 댓글 개수
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 조회 실패 시
   */
  public int getCommentCountByBoardId(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 댓글 개수 조회
    return commentDAO.countCommentsByBoardId(boardId);
  }
}
