package com.board.service;

import com.board.dao.CommentDAO;
import com.board.dto.Comment;
import com.board.exception.BoardException;
import com.board.exception.ValidationException;
import com.board.util.ValidationUtil;
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

  /**
   * 댓글을 등록합니다.
   * - 입력값 검증
   * - 댓글 정보 DB 삽입
   *
   * @param comment 등록할 댓글 정보 (boardId, comment)
   * @return 등록된 댓글 (생성된 commentId 포함)
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 댓글 등록 실패 시
   */
  public Comment createComment(Comment comment) {
    logger.info("댓글 등록 요청: boardId={}", comment.getBoardId());

    // 입력값 검증
    validateCommentInput(comment);

    try {
      // 댓글 DB 삽입
      commentDAO.insertComment(comment);

      logger.info("댓글 등록 완료: commentId={}, boardId={}",
          comment.getCommentId(), comment.getBoardId());

      return comment;

    } catch (ValidationException e) {
      throw e;
    } catch (BoardException e) {
      throw e;
    } catch (Exception e) {
      logger.error("댓글 등록 실패: {}", e.getMessage(), e);
      throw new BoardException("댓글 등록 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 댓글 입력값의 유효성을 검증합니다.
   *
   * @param comment 검증할 댓글 정보
   * @throws ValidationException 검증 실패 시
   */
  private void validateCommentInput(Comment comment) {
    // boardId 검증
    ValidationUtil.validateBoardId(comment.getBoardId());

    // 댓글 내용 검증 (1~300자)
    ValidationUtil.validateCommentContent(comment.getComment());

    logger.debug("댓글 입력값 검증 완료");
  }
}
