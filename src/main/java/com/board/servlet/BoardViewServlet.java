package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.BoardFile;
import com.board.dto.Comment;
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

/**
 * 게시글 상세 조회를 처리하는 Servlet
 *
 * URL: GET /board/view
 * 기능: 게시글 상세 정보, 첨부파일, 댓글을 조회하고 view.jsp로 포워딩
 */
@WebServlet("/board/view")
public class BoardViewServlet extends HttpServlet {
  /** BoardService 인스턴스 */
  private BoardService boardService;

  /** FileService 인스턴스 */
  private FileService fileService;

  /** CommentService 인스턴스 */
  private CommentService commentService;

  @Override
  public void init() throws ServletException {
    this.boardService = new BoardService();
    this.fileService = new FileService();
    this.commentService = new CommentService();
  }

  /**
   * GET 요청 처리: 게시글 상세 조회
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 처리 오류
   * @throws IOException I/O 오류
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // boardId 파라미터 추출 (필수)
      Long boardId = getBoardIdParameter(request);

      // 검색 조건 파라미터 추출 (선택)
      String page = request.getParameter("page");
      String category = request.getParameter("category");
      String from = request.getParameter("from");
      String to = request.getParameter("to");
      String keyword = request.getParameter("keyword");

      // 게시글 조회 및 조회수 증가
      Board board = boardService.viewBoard(boardId);

      // 첨부파일 목록 조회
      List<BoardFile> fileList = fileService.getFilesByBoardId(boardId);

      // 댓글 목록 조회
      List<Comment> commentList = commentService.getCommentsByBoardId(boardId);

      // request attribute에 데이터 설정
      request.setAttribute("board", board);
      request.setAttribute("fileList", fileList);
      request.setAttribute("commentList", commentList);

      // 검색 조건 파라미터 설정 (검색 조건 유지용)
      request.setAttribute("page", page != null ? page : "1");
      request.setAttribute("category", category);
      request.setAttribute("from", from);
      request.setAttribute("to", to);
      request.setAttribute("keyword", keyword);

      // view.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/view.jsp")
          .forward(request, response);

    } catch (BoardException e) {
      // 비즈니스 로직 오류
      handleException(request, response, e, e.getUserMessage());
    } catch (Exception e) {
      // 시스템 오류
      handleException(request, response, e, "시스템 오류가 발생했습니다.");
    }
  }

  /**
   * 쿼리 파라미터에서 게시글 ID를 추출합니다.
   *
   * @param request HTTP 요청
   * @return 게시글 ID
   * @throws BoardException boardId가 없거나 유효하지 않은 경우
   */
  private Long getBoardIdParameter(HttpServletRequest request) {
    String boardIdParam = request.getParameter("boardId");

    if (boardIdParam == null || boardIdParam.isEmpty()) {
      throw new BoardException("게시글 ID가 필요합니다.");
    }

    try {
      Long boardId = Long.parseLong(boardIdParam);
      if (boardId <= 0) {
        throw new BoardException("유효하지 않은 게시글 ID입니다.");
      }
      return boardId;
    } catch (NumberFormatException e) {
      throw new BoardException("유효하지 않은 게시글 ID입니다.");
    }
  }

  /**
   * 예외를 처리하고 에러 페이지로 포워딩합니다.
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @param e 발생한 예외
   * @param userMessage 사용자에게 표시할 메시지
   * @throws ServletException Servlet 처리 오류
   * @throws IOException I/O 오류
   */
  private void handleException(HttpServletRequest request, HttpServletResponse response,
                                Exception e, String userMessage)
      throws ServletException, IOException {
    // 로그 출력 (실제 운영 환경에서는 Logger 사용)
    e.printStackTrace();

    // 에러 메시지 설정
    request.setAttribute("error", userMessage);

    // 에러 페이지로 forward
    request.getRequestDispatcher("/WEB-INF/views/error.jsp")
        .forward(request, response);
  }
}
