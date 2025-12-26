package com.board.dto;

import java.time.LocalDateTime;

/**
 * 게시글 정보를 담는 DTO 클래스
 *
 * DB 테이블: board
 */
public class Board {
  /** 게시글 ID */
  private Long boardId;

  /** 카테고리 ID */
  private Long categoryId;

  /** 제목 (4~1000자) */
  private String title;

  /** 내용 (4~4000자) */
  private String content;

  /** 작성자 (2~10자) */
  private String userName;

  /** 비밀번호 (SHA-256 해시, 8~12자 영문+숫자 조합) */
  private String password;

  /** 조회수 */
  private Long views;

  /** 등록일시 */
  private LocalDateTime createdAt;

  /** 수정일시 */
  private LocalDateTime editedAt;

  /** 카테고리 이름 (JOIN 결과용) */
  private String categoryName;

  /** 첨부파일 개수 (목록 조회 시 사용) */
  private Integer fileCount;

  /**
   * 기본 생성자
   */
  public Board() {
  }

  /**
   * 필수 필드를 포함하는 생성자
   *
   * @param categoryId 카테고리 ID
   * @param title 제목
   * @param content 내용
   * @param userName 작성자
   * @param password 비밀번호
   */
  public Board(Long categoryId, String title, String content,
               String userName, String password) {
    this.categoryId = categoryId;
    this.title = title;
    this.content = content;
    this.userName = userName;
    this.password = password;
    this.views = 0L;
  }

  /**
   * 모든 필드를 포함하는 생성자
   *
   * @param boardId 게시글 ID
   * @param categoryId 카테고리 ID
   * @param title 제목
   * @param content 내용
   * @param userName 작성자
   * @param password 비밀번호
   * @param views 조회수
   * @param createdAt 등록일시
   * @param editedAt 수정일시
   */
  public Board(Long boardId, Long categoryId, String title, String content,
               String userName, String password, Long views,
               LocalDateTime createdAt, LocalDateTime editedAt) {
    this.boardId = boardId;
    this.categoryId = categoryId;
    this.title = title;
    this.content = content;
    this.userName = userName;
    this.password = password;
    this.views = views;
    this.createdAt = createdAt;
    this.editedAt = editedAt;
  }

  /**
   * 게시글 ID를 반환합니다.
   *
   * @return 게시글 ID
   */
  public Long getBoardId() {
    return boardId;
  }

  /**
   * 게시글 ID를 설정합니다.
   *
   * @param boardId 게시글 ID
   */
  public void setBoardId(Long boardId) {
    this.boardId = boardId;
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
   * 제목을 반환합니다.
   *
   * @return 제목
   */
  public String getTitle() {
    return title;
  }

  /**
   * 제목을 설정합니다.
   *
   * @param title 제목 (4~1000자)
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 내용을 반환합니다.
   *
   * @return 내용
   */
  public String getContent() {
    return content;
  }

  /**
   * 내용을 설정합니다.
   *
   * @param content 내용 (4~4000자)
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 작성자를 반환합니다.
   *
   * @return 작성자
   */
  public String getUserName() {
    return userName;
  }

  /**
   * 작성자를 설정합니다.
   *
   * @param userName 작성자 (2~10자)
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * 비밀번호를 반환합니다.
   *
   * @return 비밀번호 (SHA-256 해시)
   */
  public String getPassword() {
    return password;
  }

  /**
   * 비밀번호를 설정합니다.
   *
   * @param password 비밀번호 (8~12자 영문+숫자 조합)
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * 조회수를 반환합니다.
   *
   * @return 조회수
   */
  public Long getViews() {
    return views;
  }

  /**
   * 조회수를 설정합니다.
   *
   * @param views 조회수
   */
  public void setViews(Long views) {
    this.views = views;
  }

  /**
   * 등록일시를 반환합니다.
   *
   * @return 등록일시
   */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * 등록일시를 설정합니다.
   *
   * @param createdAt 등록일시
   */
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * 수정일시를 반환합니다.
   *
   * @return 수정일시
   */
  public LocalDateTime getEditedAt() {
    return editedAt;
  }

  /**
   * 수정일시를 설정합니다.
   *
   * @param editedAt 수정일시
   */
  public void setEditedAt(LocalDateTime editedAt) {
    this.editedAt = editedAt;
  }

  /**
   * 카테고리 이름을 반환합니다.
   *
   * @return 카테고리 이름 (JOIN 결과)
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

  /**
   * 첨부파일 개수를 반환합니다.
   *
   * @return 첨부파일 개수
   */
  public Integer getFileCount() {
    return fileCount;
  }

  /**
   * 첨부파일 개수를 설정합니다.
   *
   * @param fileCount 첨부파일 개수
   */
  public void setFileCount(Integer fileCount) {
    this.fileCount = fileCount;
  }

  /**
   * 조회수를 1 증가시킵니다.
   */
  public void incrementViews() {
    if (this.views == null) {
      this.views = 1L;
    } else {
      this.views++;
    }
  }

  @Override
  public String toString() {
    return "Board{" +
        "boardId=" + boardId +
        ", categoryId=" + categoryId +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", userName='" + userName + '\'' +
        ", views=" + views +
        ", createdAt=" + createdAt +
        ", editedAt=" + editedAt +
        ", categoryName='" + categoryName + '\'' +
        ", fileCount=" + fileCount +
        '}';
  }
}
