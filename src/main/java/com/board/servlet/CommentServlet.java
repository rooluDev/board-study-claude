package com.board.servlet;

import com.board.dto.Comment;
import com.board.exception.ValidationException;
import com.board.exception.BoardException;
import com.board.service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

/**
 * 댓글 등록 API 서블릿
 *
 * POST /comment: 댓글 등록
 */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
  /** CommentService 인스턴스 */
  private CommentService commentService;

  /** 날짜 포맷 */
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * 서블릿 초기화
   * Service 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    this.commentService = new CommentService();
  }

  /**
   * POST 요청 처리: 댓글 등록
   *
   * 요청 JSON:
   * {
   *   "boardId": 1,
   *   "content": "댓글 내용"
   * }
   *
   * 응답 JSON:
   * {
   *   "success": true/false,
   *   "message": "메시지",
   *   "comment": {
   *     "commentId": 1,
   *     "boardId": 1,
   *     "comment": "댓글 내용",
   *     "createdAt": "2025-12-26 12:00:00"
   *   }
   * }
   *
   * @param request HttpServletRequest 객체
   * @param response HttpServletResponse 객체
   * @throws ServletException 서블릿 오류 시
   * @throws IOException 입출력 오류 시
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // 응답 설정
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try {
      // JSON 요청 파싱
      StringBuilder jsonBuilder = new StringBuilder();
      try (BufferedReader reader = request.getReader()) {
        String line;
        while ((line = reader.readLine()) != null) {
          jsonBuilder.append(line);
        }
      }

      String jsonString = jsonBuilder.toString();

      // 간단한 JSON 파싱 (외부 라이브러리 없이)
      Long boardId = extractLongFromJson(jsonString, "boardId");
      String content = extractStringFromJson(jsonString, "content");

      if (boardId == null || content == null) {
        sendJsonResponse(response, false, "잘못된 요청입니다.", null);
        return;
      }

      // 댓글 등록
      Long commentId = commentService.createComment(boardId, content);

      // 등록된 댓글 조회
      Comment comment = commentService.getCommentById(commentId);

      // 성공 응답
      sendJsonResponse(response, true, "댓글이 등록되었습니다.", comment);

    } catch (ValidationException e) {
      // 입력값 검증 실패
      sendJsonResponse(response, false, e.getUserMessage(), null);
    } catch (BoardException e) {
      // 댓글 등록 실패
      sendJsonResponse(response, false, e.getUserMessage(), null);
    } catch (Exception e) {
      // 예상치 못한 오류
      e.printStackTrace();
      sendJsonResponse(response, false, "시스템 오류가 발생했습니다.", null);
    }
  }

  /**
   * JSON 응답을 전송합니다.
   *
   * @param response HttpServletResponse 객체
   * @param success 성공 여부
   * @param message 메시지
   * @param comment 댓글 객체 (선택)
   * @throws IOException 입출력 오류 시
   */
  private void sendJsonResponse(HttpServletResponse response, boolean success,
                                 String message, Comment comment) throws IOException {
    try (PrintWriter out = response.getWriter()) {
      StringBuilder json = new StringBuilder();
      json.append("{");
      json.append("\"success\": ").append(success).append(",");
      json.append("\"message\": \"").append(escapeJson(message)).append("\"");

      if (comment != null) {
        json.append(",\"comment\": {");
        json.append("\"commentId\": ").append(comment.getCommentId()).append(",");
        json.append("\"boardId\": ").append(comment.getBoardId()).append(",");
        json.append("\"comment\": \"").append(escapeJson(comment.getComment())).append("\",");
        json.append("\"createdAt\": \"")
            .append(comment.getCreatedAt().format(DATE_FORMATTER)).append("\"");
        json.append("}");
      }

      json.append("}");

      out.print(json.toString());
      out.flush();
    }
  }

  /**
   * JSON 문자열에서 Long 값을 추출합니다.
   *
   * @param json JSON 문자열
   * @param key 키
   * @return 추출된 Long 값
   */
  private Long extractLongFromJson(String json, String key) {
    try {
      String pattern = "\"" + key + "\"\\s*:\\s*(\\d+)";
      java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
      java.util.regex.Matcher m = p.matcher(json);
      if (m.find()) {
        return Long.parseLong(m.group(1));
      }
    } catch (Exception e) {
      // 파싱 실패
    }
    return null;
  }

  /**
   * JSON 문자열에서 String 값을 추출합니다.
   *
   * @param json JSON 문자열
   * @param key 키
   * @return 추출된 String 값
   */
  private String extractStringFromJson(String json, String key) {
    try {
      String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
      java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
      java.util.regex.Matcher m = p.matcher(json);
      if (m.find()) {
        return m.group(1);
      }
    } catch (Exception e) {
      // 파싱 실패
    }
    return null;
  }

  /**
   * JSON 문자열에서 특수 문자를 이스케이프합니다.
   *
   * @param str 원본 문자열
   * @return 이스케이프된 문자열
   */
  private String escapeJson(String str) {
    if (str == null) {
      return "";
    }
    return str
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t");
  }
}
