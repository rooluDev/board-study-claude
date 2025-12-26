package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.Category;
import com.board.exception.ValidationException;
import com.board.exception.FileUploadException;
import com.board.exception.BoardException;
import com.board.service.BoardService;
import com.board.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.board.util.Constants.MAX_FILE_SIZE;
import static com.board.util.Constants.UPLOAD_DIR;

/**
 * 게시글 등록 기능을 처리하는 서블릿
 *
 * GET: 게시글 등록 폼 표시
 * POST: 게시글 등록 처리
 */
@WebServlet("/board/post")
@MultipartConfig(
    maxFileSize = MAX_FILE_SIZE,           // 파일당 최대 2MB
    maxRequestSize = MAX_FILE_SIZE * 3     // 전체 요청 최대 6MB
)
public class BoardPostServlet extends HttpServlet {
  /** BoardService 인스턴스 */
  private BoardService boardService;
  /** CategoryService 인스턴스 */
  private CategoryService categoryService;

  /**
   * 서블릿 초기화
   * Service 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    this.boardService = new BoardService();
    this.categoryService = new CategoryService();
  }

  /**
   * GET 요청 처리: 게시글 등록 폼 표시
   *
   * @param request HttpServletRequest 객체
   * @param response HttpServletResponse 객체
   * @throws ServletException 서블릿 오류 시
   * @throws IOException 입출력 오류 시
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // 카테고리 목록 조회
      List<Category> categoryList = categoryService.getAllCategories();

      // JSP로 전달
      request.setAttribute("categoryList", categoryList);
      request.getRequestDispatcher("/WEB-INF/views/post.jsp").forward(request, response);

    } catch (BoardException e) {
      // 조회 실패 시 에러 페이지로 이동
      System.err.println("=== BoardPostServlet.doGet() 카테고리 조회 실패 ===");
      System.err.println("Exception message: " + e.getMessage());
      System.err.println("User message: " + e.getUserMessage());
      if (e.getCause() != null) {
        System.err.println("Root cause: " + e.getCause().getClass().getName());
        System.err.println("Root cause message: " + e.getCause().getMessage());
        e.getCause().printStackTrace();
      }
      e.printStackTrace();

      request.setAttribute("error", e.getUserMessage());
      request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
  }

  /**
   * POST 요청 처리: 게시글 등록
   *
   * @param request HttpServletRequest 객체
   * @param response HttpServletResponse 객체
   * @throws ServletException 서블릿 오류 시
   * @throws IOException 입출력 오류 시
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // 요청 파라미터 추출
      String categoryIdStr = request.getParameter("categoryId");
      String userName = request.getParameter("userName");
      String password = request.getParameter("password");
      String title = request.getParameter("title");
      String content = request.getParameter("content");

      // Board 객체 생성
      Board board = new Board();
      board.setCategoryId(Long.parseLong(categoryIdStr));
      board.setUserName(userName);
      board.setPassword(password);
      board.setTitle(title);
      board.setContent(content);

      // 파일 Part 목록 가져오기
      Collection<Part> fileParts = request.getParts();

      // 업로드 디렉토리 경로 가져오기
      String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);

      // 게시글 등록 (파일 업로드 포함)
      Long boardId = boardService.createBoard(board, fileParts, uploadPath);

      // 성공 시 상세 페이지로 리다이렉트
      response.sendRedirect(request.getContextPath() + "/board/view?boardId=" + boardId);

    } catch (ValidationException e) {
      // 입력값 검증 실패 시 등록 폼으로 돌아가기
      handleError(request, response, e.getUserMessage());
    } catch (FileUploadException e) {
      // 파일 업로드 실패 시 등록 폼으로 돌아가기
      handleError(request, response, e.getUserMessage());
    } catch (NumberFormatException e) {
      // 카테고리 ID 파싱 오류
      handleError(request, response, "유효하지 않은 카테고리입니다.");
    } catch (BoardException e) {
      // 게시글 등록 실패 시 등록 폼으로 돌아가기
      handleError(request, response, e.getUserMessage());
    } catch (Exception e) {
      // 예상치 못한 오류
      e.printStackTrace();
      handleError(request, response, "시스템 오류가 발생했습니다.");
    }
  }

  /**
   * 에러 처리: 에러 메시지와 함께 등록 폼으로 돌아갑니다.
   *
   * @param request HttpServletRequest 객체
   * @param response HttpServletResponse 객체
   * @param errorMessage 에러 메시지
   * @throws ServletException 서블릿 오류 시
   * @throws IOException 입출력 오류 시
   */
  private void handleError(HttpServletRequest request, HttpServletResponse response,
                           String errorMessage) throws ServletException, IOException {
    try {
      // 카테고리 목록 다시 조회
      List<Category> categoryList = categoryService.getAllCategories();

      // 에러 메시지와 카테고리 목록 전달
      request.setAttribute("error", errorMessage);
      request.setAttribute("categoryList", categoryList);

      // 입력값 유지를 위해 파라미터 다시 전달
      request.setAttribute("categoryId", request.getParameter("categoryId"));
      request.setAttribute("userName", request.getParameter("userName"));
      request.setAttribute("title", request.getParameter("title"));
      request.setAttribute("content", request.getParameter("content"));

      request.getRequestDispatcher("/WEB-INF/views/post.jsp").forward(request, response);

    } catch (Exception e) {
      // 카테고리 조회도 실패한 경우 에러 페이지로
      request.setAttribute("error", errorMessage);
      request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
  }
}
