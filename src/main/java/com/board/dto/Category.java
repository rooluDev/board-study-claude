package com.board.dto;

/**
 * 카테고리 DTO (Data Transfer Object)
 *
 * category 테이블과 매핑되는 데이터 전송 객체
 */
public class Category {

  /**
   * 카테고리 ID (Primary Key)
   */
  private Long categoryId;

  /**
   * 카테고리 이름
   */
  private String categoryName;

  /**
   * 기본 생성자
   */
  public Category() {
  }

  /**
   * 모든 필드를 포함하는 생성자
   *
   * @param categoryId 카테고리 ID
   * @param categoryName 카테고리 이름
   */
  public Category(Long categoryId, String categoryName) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }

  /**
   * 카테고리 ID를 반환합니다.
   *
   * @return 카테고리 ID
   */
  public Long getCategoryId() {
    return categoryId;
  }

  /**
   * 카테고리 ID를 설정합니다.
   *
   * @param categoryId 카테고리 ID
   */
  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * 카테고리 이름을 반환합니다.
   *
   * @return 카테고리 이름
   */
  public String getCategoryName() {
    return categoryName;
  }

  /**
   * 카테고리 이름을 설정합니다.
   *
   * @param categoryName 카테고리 이름
   */
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public String toString() {
    return "Category{" +
        "categoryId=" + categoryId +
        ", categoryName='" + categoryName + '\'' +
        '}';
  }
}
