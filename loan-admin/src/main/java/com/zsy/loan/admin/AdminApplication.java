package com.zsy.loan.admin;

import com.zsy.loan.admin.config.properties.LoanProperties;
import com.zsy.loan.admin.core.intercept.LogInterceptor;
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
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springboot.reload.plugin.annotation.EnableOnlineReload;

/**
 * SpringBoot方式启动类
 *
 * @author stylefeng
 * @Date 2017/5/21 12:06
 */
@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.zsy.loan","springboot.reload.plugin"})
@EntityScan(basePackages = "com.zsy.loan.bean.entity")
@EnableOnlineReload(activeProfile= {"dev"},needAuth=true)
public class AdminApplication extends WebMvcConfigurerAdapter {

  protected final static Logger logger = LoggerFactory.getLogger(AdminApplication.class);

  @Autowired
  LoanProperties loanProperties;

  @Autowired
  private LogInterceptor logInterceptor; // 实例化拦截器

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

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
  }

  public void addInterceptors(InterceptorRegistry registry) {
    // super.addInterceptors(registry);
    // 注册自定义的拦截器passwordStateInterceptor
    registry.addInterceptor(logInterceptor)
        .addPathPatterns("/**"); //匹配要过滤的路径
  }

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
    logger.info("AdminApplication is success!");
  }
}
