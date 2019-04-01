package com.zsy.loan.service.biz.impl;

import com.zsy.loan.bean.convey.TransferInfoVo;
import com.zsy.loan.bean.entity.biz.TBizTransferVoucherInfo;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ProcessStatusEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.TransferTypeEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.dao.biz.AcctRepo;
import com.zsy.loan.dao.biz.TransferVoucherRepo;
import com.zsy.loan.service.factory.TransferPermissionFactory;
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
 * 转账服务
 *
 * @Author zhangxh
 * @Date 2019-01-18  12:27
 */
@Service
public class TransferServiceImpl extends BaseServiceImpl {

  @Resource
  TransferVoucherRepo repository;

  @Autowired
  private AcctRepo acctRepo;

  @Autowired
  private ISystemService systemService;


  public Object save(TransferInfoVo transfer, boolean b) {

    Long outAcctType = null;
    if (transfer.getOutAcctNo() != null) {
      outAcctType = acctRepo.findById(transfer.getOutAcctNo()).get().getAcctType();
    }
    Long inAcctType = null;
    if (transfer.getInAcctNo() != null) {
      inAcctType = acctRepo.findById(transfer.getInAcctNo()).get().getAcctType();
    }

    TransferPermissionFactory
        .checkPermission((outAcctType == null ? null : outAcctType.longValue()) + "_" + TransferTypeEnum.getEnumByKey(transfer.getType()) + "_" +
            (inAcctType == null ? null : inAcctType.longValue()));

    TBizTransferVoucherInfo newInfo = TBizTransferVoucherInfo.builder().build();
    BeanKit.copyProperties(transfer, newInfo);

    /**
     * 判断状态
     */
    if (b) { //修改
      TBizTransferVoucherInfo info = repository.findById(transfer.getId()).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(info.getId()));
      }
    }

    /**
     * 赋值
     */
    if (newInfo.getAcctDate() == null) {
      newInfo.setAcctDate(systemService.getSysAcctDate());
    }
    newInfo.setStatus(ProcessStatusEnum.ING.getValue()); //处理中

    if (!b) {
      newInfo.setId(IdentifyGenerated.INSTANCE.getNextId()); //修改为统一的凭证编号规则
    }
    repository.save(newInfo);

    return true;

  }


  public Page<TBizTransferVoucherInfo> getTBizTransferVouchers(Page<TBizTransferVoucherInfo> page, TransferInfoVo condition) {

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.asc("status"));
    orders.add(Order.asc("id"));
    Pageable pageable = getPageable(page, orders);

    org.springframework.data.domain.Page<TBizTransferVoucherInfo> page1 = repository
        .findAll(new Specification<TBizTransferVoucherInfo>() {

          @Override
          public Predicate toPredicate(Root<TBizTransferVoucherInfo> root, CriteriaQuery<?> criteriaQuery,
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

            if (!ObjectUtils.isEmpty(condition.getInAcctNo())) {
              list.add(criteriaBuilder.equal(root.get("inAcctNo"), condition.getInAcctNo()));
            }

            if (!ObjectUtils.isEmpty(condition.getOutAcctNo())) {
              list.add(criteriaBuilder.equal(root.get("outAcctNo"), condition.getOutAcctNo()));
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

  public String toConfirm(List<Long> transferIds) {

    StringBuilder msg = new StringBuilder();

    for (long id : transferIds) {
      /**
       * 判断状态
       */
      TBizTransferVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      msg.append("编号:" + id);
      msg.append("|金额:" + BigDecimalUtil.formatAmt(info.getAmt()));
      msg.append("|日期:" + DateTimeKit.formatDate(info.getAcctDate()));
      msg.append("|用途:" + ConstantFactory.me().getInOutTypeName(info.getType()));
      msg.append("|出账账户:" + (info.getOutAcctNo() == null ? "-" : ConstantFactory.me().getAcctName(info.getOutAcctNo())));
      msg.append("|入账账户:" + (info.getInAcctNo() == null ? "-" : ConstantFactory.me().getAcctName(info.getInAcctNo())));
      msg.append("<BR>");
    }

    msg.append("确认后不可修改！！！");

    return msg.toString();
  }

  @Transactional
  public Boolean confirm(List<Long> transferIds) {

    /**
     * 校验
     */
    toConfirm(transferIds);

    for (long id : transferIds) {
      /**
       * 判断状态
       */
      TBizTransferVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      /**
       * 调用账户模块记账
       */
      transferAccounting(info);

      /**
       * 更新状态
       */
      repository.updateStatus(id, ProcessStatusEnum.SUCCESS.getValue(), ProcessStatusEnum.ING.getValue(), systemService.getSysAcctDate());

    }
    return true;
  }

  @Transactional
  public Boolean cancel(List<Long> transferIds) {

    for (long id : transferIds) {
      /**
       * 判断账户状态
       */
      TBizTransferVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.SUCCESS.getValue())) { //成功
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      /**
       * 调用账户模块记账
       */
      info.setAmt(info.getAmt().negate()); //金额设置成负值
      transferAccounting(info);

      repository.updateStatus(id, ProcessStatusEnum.CANCEL.getValue(), ProcessStatusEnum.SUCCESS.getValue(), systemService.getSysAcctDate());

    }
    return true;
  }

  /**
   * 转账调用记账接口共同
   */
  private void transferAccounting(TBizTransferVoucherInfo info) {
    String key = null;
    if (TransferTypeEnum.REGISTER.getValue() == info.getType()) {
      key = LoanBizTypeEnum.FUNDS_CHECK_IN + "_" + info.getType();
    } else if (TransferTypeEnum.WITHDRAW.getValue() == info.getType()) {
      key = LoanBizTypeEnum.WITHDRAW + "_" + info.getType();
    } else {
      key = LoanBizTypeEnum.TRANSFER + "_" + info.getType();
    }
    executeAccounting(key, info);
  }

  @Transactional
  public Boolean delete(List<Long> transferIds) {

    for (long id : transferIds) {
      /**
       * 判断账户状态
       */
      TBizTransferVoucherInfo info = repository.findById(id).get();
      if (!info.getStatus().equals(ProcessStatusEnum.ING.getValue())) { //处理中
        throw new LoanException(BizExceptionEnum.STATUS_ERROR, String.valueOf(id));
      }

      repository.deleteById(id);

    }
    return true;

  }

}
