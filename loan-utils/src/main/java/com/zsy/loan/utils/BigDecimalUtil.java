package com.zsy.loan.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * batch-parent.common <br/>
 * Created by PengRong on 2017/12/22. <br/>
 *
 * @author PengRong <br/>
 * @Description BigDecimalUtil ，java 原生的float ，double 只是适合用于科学计算，工程计算。不适合商业计算。 商业计算一般使用BigDecimanl
 * 类进行 精确 计算
 * @ClassName: ${BigDecimalUtil}
 * @since 2017-12-22 16:43 <br/>
 */
public class BigDecimalUtil {
  /**
   *   ROUND_CEILING
   如果   BigDecimal   是正的，则做   ROUND_UP   操作；如果为负，则做   ROUND_DOWN   操作。
   ROUND_DOWN
   从不在舍弃(即截断)的小数之前增加数字。
   ROUND_FLOOR
   如果   BigDecimal   为正，则作   ROUND_UP   ；如果为负，则作   ROUND_DOWN   。
   ROUND_HALF_DOWN
   若舍弃部分>   .5，则作   ROUND_UP；否则，作   ROUND_DOWN   。
   ROUND_HALF_EVEN
   如果舍弃部分左边的数字为奇数，则作   ROUND_HALF_EVEN   ；如果它为偶数，则作为ROUND_HALF_DOWN
   ROUND_HALF_EVEN
   若舍弃部分>=.5，则作   ROUND_UP   ；否则，作   ROUND_DOWN
   ROUND_UNNECESSARY
   该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。
   ROUND_UP
   总是在非   0   舍弃小数(即截断)之前增加数字。
   */
  /**
   * double 精确相加
   */
  public static BigDecimal add(double parama, double paramb) {
    BigDecimal bigDecimala = BigDecimal.valueOf(parama);
    BigDecimal bigDecimalb = BigDecimal.valueOf(paramb);
    return bigDecimala.add(bigDecimalb);
  }

  public static BigDecimal add(BigDecimal parama, BigDecimal paramb) {
    return parama.add(paramb);
  }

  public static BigDecimal add(BigDecimal... para) {
    BigDecimal result = BigDecimal.ZERO;

    for (int i = 0; i < para.length; i++) {
      if (i == 0) {
        result = para[i];
      } else {
        result = result.add(para[i]);
      }
    }

    return result;
  }

  /**
   * double 精确相减
   */
  public static BigDecimal sub(double parama, double paramb) {
    BigDecimal bigDecimala = BigDecimal.valueOf(parama);
    BigDecimal bigDecimalb = BigDecimal.valueOf(paramb);
    return bigDecimala.subtract(bigDecimalb);
  }

  public static BigDecimal sub(BigDecimal... para) {
    BigDecimal result = BigDecimal.ZERO;

    for (int i = 0; i < para.length; i++) {
      if (i == 0) {
        result = para[i];
      } else {
        result = result.subtract(para[i]);
      }
    }

    return result;
  }

  /**
   * double 精确相乘
   */
  public static BigDecimal mul(double parama, double paramb) {
    BigDecimal bigDecimala = BigDecimal.valueOf(parama);
    BigDecimal bigDecimalb = BigDecimal.valueOf(paramb);
    return bigDecimala.multiply(bigDecimalb);
  }

  public static BigDecimal mul(BigDecimal... para) {
    return mul(BigDecimal.ROUND_HALF_EVEN, 2, para);
  }

  public static BigDecimal mul(int routingMode, int scale, BigDecimal... para) {

    BigDecimal result = BigDecimal.ONE;

    if (routingMode <= 0) {
      routingMode = BigDecimal.ROUND_HALF_EVEN;
    }

    for (BigDecimal dec : para) {
      result = result.multiply(dec);
    }
    return result.setScale(scale, routingMode);
  }

  /**
   * 计算： value1/value2 </br>
   *
   * @param value1 被除数 </br>
   * @param value2 除数 </br>
   * @param scale 精度 定义了返回值的精度,表示精确到小数点后面几位 (又叫标度，就是科学计数法的指数 <tt>(10<sup>scale</sup> &times;
   * val)</tt>)</br>
   * @param routingMode 定义返回值的四舍五入模式 ;默认使用： BigDecimal.ROUND_HALF_EVEN 模式</br>
   */
  public static BigDecimal div(double value1, double value2, int scale, int routingMode)
      throws IllegalAccessException {
    //如果精确范围小于0，抛出异常信息
    if (scale < 0) {
      throw new IllegalAccessException("精确度不能小于0");
    }
    if (routingMode <= 0) {
      routingMode = BigDecimal.ROUND_HALF_EVEN;
    }
    BigDecimal b1 = new BigDecimal(Double.toString(value1));
    BigDecimal b2 = new BigDecimal(Double.toString(value2));
    BigDecimal result = b1.divide(b2, scale, routingMode);
    return result;
  }

  public static BigDecimal div(BigDecimal value1, BigDecimal value2) {
    return div(value1, value2, 2, BigDecimal.ROUND_HALF_EVEN);
  }

  public static BigDecimal div(BigDecimal value1, BigDecimal value2, int scale, int routingMode) {
    //如果精确范围小于0，抛出异常信息
    if (scale < 0) {
      return BigDecimal.valueOf(-1);
    }
    if (routingMode <= 0) {
      routingMode = BigDecimal.ROUND_HALF_EVEN;
    }
    BigDecimal b1 = value1;
    BigDecimal b2 = value2;
    BigDecimal result = b1.divide(b2, scale, routingMode);
    return result;
  }

  /**
   * 对value参数提供精确的小数位四舍五入处理。
   *
   * @param value 需要四舍五入的数字</br>
   * @param scale 期望小数点后保留几位 </br>
   */
  public static double round(double value, int scale) {
    double result = 0.00;
    if (scale < 0) {
      return -1;
    }
//    BigDecimal valueBig = BigDecimal.valueOf(value);
//    BigDecimal one = BigDecimal.valueOf(scale);
//    result = valueBig.divide(one, scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    result = BigDecimal.valueOf(value).setScale(scale,RoundingMode.HALF_EVEN).doubleValue();
    return result;
  }

  /**
   * 格式化 BigDecimal 为一个字符串序列；两个小数位，小数第二位 进行 四舍五入。
   */
  public static String formatAmt(BigDecimal bigDecimal) {
    if (bigDecimal == null) {
      return "0.00";
    }
    DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.CHINA);
    format.setDecimalSeparatorAlwaysShown(true);
    /** 设置小数部分允许的最大位数  */
    format.setMaximumFractionDigits(3);
    /** 设置小数部分允许的最小位数  */
    format.setMinimumFractionDigits(0);
    format.applyPattern(",##0.00");
    return format.format(bigDecimal);

  }


  /**
   * 格式化 BigDecimal 为一个字符串序列；两个小数位，小数第二位 进行 四舍五入。
   */
  public static String formatHundred(BigDecimal bigDecimal) {
    if (bigDecimal == null) {
      return "0.00";
    }
    DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.CHINA);
    format.setDecimalSeparatorAlwaysShown(true);
    /** 设置小数部分允许的最大位数  */
    format.setMaximumFractionDigits(3);
    /** 设置小数部分允许的最小位数  */
    format.setMinimumFractionDigits(0);
    format.applyPattern(",##0.00%");
    return format.format(bigDecimal);

  }

  public static String formatThousand(BigDecimal bigDecimal) {
    if (bigDecimal == null) {
      return "0.00";
    }
    DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.CHINA);
    format.setDecimalSeparatorAlwaysShown(true);
    /** 设置小数部分允许的最大位数  */
    format.setMaximumFractionDigits(3);
    /** 设置小数部分允许的最小位数  */
    format.setMinimumFractionDigits(0);
    format.applyPattern(",##0.00\u2030");
    return format.format(bigDecimal);
  }

  /**
   * 四舍六入五成双的规则：
   * 1. 被修约的数字小于5时，该数字舍去；
   * 2. 被修约的数字大于5时，则进位；
   * 3. 被修约的数字等于5时，要看5前面的数字，若是奇数则进位，若是偶数则将5舍掉，即修约后末尾数字都成为偶数；若5的后面还有不为“0”的任何数，
   * 则此时无论5的前面是奇数还是偶数，均应进位。
   * @param value
   * @param digit
   * @return
   */
  public static String sciCal(double value, int digit) {
    String result = "-999";
    try {
      double ratio = Math.pow(10, digit);
      double _num = value * ratio;
      double mod = _num % 1;
      double integer = Math.floor(_num);
      double returnNum;
      if (mod > 0.5) {
        returnNum = (integer + 1) / ratio;
      } else if (mod < 0.5) {
        returnNum = integer / ratio;
      } else {
        returnNum = (integer % 2 == 0 ? integer : integer + 1) / ratio;
      }
      BigDecimal bg = new BigDecimal(returnNum);
      result = bg.setScale((int) digit, BigDecimal.ROUND_HALF_UP).toString();
    } catch (RuntimeException e) {
      throw e;
    }
    return result;
  }

  /**
   * 四舍六入五成双的规则：
   * 1. 被修约的数字小于5时，该数字舍去；
   * 2. 被修约的数字大于5时，则进位；
   * 3. 被修约的数字等于5时，要看5前面的数字，若是奇数则进位，若是偶数则将5舍掉，即修约后末尾数字都成为偶数；若5的后面还有不为“0”的任何数，
   * 则此时无论5的前面是奇数还是偶数，均应进位。
   * @param value
   * @param digit
   * @return
   */
  public static String sciCal2(double value, int digit) {
    String result = "-999";
    BigDecimal bg = BigDecimal.valueOf(value);
    result = bg.setScale((int) digit, BigDecimal.ROUND_HALF_EVEN).toString();
    return result;
  }

  public static void main(String[] args) throws IllegalAccessException {
//    System.out.println(String.valueOf(1212.4545523542));
//    System.out.println(add(121.454, 545.15454));
//    //格式化精确计算后的值
//    System.out.println("formatBigDecimal: " + formatHundred(add(12455451.454, 545.15454)));
//    System.out.println("formatBigDecimal: " + formatThousand(add(12455451.454, 545.15454)));
//    System.out.println(sub(121.454, 545.15454));
//    System.out.println(mul(121.454, 545.15454));
//    System.out.println(div(1, 3, 5, BigDecimal.ROUND_HALF_EVEN));
//    System.out.println(round(12154.4565, 2));
//    System.out.println(add(121.454, 545.15454));
//    System.out.println(sub(121.454, 545.15454));
//    System.out.println(mul(121.454, 545.15454));
//    System.out.println(div(1, 3, 5, BigDecimal.ROUND_HALF_EVEN));
//
//    /**
//     * NumberFormat 数字形式的格式化
//     */
//    NumberFormat nFormat = NumberFormat.getNumberInstance();
//    nFormat.setMinimumIntegerDigits(3);//设置整数部分至少为3位
//    nFormat.setMaximumFractionDigits(5);//设置小数点位数为5
//    System.out.println("Format Out 3.2128345= " + nFormat.format(3.2128345));
//    // 货币形式格式化
//    NumberFormat cFormat = NumberFormat.getCurrencyInstance();
//    cFormat.setMaximumFractionDigits(3);
//    System.out.println("Format Out 321283.47656=" + cFormat.format(321283.47656));
//    // 百分比形式
//    NumberFormat percentFormat = NumberFormat.getPercentInstance();
//    percentFormat.setMaximumFractionDigits(4);
//    System.out.println("Format Out 3.2128345=" + percentFormat.format(3.2128345));
//    System.out.println("Format Out 3.2128345=" + percentFormat.format(0.2128345));

    //System.out.println("Format Out null="+nFormat.format(null));//参数是null,出现异常

//    String b = "9.815";
//    System.out.println(sciCal(b,2) +"|"+sciCal2(b,2));

    double a = 9.815d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.815d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.725d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.8150d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.72501d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.82671d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.8350d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.8351d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.8250d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.82501d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));

    a = 9.825d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));

    a = 9.8251d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.8151d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));

    a = 9.8241d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));
    a = 9.8161d;
    System.out.println(sciCal(a,2) +"|"+sciCal2(a,2));

  }


}
