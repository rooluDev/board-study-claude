package com.board.util;

import com.board.exception.BoardException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyBatis SqlSessionFactory 유틸리티 클래스
 *
 * MyBatis 설정 파일을 읽어 SqlSessionFactory를 생성하고
 * SqlSession을 생성/반환하는 기능을 제공
 */
public final class MyBatisUtil {

  private static final Logger logger = LoggerFactory.getLogger(MyBatisUtil.class);

  /**
   * MyBatis 설정 파일 경로
   */
  private static final String MYBATIS_CONFIG = "mybatis-config.xml";

  /**
   * SqlSessionFactory 싱글톤 인스턴스
   */
  private static SqlSessionFactory sqlSessionFactory;

  /**
   * 생성자를 private으로 선언하여 인스턴스화 방지
   */
  private MyBatisUtil() {
    throw new AssertionError("MyBatisUtil 클래스는 인스턴스화할 수 없습니다.");
  }

  /**
   * SqlSessionFactory를 초기화합니다.
   * 클래스 로딩 시 한 번만 실행됩니다.
   */
  static {
    try {
      InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
      logger.info("SqlSessionFactory 초기화 완료");
    } catch (IOException e) {
      logger.error("SqlSessionFactory 초기화 실패: {}", e.getMessage(), e);
      throw new BoardException("MyBatis 설정 파일을 읽을 수 없습니다.", e);
    }
  }

  /**
   * SqlSessionFactory 인스턴스를 반환합니다.
   *
   * @return SqlSessionFactory 인스턴스
   */
  public static SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  /**
   * SqlSession을 생성하여 반환합니다.
   * autoCommit이 false로 설정되며, 트랜잭션을 수동으로 관리해야 합니다.
   *
   * @return SqlSession 인스턴스
   */
  public static SqlSession openSession() {
    return sqlSessionFactory.openSession();
  }

  /**
   * SqlSession을 생성하여 반환합니다.
   *
   * @param autoCommit true면 자동 커밋, false면 수동 커밋
   * @return SqlSession 인스턴스
   */
  public static SqlSession openSession(boolean autoCommit) {
    return sqlSessionFactory.openSession(autoCommit);
  }

  /**
   * SqlSession을 안전하게 닫습니다.
   * null 체크를 수행하여 NullPointerException을 방지합니다.
   *
   * @param session 닫을 SqlSession
   */
  public static void closeSession(SqlSession session) {
    if (session != null) {
      try {
        session.close();
      } catch (Exception e) {
        logger.error("SqlSession 종료 중 오류 발생: {}", e.getMessage(), e);
      }
    }
  }

  /**
   * SqlSession을 커밋합니다.
   * null 체크를 수행하여 NullPointerException을 방지합니다.
   *
   * @param session 커밋할 SqlSession
   */
  public static void commit(SqlSession session) {
    if (session != null) {
      try {
        session.commit();
        logger.debug("트랜잭션 커밋 완료");
      } catch (Exception e) {
        logger.error("트랜잭션 커밋 중 오류 발생: {}", e.getMessage(), e);
        throw new BoardException("트랜잭션 커밋에 실패했습니다.", e);
      }
    }
  }

  /**
   * SqlSession을 롤백합니다.
   * null 체크를 수행하여 NullPointerException을 방지합니다.
   *
   * @param session 롤백할 SqlSession
   */
  public static void rollback(SqlSession session) {
    if (session != null) {
      try {
        session.rollback();
        logger.debug("트랜잭션 롤백 완료");
      } catch (Exception e) {
        logger.error("트랜잭션 롤백 중 오류 발생: {}", e.getMessage(), e);
      }
    }
  }

  /**
   * SqlSession을 커밋하고 닫습니다.
   *
   * @param session 커밋하고 닫을 SqlSession
   */
  public static void commitAndClose(SqlSession session) {
    if (session != null) {
      try {
        session.commit();
        logger.debug("트랜잭션 커밋 완료");
      } catch (Exception e) {
        logger.error("트랜잭션 커밋 중 오류 발생: {}", e.getMessage(), e);
        rollback(session);
        throw new BoardException("트랜잭션 커밋에 실패했습니다.", e);
      } finally {
        closeSession(session);
      }
    }
  }

  /**
   * SqlSession을 롤백하고 닫습니다.
   *
   * @param session 롤백하고 닫을 SqlSession
   */
  public static void rollbackAndClose(SqlSession session) {
    if (session != null) {
      try {
        session.rollback();
        logger.debug("트랜잭션 롤백 완료");
      } catch (Exception e) {
        logger.error("트랜잭션 롤백 중 오류 발생: {}", e.getMessage(), e);
      } finally {
        closeSession(session);
      }
    }
  }
}
