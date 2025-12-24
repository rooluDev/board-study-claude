package com.board.service;

import com.board.dao.FileDAO;
import com.board.dto.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 첨부파일 Service (비즈니스 로직 계층)
 *
 * 첨부파일 관련 비즈니스 로직을 처리
 * DAO를 호출하여 데이터베이스 작업 수행
 */
public class FileService {

  private static final Logger logger = LoggerFactory.getLogger(FileService.class);

  /**
   * FileDAO 인스턴스
   */
  private final FileDAO fileDAO;

  /**
   * 기본 생성자
   * FileDAO 인스턴스를 생성합니다.
   */
  public FileService() {
    this.fileDAO = new FileDAO();
  }

  /**
   * 특정 게시글의 첨부파일 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 목록
   */
  public List<File> getFilesByBoardId(Long boardId) {
    logger.info("첨부파일 목록 조회 요청: boardId={}", boardId);

    // DAO를 통해 첨부파일 목록 조회
    List<File> files = fileDAO.selectFilesByBoardId(boardId);

    logger.info("첨부파일 목록 조회 완료: {} 건", files.size());
    return files;
  }
}
