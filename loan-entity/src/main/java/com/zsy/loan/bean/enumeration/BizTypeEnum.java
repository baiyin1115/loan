package com.zsy.loan.bean.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

  /**
   * 客户类型
   */
  public enum CustomerTypeEnum {
    /**
     * 1:借款人账户,2:融资人账户
     */
    INVEST(1), LOAN(2);

    private long value;

    private CustomerTypeEnum(long value) {
      this.value = value;
    }

    @JsonValue
    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }

    @JsonCreator
    public static CustomerTypeEnum getEnumByKey(long key) {
      CustomerTypeEnum[] array = CustomerTypeEnum.values();
      for (CustomerTypeEnum item : array) {
        if (item.getValue() == key) {
          return item;
        }
      }
      return null;
    }

  }

  /**
   * 账户状态
   */
  public enum AcctStatusEnum {
    /**
     * 有效:1,冻结:2,止用:3
     */
    VALID(1), FREEZE(2), INVALID(3);

    private long value;

    private AcctStatusEnum(long value) {
      this.value = value;
    }

    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }
  }

  /**
   * 账户余额类型
   */
  public enum AcctBalanceTypeEnum {
    /**
     * 可透支:1,不可透支:2
     */
    OVERDRAW(1), NO_OVERDRAW(2);

    private long value;

    private AcctBalanceTypeEnum(long value) {
      this.value = value;
    }

    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }
  }

  /**
   * 账户类型
   */
  public enum AcctTypeEnum {
    /**
     * 1:公司卡账户,2:融资账户,3:借款人账户,4:代偿账户,5:暂收,6:暂付
     */
    COMPANY(1), INVEST(2), LOAN(3), REPLACE(4), INTERIM_IN(5),INTERIM_OUT(6);

    private long value;

    private AcctTypeEnum(long value) {
      this.value = value;
    }

    @JsonValue
    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }

    @JsonCreator
    public static AcctTypeEnum getEnumByKey(long key) {
      AcctTypeEnum[] array = AcctTypeEnum.values();
      for (AcctTypeEnum item : array) {
        if (item.getValue() == key) {
          return item;
        }
      }
      return null;
    }

  }


}
