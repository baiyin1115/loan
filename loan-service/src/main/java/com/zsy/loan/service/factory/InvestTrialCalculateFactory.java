package com.zsy.loan.service.factory;

import com.zsy.loan.bean.convey.InvestCalculateVo;
import com.zsy.loan.bean.entity.biz.TBizInvestPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestPlanStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
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
 *
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
    /**
     * 登记试算
     */
    maps.put(InvestTypeEnum.HALF_YEAR_SETTLEMENT + "_" + LoanBizTypeEnum.INVEST_CHECK_IN, InvestTrialCalculateFactory::half_year_settlement_check_in);
    maps.put(InvestTypeEnum.EXPIRATION_SETTLEMENT + "_" + LoanBizTypeEnum.INVEST_CHECK_IN,
        InvestTrialCalculateFactory::expiration_settlement_check_in);

    /**
     * 融资试算
     */
    maps.put(InvestTypeEnum.HALF_YEAR_SETTLEMENT + "_" + LoanBizTypeEnum.INVEST, InvestTrialCalculateFactory::half_year_settlement_invest);
    maps.put(InvestTypeEnum.EXPIRATION_SETTLEMENT + "_" + LoanBizTypeEnum.INVEST, InvestTrialCalculateFactory::expiration_settlement_invest);

    /**
     * 撤资试算
     */
    maps.put(InvestTypeEnum.HALF_YEAR_SETTLEMENT + "_" + LoanBizTypeEnum.DIVESTMENT, InvestTrialCalculateFactory::half_year_settlement_divestment);
    maps.put(InvestTypeEnum.EXPIRATION_SETTLEMENT + "_" + LoanBizTypeEnum.DIVESTMENT, InvestTrialCalculateFactory::expiration_settlement_divestment);

    /**
     * 延期试算
     */
    maps.put(InvestTypeEnum.HALF_YEAR_SETTLEMENT + "_" + LoanBizTypeEnum.DELAY, InvestTrialCalculateFactory::half_year_settlement_delay);
    maps.put(InvestTypeEnum.EXPIRATION_SETTLEMENT + "_" + LoanBizTypeEnum.DELAY, InvestTrialCalculateFactory::expiration_settlement_delay);

    /**
     * 结转试算
     */
    maps.put(InvestTypeEnum.HALF_YEAR_SETTLEMENT + "_" + LoanBizTypeEnum.SETTLEMENT, InvestTrialCalculateFactory::half_year_settlement_settlement);
    maps.put(InvestTypeEnum.EXPIRATION_SETTLEMENT + "_" + LoanBizTypeEnum.SETTLEMENT, InvestTrialCalculateFactory::expiration_settlement_settlement);

  }

  /**
   * 半年结转--登记试算
   */
  private static InvestCalculateVo half_year_settlement_check_in(InvestCalculateVo data) {

    log.info("半年结转--登记试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    if (data.getDdDate() == null) {
      result.setDdDate(30L); //默认为30号
    }
    if (data.getCycleInterval() == null) {
      result.setCycleInterval(30L); //周期为30天
    }

    // 期数
    result.setTermNo((long) result.getMonth());

    /**
     * 应收利息
     * 计算规则：登记日到截止日之间包含的月数是期数，如果开始日期不是月初、计算首期利息时按日息计算；如果截止日期不是月末，计算末期利息按照日计算
     */
    BigDecimal totSchdInterest = BigDecimal.valueOf(0.00);
    List<TBizInvestPlan> list = new ArrayList<>(result.getMonth());

    Date begin = data.getBeginDate();
    Date end = data.getEndDate();

    Date concurrent = data.getBeginDate();
    Date concurrentBegin = null;
    Date concurrentEnd = null;
    boolean isMonth = false;
    BigDecimal concurrentInterest = BigDecimal.valueOf(0.00);

    for (int i = 0; i < result.getMonth(); i++) {

      TBizInvestPlan plan = TBizInvestPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      if (i == 0) {
        concurrentBegin = begin;
        concurrentEnd = JodaTimeUtil.getEndDataOfMonth(begin);
      } else if (i == result.getMonth() - 1) {
        concurrentEnd = end;
      } else {
        concurrentEnd = JodaTimeUtil.getEndDataOfMonth(concurrent);
      }

      plan.setTermNo((long) i + 1); //期数

      if (data.getDdDate() != null && data.getDdDate().intValue() != 0) {
        plan.setDdDate(JodaTimeUtil.getDdDate(concurrentBegin, concurrentEnd, data.getDdDate().intValue(), false)); //计息日期
      } else {
        plan.setDdDate(concurrentEnd); //计息日期
      }

      plan.setId(null);
      plan.setInvestNo(data.getId());
      plan.setRate(data.getRate()); //利率
      plan.setBeginDate(concurrentBegin); //本期开始日期
      plan.setEndDate(concurrentEnd); //本期结束日期
      plan.setDdPrin(data.getPrin()); //本期计息本金
      plan.setPaidInterest(BigDecimal.valueOf(0.00)); //本期已提利息
      plan.setStatus(InvestPlanStatusEnum.UN_INTEREST.getValue()); //回款状态

      //按月计息
      if (JodaTimeUtil.isEndDayOfMonth(concurrentBegin) && JodaTimeUtil.isEndDayOfMonth(concurrentEnd)) {
        concurrentInterest = BigDecimalUtil.mul(data.getPrin(), data.getMonthRate());
        totSchdInterest = BigDecimalUtil.add(totSchdInterest, concurrentInterest);

        plan.setChdInterest(concurrentInterest); //本期利息
        plan.setDdNum(30L); //计息天数
      } else {
        int days = JodaTimeUtil.daysBetween(concurrentBegin, concurrentEnd);
        concurrentInterest = BigDecimalUtil.mul(data.getPrin(), data.getDayRate(), BigDecimal.valueOf(days));
        totSchdInterest = BigDecimalUtil.add(totSchdInterest, concurrentInterest);

        plan.setChdInterest(concurrentInterest); //本期利息
        plan.setDdNum((long) days); //计息天数
      }
      list.add(plan);
      concurrentBegin = concurrentEnd; //为下一次循环做准备
      concurrent = JodaTimeUtil.getAfterDayDate(concurrentBegin, 3);
    }
    result.setPlanList(list); //回款计划

    result.setTotSchdInterest(totSchdInterest); // 应收利息累计
    result.setTotAccruedInterest(BigDecimal.valueOf(0.00)); // 计提利息累计

    log.info("半年结转--登记试算--试算结果为：" + result);
    return result;
  }

  /**
   * 半年结转--融资试算
   */
  private static InvestCalculateVo half_year_settlement_invest(InvestCalculateVo data) {

    log.info("半年结转--融资试算--要试算的数据是：" + data);

    InvestCalculateVo result = half_year_settlement_check_in(data);

    log.info("半年结转--融资试算--试算结果为：" + result);
    return result;

  }


  /**
   * 半年结转--撤资试算
   */
  private static InvestCalculateVo half_year_settlement_divestment(InvestCalculateVo data) {
    /**
     * 计算规则：查找当前月，判断当前月是否计提计息，如果计息重新计算利息，可提利息=汇总计提利息-当期利息
     * 可提利息=汇总计提利息+当期利息
     */
    log.info("半年结转--撤资试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    BigDecimal calculateAmt = BigDecimal.valueOf(0.00); //计算撤资本金
    BigDecimal calculateInterest = BigDecimal.valueOf(0.00); //计算撤资利息
    BigDecimal divestmentAmt = data.getDivestmentAmt(); //撤资本金
    BigDecimal divestmentInterest = data.getDivestmentInterest(); //撤资利息
    BigDecimal concurrentInterest = BigDecimal.valueOf(0.00); //本期利息
    BigDecimal divestmentWavAmt = data.getDivestmentWavAmt() == null ? BigDecimal.valueOf(0.00) : data.getDivestmentWavAmt(); //收益调整

    /**
     * 应收利息
     * 计算规则：登记日到截止日之间包含的月数是期数，如果开始日期不是月初、计算首期利息时按日息计算；如果截止日期不是月末，计算末期利息按照日计算
     */
    calculateAmt = BigDecimalUtil.sub(data.getPrin(), data.getTotPaidPrin());
    TBizInvestPlan currentPlan = data.getCurrentPlanList().get(0); //当前期

    Date concurrentBegin = currentPlan.getBeginDate();
    Date concurrentEnd = currentPlan.getEndDate();
    Date acctDate = data.getAcctDate();
    int days = JodaTimeUtil.daysBetween(concurrentBegin, acctDate);
    concurrentInterest = BigDecimalUtil.mul(calculateAmt, currentPlan.getRate(), BigDecimal.valueOf(days));
    if (currentPlan.getStatus().equals(InvestPlanStatusEnum.INTERESTED.getValue())) { //已计息
      calculateInterest = BigDecimalUtil.sub(data.getTotAccruedInterest(), concurrentInterest);
    } else if (currentPlan.getStatus().equals(InvestPlanStatusEnum.UN_INTEREST.getValue())) {
      calculateInterest = BigDecimalUtil.add(data.getTotAccruedInterest(), concurrentInterest);
    } else {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "当前回款计划状态错误");
    }
    result.setCalculateAmt(calculateAmt);
    result.setCalculateInterest(calculateInterest);

    if (divestmentAmt == null && divestmentInterest == null) {
      return result;
    }

    if (divestmentInterest.compareTo(calculateAmt) > 0) {
      throw new LoanException(BizExceptionEnum.INVEST_AMT, "撤资本金不能大于试算撤资本金");
    }

    List<TBizInvestPlan> currentPlans = result.getCurrentPlanList(); //回款计划---未结息回款计划
    List<TBizInvestPlan> notPayRecords = result.getNotPayRecords(); //回款计划---当前期以前的未还回款计划
    List<TBizInvestPlan> afterPayRecords = result.getAfterPayRecords(); //回款计划---当前期以后的回款计划

    if (divestmentInterest.compareTo(calculateAmt) == 0) { //全额撤资

      for (TBizInvestPlan plan : currentPlans) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(concurrentInterest); //本期已提利息
      }

      for (TBizInvestPlan plan : notPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(plan.getChdInterest()); //本期已提利息
      }

      for (TBizInvestPlan plan : afterPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.END.getValue()); //已终止
      }

      result.setTotPaidPrin(BigDecimalUtil.add(data.getTotPaidPrin(), calculateAmt)); //已提本金累计
      result.setTotPaidInterest(BigDecimalUtil.add(data.getTotPaidInterest(), calculateInterest)); //已提利息累计
      result.setTotWavAmt(BigDecimalUtil.add(data.getTotWavAmt(), divestmentWavAmt));
      result.setTotAccruedInterest(BigDecimal.valueOf(0.00)); //计提利息--提出后重新计算

    } else { //部分撤资

      /**
       * 根据待还本金，设置当前期和后续期的回款计划
       * 更新凭证的已提本金累计、已提利息累计、应收利息累计
       */
      BigDecimal remainingPrin = BigDecimalUtil.sub(calculateAmt, divestmentAmt); // 剩余本金
      BigDecimal difference = BigDecimal.valueOf(0.00); // 存下差额--利息
      divestmentInterest = BigDecimal.valueOf(0.00); //撤资利息 --需要重新计算

      for (TBizInvestPlan plan : currentPlans) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(concurrentInterest); //本期已提利息
        divestmentInterest = BigDecimalUtil.add(divestmentInterest, concurrentInterest);
      }

      //新的当前期
      TBizInvestPlan newCurrentPlan = TBizInvestPlan.builder().build();
      BeanKit.copyProperties(currentPlans.get(0), newCurrentPlan);

      newCurrentPlan.setDdPrin(remainingPrin);
      newCurrentPlan.setBeginDate(data.getAcctDate()); //本期开始日期
      newCurrentPlan.setPaidInterest(BigDecimal.valueOf(0.00)); //本期已提利息
      newCurrentPlan.setStatus(InvestPlanStatusEnum.UN_INTEREST.getValue()); //回款状态
      int newDays = JodaTimeUtil.daysBetween(acctDate, concurrentEnd); //剩余计息天数
      BigDecimal newInterest = BigDecimalUtil.mul(remainingPrin, currentPlan.getRate(), BigDecimal.valueOf(newDays)); //剩余利息
      newCurrentPlan.setEndDate(concurrentEnd);
      newCurrentPlan.setChdInterest(newInterest);
      newCurrentPlan.setDdDate(concurrentEnd);
      newCurrentPlan.setDdNum((long) newDays);
      currentPlans.add(newCurrentPlan);

      for (TBizInvestPlan plan : notPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(plan.getChdInterest()); //本期已提利息
        divestmentInterest = BigDecimalUtil.add(divestmentInterest, plan.getChdInterest());
      }

      BigDecimal dayRate = BigDecimal.valueOf(0.00); //日利息
      BigDecimal monthRate = BigDecimal.valueOf(0.00);//月利息

      for (TBizInvestPlan plan : afterPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setDdPrin(remainingPrin);

        BigDecimal afterInterest = BigDecimal.valueOf(0.00);
        if (plan.getDdNum().equals(30L)) {
          monthRate = BigDecimalUtil.div(plan.getRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN);
          afterInterest = BigDecimalUtil.mul(remainingPrin, monthRate); //剩余利息
        } else {
          dayRate = BigDecimalUtil.div(plan.getRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN);
          afterInterest = BigDecimalUtil.mul(remainingPrin, dayRate, BigDecimal.valueOf(plan.getDdNum())); //剩余利息
        }

        difference = BigDecimalUtil.add(difference, BigDecimalUtil.sub(plan.getChdInterest(), afterInterest));
        plan.setChdInterest(afterInterest);
      }

      result.setTotPaidPrin(BigDecimalUtil.add(data.getTotPaidPrin(), divestmentAmt)); //已提本金累计
      result.setTotPaidInterest(BigDecimalUtil.add(data.getTotPaidInterest(), divestmentInterest)); //已提利息累计
      result.setDivestmentInterest(divestmentInterest); //撤资利息 重新计算所得
      result.setTotAccruedInterest(BigDecimal.valueOf(0.00)); //计提利息--提出后重新计算
      result.setTotSchdInterest(BigDecimalUtil.sub(data.getTotSchdInterest(), difference)); //应收利息累计
      result.setTotWavAmt(BigDecimalUtil.add(data.getTotWavAmt(), divestmentWavAmt));
    }

    log.info("半年结转--撤资试算--试算结果为：" + result);
    return result;
  }

  /**
   * 半年结转--延期试算
   */
  private static InvestCalculateVo half_year_settlement_delay(InvestCalculateVo data) {

    log.info("半年结转--延期试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    /**
     * 计算规则：当前展期期数  当前展期利息 计算新的回款计划，更新应收利息累计、更新延期期数、延期利率、结束日期
     */
    Date end = data.getEndDate();
    Long currentExtensionNo = data.getCurrentExtensionNo();
    BigDecimal currentExtensionRate = data.getCurrentExtensionRate();
    Date concurrentBegin = end;
    Date concurrentEnd = null;
    Date concurrent = JodaTimeUtil.getAfterDayDate(end, 3); //下个月

    BigDecimal remainingPrin = BigDecimalUtil.sub(data.getPrin(), data.getTotPaidPrin());
    BigDecimal concurrentInterest = BigDecimal.valueOf(0.00);
    BigDecimal schdInterest = BigDecimal.valueOf(0.00);
    BigDecimal dayRate = BigDecimalUtil.div(currentExtensionRate, BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN); //日利息
    BigDecimal monthRate = BigDecimalUtil.div(currentExtensionRate, BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN);//月利息

    List<TBizInvestPlan> list = new ArrayList<>(currentExtensionNo.intValue());
    for (int i = 0; i < currentExtensionNo.intValue(); i++) {

      TBizInvestPlan plan = TBizInvestPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      plan.setBeginDate(concurrentBegin); //本期开始日期

      //考虑不是月末的情况 延期到月末
      if (i == 0 && !JodaTimeUtil.isEndDayOfMonth(end)) {
        concurrentEnd = JodaTimeUtil.getEndDataOfMonth(concurrentBegin);
        int days = JodaTimeUtil.daysBetween(concurrentBegin, concurrentEnd);
        concurrentInterest = BigDecimalUtil.mul(remainingPrin, dayRate, BigDecimal.valueOf(days));
        plan.setDdNum((long) days); //计息天数
      } else {
        concurrentEnd = JodaTimeUtil.getEndDataOfMonth(concurrent);
        concurrentInterest = BigDecimalUtil.mul(remainingPrin, monthRate); //剩余利息
        plan.setDdNum(30L); //计息天数
      }

      plan.setEndDate(concurrentEnd); //本期结束日期
      plan.setTermNo(data.getTermNo() + data.getExtensionNo() + i + 1l); //期数

      if (data.getDdDate() != null && data.getDdDate().intValue() != 0) {
        plan.setDdDate(JodaTimeUtil.getDdDate(concurrentBegin, concurrentEnd, data.getDdDate().intValue(), false)); //计息日期
      } else {
        plan.setDdDate(concurrentEnd); //计息日期
      }

      plan.setInvestNo(data.getId());
      plan.setId(null);
      plan.setRate(currentExtensionRate); //利率
      plan.setDdPrin(remainingPrin); //本期计息本金
      plan.setPaidInterest(BigDecimal.valueOf(0.00)); //本期已提利息
      plan.setStatus(InvestPlanStatusEnum.UN_INTEREST.getValue()); //回款状态
      schdInterest = BigDecimalUtil.add(schdInterest, concurrentInterest);
      plan.setChdInterest(concurrentInterest); //本期利息

      list.add(plan);
      concurrentBegin = plan.getEndDate();//修改开始日期，为下一次循环准备
      concurrent = JodaTimeUtil.getAfterDayDate(concurrentBegin, 3);//修改开始日期，为下一次循环准备
    }
    result.setPlanList(list); //回款计划

    result.setExtensionNo(result.getExtensionNo() + currentExtensionNo);
    result.setExtensionRate(currentExtensionRate);
    result.setEndDate(concurrentEnd);
    result.setTotSchdInterest(BigDecimalUtil.add(data.getTotSchdInterest(), schdInterest)); // 应收利息累计

    log.info("半年结转--延期试算--试算结果为：" + result);
    return result;

  }

  /**
   * 半年结转--结转试算
   */
  private static InvestCalculateVo half_year_settlement_settlement(InvestCalculateVo data) {

    log.info("半年结转--结转试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    BigDecimal calculateAmt = BigDecimalUtil.sub(data.getPrin(), data.getTotPaidPrin()); //计算结转本金
    BigDecimal calculateInterest = BigDecimal.valueOf(0.00); //计算结转利息

    /**
     * 应收利息
     * 计算规则：根据待还本金、计提利息累计
     */
    for (TBizInvestPlan plan : result.getPlanList()) {
      plan.setAcctDate(data.getAcctDate()); //业务日期
      plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
      plan.setPaidInterest(plan.getChdInterest()); //本期已提利息

      calculateInterest = BigDecimalUtil.add(calculateInterest, plan.getChdInterest());
    }

    for (TBizInvestPlan plan : result.getAfterPayRecords()) {
      plan.setAcctDate(data.getAcctDate()); //业务日期
      plan.setStatus(InvestPlanStatusEnum.END.getValue()); //已终止
    }

    if (calculateInterest.compareTo(data.getTotAccruedInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.INVEST_AMT, "汇总计提金额与未回款计划中的金额累计不一致");
    }

    result.setTotPaidPrin(BigDecimalUtil.add(data.getTotPaidPrin(), calculateAmt)); //已提本金累计
    result.setTotPaidInterest(BigDecimalUtil.add(data.getTotPaidInterest(), calculateInterest)); //已提利息累计
    result.setStatus(InvestStatusEnum.SETTLEMENT.getValue());

    log.info("半年结转--结转试算--试算结果为：" + result);
    return result;
  }

  /**
   * 到期结算--登记试算
   */
  private static InvestCalculateVo expiration_settlement_check_in(InvestCalculateVo data) {

    log.info("到期结算--登记试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    if (data.getDdDate() == null) {
      result.setDdDate(30L); //默认为30号
    }
    if (data.getCycleInterval() == null) {
      result.setCycleInterval(30L); //周期为30天
    }

    // 期数
    result.setTermNo((long) result.getMonth());

    /**
     * 应收利息
     * 计算规则：登记日到截止日之间包含的月数是期数，如果开始日期不是月初、计算首期利息时按日息计算；如果截止日期不是月末，计算末期利息按照日计算
     */
    BigDecimal totSchdInterest = BigDecimal.valueOf(0.00);
    List<TBizInvestPlan> list = new ArrayList<>(result.getMonth());

    Date begin = data.getBeginDate();
    Date end = data.getEndDate();

    Date concurrentBegin = data.getBeginDate();
    Date concurrentEnd = null;
    BigDecimal concurrentInterest = BigDecimal.valueOf(0.00);

    for (int i = 0; i < result.getMonth(); i++) {

      TBizInvestPlan plan = TBizInvestPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      //最后一期 判断是不是正好满1个月
      if (i == result.getMonth() - 1 && !end.equals(JodaTimeUtil.getAfterDayMonth(concurrentBegin, 1))) {
        concurrentEnd = end;
        int days = JodaTimeUtil.daysBetween(concurrentBegin, concurrentEnd);
        concurrentInterest = BigDecimalUtil.mul(data.getPrin(), data.getDayRate(), BigDecimal.valueOf(days));
        totSchdInterest = BigDecimalUtil.add(totSchdInterest, concurrentInterest);

        plan.setChdInterest(concurrentInterest); //本期利息
        plan.setDdNum((long) days); //计息天数
      } else {
        concurrentEnd = JodaTimeUtil.getAfterDayMonth(concurrentBegin, 1);
        concurrentInterest = BigDecimalUtil.mul(data.getPrin(), data.getMonthRate());
        totSchdInterest = BigDecimalUtil.add(totSchdInterest, concurrentInterest);
        plan.setChdInterest(concurrentInterest); //本期利息
        plan.setDdNum(30L); //计息天数
      }

      plan.setTermNo((long) i + 1); //期数

      if (data.getDdDate() != null && data.getDdDate().intValue() != 0) {
        plan.setDdDate(JodaTimeUtil.getDdDate(concurrentBegin, concurrentEnd, data.getDdDate().intValue(), false)); //计息日期
      } else {
        plan.setDdDate(concurrentEnd); //计息日期
      }

      plan.setId(null);
      plan.setInvestNo(data.getId());
      plan.setRate(data.getRate()); //利率

      plan.setBeginDate(concurrentBegin); //本期开始日期
      plan.setEndDate(concurrentEnd); //本期结束日期
      plan.setDdPrin(data.getPrin()); //本期计息本金
      plan.setPaidInterest(BigDecimal.valueOf(0.00)); //本期已提利息
      plan.setStatus(InvestPlanStatusEnum.UN_INTEREST.getValue()); //回款状态

      list.add(plan);
      concurrentBegin = concurrentEnd; //为下一次循环做真棒
    }
    result.setPlanList(list); //回款计划

    result.setTotSchdInterest(totSchdInterest); // 应收利息累计
    result.setTotAccruedInterest(BigDecimal.valueOf(0.00)); // 计提利息累计

    log.info("到期结算--登记试算--试算结果为：" + result);
    return result;
  }

  /**
   * 到期结算--融资试算
   */
  private static InvestCalculateVo expiration_settlement_invest(InvestCalculateVo data) {
    log.info("到期结算--融资试算--要试算的数据是：" + data);

    InvestCalculateVo result = expiration_settlement_check_in(data);

    log.info("到期结算--融资试算--试算结果为：" + result);
    return result;
  }

  /**
   * 到期结算--撤资试算
   */
  private static InvestCalculateVo expiration_settlement_divestment(InvestCalculateVo data) {
    /**
     * 计算规则：查找当前月，判断当前月是否计提计息，如果计息重新计算利息，可提利息=汇总计提利息-当期利息
     * 可提利息=汇总计提利息+当期利息
     */
    log.info("到期结算--撤资试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    BigDecimal calculateAmt = BigDecimal.valueOf(0.00); //计算撤资本金
    BigDecimal calculateInterest = BigDecimal.valueOf(0.00); //计算撤资利息
    BigDecimal divestmentAmt = data.getDivestmentAmt(); //撤资本金
    BigDecimal divestmentInterest = data.getDivestmentInterest(); //撤资利息
    BigDecimal concurrentInterest = BigDecimal.valueOf(0.00); //本期利息
    BigDecimal divestmentWavAmt = data.getDivestmentWavAmt() == null ? BigDecimal.valueOf(0.00) : data.getDivestmentWavAmt(); //收益调整

    /**
     * 应收利息
     * 计算规则：登记日到截止日之间包含的月数是期数，如果开始日期不是月初、计算首期利息时按日息计算；如果截止日期不是月末，计算末期利息按照日计算
     */
    calculateAmt = BigDecimalUtil.sub(data.getPrin(), data.getTotPaidPrin());
    TBizInvestPlan currentPlan = data.getCurrentPlanList().get(0); //当前期

    Date concurrentBegin = currentPlan.getBeginDate();
    Date concurrentEnd = currentPlan.getEndDate();
    Date acctDate = data.getAcctDate();
    int days = JodaTimeUtil.daysBetween(concurrentBegin, acctDate);
    concurrentInterest = BigDecimalUtil.mul(calculateAmt, currentPlan.getRate(), BigDecimal.valueOf(days));

    if (currentPlan.getStatus().equals(InvestPlanStatusEnum.INTERESTED.getValue())) { //已计息
      calculateInterest = BigDecimalUtil.sub(data.getTotAccruedInterest(), concurrentInterest);
    } else if (currentPlan.getStatus().equals(InvestPlanStatusEnum.UN_INTEREST.getValue())) {
      calculateInterest = BigDecimalUtil.add(data.getTotAccruedInterest(), concurrentInterest);
    } else {
      throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "当前回款计划状态错误");
    }
    result.setCalculateAmt(calculateAmt);
    result.setCalculateInterest(calculateInterest);

    if (divestmentAmt == null && divestmentInterest == null) {
      return result;
    }

    if (divestmentInterest.compareTo(calculateAmt) > 0) {
      throw new LoanException(BizExceptionEnum.INVEST_AMT, "撤资本金不能大于试算撤资本金");
    }

    List<TBizInvestPlan> currentPlans = result.getCurrentPlanList(); //回款计划---未结息回款计划
    List<TBizInvestPlan> notPayRecords = result.getNotPayRecords(); //回款计划---当前期以前的未还回款计划
    List<TBizInvestPlan> afterPayRecords = result.getAfterPayRecords(); //回款计划---当前期以后的回款计划

    if (divestmentInterest.compareTo(calculateAmt) == 0) { //全额撤资

      for (TBizInvestPlan plan : currentPlans) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(concurrentInterest); //本期已提利息
      }

      for (TBizInvestPlan plan : notPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(plan.getChdInterest()); //本期已提利息
      }

      for (TBizInvestPlan plan : afterPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.END.getValue()); //已终止
      }

      result.setTotPaidPrin(BigDecimalUtil.add(data.getTotPaidPrin(), calculateAmt)); //已提本金累计
      result.setTotPaidInterest(BigDecimalUtil.add(data.getTotPaidInterest(), calculateInterest)); //已提利息累计
      result.setTotWavAmt(BigDecimalUtil.add(data.getTotWavAmt(), divestmentWavAmt));
      result.setTotAccruedInterest(BigDecimal.valueOf(0.00)); //计提利息--提出后重新计算

    } else { //部分撤资

      /**
       * 根据待还本金，设置当前期和后续期的回款计划
       * 更新凭证的已提本金累计、已提利息累计、应收利息累计
       */
      BigDecimal remainingPrin = BigDecimalUtil.sub(calculateAmt, divestmentAmt); // 剩余本金
      BigDecimal difference = BigDecimal.valueOf(0.00); // 存下差额--利息
      divestmentInterest = BigDecimal.valueOf(0.00); //撤资利息 --需要重新计算

      for (TBizInvestPlan plan : currentPlans) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(concurrentInterest); //本期已提利息
        divestmentInterest = BigDecimalUtil.add(divestmentInterest, concurrentInterest);
      }

      //新的当前期
      TBizInvestPlan newCurrentPlan = TBizInvestPlan.builder().build();
      BeanKit.copyProperties(currentPlans.get(0), newCurrentPlan);

      newCurrentPlan.setDdPrin(remainingPrin);
      newCurrentPlan.setBeginDate(data.getAcctDate()); //本期开始日期
      newCurrentPlan.setPaidInterest(BigDecimal.valueOf(0.00)); //本期已提利息
      newCurrentPlan.setStatus(InvestPlanStatusEnum.UN_INTEREST.getValue()); //回款状态
      int newDays = JodaTimeUtil.daysBetween(acctDate, concurrentEnd); //剩余计息天数
      BigDecimal newInterest = BigDecimalUtil.mul(remainingPrin, currentPlan.getRate(), BigDecimal.valueOf(newDays)); //剩余利息
      newCurrentPlan.setEndDate(concurrentEnd);
      newCurrentPlan.setChdInterest(newInterest);
      newCurrentPlan.setDdDate(concurrentEnd);
      newCurrentPlan.setDdNum((long) newDays);
      currentPlans.add(newCurrentPlan);

      for (TBizInvestPlan plan : notPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setStatus(InvestPlanStatusEnum.RECEIVING.getValue()); //已结息
        plan.setPaidInterest(plan.getChdInterest()); //本期已提利息
        divestmentInterest = BigDecimalUtil.add(divestmentInterest, plan.getChdInterest());
      }

      BigDecimal dayRate = BigDecimal.valueOf(0.00); //日利息
      BigDecimal monthRate = BigDecimal.valueOf(0.00);//月利息

      for (TBizInvestPlan plan : afterPayRecords) {
        plan.setAcctDate(data.getAcctDate()); //业务日期
        plan.setDdPrin(remainingPrin);

        BigDecimal afterInterest = BigDecimal.valueOf(0.00);
        if (plan.getDdNum().equals(30L)) {
          monthRate = BigDecimalUtil.div(plan.getRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN);
          afterInterest = BigDecimalUtil.mul(remainingPrin, monthRate); //剩余利息
        } else {
          dayRate = BigDecimalUtil.div(plan.getRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN);
          afterInterest = BigDecimalUtil.mul(remainingPrin, dayRate, BigDecimal.valueOf(plan.getDdNum())); //剩余利息
        }

        difference = BigDecimalUtil.add(difference, BigDecimalUtil.sub(plan.getChdInterest(), afterInterest));
        plan.setChdInterest(afterInterest);
      }

      result.setTotPaidPrin(BigDecimalUtil.add(data.getTotPaidPrin(), divestmentAmt)); //已提本金累计
      result.setTotPaidInterest(BigDecimalUtil.add(data.getTotPaidInterest(), divestmentInterest)); //已提利息累计
      result.setDivestmentInterest(divestmentInterest); //撤资利息 重新计算所得
      result.setTotAccruedInterest(BigDecimal.valueOf(0.00)); //计提利息--提出后重新计算
      result.setTotSchdInterest(BigDecimalUtil.sub(data.getTotSchdInterest(), difference)); //应收利息累计
      result.setTotWavAmt(BigDecimalUtil.add(data.getTotWavAmt(), divestmentWavAmt));
    }

    log.info("到期结算--撤资试算--试算结果为：" + result);
    return result;
  }

  /**
   * 到期结算--延期试算
   */
  private static InvestCalculateVo expiration_settlement_delay(InvestCalculateVo data) {

    log.info("到期结算--延期试算--要试算的数据是：" + data);
    InvestCalculateVo result = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(data, result);

    /**
     * 计算规则：当前展期期数  当前展期利息 计算新的回款计划，更新应收利息累计、更新延期期数、延期利率、结束日期
     */
    Date end = data.getEndDate();
    Long currentExtensionNo = data.getCurrentExtensionNo();
    BigDecimal currentExtensionRate = data.getCurrentExtensionRate();
    Date concurrentBegin = end;
    Date concurrentEnd = null;
    BigDecimal remainingPrin = BigDecimalUtil.sub(data.getPrin(), data.getTotPaidPrin());
    BigDecimal concurrentInterest = BigDecimal.valueOf(0.00);
    BigDecimal schdInterest = BigDecimal.valueOf(0.00);
    BigDecimal dayRate = BigDecimalUtil.div(currentExtensionRate, BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN); //日利息
    BigDecimal monthRate = BigDecimalUtil.div(currentExtensionRate, BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN);//月利息

    List<TBizInvestPlan> list = new ArrayList<>(currentExtensionNo.intValue());
    for (int i = 0; i < currentExtensionNo.intValue(); i++) {

      TBizInvestPlan plan = TBizInvestPlan.builder().build();
      BeanKit.copyProperties(data, plan);

      plan.setBeginDate(concurrentBegin); //本期开始日期

      concurrentEnd = JodaTimeUtil.getAfterDayMonth(concurrentBegin, 1);
      plan.setEndDate(concurrentEnd); //本期结束日期
      plan.setTermNo(data.getTermNo() + data.getExtensionNo() + i + 1l); //期数

      if (data.getDdDate() != null && data.getDdDate().intValue() != 0) {
        plan.setDdDate(JodaTimeUtil.getDdDate(concurrentBegin, concurrentEnd, data.getDdDate().intValue(), false)); //计息日期
      } else {
        plan.setDdDate(concurrentEnd); //计息日期
      }

      plan.setInvestNo(data.getId());
      plan.setId(null);
      plan.setRate(currentExtensionRate); //利率
      plan.setDdPrin(remainingPrin); //本期计息本金
      plan.setPaidInterest(BigDecimal.valueOf(0.00)); //本期已提利息
      plan.setStatus(InvestPlanStatusEnum.UN_INTEREST.getValue()); //回款状态
      concurrentInterest = BigDecimalUtil.mul(remainingPrin, monthRate); //剩余利息
      schdInterest = BigDecimalUtil.add(schdInterest, concurrentInterest);
      plan.setChdInterest(concurrentInterest); //本期利息
      plan.setDdNum(30L); //计息天数

      list.add(plan);
      concurrentBegin = plan.getEndDate();//修改开始日期，为下一次循环准备
    }
    result.setPlanList(list); //回款计划

    result.setExtensionNo(result.getExtensionNo() + currentExtensionNo);
    result.setExtensionRate(currentExtensionRate);
    result.setEndDate(concurrentEnd);
    result.setTotSchdInterest(BigDecimalUtil.add(data.getTotSchdInterest(), schdInterest)); // 应收利息累计

    log.info("到期结算--延期试算--试算结果为：" + result);
    return result;
  }

  /**
   * 到期结算--结转试算
   */
  private static InvestCalculateVo expiration_settlement_settlement(InvestCalculateVo data) {
    throw new LoanException(BizExceptionEnum.NOT_FOUND, "没有结转业务");
  }


}
