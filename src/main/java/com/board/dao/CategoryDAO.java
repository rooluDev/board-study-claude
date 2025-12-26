package com.board.dao;

import com.board.dto.Category;
import com.board.exception.BoardException;
import com.board.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Category 테이블에 대한 데이터 접근 객체
 *
 * MyBatis를 사용하여 카테고리 관련 CRUD 작업을 수행합니다.
 */
public class CategoryDAO {
  /**
   * 전체 카테고리 목록을 조회합니다.
   *
   * @return 카테고리 목록
   * @throws BoardException 조회 실패 시
   */
  public List<Category> selectAllCategories() {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectList("com.board.dao.CategoryDAO.selectAllCategories");
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select all categories",
          "카테고리 목록을 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 카테고리 ID로 카테고리를 조회합니다.
   *
   * @param categoryId 카테고리 ID
   * @return 카테고리 정보 (존재하지 않으면 null)
   * @throws BoardException 조회 실패 시
   */
  public Category selectCategoryById(Long categoryId) {
    try (SqlSession session = MyBatisUtil.openSession()) {
      return session.selectOne(
          "com.board.dao.CategoryDAO.selectCategoryById",
          categoryId
      );
    } catch (Exception e) {
      throw new BoardException(
          "Failed to select category by id: " + categoryId,
          "카테고리를 조회하는데 실패했습니다.",
          e
      );
    }
  }

  /**
   * 카테고리를 등록합니다.
   *
   * @param category 등록할 카테고리 정보
   * @return 생성된 카테고리 ID
   * @throws BoardException 등록 실패 시
   */
  public Long insertCategory(Category category) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      session.insert("com.board.dao.CategoryDAO.insertCategory", category);
      session.commit();
      return category.getCategoryId();
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to insert category: " + category.getCategoryName(),
          "카테고리를 등록하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 카테고리를 수정합니다.
   *
   * @param category 수정할 카테고리 정보 (categoryId, categoryName 필수)
   * @throws BoardException 수정 실패 시
   */
  public void updateCategory(Category category) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.update(
          "com.board.dao.CategoryDAO.updateCategory",
          category
      );

      if (affectedRows == 0) {
        throw new BoardException("카테고리를 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to update category: " + category.getCategoryId(),
          "카테고리를 수정하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }

  /**
   * 카테고리를 삭제합니다.
   *
   * @param categoryId 삭제할 카테고리 ID
   * @throws BoardException 삭제 실패 시
   */
  public void deleteCategory(Long categoryId) {
    SqlSession session = null;
    try {
      session = MyBatisUtil.openSession();
      int affectedRows = session.delete(
          "com.board.dao.CategoryDAO.deleteCategory",
          categoryId
      );

      if (affectedRows == 0) {
        throw new BoardException("카테고리를 찾을 수 없습니다.");
      }

      session.commit();
    } catch (BoardException e) {
      MyBatisUtil.rollbackAndClose(session);
      throw e;
    } catch (Exception e) {
      MyBatisUtil.rollbackAndClose(session);
      throw new BoardException(
          "Failed to delete category: " + categoryId,
          "카테고리를 삭제하는데 실패했습니다.",
          e
      );
    } finally {
      MyBatisUtil.closeSession(session);
    }
  }
}
