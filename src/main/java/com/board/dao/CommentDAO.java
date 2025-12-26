package com.board.dao;

import com.board.dto.Comment;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Comment 테이블에 대한 데이터 접근 객체
 *
 * MyBatis를 사용하여 댓글 관련 CRUD 작업을 수행합니다.
 */
public class CommentDAO {
  /**
   * 게시글 ID로 댓글 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 댓글 목록 (등록일시 오름차순)
   * @throws BoardException 조회 실패 시
   */
  public List<Comment> selectCommentsByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectList(
          "com.board.dao.CommentDAO.selectCommentsByBoardId",
          boardId
      );
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select comments by board id: " + boardId,
          "댓글 목록을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 댓글 ID로 댓글을 조회합니다.
   *
   * @param commentId 댓글 ID
   * @return 댓글 정보 (존재하지 않으면 null)
   * @throws BoardException 조회 실패 시
   */
  public Comment selectCommentById(Long commentId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectOne(
          "com.board.dao.CommentDAO.selectCommentById",
          commentId
      );
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select comment by id: " + commentId,
          "댓글을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 댓글을 등록합니다.
   *
   * @param comment 등록할 댓글 정보
   * @return 생성된 댓글 ID
   * @throws BoardException 등록 실패 시
   */
  public Long insertComment(Comment comment) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.insert("com.board.dao.CommentDAO.insertComment", comment);
      session.commit();
      return comment.getCommentId();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to insert comment",
          "댓글을 등록하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 댓글을 수정합니다.
   *
   * @param comment 수정할 댓글 정보 (commentId, comment 필수)
   * @throws BoardException 수정 실패 시
   */
  public void updateComment(Comment comment) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.update(
          "com.board.dao.CommentDAO.updateComment",
          comment
      );

      if (affectedRows == 0) {
        throw new BoardException("댓글을 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to update comment: " + comment.getCommentId(),
          "댓글을 수정하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 댓글을 삭제합니다.
   *
   * @param commentId 삭제할 댓글 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteComment(Long commentId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.delete(
          "com.board.dao.CommentDAO.deleteComment",
          commentId
      );

      if (affectedRows == 0) {
        throw new BoardException("댓글을 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete comment: " + commentId,
          "댓글을 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글의 모든 댓글을 삭제합니다.
   *
   * @param boardId 게시글 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteCommentsByBoardId(Long boardId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.delete(
          "com.board.dao.CommentDAO.deleteCommentsByBoardId",
          boardId
      );
      session.commit();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete comments by board id: " + boardId,
          "댓글을 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글의 댓글 개수를 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 댓글 개수
   * @throws BoardException 조회 실패 시
   */
  public int countCommentsByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      Integer count = session.selectOne(
          "com.board.dao.CommentDAO.countCommentsByBoardId",
          boardId
      );
      return count != null ? count : 0;
    } catch (Exception e) {
      throw new BoardException(
          "Failed to count comments by board id: " + boardId,
          "댓글 개수를 조회하는데 실패했습니다.",
          e
      );
    }
  }
}
