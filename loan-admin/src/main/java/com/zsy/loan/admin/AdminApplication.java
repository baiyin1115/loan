package com.zsy.loan.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages = {"com.zsy.loan", "springboot.reload.plugin"})
@EntityScan(basePackages = "com.zsy.loan.bean.entity")
@EnableOnlineReload(activeProfile = {"dev"}, needAuth = true)
public class AdminApplication {

  protected final static Logger logger = LoggerFactory.getLogger(AdminApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
    logger.info("AdminApplication is success!");
  }
}
