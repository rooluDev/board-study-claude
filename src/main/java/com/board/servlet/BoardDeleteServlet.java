package com.board.servlet;

import com.board.exception.AuthenticationException;
import com.board.exception.ValidationException;
import com.board.exception.BoardException;
import com.board.service.BoardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.board.util.Constants.UPLOAD_DIR;

/**
 * 게시글 삭제 API 서블릿
 *
 * POST /board/delete: 게시글 삭제
 */
@WebServlet("/board/delete")
public class BoardDeleteServlet extends HttpServlet {
  /** BoardService 인스턴스 */
  private BoardService boardService;

  /**
   * 서블릿 초기화
   * Service 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    this.boardService = new BoardService();
  }

  /**
   * POST 요청 처리: 게시글 삭제
   *
   * 요청 JSON:
   * {
   *   "boardId": 1,
   *   "password": "password123"
   * }
   *
   * 응답 JSON:
   * {
   *   "success": true/false,
   *   "message": "메시지",
   *   "redirectUrl": "/boards"
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
      String password = extractStringFromJson(jsonString, "password");

      if (boardId == null || password == null) {
        sendJsonResponse(response, false, "잘못된 요청입니다.", null);
        return;
      }

      // 업로드 디렉토리 경로 가져오기
      String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);

      // 게시글 삭제
      boardService.deleteBoard(boardId, password, uploadPath);

      // 성공 응답
      String redirectUrl = request.getContextPath() + "/boards";
      sendJsonResponse(response, true, "게시글이 삭제되었습니다.", redirectUrl);

    } catch (AuthenticationException e) {
      // 비밀번호 불일치
      sendJsonResponse(response, false, e.getUserMessage(), null);
    } catch (ValidationException e) {
      // 입력값 검증 실패
      sendJsonResponse(response, false, e.getUserMessage(), null);
    } catch (BoardException e) {
      // 게시글 삭제 실패
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
   * @param redirectUrl 리다이렉트 URL (선택)
   * @throws IOException 입출력 오류 시
   */
  private void sendJsonResponse(HttpServletResponse response, boolean success,
                                 String message, String redirectUrl) throws IOException {
    try (PrintWriter out = response.getWriter()) {
      StringBuilder json = new StringBuilder();
      json.append("{");
      json.append("\"success\": ").append(success).append(",");
      json.append("\"message\": \"").append(escapeJson(message)).append("\"");
      if (redirectUrl != null) {
        json.append(",\"redirectUrl\": \"").append(escapeJson(redirectUrl)).append("\"");
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
