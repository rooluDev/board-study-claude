package com.board.dao;

import com.board.dto.File;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 첨부파일 DAO (Data Access Object)
 *
 * 첨부파일 관련 데이터베이스 CRUD 작업을 수행
 * MyBatis SqlSession을 사용하여 데이터베이스에 접근
 */
public class FileDAO {

  private static final Logger logger = LoggerFactory.getLogger(FileDAO.class);

  /**
   * 특정 게시글의 첨부파일 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 목록
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public List<File> selectFilesByBoardId(Long boardId) {
    logger.debug("첨부파일 목록 조회: boardId={}", boardId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      List<File> files = session.selectList("com.board.dao.FileDAO.selectFilesByBoardId", boardId);

      logger.info("첨부파일 목록 조회 완료: {} 건", files.size());
      return files;

    } catch (Exception e) {
      logger.error("첨부파일 목록 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("첨부파일 목록을 조회하는 중 오류가 발생했습니다.", e);
    }
  }
}
