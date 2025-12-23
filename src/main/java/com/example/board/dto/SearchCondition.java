package com.example.board.dto;

import java.time.LocalDate;

/**
 * 게시글 검색 조건 DTO.
 */
public class SearchCondition {

  private Long categoryId;
  private LocalDate from;
  private LocalDate to;
  private String keyword;
  private int page;
  private int limit;
  private int offset;

  public SearchCondition() {
    this.page = 1;
    this.limit = 10;
    this.offset = 0;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public LocalDate getFrom() {
    return from;
  }

  public void setFrom(LocalDate from) {
    this.from = from;
  }

  public LocalDate getTo() {
    return to;
  }

  public void setTo(LocalDate to) {
    this.to = to;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
    this.offset = (page - 1) * this.limit;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
    this.offset = (this.page - 1) * limit;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  @Override
  public String toString() {
    return "SearchCondition{"
        + "categoryId=" + categoryId
        + ", from=" + from
        + ", to=" + to
        + ", keyword='" + keyword + '\''
        + ", page=" + page
        + ", limit=" + limit
        + ", offset=" + offset
        + '}';
  }
}
