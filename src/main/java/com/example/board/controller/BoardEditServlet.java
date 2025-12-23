package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.CategoryDTO;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.PasswordMismatchException;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 수정 Servlet.
 */
public class BoardEditServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardEditServlet.class);
  private final BoardService boardService = new BoardService();
  private final FileService fileService = new FileService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      String boardIdParam = request.getParameter("boardId");
      if (boardIdParam == null || boardIdParam.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "게시글 ID가 필요합니다.");
        return;
      }

      Long boardId = Long.parseLong(boardIdParam);
      BoardDTO board = boardService.getBoard(boardId);

      // 카테고리 목록
      List<CategoryDTO> categories = boardService.getAllCategories();

      request.setAttribute("board", board);
      request.setAttribute("categories", categories);

      request.getRequestDispatcher("/WEB-INF/views/board/edit.jsp")
          .forward(request, response);

    } catch (BoardNotFoundException e) {
      logger.warn("게시글을 찾을 수 없음", e);
      response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
    } catch (Exception e) {
      logger.error("게시글 수정 페이지 로드 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      String boardIdParam = request.getParameter("boardId");
      String password = request.getParameter("password");
      String title = request.getParameter("title");
      String content = request.getParameter("content");
      String deletedFileIdsParam = request.getParameter("deletedFileIdList");

      Long boardId = Long.parseLong(boardIdParam);

      // 입력값 검증
      ValidationUtil.validatePassword(password);
      ValidationUtil.validateTitle(title);
      ValidationUtil.validateContent(content);

      // BoardDTO 생성
      BoardDTO board = new BoardDTO();
      board.setBoardId(boardId);
      board.setPassword(password);
      board.setTitle(title);
      board.setContent(content);

      // 게시글 수정
      boardService.updateBoard(board);

      // 삭제할 파일 처리
      if (deletedFileIdsParam != null && !deletedFileIdsParam.isEmpty()) {
        String uploadPath = getServletContext().getRealPath(BoardConstants.UPLOAD_PATH);
        String[] fileIds = deletedFileIdsParam.split(",");
        List<Long> deletedFileIds = new ArrayList<>();
        for (String fileId : fileIds) {
          deletedFileIds.add(Long.parseLong(fileId.trim()));
        }
        fileService.deleteFiles(deletedFileIds, uploadPath);
      }

      // 새 파일 업로드
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
        fileService.uploadFiles(fileParts, boardId, uploadPath);
      }

      // 게시글 상세 페이지로 리다이렉트
      response.sendRedirect(request.getContextPath() + "/board/view?boardId=" + boardId);

    } catch (PasswordMismatchException e) {
      logger.warn("비밀번호 불일치", e);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    } catch (ValidationException e) {
      logger.warn("게시글 수정 검증 실패", e);
      request.setAttribute("error", e.getMessage());
      doGet(request, response);
    } catch (Exception e) {
      logger.error("게시글 수정 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }
}