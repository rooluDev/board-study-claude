package com.board.dto;

import java.sql.Timestamp;

/**
 * 게시글 DTO (Data Transfer Object)
 *
 * board 테이블과 매핑되는 데이터 전송 객체
 */
public class Board {

  /**
   * 게시글 ID (Primary Key)
   */
  private Long boardId;

  /**
   * 카테고리 ID (Foreign Key)
   */
  private Long categoryId;

  /**
   * 제목 (4~1000자)
   */
  private String title;

  /**
   * 내용 (4~4000자)
   */
  private String content;

  /**
   * 작성자 (4~10자)
   */
  private String userName;

  /**
   * 비밀번호 (SHA2-256 해시값, 64자)
   */
  private String password;

  /**
   * 조회수
   */
  private Long views;

  /**
   * 등록일시
   */
  private Timestamp createdAt;

  /**
   * 수정일시
   */
  private Timestamp editedAt;

  /**
   * 카테고리명 (조인을 통해 조회, 목록 조회용)
   */
  private String categoryName;

  /**
   * 첨부파일 존재 여부 (목록 조회용)
   */
  private Boolean hasFile;

  /**
   * 기본 생성자
   */
  public Board() {
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
               Timestamp createdAt, Timestamp editedAt) {
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
   * @param title 제목
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
   * @param content 내용
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
   * @param userName 작성자
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * 비밀번호를 반환합니다.
   *
   * @return 비밀번호 (해시값)
   */
  public String getPassword() {
    return password;
  }

  /**
   * 비밀번호를 설정합니다.
   *
   * @param password 비밀번호 (해시값)
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
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  /**
   * 등록일시를 설정합니다.
   *
   * @param createdAt 등록일시
   */
  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * 수정일시를 반환합니다.
   *
   * @return 수정일시
   */
  public Timestamp getEditedAt() {
    return editedAt;
  }

  /**
   * 수정일시를 설정합니다.
   *
   * @param editedAt 수정일시
   */
  public void setEditedAt(Timestamp editedAt) {
    this.editedAt = editedAt;
  }

  /**
   * 카테고리명을 반환합니다.
   *
   * @return 카테고리명
   */
  public String getCategoryName() {
    return categoryName;
  }

  /**
   * 카테고리명을 설정합니다.
   *
   * @param categoryName 카테고리명
   */
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  /**
   * 첨부파일 존재 여부를 반환합니다.
   *
   * @return 첨부파일이 있으면 true, 없으면 false
   */
  public Boolean getHasFile() {
    return hasFile;
  }

  /**
   * 첨부파일 존재 여부를 설정합니다.
   *
   * @param hasFile 첨부파일 존재 여부
   */
  public void setHasFile(Boolean hasFile) {
    this.hasFile = hasFile;
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
        '}';
  }
}
