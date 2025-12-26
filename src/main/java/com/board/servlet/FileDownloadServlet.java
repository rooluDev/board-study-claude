package com.board.servlet;

import com.board.dto.BoardFile;
import com.board.exception.BoardException;
import com.board.exception.FileUploadException;
import com.board.service.FileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.board.util.Constants.UPLOAD_DIR;

/**
 * 첨부파일 다운로드 서블릿
 *
 * GET /download: 첨부파일 다운로드
 */
@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {
  /** FileService 인스턴스 */
  private FileService fileService;

  /** 버퍼 크기 (8KB) */
  private static final int BUFFER_SIZE = 8192;

  /**
   * 서블릿 초기화
   * Service 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    this.fileService = new FileService();
  }

  /**
   * GET 요청 처리: 첨부파일 다운로드
   *
   * 요청 파라미터:
   * - fileId: 파일 ID (필수)
   *
   * 응답:
   * - Content-Type: application/octet-stream
   * - Content-Disposition: attachment; filename*=UTF-8''인코딩된파일명
   * - 파일 바이너리 데이터
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
      // fileId 파라미터 받기
      String fileIdParam = request.getParameter("fileId");

      if (fileIdParam == null || fileIdParam.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 ID가 필요합니다.");
        return;
      }

      Long fileId;
      try {
        fileId = Long.parseLong(fileIdParam);
      } catch (NumberFormatException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 파일 ID입니다.");
        return;
      }

      // 업로드 디렉토리 경로 가져오기
      String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);

      // 파일 정보 조회 및 물리적 파일 존재 확인
      BoardFile boardFile = fileService.downloadFile(fileId, uploadPath);

      // 물리적 파일 객체 생성
      File physicalFile = new File(uploadPath, boardFile.getPhysicalName());

      // 원본 파일명 UTF-8 인코딩
      String encodedFileName = URLEncoder.encode(boardFile.getOriginalName(), StandardCharsets.UTF_8)
          .replaceAll("\\+", "%20");

      // 응답 헤더 설정
      response.setContentType("application/octet-stream");
      response.setContentLength((int) physicalFile.length());
      response.setHeader("Content-Disposition",
          "attachment; filename*=UTF-8''" + encodedFileName);

      // 파일 스트리밍 (버퍼 사용)
      try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(physicalFile));
           OutputStream os = response.getOutputStream()) {

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = bis.read(buffer)) != -1) {
          os.write(buffer, 0, bytesRead);
        }

        os.flush();
      }

    } catch (FileUploadException e) {
      // 물리적 파일이 존재하지 않는 경우
      response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getUserMessage());
    } catch (BoardException e) {
      // 파일을 찾을 수 없거나 유효하지 않은 경우
      response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getUserMessage());
    } catch (Exception e) {
      // 예상치 못한 오류
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "파일 다운로드 중 오류가 발생했습니다.");
    }
  }
}
