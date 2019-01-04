package com.zsy.loan.admin.config.web;

import com.zsy.loan.admin.core.xss.XssFilter;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.zsy.loan.service.web.listener.ConfigListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import java.util.Arrays;
import java.util.Properties;

/**
 * web 配置类
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:03:32
 */
@Configuration
public class WebConfig {

  /**
   * xssFilter注册
   */
  @Bean
  public FilterRegistrationBean xssFilterRegistration() {
    XssFilter xssFilter = new XssFilter();
    xssFilter.setUrlExclusion(Arrays.asList("/notice/update", "/notice/add"));
    FilterRegistrationBean registration = new FilterRegistrationBean(xssFilter);
    registration.addUrlPatterns("/*");
    return registration;
  }

  /**
   * RequestContextListener注册
   */
  @Bean
  public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
    return new ServletListenerRegistrationBean<>(new RequestContextListener());
  }

  /**
   * ConfigListener注册
   */
  @Bean
  public ServletListenerRegistrationBean<ConfigListener> configListenerRegistration() {
    return new ServletListenerRegistrationBean<>(new ConfigListener());
  }

  /**
   * 验证码生成相关
   */
  @Bean
  public DefaultKaptcha kaptcha() {
    Properties properties = new Properties();
    properties.put("kaptcha.border", "no");
    properties.put("kaptcha.border.color", "105,179,90");
    properties.put("kaptcha.textproducer.font.color", "blue");
    properties.put("kaptcha.image.width", "125");
    properties.put("kaptcha.image.height", "45");
    properties.put("kaptcha.textproducer.font.size", "45");
    properties.put("kaptcha.session.key", "code");
    properties.put("kaptcha.textproducer.char.length", "4");
    properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
    Config config = new Config(properties);
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }
}
