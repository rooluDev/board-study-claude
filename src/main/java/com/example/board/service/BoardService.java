package com.example.board.service;

import com.example.board.dao.BoardDAO;
import com.example.board.dao.CategoryDAO;
import com.example.board.dao.CommentDAO;
import com.example.board.dao.FileDAO;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.CategoryDTO;
import com.example.board.dto.SearchCondition;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.PasswordMismatchException;
import com.example.board.util.ValidationUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 Service.
 */
public class BoardService {

  private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

  private final BoardDAO boardDAO;
  private final CommentDAO commentDAO;
  private final FileDAO fileDAO;
  private final CategoryDAO categoryDAO;

  public BoardService() {
    this.boardDAO = new BoardDAO();
    this.commentDAO = new CommentDAO();
    this.fileDAO = new FileDAO();
    this.categoryDAO = new CategoryDAO();
  }

  /**
   * 게시글 목록 조회.
   *
   * @param condition 검색 조건
   * @return 게시글 목록
   */
  public List<BoardDTO> getBoardList(SearchCondition condition) {
    logger.debug("게시글 목록 조회 시작: {}", condition);
    return boardDAO.selectList(condition);
  }

  /**
   * 게시글 총 개수 조회.
   *
   * @param condition 검색 조건
   * @return 게시글 개수
   */
  public int getBoardCount(SearchCondition condition) {
    return boardDAO.count(condition);
  }

  /**
   * 게시글 상세 조회.
   *
   * @param boardId 게시글 ID
   * @return 게시글 DTO
   * @throws BoardNotFoundException 게시글을 찾을 수 없는 경우
   */
  public BoardDTO getBoard(Long boardId) throws BoardNotFoundException {
    logger.debug("게시글 조회 시작: boardId={}", boardId);

    BoardDTO board = boardDAO.selectById(boardId);
    if (board == null) {
      logger.warn("게시글을 찾을 수 없음: boardId={}", boardId);
      throw new BoardNotFoundException("게시글을 찾을 수 없습니다. ID: " + boardId);
    }

    // 첨부파일 조회
    board.setFiles(fileDAO.selectByBoardId(boardId));

    // 댓글 조회
    board.setComments(commentDAO.selectByBoardId(boardId));

    logger.info("게시글 조회 성공: boardId={}, title={}", boardId, board.getTitle());
    return board;
  }

  /**
   * 게시글 등록.
   *
   * @param board 게시글 DTO
   * @throws Exception 등록 실패 시
   */
  public void createBoard(BoardDTO board) throws Exception {
    logger.debug("게시글 등록 시작: title={}", board.getTitle());

    // 입력값 검증
    ValidationUtil.validateCategoryId(board.getCategoryId());
    ValidationUtil.validateTitle(board.getTitle());
    ValidationUtil.validateContent(board.getContent());
    ValidationUtil.validateUserName(board.getUserName());
    ValidationUtil.validatePassword(board.getPassword());

    // 게시글 등록
    int result = boardDAO.insert(board);
    if (result == 0) {
      throw new RuntimeException("게시글 등록 실패");
    }

    logger.info("게시글 등록 성공: boardId={}, title={}", board.getBoardId(), board.getTitle());
  }

  /**
   * 게시글 수정.
   *
   * @param board 게시글 DTO
   * @throws Exception 수정 실패 시
   */
  public void updateBoard(BoardDTO board) throws Exception {
    logger.debug("게시글 수정 시작: boardId={}", board.getBoardId());

    // 입력값 검증
    ValidationUtil.validateTitle(board.getTitle());
    ValidationUtil.validateContent(board.getContent());

    // 비밀번호 확인
    if (!boardDAO.verifyPassword(board.getBoardId(), board.getPassword())) {
      throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
    }

    // 게시글 수정
    int result = boardDAO.update(board);
    if (result == 0) {
      throw new RuntimeException("게시글 수정 실패");
    }

    logger.info("게시글 수정 성공: boardId={}", board.getBoardId());
  }

  /**
   * 게시글 삭제.
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호
   * @throws Exception 삭제 실패 시
   */
  public void deleteBoard(Long boardId, String password) throws Exception {
    logger.debug("게시글 삭제 시작: boardId={}", boardId);

    // 비밀번호 확인
    if (!boardDAO.verifyPassword(boardId, password)) {
      throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
    }

    // 게시글 삭제 (파일, 댓글은 CASCADE로 자동 삭제)
    int result = boardDAO.delete(boardId);
    if (result == 0) {
      throw new RuntimeException("게시글 삭제 실패");
    }

    logger.info("게시글 삭제 성공: boardId={}", boardId);
  }

  /**
   * 조회수 증가.
   *
   * @param boardId 게시글 ID
   */
  public void incrementViews(Long boardId) {
    boardDAO.incrementViews(boardId);
    logger.debug("조회수 증가: boardId={}", boardId);
  }

  /**
   * 비밀번호 확인.
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호
   * @return 일치 여부
   */
  public boolean verifyPassword(Long boardId, String password) {
    return boardDAO.verifyPassword(boardId, password);
  }

  /**
   * 모든 카테고리 조회.
   *
   * @return 카테고리 목록
   */
  public List<CategoryDTO> getAllCategories() {
    return categoryDAO.selectAll();
  }
}
