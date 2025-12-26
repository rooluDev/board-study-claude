package com.board.service;

import com.board.dao.CategoryDAO;
import com.board.dto.Category;
import com.board.exception.BoardException;

import java.util.List;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * DAO를 통해 데이터를 조회하고, 비즈니스 규칙을 적용합니다.
 */
public class CategoryService {
  /** CategoryDAO 인스턴스 */
  private final CategoryDAO categoryDAO;

  /**
   * 기본 생성자
   * CategoryDAO를 초기화합니다.
   */
  public CategoryService() {
    this.categoryDAO = new CategoryDAO();
  }

  /**
   * 의존성 주입을 위한 생성자
   *
   * @param categoryDAO CategoryDAO 인스턴스
   */
  public CategoryService(CategoryDAO categoryDAO) {
    this.categoryDAO = categoryDAO;
  }

  /**
   * 전체 카테고리 목록을 조회합니다.
   *
   * @return 카테고리 목록
   * @throws BoardException 조회 실패 시
   */
  public List<Category> getAllCategories() {
    return categoryDAO.selectAllCategories();
  }

  /**
   * 카테고리 ID로 카테고리를 조회합니다.
   *
   * @param categoryId 카테고리 ID
   * @return 카테고리 정보
   * @throws BoardException 카테고리가 존재하지 않거나 조회 실패 시
   */
  public Category getCategoryById(Long categoryId) {
    if (categoryId == null || categoryId <= 0) {
      throw new BoardException("유효하지 않은 카테고리입니다.");
    }

    Category category = categoryDAO.selectCategoryById(categoryId);

    if (category == null) {
      throw new BoardException("카테고리를 찾을 수 없습니다.");
    }

    return category;
  }

  /**
   * 카테고리를 등록합니다.
   *
   * @param categoryName 카테고리 이름
   * @return 생성된 카테고리 ID
   * @throws BoardException 등록 실패 시
   */
  public Long createCategory(String categoryName) {
    if (categoryName == null || categoryName.trim().isEmpty()) {
      throw new BoardException("카테고리 이름을 입력해주세요.");
    }

    Category category = new Category();
    category.setCategoryName(categoryName.trim());

    return categoryDAO.insertCategory(category);
  }

  /**
   * 카테고리를 수정합니다.
   *
   * @param categoryId 카테고리 ID
   * @param categoryName 수정할 카테고리 이름
   * @throws BoardException 수정 실패 시
   */
  public void updateCategory(Long categoryId, String categoryName) {
    if (categoryId == null || categoryId <= 0) {
      throw new BoardException("유효하지 않은 카테고리입니다.");
    }
    if (categoryName == null || categoryName.trim().isEmpty()) {
      throw new BoardException("카테고리 이름을 입력해주세요.");
    }

    Category category = new Category();
    category.setCategoryId(categoryId);
    category.setCategoryName(categoryName.trim());

    categoryDAO.updateCategory(category);
  }

  /**
   * 카테고리를 삭제합니다.
   *
   * @param categoryId 삭제할 카테고리 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteCategory(Long categoryId) {
    if (categoryId == null || categoryId <= 0) {
      throw new BoardException("유효하지 않은 카테고리입니다.");
    }

    categoryDAO.deleteCategory(categoryId);
  }
}
