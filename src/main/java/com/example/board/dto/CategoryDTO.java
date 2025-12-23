package com.example.board.dto;

/**
 * 카테고리 DTO.
 */
public class CategoryDTO {

  private Long categoryId;
  private String categoryName;

  public CategoryDTO() {
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public String toString() {
    return "CategoryDTO{"
        + "categoryId=" + categoryId
        + ", categoryName='" + categoryName + '\''
        + '}';
  }
}
