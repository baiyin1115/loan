package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.convey.InvestCalculateVo;
import com.zsy.loan.bean.convey.InvestConfirmInfoVo;
import com.zsy.loan.bean.convey.InvestDelayInfoVo;
import com.zsy.loan.bean.convey.InvestInfoVo;
import com.zsy.loan.bean.dictmap.biz.InvestDict;
import com.zsy.loan.bean.entity.biz.TBizInvestInfo;
import com.zsy.loan.bean.entity.biz.TBizInvestPlan;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.biz.InvestInfoRepo;
import com.zsy.loan.service.biz.impl.InvestServiceImpl;
import com.zsy.loan.service.factory.InvestStatusFactory;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.biz.InvestPlanWrapper;
import com.zsy.loan.service.wrapper.biz.InvestWrapper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateTimeKit;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.factory.Page;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 融资控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/invest")
public class InvestController extends BaseController {

  private String PREFIX = "/biz/invest/";

  @Resource
  InvestInfoRepo investInfoRepo;

  @Resource
  InvestServiceImpl investService;


  /**
   * 跳转到融资管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "invest.html";
  }

  /**
   * 获取所有融资列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(TBizInvestInfo condition) {

    Page<TBizInvestInfo> page = new PageFactory<TBizInvestInfo>().defaultPage();

    page = investService.getTBizInvests(page, condition);
    page.setRecords(
        (List<TBizInvestInfo>) new InvestWrapper(BeanUtil.objectsToMaps(page.getRecords()))
            .wrap());
    return super.packForBT(page);
  }

  /**
   * 获取回款计划列表
   */
  @RequestMapping(value = "/invest_plan_list")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "获取回款计划列表", notes = "获取回款计划列表")
  public Object investPlanList(TBizInvestPlan condition) {

    Page<TBizInvestPlan> page = new PageFactory<TBizInvestPlan>().defaultPage();

    page = investService.getPlanPages(page, condition);
    page.setRecords((List<TBizInvestPlan>) new InvestPlanWrapper(BeanUtil.objectsToMaps(page.getRecords())).wrap());
    return super.packForBT(page);
  }

  /**
   * 跳转到客户选择页面
   */
  @RequestMapping("/loan_cust_list")
  public String loanCustList() {
    return PREFIX + "invest_customer.html";
  }

  /**
   * 跳转到添加融资
   */
  @RequestMapping("/invest_add")
  public String investAdd() {
    return PREFIX + "invest_add.html";
  }

  /**
   * 新增融资
   */
  @BussinessLog(value = "添加融资", dict = InvestDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  public Object add(@Valid @RequestBody InvestInfoVo invest, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    //结束日期必须在开始日期之后
    if (!DateUtil.compareDate(invest.getBeginDate(), invest.getEndDate())) {
      throw new LoanException(BizExceptionEnum.INVEST_DATE, "");
    }

    invest.setBizType(LoanBizTypeEnum.INVEST_CHECK_IN.getValue()); //设置业务类型

    return investService.save(invest, false);
  }

  /**
   * 融资详情
   */
  @RequestMapping(value = "/detail/{investId}")
  @Permission
  @ResponseBody
  public Object detail(@PathVariable("investId") Long investId) {
    return investInfoRepo.findById(investId).get();
  }

  /**
   * 跳转到修改融资
   */
  @Permission
  @RequestMapping("/to_invest_update/{investId}")
  public String investUpdate(@PathVariable Long investId, Model model) {
    TBizInvestInfo invest = investInfoRepo.findById(investId).get();

    invest.setRemark(invest.getRemark() == null ? "" : invest.getRemark().trim());
    invest.setCustName(ConstantFactory.me().getCustomerName(invest.getCustNo()));
    invest.setInAcctName(ConstantFactory.me().getAcctName(invest.getInAcctNo()));

    model.addAttribute("invest", invest);
    LogObjectHolder.me().set(invest);
    return PREFIX + "invest_edit.html";

  }

  /**
   * 修改融资
   */
  @BussinessLog(value = "更新融资", dict = InvestDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(@Valid @RequestBody InvestInfoVo invest, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (invest.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    investService.save(invest, true);
    return SUCCESS_TIP;
  }


  /**
   * 试算
   */
  @BussinessLog(value = "试算", dict = InvestDict.class)
  @RequestMapping(value = "/calculate")
  @Permission
  @ResponseBody
  @ApiOperation(value = "试算", notes = "试算")
  public InvestCalculateVo calculate(@Valid @RequestBody InvestCalculateVo investCalculateVo,
      BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    //借款结束日期必须在开始日期之后
    if (!DateUtil.compareDate(investCalculateVo.getBeginDate(), investCalculateVo.getEndDate())) {
      throw new LoanException(BizExceptionEnum.INVEST_DATE, "");
    }

    investCalculateVo.setBizType(LoanBizTypeEnum.INVEST_CHECK_IN.getValue()); //设置业务类型

    InvestCalculateVo calculateVo = investService.calculate(investCalculateVo);

    StringBuffer tmp = new StringBuffer();
    tmp.append("本金：" + BigDecimalUtil.formatAmt(calculateVo.getPrin()));
    tmp.append(",应收利息：" + BigDecimalUtil.formatAmt(calculateVo.getTotSchdInterest()));
    tmp.append("<BR>");

    for (TBizInvestPlan plan : calculateVo.getPlanList()) {
      tmp.append("--------------------------------------------------------------<BR>");
      tmp.append("第：" + plan.getTermNo() + "期");
      tmp.append(",计息日期：" + DateTimeKit.formatDate(plan.getDdDate()));
      tmp.append(",本期计息本金：" + BigDecimalUtil.formatAmt(plan.getDdPrin()));
      tmp.append(",本期利息：" + BigDecimalUtil.formatAmt(plan.getChdInterest()));
      tmp.append(",本期开始日期：" + DateTimeKit.formatDate(plan.getBeginDate()));
      tmp.append(",本期结束日期：" + DateTimeKit.formatDate(plan.getEndDate()));
      tmp.append(",计息天数：" + plan.getDdNum());
      tmp.append("<BR>");
    }
    calculateVo.setResultMsg(tmp.toString());

    return calculateVo;
  }

  /**
   * 删除融资
   */
  @BussinessLog(value = "删除融资", dict = InvestDict.class)
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object delete(@RequestBody List<Long> investIds) {

    //缓存被删除的融资名称
//    LogObjectHolder.me().set(investService.getInvestNames(investIds));
    investInfoRepo.deleteByIds(investIds);
    return SUCCESS_TIP;

  }

  /**
   * 跳转到修改融资
   */
  @Permission
  @RequestMapping("/to_invest_confirm/{investId}")
  public String investConfirm(@PathVariable Long investId, Model model) {
    TBizInvestInfo invest = investInfoRepo.findById(investId).get();

    //校验状态
    InvestStatusFactory.checkCurrentStatus(invest.getStatus() + "_" + LoanBizTypeEnum.INVEST.getValue());

    invest.setRemark(invest.getRemark() == null ? "" : invest.getRemark().trim());
    invest.setCustName(ConstantFactory.me().getCustomerName(invest.getCustNo()));
    invest.setInAcctName(ConstantFactory.me().getAcctName(invest.getInAcctNo()));
    invest.setInvestTypeName(ConstantFactory.me().getInvestTypeName(invest.getInvestType()));
    invest.setOrgName(ConstantFactory.me().getOrgNoName(invest.getOrgNo()));

    model.addAttribute("invest", invest);
    LogObjectHolder.me().set(invest);
    return PREFIX + "invest_confirm.html";

  }

  /**
   * 确认融资
   */
  @BussinessLog(value = "确认融资", dict = InvestDict.class)
  @RequestMapping(value = "/confirm")
  @Permission
  @ResponseBody
  public Object confirm(@Valid @RequestBody InvestConfirmInfoVo invest, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (invest.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //校验状态
    InvestStatusFactory.checkCurrentStatus(invest.getStatus() + "_" + LoanBizTypeEnum.INVEST.getValue());

    investService.confirm(invest, true);
    return SUCCESS_TIP;
  }

  /**
   * 跳转到延期
   */
  @Permission
  @RequestMapping("/to_invest_delay/{investId}")
  public String investDelay(@PathVariable Long investId, Model model) {
    TBizInvestInfo invest = investInfoRepo.findById(investId).get();

    //校验状态
    InvestStatusFactory.checkCurrentStatus(invest.getStatus() + "_" + LoanBizTypeEnum.DELAY.getValue());

    invest.setRemark(invest.getRemark() == null ? "" : invest.getRemark().trim());
    invest.setCustName(ConstantFactory.me().getCustomerName(invest.getCustNo()));
    invest.setInAcctName(ConstantFactory.me().getAcctName(invest.getInAcctNo()));
    invest.setInvestTypeName(ConstantFactory.me().getInvestTypeName(invest.getInvestType()));
    invest.setOrgName(ConstantFactory.me().getOrgNoName(invest.getOrgNo()));

    model.addAttribute("invest", invest);
    LogObjectHolder.me().set(invest);
    return PREFIX + "invest_delay.html";

  }

  /**
   * 延期
   */
  @BussinessLog(value = "延期", dict = InvestDict.class)
  @RequestMapping(value = "/delay")
  @Permission
  @ResponseBody
  public Object delay(@Valid @RequestBody InvestDelayInfoVo invest, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (invest.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    //校验状态
    InvestStatusFactory.checkCurrentStatus(invest.getStatus() + "_" + LoanBizTypeEnum.DELAY.getValue());

    investService.delay(invest, true);
    return SUCCESS_TIP;
  }

  /**
   * 延期试算
   */
  @BussinessLog(value = "延期试算", dict = InvestDict.class)
  @RequestMapping(value = "/delayCalculate")
  @Permission
  @ResponseBody
  @ApiOperation(value = "试算", notes = "试算")
  public InvestCalculateVo delayCalculate(@Valid @RequestBody InvestCalculateVo investCalculateVo,
      BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    investCalculateVo.setBizType(LoanBizTypeEnum.DELAY.getValue()); //设置业务类型

    InvestCalculateVo calculateVo = investService.delayCalculate(investCalculateVo);

    StringBuffer tmp = new StringBuffer();
    tmp.append("本金：" + BigDecimalUtil.formatAmt(calculateVo.getPrin()));
    tmp.append(",应收利息：" + BigDecimalUtil.formatAmt(calculateVo.getTotSchdInterest()));
    tmp.append("<BR>");

    for (TBizInvestPlan plan : calculateVo.getPlanList()) {
      tmp.append("--------------------------------------------------------------<BR>");
      tmp.append("第：" + plan.getTermNo() + "期");
      tmp.append(",计息日期：" + DateTimeKit.formatDate(plan.getDdDate()));
      tmp.append(",本期计息本金：" + BigDecimalUtil.formatAmt(plan.getDdPrin()));
      tmp.append(",本期利息：" + BigDecimalUtil.formatAmt(plan.getChdInterest()));
      tmp.append(",本期开始日期：" + DateTimeKit.formatDate(plan.getBeginDate()));
      tmp.append(",本期结束日期：" + DateTimeKit.formatDate(plan.getEndDate()));
      tmp.append(",计息天数：" + plan.getDdNum());
      tmp.append(",状态：" + ConstantFactory.me().getInvestStatusName(plan.getStatus()));
      tmp.append("<BR>");
    }
    calculateVo.setResultMsg(tmp.toString());

    return calculateVo;
  }


  /**
   * 获取融资列表
   */
  @RequestMapping(value = "/selectInvestTreeList")
  @ResponseBody
  public List<ZTreeNode> selectInvestTreeList() {
    List<ZTreeNode> treeList = investService.getTreeList();
//    roleTreeList.add(ZTreeNode.createParent());
    return treeList;

  }

}
