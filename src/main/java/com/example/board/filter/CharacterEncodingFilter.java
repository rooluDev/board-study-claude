package com.example.board.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * 문자 인코딩 필터.
 *
 * <p>모든 요청과 응답에 UTF-8 인코딩을 적용합니다.
 * web.xml에서 설정한 인코딩 값을 사용합니다.
 */
public class CharacterEncodingFilter implements Filter {

  private String encoding;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.encoding = filterConfig.getInitParameter("encoding");
    if (this.encoding == null) {
      this.encoding = "UTF-8";
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    // 요청 인코딩 설정
    request.setCharacterEncoding(encoding);

    // 응답 인코딩 설정
    response.setCharacterEncoding(encoding);

    // 다음 필터 또는 서블릿 실행
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    // 필터 종료 시 정리 작업 (필요시)
  }
}
