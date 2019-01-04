package com.zsy.loan.utils.logback;

public class ThreadLocalUtil {

  public static ThreadLocal<Long> requestTime = new ThreadLocal<>();
}
