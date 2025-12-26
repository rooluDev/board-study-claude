package com.board.dao;

import com.board.dto.Board;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Board 테이블에 대한 데이터 접근 객체
 *
 * MyBatis를 사용하여 게시글 관련 CRUD 작업을 수행합니다.
 */
public class BoardDAO {
  /**
   * 게시글 목록을 조회합니다. (페이징, 검색)
   *
   * @param limit 페이지당 게시글 수
   * @param offset 시작 위치
   * @param category 카테고리 ID (선택)
   * @param from 등록일시 시작 (선택)
   * @param to 등록일시 종료 (선택)
   * @param keyword 검색어 (선택, 제목/내용/작성자)
   * @return 게시글 목록
   * @throws BoardException 조회 실패 시
   */
  public List<Board> selectBoardList(int limit, int offset, Integer category,
                                      String from, String to, String keyword) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      Map<String, Object> params = new HashMap<>();
      params.put("limit", limit);
      params.put("offset", offset);
      params.put("category", category);
      params.put("from", from);
      params.put("to", to);
      params.put("keyword", keyword);

      return session.selectList("com.board.dao.BoardDAO.selectBoardList", params);
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select board list: limit=" + limit + ", offset=" + offset,
          "게시글 목록을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 전체 게시글 수를 조회합니다. (검색)
   *
   * @param category 카테고리 ID (선택)
   * @param from 등록일시 시작 (선택)
   * @param to 등록일시 종료 (선택)
   * @param keyword 검색어 (선택, 제목/내용/작성자)
   * @return 전체 게시글 수
   * @throws BoardException 조회 실패 시
   */
  public int countBoards(Integer category, String from, String to, String keyword) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      Map<String, Object> params = new HashMap<>();
      params.put("category", category);
      params.put("from", from);
      params.put("to", to);
      params.put("keyword", keyword);

      Integer count = session.selectOne("com.board.dao.BoardDAO.countBoards", params);
      return count != null ? count : 0;
    } catch (Exception e) {
      throw new BoardException(
          "Failed to count boards",
          "전체 게시글 수를 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 게시글 ID로 게시글을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 게시글 정보 (존재하지 않으면 null)
   * @throws BoardException 조회 실패 시
   */
  public Board selectBoardById(Long boardId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectOne("com.board.dao.BoardDAO.selectBoardById", boardId);
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select board by id: " + boardId,
          "게시글을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 조회수를 1 증가시킵니다.
   *
   * @param boardId 게시글 ID
   * @throws BoardException 업데이트 실패 시
   */
  public void updateViewCount(Long boardId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.update(
          "com.board.dao.BoardDAO.updateViewCount",
          boardId
      );

      if (affectedRows == 0) {
        throw new BoardException("게시글을 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to update view count: " + boardId,
          "조회수를 증가시키는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글을 등록합니다.
   *
   * @param board 등록할 게시글 정보
   * @return 생성된 게시글 ID
   * @throws BoardException 등록 실패 시
   */
  public Long insertBoard(Board board) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.insert("com.board.dao.BoardDAO.insertBoard", board);
      session.commit();
      return board.getBoardId();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to insert board: " + board.getTitle(),
          "게시글을 등록하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글을 수정합니다.
   *
   * @param board 수정할 게시글 정보 (boardId, title, content 필수)
   * @throws BoardException 수정 실패 시
   */
  public void updateBoard(Board board) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.update(
          "com.board.dao.BoardDAO.updateBoard",
          board
      );

      if (affectedRows == 0) {
        throw new BoardException("게시글을 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to update board: " + board.getBoardId(),
          "게시글을 수정하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글을 삭제합니다.
   *
   * @param boardId 삭제할 게시글 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteBoard(Long boardId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.delete(
          "com.board.dao.BoardDAO.deleteBoard",
          boardId
      );

      if (affectedRows == 0) {
        throw new BoardException("게시글을 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete board: " + boardId,
          "게시글을 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 비밀번호를 확인합니다.
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호 (평문)
   * @return 일치하면 true, 불일치하면 false
   * @throws BoardException 확인 실패 시
   */
  public boolean verifyPassword(Long boardId, String password) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      Map<String, Object> params = new HashMap<>();
      params.put("boardId", boardId);
      params.put("password", password);

      Integer count = session.selectOne(
          "com.board.dao.BoardDAO.verifyPassword",
          params
      );

      return count != null && count > 0;
    } catch (Exception e) {
      throw new BoardException(
          "Failed to verify password: boardId=" + boardId,
          "비밀번호 확인에 실패했습니다.",
          e
      );
    }
  }
}
