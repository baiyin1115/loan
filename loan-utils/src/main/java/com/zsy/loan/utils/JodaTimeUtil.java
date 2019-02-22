package com.zsy.loan.utils;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

/**
 * Created by Administrator on 2019/2/12/012.
 */
public class JodaTimeUtil {

  public static void main(String[] args) {

//    Date dateTime1 = DateTimeKit.parse("2019-02-28", DateTimeKit.NORM_DATE_PATTERN);
////
//    Date dateTime2 = getAfterDayMonth(dateTime1, 1);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//    dateTime2 = getAfterDayMonth(dateTime1, 2);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//    dateTime2 = getAfterDayMonth(dateTime1, 3);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//    dateTime2 = getAfterDayMonth(dateTime1, 4);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//
//    dateTime2 = getAfterDayDate(dateTime1, 30);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//    dateTime2 = getAfterDayDate(dateTime1, 30);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//    dateTime2 = getAfterDayDate(dateTime1, 30);
//    System.out.println(DateTimeKit.formatDate(dateTime2));
//    dateTime2 = getAfterDayDate(dateTime1, 30);
//    System.out.println(DateTimeKit.formatDate(dateTime2));

    Date dateBegin = DateTimeKit.parse("2019-02-01", DateTimeKit.NORM_DATE_PATTERN);
    Date dateEnd = DateTimeKit.parse("2019-02-03", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
//    System.out.println(MonthsBetween(dateBegin, dateEnd));
//    System.out.println(getMonthFloor(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-23", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-04-17", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(daysBetween(dateBegin, dateEnd));
//    System.out.println(MonthsBetween(dateBegin, dateEnd));
//    System.out.println(getMonthFloor(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-30", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-04-30", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(daysBetween(dateBegin, dateEnd));
//    System.out.println(MonthsBetween(dateBegin, dateEnd));
//    System.out.println(getMonthFloor(dateBegin, dateEnd));

//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 5)));
//
//    dateBegin = DateTimeKit.parse("2019-02-28", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-31", DateTimeKit.NORM_DATE_PATTERN);
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 27)));
//
//    dateBegin = DateTimeKit.parse("2019-01-15", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-02-15", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 16)));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 30)));

  }

  /**
   * 计算n天后的日期
   */
  public static Date getAfterDayDate(Date early, int days) {

    DateTime dateTime = new DateTime(early);
    if (days >= 0) {
      return dateTime.plusDays(days).toDate();
    } else {
      return dateTime.minusDays(days).toDate();
    }
  }

  /**
   * 计算n月后的日期
   */
  public static Date getAfterDayMonth(Date early, int month) {

    DateTime dateTime = new DateTime(early);
    if (month >= 0) {
      return dateTime.plusMonths(month).toDate();
    } else {
      return dateTime.minusMonths(month).toDate();
    }
  }

  /**
   * 计算两个时间差的天数
   */
  public static int daysBetween(Date begin, Date end) {

    DateTime beginTime = new DateTime(begin);
    DateTime endTime = new DateTime(end);

    int days = Days.daysBetween(beginTime, endTime).getDays();
    return days;
  }

  /**
   * 计算两个时间差的月数 2019-01-23/2019-04-17 == 2 个月 or 2019-01-23/2019-04-30 == 3 个月
   * or 2019-01-23/2019-04-23 == 3 个月
   */
  public static int MonthsBetween(Date begin, Date end) {

    DateTime beginTime = new DateTime(begin);
    DateTime endTime = new DateTime(end);

    return MonthsBetween(beginTime, endTime);
  }

  public static int MonthsBetween(DateTime beginTime, DateTime endTime) {

    if (beginTime.equals(endTime)) {
      return 0;
    }
    if (beginTime.isAfter(endTime)) {
      DateTime temp = endTime;
      endTime = beginTime;
      beginTime = temp;
    }

    int months = Months.monthsBetween(beginTime, endTime).getMonths();
    return months;
  }


  /**
   * 计算两个日期之间相差的月数 2019-01-23/2019-04-17 == 3 个月 or 2019-01-23/2019-04-30 == 4 个月
   * or 2019-01-23/2019-04-23 == 3 个月
   */
  public static int getMonthFloor(Date begin, Date end) {
    int iMonth = 0;
    int flag = 0;
    try {

      DateTime beginTime = new DateTime(begin);
      DateTime endTime = new DateTime(end);

      if (beginTime.getDayOfMonth() != endTime.getDayOfMonth()) {
        flag = 1;
      }

      iMonth = MonthsBetween(beginTime, endTime) + flag;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return iMonth;
  }


  /**
   * 取得大于等于开始时间 小于等于结束时间的某一天 指定日期必须<=27号
   */
  public static Date getDdDate(Date begin, Date end, int dd) {

    DateTime beginTime = new DateTime(begin);
    DateTime endTime = new DateTime(end);

    if (beginTime.getMonthOfYear() == endTime.getMonthOfYear()) { //同一个月

      DateTime ddDateTime = null;

      if (dd > beginTime.dayOfMonth().getMaximumValue()) {
        ddDateTime = beginTime.dayOfMonth().withMaximumValue();
      } else {
        ddDateTime = beginTime.withDayOfMonth(dd);
      }

      if (ddDateTime.compareTo(beginTime) >= 0 && ddDateTime.compareTo(endTime) <= 0) {
        return ddDateTime.toDate();
      } else {
        return endTime.toDate(); //不在开始结束范围内返回结束日期
      }
    } else {

      DateTime beginDD = null;
      DateTime endDD = null;

      if (dd > beginTime.dayOfMonth().getMaximumValue()) {
        beginDD = beginTime.dayOfMonth().withMaximumValue();
      } else {
        beginDD = beginTime.withDayOfMonth(dd);
      }

      if (dd > endTime.dayOfMonth().getMaximumValue()) {
        endDD = endTime.dayOfMonth().withMaximumValue();
      } else {
        endDD = endTime.withDayOfMonth(dd);
      }

      if (beginDD.compareTo(beginTime) >= 0 && beginDD.compareTo(endTime) <= 0) {
        return beginDD.toDate();
      } else if (endDD.compareTo(beginTime) >= 0 && endDD.compareTo(endTime) <= 0) {
        return endDD.toDate();
      } else {
        return endTime.toDate(); //不在开始结束范围内返回结束日期
      }
    }
  }

}
