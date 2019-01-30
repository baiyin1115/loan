package com.zsy.loan.service.factory;

import com.zsy.loan.bean.enumeration.BizTypeEnum.RepayTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ServiceFeeTypeEnum;
import com.zsy.loan.bean.request.LoanCalculateRequest;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * 根据还款方式、本金、利率、服务费收取方式、服务费比例、开始结束日期计算
 * 利息、服务费、放款金额、期数、应还本金、应还利息、应收服务费
 *
 * @Author zhangxh
 * @Date 2019-01-30  9:59
 */
@Slf4j
public class TrialCalculateFactory {

  //向上取整用Math.ceil(double a)向下取整用Math.floor(double a)

  /**
   * 试算工厂
   */
  public static Map<RepayTypeEnum, Function<LoanCalculateRequest, LoanCalculateRequest>> maps
      = new HashMap<RepayTypeEnum, Function<LoanCalculateRequest, LoanCalculateRequest>>();

  static {
    //这里用到landa表达式，新特性。 其中 Cat，Dog 可以看成 if-else 中的条件
    maps.put(RepayTypeEnum.EQUAL_LOAN, bean -> equalLoan(bean));
    maps.put(RepayTypeEnum.EQUAL_PRINCIPAL, bean -> equalPrincipal(bean));
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL, bean -> bInterestAPrincipal(bean));
    maps.put(RepayTypeEnum.B_INTEREST_A_PRINCIPAL_2, bean -> bInterestAPrincipal2(bean));
    maps.put(RepayTypeEnum.A_DEBT_SERVICE_DUE, bean -> aDebtServiceDue(bean));
  }

  /**
   * 先息后本
   * @param data
   * @return
   */
  private static LoanCalculateRequest bInterestAPrincipal(LoanCalculateRequest data) {

    log.info("要试算的数据是："+data);
    LoanCalculateRequest result =  LoanCalculateRequest.builder().build();
    BeanKit.copyProperties(data,result);

    // 期数
    result.setTermNo((long)result.getMonth());

    // 应收利息 = 本金*月利息*期数
    result.setReceiveInterest(BigDecimalUtil.mul(data.getPrin(),data.getMonthRate(), BigDecimal.valueOf(data.getMonth())));

    // 服务费 = 本金 * 服务费比例
    result.setServiceFee(BigDecimalUtil.mul(data.getPrin(),data.getServiceFeeScale()));

    // 放款金额 首期直接扣减服务费
    if(data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())){
      result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(),result.getServiceFee()));
    }else{
      result.setLendingAmt(data.getPrin());
    }

    // 应还本金
    result.setSchdPrin(data.getPrin());

    // 应还利息
    result.setSchdInterest(result.getReceiveInterest());

    // 应收服务费
    result.setSchdServFee(result.getServiceFee());

    log.info("试算结果为："+result);
    return result;
  }

  /**
   * 4:先息后本[上交息],
   * @param data
   * @return
   */
  private static LoanCalculateRequest bInterestAPrincipal2(LoanCalculateRequest data) {

    return bInterestAPrincipal(data);
  }

  /**
   * 5:一次性还本付息
   * @param data
   * @return
   */
  private static LoanCalculateRequest aDebtServiceDue(LoanCalculateRequest data) {

    log.info("要试算的数据是："+data);
    LoanCalculateRequest result =  LoanCalculateRequest.builder().build();
    BeanKit.copyProperties(data,result);

    // 期数 = 就是一期
    result.setTermNo(1l);

    // 应收利息 = 本金*日利息*天数
    result.setReceiveInterest(BigDecimalUtil.mul(data.getPrin(),data.getDayRate(), BigDecimal.valueOf(data.getDay())));

    // 服务费 = 本金 * 服务费比例
    result.setServiceFee(BigDecimalUtil.mul(data.getPrin(),data.getServiceFeeScale()));

    // 放款金额 首期直接扣减服务费
    if(data.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue())){
      result.setLendingAmt(BigDecimalUtil.sub(data.getPrin(),result.getServiceFee()));
    }else{
      result.setLendingAmt(data.getPrin());
    }

    // 应还本金
    result.setSchdPrin(data.getPrin());

    // 应还利息
    result.setSchdInterest(result.getReceiveInterest());

    // 应收服务费
    result.setSchdServFee(result.getServiceFee());

    log.info("试算结果为："+result);
    return result;

  }

  /**
   * 等额本息
   * @param data
   * @return
   */
  private static LoanCalculateRequest equalLoan(LoanCalculateRequest data) {

    //TODO
    return null;
  }

  /**
   * 等额本金
   * @param data
   * @return
   */
  private static LoanCalculateRequest equalPrincipal(LoanCalculateRequest data) {

    //TODO
    return null;
  }



}
