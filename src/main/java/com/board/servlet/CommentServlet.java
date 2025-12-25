package com.board.servlet;

import com.board.dto.Comment;
import com.board.exception.BoardException;
import com.board.exception.ValidationException;
import com.board.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 댓글 Servlet
 *
 * POST /comment - 댓글 등록 (AJAX)
 */
@WebServlet(name = "CommentServlet", urlPatterns = "/comment")
public class CommentServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(CommentServlet.class);

  /**
   * CommentService 인스턴스
   */
  private CommentService commentService;

  /**
   * Gson 인스턴스 (JSON 파싱 및 생성)
   */
  private Gson gson;

  /**
   * 날짜 포맷터 (YYYY-MM-DD HH:mm:ss)
   */
  private SimpleDateFormat dateFormat;

  /**
   * Servlet 초기화
   * Service 및 Gson 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.commentService = new CommentService();
    this.gson = new Gson();
    this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    logger.info("CommentServlet 초기화 완료");
  }

  /**
   * POST 요청 처리
   * 댓글 등록 (AJAX)
   * - Content-Type: application/json
   * - Request Body: {"boardId": 1, "content": "댓글 내용"}
   * - Response: {"success": true/false, "message": "...", "comment": {...}}
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("댓글 등록 요청 (AJAX)");

    // 응답 설정
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // JSON 응답 객체
    JsonObject jsonResponse = new JsonObject();

    try {
      // 요청 본문 읽기
      StringBuilder requestBody = new StringBuilder();
      try (BufferedReader reader = request.getReader()) {
        String line;
        while ((line = reader.readLine()) != null) {
          requestBody.append(line);
        }
      }

      logger.debug("요청 본문: {}", requestBody.toString());

      // JSON 파싱
      JsonObject requestJson = gson.fromJson(requestBody.toString(), JsonObject.class);

      // boardId, content 추출
      Long boardId = requestJson.get("boardId").getAsLong();
      String content = requestJson.get("content").getAsString();

      logger.debug("댓글 등록 시도: boardId={}, content={}", boardId, content);

      // Comment DTO 생성
      Comment comment = new Comment();
      comment.setBoardId(boardId);
      comment.setComment(content);

      // 댓글 등록
      Comment savedComment = commentService.createComment(comment);

      // 성공 응답
      jsonResponse.addProperty("success", true);
      jsonResponse.addProperty("message", "댓글이 성공적으로 등록되었습니다.");

      // 등록된 댓글 정보 추가
      JsonObject commentJson = new JsonObject();
      commentJson.addProperty("commentId", savedComment.getCommentId());
      commentJson.addProperty("boardId", savedComment.getBoardId());
      commentJson.addProperty("comment", savedComment.getComment());
      commentJson.addProperty("createdAt", dateFormat.format(savedComment.getCreatedAt()));

      jsonResponse.add("comment", commentJson);

      logger.info("댓글 등록 성공: commentId={}, boardId={}",
          savedComment.getCommentId(), savedComment.getBoardId());

    } catch (ValidationException e) {
      // 입력값 검증 실패
      logger.warn("입력값 검증 실패: {}", e.getMessage());

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", e.getMessage());

    } catch (BoardException e) {
      // 댓글 등록 실패
      logger.error("댓글 등록 실패: {}", e.getMessage());

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", e.getMessage());

    } catch (Exception e) {
      // 예상치 못한 오류
      logger.error("댓글 등록 중 예상치 못한 오류 발생: {}", e.getMessage(), e);

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", "댓글 등록 중 오류가 발생했습니다.");
    }

    // JSON 응답 전송
    try (PrintWriter out = response.getWriter()) {
      out.print(gson.toJson(jsonResponse));
      out.flush();
    }
  }
}
