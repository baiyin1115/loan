package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.InvestCalculateVo;
import com.zsy.loan.bean.convey.InvestConfirmInfoVo;
import com.zsy.loan.bean.convey.InvestDelayInfoVo;
import com.zsy.loan.bean.convey.InvestDivestmentInfoVo;
import com.zsy.loan.bean.convey.InvestInfoVo;
import com.zsy.loan.bean.entity.biz.TBizInvestInfo;
import com.zsy.loan.bean.entity.biz.TBizInvestPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestPlanStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.SettlementFlagEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.biz.InvestInfoRepo;
import com.zsy.loan.dao.biz.InvestPlanRepo;
import com.zsy.loan.service.factory.InvestStatusFactory;
import com.zsy.loan.service.factory.InvestTrialCalculateFactory;
import com.zsy.loan.service.sequence.IdentifyGenerated;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.JodaTimeUtil;
import com.zsy.loan.utils.factory.Page;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 融资服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class InvestServiceImpl extends BaseServiceImpl {

  @Autowired
  private InvestPlanRepo investPlanRepo;

  @Autowired
  private InvestInfoRepo repository;

  @Autowired
  private ISystemService systemService;

  /**
   * 取得凭证list
   */
  public Page<TBizInvestInfo> getTBizInvests(Page<TBizInvestInfo> page, InvestInfoVo condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("status"));
    orders.add(Order.asc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizInvestInfo> page1 = repository
        .findAll(new Specification<TBizInvestInfo>() {

          @Override
          public Predicate toPredicate(Root<TBizInvestInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getOrgNo())) {
              list.add(criteriaBuilder.equal(root.get("orgNo"), condition.getOrgNo()));
            }

            if (!ObjectUtils.isEmpty(condition.getCustNo())) {
              list.add(criteriaBuilder.equal(root.get("custNo"), condition.getCustNo()));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryBeginDate())) {
              list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryBeginDate())));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryEndDate())) {
              list.add(criteriaBuilder.lessThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryEndDate())));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;

  }

  public Page<TBizInvestPlan> getPlanPages(Page<TBizInvestPlan> page, TBizInvestPlan condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("status"));
    orders.add(Order.asc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizInvestPlan> page1 = investPlanRepo
        .findAll(new Specification<TBizInvestPlan>() {

          @Override
          public Predicate toPredicate(Root<TBizInvestPlan> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getInvestNo())) {
              list.add(criteriaBuilder.equal(root.get("investNo"), condition.getInvestNo()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  /**
   * 保存
   */
  public Object save(@Valid InvestInfoVo invest, boolean b) {
    /**
     * 判断状态
     */
    if (b) { //修改
      TBizInvestInfo loanInfo = repository.findById(invest.getId()).get();
      if (!loanInfo.getStatus().equals(InvestStatusEnum.CHECK_IN.getValue())) { //登记
        throw new LoanException(BizExceptionEnum.LOAN_NOT_CHECK_IN, String.valueOf(invest.getId()));
      }
    }

    /**
     * 试算
     */
    InvestCalculateVo calculateRequest = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(invest, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.INVEST_CHECK_IN.getValue()); //设置业务类型
    InvestCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!invest.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
//    if (invest.getTotSchdInterest().compareTo(result.getTotSchdInterest()) != 0) {
//      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
//    }

    TBizInvestInfo info = TBizInvestInfo.builder().build();
    BeanKit.copyProperties(invest, info);

    /**
     * 赋值
     */
    info.setExtensionNo(0l);
    info.setExtensionRate(BigDecimal.valueOf(0.00));
    info.setAcctDate(systemService.getSysAcctDate());
    info.setStatus(InvestStatusEnum.CHECK_IN.getValue()); //登记
    info.setPrin(result.getPrin()); // 本金
    info.setTotSchdInterest(result.getTotSchdInterest()); // 应收利息

    info.setTotPaidPrin(BigDecimal.valueOf(0.00)); // 已还本金累计
    info.setTotPaidInterest(BigDecimal.valueOf(0.00)); // 已还利息累计
    info.setTotWavAmt(BigDecimal.valueOf(0.00)); // 减免金额累计
    info.setTotAccruedInterest(BigDecimal.valueOf(0.00)); //计提利息累计

    if(!b){
      info.setId(IdentifyGenerated.INSTANCE.getNextId()); //修改为统一的凭证编号规则
    }

    repository.save(info);

    return true;
  }

  /**
   * 试算
   */
  public InvestCalculateVo calculate(InvestCalculateVo calculate) {

    /**
     * 根据本金、利率、开始结束日期计算利息、期数、应还本金、应还利息、回款计划相关信息
     */
    //设置共同信息
    setCalculateCommon(calculate);

    /**
     * 查询下回款计划
     */
    List<TBizInvestPlan> investPlans = investPlanRepo.findByInvestNo(calculate.getId());
    calculate.setPlanList(investPlans);

    return executeCalculate(calculate);

  }

  /**
   * 设置试算的共同数据信息
   */
  private void setCalculateCommon(InvestCalculateVo calculate) {
    calculate.setDayRate(BigDecimalUtil.div(calculate.getRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN)); //日利息
    calculate.setMonthRate(BigDecimalUtil.div(calculate.getRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN)); //月利息

    calculate.setDay(JodaTimeUtil.daysBetween(calculate.getBeginDate(), calculate.getEndDate())); //相差天数

    if (calculate.getInvestType().equals(InvestTypeEnum.HALF_YEAR_SETTLEMENT.getValue())) {
      calculate.setMonth(JodaTimeUtil.getMonthContain(calculate.getBeginDate(), calculate.getEndDate())); //相差月数
    } else {
      calculate.setMonth(JodaTimeUtil.getMonthFloor(calculate.getBeginDate(), calculate.getEndDate())); //相差月数
    }

    if (calculate.getExtensionRate() != null) {
      calculate.setDelayDayRate(BigDecimalUtil.div(calculate.getExtensionRate(), BigDecimal.valueOf(360), 6, BigDecimal.ROUND_HALF_EVEN)); //延期日利息
      calculate.setDelayMonthRate(BigDecimalUtil.div(calculate.getExtensionRate(), BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_EVEN)); //延期月利息
    }

    calculate.setAcctDate(systemService.getSysAcctDate()); //设置当前账务日期
  }


  /**
   * 执行试算
   */
  public InvestCalculateVo executeCalculate(InvestCalculateVo calculate) {

    InvestCalculateVo result = InvestTrialCalculateFactory.maps
        .get(InvestTypeEnum.getEnumByKey(calculate.getInvestType()) + "_" + LoanBizTypeEnum.getEnumByKey(calculate.getBizType())).apply(calculate);
    return result;
  }


  /**
   * 确认
   */
  @Transactional
  public boolean confirm(@Valid InvestConfirmInfoVo invest, boolean b) {

    /**
     * 锁记录
     */
    TBizInvestInfo old = repository.lockRecordByIdStatus(invest.getId(), invest.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING, invest.getId() + "_" + invest.getStatus());
    }

    /**
     * 试算
     */
    InvestCalculateVo calculateRequest = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(invest, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.INVEST.getValue()); //设置业务类型
    InvestCalculateVo result = calculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (!invest.getTermNo().equals(result.getTermNo())) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "期数不一致");
    }
    if (invest.getTotSchdInterest().compareTo(result.getTotSchdInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }

    TBizInvestInfo info = TBizInvestInfo.builder().build();
    BeanKit.copyProperties(invest, info);

    /**
     * 修改凭证信息
     */
    info.setStatus(InvestStatusFactory.getNextStatus(invest.getStatus() + "_" + LoanBizTypeEnum.INVEST.getValue()).getValue()); //融资
    info.setAcctDate(systemService.getSysAcctDate());
    repository
        .confirm(info.getId(), info.getStatus(), info.getRemark(), result.getDdDate(), systemService.getSysAcctDate(),
            result.getTotSchdInterest());

    /**
     * 先删除后插保存还款计划信息
     */
    investPlanRepo.deleteByInvestNo(info.getId());
    investPlanRepo.saveAll(result.getPlanList());

    /**
     * 调用账户模块记账
     */
    String key = LoanBizTypeEnum.INVEST + "_" + "";
    executeAccounting(key,result);

    return true;

  }

  public InvestCalculateVo delayCalculate(@Valid InvestCalculateVo calculate) {

    /**
     * 根据本金、利率、开始结束日期计算利息、期数、应还本金、应还利息、回款计划相关信息
     */
    //设置共同信息
    setCalculateCommon(calculate);

    /**
     * 查询下回款计划
     */
    List<TBizInvestPlan> investPlans = investPlanRepo.findByInvestNo(calculate.getId());
    calculate.setPlanList(investPlans);

    return executeCalculate(calculate);
  }

  /**
   * 延期
   */
  @Transactional
  public boolean delay(@Valid InvestDelayInfoVo invest, boolean b) {

    /**
     * 锁记录
     */
    TBizInvestInfo old = repository.lockRecordByIdStatus(invest.getId(), invest.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING, invest.getId() + "_" + invest.getStatus());
    }

    /**
     * 试算
     */
    InvestCalculateVo calculateRequest = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(invest, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.DELAY.getValue()); //设置业务类型
    InvestCalculateVo result = delayCalculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (invest.getCurrentExtensionNo().compareTo(result.getCurrentExtensionNo()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "当前延期期数不一致");
    }
    if (invest.getCurrentExtensionRate().compareTo(result.getCurrentExtensionRate()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "当前延期利率不一致");
    }

    TBizInvestInfo info = TBizInvestInfo.builder().build();
    BeanKit.copyProperties(invest, info);

    /**
     * 调整还款计划
     */
    //插入展期的还款计划
    investPlanRepo.saveAll(result.getPlanList());

    /**
     * 修改凭证信息
     */
    info.setStatus(InvestStatusFactory.getNextStatus(invest.getStatus() + "_" + LoanBizTypeEnum.DELAY.getValue()).getValue()); //融资
    info.setAcctDate(systemService.getSysAcctDate());
    repository
        .delay(info.getId(), info.getStatus(), info.getRemark(), systemService.getSysAcctDate(), result.getTotSchdInterest(),
            result.getCurrentExtensionNo(), result.getCurrentExtensionRate(), result.getEndDate());

    return true;

  }


  /**
   * 计提利息
   */
  @Transactional
  public void accrual(List<Long> investPlanIds) {

    for (int i = 0; i < investPlanIds.size(); i++) {
      TBizInvestPlan investPlan = investPlanRepo.findById(investPlanIds.get(i)).get();

      if (!investPlan.getStatus().equals(InvestPlanStatusEnum.UN_INTEREST.getValue())) {
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, "回款状态不是未计息");
      }

      //计息日必须在当前系统时间之后
      if (!DateUtil.compareDate(investPlan.getDdDate(), systemService.getSysAcctDate())) {
        throw new LoanException(BizExceptionEnum.DATE_COMPARE_ERROR, "计息日必须在当前系统时间之后");
      }

      /**
       * 更新状态
       */
      investPlan.setStatus(InvestPlanStatusEnum.INTERESTED.getValue());
      investPlanRepo.save(investPlan);

      /**
       * 更新融资凭证
       */
      repository.accrual(investPlan.getInvestNo(), investPlan.getChdInterest(), systemService.getSysAcctDate());
    }
  }

  /**
   * 撤资
   */
  @Transactional
  public boolean divestment(@Valid InvestDivestmentInfoVo invest, boolean b) {

    /**
     * 锁记录
     */
    TBizInvestInfo old = repository.lockRecordByIdStatus(invest.getId(), invest.getStatus());
    if (old == null) {
      throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING, invest.getId() + "_" + invest.getStatus());
    }

    /**
     * 试算
     */
    InvestCalculateVo calculateRequest = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(invest, calculateRequest);

    if (invest.getDivestmentAmt().compareTo(invest.getCalculateAmt()) == 0) {
      calculateRequest.setBizType(LoanBizTypeEnum.DIVESTMENT.getValue()); //设置业务类型
    } else {
      calculateRequest.setBizType(LoanBizTypeEnum.PART_DIVESTMENT.getValue()); //设置业务类型
    }

    InvestCalculateVo result = divestmentCalculate(calculateRequest); //试算结果

    /**
     * 比较试算结果是否与传入的一致
     */
    if (invest.getCalculateAmt().compareTo(result.getCalculateAmt()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "计算撤资本金");
    }
    if (invest.getCalculateInterest().compareTo(result.getCalculateInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "计算撤资利息");
    }

    TBizInvestInfo info = TBizInvestInfo.builder().build();
    BeanKit.copyProperties(invest, info);

    /**
     * 调整还款计划
     */
    investPlanRepo.saveAll(result.getCurrentPlanList());
    investPlanRepo.saveAll(result.getAfterPayRecords());
    investPlanRepo.saveAll(result.getInterestedRecords());

    /**
     * 调用账户模块记账
     */
    String key = LoanBizTypeEnum.getEnumByKey(calculateRequest.getBizType()) + "_" + "";
    executeAccounting(key,result);

    /**
     * 修改凭证信息
     */
    if (invest.getDivestmentAmt().compareTo(invest.getCalculateAmt()) == 0) {
      info.setStatus(InvestStatusFactory.getNextStatus(invest.getStatus() + "_" + LoanBizTypeEnum.DIVESTMENT.getValue()).getValue());
    } else {
      info.setStatus(InvestStatusFactory.getNextStatus(invest.getStatus() + "_" + LoanBizTypeEnum.PART_DIVESTMENT.getValue()).getValue());
    }
    info.setAcctDate(systemService.getSysAcctDate());

    //计提利息累计修改为0.00
    repository.divestment(info.getId(), info.getStatus(), info.getRemark(), systemService.getSysAcctDate(), result.getTotSchdInterest(),
        result.getTotPaidPrin(), BigDecimal.valueOf(0.00), result.getTotWavAmt(), result.getTotPaidInterest());

    return true;
  }

  /**
   * 撤资试算
   */
  public InvestCalculateVo divestmentCalculate(@Valid InvestCalculateVo calculate) {

    //校验状态
    InvestStatusFactory.checkCurrentStatus(calculate.getStatus() + "_" + calculate.getBizType());

    /**
     * 根据本金、利率、开始结束日期计算利息、期数、应还本金、应还利息、回款计划相关信息
     */
    //设置共同信息
    setCalculateCommon(calculate);

    /**
     * 查询下回款计划
     */
    Date acctDate = systemService.getSysAcctDate();
    List<TBizInvestPlan> currentPlans = investPlanRepo.findCurrentTermRecord(calculate.getId(), acctDate);
    if (currentPlans == null || currentPlans.size() == 0) {
      throw new LoanException(BizExceptionEnum.NOT_FOUND, "未查询到当前期的还款计划");
    }
    calculate.setCurrentPlanList(currentPlans);

    Long currentTermNo = currentPlans.get(0).getTermNo();
    List<Long> status = new ArrayList<>(1);
    status.add(InvestPlanStatusEnum.INTERESTED.getValue());
    List<TBizInvestPlan> InterestedPlans = investPlanRepo.findBeforeRecord(calculate.getId(), status, currentTermNo);
    calculate.setInterestedRecords(InterestedPlans);

    List<Long> status2 = new ArrayList<>(1);
    status2.add(InvestPlanStatusEnum.UN_INTEREST.getValue());
    List<TBizInvestPlan> unInterestRecords = investPlanRepo.findBeforeRecord(calculate.getId(), status2, currentTermNo);
    if (unInterestRecords != null && unInterestRecords.size() != 0) {
      throw new LoanException(BizExceptionEnum.STATUS_ERROR, "有未计息的回款计划");
    }

    List<TBizInvestPlan> afterPayRecords = investPlanRepo.findAfterRecord(calculate.getId(), currentTermNo);
    calculate.setAfterPayRecords(afterPayRecords);

    return executeCalculate(calculate);
  }

  /**
   * 取得撤资数据
   */
  public InvestCalculateVo getDivestmentInfo(Long investId) {

    //借据
    TBizInvestInfo invest = repository.findById(investId).get();

    /**
     * 试算
     */
    InvestCalculateVo calculateRequest = InvestCalculateVo.builder().build();
    BeanKit.copyProperties(invest, calculateRequest);
    calculateRequest.setBizType(LoanBizTypeEnum.DIVESTMENT.getValue()); //设置业务类型
    InvestCalculateVo result = divestmentCalculate(calculateRequest); //试算结果

    return result;
  }

  /**
   * 结转check
   */
  public String toSettlement(List<Long> investIds) {

    StringBuilder msg = new StringBuilder();
    /**
     * 校验 结转标识是否开启、时间是否是630 1231以后、回款计划是否已计提、计提金额是否跟回款计划金额一致、融资凭证状态是否正确
     */
    Integer settlementFlag = systemService.getSettlementFlag();
    if (settlementFlag == null || SettlementFlagEnum.NOT_SETTLEMENT.getValue() == settlementFlag) {
      throw new LoanException(BizExceptionEnum.STATUS_ERROR, "请在系统维护里设置结转标识！！！");
    }

    Date settleDate = null;  //结转截止日期
    Date acctDate = systemService.getSysAcctDate();
    int year = JodaTimeUtil.getYear(acctDate);
    int month = JodaTimeUtil.getMonthOfYear(acctDate);
    if (SettlementFlagEnum.MID_YEAR_SETTLEMENT.getValue() == settlementFlag) { //年中结转
      settleDate = DateUtil.parseDate(year + "-06-30");
    }
    if (SettlementFlagEnum.YEAR_SETTLEMENT.getValue() == settlementFlag) { //年末结转
      if (month == 12) {
        settleDate = DateUtil.parseDate(year + "-12-31");
      } else { //如果不是12月 证明这次年末结转已经跨年
        settleDate = DateUtil.parseDate((year - 1) + "-12-31");
      }
    }
    if (!DateUtil.compareDate(settleDate, acctDate)) {
      throw new LoanException(BizExceptionEnum.DATE_COMPARE_ERROR, "结转日期必须大于等于6月30号或12月31号");
    }

    for (Long investId : investIds) {
      TBizInvestInfo invest = repository.findById(investId).get();

      if (!invest.getInvestType().equals(InvestTypeEnum.HALF_YEAR_SETTLEMENT.getValue())) {
        throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "选中信息不是可结转的业务");
      }

      //校验状态
      InvestStatusFactory.checkCurrentStatus(invest.getStatus() + "_" + LoanBizTypeEnum.SETTLEMENT.getValue());

      List<Long> status = new ArrayList<>(1);
      status.add(InvestPlanStatusEnum.INTERESTED.getValue());
      Map<String, Object> result = investPlanRepo.findSettlement(investId, status, settleDate);

      if (result == null || result.get("interest") == null) {
        throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "查询回款计划错误");
      }

      BigDecimal interest = (BigDecimal) result.get("interest");
      if (interest.compareTo(invest.getTotAccruedInterest()) != 0) {
        throw new LoanException(BizExceptionEnum.PARAMETER_ERROR, "凭证计提利息与回款计划不一致");
      }

      msg.append("id：" + investId);
      msg.append("|本金：" + BigDecimalUtil.formatAmt(BigDecimalUtil.sub(invest.getPrin(), invest.getTotPaidPrin())));
      msg.append("|利息：" + BigDecimalUtil.formatAmt(invest.getTotAccruedInterest()));
      msg.append("<BR>");

    }

    return msg.toString();
  }

  /**
   * 结转
   */
  @Transactional
  public void settlement(List<Long> investIds) {

    /**
     * 校验
     */
    toSettlement(investIds);

    Integer settlementFlag = systemService.getSettlementFlag();
    Date settleDate = null;  //结转截止日期
    Date acctDate = systemService.getSysAcctDate();
    int year = JodaTimeUtil.getYear(acctDate);
    if (SettlementFlagEnum.MID_YEAR_SETTLEMENT.getValue() == settlementFlag) { //年中结转
      settleDate = DateUtil.parseDate(year + "-06-30");
    }
    if (SettlementFlagEnum.YEAR_SETTLEMENT.getValue() == settlementFlag) { //年中结转
      if (year == 12) {
        settleDate = DateUtil.parseDate(year + "-12-31");
      } else { //如果不是12月 证明这次年末结转已经跨年
        settleDate = DateUtil.parseDate((year - 1) + "-12-31");
      }
    }

    for (Long investId : investIds) {

      TBizInvestInfo invest = repository.findById(investId).get();
      /**
       * 锁记录
       */
      TBizInvestInfo old = repository.lockRecordByIdStatus(invest.getId(), invest.getStatus());
      if (old == null) {
        throw new LoanException(BizExceptionEnum.NOT_EXISTED_ING, invest.getId() + "_" + invest.getStatus());
      }

      /**
       * 调整还款计划
       */
      //更新结息还款计划
      investPlanRepo.updateSettlement(investId, InvestPlanStatusEnum.INTERESTED.getValue(), InvestPlanStatusEnum.RECEIVING.getValue(),
          settleDate, acctDate);

      //更新未结息还款计划
      investPlanRepo.updateSettlementAfter(investId, InvestPlanStatusEnum.UN_INTEREST.getValue(), InvestPlanStatusEnum.END.getValue(),
          settleDate, acctDate);

      /**
       * 调用账户模块记账
       */
      String key =  LoanBizTypeEnum.SETTLEMENT + "_" + "";
      executeAccounting(key,old);

      /**
       * 更新凭证
       */
      repository.settlement(investId, acctDate, BigDecimalUtil.add(old.getTotAccruedInterest(), old.getTotPaidInterest()), old.getPrin(), BigDecimal
          .valueOf(0.00), InvestStatusEnum.SETTLEMENT.getValue());

      /**
       * 插入结转后的凭证--登记状态
       */
      InvestInfoVo newInfo = InvestInfoVo.builder().build();
      BeanKit.copyProperties(old, newInfo);
      newInfo.setId(null);
      newInfo.setBeginDate(settleDate);
      newInfo.setEndDate(JodaTimeUtil.getAfterDayMonth(settleDate, 6));
      BigDecimal prin = BigDecimalUtil.add(BigDecimalUtil.sub(old.getPrin(), old.getTotPaidPrin()), old.getTotAccruedInterest());
      newInfo.setPrin(prin); //默认本金=待收本金+计提利息
      newInfo.setTermNo(6l);
      save(newInfo, false);

    }

  }

  @Transactional
  public Boolean delete(List<Long> investIds) {

    for (long investId : investIds) {
      /**
       * 判断账户状态
       */
      TBizInvestInfo info = repository.findById(investId).get();
      if (!info.getStatus().equals(InvestStatusEnum.CHECK_IN.getValue())) { //登记
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(investId));
      }

      repository.deleteById(investId);

    }
    return true;

  }
}
