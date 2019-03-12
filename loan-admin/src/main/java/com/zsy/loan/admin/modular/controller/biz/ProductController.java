package com.zsy.loan.admin.modular.controller.biz;

import com.zsy.loan.admin.core.base.controller.BaseController;
import com.zsy.loan.admin.core.page.PageFactory;
import com.zsy.loan.bean.annotion.core.BussinessLog;
import com.zsy.loan.bean.annotion.core.Permission;
import com.zsy.loan.bean.dictmap.biz.ProductDict;
import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.logback.oplog.OpLog;
import com.zsy.loan.bean.convey.ProductInfoVo;
import com.zsy.loan.bean.vo.node.ZTreeNode;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.service.biz.impl.ProductServiceImpl;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.service.wrapper.biz.ProductWrapper;
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
 * 产品控制器
 *
 * @Author zhangxh
 * @Date 2019-01-18  14:38
 */
@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

  private String PREFIX = "/biz/product/";

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
    TBizProductInfo product = productInfoRepo.findById(productId).get();

    product.setRemark(product.getRemark()==null?"":product.getRemark().trim());

    model.addAttribute("product", product);
    LogObjectHolder.me().set(product);
    return PREFIX + "product_edit.html";

  }

  /**
   * 新增产品
   */
  @BussinessLog(value = "添加产品", dict = ProductDict.class)
  @RequestMapping(value = "/add")
  @Permission
  @ResponseBody
  public Object add(@Valid @RequestBody ProductInfoVo product, BindingResult error) {

    /**
     * 处理error
     */
    exportErr(error);

    return productService.save(product);
  }

  /**
   * 获取所有产品列表
   */
  @RequestMapping(value = "/list")
  @Permission
  @ResponseBody
  public Object list(String condition) {
//    List<TBizProductInfo> list = productService.query(condition);
//    return super.wrapObject(new ProductWrapper(BeanUtil.objectsToMaps(list)));
    Page<TBizProductInfo> page = new PageFactory<TBizProductInfo>().defaultPage();

    page = productService.getTBizProducts(page, condition);
    page.setRecords(
        (List<TBizProductInfo>) new ProductWrapper(BeanUtil.objectsToMaps(page.getRecords()))
            .wrap());
    return super.packForBT(page);
  }

  /**
   * 产品详情
   */
  @RequestMapping(value = "/detail/{productId}")
  @Permission
  @ResponseBody
  public Object detail(@PathVariable("productId") Long productId) {
    return productInfoRepo.findById(productId).get();
  }

  /**
   * 修改产品
   */
  @BussinessLog(value = "更新产品", dict = ProductDict.class)
  @RequestMapping(value = "/update")
  @Permission
  @ResponseBody
  public Object update(@Valid @RequestBody ProductInfoVo product, BindingResult error) {
    /**
     * 处理error
     */
    exportErr(error);

    if (product.getId() == null) {
      throw new LoanException(BizExceptionEnum.REQUEST_NULL);
    }

    productService.save(product);
    return SUCCESS_TIP;
  }

  /**
   * 删除产品
   */
  @BussinessLog(value = "删除产品", dict = ProductDict.class)
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @Permission
  @ResponseBody
  @OpLog
  public Object delete(@RequestBody List<Long> productIds) {

    //缓存被删除的产品名称
    LogObjectHolder.me().set(productService.getProductNames(productIds));
    productInfoRepo.deleteByIds(productIds);
    return SUCCESS_TIP;

  }

  /**
   * 获取产品列表
   */
  @RequestMapping(value = "/selectProductTreeList")
  @ResponseBody
  public List<ZTreeNode> selectProductTreeList() {
    List<ZTreeNode> treeList = productService.getTreeList();
//    roleTreeList.add(ZTreeNode.createParent());
    return treeList;

  }

}
