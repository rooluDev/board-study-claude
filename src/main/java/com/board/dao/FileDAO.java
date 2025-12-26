package com.board.dao;

import com.board.dto.BoardFile;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * File 테이블에 대한 데이터 접근 객체
 *
 * MyBatis를 사용하여 첨부파일 관련 CRUD 작업을 수행합니다.
 */
public class FileDAO {
  /**
   * 게시글 ID로 첨부파일 목록을 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 목록
   * @throws BoardException 조회 실패 시
   */
  public List<BoardFile> selectFilesByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectList(
          "com.board.dao.FileDAO.selectFilesByBoardId",
          boardId
      );
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select files by board id: " + boardId,
          "첨부파일 목록을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 파일 ID로 첨부파일을 조회합니다.
   *
   * @param fileId 파일 ID
   * @return 첨부파일 정보 (존재하지 않으면 null)
   * @throws BoardException 조회 실패 시
   */
  public BoardFile selectFileById(Long fileId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectOne(
          "com.board.dao.FileDAO.selectFileById",
          fileId
      );
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select file by id: " + fileId,
          "첨부파일을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 첨부파일을 등록합니다.
   *
   * @param file 등록할 첨부파일 정보
   * @return 생성된 파일 ID
   * @throws BoardException 등록 실패 시
   */
  public Long insertFile(BoardFile file) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.insert("com.board.dao.FileDAO.insertFile", file);
      session.commit();
      return file.getFileId();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to insert file: " + file.getOriginalName(),
          "첨부파일을 등록하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 첨부파일을 삭제합니다.
   *
   * @param fileId 삭제할 파일 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteFile(Long fileId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.delete(
          "com.board.dao.FileDAO.deleteFile",
          fileId
      );

      if (affectedRows == 0) {
        throw new BoardException("첨부파일을 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete file: " + fileId,
          "첨부파일을 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글의 모든 첨부파일을 삭제합니다.
   *
   * @param boardId 게시글 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteFilesByBoardId(Long boardId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.delete(
          "com.board.dao.FileDAO.deleteFilesByBoardId",
          boardId
      );
      session.commit();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete files by board id: " + boardId,
          "첨부파일을 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 게시글의 첨부파일 개수를 조회합니다.
   *
   * @param boardId 게시글 ID
   * @return 첨부파일 개수
   * @throws BoardException 조회 실패 시
   */
  public int countFilesByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      Integer count = session.selectOne(
          "com.board.dao.FileDAO.countFilesByBoardId",
          boardId
      );
      return count != null ? count : 0;
    } catch (Exception e) {
      throw new BoardException(
          "Failed to count files by board id: " + boardId,
          "첨부파일 개수를 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 여러 파일 ID로 첨부파일을 삭제합니다.
   *
   * @param fileIds 삭제할 파일 ID 리스트
   * @throws BoardException 삭제 실패 시
   */
  public void deleteFilesByIds(List<Long> fileIds) {
    if (fileIds == null || fileIds.isEmpty()) {
      return;
    }

    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.delete(
          "com.board.dao.FileDAO.deleteFilesByIds",
          fileIds
      );
      session.commit();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete files by ids",
          "첨부파일을 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }
}
