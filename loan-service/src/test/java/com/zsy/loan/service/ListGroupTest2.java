package com.zsy.loan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zsy.loan.bean.convey.AccountingDetailVo;
import com.zsy.loan.utils.CollectorsUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/3/29/029.
 */
public class ListGroupTest2 {
  public static void main(String[] args) {

    List<AccountingDetailVo> voList = new ArrayList<>();
    AccountingDetailVo vo1 = AccountingDetailVo.builder().acctNo(1L).balDir("-").amt(BigDecimal.valueOf(500.22)).build();
    AccountingDetailVo vo2 = AccountingDetailVo.builder().acctNo(2L).balDir("+").amt(BigDecimal.valueOf(500.33)).build();
    AccountingDetailVo vo3 = AccountingDetailVo.builder().acctNo(1L).balDir("-").amt(BigDecimal.valueOf(500.44)).build();
    AccountingDetailVo vo4 = AccountingDetailVo.builder().acctNo(2L).balDir("+").amt(BigDecimal.valueOf(500.55)).build();
    AccountingDetailVo vo5 = AccountingDetailVo.builder().acctNo(3L).balDir("+").amt(BigDecimal.valueOf(500.66)).build();
    voList.add(vo1);
    voList.add(vo2);
    voList.add(vo3);
    voList.add(vo4);
    voList.add(vo5);
    TreeMap<Long, BigDecimal> resultList333 = voList.stream().collect(
        Collectors.groupingBy(AccountingDetailVo::getAcctNo,TreeMap::new, CollectorsUtils.summingBigDecimal(AccountingDetailVo::getAmtByBalDir)));

    System.out.println(JSON.toJSONString(resultList333, SerializerFeature.PrettyFormat));

  }

  public static void aaa(){
//    List<Coupon> couponList = new ArrayList<>();
//    Coupon coupon1 = new Coupon(1,100,"优惠券1");
//    Coupon coupon2 = new Coupon(2,200,"优惠券2");
//    Coupon coupon3 = new Coupon(3,300,"优惠券3");
//    Coupon coupon4 = new Coupon(3,400,"优惠券4");
//    couponList.add(coupon1);
//    couponList.add(coupon2);
//    couponList.add(coupon3);
//    couponList.add(coupon4);
//
//    Map<Integer, List<String>> resultList = couponList.stream().collect(
//        Collectors.groupingBy(Coupon::getCouponId,Collectors.mapping(Coupon::getName,Collectors.toList())));
//    System.out.println(JSON.toJSONString(resultList, SerializerFeature.PrettyFormat));

    List<AccountingDetailVo> voList = new ArrayList<>();
    AccountingDetailVo vo1 = AccountingDetailVo.builder().acctNo(1L).balDir("+").amt(BigDecimal.valueOf(500.00)).build();
    AccountingDetailVo vo2 = AccountingDetailVo.builder().acctNo(2L).balDir("+").amt(BigDecimal.valueOf(500.00)).build();
    AccountingDetailVo vo3 = AccountingDetailVo.builder().acctNo(1L).balDir("+").amt(BigDecimal.valueOf(500.00)).build();
    AccountingDetailVo vo4 = AccountingDetailVo.builder().acctNo(1L).balDir("+").amt(BigDecimal.valueOf(500.00)).build();

    voList.add(vo1);
    voList.add(vo2);
    voList.add(vo3);
    voList.add(vo4);

    TreeMap<Long, DoubleSummaryStatistics> resultList111 = voList.stream().collect(
        Collectors.groupingBy(AccountingDetailVo::getAcctNo,TreeMap::new,Collectors.summarizingDouble(AccountingDetailVo::getAmtByBalDirToDouble)));

    System.out.println(JSON.toJSONString(resultList111, SerializerFeature.PrettyFormat));

    TreeMap<Long, List<AccountingDetailVo>> resultList222 = voList.stream().collect(
        Collectors.groupingBy(AccountingDetailVo::getAcctNo,TreeMap::new,Collectors.toList()));

    TreeMap<Long, BigDecimal> acctMap = new TreeMap<>();
    for (Entry<Long, List<AccountingDetailVo>> entry:resultList222.entrySet()) {
      acctMap.put(entry.getKey(),
          entry.getValue().stream().map(AccountingDetailVo::getAmtByBalDir).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    System.out.println(JSON.toJSONString(acctMap, SerializerFeature.PrettyFormat));

    TreeMap<Long, BigDecimal> resultList333 = voList.stream().collect(
        Collectors.groupingBy(AccountingDetailVo::getAcctNo,TreeMap::new, CollectorsUtils.summingBigDecimal(AccountingDetailVo::getAmtByBalDir)));

    System.out.println(JSON.toJSONString(resultList333, SerializerFeature.PrettyFormat));

  }
}