//package com.zsy.loan.admin.reload;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
///**
// * copy朋友的项目，热加载的原理还是不知道  --貌似开发环境里面idea默认就支持了
// *
// * @Author zhangxh
// * @Date 2019-01-21  11:21
// */
//@Configuration
//public class OnlineReloadConfiguration implements BeanFactoryAware, EnvironmentAware {
//
//  private ConfigurableListableBeanFactory beanFactory;
//
//  private Environment environment;
//
//  @Override
//  public void setEnvironment(Environment environment) {
//    this.environment = environment;
//    System.out.println(this.environment.getActiveProfiles());
//  }
//
//  @Override
//  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//    this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
//  }
//
////	@Bean
////	public OnlineReloadEndpoint onlineReloadEndpoint() {
////		return new OnlineReloadEndpoint();
////	}
//
//}
