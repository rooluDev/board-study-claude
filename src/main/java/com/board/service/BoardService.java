package com.board.service;

import com.board.dao.BoardDAO;
import com.board.dao.CommentDAO;
import com.board.dto.Board;
import com.board.exception.AuthenticationException;
import com.board.exception.BoardException;
import com.board.exception.FileUploadException;
import com.board.exception.ValidationException;
import com.board.util.ValidationUtil;
import jakarta.servlet.http.Part;

import java.util.Collection;
import java.util.List;

import static com.board.util.Constants.PAGE_SIZE;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * DAO를 통해 데이터를 조회하고, 비즈니스 규칙을 적용합니다.
 */
public class BoardService {
  /** BoardDAO 인스턴스 */
  private final BoardDAO boardDAO;
  /** CommentDAO 인스턴스 */
  private final CommentDAO commentDAO;
  /** FileService 인스턴스 */
  private final FileService fileService;

  /**
   * 기본 생성자
   * BoardDAO, CommentDAO, FileService를 초기화합니다.
   */
  public BoardService() {
    this.boardDAO = new BoardDAO();
    this.commentDAO = new CommentDAO();
    this.fileService = new FileService();
  }

  /**
   * 의존성 주입을 위한 생성자
   *
   * @param boardDAO BoardDAO 인스턴스
   * @param commentDAO CommentDAO 인스턴스
   * @param fileService FileService 인스턴스
   */
  public BoardService(BoardDAO boardDAO, CommentDAO commentDAO, FileService fileService) {
    this.boardDAO = boardDAO;
    this.commentDAO = commentDAO;
    this.fileService = fileService;
  }

  /**
   * 게시글 목록을 조회합니다. (페이징, 검색)
   *
   * @param page 페이지 번호 (1부터 시작)
   * @param category 카테고리 ID (선택)
   * @param from 등록일시 시작 (선택)
   * @param to 등록일시 종료 (선택)
   * @param keyword 검색어 (선택, 제목/내용/작성자)
   * @return 게시글 목록
   * @throws ValidationException 페이지 번호가 유효하지 않은 경우
   * @throws BoardException 조회 실패 시
   */
  public List<Board> getBoardList(int page, Integer category, String from,
                                   String to, String keyword) {
    // 페이지 번호 유효성 검증
    int validPage = ValidationUtil.validatePage(page);

    // OFFSET 계산: (페이지번호 - 1) * 페이지크기
    int offset = (validPage - 1) * PAGE_SIZE;

    // 게시글 목록 조회
    return boardDAO.selectBoardList(PAGE_SIZE, offset, category, from, to, keyword);
  }

  /**
   * 전체 게시글 수를 조회합니다. (검색)
   *
   * @param category 카테고리 ID (선택)
   * @param from 등록일시 시작 (선택)
   * @param to 등록일시 종료 (선택)
   * @param keyword 검색어 (선택, 제목/내용/작성자)
   * @return 전체 게시글 수
   * @throws BoardException 조회 실패 시
   */
  public int getTotalBoardCount(Integer category, String from, String to, String keyword) {
    return boardDAO.countBoards(category, from, to, keyword);
  }

  /**
   * 전체 페이지 수를 계산합니다. (검색)
   *
   * @param category 카테고리 ID (선택)
   * @param from 등록일시 시작 (선택)
   * @param to 등록일시 종료 (선택)
   * @param keyword 검색어 (선택, 제목/내용/작성자)
   * @return 전체 페이지 수
   * @throws BoardException 조회 실패 시
   */
  public int getTotalPages(Integer category, String from, String to, String keyword) {
    int totalCount = getTotalBoardCount(category, from, to, keyword);
    return (int) Math.ceil((double) totalCount / PAGE_SIZE);
  }

  /**
   * 게시글 ID로 게시글을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 게시글 정보
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 게시글이 존재하지 않거나 조회 실패 시
   */
  public Board getBoardById(Long boardId) {
    // 게시글 ID 유효성 검증
    ValidationUtil.validateBoardId(boardId);

    // 게시글 조회
    Board board = boardDAO.selectBoardById(boardId);

    if (board == null) {
      throw new BoardException("게시글을 찾을 수 없습니다.");
    }

    return board;
  }

  /**
   * 게시글을 조회하고 조회수를 1 증가시킵니다.
   *
   * @param boardId 게시글 ID
   * @return 게시글 정보
   * @throws ValidationException 게시글 ID가 유효하지 않은 경우
   * @throws BoardException 게시글이 존재하지 않거나 조회 실패 시
   */
  public Board viewBoard(Long boardId) {
    // 게시글 조회
    Board board = getBoardById(boardId);

    // 조회수 증가
    boardDAO.updateViewCount(boardId);

    // 조회수 증가 반영
    board.incrementViews();

    return board;
  }

  /**
   * 게시글을 등록합니다.
   *
   * @param board 등록할 게시글 정보
   * @return 생성된 게시글 ID
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 등록 실패 시
   */
  public Long createBoard(Board board) {
    // 입력값 검증
    ValidationUtil.validateCategoryId(board.getCategoryId());
    ValidationUtil.validateWriter(board.getUserName());
    ValidationUtil.validatePassword(board.getPassword());
    ValidationUtil.validateTitle(board.getTitle());
    ValidationUtil.validateContent(board.getContent());

    // 게시글 등록
    return boardDAO.insertBoard(board);
  }

  /**
   * 게시글을 등록합니다. (파일 업로드 포함)
   *
   * @param board 등록할 게시글 정보
   * @param fileParts 업로드할 파일 Part 목록
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @return 생성된 게시글 ID
   * @throws ValidationException 입력값 검증 실패 시
   * @throws FileUploadException 파일 업로드 실패 시
   * @throws BoardException 등록 실패 시
   */
  public Long createBoard(Board board, Collection<Part> fileParts, String uploadPath) {
    // 입력값 검증
    ValidationUtil.validateCategoryId(board.getCategoryId());
    ValidationUtil.validateWriter(board.getUserName());
    ValidationUtil.validatePassword(board.getPassword());
    ValidationUtil.validateTitle(board.getTitle());
    ValidationUtil.validateContent(board.getContent());

    Long boardId = null;

    try {
      // 게시글 등록
      boardId = boardDAO.insertBoard(board);

      // 파일 업로드 (파일이 있는 경우)
      if (fileParts != null && !fileParts.isEmpty()) {
        fileService.uploadFiles(fileParts, boardId, uploadPath);
      }

      return boardId;

    } catch (ValidationException | FileUploadException e) {
      // 파일 업로드 실패 시 게시글 롤백
      rollbackBoardCreation(boardId);
      throw e;
    } catch (Exception e) {
      // 예상치 못한 오류 시 게시글 롤백
      rollbackBoardCreation(boardId);
      throw new BoardException(
          "Failed to create board: " + e.getMessage(),
          "게시글 등록에 실패했습니다.",
          e
      );
    }
  }

  /**
   * 게시글 생성 실패 시 롤백 처리를 수행합니다.
   *
   * @param boardId 롤백할 게시글 ID
   */
  private void rollbackBoardCreation(Long boardId) {
    if (boardId != null) {
      try {
        boardDAO.deleteBoard(boardId);
      } catch (Exception rollbackEx) {
        // 롤백 실패 로그
        System.err.println("Failed to rollback board creation: " + boardId);
        rollbackEx.printStackTrace();
      }
    }
  }

  /**
   * 게시글을 수정합니다.
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호 (평문)
   * @param title 수정할 제목
   * @param content 수정할 내용
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 비밀번호 불일치 또는 수정 실패 시
   */
  public void updateBoard(Long boardId, String password, String title, String content) {
    // 입력값 검증
    ValidationUtil.validateBoardId(boardId);
    ValidationUtil.validatePassword(password);
    ValidationUtil.validateTitle(title);
    ValidationUtil.validateContent(content);

    // 비밀번호 확인
    if (!boardDAO.verifyPassword(boardId, password)) {
      throw new BoardException("비밀번호가 일치하지 않습니다.");
    }

    // 게시글 수정
    Board board = new Board();
    board.setBoardId(boardId);
    board.setTitle(title);
    board.setContent(content);

    boardDAO.updateBoard(board);
  }

  /**
   * 게시글을 삭제합니다.
   * 삭제 순서: 댓글 → 첨부파일(DB) → 첨부파일(물리) → 게시글
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호 (평문)
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @throws AuthenticationException 비밀번호 불일치 시
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 삭제 실패 시
   */
  public void deleteBoard(Long boardId, String password, String uploadPath) {
    // 입력값 검증
    ValidationUtil.validateBoardId(boardId);
    ValidationUtil.validatePassword(password);

    // 비밀번호 확인
    checkPassword(boardId, password);

    try {
      // 1. 댓글 삭제
      commentDAO.deleteCommentsByBoardId(boardId);

      // 2. 첨부파일 삭제 (DB + 물리 파일)
      fileService.deleteAllFilesByBoardId(boardId, uploadPath);

      // 3. 게시글 삭제
      boardDAO.deleteBoard(boardId);

    } catch (AuthenticationException e) {
      throw e;
    } catch (Exception e) {
      throw new BoardException(
          "Failed to delete board: " + boardId,
          "게시글 삭제에 실패했습니다.",
          e
      );
    }
  }

  /**
   * 비밀번호를 확인합니다.
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호 (평문)
   * @return 일치하면 true, 불일치하면 false
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 확인 실패 시
   */
  public boolean verifyPassword(Long boardId, String password) {
    // 입력값 검증
    ValidationUtil.validateBoardId(boardId);
    ValidationUtil.validatePassword(password);

    // 비밀번호 확인
    return boardDAO.verifyPassword(boardId, password);
  }

  /**
   * 비밀번호를 확인합니다. (인증 실패 시 예외 발생)
   *
   * @param boardId 게시글 ID
   * @param password 비밀번호 (평문)
   * @throws AuthenticationException 비밀번호 불일치 시
   * @throws ValidationException 입력값 검증 실패 시
   * @throws BoardException 확인 실패 시
   */
  public void checkPassword(Long boardId, String password) {
    if (!verifyPassword(boardId, password)) {
      throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
    }
  }

  /**
   * 게시글을 수정합니다. (파일 업데이트 포함)
   *
   * @param board 수정할 게시글 정보 (boardId, password, title, content 필수)
   * @param newFileParts 새로 추가할 파일 Part 목록
   * @param deletedFileIds 삭제할 파일 ID 리스트
   * @param uploadPath 업로드 디렉토리 절대 경로
   * @throws AuthenticationException 비밀번호 불일치 시
   * @throws ValidationException 입력값 검증 실패 시
   * @throws FileUploadException 파일 업로드 실패 시
   * @throws BoardException 수정 실패 시
   */
  public void updateBoard(Board board, Collection<Part> newFileParts,
                          List<Long> deletedFileIds, String uploadPath) {
    // 입력값 검증
    ValidationUtil.validateBoardId(board.getBoardId());
    ValidationUtil.validatePassword(board.getPassword());
    ValidationUtil.validateTitle(board.getTitle());
    ValidationUtil.validateContent(board.getContent());

    // 비밀번호 확인
    checkPassword(board.getBoardId(), board.getPassword());

    try {
      // 게시글 수정
      boardDAO.updateBoard(board);

      // 파일 업데이트
      if ((newFileParts != null && !newFileParts.isEmpty()) ||
          (deletedFileIds != null && !deletedFileIds.isEmpty())) {
        fileService.updateFiles(
            board.getBoardId(),
            newFileParts,
            deletedFileIds,
            uploadPath
        );
      }

    } catch (AuthenticationException | ValidationException | FileUploadException e) {
      throw e;
    } catch (Exception e) {
      throw new BoardException(
          "Failed to update board: " + board.getBoardId(),
          "게시글 수정에 실패했습니다.",
          e
      );
    }
  }
}
