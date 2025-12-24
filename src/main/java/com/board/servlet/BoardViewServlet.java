package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.Comment;
import com.board.dto.File;
import com.board.exception.BoardException;
import com.board.service.BoardService;
import com.board.service.CommentService;
import com.board.service.FileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 상세 조회 Servlet
 *
 * GET /board/view 요청을 처리하여 게시글 상세 페이지를 제공
 * 조회수 증가, 첨부파일 목록, 댓글 목록을 함께 조회
 */
@WebServlet(name = "BoardViewServlet", urlPatterns = "/board/view")
public class BoardViewServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardViewServlet.class);

  /**
   * BoardService 인스턴스
   */
  private BoardService boardService;

  /**
   * FileService 인스턴스
   */
  private FileService fileService;

  /**
   * CommentService 인스턴스
   */
  private CommentService commentService;

  /**
   * Servlet 초기화
   * Service 인스턴스들을 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.boardService = new BoardService();
    this.fileService = new FileService();
    this.commentService = new CommentService();
    logger.info("BoardViewServlet 초기화 완료");
  }

  /**
   * GET 요청 처리
   * 게시글 상세 정보를 조회하여 view.jsp로 forward
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 상세 조회 요청");

    try {
      // boardId 파라미터 추출 (필수)
      Long boardId = getBoardIdParameter(request);

      logger.debug("요청 boardId: {}", boardId);

      // 게시글 조회 (조회수 증가 포함)
      Board board = boardService.getBoardById(boardId);

      // 첨부파일 목록 조회
      List<File> files = fileService.getFilesByBoardId(boardId);

      // 댓글 목록 조회
      List<Comment> comments = commentService.getCommentsByBoardId(boardId);

      // 검색 조건 파라미터 추출 (목록으로 돌아갈 때 사용)
      String page = request.getParameter("page");
      String category = request.getParameter("category");
      String from = request.getParameter("from");
      String to = request.getParameter("to");
      String keyword = request.getParameter("keyword");

      // request attribute 설정
      request.setAttribute("board", board);
      request.setAttribute("files", files);
      request.setAttribute("comments", comments);

      // 검색 조건 유지
      request.setAttribute("page", page);
      request.setAttribute("category", category);
      request.setAttribute("from", from);
      request.setAttribute("to", to);
      request.setAttribute("keyword", keyword);

      logger.info("게시글 상세 조회 성공: boardId={}, 첨부파일 {} 건, 댓글 {} 건",
          boardId, files.size(), comments.size());

      // view.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/view.jsp").forward(request, response);

    } catch (BoardException e) {
      logger.error("게시글 상세 조회 중 오류 발생: {}", e.getMessage(), e);

      // 오류 메시지 설정
      request.setAttribute("errorMessage", e.getMessage());

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);

    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);

      // 일반적인 오류 메시지 설정
      request.setAttribute("errorMessage", "게시글을 조회하는 중 오류가 발생했습니다.");

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
    }
  }

  /**
   * 요청 파라미터에서 게시글 ID를 추출합니다.
   *
   * @param request HTTP 요청
   * @return 게시글 ID
   * @throws BoardException boardId 파라미터가 없거나 잘못된 형식인 경우
   */
  private Long getBoardIdParameter(HttpServletRequest request) {
    String boardIdParam = request.getParameter("boardId");

    if (boardIdParam == null || boardIdParam.trim().isEmpty()) {
      throw new BoardException("게시글 ID가 필요합니다.");
    }

    try {
      return Long.parseLong(boardIdParam);
    } catch (NumberFormatException e) {
      logger.warn("잘못된 게시글 ID 형식: {}", boardIdParam);
      throw new BoardException("유효하지 않은 게시글 ID입니다.");
    }
  }
}
