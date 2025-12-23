package com.example.board.dao;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.SearchCondition;
import com.example.board.util.MyBatisUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 DAO.
 */
public class BoardDAO {

  private static final Logger logger = LoggerFactory.getLogger(BoardDAO.class);
  private static final String NAMESPACE = "com.example.board.dao.BoardDAO";

  /**
   * 게시글 ID로 조회.
   *
   * @param boardId 게시글 ID
   * @return 게시글 DTO
   */
  public BoardDTO selectById(Long boardId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectOne(NAMESPACE + ".selectById", boardId);
    }
  }

  /**
   * 검색 조건에 따른 게시글 목록 조회.
   *
   * @param condition 검색 조건
   * @return 게시글 목록
   */
  public List<BoardDTO> selectList(SearchCondition condition) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectList(NAMESPACE + ".selectList", condition);
    }
  }

  /**
   * 검색 조건에 따른 게시글 총 개수 조회.
   *
   * @param condition 검색 조건
   * @return 게시글 개수
   */
  public int count(SearchCondition condition) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectOne(NAMESPACE + ".count", condition);
    }
  }

  /**
   * 게시글 등록.
   *
   * @param board 게시글 DTO
   * @return 등록된 행 수
   */
  public int insert(BoardDTO board) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.insert(NAMESPACE + ".insert", board);
      session.commit();
      return result;
    }
  }

  /**
   * 게시글 수정.
   *
   * @param board 게시글 DTO
   * @return 수정된 행 수
   */
  public int update(BoardDTO board) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.update(NAMESPACE + ".update", board);
      session.commit();
      return result;
    }
  }

  /**
   * 조회수 증가.
   *
   * @param boardId 게시글 ID
   * @return 수정된 행 수
   */
  public int incrementViews(Long boardId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.update(NAMESPACE + ".incrementViews", boardId);
      session.commit();
      return result;
    }
  }

  /**
   * 게시글 삭제.
   *
   * @param boardId 게시글 ID
   * @return 삭제된 행 수
   */
  public int delete(Long boardId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.delete(NAMESPACE + ".delete", boardId);
      session.commit();
      return result;
    }
  }

  /**
   * 비밀번호 확인.
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호
   * @return 일치 여부
   */
  public boolean verifyPassword(Long boardId, String password) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      Map<String, Object> params = new HashMap<>();
      params.put("boardId", boardId);
      params.put("password", password);
      Integer count = session.selectOne(NAMESPACE + ".verifyPassword", params);
      return count != null && count > 0;
    }
  }
}
