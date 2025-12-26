package com.board.util;

import com.board.exception.BoardException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyBatis SqlSession 생성 및 관리를 담당하는 유틸리티 클래스
 *
 * SqlSessionFactory를 싱글톤으로 관리하며, SqlSession 생성/반환 메서드를 제공합니다.
 */
public final class MyBatisUtil {
  /** MyBatis 설정 파일 경로 */
  private static final String MYBATIS_CONFIG = "mybatis-config.xml";

  /** SqlSessionFactory 싱글톤 인스턴스 */
  private static SqlSessionFactory sqlSessionFactory;

  /**
   * private 생성자로 인스턴스화 방지
   */
  private MyBatisUtil() {
    throw new AssertionError("Cannot instantiate MyBatisUtil class");
  }

  /**
   * SqlSessionFactory 인스턴스를 반환합니다.
   * 최초 호출 시 초기화되며, 이후 호출에서는 동일한 인스턴스를 반환합니다.
   *
   * @return SqlSessionFactory 인스턴스
   * @throws BoardException MyBatis 설정 파일 로드 실패 시
   */
  public static synchronized SqlSessionFactory getSqlSessionFactory() {
    if (sqlSessionFactory == null) {
      try (InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG)) {
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
      } catch (IOException e) {
        throw new BoardException(
            "Failed to load MyBatis configuration: " + MYBATIS_CONFIG,
            "데이터베이스 연결 설정을 불러오는데 실패했습니다.",
            e
        );
      }
    }
    return sqlSessionFactory;
  }

  /**
   * 새로운 SqlSession을 생성하여 반환합니다.
   * autoCommit이 false로 설정됩니다. (수동 커밋)
   *
   * @return SqlSession 인스턴스
   */
  public static SqlSession openSession() {
    return getSqlSessionFactory().openSession();
  }

  /**
   * 새로운 SqlSession을 생성하여 반환합니다.
   *
   * @param autoCommit true이면 자동 커밋, false이면 수동 커밋
   * @return SqlSession 인스턴스
   */
  public static SqlSession openSession(boolean autoCommit) {
    return getSqlSessionFactory().openSession(autoCommit);
  }

  /**
   * SqlSession을 안전하게 닫습니다.
   * null 체크를 수행하므로 null이 전달되어도 오류가 발생하지 않습니다.
   *
   * @param session 닫을 SqlSession (null 허용)
   */
  public static void closeSession(SqlSession session) {
    if (session != null) {
      session.close();
    }
  }

  /**
   * SqlSession을 커밋하고 닫습니다.
   *
   * @param session 커밋 및 닫을 SqlSession (null 허용)
   */
  public static void commitAndClose(SqlSession session) {
    if (session != null) {
      try {
        session.commit();
      } finally {
        session.close();
      }
    }
  }

  /**
   * SqlSession을 롤백하고 닫습니다.
   *
   * @param session 롤백 및 닫을 SqlSession (null 허용)
   */
  public static void rollbackAndClose(SqlSession session) {
    if (session != null) {
      try {
        session.rollback();
      } finally {
        session.close();
      }
    }
  }
}
