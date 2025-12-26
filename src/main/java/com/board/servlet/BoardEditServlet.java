package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.BoardFile;
import com.board.exception.AuthenticationException;
import com.board.exception.FileUploadException;
import com.board.exception.ValidationException;
import com.board.exception.BoardException;
import com.board.service.BoardService;
import com.board.service.FileService;
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

import static com.board.util.Constants.MAX_FILE_SIZE;
import static com.board.util.Constants.UPLOAD_DIR;

/**
 * 게시글 수정 기능을 처리하는 서블릿
 *
 * GET: 게시글 수정 폼 표시
 * POST: 게시글 수정 처리
 */
@WebServlet("/board/edit")
@MultipartConfig(
    maxFileSize = MAX_FILE_SIZE,           // 파일당 최대 2MB
    maxRequestSize = MAX_FILE_SIZE * 3     // 전체 요청 최대 6MB
)
public class BoardEditServlet extends HttpServlet {
  /** BoardService 인스턴스 */
  private BoardService boardService;
  /** FileService 인스턴스 */
  private FileService fileService;

  /**
   * 서블릿 초기화
   * Service 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    this.boardService = new BoardService();
    this.fileService = new FileService();
  }

  /**
   * GET 요청 처리: 게시글 수정 폼 표시
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
      // boardId 파라미터 추출
      String boardIdStr = request.getParameter("boardId");
      if (boardIdStr == null || boardIdStr.isEmpty()) {
        throw new BoardException("게시글 ID가 필요합니다.");
      }

      Long boardId = Long.parseLong(boardIdStr);

      // 게시글 조회
      Board board = boardService.getBoardById(boardId);

      // 첨부파일 목록 조회
      List<BoardFile> fileList = fileService.getFilesByBoardId(boardId);

      // 검색 조건 파라미터 전달
      request.setAttribute("board", board);
      request.setAttribute("fileList", fileList);
      request.setAttribute("page", request.getParameter("page"));
      request.setAttribute("category", request.getParameter("category"));
      request.setAttribute("from", request.getParameter("from"));
      request.setAttribute("to", request.getParameter("to"));
      request.setAttribute("keyword", request.getParameter("keyword"));

      // edit.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);

    } catch (NumberFormatException e) {
      // boardId 파싱 오류
      request.setAttribute("error", "유효하지 않은 게시글입니다.");
      request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    } catch (BoardException e) {
      // 게시글 조회 실패
      request.setAttribute("error", e.getUserMessage());
      request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
  }

  /**
   * POST 요청 처리: 게시글 수정
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
      String boardIdStr = request.getParameter("boardId");
      String password = request.getParameter("password");
      String title = request.getParameter("title");
      String content = request.getParameter("content");

      // 검색 조건 파라미터
      String page = request.getParameter("page");
      String category = request.getParameter("category");
      String from = request.getParameter("from");
      String to = request.getParameter("to");
      String keyword = request.getParameter("keyword");

      // Board 객체 생성
      Long boardId = Long.parseLong(boardIdStr);
      Board board = new Board();
      board.setBoardId(boardId);
      board.setPassword(password);
      board.setTitle(title);
      board.setContent(content);

      // 삭제할 파일 ID 리스트 추출
      List<Long> deletedFileIds = new ArrayList<>();
      String[] deletedFileIdStrs = request.getParameterValues("deletedFileIds");
      if (deletedFileIdStrs != null) {
        for (String fileIdStr : deletedFileIdStrs) {
          if (fileIdStr != null && !fileIdStr.isEmpty()) {
            deletedFileIds.add(Long.parseLong(fileIdStr));
          }
        }
      }

      // 새 파일 Part 목록 가져오기
      Collection<Part> fileParts = request.getParts();

      // 업로드 디렉토리 경로 가져오기
      String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);

      // 게시글 수정 (파일 업데이트 포함)
      boardService.updateBoard(board, fileParts, deletedFileIds, uploadPath);

      // 검색 조건 쿼리 스트링 생성
      StringBuilder searchParams = new StringBuilder();
      if (page != null && !page.isEmpty()) {
        searchParams.append("&page=").append(page);
      }
      if (category != null && !category.isEmpty()) {
        searchParams.append("&category=").append(category);
      }
      if (from != null && !from.isEmpty()) {
        searchParams.append("&from=").append(from);
      }
      if (to != null && !to.isEmpty()) {
        searchParams.append("&to=").append(to);
      }
      if (keyword != null && !keyword.isEmpty()) {
        searchParams.append("&keyword=").append(keyword);
      }

      // 성공 시 상세 페이지로 리다이렉트 (검색 조건 유지)
      response.sendRedirect(
          request.getContextPath() + "/board/view?boardId=" + boardId + searchParams.toString()
      );

    } catch (AuthenticationException e) {
      // 비밀번호 불일치
      handleError(request, response, e.getUserMessage());
    } catch (ValidationException e) {
      // 입력값 검증 실패
      handleError(request, response, e.getUserMessage());
    } catch (FileUploadException e) {
      // 파일 업로드 실패
      handleError(request, response, e.getUserMessage());
    } catch (NumberFormatException e) {
      // boardId 또는 fileId 파싱 오류
      handleError(request, response, "유효하지 않은 요청입니다.");
    } catch (BoardException e) {
      // 게시글 수정 실패
      handleError(request, response, e.getUserMessage());
    } catch (Exception e) {
      // 예상치 못한 오류
      e.printStackTrace();
      handleError(request, response, "시스템 오류가 발생했습니다.");
    }
  }

  /**
   * 에러 처리: 에러 메시지와 함께 수정 폼으로 돌아갑니다.
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
      // boardId 파라미터 추출
      String boardIdStr = request.getParameter("boardId");
      Long boardId = Long.parseLong(boardIdStr);

      // 게시글 조회
      Board board = boardService.getBoardById(boardId);

      // 첨부파일 목록 조회
      List<BoardFile> fileList = fileService.getFilesByBoardId(boardId);

      // 에러 메시지와 데이터 전달
      request.setAttribute("error", errorMessage);
      request.setAttribute("board", board);
      request.setAttribute("fileList", fileList);
      request.setAttribute("page", request.getParameter("page"));
      request.setAttribute("category", request.getParameter("category"));
      request.setAttribute("from", request.getParameter("from"));
      request.setAttribute("to", request.getParameter("to"));
      request.setAttribute("keyword", request.getParameter("keyword"));

      // edit.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);

    } catch (Exception e) {
      // 에러 처리 중 또 다른 오류 발생 시 에러 페이지로
      request.setAttribute("error", errorMessage);
      request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
  }
}
