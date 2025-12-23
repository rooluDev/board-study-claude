package com.example.board.controller;

import com.example.board.constants.BoardConstants;
import com.example.board.dto.FileDTO;
import com.example.board.service.FileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 파일 다운로드 Servlet.
 */
public class FileDownloadServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(FileDownloadServlet.class);
  private final FileService fileService = new FileService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      String fileIdParam = request.getParameter("fileId");
      if (fileIdParam == null || fileIdParam.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 ID가 필요합니다.");
        return;
      }

      Long fileId = Long.parseLong(fileIdParam);
      FileDTO fileDTO = fileService.getFile(fileId);

      if (fileDTO == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
        return;
      }

      // 파일 경로
      String uploadPath = getServletContext().getRealPath(BoardConstants.UPLOAD_PATH);
      String fullPath = uploadPath + File.separator + fileDTO.getFilePath()
          + File.separator + fileDTO.getPhysicalName();

      File file = new File(fullPath);
      if (!file.exists()) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
        return;
      }

      // 응답 헤더 설정
      response.setContentType("application/octet-stream");
      response.setContentLength((int) file.length());

      String encodedFileName = URLEncoder.encode(fileDTO.getOriginalName(), "UTF-8")
          .replaceAll("\\+", "%20");
      response.setHeader("Content-Disposition",
          "attachment; filename=\"" + encodedFileName + "\"");

      // 파일 전송
      try (FileInputStream fis = new FileInputStream(file);
           OutputStream os = response.getOutputStream()) {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
          os.write(buffer, 0, bytesRead);
        }
        os.flush();
      }

      logger.info("파일 다운로드 성공: fileId={}, fileName={}",
          fileId, fileDTO.getOriginalName());

    } catch (Exception e) {
      logger.error("파일 다운로드 실패", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          BoardConstants.MSG_INTERNAL_SERVER_ERROR);
    }
  }
}