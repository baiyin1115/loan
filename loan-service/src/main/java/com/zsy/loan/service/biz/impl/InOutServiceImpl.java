package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.InOutInfoVo;
import com.zsy.loan.bean.entity.biz.TBizInOutVoucherInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ProcessStatusEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.biz.InOutVoucherInfoRepo;
import com.zsy.loan.service.sequence.IdentifyGenerated;
import com.zsy.loan.service.system.ISystemService;
import com.zsy.loan.service.system.impl.ConstantFactory;
import com.zsy.loan.utils.BeanKit;
import com.zsy.loan.utils.BigDecimalUtil;
import com.zsy.loan.utils.DateTimeKit;
import com.zsy.loan.utils.DateUtil;
import com.zsy.loan.utils.factory.Page;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 收支服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class InOutServiceImpl extends BaseServiceImpl {

  @Resource
  InOutVoucherInfoRepo repository;

  @Autowired
  private ISystemService systemService;


  public Object save(InOutInfoVo inOut, boolean b) {

    /**
     * 判断状态
     */
    if (b) { //修改
      TBizInOutVoucherInfo info = repository.findById(inOut.getId()).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(info.getId()));
      }
    }

    TBizInOutVoucherInfo newInfo = TBizInOutVoucherInfo.builder().build();
    BeanKit.copyProperties(inOut, newInfo);

    /**
     * 赋值
     */
    if (newInfo.getAcctDate() == null) {
      newInfo.setAcctDate(systemService.getSysAcctDate());
    }
    newInfo.setStatus(ProcessStatusEnum.ING.getValue()); //处理中

    if(!b){
      newInfo.setId(IdentifyGenerated.INSTANCE.getNextId()); //修改为统一的凭证编号规则
    }

    repository.save(newInfo);

    return true;

  }

  public Page<TBizInOutVoucherInfo> getTBizInOutVouchers(Page<TBizInOutVoucherInfo> page, InOutInfoVo condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.asc("status"));
    orders.add(Order.asc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizInOutVoucherInfo> page1 = repository
        .findAll(new Specification<TBizInOutVoucherInfo>() {

          @Override
          public Predicate toPredicate(Root<TBizInOutVoucherInfo> root, CriteriaQuery<?> criteriaQuery,
              CriteriaBuilder criteriaBuilder) {

            List<Predicate> list = new ArrayList<Predicate>();

            // 设置有权限的公司
            setOrgList(condition.getOrgNo(), root.get("orgNo"), criteriaBuilder, list);

            if (!ObjectUtils.isEmpty(condition.getOrgNo())) {
              list.add(criteriaBuilder.equal(root.get("orgNo"), condition.getOrgNo()));
            }

            if (!ObjectUtils.isEmpty(condition.getType())) {
              list.add(criteriaBuilder.equal(root.get("type"), condition.getType()));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryBeginDate())) {
              list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryBeginDate())));
            }

            if (!ObjectUtils.isEmpty(condition.getQueryEndDate())) {
              list.add(criteriaBuilder.lessThanOrEqualTo(root.get("acctDate"), DateUtil.parseDate(condition.getQueryEndDate())));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
          }
        }, pageable);

    page.setTotal(Integer.valueOf(page1.getTotalElements() + ""));
    page.setRecords(page1.getContent());
    return page;
  }

  public String toConfirm(List<Long> inOutIds) {

    StringBuilder msg = new StringBuilder();

    for (long id : inOutIds) {
      /**
       * 判断状态
       */
      TBizInOutVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      msg.append("编号:" + id);
      msg.append("|金额:" + BigDecimalUtil.formatAmt(info.getAmt()));
      msg.append("|日期:" + DateTimeKit.formatDate(info.getAcctDate()));
      msg.append("|用途:" + ConstantFactory.me().getInOutTypeName(info.getType()));
      msg.append("|账户:" + ConstantFactory.me().getAcctName(info.getAcctNo()));
      msg.append("<BR>");
    }

    msg.append("确认后不可修改！！！");

    return msg.toString();
  }

  @Transactional
  public Boolean confirm(List<Long> inOutIds) {

    /**
     * 校验
     */
    toConfirm(inOutIds);

    for (long id : inOutIds) {
      /**
       * 判断状态
       */
      TBizInOutVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      /**
       * 调用账户模块记账
       */
      //TODO

      repository.updateStatus(id, ProcessStatusEnum.SUCCESS.getValue(), ProcessStatusEnum.ING.getValue(), systemService.getSysAcctDate());

    }
    return true;
  }

  @Transactional
  public Boolean cancel(List<Long> inOutIds) {

    for (long id : inOutIds) {
      /**
       * 判断账户状态
       */
      TBizInOutVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.SUCCESS.getValue())) { //成功
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      /**
       * 调用账户模块记账
       */
      //TODO

      repository.updateStatus(id, ProcessStatusEnum.CANCEL.getValue(), ProcessStatusEnum.SUCCESS.getValue(), systemService.getSysAcctDate());

    }
    return true;
  }

  @Transactional
  public Boolean delete(List<Long> inOutIds) {

    for (long id : inOutIds) {
      /**
       * 判断账户状态
       */
      TBizInOutVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      repository.deleteById(id);

    }
    return true;

  }
}
