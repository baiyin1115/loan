package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.base.tips.SuccessTip;
import com.zsy.loan.admin.core.base.tips.Tip;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.convey.TransferInfoVo;
import com.zsy.loan.bean.dictmap.biz.TransferDict;
import com.zsy.loan.bean.entity.biz.TBizTransferVoucherInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.dao.biz.TransferVoucherRepo;
import com.zsy.loan.service.biz.impl.TransferServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.biz.TransferWrapper;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.factory.Page;
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
 * 转账控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/transfer")
public class TransferController extends BaseController {

  private String PREFIX = "/biz/transfer/";

  @Resource
  TransferVoucherRepo repo;

  @Resource
  TransferServiceImpl service;


  /**
   * 跳转到转账管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "transfer.html";
  }

  /**
   * 跳转到添加转账
   */
  @RequestMapping("/transfer_add")
  public String infoVoAdd() {
    return PREFIX + "transfer_add.html";
  }

  /**
   * 跳转到修改转账
   */
  @Permission
  @RequestMapping("/to_transfer_update/{transferId}")
  public String infoVoUpdate(@PathVariable Long transferId, Model model) {
    TransferInfoVo transfer = TransferInfoVo.builder().build();
    BeanKit.copyProperties(repo.findById(transferId).get(), transfer);

    if (transfer.getInAcctNo() != null) {
      transfer.setInAcctName(ConstantFactory.me().getAcctName(transfer.getInAcctNo()));
    }
    if (transfer.getOutAcctNo() != null) {
      transfer.setOutAcctName(ConstantFactory.me().getAcctName(transfer.getOutAcctNo()));
    }

    transfer.setRemark(transfer.getRemark() == null ? "" : transfer.getRemark().trim());

    model.addAttribute("transfer", transfer);
    LogObjectHolder.me().set(transfer);
    return PREFIX + "transfer_edit.html";

  }

  /**
   * 登记转账
   */
  @BussinessLog(value = "登记转账", dict = TransferDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  public Object add(@Valid @RequestBody TransferInfoVo infoVo, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    return service.save(infoVo, false);
  }

  /**
   * 获取所有转账列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(TransferInfoVo condition) {

    Page<TBizTransferVoucherInfo> page = new PageFactory<TBizTransferVoucherInfo>().defaultPage();

    page = service.getTBizTransferVouchers(page, condition);
    page.setRecords(
        (List<TBizTransferVoucherInfo>) new TransferWrapper(BeanUtil.objectsToMaps(page.getRecords()))
            .wrap());
    return super.packForBT(page);
  }

  /**
   * 转账详情
   */
  @RequestMapping(value = "/detail/{transferId}")
  @Permission
  @ResponseBody
  public Object detail(@PathVariable("transferId") Long id) {
    return repo.findById(id).get();
  }

  /**
   * 修改转账
   */
  @BussinessLog(value = "更新转账", dict = TransferDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(@Valid @RequestBody TransferInfoVo infoVo, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (infoVo.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    service.save(infoVo, true);
    return SUCCESS_TIP;
  }

  /**
   * 删除转账
   */
  @BussinessLog(value = "删除转账", dict = TransferDict.class)
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object delete(@RequestBody List<Long> ids) {

    if (ids == null || ids.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    service.delete(ids);
    return SUCCESS_TIP;

  }

  /**
   * 确认检查
   */
  @BussinessLog(value = "确认检查", dict = TransferDict.class)
  @RequestMapping(value = "/to_confirm", method = RequestMethod.POST)
  @ResponseBody
  @OpLog
  public Object toConfirm(@RequestBody List<Long> ids) {
    String resultMsg = service.toConfirm(ids);

    Tip tip = new SuccessTip();
    tip.setCode(200);
    tip.setMessage(resultMsg);

    return tip;

  }

  /**
   * 确认转账
   */
  @BussinessLog(value = "确认转账", dict = TransferDict.class)
  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object confirm(@RequestBody List<Long> ids) {

    if (ids == null || ids.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    service.confirm(ids);
    return SUCCESS_TIP;

  }

  /**
   * 撤销转账
   */
  @BussinessLog(value = "撤销转账", dict = TransferDict.class)
  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object cancel(@RequestBody List<Long> ids) {

    if (ids == null || ids.size() == 0) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    service.cancel(ids);
    return SUCCESS_TIP;

  }

}
