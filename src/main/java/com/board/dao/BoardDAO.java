package com.board.dao;

import com.board.dto.Board;
import com.board.exception.BoardException;
import com.board.util.Constants;
import com.board.util.MyBatisUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 DAO (Data Access Object)
 *
 * 게시글 관련 데이터베이스 CRUD 작업을 수행
 * MyBatis SqlSession을 사용하여 데이터베이스에 접근
 */
public class BoardDAO {

  private static final Logger logger = LoggerFactory.getLogger(BoardDAO.class);

  /**
   * 페이징된 게시글 목록을 조회합니다.
   *
   * @param page 페이지 번호 (1부터 시작)
   * @return 게시글 목록
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public List<Board> selectBoardList(int page) {
    // OFFSET 계산: (페이지 번호 - 1) * 페이지 크기
    int offset = (page - 1) * Constants.PAGE_SIZE;

    logger.debug("게시글 목록 조회: page={}, offset={}, pageSize={}",
        page, offset, Constants.PAGE_SIZE);

    try (SqlSession session = MyBatisUtil.openSession()) {
      Map<String, Object> params = new HashMap<>();
      params.put("offset", offset);
      params.put("pageSize", Constants.PAGE_SIZE);

      List<Board> boards = session.selectList("com.board.dao.BoardDAO.selectBoardList", params);

      logger.info("게시글 목록 조회 완료: {} 건", boards.size());
      return boards;

    } catch (Exception e) {
      logger.error("게시글 목록 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("게시글 목록을 조회하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 전체 게시글 수를 조회합니다.
   *
   * @return 전체 게시글 수
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public int countBoards() {
    logger.debug("전체 게시글 수 조회");

    try (SqlSession session = MyBatisUtil.openSession()) {
      Integer count = session.selectOne("com.board.dao.BoardDAO.countBoards");

      if (count == null) {
        count = 0;
      }

      logger.info("전체 게시글 수: {} 건", count);
      return count;

    } catch (Exception e) {
      logger.error("전체 게시글 수 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("전체 게시글 수를 조회하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 게시글 ID로 게시글 상세 정보를 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 게시글 상세 정보 (없으면 null)
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public Board selectBoardById(Long boardId) {
    logger.debug("게시글 상세 조회: boardId={}", boardId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      Board board = session.selectOne("com.board.dao.BoardDAO.selectBoardById", boardId);

      if (board != null) {
        logger.info("게시글 상세 조회 완료: boardId={}, title={}", boardId, board.getTitle());
      } else {
        logger.warn("게시글을 찾을 수 없음: boardId={}", boardId);
      }

      return board;

    } catch (Exception e) {
      logger.error("게시글 상세 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("게시글을 조회하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 게시글 조회수를 1 증가시킵니다.
   *
   * @param boardId 게시글 ID
   * @throws BoardException 데이터베이스 업데이트 중 오류 발생 시
   */
  public void updateViewCount(Long boardId) {
    logger.debug("조회수 증가: boardId={}", boardId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.update("com.board.dao.BoardDAO.updateViewCount", boardId);

      if (affectedRows > 0) {
        session.commit();
        logger.info("조회수 증가 완료: boardId={}", boardId);
      } else {
        logger.warn("조회수 증가 실패 (게시글 없음): boardId={}", boardId);
      }

    } catch (Exception e) {
      logger.error("조회수 증가 실패: {}", e.getMessage(), e);
      throw new BoardException("조회수를 증가시키는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 새로운 게시글을 등록합니다.
   * 비밀번호는 SHA2(256)로 해싱되어 저장됩니다.
   * 등록 후 생성된 boardId가 Board 객체에 설정됩니다.
   *
   * @param board 등록할 게시글 정보
   * @throws BoardException 데이터베이스 삽입 중 오류 발생 시
   */
  public void insertBoard(Board board) {
    logger.debug("게시글 등록: title={}, userName={}, categoryId={}",
        board.getTitle(), board.getUserName(), board.getCategoryId());

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.insert("com.board.dao.BoardDAO.insertBoard", board);

      if (affectedRows > 0) {
        session.commit();
        logger.info("게시글 등록 완료: boardId={}, title={}", board.getBoardId(), board.getTitle());
      } else {
        logger.error("게시글 등록 실패: affectedRows=0");
        throw new BoardException("게시글 등록에 실패했습니다.");
      }

    } catch (BoardException e) {
      throw e;
    } catch (Exception e) {
      logger.error("게시글 등록 실패: {}", e.getMessage(), e);
      throw new BoardException("게시글을 등록하는 중 오류가 발생했습니다.", e);
    }
  }
}
