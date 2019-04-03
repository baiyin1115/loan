package com.zsy.loan.service.factory;

import com.zsy.loan.bean.convey.AccountingDetailVo;
import com.zsy.loan.bean.convey.AccountingMainVo;
import com.zsy.loan.bean.convey.IBizToAcct;
import com.zsy.loan.bean.convey.InvestCalculateVo;
import com.zsy.loan.bean.convey.LoanBreachVo;
import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.convey.LoanRepayPlanVo;
import com.zsy.loan.bean.entity.biz.TBizInOutVoucherInfo;
import com.zsy.loan.bean.entity.biz.TBizInvestInfo;
import com.zsy.loan.bean.entity.biz.TBizTransferVoucherInfo;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AcctTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.AmtTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.BalDirEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.InOutTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.LoanBizTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.ServiceFeeTypeEnum;
import com.zsy.loan.bean.enumeration.BizTypeEnum.TransferTypeEnum;
import com.zsy.loan.utils.BigDecimalUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 业务bean转成记账bean
 *
 * @Author zhangxh
 * @Date 2019-01-30  9:59
 */
@Slf4j
@Service
public class BizToAccountingFactory {

  /**
   * 转换工厂
   */
  public static Map<String, Function<IBizToAcct, AccountingMainVo>> maps = new HashMap<>();

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
    maps.put(LoanBizTypeEnum.TRANSFER + "_" + TransferTypeEnum.DISCOUNT, BizToAccountingFactory::transfer); //5:减免
    maps.put(LoanBizTypeEnum.TRANSFER + "_" + TransferTypeEnum.OTHER, BizToAccountingFactory::transfer); //6:其他

    /**
     * 资金提出
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
    maps.put(LoanBizTypeEnum.REPAY + "_" + "", BizToAccountingFactory::repay);

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

  private static AccountingMainVo funds_check_in(IBizToAcct iacct) {

    /**
     * 登记 1笔
     *
     * 暂收户(+)|发生额|资金|资金登记
     */
    TBizTransferVoucherInfo info = (TBizTransferVoucherInfo) iacct;

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

  private static AccountingMainVo transfer(IBizToAcct iacct) {
    /**
     * 转账 2笔
     *
     * 入账账户(+)|发生额|相关记账类型|相关业务类型
     * 出账账户(-)|发生额|相关记账类型|相关业务类型
     */
    TBizTransferVoucherInfo info = (TBizTransferVoucherInfo) iacct;

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

  private static AccountingMainVo withdraw(IBizToAcct iacct) {

    /**
     * 资金提出 1笔
     *
     * 暂付户(-)|发生额|资金|资金提出
     */
    TBizTransferVoucherInfo info = (TBizTransferVoucherInfo) iacct;

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

  private static AccountingMainVo in(IBizToAcct iacct) {
    /**
     * 收入 2笔
     *
     * 暂收(-)|发生额|资金|收入
     * 公司账户(+)|发生额|资金|收入
     */
    TBizInOutVoucherInfo info = (TBizInOutVoucherInfo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.IN.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(2);

    AccountingDetailVo detailVoSub = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue())
        .acctType(AcctTypeEnum.INTERIM_IN.getValue()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue())
        .acctNo(info.getAcctNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub);
    detailVoList.add(detailVoAdd);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo out(IBizToAcct iacct) {
    /**
     * 支出 2笔
     *
     * 公司账户(-)|发生额|资金|支出
     * 暂付(+)|发生额|资金|支出
     */
    TBizInOutVoucherInfo info = (TBizInOutVoucherInfo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.OUT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(2);

    AccountingDetailVo detailVoSub = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue()).acctNo(info.getAcctNo())
        .balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd = AccountingDetailVo.builder().amt(info.getAmt()).amtType(AmtTypeEnum.FUNDS.getValue())
        .acctType(AcctTypeEnum.INTERIM_OUT.getValue()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub);
    detailVoList.add(detailVoAdd);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo put(IBizToAcct iacct) {
    /**
     * 放款 6笔
     *
     * 借款人账户 (+)|本金|本金|放款
     * 公司卡账户(-)|本金|本金|放款
     *
     * 借款人账户 (-)|服务费|服务费|放款
     * 公司卡账户(+)|服务费|服务费|放款
     *
     * 借款人账户 (-)|放款金额|资金|放款
     * 暂付(+)|放款金额|资金|放款
     */
    LoanCalculateVo info = (LoanCalculateVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.PUT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(1);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getLendingAcct()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    if (info.getServiceFeeType().equals(ServiceFeeTypeEnum.FIRST.getValue()) && info.getSchdServFee().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      //首期登记服务费
      AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getSchdServFee()).amtType(AmtTypeEnum.SERVICE_FEE.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getSchdServFee()).amtType(AmtTypeEnum.SERVICE_FEE.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub2);
      detailVoList.add(detailVoAdd2);
    }

    AccountingDetailVo detailVoSub3 = AccountingDetailVo.builder().amt(info.getLendingAmt()).amtType(AmtTypeEnum.FUNDS.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd3 = AccountingDetailVo.builder().amt(info.getLendingAmt()).amtType(AmtTypeEnum.FUNDS.getValue())
        .acctType(AcctTypeEnum.INTERIM_OUT.getValue()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub3);
    detailVoList.add(detailVoAdd3);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo invest(IBizToAcct iacct) {

    /**
     * 融资 2笔
     *
     * 融资人账户(-)|发生额|本金|融资
     * 公司账户(+)|发生额|本金|融资
     *
     */

    InvestCalculateVo info = (InvestCalculateVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.INVEST.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(1);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo divestment(IBizToAcct iacct) {

    /**
     * 撤资 8笔
     *
     * 融资人账户(+)|发生额|本金|撤资
     * 公司账户(-)|发生额|本金|撤资
     *
     * 融资人账户(+)|发生额|利息|撤资
     * 公司账户(-)|发生额|利息|撤资
     *
     * 融资人账户(+)|发生额|补偿|撤资
     * 公司账户(-)|发生额|补偿|撤资
     *
     * 融资人账户(-)|发生额|资金|撤资
     * 暂付(+)|发生额|资金|撤资
     *
     */

    InvestCalculateVo info = (InvestCalculateVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.DIVESTMENT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(4);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getDivestmentAmt()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getDivestmentAmt()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getDivestmentInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getDivestmentInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub2);
    detailVoList.add(detailVoAdd2);

    AccountingDetailVo detailVoSub3 = AccountingDetailVo.builder().amt(info.getDivestmentWavAmt()).amtType(AmtTypeEnum.RECOMPENSE.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd3 = AccountingDetailVo.builder().amt(info.getDivestmentWavAmt()).amtType(AmtTypeEnum.RECOMPENSE.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub3);
    detailVoList.add(detailVoAdd3);

    BigDecimal out = BigDecimalUtil.add(info.getDivestmentAmt(), info.getDivestmentInterest(), info.getDivestmentWavAmt());
    AccountingDetailVo detailVoSub4 = AccountingDetailVo.builder().amt(out).amtType(AmtTypeEnum.FUNDS.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd4 = AccountingDetailVo.builder().amt(out).amtType(AmtTypeEnum.FUNDS.getValue())
        .acctType(AcctTypeEnum.INTERIM_OUT.getValue()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub4);
    detailVoList.add(detailVoAdd4);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo part_divestment(IBizToAcct iacct) {

    /**
     * 部分撤资 8笔
     *
     * 融资人账户(+)|发生额|本金|撤资
     * 公司账户(-)|发生额|本金|撤资
     *
     * 融资人账户(+)|发生额|利息|撤资
     * 公司账户(-)|发生额|利息|撤资
     *
     * 融资人账户(+)|发生额|补偿|撤资
     * 公司账户(-)|发生额|补偿|撤资
     *
     * 融资人账户(-)|发生额|资金|撤资
     * 暂付(+)|发生额|资金|撤资
     *
     */

    InvestCalculateVo info = (InvestCalculateVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.PART_DIVESTMENT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(4);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getDivestmentAmt()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getDivestmentAmt()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    //用计提利息
    AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getTotAccruedInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getTotAccruedInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub2);
    detailVoList.add(detailVoAdd2);

    AccountingDetailVo detailVoSub3 = AccountingDetailVo.builder().amt(info.getDivestmentWavAmt()).amtType(AmtTypeEnum.RECOMPENSE.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd3 = AccountingDetailVo.builder().amt(info.getDivestmentWavAmt()).amtType(AmtTypeEnum.RECOMPENSE.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub3);
    detailVoList.add(detailVoAdd3);

    BigDecimal out = BigDecimalUtil.add(info.getDivestmentAmt(), info.getDivestmentInterest(), info.getDivestmentWavAmt());
    AccountingDetailVo detailVoSub4 = AccountingDetailVo.builder().amt(out).amtType(AmtTypeEnum.FUNDS.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd4 = AccountingDetailVo.builder().amt(out).amtType(AmtTypeEnum.FUNDS.getValue())
        .acctType(AcctTypeEnum.INTERIM_OUT.getValue()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub4);
    detailVoList.add(detailVoAdd4);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo settlement(IBizToAcct iacct) {

    /**
     * 结转 4笔
     *
     * 融资人账户(+)|发生额|本金|结转
     * 公司账户(-)|发生额|本金|结转
     *
     * 融资人账户(+)|发生额|利息|结转
     * 公司账户(-)|发生额|利息|结转
     *
     */

    TBizInvestInfo info = (TBizInvestInfo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.SETTLEMENT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(2);

    BigDecimal prin = BigDecimalUtil.sub(info.getPrin(), info.getTotPaidPrin());
    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(prin).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(prin).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getTotAccruedInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
        .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getTotAccruedInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub2);
    detailVoList.add(detailVoAdd2);

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo repay(IBizToAcct iacct) {

    /**
     * 还款 10笔
     *
     * 借款人账户(-)|发生额|本金|还款
     * 公司账户(+)|发生额|本金|还款
     *
     * 借款人账户(-)|发生额|利息|还款
     * 公司账户(+)|发生额|利息|还款
     *
     * 借款人账户(-)|发生额|服务费|还款
     * 公司账户(+)|发生额|服务费|还款
     *
     * 借款人账户(-)|发生额|罚息|还款
     * 公司账户(+)|发生额|罚息|还款
     *
     * 公司账户(-)|发生额|减免|还款
     * 借款人账户(+)|发生额|减免|还款
     */

    LoanRepayPlanVo info = (LoanRepayPlanVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo())//.voucherNo(info.getId())
        .voucherPlanNo(info.getId()).type(LoanBizTypeEnum.REPAY.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(4);

    if (info.getCurrentPrin().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getCurrentPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getCurrentPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
          .acctNo(info.getInAcctNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub1);
      detailVoList.add(detailVoAdd1);
    }

    if (info.getCurrentInterest().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getCurrentInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getCurrentInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
          .acctNo(info.getInAcctNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub2);
      detailVoList.add(detailVoAdd2);
    }

    if (info.getCurrentServFee().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub3 = AccountingDetailVo.builder().amt(info.getCurrentServFee()).amtType(AmtTypeEnum.SERVICE_FEE.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd3 = AccountingDetailVo.builder().amt(info.getCurrentServFee()).amtType(AmtTypeEnum.SERVICE_FEE.getValue())
          .acctNo(info.getInAcctNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub3);
      detailVoList.add(detailVoAdd3);
    }

    if (info.getCurrentPen().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub4 = AccountingDetailVo.builder().amt(info.getCurrentPen()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd4 = AccountingDetailVo.builder().amt(info.getCurrentPen()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .acctNo(info.getInAcctNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub4);
      detailVoList.add(detailVoAdd4);
    }

    if (info.getCurrentWav().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub5 = AccountingDetailVo.builder().amt(info.getCurrentWav()).amtType(AmtTypeEnum.REDUCE.getValue())
          .acctNo(info.getInAcctNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd5 = AccountingDetailVo.builder().amt(info.getCurrentWav()).amtType(AmtTypeEnum.REDUCE.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub5);
      detailVoList.add(detailVoAdd5);
    }

    main.setDetail(detailVoList);

    return main;


  }


  private static AccountingMainVo compensation(IBizToAcct iacct) {

    /**
     * 代偿 8笔
     *
     * 代偿账户(-)|发生额|本金|代偿
     * 借款人账户(+)|发生额|本金|代偿
     *
     * 代偿账户(-)|发生额|罚息|代偿
     * 借款人账户(+)|发生额|罚息|代偿
     *
     * 借款人账户(-)|发生额|本金|代偿
     * 公司账户(+)|发生额|本金|代偿
     *
     * 借款人账户(-)|发生额|罚息|代偿
     * 公司账户(+)|发生额|罚息|代偿
     *
     */

    LoanBreachVo info = (LoanBreachVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.COMPENSATION.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(4);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getCurrentBreachPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getCompensationAcct()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getCurrentBreachPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    if (info.getCurrentBreachFee().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getCurrentBreachFee()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .acctNo(info.getCompensationAcct()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getCurrentBreachFee()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub2);
      detailVoList.add(detailVoAdd2);
    }

    AccountingDetailVo detailVoSub3 = AccountingDetailVo.builder().amt(info.getCurrentBreachPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd3 = AccountingDetailVo.builder().amt(info.getCurrentBreachPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub3);
    detailVoList.add(detailVoAdd3);

    if (info.getCurrentBreachFee().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub4 = AccountingDetailVo.builder().amt(info.getCurrentBreachFee()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd4 = AccountingDetailVo.builder().amt(info.getCurrentBreachFee()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub4);
      detailVoList.add(detailVoAdd4);
    }

    main.setDetail(detailVoList);

    return main;

  }

  private static AccountingMainVo prepayment(IBizToAcct iacct) {

    /**
     * 提前还款 10笔
     *
     * 借款人账户(-)|发生额|本金|提前还款
     * 公司账户(+)|发生额|本金|提前还款
     *
     * 借款人账户(-)|发生额|利息|提前还款
     * 公司账户(+)|发生额|利息|提前还款
     *
     * 借款人账户(-)|发生额|服务费|提前还款
     * 公司账户(+)|发生额|服务费|提前还款
     *
     * 借款人账户(-)|发生额|罚息|提前还款
     * 公司账户(+)|发生额|罚息|提前还款
     *
     * 公司账户(-)|发生额|减免|还款
     * 借款人账户(+)|发生额|减免|还款
     */

    LoanCalculateVo info = (LoanCalculateVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.PREPAYMENT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(4);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getCurrentRepayPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getCurrentRepayPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    if (info.getRepayInterest().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getRepayInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getRepayInterest()).amtType(AmtTypeEnum.INTEREST.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub2);
      detailVoList.add(detailVoAdd2);
    }

    if (info.getRepayServFee().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub3 = AccountingDetailVo.builder().amt(info.getRepayServFee()).amtType(AmtTypeEnum.SERVICE_FEE.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd3 = AccountingDetailVo.builder().amt(info.getRepayServFee()).amtType(AmtTypeEnum.SERVICE_FEE.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub3);
      detailVoList.add(detailVoAdd3);
    }

    if (info.getRepayPen().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub4 = AccountingDetailVo.builder().amt(info.getRepayPen()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd4 = AccountingDetailVo.builder().amt(info.getRepayPen()).amtType(AmtTypeEnum.BREACH_INTEREST.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub4);
      detailVoList.add(detailVoAdd4);
    }

    if (info.getCurrentRepayWav().compareTo(BigDecimal.valueOf(0.00)) != 0) {
      AccountingDetailVo detailVoSub5 = AccountingDetailVo.builder().amt(info.getCurrentRepayWav()).amtType(AmtTypeEnum.REDUCE.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd5 = AccountingDetailVo.builder().amt(info.getCurrentRepayWav()).amtType(AmtTypeEnum.REDUCE.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub5);
      detailVoList.add(detailVoAdd5);
    }

    main.setDetail(detailVoList);

    return main;
  }

  private static AccountingMainVo part_repayment(IBizToAcct iacct) {

    /**
     * 部分还款 4笔
     *
     * 借款人账户(-)|发生额|本金|部分还款
     * 公司账户(+)|发生额|本金|部分还款
     *
     * 借款人账户(-)|-退回金额|利息|部分还款
     * 公司账户(+)|-退回金额|利息|部分还款
     *
     */

    LoanCalculateVo info = (LoanCalculateVo) iacct;

    AccountingMainVo main = AccountingMainVo.builder().acctDate(info.getAcctDate()).orgNo(info.getOrgNo()).voucherNo(info.getId())
        .type(LoanBizTypeEnum.PART_REPAYMENT.getValue())
        .build();

    List<AccountingDetailVo> detailVoList = new ArrayList<>(4);

    AccountingDetailVo detailVoSub1 = AccountingDetailVo.builder().amt(info.getCurrentRepayPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
    AccountingDetailVo detailVoAdd1 = AccountingDetailVo.builder().amt(info.getCurrentRepayPrin()).amtType(AmtTypeEnum.CAPITAL.getValue())
        .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

    detailVoList.add(detailVoSub1);
    detailVoList.add(detailVoAdd1);

    if (info.getBackAmt().compareTo(BigDecimal.valueOf(0.00)) != 0) { //利息为负数
      AccountingDetailVo detailVoSub2 = AccountingDetailVo.builder().amt(info.getBackAmt().negate()).amtType(AmtTypeEnum.INTEREST.getValue())
          .custNo(info.getCustNo()).balDir(BalDirEnum.SUB.getValue()).build();
      AccountingDetailVo detailVoAdd2 = AccountingDetailVo.builder().amt(info.getBackAmt().negate()).amtType(AmtTypeEnum.INTEREST.getValue())
          .acctNo(info.getLendingAcct()).balDir(BalDirEnum.ADD.getValue()).build();

      detailVoList.add(detailVoSub2);
      detailVoList.add(detailVoAdd2);
    }

    main.setDetail(detailVoList);

    return main;
  }


}
