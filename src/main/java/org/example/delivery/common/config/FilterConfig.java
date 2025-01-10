package org.example.delivery.common.config;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.config.jwt.JwtFilter;
import org.example.delivery.common.config.jwt.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

  private final JwtUtil jwtUtil;

  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilter() {
    FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new JwtFilter(jwtUtil));
    registrationBean.addUrlPatterns("/*");

    return registrationBean;
  }
}
