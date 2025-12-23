package com.example.board.dao;

import com.example.board.dto.CommentDTO;
import com.example.board.util.MyBatisUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 * 댓글 DAO.
 */
public class CommentDAO {

  private static final String NAMESPACE = "com.example.board.dao.CommentDAO";

  public List<CommentDTO> selectByBoardId(Long boardId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectList(NAMESPACE + ".selectByBoardId", boardId);
    }
  }

  public int insert(CommentDTO comment) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      int result = session.insert(NAMESPACE + ".insert", comment);
      session.commit();
      return result;
    }
  }
}
