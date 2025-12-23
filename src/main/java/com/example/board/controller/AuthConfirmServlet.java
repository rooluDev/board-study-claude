package com.example.board.controller;

import com.example.board.constants.BoardConstants;
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
 * 작성자 확인 (비밀번호 검증) Servlet.
 */
public class AuthConfirmServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(AuthConfirmServlet.class);
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

      // 비밀번호 확인
      boolean isValid = boardService.verifyPassword(boardId, password);

      // 응답
      JsonObject result = new JsonObject();
      result.addProperty("success", isValid);
      if (isValid) {
        result.addProperty("message", "비밀번호가 확인되었습니다.");
      } else {
        result.addProperty("message", BoardConstants.MSG_UNAUTHORIZED);
      }

      response.getWriter().write(gson.toJson(result));

    } catch (Exception e) {
      logger.error("작성자 확인 실패", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      JsonObject result = new JsonObject();
      result.addProperty("success", false);
      result.addProperty("message", BoardConstants.MSG_INTERNAL_SERVER_ERROR);
      response.getWriter().write(gson.toJson(result));
    }
  }
}