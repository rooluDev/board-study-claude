package com.example.board.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyBatis 세션 관리 유틸리티.
 */
public class MyBatisUtil {

  private static final Logger logger = LoggerFactory.getLogger(MyBatisUtil.class);
  private static SqlSessionFactory sqlSessionFactory;

  static {
    try {
      String resource = "mybatis/mybatis-config.xml";
      InputStream inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
      logger.info("MyBatis SqlSessionFactory initialized successfully");
    } catch (IOException e) {
      logger.error("Failed to initialize MyBatis SqlSessionFactory", e);
      throw new RuntimeException("Failed to initialize MyBatis", e);
    }
  }

  /**
   * SqlSession 생성.
   *
   * @return SqlSession
   */
  public static SqlSession getSqlSession() {
    return sqlSessionFactory.openSession();
  }

  /**
   * SqlSession 생성 (autoCommit 설정).
   *
   * @param autoCommit 자동 커밋 여부
   * @return SqlSession
   */
  public static SqlSession getSqlSession(boolean autoCommit) {
    return sqlSessionFactory.openSession(autoCommit);
  }

  /**
   * 인스턴스화 방지를 위한 private 생성자.
   */
  private MyBatisUtil() {
    throw new AssertionError("Cannot instantiate utility class");
  }
}
