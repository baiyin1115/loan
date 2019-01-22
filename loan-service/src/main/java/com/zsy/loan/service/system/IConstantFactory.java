package com.zsy.loan.service.system;


import com.zsy.loan.bean.entity.biz.TBizProductInfo;
import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.bean.vo.DictVo;

import java.util.List;

/**
 * 常量生产工厂的接口
 *
 * @author fengshuonan
 * @date 2017-06-14 21:12
 */
public interface IConstantFactory {

  /**
   * 根据用户id获取用户名称
   *
   * @author stylefeng
   * @Date 2017/5/9 23:41
   */
  String getUserNameById(Integer userId);

  /**
   * 根据用户id获取用户账号
   *
   * @author stylefeng
   * @date 2017年5月16日21:55:371
   */
  String getUserAccountById(Integer userId);

  /**
   * 通过角色ids获取角色名称
   */
  String getRoleName(String roleIds);

  /**
   * 通过角色id获取角色名称
   */
  String getSingleRoleName(Integer roleId);

  /**
   * 通过角色id获取角色英文名称
   */
  String getSingleRoleTip(Integer roleId);

  /**
   * 获取部门名称
   */
  String getDeptName(Integer deptId);

  /**
   * 获取部门名称
   */
  String getDeptName(String deptIds);

  /**
   * 获取部门id all
   */
  List<Dept> getDeptAll();

  /**
   * 获取菜单的名称们(多个)
   */
  String getMenuNames(String menuIds);

  /**
   * 获取菜单名称
   */
  String getMenuName(Long menuId);

  /**
   * 获取菜单名称通过编号
   */
  String getMenuNameByCode(String code);

  /**
   * 根据字典名称获取字典列表
   */
  List<DictVo> findByDictName(String dictName);

  /**
   * 获取字典名称
   */
  String getDictName(Integer dictId);

  /**
   * 获取通知标题
   */
  String getNoticeTitle(Integer dictId);

  /**
   * 根据字典名称和字典中的值获取对应的名称
   */
  String getDictsByName(String name, String val);

  /**
   * 获取性别名称
   */
  String getSexName(Integer sex);

  /**
   * 获取银行卡类型名称
   */
  String getCardTypeName(String cardType);

  /**
   * 获取个人证件类型
   */
  String getIdCardTypeName(String cardType);

  /**
   * 获取联系人关系
   */
  String getRelationName(String relation);

  /**
   * 获取用户登录状态
   */
  String getStatusName(Integer status);

  /**
   * 获取菜单状态
   */
  String getMenuStatusName(Integer status);

  /**
   * 查询字典
   */
  List<Dict> findInDict(Integer id);

  /**
   * 获取被缓存的对象(用户删除业务)
   */
  String getCacheObject(String para);

  /**
   * 获取子部门id
   */
  List<Integer> getSubDeptId(Integer deptid);

  /**
   * 获取所有父部门id
   */
  List<Integer> getParentDeptIds(Integer deptid);


  /**
   * 获取指定名称下的字典列表
   */
  List<Dict> getDicts(String pname);

  /**
   * 获取全局参数
   */
  String getCfg(String cfgName);

  /**
   * 获取服务费收取方式名称
   */
  String getServiceFeeTypeName(Long id);

  /**
   * 获取是否罚息名称
   */
  String getIsPenName(Long id);

  /**
   * 获取性别名称
   */
  String getPenNumberName(Long id);

  /**
   * 获取还款方式名称
   */
  String getRepayTypeName(Long id);

  /**
   * 获取贷款类型名称
   */
  String getLoanTypeName(Long id);

  /**
   * 获取产品名称
   */
  String getProductName(Long deptId);

  /**
   * 获取产品名称
   */
  List<TBizProductInfo> getProductNames(List<Long> deptId);

  /**
   * 获取公司名称
   */
  String getOrgNoName(Integer id);

}
