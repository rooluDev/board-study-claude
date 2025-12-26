package com.board.servlet;

import com.board.dto.Board;
import com.board.exception.BoardException;
import com.board.service.BoardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 게시글 목록 조회를 처리하는 Servlet
 *
 * URL: GET /boards
 * 기능: 게시글 목록을 페이징하여 조회하고 list.jsp로 포워딩
 */
@WebServlet("/boards")
public class BoardListServlet extends HttpServlet {
  /** BoardService 인스턴스 */
  private BoardService boardService;

  @Override
  public void init() throws ServletException {
    this.boardService = new BoardService();
  }

  /**
   * GET 요청 처리: 게시글 목록 조회 및 검색
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
      // 쿼리 파라미터에서 page 추출 (기본값: 1)
      int page = getPageParameter(request);

      // 검색 파라미터 추출
      Integer category = getCategoryParameter(request);
      String from = getStringParameter(request, "from");
      String to = getStringParameter(request, "to");
      String keyword = getStringParameter(request, "keyword");

      // 게시글 목록 조회
      List<Board> boardList = boardService.getBoardList(page, category, from, to, keyword);

      // 전체 페이지 수 계산
      int totalPages = boardService.getTotalPages(category, from, to, keyword);

      // 전체 게시글 수 조회
      int totalCount = boardService.getTotalBoardCount(category, from, to, keyword);

      // request attribute에 데이터 설정
      request.setAttribute("boardList", boardList);
      request.setAttribute("currentPage", page);
      request.setAttribute("totalPages", totalPages);
      request.setAttribute("totalCount", totalCount);

      // 검색 조건 설정 (JSP에서 사용)
      request.setAttribute("category", category);
      request.setAttribute("from", from);
      request.setAttribute("to", to);
      request.setAttribute("keyword", keyword);

      // list.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/list.jsp")
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
   * 쿼리 파라미터에서 페이지 번호를 추출합니다.
   *
   * @param request HTTP 요청
   * @return 페이지 번호 (기본값: 1)
   */
  private int getPageParameter(HttpServletRequest request) {
    String pageParam = request.getParameter("page");
    if (pageParam == null || pageParam.isEmpty()) {
      return 1;
    }

    try {
      int page = Integer.parseInt(pageParam);
      return page > 0 ? page : 1;
    } catch (NumberFormatException e) {
      return 1;
    }
  }

  /**
   * 쿼리 파라미터에서 카테고리 ID를 추출합니다.
   *
   * @param request HTTP 요청
   * @return 카테고리 ID (null이면 전체)
   */
  private Integer getCategoryParameter(HttpServletRequest request) {
    String categoryParam = request.getParameter("category");
    if (categoryParam == null || categoryParam.isEmpty()) {
      return null;
    }

    try {
      return Integer.parseInt(categoryParam);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * 쿼리 파라미터에서 문자열 값을 추출합니다.
   *
   * @param request HTTP 요청
   * @param paramName 파라미터 이름
   * @return 문자열 값 (없으면 null)
   */
  private String getStringParameter(HttpServletRequest request, String paramName) {
    String value = request.getParameter(paramName);
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    return value.trim();
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
