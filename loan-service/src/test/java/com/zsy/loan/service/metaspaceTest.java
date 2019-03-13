package com.zsy.loan.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2019/3/12/012.
 */
public class metaspaceTest {


  static String base = "string";

  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
//      String str = base + base;
//      base = str;
      //list.add(str);
      final double d = Math.random();
      final int x = (int)(d*1000000);

      list.add(String.valueOf(System.currentTimeMillis()+x).intern());
//      String.valueOf(str).intern();
//      String.valueOf(System.currentTimeMillis()+x).intern();
      //str.intern();
      if(i%5==0){
        System.out.println("===========>"+i);
        try {
          Thread.currentThread().sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      //list.add(str.intern());
    }
//
//    List<String> list2 = new ArrayList<String>(list.size());
//    for (int i = 0; i < 8; i++) {
//      list2.add("");
//    }
//
//    Collections.copy(list2,list);
//
//    System.out.println(list.get(0) == list.get(0));
//
//    String[] list = new String[8];
//    for (int i = 0; i < 8; i++) {
//      //String str = base + base;
//      // base = str;
//      //list.add(str);
//      final double d = Math.random();
//      final int x = (int)(d*1000000);
//
//      list[i] = String.valueOf(System.currentTimeMillis()+x).intern();
//      //str.intern();
////      if(i%5==0){
////        System.out.println("===========>"+i);
////        try {
////          Thread.currentThread().sleep(10);
////        } catch (InterruptedException e) {
////          e.printStackTrace();
////        }
////      }
//      //list.add(str.intern());
//    }

//    List<String> list2 = new ArrayList<String>(list.size());
//    for (int i = 0; i < 8; i++) {
//      list2.add("");
//    }

//    Collections.copy(list2,list);

//    String[] list2 = Arrays.copyOf(list,8);
//    System.out.println(list[0] == list2[0]);
//    System.out.println(list == list2);
//    System.out.println(list.hashCode() == list2.hashCode());



//    System.out.println(list.get(0) == list.get(0));
  }
}
