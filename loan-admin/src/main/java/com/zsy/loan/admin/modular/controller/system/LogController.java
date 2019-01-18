package com.zsy.loan.admin.modular.controller.system;

import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.constant.Const;
import com.zsy.loan.bean.constant.factory.PageFactory;
import com.zsy.loan.bean.constant.state.BizLogType;
import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.support.BeanKit;
import com.zsy.loan.service.system.OperationLogService;
import com.zsy.loan.service.warpper.LogWarpper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.bean.entity.system.OperationLog;
import com.zsy.loan.dao.system.OperationLogRepository;
import com.zsy.loan.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

  private static String PREFIX = "/system/log/";

  @Resource
  private OperationLogRepository operationLogRepository;
  @Autowired
  private OperationLogService operationLogService;


  /**
   * 跳转到日志管理的首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "log.html";
  }

  /**
   * 查询操作日志列表
   */
  @RequestMapping("/list")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object list(@RequestParam(required = false) String beginTime,
      @RequestParam(required = false) String endTime,
      @RequestParam(required = false) String logName,
      @RequestParam(required = false) Integer logType) {
    Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();

    page = operationLogService
        .getOperationLogs(page, beginTime, endTime, logName, BizLogType.valueOf(logType));
    page.setRecords(
        (List<OperationLog>) new LogWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp());
    return super.packForBT(page);
  }

  /**
   * 查询操作日志详情
   */
  @RequestMapping("/detail/{id}")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object detail(@PathVariable Integer id) {
    OperationLog operationLog = operationLogRepository.findOne(id);
    Map<String, Object> stringObjectMap = BeanKit.beanToMap(operationLog);
    return super.warpObject(new LogWarpper(stringObjectMap));
  }

  /**
   * 清空日志
   */
  @BussinessLog(value = "清空业务日志")
  @RequestMapping("/delLog")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object delLog() {

    operationLogRepository.clear();
    return SUCCESS_TIP;
  }
}
