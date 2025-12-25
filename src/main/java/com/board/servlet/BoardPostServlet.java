package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.Category;
import com.board.exception.BoardException;
import com.board.exception.FileUploadException;
import com.board.exception.ValidationException;
import com.board.service.BoardService;
import com.board.service.CategoryService;
import com.board.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
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
 * 게시글 등록 Servlet
 *
 * GET /board/post - 게시글 등록 폼 제공
 * POST /board/post - 게시글 등록 처리
 */
@WebServlet(name = "BoardPostServlet", urlPatterns = "/board/post")
@MultipartConfig(
    maxFileSize = Constants.MAX_FILE_SIZE,           // 파일당 최대 2MB
    maxRequestSize = Constants.MAX_FILE_SIZE * 3,    // 전체 요청 최대 6MB
    fileSizeThreshold = 1024 * 1024                  // 1MB 이상이면 디스크에 임시 저장
)
public class BoardPostServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardPostServlet.class);

  /**
   * BoardService 인스턴스
   */
  private BoardService boardService;

  /**
   * CategoryService 인스턴스
   */
  private CategoryService categoryService;

  /**
   * Servlet 초기화
   * Service 인스턴스들을 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.boardService = new BoardService();
    this.categoryService = new CategoryService();
    logger.info("BoardPostServlet 초기화 완료");
  }

  /**
   * GET 요청 처리
   * 게시글 등록 폼을 제공합니다.
   * 카테고리 목록을 조회하여 드롭다운에 표시
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 등록 폼 요청");

    try {
      // 카테고리 목록 조회
      List<Category> categories = categoryService.getAllCategories();

      // request attribute 설정
      request.setAttribute("categories", categories);

      logger.info("게시글 등록 폼 제공: 카테고리 {} 건", categories.size());

      // post.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/post.jsp").forward(request, response);

    } catch (Exception e) {
      logger.error("게시글 등록 폼 제공 중 오류 발생: {}", e.getMessage(), e);

      // 오류 메시지 설정
      request.setAttribute("errorMessage", "게시글 등록 폼을 불러오는 중 오류가 발생했습니다.");

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
    }
  }

  /**
   * POST 요청 처리
   * 게시글 등록을 처리합니다.
   * - multipart/form-data 파싱
   * - 입력값 검증 (프론트엔드에서도 검증하지만 백엔드에서도 필수)
   * - 게시글 및 첨부파일 등록
   * - 성공 시 상세 페이지로 redirect
   * - 실패 시 오류 메시지와 함께 폼으로 돌아감
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 등록 요청");

    // 한글 인코딩 설정
    request.setCharacterEncoding("UTF-8");

    try {
      // 폼 데이터 추출
      Long categoryId = extractLongParameter(request, "categoryId");
      String title = extractStringParameter(request, "title");
      String content = extractStringParameter(request, "content");
      String userName = extractStringParameter(request, "userName");
      String password = extractStringParameter(request, "password");

      logger.debug("폼 데이터 추출: categoryId={}, title={}, userName={}",
          categoryId, title, userName);

      // Board DTO 생성
      Board board = new Board();
      board.setCategoryId(categoryId);
      board.setTitle(title);
      board.setContent(content);
      board.setUserName(userName);
      board.setPassword(password);

      // 파일 Part 추출
      List<Part> fileParts = extractFileParts(request);

      logger.debug("파일 Part 추출: {} 건", fileParts.size());

      // 게시글 등록 (검증 + DB 삽입 + 파일 업로드)
      Long boardId = boardService.createBoard(board, fileParts);

      logger.info("게시글 등록 성공: boardId={}", boardId);

      // 상세 페이지로 redirect
      response.sendRedirect(request.getContextPath() + "/board/view?boardId=" + boardId);

    } catch (ValidationException e) {
      logger.warn("게시글 등록 유효성 검증 실패: {}", e.getMessage());
      handlePostError(request, response, e.getMessage(), "입력값 검증 실패");

    } catch (FileUploadException e) {
      logger.error("파일 업로드 실패: {}", e.getMessage(), e);
      handlePostError(request, response, e.getMessage(), "파일 업로드 실패");

    } catch (BoardException e) {
      logger.error("게시글 등록 실패: {}", e.getMessage(), e);
      handlePostError(request, response, e.getMessage(), "게시글 등록 실패");

    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
      handlePostError(request, response, "게시글 등록 중 오류가 발생했습니다.", "시스템 오류");
    }
  }

  /**
   * 요청에서 문자열 파라미터를 추출합니다.
   *
   * @param request HTTP 요청
   * @param paramName 파라미터 이름
   * @return 파라미터 값 (trim 적용)
   */
  private String extractStringParameter(HttpServletRequest request, String paramName) {
    String value = request.getParameter(paramName);
    return (value != null) ? value.trim() : "";
  }

  /**
   * 요청에서 Long 타입 파라미터를 추출합니다.
   *
   * @param request HTTP 요청
   * @param paramName 파라미터 이름
   * @return Long 값
   * @throws ValidationException 파라미터가 없거나 형식이 잘못된 경우
   */
  private Long extractLongParameter(HttpServletRequest request, String paramName) {
    String value = request.getParameter(paramName);

    if (value == null || value.trim().isEmpty()) {
      throw new ValidationException(paramName + " 값이 필요합니다.");
    }

    try {
      return Long.parseLong(value.trim());
    } catch (NumberFormatException e) {
      throw new ValidationException(paramName + " 형식이 올바르지 않습니다.");
    }
  }

  /**
   * 요청에서 파일 Part 목록을 추출합니다.
   * 파일 input의 name이 "files"인 모든 Part를 추출
   *
   * @param request HTTP 요청
   * @return 파일 Part 목록
   * @throws ServletException Part 추출 실패 시
   * @throws IOException I/O 오류 시
   */
  private List<Part> extractFileParts(HttpServletRequest request)
      throws ServletException, IOException {
    List<Part> fileParts = new ArrayList<>();

    // "files"라는 이름의 모든 Part 추출
    Collection<Part> parts = request.getParts();
    for (Part part : parts) {
      if ("files".equals(part.getName())) {
        fileParts.add(part);
      }
    }

    return fileParts;
  }

  /**
   * POST 요청 처리 중 오류 발생 시 처리
   * 오류 메시지와 함께 등록 폼으로 다시 forward
   * 기존 입력값을 유지하여 사용자 편의성 제공
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @param errorMessage 사용자에게 표시할 오류 메시지
   * @param logMessage 로그에 기록할 메시지
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  private void handlePostError(HttpServletRequest request, HttpServletResponse response,
      String errorMessage, String logMessage) throws ServletException, IOException {

    logger.warn("게시글 등록 실패: {}", logMessage);

    try {
      // 카테고리 목록 다시 조회 (폼 표시를 위해)
      List<Category> categories = categoryService.getAllCategories();
      request.setAttribute("categories", categories);

      // 오류 메시지 설정
      request.setAttribute("errorMessage", errorMessage);

      // 기존 입력값 유지 (비밀번호는 제외)
      request.setAttribute("categoryId", request.getParameter("categoryId"));
      request.setAttribute("title", request.getParameter("title"));
      request.setAttribute("content", request.getParameter("content"));
      request.setAttribute("userName", request.getParameter("userName"));

      // post.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/post.jsp").forward(request, response);

    } catch (Exception e) {
      logger.error("오류 처리 중 추가 오류 발생: {}", e.getMessage(), e);

      // 최악의 경우 에러 페이지로 redirect
      request.setAttribute("errorMessage", "게시글 등록 중 오류가 발생했습니다.");
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
    }
  }
}
