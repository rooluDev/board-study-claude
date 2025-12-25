package com.board.servlet;

import com.board.exception.AuthenticationException;
import com.board.exception.BoardException;
import com.board.exception.ValidationException;
import com.board.service.BoardService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 삭제 Servlet
 *
 * POST /board/delete - 게시글 삭제 (AJAX)
 */
@WebServlet(name = "BoardDeleteServlet", urlPatterns = "/board/delete")
public class BoardDeleteServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardDeleteServlet.class);

  /**
   * BoardService 인스턴스
   */
  private BoardService boardService;

  /**
   * Gson 인스턴스 (JSON 파싱 및 생성)
   */
  private Gson gson;

  /**
   * Servlet 초기화
   * Service 및 Gson 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.boardService = new BoardService();
    this.gson = new Gson();
    logger.info("BoardDeleteServlet 초기화 완료");
  }

  /**
   * POST 요청 처리
   * 게시글 삭제 (AJAX)
   * - Content-Type: application/json
   * - Request Body: {"boardId": 1, "password": "password123"}
   * - Response: {"success": true/false, "message": "...", "redirectUrl": "/boards"}
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 삭제 요청 (AJAX)");

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

      // boardId, password 추출
      Long boardId = requestJson.get("boardId").getAsLong();
      String password = requestJson.get("password").getAsString();

      logger.debug("게시글 삭제 시도: boardId={}", boardId);

      // 게시글 삭제 (비밀번호 확인 → 댓글 삭제 → 파일 삭제 → 게시글 삭제)
      boardService.deleteBoard(boardId, password);

      // 성공 응답
      jsonResponse.addProperty("success", true);
      jsonResponse.addProperty("message", "게시글이 성공적으로 삭제되었습니다.");
      jsonResponse.addProperty("redirectUrl", "/boards");

      logger.info("게시글 삭제 성공: boardId={}", boardId);

    } catch (AuthenticationException e) {
      // 비밀번호 불일치
      logger.warn("비밀번호 불일치: {}", e.getMessage());

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", e.getMessage());

    } catch (ValidationException e) {
      // 입력값 검증 실패
      logger.warn("입력값 검증 실패: {}", e.getMessage());

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", e.getMessage());

    } catch (BoardException e) {
      // 게시글 삭제 실패
      logger.error("게시글 삭제 실패: {}", e.getMessage());

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", e.getMessage());

    } catch (Exception e) {
      // 예상치 못한 오류
      logger.error("게시글 삭제 중 예상치 못한 오류 발생: {}", e.getMessage(), e);

      jsonResponse.addProperty("success", false);
      jsonResponse.addProperty("message", "게시글 삭제 중 오류가 발생했습니다.");
    }

    // JSON 응답 전송
    try (PrintWriter out = response.getWriter()) {
      out.print(gson.toJson(jsonResponse));
      out.flush();
    }
  }
}
