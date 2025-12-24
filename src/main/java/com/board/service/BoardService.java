package com.board.service;

import com.board.dao.BoardDAO;
import com.board.dto.Board;
import com.board.exception.BoardException;
import com.board.exception.ValidationException;
import com.board.util.Constants;
import com.board.util.ValidationUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 Service (비즈니스 로직 계층)
 *
 * 게시글 관련 비즈니스 로직을 처리
 * DAO를 호출하여 데이터베이스 작업 수행
 */
public class BoardService {

  private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

  /**
   * BoardDAO 인스턴스
   */
  private final BoardDAO boardDAO;

  /**
   * 기본 생성자
   * BoardDAO 인스턴스를 생성합니다.
   */
  public BoardService() {
    this.boardDAO = new BoardDAO();
  }

  /**
   * 페이징된 게시글 목록을 조회합니다.
   *
   * @param page 페이지 번호 (1부터 시작)
   * @return 게시글 목록
   * @throws ValidationException 페이지 번호가 유효하지 않은 경우
   */
  public List<Board> getBoardList(int page) {
    // 페이지 번호 유효성 검증
    validatePageNumber(page);

    logger.info("게시글 목록 조회 요청: page={}", page);

    // DAO를 통해 게시글 목록 조회
    List<Board> boards = boardDAO.selectBoardList(page);

    logger.info("게시글 목록 조회 완료: {} 건", boards.size());
    return boards;
  }

  /**
   * 전체 게시글 수를 조회합니다.
   *
   * @return 전체 게시글 수
   */
  public int getTotalCount() {
    logger.debug("전체 게시글 수 조회 요청");

    int totalCount = boardDAO.countBoards();

    logger.info("전체 게시글 수: {} 건", totalCount);
    return totalCount;
  }

  /**
   * 전체 페이지 수를 계산합니다.
   *
   * @return 전체 페이지 수
   */
  public int getTotalPages() {
    int totalCount = getTotalCount();

    // 전체 페이지 수 = (전체 게시글 수 + 페이지 크기 - 1) / 페이지 크기
    // 예: 총 25개, 페이지당 10개 -> (25 + 10 - 1) / 10 = 3 페이지
    int totalPages = (totalCount + Constants.PAGE_SIZE - 1) / Constants.PAGE_SIZE;

    // 최소 1 페이지
    if (totalPages == 0) {
      totalPages = 1;
    }

    logger.debug("전체 페이지 수: {} 페이지 (전체 게시글 {} 건)", totalPages, totalCount);
    return totalPages;
  }

  /**
   * 페이지 번호의 유효성을 검증합니다.
   *
   * @param page 검증할 페이지 번호
   * @throws ValidationException 페이지 번호가 1 미만인 경우
   */
  private void validatePageNumber(int page) {
    if (page < 1) {
      throw new ValidationException("페이지 번호는 1 이상이어야 합니다.");
    }
  }

  /**
   * 게시글 ID로 게시글 상세 정보를 조회합니다.
   * 조회 시 조회수가 1 증가합니다.
   *
   * @param boardId 게시글 ID
   * @return 게시글 상세 정보
   * @throws ValidationException boardId가 유효하지 않은 경우
   * @throws BoardException 게시글을 찾을 수 없는 경우
   */
  public Board getBoardById(Long boardId) {
    // boardId 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    logger.info("게시글 상세 조회 요청: boardId={}", boardId);

    // 조회수 증가
    boardDAO.updateViewCount(boardId);

    // 게시글 조회
    Board board = boardDAO.selectBoardById(boardId);

    if (board == null) {
      logger.warn("게시글을 찾을 수 없음: boardId={}", boardId);
      throw new BoardException("게시글을 찾을 수 없습니다.");
    }

    logger.info("게시글 상세 조회 완료: boardId={}, title={}", boardId, board.getTitle());
    return board;
  }
}
