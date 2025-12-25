package com.board.servlet;

import com.board.dto.Board;
import com.board.dto.File;
import com.board.exception.AuthenticationException;
import com.board.exception.BoardException;
import com.board.exception.FileUploadException;
import com.board.exception.ValidationException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 게시글 수정 Servlet
 *
 * GET /board/edit - 게시글 수정 폼 제공
 * POST /board/edit - 게시글 수정 처리
 */
@WebServlet(name = "BoardEditServlet", urlPatterns = "/board/edit")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024,          // 파일당 최대 2MB
    maxRequestSize = 2 * 1024 * 1024 * 3,   // 전체 요청 최대 6MB
    fileSizeThreshold = 1024 * 1024         // 1MB 이상이면 디스크에 임시 저장
)
public class BoardEditServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(BoardEditServlet.class);

  /**
   * BoardService 인스턴스
   */
  private BoardService boardService;

  /**
   * FileService 인스턴스
   */
  private FileService fileService;

  /**
   * Servlet 초기화
   * Service 인스턴스들을 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.boardService = new BoardService();
    this.fileService = new FileService();
    logger.info("BoardEditServlet 초기화 완료");
  }

  /**
   * GET 요청 처리
   * 게시글 수정 폼을 제공합니다.
   * - boardId로 게시글 조회
   * - 첨부파일 목록 조회
   * - 검색 조건 유지
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 수정 폼 요청");

    try {
      // boardId 파라미터 추출 (필수)
      Long boardId = getBoardIdParameter(request);

      logger.debug("요청 boardId: {}", boardId);

      // 게시글 조회 (조회수는 증가하지 않음)
      Board board = boardService.getBoardById(boardId);

      // 첨부파일 목록 조회
      List<File> files = fileService.getFilesByBoardId(boardId);

      // 검색 조건 파라미터 추출 (목록으로 돌아갈 때 사용)
      String page = request.getParameter("page");
      String category = request.getParameter("category");
      String from = request.getParameter("from");
      String to = request.getParameter("to");
      String keyword = request.getParameter("keyword");

      // request attribute 설정
      request.setAttribute("board", board);
      request.setAttribute("files", files);

      // 검색 조건 유지
      request.setAttribute("page", page);
      request.setAttribute("category", category);
      request.setAttribute("from", from);
      request.setAttribute("to", to);
      request.setAttribute("keyword", keyword);

      logger.info("게시글 수정 폼 제공: boardId={}, 첨부파일 {} 건",
          boardId, files.size());

      // edit.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);

    } catch (BoardException e) {
      logger.error("게시글 수정 폼 제공 중 오류 발생: {}", e.getMessage(), e);

      // 오류 메시지 설정
      request.setAttribute("errorMessage", e.getMessage());

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);

    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);

      // 일반적인 오류 메시지 설정
      request.setAttribute("errorMessage", "게시글 수정 폼을 불러오는 중 오류가 발생했습니다.");

      // 에러 페이지로 forward
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
    }
  }

  /**
   * POST 요청 처리
   * 게시글 수정을 처리합니다.
   * - multipart/form-data 파싱
   * - 입력값 검증
   * - 비밀번호 확인
   * - 게시글 및 파일 수정
   * - 성공 시 상세 페이지로 redirect (검색 조건 유지)
   * - 실패 시 오류 메시지와 함께 edit.jsp로 forward
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("게시글 수정 요청");

    // 한글 인코딩 설정
    request.setCharacterEncoding("UTF-8");

    try {
      // 폼 데이터 추출
      Long boardId = extractLongParameter(request, "boardId");
      String title = extractStringParameter(request, "title");
      String content = extractStringParameter(request, "content");
      String password = extractStringParameter(request, "password");

      // 검색 조건 파라미터
      String page = extractStringParameter(request, "page");
      String category = extractStringParameter(request, "category");
      String from = extractStringParameter(request, "from");
      String to = extractStringParameter(request, "to");
      String keyword = extractStringParameter(request, "keyword");

      logger.debug("폼 데이터 추출: boardId={}, title={}", boardId, title);

      // Board DTO 생성
      Board board = new Board();
      board.setBoardId(boardId);
      board.setTitle(title);
      board.setContent(content);
      board.setPassword(password);

      // 파일 Part 추출 (새 파일)
      List<Part> newFiles = extractFileParts(request);

      // 삭제할 파일 ID 목록 추출
      List<Long> deletedFileIds = extractDeletedFileIds(request);

      logger.debug("파일 Part 추출: 새 파일 {} 건, 삭제 파일 {} 건",
          newFiles.size(), deletedFileIds.size());

      // 게시글 수정 (검증 + 비밀번호 확인 + DB 업데이트 + 파일 업데이트)
      boardService.updateBoard(board, newFiles, deletedFileIds);

      logger.info("게시글 수정 성공: boardId={}", boardId);

      // 상세 페이지로 redirect (검색 조건 유지)
      String redirectUrl = buildRedirectUrl(request.getContextPath(), boardId, page, category, from, to, keyword);
      response.sendRedirect(redirectUrl);

    } catch (AuthenticationException e) {
      logger.warn("비밀번호 확인 실패: {}", e.getMessage());
      handlePostError(request, response, e.getMessage(), "비밀번호 불일치");

    } catch (ValidationException e) {
      logger.warn("게시글 수정 유효성 검증 실패: {}", e.getMessage());
      handlePostError(request, response, e.getMessage(), "입력값 검증 실패");

    } catch (FileUploadException e) {
      logger.error("파일 업데이트 실패: {}", e.getMessage(), e);
      handlePostError(request, response, e.getMessage(), "파일 업데이트 실패");

    } catch (BoardException e) {
      logger.error("게시글 수정 실패: {}", e.getMessage(), e);
      handlePostError(request, response, e.getMessage(), "게시글 수정 실패");

    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
      handlePostError(request, response, "게시글 수정 중 오류가 발생했습니다.", "시스템 오류");
    }
  }

  /**
   * 요청 파라미터에서 게시글 ID를 추출합니다.
   *
   * @param request HTTP 요청
   * @return 게시글 ID
   * @throws BoardException boardId 파라미터가 없거나 잘못된 형식인 경우
   */
  private Long getBoardIdParameter(HttpServletRequest request) {
    String boardIdParam = request.getParameter("boardId");

    if (boardIdParam == null || boardIdParam.trim().isEmpty()) {
      throw new BoardException("게시글 ID가 필요합니다.");
    }

    try {
      return Long.parseLong(boardIdParam);
    } catch (NumberFormatException e) {
      logger.warn("잘못된 게시글 ID 형식: {}", boardIdParam);
      throw new BoardException("유효하지 않은 게시글 ID입니다.");
    }
  }

  /**
   * 요청에서 문자열 파라미터를 추출합니다.
   *
   * @param request HTTP 요청
   * @param paramName 파라미터 이름
   * @return 파라미터 값 (trim 적용, null이면 빈 문자열)
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
   * 요청에서 삭제할 파일 ID 목록을 추출합니다.
   * deletedFileIds 파라미터를 배열로 받음
   *
   * @param request HTTP 요청
   * @return 삭제할 파일 ID 목록
   */
  private List<Long> extractDeletedFileIds(HttpServletRequest request) {
    List<Long> deletedFileIds = new ArrayList<>();

    String[] deletedFileIdArray = request.getParameterValues("deletedFileIds");

    if (deletedFileIdArray != null) {
      for (String fileIdStr : deletedFileIdArray) {
        try {
          if (fileIdStr != null && !fileIdStr.trim().isEmpty()) {
            deletedFileIds.add(Long.parseLong(fileIdStr.trim()));
          }
        } catch (NumberFormatException e) {
          logger.warn("잘못된 파일 ID 형식: {}", fileIdStr);
          // 잘못된 형식은 무시하고 계속 진행
        }
      }
    }

    return deletedFileIds;
  }

  /**
   * 리다이렉트 URL을 생성합니다.
   * 검색 조건을 쿼리 문자열로 포함
   *
   * @param contextPath 컨텍스트 경로
   * @param boardId 게시글 ID
   * @param page 페이지 번호
   * @param category 카테고리
   * @param from 시작일
   * @param to 종료일
   * @param keyword 검색어
   * @return 리다이렉트 URL
   */
  private String buildRedirectUrl(String contextPath, Long boardId, String page,
      String category, String from, String to, String keyword) {
    StringBuilder url = new StringBuilder(contextPath);
    url.append("/board/view?boardId=").append(boardId);

    if (page != null && !page.isEmpty()) {
      url.append("&page=").append(page);
    }
    if (category != null && !category.isEmpty()) {
      url.append("&category=").append(category);
    }
    if (from != null && !from.isEmpty()) {
      url.append("&from=").append(from);
    }
    if (to != null && !to.isEmpty()) {
      url.append("&to=").append(to);
    }
    if (keyword != null && !keyword.isEmpty()) {
      url.append("&keyword=").append(keyword);
    }

    return url.toString();
  }

  /**
   * POST 요청 처리 중 오류 발생 시 처리
   * 오류 메시지와 함께 수정 폼으로 다시 forward
   * 기존 입력값과 파일 목록을 유지
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

    logger.warn("게시글 수정 실패: {}", logMessage);

    try {
      // boardId 추출
      Long boardId = extractLongParameter(request, "boardId");

      // 원본 게시글 정보 조회
      Board originalBoard = boardService.getBoardById(boardId);

      // 첨부파일 목록 조회
      List<File> files = fileService.getFilesByBoardId(boardId);

      // 검색 조건 파라미터
      String page = extractStringParameter(request, "page");
      String category = extractStringParameter(request, "category");
      String from = extractStringParameter(request, "from");
      String to = extractStringParameter(request, "to");
      String keyword = extractStringParameter(request, "keyword");

      // request attribute 설정
      request.setAttribute("board", originalBoard);
      request.setAttribute("files", files);
      request.setAttribute("errorMessage", errorMessage);

      // 검색 조건 유지
      request.setAttribute("page", page);
      request.setAttribute("category", category);
      request.setAttribute("from", from);
      request.setAttribute("to", to);
      request.setAttribute("keyword", keyword);

      // 사용자 입력값 유지 (제목, 내용)
      String title = extractStringParameter(request, "title");
      String content = extractStringParameter(request, "content");
      if (!title.isEmpty()) {
        originalBoard.setTitle(title);
      }
      if (!content.isEmpty()) {
        originalBoard.setContent(content);
      }

      // edit.jsp로 forward
      request.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(request, response);

    } catch (Exception e) {
      logger.error("오류 처리 중 추가 오류 발생: {}", e.getMessage(), e);

      // 최악의 경우 에러 페이지로 redirect
      request.setAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다.");
      request.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, response);
    }
  }
}
