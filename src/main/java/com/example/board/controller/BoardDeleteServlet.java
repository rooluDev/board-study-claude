package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.exception.PasswordMismatchException;
import com.example.board.service.BoardService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 삭제 Servlet.
 */
public class BoardDeleteServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardDeleteServlet.class);
  private final BoardService boardService = new BoardService();
  private final Gson gson = new Gson();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try {
      // JSON 요청 파싱
      StringBuilder sb = new StringBuilder();
      try (BufferedReader reader = request.getReader()) {
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
      }

      JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
      Long boardId = json.get("boardId").getAsLong();
      String password = json.get("password").getAsString();

      // 게시글 삭제
      boardService.deleteBoard(boardId, password);

      // 성공 응답
      JsonObject result = new JsonObject();
      result.addProperty("success", true);
      result.addProperty("message", "게시글이 삭제되었습니다.");
      response.getWriter().write(gson.toJson(result));

    } catch (PasswordMismatchException e) {
      logger.warn("비밀번호 불일치", e);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      JsonObject result = new JsonObject();
      result.addProperty("success", false);
      result.addProperty("message", e.getMessage());
      response.getWriter().write(gson.toJson(result));
    } catch (Exception e) {
      logger.error("게시글 삭제 실패", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      JsonObject result = new JsonObject();
      result.addProperty("success", false);
      result.addProperty("message", BoardConstants.MSG_INTERNAL_SERVER_ERROR);
      response.getWriter().write(gson.toJson(result));
    }
  }
}