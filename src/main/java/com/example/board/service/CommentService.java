package com.example.board.service;

import com.example.board.dao.CommentDAO;
import com.example.board.dto.CommentDTO;
import com.example.board.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 댓글 Service.
 */
public class CommentService {

  private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
  private final CommentDAO commentDAO;

  public CommentService() {
    this.commentDAO = new CommentDAO();
  }

  /**
   * 댓글 등록.
   *
   * @param comment 댓글 DTO
   * @throws Exception 등록 실패 시
   */
  public void createComment(CommentDTO comment) throws Exception {
    logger.debug("댓글 등록 시작: boardId={}", comment.getBoardId());

    // 입력값 검증
    ValidationUtil.validateComment(comment.getComment());

    // 댓글 등록
    int result = commentDAO.insert(comment);
    if (result == 0) {
      throw new RuntimeException("댓글 등록 실패");
    }

    logger.info("댓글 등록 성공: commentId={}, boardId={}",
        comment.getCommentId(), comment.getBoardId());
  }
}
