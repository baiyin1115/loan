package com.zsy.loan.bean.enumeration;

public class BizTypeEnum {

  /**
   * 客户状态
   */
  public enum CustomerStatusEnum {
    /**
     * 正常:1,黑名单:2,删除:3
     */
    NORMAL(1), BLACKLIST(2), DELETE(3);

    private long value;

    private CustomerStatusEnum(long value) {
      this.value = value;
    }

    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }
  }


}
