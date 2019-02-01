/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zsy.loan.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtil {


  private static final Object lock = new Object();

  private static final Map<String, ThreadLocal<SimpleDateFormat>> pool = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

  /**
   * 获取YYYY格式
   */
  public static String getYear() {
    return formatDate(new Date(), "yyyy");
  }

  /**
   * 获取YYYY格式
   */
  public static String getYear(Date date) {
    return formatDate(date, "yyyy");
  }

  /**
   * 获取YYYY-MM-DD格式
   */
  public static String getDay() {
    return formatDate(new Date(), "yyyy-MM-dd");
  }

  /**
   * 获取YYYY-MM-DD格式
   */
  public static String getDay(Date date) {
    if (date == null) {
      return "";
    }
    return formatDate(date, "yyyy-MM-dd");
  }

  /**
   * 获取YYYYMMDD格式
   */
  public static String getDays() {
    return formatDate(new Date(), "yyyyMMdd");
  }

  /**
   * 获取YYYYMMDD格式
   */
  public static String getDays(Date date) {
    return formatDate(date, "yyyyMMdd");
  }

  /**
   * 获取YYYY-MM-DD HH:mm:ss格式
   */
  public static String getTime() {
    return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
  }

  /**
   * 获取YYYY-MM-DD HH:mm:ss.SSS格式
   */
  public static String getMsTime() {
    return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
  }

  /**
   * 获取YYYYMMDDHHmmss格式
   */
  public static String getAllTime() {
    return formatDate(new Date(), "yyyyMMddHHmmss");
  }

  /**
   * 获取YYYY-MM-DD HH:mm:ss格式
   */
  public static String getTime(Date date) {
    return formatDate(date, "yyyy-MM-dd HH:mm:ss");
  }

  public static String formatDate(Date date, String pattern) {
    String formatDate = null;
    if (StringUtils.isNotEmpty(pattern)) {
      formatDate = DateFormatUtils.format(date, pattern);
    } else {
      formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
    }
    return formatDate;
  }

  /**
   * @return boolean
   * @Title: compareDate
   * @Description:(日期比较，如果e>s 返回true 否则返回false)
   * @author luguosui
   */
  public static boolean compareDate(String start, String end) {
    if (parseDate(end) == null || parseDate(start) == null) {
      return false;
    }
    return parseDate(end).getTime() > parseDate(start).getTime();
  }

  public static boolean compareDate(Date start, Date end) {
    return end.getTime() >= start.getTime();
  }

  /**
   * 格式化日期
   */
  public static Date parseDate(String date) {
    return parse(date, "yyyy-MM-dd");
  }

  /**
   * 格式化日期
   */
  public static Date parseTime(String date) {
    return parse(date, "yyyy-MM-dd HH:mm:ss");
  }

  /**
   * 格式化日期
   */
  public static Date parse(String date, String pattern) {
    if (date != null) {
      if (pattern == null || "".equals(pattern)) {
        return null;
      }
      DateFormat format = getDFormat(pattern);
      try {
        return format.parse(date);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static SimpleDateFormat getDFormat(String pattern) {
    ThreadLocal<SimpleDateFormat> tl = pool.get(pattern);
    if (tl == null) {
      synchronized (lock) {
        tl = pool.get(pattern);
        if (tl == null) {
          final String p = pattern;
          tl = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected synchronized SimpleDateFormat initialValue() {
              return new SimpleDateFormat(p);
            }
          };
          pool.put(p, tl);
        }
      }
    }
    return tl.get();
  }

  /**
   * 格式化日期
   */
  public static String format(Date date, String pattern) {
    return DateFormatUtils.format(date, pattern);
  }

  /**
   * 把日期转换为Timestamp
   */
  public static Timestamp format(Date date) {
    return new Timestamp(date.getTime());
  }

  public static Timestamp getTimestamp() {
    return new Timestamp(new Date().getTime());
  }

  /**
   * 校验日期是否合法
   */
  public static boolean isValidDate(String s) {
    return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
  }

  /**
   * 校验日期是否合法
   */
  public static boolean isValidDate(String s, String pattern) {
    return parse(s, pattern) != null;
  }

  public static int getDiffYear(String startTime, String endTime) {
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    try {
      int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
          startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
      return years;
    } catch (Exception e) {
      // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
      return 0;
    }
  }

  /**
   * <li>功能描述：时间相减得到天数
   *
   * @return long
   * @author Administrator
   */
  public static long getDaySub(String beginDateStr, String endDateStr) {
    long day = 0;
    SimpleDateFormat format = new SimpleDateFormat(
        "yyyy-MM-dd");
    Date beginDate = null;
    Date endDate = null;

    try {
      beginDate = format.parse(beginDateStr);
      endDate = format.parse(endDateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
    // System.out.println("相隔的天数="+day);

    return day;
  }

  public static long getDaySub(Date beginDate, Date endDate) {
    long day = 0;
    day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
    return day;
  }

  /**
   * 获取两个日期相差的月数
   *
   * @param end 较大的日期
   * @param begin 较小的日期
   * @return 如果d1>d2返回 月数差 否则返回0
   */
  public static int getMonthDiff(Date end, Date begin) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(end);
    c2.setTime(begin);
    if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
      return 0;
    }
    int year1 = c1.get(Calendar.YEAR);
    int year2 = c2.get(Calendar.YEAR);
    int month1 = c1.get(Calendar.MONTH);
    int month2 = c2.get(Calendar.MONTH);
    int day1 = c1.get(Calendar.DAY_OF_MONTH);
    int day2 = c2.get(Calendar.DAY_OF_MONTH);
    // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
    int yearInterval = year1 - year2;
    // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
    if (month1 < month2 || month1 == month2 && day1 < day2) {
      yearInterval--;
    }
    // 获取月数差值
    int monthInterval = (month1 + 12) - month2;
    if (day1 < day2) {
      monthInterval--;
    }
    monthInterval %= 12;
    return yearInterval * 12 + monthInterval;
  }

  /**
   * 计算两个日期之间相差的月数 2019-01-23/2019-04-17 == 2 个月 or 2019-01-23/2019-04-30 == 3 个月
   */
  public static int getMonth(Date date1, Date date2) {
    int iMonth = 0;
    int flag = 0;
    try {
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date1);

      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date2);

      if (cal2.equals(cal1)) {
        return 0;
      }
      if (cal1.after(cal2)) {
        Calendar temp = cal1;
        cal1 = cal2;
        cal2 = temp;
      }
      if (cal2.get(Calendar.DAY_OF_MONTH) < cal1.get(Calendar.DAY_OF_MONTH)) {
        flag = 1;
      }

      if (cal2.get(Calendar.YEAR) > cal1.get(Calendar.YEAR)) {
        iMonth =
            ((cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12 + cal2.get(Calendar.MONTH)
                - flag)
                - cal1.get(Calendar.MONTH);
      } else {
        iMonth = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH) - flag;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return iMonth;
  }

  /**
   * 计算两个日期之间相差的月数 2019-01-23/2019-04-17 == 3 个月 or 2019-01-23/2019-04-30 == 4 个月
   */
  public static int getMonthFloor(Date date1, Date date2) {
    int iMonth = 0;
    int flag = 0;
    try {
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date1);

      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date2);

      if (cal2.equals(cal1)) {
        return 0;
      }
      if (cal1.after(cal2)) {
        Calendar temp = cal1;
        cal1 = cal2;
        cal2 = temp;
      }
      if (cal2.get(Calendar.DAY_OF_MONTH) > cal1.get(Calendar.DAY_OF_MONTH)) {
        flag = 1;
      }

      if (cal2.get(Calendar.YEAR) > cal1.get(Calendar.YEAR)) {
        iMonth =
            ((cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12 + cal2.get(Calendar.MONTH)
                - flag)
                - cal1.get(Calendar.MONTH);
      } else {
        iMonth = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH) + flag;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return iMonth;
  }


  public static final int daysBetween(Date early, Date late) {

    java.util.Calendar calst = java.util.Calendar.getInstance();
    java.util.Calendar caled = java.util.Calendar.getInstance();
    calst.setTime(early);
    caled.setTime(late);
    //设置时间为0时
    calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
    calst.set(java.util.Calendar.MINUTE, 0);
    calst.set(java.util.Calendar.SECOND, 0);
    caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
    caled.set(java.util.Calendar.MINUTE, 0);
    caled.set(java.util.Calendar.SECOND, 0);
    //得到两个日期相差的天数
    int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
        .getTime().getTime() / 1000)) / 3600 / 24;

    return days;
  }

  /**
   * 得到n天之后的日期
   */
  public static String getAfterDayString(int days) {

    Calendar canlendar = Calendar.getInstance(); // java.util包
    canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
    Date date = canlendar.getTime();

    SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateStr = sdfd.format(date);

    return dateStr;
  }

  public static Date getAfterDayDate(Date early, int days) {

    Calendar canlendar = java.util.Calendar.getInstance(); // java.util包
    canlendar.setTime(early);
    canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
    Date date = canlendar.getTime();

    return date;
  }

  /**
   * 时间加减月数
   * @param startDate 要处理的时间，Null则为当前时间
   * @param months 加减的月数
   * @return Date
   */
  public static Date dateAddMonths(Date startDate, int months) {
    if (startDate == null) {
      startDate = new Date();
    }
    Calendar c = Calendar.getInstance();
    c.setTime(startDate);
    c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
    return c.getTime();
  }

  public static void main(String[] args) {
    System.out.println(format(getAfterDayDate(parseDate("2019-03-30"),30+30-getDaysOfMonth("201904")),"yyyy-MM-dd"));
    System.out.println(format(getAfterDayDate(parseDate("2019-04-30"),60+30-getDaysOfMonth("201904")),"yyyy-MM-dd"));
  }


  public static int getDaysOfMonth(String strdate) {
    int m = Integer.parseInt(strdate.substring(4, 6));
    switch (m) {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        return 31;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      case 2:
        if (isLeapYear(strdate)) {
          return 29;
        } else {
          return 28;
        }
      default:
        return 0;
    }
  }

  /**
   * 判断年份是否为闰年
   * 判断闰年的条件， 能被4整除同时不能被100整除，或者能被400整除
   */
  public static boolean isLeapYear(String strdate) {

    int year = Integer.parseInt(strdate.substring(0, 4));

    boolean isLeapYear = false;
    if (year % 4 == 0 && year % 100 != 0) {
      isLeapYear = true;
    } else if (year % 400 == 0) {
      isLeapYear = true;
    }
    return isLeapYear;
  }


//  public static Date getAfterDayDates(Date early, int days, int termNo) {
//
//    List<Date> arr = new ArrayList<>(termNo);
//
//    Calendar calendar = java.util.Calendar.getInstance(); // java.util包
//    calendar.setTime(early);
//
//    int year = calendar.get(Calendar.YEAR);
//    int month = calendar.get(Calendar.MONTH);
//    int day = calendar.get(Calendar.DATE);
//
//    for (int i = 0; i < termNo; i++) {
//      calendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
//      Date newDate = calendar.getTime();
//
//    }
//
//
//
//    return date;
//  }

  /**
   * 得到n天之后是周几
   */
  public static String getAfterDayWeek(String days) {
    int daysInt = Integer.parseInt(days);

    Calendar canlendar = Calendar.getInstance(); // java.util包
    canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
    Date date = canlendar.getTime();

    SimpleDateFormat sdf = new SimpleDateFormat("E");
    String dateStr = sdf.format(date);

    return dateStr;
  }




}
