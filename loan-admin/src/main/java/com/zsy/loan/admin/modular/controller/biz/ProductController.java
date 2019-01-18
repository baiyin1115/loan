package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.DeptDict;
import com.zsy.loan.bean.dictmap.ProductDict;
import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.biz.impl.ProductServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.service.warpper.DeptWarpper;
import com.zsy.loan.service.warpper.ProductWarpper;
import com.zsy.loan.utils.BeanUtil;
import com.zsy.loan.utils.Convert;
import com.zsy.loan.utils.ToolUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 产品控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

  private String PREFIX = "/biz/product/";

  @Resource
  UserRepository userRepository;

  @Resource
  ProductInfoRepo productInfoRepo;

  @Resource
  ProductServiceImpl productService;
  

  /**
   * 跳转到产品管理首页
   */
  @RequestMapping("")
  public String index() {
    return PREFIX + "product.html";
  }

  /**
   * 跳转到添加产品
   */
  @RequestMapping("/product_add")
  public String productAdd() {
    return PREFIX + "product_add.html";
  }

  /**
   * 跳转到修改产品
   */
  @Permission
  @RequestMapping("/product_update/{productId}")
  public String productUpdate(@PathVariable Long productId, Model model) {
    TBizProductInfo product = productInfoRepo.findOne(productId);
    model.addAttribute(product);
    LogObjectHolder.me().set(product);
    return PREFIX + "product_edit.html";
  }

  /**
   * 新增产品
   */
  @BussinessLog(value = "添加产品", key = "productName", dict = ProductDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  public Object add(TBizProductInfo product) {
    if (ToolUtil.isOneEmpty(product, product.getProductName())) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    return this.productInfoRepo.save(product);
  }

  /**
   * 获取所有产品列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(String condition) {
    List<TBizProductInfo> list = productService.query(condition);
    return super.warpObject(new ProductWarpper(BeanUtil.objectsToMaps(list)));
  }

  /**
   * 产品详情
   */
  @RequestMapping(value = "/detail/{productId}")
  @Permission
  @ResponseBody
  public Object detail(@PathVariable("productId") Long productId) {
    return productInfoRepo.findOne(productId);
  }

  /**
   * 修改产品
   */
  @BussinessLog(value = "修改产品", key = "productName", dict = ProductDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(TBizProductInfo product) {
    if (ToolUtil.isEmpty(product) || product.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }
    productInfoRepo.save(product);
    return SUCCESS_TIP;
  }

  /**
   * 删除产品
   */
  @BussinessLog(value = "删除产品", key = "productName", dict = ProductDict.class)
  @RequestMapping(value = "/delete")
  @Permission
  @ResponseBody
  public Object delete(@RequestParam Long productId) {
    //缓存被删除的产品名称
    LogObjectHolder.me().set(ConstantFactory.me().getProductName(productId));
    productInfoRepo.delete(productId);
    return SUCCESS_TIP;
  }

}