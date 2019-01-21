//package com.zsy.loan.admin.reload;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Inherited;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import org.springframework.context.annotation.Import;
//
///**
// * copy朋友的项目，热加载的原理还是不知道  --貌似开发环境里面idea默认就支持了
// *
// * @Author zhangxh
// * @Date 2019-01-21  11:20
// */
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Inherited
//@Import(OnlineReloadSelector.class)
//public @interface EnableOnlineReload {
//
//  String[] activeProfile() default {"dev", "qa"};
//
//  String[] enableReloadPackages() default {};
//
//  boolean needAuth() default false;
//
//}