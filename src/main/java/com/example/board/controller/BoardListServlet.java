package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.CategoryDTO;
import com.example.board.dto.SearchCondition;
import com.example.board.service.BoardService;
import com.example.board.util.DateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 목록 Servlet.
 */
public class BoardListServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardListServlet.class);
  private final BoardService boardService = new BoardService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // 검색 조건 생성
      SearchCondition condition = new SearchCondition();

      // 페이지 번호
      String pageParam = request.getParameter("page");
      int page = 1;
      if (pageParam != null && !pageParam.isEmpty()) {
        try {
          page = Integer.parseInt(pageParam);
          if (page < 1) {
            page = 1;
          }
        } catch (NumberFormatException e) {
          page = 1;
        }
      }
      condition.setPage(page);
      condition.setLimit(BoardConstants.DEFAULT_PAGE_SIZE);

      // 카테고리
      String categoryParam = request.getParameter("category");
      if (categoryParam != null && !categoryParam.isEmpty()) {
        try {
          condition.setCategoryId(Long.parseLong(categoryParam));
        } catch (NumberFormatException e) {
          // 무시
        }
      }

      // 날짜 범위
      String fromParam = request.getParameter("from");
      if (fromParam != null && !fromParam.isEmpty()) {
        condition.setFrom(DateUtil.parseDate(fromParam));
      }

      String toParam = request.getParameter("to");
      if (toParam != null && !toParam.isEmpty()) {
        condition.setTo(DateUtil.parseDate(toParam));
      }

      // 검색어
      String keyword = request.getParameter("keyword");
      if (keyword != null && !keyword.trim().isEmpty()) {
        condition.setKeyword(keyword.trim());
      }

      // 게시글 목록 조회
      List<BoardDTO> boardList = boardService.getBoardList(condition);
      int totalCount = boardService.getBoardCount(condition);

      // 페이징 정보 계산
      int totalPages = (int) Math.ceil((double) totalCount / BoardConstants.DEFAULT_PAGE_SIZE);

      // 카테고리 목록 조회
      List<CategoryDTO> categories = boardService.getAllCategories();

      // JSP로 데이터 전달
      request.setAttribute("boardList", boardList);
      request.setAttribute("categories", categories);
      request.setAttribute("currentPage", page);
      request.setAttribute("totalPages", totalPages);
      request.setAttribute("totalCount", totalCount);
      request.setAttribute("condition", condition);

      request.getRequestDispatcher("/WEB-INF/views/board/list.jsp")
          .forward(request, response);

    } catch (Exception e) {
      logger.error("게시글 목록 조회 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }
}