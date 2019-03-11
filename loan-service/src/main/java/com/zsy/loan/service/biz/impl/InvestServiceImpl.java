package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.InvestCalculateVo;
import com.zsy.loan.bean.convey.InvestInfoVo;
import com.zsy.loan.bean.entity.biz.TBizInvestInfo;
import com.zsy.loan.bean.entity.biz.TBizInvestPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InvestTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.biz.InvestInfoRepo;
import com.zsy.loan.dao.biz.InvestPlanRepo;
import com.zsy.loan.service.factory.InvestTrialCalculateFactory;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.JodaTimeUtil;
import com.zsy.loan.utils.factory.Page;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    if (invest.getTotSchdInterest().compareTo(result.getTotSchdInterest()) != 0) {
      throw new LoanException(BizExceptionEnum.CALCULATE_REQ_NOT_MATCH, "应收利息不一致");
    }

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

    repository.save(info);

    return true;
  }

  /**
   * 试算
   */
  private InvestCalculateVo calculate(InvestCalculateVo calculate) {

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
    calculate.setMonth(JodaTimeUtil.getMonthContain(calculate.getBeginDate(), calculate.getEndDate())); //相差月数

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

    //预留出业务类型
    InvestCalculateVo result = InvestTrialCalculateFactory.maps
        .get(InvestTypeEnum.getEnumByKey(calculate.getInvestType()) + "_" + LoanBizTypeEnum.getEnumByKey(calculate.getBizType())).apply(calculate);
    return result;
  }

  /**
   * 取得凭证list
   */
  public Page<TBizInvestInfo> getTBizInvests(Page<TBizInvestInfo> page, TBizInvestInfo condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("status"));
    orders.add(Order.desc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizInvestInfo> page1 = repository
        .findAll(new Specification<TBizInvestInfo>() {

          @Override
          public Predicate toPredicate(Root<TBizInvestInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getUserNo())) {
              list.add(criteriaBuilder.equal(root.get("userNo"), condition.getUserNo()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;

  }

  public List<ZTreeNode> getTreeList() {
    return null;
  }
}
