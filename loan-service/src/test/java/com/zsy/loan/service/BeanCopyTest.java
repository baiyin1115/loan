package com.zsy.loan.service;

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

//    TBizRepayPlan plan = TBizRepayPlan.builder().id(0L).acctDate(new Date()).build();
//    List list =  new ArrayList();
//    list.add(plan);
//
//    LoanCalculateVo vo = LoanCalculateVo.builder().id(1L).repayPlanList(list).acctDate(new Date()).build();
//
//    LoanCalculateVo result = LoanCalculateVo.builder().build();
//
//    BeanKit.copyProperties(vo,result);
//
//    System.out.println(result.toString());
//
////    System.out.println(result.getRepayPlanList().hashCode()+"_"+vo.getRepayPlanList().hashCode());
//
//    System.out.println(vo.getRepayPlanList().equals(result.getRepayPlanList()) + "_"
//        +(vo.getRepayPlanList() == result.getRepayPlanList()));
//
//    vo.setId(123L);
//    vo.getRepayPlanList().get(0).setStatus(0L);
//
//    System.out.println(vo.toString());
//    System.out.println(result.toString());

//    String str2 = "SEUCalvin";//新加的一行代码，其余不变
//    String str1 = new String("SEU")+ new String("Calvin");
//    System.out.println(str1.intern() == str1);
//    System.out.println(str1 == "SEUCalvin");

//    String str1 = new String("SEU")+ new String("Calvin");
//    str1.intern();
////    System.out.println(str1.intern() == str1);
//    System.out.println(str1 == "SEUCalvin");

//    String str1 = new String("SEU") + new String("Calvin");
//    str1.intern();
////    System.out.println(str1.intern() == str1);
//    String str2 = "SEUCalvin";
//    System.out.println(str1 == str2);

//    String a3 = new String("AA");    //在堆上创建对象AA
//    a3 = a3.intern(); //在常量池上创建对象AA的引用
//    String a4 = "AA"; //常量池上存在引用AA，直接返回该引用指向的堆空间对象，即a3
//    System.out.println(a3 == a4); //true,如果这个例子不理解，请看完整篇文章再回来看这里

    String str1 = new String("SEU1");
    String str2 = "SEU1";

    System.out.println(str1 == str2);

    String str4 = "SEU2";
    String str3 = new String("SEU2");

    System.out.println(str3 == str4);

    String str5 = "SEU3";
    String str6 = "SE" + "U3";
    System.out.println(str5 == str6);

    String str7 = new String("SE2") + new String("U4");
    str7.intern();
    String str8 = "SE2U4";
    System.out.println(str7 == str8);

    String str9 = new String("SE9") + new String("U10");
    String str10 = str9.intern();
    System.out.println(str9 == str10);

    String str11 = str2 + str4;
    String str12 ="SEU1SEU2";
    System.out.println(str11 == str12);

    String str13 = "SEUb" + "SEUm";
    String str14 ="SEUbSEUm";
    System.out.println(str13 == str14);


  }

}
