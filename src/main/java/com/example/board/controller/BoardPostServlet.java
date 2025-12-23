package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.CategoryDTO;
import com.example.board.exception.ValidationException;
import com.example.board.service.BoardService;
import com.example.board.service.FileService;
import com.example.board.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 작성 Servlet.
 */
public class BoardPostServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardPostServlet.class);
  private final BoardService boardService = new BoardService();
  private final FileService fileService = new FileService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // 카테고리 목록 조회
      List<CategoryDTO> categories = boardService.getAllCategories();
      request.setAttribute("categories", categories);

      // 검색 조건 유지
      request.setAttribute("page", request.getParameter("page"));
      request.setAttribute("category", request.getParameter("category"));
      request.setAttribute("from", request.getParameter("from"));
      request.setAttribute("to", request.getParameter("to"));
      request.setAttribute("keyword", request.getParameter("keyword"));

      request.getRequestDispatcher("/WEB-INF/views/board/post.jsp")
          .forward(request, response);

    } catch (Exception e) {
      logger.error("게시글 작성 페이지 로드 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // 파라미터 추출
      String categoryIdParam = request.getParameter("categoryId");
      String userName = request.getParameter("writer");
      String password = request.getParameter("password");
      String passwordConfirm = request.getParameter("passwordConfirm");
      String title = request.getParameter("title");
      String content = request.getParameter("content");

      // 입력값 검증
      ValidationUtil.validateCategoryId(
          categoryIdParam != null ? Long.parseLong(categoryIdParam) : null);
      ValidationUtil.validateUserName(userName);
      ValidationUtil.validatePassword(password);
      ValidationUtil.validatePasswordConfirm(password, passwordConfirm);
      ValidationUtil.validateTitle(title);
      ValidationUtil.validateContent(content);

      // BoardDTO 생성
      BoardDTO board = new BoardDTO();
      board.setCategoryId(Long.parseLong(categoryIdParam));
      board.setUserName(userName);
      board.setPassword(password);
      board.setTitle(title);
      board.setContent(content);

      // 게시글 등록
      boardService.createBoard(board);

      // 파일 업로드
      Collection<Part> parts = request.getParts();
      List<Part> fileParts = new ArrayList<>();
      for (Part part : parts) {
        if ("files".equals(part.getName())
            && part.getSubmittedFileName() != null
            && !part.getSubmittedFileName().isEmpty()) {
          fileParts.add(part);
        }
      }

      if (!fileParts.isEmpty()) {
        String uploadPath = getServletContext().getRealPath(BoardConstants.UPLOAD_PATH);
        fileService.uploadFiles(fileParts, board.getBoardId(), uploadPath);
      }

      // 게시글 상세 페이지로 리다이렉트
      response.sendRedirect(request.getContextPath() + "/board/view?boardId=" + board.getBoardId());

    } catch (ValidationException e) {
      logger.warn("게시글 작성 검증 실패", e);
      request.setAttribute("error", e.getMessage());
      doGet(request, response);
    } catch (Exception e) {
      logger.error("게시글 작성 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }
}