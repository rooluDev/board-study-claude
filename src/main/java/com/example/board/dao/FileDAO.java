package com.example.board.dao;

import com.example.board.dto.FileDTO;
import com.example.board.util.MyBatisUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 * 파일 DAO.
 */
public class FileDAO {

  private static final String NAMESPACE = "com.example.board.dao.FileDAO";

  public FileDTO selectById(Long fileId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectOne(NAMESPACE + ".selectById", fileId);
    }
  }

  public List<FileDTO> selectByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectList(NAMESPACE + ".selectByBoardId", boardId);
    }
  }

  public int insert(FileDTO file) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.insert(NAMESPACE + ".insert", file);
      session.commit();
      return result;
    }
  }

  public int deleteById(Long fileId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.delete(NAMESPACE + ".deleteById", fileId);
      session.commit();
      return result;
    }
  }

  public int deleteByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.delete(NAMESPACE + ".deleteByBoardId", boardId);
      session.commit();
      return result;
    }
  }
}
