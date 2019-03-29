package com.zsy.loan.service;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/3/29/029.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Coupon implements Serializable {

  private static final long serialVersionUID = 8579740700258601262L;
  private Integer couponId;
  private Integer price;
  private String name;



//  public Coupon(Integer couponId, Integer price, String name) {
//    this.couponId = couponId;
//    this.price = price;
//    this.name = name;
//  }
//
//  public Integer getCouponId() {
//    return couponId;
//  }
//
//  public void setCouponId(Integer couponId) {
//    this.couponId = couponId;
//  }
//
//  public Integer getPrice() {
//    return price;
//  }
//
//  public void setPrice(Integer price) {
//    this.price = price;
//  }
//
//  public String getName() {
//    return name;
//  }
//
//  public void setName(String name) {
//    this.name = name;
//  }
}
