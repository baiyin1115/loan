package com.zsy.loan.service;

import com.zsy.loan.bean.convey.LoanCalculateVo;
import com.zsy.loan.bean.entity.biz.TBizRepayPlan;
import com.zsy.loan.utils.BeanKit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2019/3/8/008.
 */
public class BeanCopyTest {

  public static void main(String[] args) {
//    TBizRepayPlan plan = TBizRepayPlan.builder().id(0L).acctDate(new Date()).build();
//    List list =  new ArrayList();
//    list.add(plan);
//
//    LoanCalculateVo vo = LoanCalculateVo.builder().id(1L).repayPlanList(list).acctDate(new Date()).build();
//
//    LoanCalculateVo result = LoanCalculateVo.builder().build();
//
//    BeanUtils.copyProperties(vo,result);
//
//    System.out.println(result.toString());
//
//    System.out.println(result.getRepayPlanList().hashCode()+"_"+vo.getRepayPlanList().hashCode());
//
//    System.out.println(vo.getRepayPlanList().equals(result.getRepayPlanList()) + "_"
//    +(vo.getRepayPlanList() == result.getRepayPlanList()));
//
//    vo.setRepayPlanList(null);
//
//    System.out.println(vo.toString());
//    System.out.println(result.toString());

    TBizRepayPlan plan = TBizRepayPlan.builder().id(0L).acctDate(new Date()).build();
    List list =  new ArrayList();
    list.add(plan);

    LoanCalculateVo vo = LoanCalculateVo.builder().id(1L).repayPlanList(list).acctDate(new Date()).build();

    LoanCalculateVo result = LoanCalculateVo.builder().build();

    BeanKit.copyProperties(vo,result);

    System.out.println(result.toString());

//    System.out.println(result.getRepayPlanList().hashCode()+"_"+vo.getRepayPlanList().hashCode());

    System.out.println(vo.getRepayPlanList().equals(result.getRepayPlanList()) + "_"
        +(vo.getRepayPlanList() == result.getRepayPlanList()));

    vo.setId(123L);
    vo.getRepayPlanList().get(0).setStatus(0L);

    System.out.println(vo.toString());
    System.out.println(result.toString());

  }

}
