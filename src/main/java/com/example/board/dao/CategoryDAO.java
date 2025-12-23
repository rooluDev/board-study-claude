package com.example.board.dao;

import com.example.board.dto.CategoryDTO;
import com.example.board.util.MyBatisUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 * 카테고리 DAO.
 */
public class CategoryDAO {

  private static final String NAMESPACE = "com.example.board.dao.CategoryDAO";

  public List<CategoryDTO> selectAll() {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectList(NAMESPACE + ".selectAll");
    }
  }

  public CategoryDTO selectById(Long categoryId) {
    try (SqlSession session = MyBatisUtil.getSqlSession()) {
      return session.selectOne(NAMESPACE + ".selectById", categoryId);
    }
  }
}
