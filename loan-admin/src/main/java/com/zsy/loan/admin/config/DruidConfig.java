package com.zsy.loan.admin.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import javax.sql.DataSource;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019/1/4/004.
 */
@Configuration
public class DruidConfig {



//
//  /**
//   * druid数据库连接池监控
//   */
//  @Bean
//  public DruidStatInterceptor druidStatInterceptor() {
//    return new DruidStatInterceptor();
//  }
//
//
//  @Bean
//  public JdkRegexpMethodPointcut druidStatPointcut() {
//    JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
//    String patterns = "com.zsy.loan.dao.*";
//    //可以set多个
//    druidStatPointcut.setPatterns(patterns);
//    return druidStatPointcut;
//  }
//
//  /**
//   * druid数据库连接池监控
//   */
//  @Bean
//  public BeanTypeAutoProxyCreator beanTypeAutoProxyCreator() {
//    BeanTypeAutoProxyCreator beanTypeAutoProxyCreator = new BeanTypeAutoProxyCreator();
//    beanTypeAutoProxyCreator.setTargetBeanType(DruidDataSource.class);
//    beanTypeAutoProxyCreator.setInterceptorNames("druidStatInterceptor");
//    return beanTypeAutoProxyCreator;
//  }
//
//  /**
//   * druid 为druidStatPointcut添加拦截
//   */
//  @Bean
//  public Advisor druidStatAdvisor() {
//    return new DefaultPointcutAdvisor(druidStatPointcut(), druidStatInterceptor());
//  }
}
