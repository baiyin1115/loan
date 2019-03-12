package com.zsy.loan.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/12/012.
 */
public class metaspaceTest {


  static String base = "string";

  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      String str = base + base;
      base = str;
      list.add(str.intern());
    }
  }
}
