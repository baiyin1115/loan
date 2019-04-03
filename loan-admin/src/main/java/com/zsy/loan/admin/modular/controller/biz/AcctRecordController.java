package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.convey.AcctRecordVo;
import com.zsy.loan.bean.entity.biz.TBizAcctRecord;
import com.zsy.loan.service.biz.impl.AccountRecordServiceImpl;
import com.zsy.loan.service.wrapper.biz.AcctRecordWrapper;
import com.zsy.loan.service.wrapper.biz.TransferWrapper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.factory.Page;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 交易流水控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/acctRecord")
public class AcctRecordController extends BaseController {

  private String PREFIX = "/biz/acctRecord/";

  @Resource
  AccountRecordServiceImpl service;


  /**
   * 跳转到交易流水管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "acct_record.html";
  }


  /**
   * 获取所有交易流水列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(AcctRecordVo condition) {

    Page<TBizAcctRecord> page = new PageFactory<TBizAcctRecord>().defaultPage();

    page = service.getTBizAcctRecords(page, condition);
    page.setRecords(
        (List<TBizAcctRecord>) new AcctRecordWrapper(BeanUtil.objectsToMaps(page.getRecords()))
            .wrap());
    return super.packForBT(page);
  }

}
