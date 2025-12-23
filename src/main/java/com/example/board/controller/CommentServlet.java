package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.dto.CommentDTO;
import com.example.board.exception.ValidationException;
import com.example.board.service.CommentService;
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
 * 댓글 Servlet.
 */
public class CommentServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(CommentServlet.class);
  private final CommentService commentService = new CommentService();
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
      String content = json.get("content").getAsString();

      // CommentDTO 생성
      CommentDTO comment = new CommentDTO();
      comment.setBoardId(boardId);
      comment.setComment(content);

      // 댓글 등록
      commentService.createComment(comment);

      // 성공 응답
      JsonObject result = new JsonObject();
      result.addProperty("success", true);
      result.addProperty("message", "댓글이 등록되었습니다.");
      result.addProperty("commentId", comment.getCommentId());
      response.getWriter().write(gson.toJson(result));

    } catch (ValidationException e) {
      logger.warn("댓글 등록 검증 실패", e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      JsonObject result = new JsonObject();
      result.addProperty("success", false);
      result.addProperty("message", e.getMessage());
      response.getWriter().write(gson.toJson(result));
    } catch (Exception e) {
      logger.error("댓글 등록 실패", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      JsonObject result = new JsonObject();
      result.addProperty("success", false);
      result.addProperty("message", BoardConstants.MSG_INTERNAL_SERVER_ERROR);
      response.getWriter().write(gson.toJson(result));
    }
  }
}