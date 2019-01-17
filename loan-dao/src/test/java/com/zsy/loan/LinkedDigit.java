package com.zsy.loan;

import java.util.LinkedList;
import java.util.List;
import org.aspectj.weaver.Lint;

/**
 * Created by Administrator on 2019/1/17/017.
 */
public class LinkedDigit {

  //每个数减1,返回是否都为0
  static  boolean DescreOne(int[] orgVector)
  {
    boolean bAllZero= true;
    for(int i = 0 ; i < orgVector.length; i++)
    {
      int value = orgVector[i];
      if(value>0)
      {
        orgVector[i] = (value-1);
        bAllZero =false;
      }
    }
    return bAllZero;
  }


  //左边第一个不为0到右边第一个不为0的数中间一共多少个0
  static int CountLine(int[] orgVector)
  {
    int leftNotZeroIndex = 0;
    int rightNotZeroIndex = orgVector.length-1;
    //计算左边的坐标
    while(leftNotZeroIndex < orgVector.length)
    {
      if(orgVector[leftNotZeroIndex]>0)
      {
        break;
      }

      leftNotZeroIndex++;
    }


    //计算右边的坐标
    while(rightNotZeroIndex > 0)
    {
      if(orgVector[rightNotZeroIndex] > 0)
      {
        break;
      }
      rightNotZeroIndex--;
    }

    //数0
    int nCount = 0;
    while( leftNotZeroIndex < rightNotZeroIndex)
    {
      if(orgVector[leftNotZeroIndex]==0)
      {
        nCount++;
      }
      leftNotZeroIndex++;
    }
    return nCount;
  }


  static int WaterCount(int[] orgVec)
  {
    int nSum = 0;
    int nCount = 0;
    while(true)
    {
      nCount = CountLine(orgVec);
      nSum += nCount;
      if(DescreOne(orgVec))
      {
        break;
      }
    }
    return nSum;
  }

  public static void main(String[] args) {
    int[] sampleVec = {0,1,0,2,1,0,1,3,2,1,2,1};
    System.out.println(WaterCount(sampleVec));
    int[] sampleVec1 = {0,1,0,2,1,0,1,3,2,1,2,1};
    System.out.println(WaterCount(sampleVec1));

    int[] sampleVec2 = {3,2,1,1,2,3};
    System.out.println(WaterCount(sampleVec2));

    int[] sampleVec3 = {3,2,1,1,2,3,1,1,4};
    System.out.println(WaterCount(sampleVec3));

    int[] sampleVec4 = {3,2,1,1,1,2,4,1,1,3,2,1};
    System.out.println(WaterCount(sampleVec4));
  }

}