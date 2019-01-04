package com.zsy.loan.admin;

import com.zsy.loan.admin.config.properties.LoanProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * SpringBoot方式启动类
 *
 * @author stylefeng
 * @Date 2017/5/21 12:06
 */
//@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "com.zsy.loan")
@EntityScan(basePackages = "com.zsy.loan.bean.entity")
@EnableJpaRepositories(basePackages = "com.zsy.loan.dao")
public class AdminApplication extends WebMvcConfigurerAdapter {

  protected final static Logger logger = LoggerFactory.getLogger(AdminApplication.class);

  @Autowired
  LoanProperties loanProperties;

  /**
   * 增加swagger的支持
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (loanProperties.getSwaggerOpen()) {
      registry.addResourceHandler("swagger-ui.html")
          .addResourceLocations("classpath:/META-INF/resources/");
      registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
    logger.info("AdminApplication is success!");
  }
}
