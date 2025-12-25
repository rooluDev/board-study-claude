package com.board.service;

import com.board.dao.CategoryDAO;
import com.board.dto.Category;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 카테고리 Service (비즈니스 로직 계층)
 *
 * 카테고리 관련 비즈니스 로직을 처리
 * DAO를 호출하여 데이터베이스 작업 수행
 */
public class CategoryService {

  private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

  /**
   * CategoryDAO 인스턴스
   */
  private final CategoryDAO categoryDAO;

  /**
   * 기본 생성자
   * CategoryDAO 인스턴스를 생성합니다.
   */
  public CategoryService() {
    this.categoryDAO = new CategoryDAO();
  }

  /**
   * 전체 카테고리 목록을 조회합니다.
   *
   * @return 카테고리 목록
   */
  public List<Category> getAllCategories() {
    logger.info("전체 카테고리 목록 조회 요청");

    // DAO를 통해 카테고리 목록 조회
    List<Category> categories = categoryDAO.selectAllCategories();

    logger.info("카테고리 목록 조회 완료: {} 건", categories.size());
    return categories;
  }
}
