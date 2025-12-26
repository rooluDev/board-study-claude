package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.Category;
import com.board.exception.BoardException;
import com.board.service.BoardService;
import com.board.service.CategoryService;
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
 * 게시글 목록 조회 Servlet
 *
 * GET /boards 요청을 처리하여 게시글 목록 페이지를 제공
 * 페이징 처리를 통해 한 페이지당 10개의 게시글을 표시
 */
@WebServlet(name = "BoardListServlet", urlPatterns = "/boards")
public class BoardListServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardListServlet.class);

  /**
   * BoardService 인스턴스
   */
  private BoardService boardService;

  /**
   * CategoryService 인스턴스
   */
  private CategoryService categoryService;

  /**
   * Servlet 초기화
   * BoardService 및 CategoryService 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.boardService = new BoardService();
    this.categoryService = new CategoryService();
    logger.info("BoardListServlet 초기화 완료");
  }

  /**
   * GET 요청 처리
   * 게시글 목록을 조회하여 list.jsp로 forward (검색 기능 포함)
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 목록 조회 요청");

    try {
      // 페이지 번호 파라미터 추출 (기본값: 1)
      int page = getPageParameter(request);

      // 검색 파라미터 추출
      Integer category = getCategoryParameter(request);
      String from = getSearchParameter(request, "from");
      String to = getSearchParameter(request, "to");
      String keyword = getSearchParameter(request, "keyword");

      // from, to 기본값 설정 (파라미터가 없을 때만)
      if (from == null) {
        // 1년 전 날짜
        java.time.LocalDate oneYearAgo = java.time.LocalDate.now().minusYears(1);
        from = oneYearAgo.toString(); // yyyy-MM-dd 형식
      }
      if (to == null) {
        // 현재 날짜
        java.time.LocalDate today = java.time.LocalDate.now();
        to = today.toString(); // yyyy-MM-dd 형식
      }

      logger.debug("검색 조건 - page: {}, category: {}, from: {}, to: {}, keyword: {}",
          page, category, from, to, keyword);

      // 게시글 목록 조회 (검색 조건 포함)
      List<Board> boards = boardService.getBoardList(page, category, from, to, keyword);

      // 전체 페이지 수 계산 (검색 조건 포함)
      int totalPages = boardService.getTotalPages(category, from, to, keyword);

      // 전체 게시글 수 조회 (검색 조건 포함)
      int totalCount = boardService.getTotalCount(category, from, to, keyword);

      // 카테고리 목록 조회 (검색 폼용)
      List<Category> categories = categoryService.getAllCategories();

      // request attribute 설정 (JSP에서 사용)
      request.setAttribute("boards", boards);
      request.setAttribute("currentPage", page);
      request.setAttribute("totalPages", totalPages);
      request.setAttribute("totalCount", totalCount);
      request.setAttribute("categories", categories);

      // 검색 조건을 request attribute로 설정 (JSP에서 검색 폼 유지용)
      request.setAttribute("category", category != null ? String.valueOf(category) : "");
      request.setAttribute("from", from != null ? from : "");
      request.setAttribute("to", to != null ? to : "");
      request.setAttribute("keyword", keyword != null ? keyword : "");

      logger.info("게시글 목록 조회 성공: {} 건, 페이지 {}/{}",
          boards.size(), page, totalPages);

      // list.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/list.jsp").forward(request, response);

    } catch (BoardException e) {
      logger.error("게시글 목록 조회 중 오류 발생: {}", e.getMessage(), e);

      // 오류 메시지 설정
      request.setAttribute("errorMessage", e.getMessage());

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);

    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);

      // 일반적인 오류 메시지 설정
      request.setAttribute("errorMessage", "게시글 목록을 조회하는 중 오류가 발생했습니다.");

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
    }
  }

  /**
   * 요청 파라미터에서 페이지 번호를 추출합니다.
   * 파라미터가 없거나 잘못된 형식인 경우 기본값 1을 반환합니다.
   *
   * @param request HTTP 요청
   * @return 페이지 번호 (최소 1)
   */
  private int getPageParameter(HttpServletRequest request) {
    String pageParam = request.getParameter("page");

    if (pageParam == null || pageParam.trim().isEmpty()) {
      return 1;
    }

    try {
      int page = Integer.parseInt(pageParam);
      return Math.max(page, 1); // 최소값 1
    } catch (NumberFormatException e) {
      logger.warn("잘못된 페이지 번호 형식: {}", pageParam);
      return 1;
    }
  }

  /**
   * 요청 파라미터에서 카테고리 ID를 추출합니다.
   * 파라미터가 없거나 잘못된 형식인 경우 null을 반환합니다.
   *
   * @param request HTTP 요청
   * @return 카테고리 ID (null 가능)
   */
  private Integer getCategoryParameter(HttpServletRequest request) {
    String categoryParam = request.getParameter("category");

    if (categoryParam == null || categoryParam.trim().isEmpty()) {
      return null;
    }

    try {
      return Integer.parseInt(categoryParam);
    } catch (NumberFormatException e) {
      logger.warn("잘못된 카테고리 ID 형식: {}", categoryParam);
      return null;
    }
  }

  /**
   * 요청 파라미터에서 검색 조건을 추출합니다.
   * 파라미터가 없거나 빈 문자열인 경우 null을 반환합니다.
   *
   * @param request HTTP 요청
   * @param paramName 파라미터 이름
   * @return 검색 조건 값 (null 가능)
   */
  private String getSearchParameter(HttpServletRequest request, String paramName) {
    String param = request.getParameter(paramName);

    if (param == null || param.trim().isEmpty()) {
      return null;
    }

    return param.trim();
  }
}
