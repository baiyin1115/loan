package com.zsy.loan.admin.core.util;

import com.zsy.loan.admin.config.properties.LoanProperties;
import com.zsy.loan.bean.vo.SpringContextHolder;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

  /**
   * 获取验证码开关
   *
   * @author stylefeng
   * @Date 2017/5/23 22:34
   */
  public static Boolean getKaptchaOnOff() {
    return SpringContextHolder.getBean(LoanProperties.class).getKaptchaOpen();
  }
}