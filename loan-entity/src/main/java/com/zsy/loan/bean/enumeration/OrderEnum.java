package com.zsy.loan.bean.enumeration;

/**
 * 数据库排序
 *
 * @author fengshuonan
 * @Date 2017年5月31日20:48:41
 */
public enum OrderEnum {

  ASC("asc"), DESC("desc");

  private String des;

  OrderEnum(String des) {
    this.des = des;
  }

  public String getDes() {
    return des;
  }

  public void setDes(String des) {
    this.des = des;
  }
}
