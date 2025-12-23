package com.example.board.service;

import com.example.board.dao.CategoryDAO;
import com.example.board.dto.CategoryDTO;
import java.util.List;

/**
 * 카테고리 Service.
 */
public class CategoryService {

  private final CategoryDAO categoryDAO;

  public CategoryService() {
    this.categoryDAO = new CategoryDAO();
  }

  /**
   * 모든 카테고리 조회.
   *
   * @return 카테고리 목록
   */
  public List<CategoryDTO> getAllCategories() {
    return categoryDAO.selectAll();
  }

  /**
   * 카테고리 조회.
   *
   * @param categoryId 카테고리 ID
   * @return 카테고리 DTO
   */
  public CategoryDTO getCategory(Long categoryId) {
    return categoryDAO.selectById(categoryId);
  }
}