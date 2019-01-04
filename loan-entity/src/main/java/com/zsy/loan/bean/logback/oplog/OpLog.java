package com.zsy.loan.bean.logback.oplog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求上加入这个注解可以打印请求参数
 *
 * @Author zhangxh
 * @Date 2019-01-04  12:49
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpLog {

  String name() default "";

  String desc() default "";
}