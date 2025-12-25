package com.board.dao;

import com.board.dto.Category;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 카테고리 DAO (Data Access Object)
 *
 * 카테고리 관련 데이터베이스 조회 작업을 수행
 * MyBatis SqlSession을 사용하여 데이터베이스에 접근
 */
public class CategoryDAO {

  private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);

  /**
   * 전체 카테고리 목록을 조회합니다.
   *
   * @return 카테고리 목록
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public List<Category> selectAllCategories() {
    logger.debug("전체 카테고리 목록 조회");

    try (SqlSession session = MyBatisUtil.openSession()) {
      List<Category> categories = session.selectList(
          "com.board.dao.CategoryDAO.selectAllCategories");

      logger.info("카테고리 목록 조회 완료: {} 건", categories.size());
      return categories;

    } catch (Exception e) {
      logger.error("카테고리 목록 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("카테고리 목록을 조회하는 중 오류가 발생했습니다.", e);
    }
  }
}
