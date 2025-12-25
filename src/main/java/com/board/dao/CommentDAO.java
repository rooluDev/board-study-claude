package com.board.dao;

import com.board.dto.Comment;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 댓글 DAO (Data Access Object)
 *
 * 댓글 관련 데이터베이스 CRUD 작업을 수행
 * MyBatis SqlSession을 사용하여 데이터베이스에 접근
 */
public class CommentDAO {

  private static final Logger logger = LoggerFactory.getLogger(CommentDAO.class);

  /**
   * 특정 게시글의 댓글 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 댓글 목록
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public List<Comment> selectCommentsByBoardId(Long boardId) {
    logger.debug("댓글 목록 조회: boardId={}", boardId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      List<Comment> comments = session.selectList(
          "com.board.dao.CommentDAO.selectCommentsByBoardId", boardId);

      logger.info("댓글 목록 조회 완료: {} 건", comments.size());
      return comments;

    } catch (Exception e) {
      logger.error("댓글 목록 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("댓글 목록을 조회하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 특정 게시글의 모든 댓글을 삭제합니다.
   * 게시글 삭제 시 사용 (CASCADE 대신 명시적 삭제)
   *
   * @param boardId 게시글 ID
   * @throws BoardException 데이터베이스 삭제 중 오류 발생 시
   */
  public void deleteCommentsByBoardId(Long boardId) {
    logger.debug("댓글 삭제: boardId={}", boardId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.delete("com.board.dao.CommentDAO.deleteCommentsByBoardId", boardId);

      session.commit();
      logger.info("댓글 삭제 완료: boardId={}, 삭제된 댓글 {} 건", boardId, affectedRows);

    } catch (Exception e) {
      logger.error("댓글 삭제 실패: {}", e.getMessage(), e);
      throw new BoardException("댓글을 삭제하는 중 오류가 발생했습니다.", e);
    }
  }
}
