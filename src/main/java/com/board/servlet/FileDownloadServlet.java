package com.board.servlet;

import com.board.dto.File;
import com.board.exception.FileUploadException;
import com.board.exception.ValidationException;
import com.board.service.FileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 파일 다운로드 Servlet
 *
 * GET /download - 첨부파일 다운로드
 */
@WebServlet(name = "FileDownloadServlet", urlPatterns = "/download")
public class FileDownloadServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(FileDownloadServlet.class);

  /**
   * 파일 스트리밍 버퍼 크기 (8KB)
   */
  private static final int BUFFER_SIZE = 8192;

  /**
   * FileService 인스턴스
   */
  private FileService fileService;

  /**
   * Servlet 초기화
   * FileService 인스턴스를 생성합니다.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.fileService = new FileService();
    logger.info("FileDownloadServlet 초기화 완료");
  }

  /**
   * GET 요청 처리
   * 첨부파일 다운로드
   * - Query Parameter: fileId (필수)
   * - Content-Type: application/octet-stream
   * - Content-Disposition: attachment; filename*=UTF-8''인코딩된파일명
   *
   * @param request HTTP 요청
   * @param response HTTP 응답
   * @throws ServletException Servlet 예외
   * @throws IOException I/O 예외
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.info("파일 다운로드 요청");

    try {
      // fileId 파라미터 추출
      String fileIdParam = request.getParameter("fileId");

      if (fileIdParam == null || fileIdParam.trim().isEmpty()) {
        throw new ValidationException("파일 ID가 필요합니다.");
      }

      Long fileId = Long.parseLong(fileIdParam);
      logger.debug("파일 다운로드 시도: fileId={}", fileId);

      // 파일 정보 조회 및 검증
      File file = fileService.downloadFile(fileId);

      // 한글 파일명 인코딩 (UTF-8)
      String encodedFileName = URLEncoder.encode(file.getOriginalName(), "UTF-8")
          .replaceAll("\\+", "%20");

      // 응답 헤더 설정
      response.setContentType("application/octet-stream");
      response.setContentLengthLong(file.getSize());
      response.setHeader("Content-Disposition",
          "attachment; filename*=UTF-8''" + encodedFileName);

      logger.debug("응답 헤더 설정 완료: originalName={}, encodedName={}, size={}",
          file.getOriginalName(), encodedFileName, file.getSize());

      // 파일 스트리밍 (버퍼 사용)
      try (BufferedInputStream bis = new BufferedInputStream(
          new FileInputStream(file.getFilePath()), BUFFER_SIZE);
          BufferedOutputStream bos = new BufferedOutputStream(
              response.getOutputStream(), BUFFER_SIZE)) {

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = bis.read(buffer)) != -1) {
          bos.write(buffer, 0, bytesRead);
        }

        bos.flush();

        logger.info("파일 다운로드 완료: fileId={}, originalName={}",
            fileId, file.getOriginalName());
      }

    } catch (NumberFormatException e) {
      logger.warn("잘못된 파일 ID 형식: {}", e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파일 ID 형식입니다.");

    } catch (ValidationException e) {
      logger.warn("입력값 검증 실패: {}", e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

    } catch (FileUploadException e) {
      logger.error("파일 다운로드 실패: {}", e.getMessage());
      response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());

    } catch (IOException e) {
      logger.error("파일 스트리밍 중 오류 발생: {}", e.getMessage(), e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "파일 다운로드 중 오류가 발생했습니다.");

    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "파일 다운로드 중 오류가 발생했습니다.");
    }
  }
}
