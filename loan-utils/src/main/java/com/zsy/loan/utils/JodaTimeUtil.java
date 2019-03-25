package com.zsy.loan.utils;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

/**
 * Created by Administrator on 2019/2/12/012.
 */
public class JodaTimeUtil {

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

  public static int daysBetween(DateTime begin, DateTime end) {

    int days = Days.daysBetween(begin, end).getDays();
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

      if(isEndDayOfMonth(beginTime) && isEndDayOfMonth(endTime)){
        flag = 0;
      }else if (beginTime.getDayOfMonth() != endTime.getDayOfMonth()){
        flag = 1;
      }

      iMonth = MonthsBetween(beginTime, endTime) + flag;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return iMonth;
  }

  /**
   * 计算两个日期之间包含的月数 2019-01-31/2019-04-17 == 4 个月 or 2019-01-23/2019-04-30 == 4 个月
   * or 2019-01-01/2019-04-23 == 4 个月
   */
  public static int getMonthContain(Date begin, Date end) {
    int iMonth = 0;
    try {

      DateTime beginTime = new DateTime(begin);
      DateTime endTime = new DateTime(end);

      if (beginTime.equals(endTime)) {
        return 0;
      }
      if (beginTime.isAfter(endTime)) {
        DateTime temp = endTime;
        endTime = beginTime;
        beginTime = temp;
      }

      int YM = (endTime.getYear() - beginTime.getYear()) * 12;
      int beginM = beginTime.getMonthOfYear();
      int endM = endTime.getMonthOfYear();

      if (isEndDayOfMonth(beginTime)) { //开始日期是当月最后一天的话，这个月不算
        iMonth = endM - beginM;
      } else {
        iMonth = endM - beginM + 1;
      }
      iMonth = YM + iMonth;

//      int nextYM = 0;
//      do {
//        iMonth +=1;
//        beginTime = beginTime.plusMonths(1);
//        nextYM = beginTime.getYear() + beginTime.getMonthOfYear();
//      } while (nextYM < endYM);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return iMonth;
  }

  public static Date getFirstDateOfMonth(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.dayOfMonth().withMinimumValue().toDate();
  }

  public static Date getEndDataOfMonth(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.dayOfMonth().withMaximumValue().toDate();
  }

  public static boolean isFirstDayOfMonth(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.dayOfMonth().get() == 1;
  }

  public static int getYear(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.getYear();
  }

  public static int getMonthOfYear(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.getMonthOfYear();
  }

  public static boolean isFirstDayOfMonth(DateTime dateTime) {
    return dateTime.dayOfMonth().get() == 1;
  }

  public static boolean isEndDayOfMonth(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.dayOfMonth().get() == dateTime.dayOfMonth().withMaximumValue().dayOfMonth().get();
  }

  public static boolean isEndDayOfMonth(DateTime dateTime) {
    return dateTime.dayOfMonth().get() == dateTime.dayOfMonth().withMaximumValue().dayOfMonth().get();
  }


  /**
   * 取得dd日期
   */
  public static Date getDdDate(Date begin, Date end, int dd, boolean isBegin) {

    DateTime beginTime = new DateTime(begin);
    DateTime endTime = new DateTime(end);
    DateTime ddBeginTime = null;
    DateTime ddEndTime = null;

    if (dd > beginTime.dayOfMonth().getMaximumValue()) {
      ddBeginTime = beginTime.dayOfMonth().withMaximumValue();
    } else {
      ddBeginTime = beginTime.withDayOfMonth(dd);
    }

    if (dd > endTime.dayOfMonth().getMaximumValue()) {
      ddEndTime = endTime.dayOfMonth().withMaximumValue();
    } else {
      ddEndTime = endTime.withDayOfMonth(dd);
    }

    if (isBegin) {
      if (ddBeginTime.compareTo(beginTime) >= 0 && ddBeginTime.compareTo(endTime) <= 0) { //先记着开始日期
        return ddBeginTime.toDate();
      }
      if (ddEndTime.compareTo(beginTime) >= 0 && ddEndTime.compareTo(endTime) <= 0) {
        return ddEndTime.toDate();
      }

      return beginTime.toDate(); //不在开始结束范围内返回开始日期
    } else {
      if (ddEndTime.compareTo(beginTime) >= 0 && ddEndTime.compareTo(endTime) <= 0) { //先记着结束日期来
        return ddEndTime.toDate();
      }

      if (ddBeginTime.compareTo(beginTime) >= 0 && ddBeginTime.compareTo(endTime) <= 0) {
        return ddBeginTime.toDate();
      }
      return endTime.toDate(); //不在开始结束范围内返回结束日期
    }
  }

  public static void main(String[] args) {

    Date dateBegin = DateTimeKit.parse("2019-02-01", DateTimeKit.NORM_DATE_PATTERN);
    Date dateEnd = DateTimeKit.parse("2019-02-03", DateTimeKit.NORM_DATE_PATTERN);

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

    DateTimeKit.parse("2019-02-01", DateTimeKit.NORM_DATE_PATTERN);
    DateTimeKit.parse("2019-02-03", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

    dateBegin = DateTimeKit.parse("2019-01-23", DateTimeKit.NORM_DATE_PATTERN);
    dateEnd = DateTimeKit.parse("2019-04-17", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

    dateBegin = DateTimeKit.parse("2019-01-30", DateTimeKit.NORM_DATE_PATTERN);
    dateEnd = DateTimeKit.parse("2019-04-30", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

    dateBegin = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);
    dateEnd = DateTimeKit.parse("2019-04-30", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

    dateBegin = DateTimeKit.parse("2018-12-31", DateTimeKit.NORM_DATE_PATTERN);
    dateEnd = DateTimeKit.parse("2019-06-30", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

    dateBegin = DateTimeKit.parse("2018-12-29", DateTimeKit.NORM_DATE_PATTERN);
    dateEnd = DateTimeKit.parse("2019-05-31", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

    dateBegin = DateTimeKit.parse("2018-12-31", DateTimeKit.NORM_DATE_PATTERN);
    dateEnd = DateTimeKit.parse("2019-07-31", DateTimeKit.NORM_DATE_PATTERN);

    System.out.println(daysBetween(dateBegin, dateEnd));
    System.out.println(MonthsBetween(dateBegin, dateEnd));
    System.out.println(getMonthFloor(dateBegin, dateEnd));
    System.out.println(getMonthContain(dateBegin, dateEnd));

//    dateBegin = DateTimeKit.parse("2019-02-27", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-27", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 29, false)));
//
//    dateBegin = DateTimeKit.parse("2019-01-30", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-02-28", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 5, false)));
//
//    dateBegin = DateTimeKit.parse("2019-02-28", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-31", DateTimeKit.NORM_DATE_PATTERN);
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 27, false)));
//
//    dateBegin = DateTimeKit.parse("2019-01-15", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-02-15", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 16, false)));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 30, false)));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-31", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 2, true)));
//    System.out.println(DateTimeKit.formatDate(getDdDate(dateBegin, dateEnd, 2, false)));

//    Date dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    Date dateEnd = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);

//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-31", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
////
//    dateBegin = DateTimeKit.parse("2019-01-05", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-20", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-05", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-31", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-1", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-30", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-03-15", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-04-17", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-23", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-04-30", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-04-23", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getMonthContain(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(daysBetween(dateBegin, dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-01-02", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(daysBetween(dateBegin, dateEnd));

//    System.out.println(isFirstDayOfMonth(dateBegin));
//    System.out.println(isEndDayOfMonth(dateBegin));
//
//    System.out.println(isFirstDayOfMonth(dateEnd));
//    System.out.println(isEndDayOfMonth(dateEnd));
//
//    dateBegin = DateTimeKit.parse("2019-02-28", DateTimeKit.NORM_DATE_PATTERN);
//    System.out.println(isFirstDayOfMonth(dateBegin));
//    System.out.println(isEndDayOfMonth(dateBegin));
//
//    dateBegin = DateTimeKit.parse("2019-02-15", DateTimeKit.NORM_DATE_PATTERN);
//    System.out.println(isFirstDayOfMonth(dateBegin));
//    System.out.println(isEndDayOfMonth(dateBegin));
//
//    dateBegin = DateTimeKit.parse("2019-01-01", DateTimeKit.NORM_DATE_PATTERN);
//    dateEnd = DateTimeKit.parse("2019-01-31", DateTimeKit.NORM_DATE_PATTERN);
//
//    System.out.println(getDdDate(dateBegin,dateEnd,27));
  }


}
