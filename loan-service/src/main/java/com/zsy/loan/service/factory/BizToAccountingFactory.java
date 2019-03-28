package com.zsy.loan.service.factory;

import com.zsy.loan.bean.convey.AccountingDetailVo;
import com.zsy.loan.bean.convey.AccountingMainVo;
import com.zsy.loan.bean.entity.biz.IBizInfo;
import com.zsy.loan.bean.entity.biz.TBizInOutVoucherInfo;
import com.zsy.loan.bean.entity.biz.TBizTransferVoucherInfo;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AmtTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.BalDirEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InOutTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.TransferTypeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务bean转成记账bean
 *
 * @Author zhangxh
 * @Date 2019-01-30  9:59
 */
@Slf4j
public class BizToAccountingFactory {

  /**
   * 转换工厂
   */
  public static Map<String, Function<IBizInfo, AccountingMainVo>> maps = new HashMap<>();

  static {
    /**
     * 资金登记
     */
    maps.put(LoanBizTypeEnum.FUNDS_CHECK_IN + "_" + TransferTypeEnum.REGISTER, BizToAccountingFactory::funds_check_in); //1:资金登记

    /**
     * 转账
     */
    maps.put(LoanBizTypeEnum.TRANSFER + "_" + TransferTypeEnum.DISTRIBUTION, BizToAccountingFactory::transfer); //3:资金分拨
    maps.put(LoanBizTypeEnum.TRANSFER + "_" + TransferTypeEnum.SUPPLEMENT, BizToAccountingFactory::transfer); //4:补偿
    maps.put(LoanBizTypeEnum.TRANSFER + "_" + TransferTypeEnum.DISCOUNT, BizToAccountingFactory::transfer); //5:贴息
    maps.put(LoanBizTypeEnum.TRANSFER + "_" + TransferTypeEnum.OTHER, BizToAccountingFactory::transfer); //6:其他

    /**
     * 提现
     */
    maps.put(LoanBizTypeEnum.WITHDRAW + "_" + TransferTypeEnum.WITHDRAW, BizToAccountingFactory::withdraw); //2:资金提出

    /**
     * 收入
     */
    maps.put(LoanBizTypeEnum.IN + "_" + InOutTypeEnum.PUBLIC_IN, BizToAccountingFactory::in); //6:对公收入
    maps.put(LoanBizTypeEnum.IN + "_" + InOutTypeEnum.INTEREST, BizToAccountingFactory::in); //7:利息收入

    /**
     * 支出
     */
    maps.put(LoanBizTypeEnum.OUT + "_" + InOutTypeEnum.GENERAL, BizToAccountingFactory::out); //1:一般预算支出
    maps.put(LoanBizTypeEnum.OUT + "_" + InOutTypeEnum.OFFICE, BizToAccountingFactory::out); //2:办公支出
    maps.put(LoanBizTypeEnum.OUT + "_" + InOutTypeEnum.OTHER, BizToAccountingFactory::out); //3:其他支出
    maps.put(LoanBizTypeEnum.OUT + "_" + InOutTypeEnum.SPLITTING, BizToAccountingFactory::out); //4:股东分润
    maps.put(LoanBizTypeEnum.OUT + "_" + InOutTypeEnum.PUBLIC_OUT, BizToAccountingFactory::out); //5:对公支出

    /**
     * 放款
     */
    maps.put(LoanBizTypeEnum.PUT + "_" + "", BizToAccountingFactory::put);

    /**
     * 还款
     */
    maps.put(LoanBizTypeEnum.REPAY + "_" + "", BizToAccountingFactory::put);

    /**
     * 融资
     */
    maps.put(LoanBizTypeEnum.INVEST + "_" + "", BizToAccountingFactory::invest);

    /**
     * 撤资
     */
    maps.put(LoanBizTypeEnum.DIVESTMENT + "_" + "", BizToAccountingFactory::divestment);

    /**
     * 结转
     */
    maps.put(LoanBizTypeEnum.SETTLEMENT + "_" + "", BizToAccountingFactory::settlement);

    /**
     * 代偿
     */
    maps.put(LoanBizTypeEnum.COMPENSATION + "_" + "", BizToAccountingFactory::compensation);

    /**
     * 提前还款
     */
    maps.put(LoanBizTypeEnum.PREPAYMENT + "_" + "", BizToAccountingFactory::prepayment);

    /**
     * 部分还款
     */
    maps.put(LoanBizTypeEnum.PREPAYMENT + "_" + "", BizToAccountingFactory::part_repayment);

    /**
     * 部分撤资
     */
    maps.put(LoanBizTypeEnum.PART_DIVESTMENT + "_" + "", BizToAccountingFactory::part_divestment);

  }






  private static AccountingMainVo funds_check_in(IBizInfo iBizInfo) {

    /**
     * 登记就一笔流水
     */
    TBizTransferVoucherInfo info = (TBizTransferVoucherInfo) iBizInfo;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.FUNDS_CHECK_IN.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(1);

    AccountingDetailVo detailVo = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getInAcctNo())
        .balDir(BalDirEnum.ADD.getValue()).build();
    detailVoList.add(detailVo);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo transfer(IBizInfo iBizInfo) {
    /**
     * 转账两笔
     */
    TBizTransferVoucherInfo info = (TBizTransferVoucherInfo) iBizInfo;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.TRANSFER.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(2);

    AccountingDetailVo detailVoOut = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getOutAcctNo())
        .balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoIn = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getInAcctNo())
        .balDir(BalDirEnum.ADD.getValue()).build();
    detailVoList.add(detailVoOut);
    detailVoList.add(detailVoIn);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo withdraw(IBizInfo iBizInfo) {

    /**
     * 提现一笔
     */
    TBizTransferVoucherInfo info = (TBizTransferVoucherInfo) iBizInfo;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.WITHDRAW.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(1);

    AccountingDetailVo detailVo = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getOutAcctNo())
        .balDir(BalDirEnum.SUB.getValue()).build();
    detailVoList.add(detailVo);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo in(IBizInfo iBizInfo) {
    /**
     * 收入一笔
     */
    TBizInOutVoucherInfo info = (TBizInOutVoucherInfo) iBizInfo;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.IN.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(1);

    AccountingDetailVo detailVo = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getAcctNo())
        .balDir(BalDirEnum.ADD.getValue()).build();
    detailVoList.add(detailVo);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo out(IBizInfo iBizInfo) {
    /**
     * 支出一笔
     */
    TBizInOutVoucherInfo info = (TBizInOutVoucherInfo) iBizInfo;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.OUT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(1);

    AccountingDetailVo detailVo = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getAcctNo())
        .balDir(BalDirEnum.SUB.getValue()).build();
    detailVoList.add(detailVo);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo put(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo invest(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo divestment(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo settlement(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo compensation(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo prepayment(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo part_repayment(IBizInfo iBizInfo) {
    return null;
  }

  private static AccountingMainVo part_divestment(IBizInfo iBizInfo) {
    return null;
  }


















}
