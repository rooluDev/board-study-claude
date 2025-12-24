package com.board.service;

import com.board.dao.CommentDAO;
import com.board.dto.Comment;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 댓글 Service (비즈니스 로직 계층)
 *
 * 댓글 관련 비즈니스 로직을 처리
 * DAO를 호출하여 데이터베이스 작업 수행
 */
public class CommentService {

  private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

  /**
   * CommentDAO 인스턴스
   */
  private final CommentDAO commentDAO;

  /**
   * 기본 생성자
   * CommentDAO 인스턴스를 생성합니다.
   */
  public CommentService() {
    this.commentDAO = new CommentDAO();
  }

  /**
   * 특정 게시글의 댓글 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 댓글 목록
   */
  public List<Comment> getCommentsByBoardId(Long boardId) {
    logger.info("댓글 목록 조회 요청: boardId={}", boardId);

    // DAO를 통해 댓글 목록 조회
    List<Comment> comments = commentDAO.selectCommentsByBoardId(boardId);

    logger.info("댓글 목록 조회 완료: {} 건", comments.size());
    return comments;
  }
}
