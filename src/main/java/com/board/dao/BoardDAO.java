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
}
