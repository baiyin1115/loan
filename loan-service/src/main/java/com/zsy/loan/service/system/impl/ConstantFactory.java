package com.zsy.loan.service.system.impl;

import com.zsy.loan.bean.constant.cache.CacheConstantKey;
import com.zsy.loan.bean.entity.system.Dept;
import com.zsy.loan.bean.entity.system.Dict;
import com.zsy.loan.bean.entity.system.Menu;
import com.zsy.loan.bean.entity.system.Notice;
import com.zsy.loan.bean.entity.system.Role;
import com.zsy.loan.bean.entity.system.User;
import com.zsy.loan.bean.enumeration.ManagerStatusEnum;
import com.zsy.loan.bean.enumeration.MenuStatusEnum;
import com.zsy.loan.bean.vo.DictVo;
import com.zsy.loan.bean.vo.SpringContextHolder;
import com.zsy.loan.dao.biz.ProductInfoRepo;
import com.zsy.loan.dao.cache.ConfigCache;
import com.zsy.loan.dao.cache.DictCache;
import com.zsy.loan.dao.system.DeptRepository;
import com.zsy.loan.dao.system.DictRepository;
import com.zsy.loan.dao.system.MenuRepository;
import com.zsy.loan.dao.system.RoleRepository;
import com.zsy.loan.dao.system.SysNoticeRepository;
import com.zsy.loan.dao.system.UserRepository;
import com.zsy.loan.service.system.IConstantFactory;
import com.zsy.loan.service.system.LogObjectHolder;
import com.zsy.loan.utils.Convert;
import com.zsy.loan.utils.StrKit;
import com.zsy.loan.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
@CacheConfig
public class ConstantFactory implements IConstantFactory {

  //  public static TimeCacheMap<String, String> cache = new TimeCacheMap<String, String>(3600, 2);
  private RoleRepository roleRepository = SpringContextHolder.getBean(RoleRepository.class);
  private DeptRepository deptRepository = SpringContextHolder.getBean(DeptRepository.class);
  private DictRepository dictRepository = SpringContextHolder.getBean(DictRepository.class);
  private UserRepository userRepository = SpringContextHolder.getBean(UserRepository.class);
  private MenuRepository menuRepository = SpringContextHolder.getBean(MenuRepository.class);
  private SysNoticeRepository sysNoticeRepository = SpringContextHolder
      .getBean(SysNoticeRepository.class);
  private ProductInfoRepo productInfoRepo = SpringContextHolder.getBean(ProductInfoRepo.class);

  private ConfigCache configCache = SpringContextHolder.getBean(ConfigCache.class);
  private DictCache dictCache = SpringContextHolder.getBean(DictCache.class);

  public static IConstantFactory me() {
    return SpringContextHolder.getBean("constantFactory");
  }

  public String get(String key) {
    return configCache.get(key) == null ? null : (String) configCache.get(key);
  }

  public void set(String key, String val) {
    configCache.set(key, val);
  }

  public String getDict(String key) {
    return dictCache.get(key) == null ? null : (String) dictCache.get(key);
  }

  public void setDict(String key, String val) {
    dictCache.set(key, val);
  }

  /**
   * 根据用户id获取用户名称
   *
   * @author stylefeng
   * @Date 2017/5/9 23:41
   */
  @Override
  public String getUserNameById(Integer userId) {
    String val = get(CacheConstantKey.SYS_USER_NAME + userId);
    if (StringUtils.isNotEmpty(val)) {
      return val;
    }
    Optional<User> optUser = userRepository.findById(userId);
    User user = optUser.get();
    if (user != null) {
      val = user.getName();
      set(CacheConstantKey.SYS_USER_NAME + userId, val);
      return val;
    } else {
      return "--";
    }
  }

  /**
   * 根据用户id获取用户账号
   *
   * @author stylefeng
   * @date 2017年5月16日21:55:371
   */
  @Override
  public String getUserAccountById(Integer userId) {
    Optional<User> optUser = userRepository.findById(userId);
    User user = optUser.get();
    if (user != null) {
      return user.getAccount();
    } else {
      return "--";
    }
  }

  /**
   * 通过角色ids获取角色名称
   */
  @Override
  public String getRoleName(String roleIds) {
    String val = get(CacheConstantKey.ROLES_NAME + roleIds);
    if (StringUtils.isNotEmpty(val)) {
      return val;
    }
    Integer[] roles = Convert.toIntArray(roleIds);
    StringBuilder sb = new StringBuilder();
    for (int role : roles) {
      Optional<Role> optRoleObj = roleRepository.findById(role);
      Role roleObj = optRoleObj.get();
      if (StringUtils.isNotNullOrEmpty(roleObj) && StringUtils.isNotEmpty(roleObj.getName())) {
        sb.append(roleObj.getName()).append(",");
      }
    }
    val = StrKit.removeSuffix(sb.toString(), ",");
    set(CacheConstantKey.ROLES_NAME + roleIds, val);
    return val;
  }

  /**
   * 通过角色id获取角色名称
   */
  @Override
  public String getSingleRoleName(Integer roleId) {
    if (0 == roleId) {
      return "--";
    }
    Optional<Role> optRoleObj = roleRepository.findById(roleId);
    Role roleObj = optRoleObj.get();
    if (StringUtils.isNotNullOrEmpty(roleObj) && StringUtils.isNotEmpty(roleObj.getName())) {
      return roleObj.getName();
    }
    return "";
  }

  /**
   * 通过角色id获取角色英文名称
   */
  @Override
  public String getSingleRoleTip(Integer roleId) {
    if (0 == roleId) {
      return "--";
    }
    Role roleObj = roleRepository.findById(roleId).get();
    if (StringUtils.isNotNullOrEmpty(roleObj) && StringUtils.isNotEmpty(roleObj.getName())) {
      return roleObj.getTips();
    }
    return "";
  }

  /**
   * 获取部门名称
   */
  @Override
  public String getDeptName(Integer deptId) {
    String val = get(CacheConstantKey.DEPT_NAME + deptId);
    if (StringUtils.isNotEmpty(val)) {
      return val;
    }
    Dept dept = deptRepository.getOne(deptId);
    if (StringUtils.isNotNullOrEmpty(dept) && StringUtils.isNotEmpty(dept.getFullname())) {
      val = dept.getFullname();
      set(CacheConstantKey.DEPT_NAME + deptId, val);
      return val;
    }
    return "";
  }

  /**
   * 通过角色ids获取角色名称
   */
  @Override
  public String getDeptName(String deptIds) {
    String val = get(CacheConstantKey.DEPT_NAME + deptIds);
    if (StringUtils.isNotEmpty(val)) {
      return val;
    }
    Integer[] depts = Convert.toIntArray(deptIds);
    StringBuilder sb = new StringBuilder();
    for (int dept : depts) {
      Dept deptObj = deptRepository.findById(dept).orElse(null);
      if (StringUtils.isNotNullOrEmpty(deptObj) && StringUtils.isNotEmpty(deptObj.getFullname())) {
        sb.append(deptObj.getFullname()).append(",");
      }
    }
    val = StrKit.removeSuffix(sb.toString(), ",");
    set(CacheConstantKey.DEPT_NAME + deptIds, val);
    return val;
  }

  public List<Dept> getDeptAll() {
    return deptRepository.findAll();
  }

  /**
   * 获取菜单的名称们(多个)
   */
  @Override
  public String getMenuNames(String menuIds) {
    Integer[] menus = Convert.toIntArray(menuIds);
    StringBuilder sb = new StringBuilder();
    for (int menu : menus) {
      Menu menuObj = menuRepository.findById(Long.valueOf(menu)).get();
      if (StringUtils.isNotNullOrEmpty(menuObj) && StringUtils.isNotEmpty(menuObj.getName())) {
        sb.append(menuObj.getName()).append(",");
      }
    }
    return StrKit.removeSuffix(sb.toString(), ",");
  }

  /**
   * 获取菜单名称
   */
  @Override
  public String getMenuName(Long menuId) {

    Menu menu = menuRepository.findById(menuId).get();
    if (menu == null) {
      return "";
    } else {
      return menu.getName();
    }
  }

  /**
   * 获取菜单名称通过编号
   */
  @Override
  public String getMenuNameByCode(String code) {

    Menu menu = menuRepository.findByCode(code);
    if (menu == null) {
      return "";
    } else {
      return menu.getName();
    }
  }

  @Override
  public List<DictVo> findByDictName(String dictName) {

    List<DictVo> list = new ArrayList<DictVo>();

    List<Dict> dicts = dictCache.getDictsByPname(dictName);
    if (dicts != null) {
      for (int i = 0; i < dicts.size(); i++) {
        Dict dict = dicts.get(i);
        DictVo dictVo = new DictVo(dict.getNum(), dict.getName());
        list.add(dictVo);
      }
    }
    return list;
  }

  /**
   * 获取字典名称
   */
  @Override
  public String getDictName(Integer dictId) {

    String val = get(CacheConstantKey.DICT_NAME + dictId);
    if (StringUtils.isNotEmpty(val)) {
      return val;
    }
    val = dictCache.getDict(dictId);
    set(CacheConstantKey.DICT_NAME + dictId, val);
    return val;

  }

  /**
   * 获取通知标题
   */
  @Override
  public String getNoticeTitle(Integer dictId) {

    Notice notice = sysNoticeRepository.findById(dictId).get();
    if (notice == null) {
      return "";
    } else {
      return notice.getTitle();
    }

  }

  /**
   * 根据字典名称和字典中的值获取对应的名称
   */
  @Override
  public String getDictsByName(String name, String val) {
    String result = getDict(CacheConstantKey.DICT_NAME + name + val);
    if (StringUtils.isNotEmpty(result)) {
      return result;
    }
    List<Dict> dicts = dictCache.getDictsByPname(name);
    for (Dict item : dicts) {
      if (item.getNum() != null && item.getNum().equals(val)) {
        result = item.getName();
        setDict(CacheConstantKey.DICT_NAME + name + val, result);
        return result;
      }
    }
    return "";

  }

  /**
   * 获取用户登录状态
   */
  @Override
  public String getStatusName(Integer status) {
    return ManagerStatusEnum.valueOf(status);
  }

  /**
   * 获取菜单状态
   */
  @Override
  public String getMenuStatusName(Integer status) {
    return MenuStatusEnum.valueOf(status);
  }

  /**
   * 查询字典
   */
  @Override
  public List<Dict> findInDict(Integer id) {
    return dictRepository.findByPid(id);

  }

  /**
   * 获取被缓存的对象(用户删除业务)
   */
  @Override
  public String getCacheObject(String para) {
    return LogObjectHolder.me().get().toString();
  }

  /**
   * 获取子部门id
   */
  @Override
  public List<Integer> getSubDeptId(Integer deptid) {

    List<Dept> depts = this.deptRepository.findByPidsLike("%[" + deptid + "]%");

    ArrayList<Integer> deptids = new ArrayList<>();

    if (depts != null && depts.size() > 0) {
      for (Dept dept : depts) {
        deptids.add(dept.getId());
      }
    }

    return deptids;
  }

  /**
   * 获取所有父部门id
   */
  @Override
  public List<Integer> getParentDeptIds(Integer deptid) {
    Dept dept = deptRepository.findById(deptid).get();
    String pids = dept.getPids();
    String[] split = pids.split(",");
    ArrayList<Integer> parentDeptIds = new ArrayList<>();
    for (String s : split) {
      parentDeptIds.add(Integer.valueOf(StrKit.removeSuffix(StrKit.removePrefix(s, "["), "]")));
    }
    return parentDeptIds;
  }


  @Override
  public List<Dict> getDicts(String pname) {
    List<Dict> result = dictCache.getDictsByPname(pname);
    return result;
  }

  @Override
  public String getCfg(String cfgName) {
    String val = get(CacheConstantKey.CFG + cfgName);
    if (StringUtils.isNotEmpty(val)) {
      return val;
    }
    val = (String) configCache.get(cfgName);
    set(CacheConstantKey.CFG + cfgName, val);
    return val;
  }

  /**
   * 获取公司名称
   */
  @Override
  public String getOrgNoName(Integer id) {
    return getDeptName(id);
  }


  /**
   * 获取性别名称
   */
  @Override
  public String getSexName(Integer sex) {
    return getDictsByName("性别", String.valueOf(sex));
  }

  @Override
  public String getCardTypeName(String cardType) {
    return getDictsByName("银行卡类型", cardType);
  }

  @Override
  public String getIdCardTypeName(String cardType) {
    return getDictsByName("证件类型", cardType);
  }

  @Override
  public String getRelationName(String relation) {
    return getDictsByName("联系人关系", relation);
  }

  @Override
  public String getCustomerTypeName(Long id) {
    return getDictsByName("客户类型", String.valueOf(id));
  }

  @Override
  public String getCustomerStatusName(Long id) {
    return getDictsByName("客户状态", String.valueOf(id));
  }

  @Override
  public String getAcctTypeName(Long acctType) {
    return getDictsByName("账户类型", String.valueOf(acctType));
  }

  @Override
  public String getBalanceTypeName(Long balanceType) {
    return getDictsByName("余额性质", String.valueOf(balanceType));
  }

  @Override
  public String getAcctStatusName(Long status) {
    return getDictsByName("账户状态", String.valueOf(status));
  }

  @Override
  public String getLoanStatusName(Long status) {
    return getDictsByName("借据状态", String.valueOf(status));
  }

  @Override
  public String getLoanVoucherTypeName(Long status) {
    return getDictsByName("借据凭证类型", String.valueOf(status));
  }

  @Override
  public String getRepayStatusName(Long status) {
    return getDictsByName("还款状态", String.valueOf(status));
  }

  @Override
  public String getAmtTypeName(Long status) {
    return getDictsByName("资金类型", String.valueOf(status));
  }

  @Override
  public String getLoanBizTypeName(Long status) {
    return getDictsByName("业务类型", String.valueOf(status));
  }

  @Override
  public String getInvestStatusName(Long status) {
    return getDictsByName("融资状态", String.valueOf(status));
  }

  @Override
  public String getInvestPlanStatusName(Long status) {
    return getDictsByName("回款状态", String.valueOf(status));
  }

  @Override
  public String getProcessStatusName(Long status) {
    return getDictsByName("处理状态", String.valueOf(status));
  }

  @Override
  public String getInOutTypeName(Long status) {
    return getDictsByName("收支用途", String.valueOf(status));
  }

  @Override
  public String getTransferTypeName(Long status) {
    return getDictsByName("转账用途", String.valueOf(status));
  }

  @Override
  public String getServiceFeeTypeName(Long id) {
    return getDictsByName("服务费收取方式", String.valueOf(id));
  }

  @Override
  public String getIsPenName(Long id) {
    return getDictsByName("是否", String.valueOf(id));
  }

  @Override
  public String getPenNumberName(Long id) {
    return getDictsByName("罚息基数", String.valueOf(id));
  }

  @Override
  public String getRepayTypeName(Long id) {
    return getDictsByName("还款方式", String.valueOf(id));
  }

  @Override
  public String getLoanTypeName(Long id) {
    return getDictsByName("贷款类型", String.valueOf(id));
  }


}
