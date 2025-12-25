package com.board.service;

import com.board.dao.BoardDAO;
import com.board.dto.Board;
import com.board.exception.AuthenticationException;
import com.board.exception.BoardException;
import com.board.exception.ValidationException;
import com.board.util.Constants;
import com.board.util.ValidationUtil;
import jakarta.servlet.http.Part;
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
   * FileService 인스턴스
   */
  private final FileService fileService;

  /**
   * 기본 생성자
   * BoardDAO와 FileService 인스턴스를 생성합니다.
   */
  public BoardService() {
    this.boardDAO = new BoardDAO();
    this.fileService = new FileService();
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

  /**
   * 새로운 게시글을 등록합니다.
   * - 입력값 검증
   * - 게시글 정보 DB 삽입
   * - 첨부파일 업로드 (있는 경우)
   *
   * @param board 등록할 게시글 정보
   * @param fileParts 첨부파일 Part 목록 (null 또는 빈 리스트 가능)
   * @return 등록된 게시글 ID
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 게시글 등록 실패 시
   */
  public Long createBoard(Board board, List<Part> fileParts) {
    logger.info("게시글 등록 요청: title={}, userName={}, categoryId={}",
        board.getTitle(), board.getUserName(), board.getCategoryId());

    // 입력값 검증
    validateBoardInput(board);

    try {
      // 게시글 DB 삽입
      boardDAO.insertBoard(board);
      Long boardId = board.getBoardId();

      logger.info("게시글 등록 완료: boardId={}", boardId);

      // 첨부파일 업로드 (파일이 있는 경우)
      if (fileParts != null && !fileParts.isEmpty()) {
        try {
          fileService.uploadFiles(fileParts, boardId);
        } catch (Exception e) {
          logger.error("파일 업로드 실패: boardId={}, error={}", boardId, e.getMessage());
          throw new BoardException("게시글은 등록되었으나 파일 업로드에 실패했습니다: " + e.getMessage(), e);
        }
      }

      logger.info("게시글 등록 프로세스 완료: boardId={}", boardId);
      return boardId;

    } catch (BoardException | ValidationException e) {
      throw e;
    } catch (Exception e) {
      logger.error("게시글 등록 실패: {}", e.getMessage(), e);
      throw new BoardException("게시글 등록 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 게시글 입력값의 유효성을 검증합니다.
   *
   * @param board 검증할 게시글 정보
   * @throws ValidationException 검증 실패 시
   */
  private void validateBoardInput(Board board) {
    // 카테고리 ID 검증
    ValidationUtil.validateCategoryId(board.getCategoryId());

    // 제목 검증 (4~1000자)
    ValidationUtil.validateTitle(board.getTitle());

    // 내용 검증 (4~4000자)
    ValidationUtil.validateContent(board.getContent());

    // 작성자명 검증 (2~10자)
    ValidationUtil.validateUserName(board.getUserName());

    // 비밀번호 검증 (8~12자, 영문+숫자)
    ValidationUtil.validatePassword(board.getPassword());

    logger.debug("게시글 입력값 검증 완료");
  }

  /**
   * 게시글 비밀번호를 확인합니다.
   * 비밀번호가 일치하지 않으면 AuthenticationException을 던집니다.
   *
   * @param boardId 게시글 ID
   * @param password 확인할 비밀번호
   * @throws ValidationException boardId 또는 password가 유효하지 않은 경우
   * @throws AuthenticationException 비밀번호가 일치하지 않는 경우
   */
  public void checkPassword(Long boardId, String password) {
    // boardId 검증
    ValidationUtil.validateBoardId(boardId);

    // 비밀번호 검증
    if (password == null || password.trim().isEmpty()) {
      throw new ValidationException("비밀번호를 입력해주세요.");
    }

    logger.info("비밀번호 확인 요청: boardId={}", boardId);

    // DAO를 통해 비밀번호 확인
    boolean isMatch = boardDAO.checkPassword(boardId, password);

    if (!isMatch) {
      logger.warn("비밀번호 불일치: boardId={}", boardId);
      throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
    }

    logger.info("비밀번호 확인 성공: boardId={}", boardId);
  }

  /**
   * 게시글을 수정합니다.
   * - 입력값 검증
   * - 비밀번호 확인
   * - 게시글 수정
   * - 파일 업데이트
   *
   * @param board 수정할 게시글 정보 (boardId, title, content, password)
   * @param newFiles 새로 추가할 파일 Part 목록
   * @param deletedFileIds 삭제할 기존 파일 ID 목록
   * @throws ValidationException 입력값 검증 실패 시
   * @throws AuthenticationException 비밀번호 불일치 시
   * @throws BoardException 게시글 수정 실패 시
   */
  public void updateBoard(Board board, List<Part> newFiles, List<Long> deletedFileIds) {
    logger.info("게시글 수정 요청: boardId={}, title={}",
        board.getBoardId(), board.getTitle());

    // boardId 검증
    ValidationUtil.validateBoardId(board.getBoardId());

    // 제목, 내용 검증
    ValidationUtil.validateTitle(board.getTitle());
    ValidationUtil.validateContent(board.getContent());

    // 비밀번호 확인
    checkPassword(board.getBoardId(), board.getPassword());

    try {
      // 게시글 수정 (제목, 내용)
      boardDAO.updateBoard(board);

      logger.info("게시글 수정 완료: boardId={}", board.getBoardId());

      // 파일 업데이트 (파일 변경사항이 있는 경우)
      if ((deletedFileIds != null && !deletedFileIds.isEmpty())
          || (newFiles != null && !newFiles.isEmpty())) {
        try {
          fileService.updateFiles(board.getBoardId(), newFiles, deletedFileIds);
        } catch (Exception e) {
          logger.error("파일 업데이트 실패: boardId={}, error={}",
              board.getBoardId(), e.getMessage());
          throw new BoardException("게시글은 수정되었으나 파일 업데이트에 실패했습니다: "
              + e.getMessage(), e);
        }
      }

      logger.info("게시글 수정 프로세스 완료: boardId={}", board.getBoardId());

    } catch (AuthenticationException | ValidationException e) {
      throw e;
    } catch (BoardException e) {
      throw e;
    } catch (Exception e) {
      logger.error("게시글 수정 실패: {}", e.getMessage(), e);
      throw new BoardException("게시글 수정 중 오류가 발생했습니다.", e);
    }
  }
}
