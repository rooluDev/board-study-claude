package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.dto.BoardDTO;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.service.BoardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 상세 조회 Servlet.
 */
public class BoardViewServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardViewServlet.class);
  private final BoardService boardService = new BoardService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // 게시글 ID 파라미터
      String boardIdParam = request.getParameter("boardId");
      if (boardIdParam == null || boardIdParam.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "게시글 ID가 필요합니다.");
        return;
      }

      Long boardId = Long.parseLong(boardIdParam);

      // 조회수 증가
      boardService.incrementViews(boardId);

      // 게시글 조회
      BoardDTO board = boardService.getBoard(boardId);

      // JSP로 데이터 전달
      request.setAttribute("board", board);

      // 검색 조건 유지 (파라미터 그대로 전달)
      request.setAttribute("page", request.getParameter("page"));
      request.setAttribute("category", request.getParameter("category"));
      request.setAttribute("from", request.getParameter("from"));
      request.setAttribute("to", request.getParameter("to"));
      request.setAttribute("keyword", request.getParameter("keyword"));

      request.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
          .forward(request, response);

    } catch (BoardNotFoundException e) {
      logger.warn("게시글을 찾을 수 없음", e);
      response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
    } catch (NumberFormatException e) {
      logger.warn("잘못된 게시글 ID", e);
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, BoardConstants.MSG_BAD_REQUEST);
    } catch (Exception e) {
      logger.error("게시글 조회 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }
}