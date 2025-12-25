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

  /**
   * 단일 첨부파일을 등록합니다.
   * 등록 후 생성된 fileId가 File 객체에 설정됩니다.
   *
   * @param file 등록할 파일 정보
   * @throws BoardException 데이터베이스 삽입 중 오류 발생 시
   */
  public void insertFile(File file) {
    logger.debug("첨부파일 등록: originalName={}, boardId={}",
        file.getOriginalName(), file.getBoardId());

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.insert("com.board.dao.FileDAO.insertFile", file);

      if (affectedRows > 0) {
        session.commit();
        logger.info("첨부파일 등록 완료: fileId={}, originalName={}",
            file.getFileId(), file.getOriginalName());
      } else {
        logger.error("첨부파일 등록 실패: affectedRows=0");
        throw new BoardException("첨부파일 등록에 실패했습니다.");
      }

    } catch (BoardException e) {
      throw e;
    } catch (Exception e) {
      logger.error("첨부파일 등록 실패: {}", e.getMessage(), e);
      throw new BoardException("첨부파일을 등록하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 여러 첨부파일을 배치로 등록합니다.
   * 성능 최적화를 위해 단일 쿼리로 여러 파일을 삽입합니다.
   *
   * @param files 등록할 파일 목록
   * @throws BoardException 데이터베이스 삽입 중 오류 발생 시
   */
  public void insertFiles(List<File> files) {
    if (files == null || files.isEmpty()) {
      logger.debug("등록할 파일이 없음");
      return;
    }

    logger.debug("첨부파일 배치 등록: {} 건", files.size());

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.insert("com.board.dao.FileDAO.insertFiles", files);

      if (affectedRows > 0) {
        session.commit();
        logger.info("첨부파일 배치 등록 완료: {} 건", affectedRows);
      } else {
        logger.error("첨부파일 배치 등록 실패: affectedRows=0");
        throw new BoardException("첨부파일 배치 등록에 실패했습니다.");
      }

    } catch (BoardException e) {
      throw e;
    } catch (Exception e) {
      logger.error("첨부파일 배치 등록 실패: {}", e.getMessage(), e);
      throw new BoardException("첨부파일을 등록하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 파일 ID로 파일 정보를 조회합니다.
   * 삭제 전 물리 파일명 확인에 사용
   *
   * @param fileId 파일 ID
   * @return 파일 정보 (없으면 null)
   * @throws BoardException 데이터베이스 조회 중 오류 발생 시
   */
  public File selectFileById(Long fileId) {
    logger.debug("파일 정보 조회: fileId={}", fileId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      File file = session.selectOne("com.board.dao.FileDAO.selectFileById", fileId);

      if (file != null) {
        logger.info("파일 정보 조회 완료: fileId={}, originalName={}",
            fileId, file.getOriginalName());
      } else {
        logger.warn("파일을 찾을 수 없음: fileId={}", fileId);
      }

      return file;

    } catch (Exception e) {
      logger.error("파일 정보 조회 실패: {}", e.getMessage(), e);
      throw new BoardException("파일 정보를 조회하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 단일 파일을 삭제합니다.
   *
   * @param fileId 파일 ID
   * @throws BoardException 데이터베이스 삭제 중 오류 발생 시
   */
  public void deleteFile(Long fileId) {
    logger.debug("파일 삭제: fileId={}", fileId);

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.delete("com.board.dao.FileDAO.deleteFile", fileId);

      if (affectedRows > 0) {
        session.commit();
        logger.info("파일 삭제 완료: fileId={}", fileId);
      } else {
        logger.warn("파일 삭제 실패 (파일 없음): fileId={}", fileId);
      }

    } catch (Exception e) {
      logger.error("파일 삭제 실패: {}", e.getMessage(), e);
      throw new BoardException("파일을 삭제하는 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 여러 파일을 삭제합니다.
   *
   * @param fileIds 파일 ID 목록
   * @throws BoardException 데이터베이스 삭제 중 오류 발생 시
   */
  public void deleteFilesByIds(List<Long> fileIds) {
    if (fileIds == null || fileIds.isEmpty()) {
      logger.debug("삭제할 파일이 없음");
      return;
    }

    logger.debug("파일 배치 삭제: {} 건", fileIds.size());

    try (SqlSession session = MyBatisUtil.openSession()) {
      int affectedRows = session.delete("com.board.dao.FileDAO.deleteFilesByIds", fileIds);

      if (affectedRows > 0) {
        session.commit();
        logger.info("파일 배치 삭제 완료: {} 건", affectedRows);
      } else {
        logger.warn("파일 배치 삭제 실패: 삭제된 파일이 없음");
      }

    } catch (Exception e) {
      logger.error("파일 배치 삭제 실패: {}", e.getMessage(), e);
      throw new BoardException("파일을 삭제하는 중 오류가 발생했습니다.", e);
    }
  }
}
