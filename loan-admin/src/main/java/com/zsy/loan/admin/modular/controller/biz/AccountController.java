package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.biz.AcctDict;
import com.zsy.loan.bean.entity.biz.TBizAcct;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctBalanceTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.bean.convey.AcctVo;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.biz.AcctRepo;
import com.zsy.loan.service.biz.impl.AcctServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.biz.AcctWrapper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.factory.Page;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
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
 * 账户控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

  private String PREFIX = "/biz/account/";

  @Resource
  AcctRepo acctRepo;

  @Resource
  AcctServiceImpl acctService;

  /**
   * 跳转到账户管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "account.html";
  }

  /**
   * 跳转到添加账户
   */
  @RequestMapping("/account_add")
  public String accountAdd() {
    return PREFIX + "account_add.html";
  }

  /**
   * 跳转到修改账户
   */
  @Permission
  @RequestMapping("/account_update/{accountId}")
  public String accountUpdate(@PathVariable Long accountId, Model model) {
    TBizAcct account = acctRepo.findById(accountId).get();

    account.setRemark(account.getRemark()==null?"":account.getRemark().trim());
    account.setAcctTypeName(ConstantFactory.me().getAcctTypeName(account.getAcctType()));
    account.setStatusName(ConstantFactory.me().getAcctStatusName(account.getStatus()));

    model.addAttribute("account", account);
    LogObjectHolder.me().set(account);
    return PREFIX + "account_edit.html";

  }

  /**
   * 新增账户
   */
  @BussinessLog(value = "添加账户", dict = AcctDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  @ApiOperation(value = "新增账户", notes = "新增账户")
  public Object add(@Valid @RequestBody AcctVo account, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    /**
     * 校验
     */
    //不能开立投资人、借款人账户
    if (account.getAcctType().equals(AcctTypeEnum.INVEST.getValue()) || account.getAcctType()
        .equals(AcctTypeEnum.LOAN.getValue())) {
      throw new LoanException(BizExceptionEnum.ACCOUNT_NO_ADD, String.valueOf(account.getCustNo()));
    }

    //公司、代偿账户不能透支
    if (!(account.getAcctType().equals(AcctTypeEnum.INTERIM_IN.getValue())
        || account.getAcctType().equals(AcctTypeEnum.INTERIM_OUT.getValue())) && account
        .getBalanceType().equals(AcctBalanceTypeEnum.OVERDRAW.getValue())) {
      throw new LoanException(BizExceptionEnum.ACCOUNT_NO_OVERDRAW,
          String.valueOf(account.getCustNo()));
    }

    return acctService.save(account, false);
  }

  /**
   * 获取所有账户列表
   * 通过账户名、手机号、身份证号码 模糊查询
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "获取所有账户列表", notes = "获取所有账户列表")
  public Object list(TBizAcct condition) {

    Page<TBizAcct> page = new PageFactory<TBizAcct>().defaultPage();

    page = acctService.getTBizAccounts(page, condition);
    page.setRecords(
        (List<TBizAcct>) new AcctWrapper(BeanUtil.objectsToMaps(page.getRecords()))
            .wrap());
    return super.packForBT(page);
  }

  /**
   * 账户详情
   */
  @RequestMapping(value = "/detail/{accountId}")
  @Permission
  @ResponseBody
  @ApiOperation(value = "账户详情", notes = "账户详情")
  public Object detail(@PathVariable("accountId") Long accountId) {
    return acctRepo.findById(accountId).get();
  }

  /**
   * 修改账户
   */
  @BussinessLog(value = "更新账户", dict = AcctDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "修改账户", notes = "修改账户")
  public Object update(@Valid @RequestBody AcctVo account, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (account.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    /**
     * 修改校验
     */
    //公司、代偿账户不能透支
    if (!(account.getAcctType().equals(AcctTypeEnum.INTERIM_IN.getValue())
        || account.getAcctType().equals(AcctTypeEnum.INTERIM_OUT.getValue()))
        && account.getBalanceType()
        .equals(AcctBalanceTypeEnum.OVERDRAW.getValue())) {
      throw new LoanException(BizExceptionEnum.ACCOUNT_NO_OVERDRAW,
          String.valueOf(account.getCustNo()));
    }

    acctService.save(account, true);
    return SUCCESS_TIP;
  }

  /**
   * 删除账户
   */
  @BussinessLog(value = "逻辑删除账户", dict = AcctDict.class)
  @RequestMapping(value = "/logic_delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "逻辑删除账户", notes = "逻辑删除账户")
  public Object logicDelete(@RequestBody List<Long> accountIds) {

    if (accountIds == null || accountIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    acctService.logicDelete(accountIds);
    return SUCCESS_TIP;
  }

  /**
   * 冻结账户
   */
  @BussinessLog(value = "冻结账户", dict = AcctDict.class)
  @RequestMapping(value = "/freeze", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "冻结账户", notes = "冻结账户")
  public Object freeze(@RequestBody List<Long> accountIds) {

    if (accountIds == null || accountIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    acctService.freeze(accountIds);
    return SUCCESS_TIP;
  }

  /**
   * 取消黑名单用户
   */
  @BussinessLog(value = "解除冻结账户", dict = AcctDict.class)
  @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  @ApiOperation(value = "解除冻结账户", notes = "解除冻结账户")
  public Object unfreeze(@RequestBody List<Long> accountIds) {

    if (accountIds == null || accountIds.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    acctService.unfreeze(accountIds);
    return SUCCESS_TIP;
  }

  /**
   * 获取公司卡账户列表
   */
  @RequestMapping(value = "/selectCompanyAcctTreeList")
  @ResponseBody
  public List<ZTreeNode> selectCompanyAcctTreeList() {

    List para  =  new ArrayList(1);
    para.add(AcctTypeEnum.COMPANY.getValue());

    List<ZTreeNode> treeList = acctService.getAcctTreeList(para);
//    roleTreeList.add(ZTreeNode.createParent());
    return treeList;

  }

  /**
   * 获取违约账户列表
   */
  @RequestMapping(value = "/selectCompensationAcctTreeList")
  @ResponseBody
  public List<ZTreeNode> selectCompensationAcctTreeList() {

    List para  =  new ArrayList(1);
    para.add(AcctTypeEnum.REPLACE.getValue());

    List<ZTreeNode> treeList = acctService.getAcctTreeList(para);
//    roleTreeList.add(ZTreeNode.createParent());
    return treeList;

  }

}
