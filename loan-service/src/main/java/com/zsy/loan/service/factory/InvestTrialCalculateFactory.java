package com.zsy.loan.service.factory;

import com.zsy.loan.bean.convey.InvestCalculateVo;
import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ServiceFeeTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.JodaTimeUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * 融资试算工厂
 * @Author zhangxh
 * @Date 2019-01-30  9:59
 */
@Slf4j
public class InvestTrialCalculateFactory {

  //向上取整用Math.ceil(double a)向下取整用Math.floor(double a)

  /**
   * 试算工厂
   */
  public static Map<String, Function<InvestCalculateVo, InvestCalculateVo>> maps = new HashMap<>();

  static {
  }

  //todo

}
