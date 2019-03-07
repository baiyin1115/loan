package com.zsy.loan.admin.modular.controller.system;

import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.constant.Const;
import com.zsy.loan.bean.dictmap.system.DictMap;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.system.DictRepository;
import com.zsy.loan.service.system.DictService;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.wrapper.system.DictWrapper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.ToolUtil;
import com.zsy.loan.bean.entity.system.Dict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典控制器
 *
 * @author fengshuonan
 * @Date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

  private String PREFIX = "/system/dict/";


  @Resource
  DictRepository dictRepository;

  @Resource
  DictService dictService;

  /**
   * 跳转到字典管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "dict.html";
  }

  /**
   * 跳转到添加字典
   */
  @RequestMapping("/dict_add")
  public String deptAdd() {
    return PREFIX + "dict_add.html";
  }

  /**
   * 跳转到修改字典
   */
  @Permission(Const.ADMIN_NAME)
  @RequestMapping("/dict_edit/{dictId}")
  public String deptUpdate(@PathVariable Integer dictId, Model model) {
    Dict dict = dictRepository.getOne(dictId);
    model.addAttribute("dict", dict);
    List<Dict> subDicts = dictRepository.findByPid(dictId.intValue());
    model.addAttribute("subDicts", subDicts);
    LogObjectHolder.me().set(dict);
    return PREFIX + "dict_edit.html";
  }

  /**
   * 新增字典
   *
   * @param dictValues 格式例如   "1:启用;2:禁用;3:冻结"
   */
  @BussinessLog(value = "添加字典记录", key = "dictName,dictValues", dict = DictMap.class)
  @RequestMapping(value = "/add")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object add(String dictName, String dictValues) {
    if (ToolUtil.isOneEmpty(dictName, dictValues)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    this.dictService.addDict(dictName, dictValues);
    return SUCCESS_TIP;
  }

  /**
   * 获取所有字典列表
   */
  @RequestMapping(value = "/list")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object list(String condition) {
    List<Dict> list = dictRepository.findByPid(0);
    return super.wrapObject(new DictWrapper(BeanUtil.objectsToMaps(list)));
  }

  /**
   * 字典详情
   */
  @RequestMapping(value = "/detail/{dictId}")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object detail(@PathVariable("dictId") Integer dictId) {
    return dictRepository.getOne(dictId);
  }

  /**
   * 修改字典
   */
  @BussinessLog(value = "修改字典", key = "dictName,dictValues", dict = DictMap.class)
  @RequestMapping(value = "/update")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object update(Integer dictId, String dictName, String dictValues) {
    if (ToolUtil.isOneEmpty(dictId, dictName, dictValues)) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    dictService.editDict(dictId, dictName, dictValues);
    return SUCCESS_TIP;
  }

  /**
   * 删除字典记录
   */
  @BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
  @RequestMapping(value = "/delete")
  @Permission(Const.ADMIN_NAME)
  @ResponseBody
  public Object delete(@RequestParam Integer dictId) {

    //缓存被删除的名称
    LogObjectHolder.me().set(ConstantFactory.me().getDictName(dictId));

    this.dictService.delteDict(dictId);
    return SUCCESS_TIP;
  }

}
