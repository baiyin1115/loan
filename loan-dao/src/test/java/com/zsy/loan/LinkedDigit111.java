package com.zsy.loan;

/**
 * Created by Administrator on 2019/1/17/017.
 */
public class LinkedDigit111 {

  //遍历一遍，找到最大值的位置
  static int searchMaxNode(int[] orgVector) {
    int position = 0;
    for (int i = 0; i < orgVector.length; i++) {
      int value = orgVector[i];
      if (value > orgVector[position]) {
        position = i;
      }
    }
    return position;
  }

  /**
   * 计算水量 最大值的左右两边分别计算
   *
   * @param orgVector 数组
   * @param maxPosition 最大节点位置
   * @param isZero 是否从0开始
   */
  static int count(int[] orgVector, int maxPosition, boolean isZero) {
    int start = 0, end = 0, sum = 0, waterNum = 0;
    boolean isStart = false; //是否开始计算
    if (isZero) { //从0位置开始搜索
      for (int i = 0; i <= maxPosition; i++) {

        //查找start节点
        if (!isStart) {

          if (i + 1 == orgVector.length) { //如果马上就越界了，就退出
            break;
          }

          if (orgVector[i] < orgVector[i + 1]) {
            continue;
          } else {
            start = i;
            isStart = true;
            continue;
          }
        }

        //查找end节点
        if (isStart) {
          if (i == start + 1) { //end节点不能是start的下一个节点
            sum += orgVector[i];
            continue;
          } else if (orgVector[i] < orgVector[start]) {
            sum += orgVector[i];
            continue;
          } else if (orgVector[i] >= orgVector[start]) {
            end = i;
            //水量=max(end值,start值)*(end位置-start位置-1)-sum(当中节点的值)
            waterNum += orgVector[start] * (+end - start - 1) - sum;

            sum = 0;
            if (i + 1 == orgVector.length) { //如果马上就越界了，就退出
              break;
            }
            if (orgVector[i] > orgVector[i + 1]) { //判断当前end节点符不符合start节点的要求
              start = end;
              isStart = true;
            } else {
              isStart = false;
            }

          }
        }

        if (end == maxPosition) {
          break;
        }
      }
    } else {//从最大位置开始
      for (int i = orgVector.length - 1; i >= maxPosition; i--) {

        //查找start节点
        if (!isStart) {

          if (i - 1 < 0) { //如果马上就越界了，就退出
            break;
          }

          if (orgVector[i] < orgVector[i - 1]) {
            continue;
          } else {
            start = i;
            isStart = true;
            continue;
          }
        }

        //查找end节点
        if (isStart) {
          if (i == start - 1) { //end节点不能是start的上一个节点
            sum += orgVector[i];
            continue;
          } else if (orgVector[i] < orgVector[start]) {
            sum += orgVector[i];
            continue;
          } else if (orgVector[i] >= orgVector[start]) {
            end = i;
            //水量=max(end值,start值)*(end位置-start位置-1)-sum(当中节点的值)
            waterNum += orgVector[start] * (-end + start - 1) - sum;

            sum = 0;

            if (i - 1 < 0) { //如果马上就越界了，就退出
              break;
            }
            if (orgVector[i] > orgVector[i - 1]) { //判断当前end节点符不符合start节点的要求
              start = end;
              isStart = true;
            } else {
              isStart = false;
            }
          }
        }

        if (end == maxPosition) {
          break;
        }
      }
    }

    return waterNum;

  }

  /**
   * 计算水量 最大值的左右两边分别计算，不过复用了循环
   *
   * @param orgVector 数组
   * @param maxPosition 最大节点位置
   * @param step 步长：+1/-1
   * @param begin 起始位置
   */
  static int count(int[] orgVector, int maxPosition, int step, int begin) {
    int start = 0, end = 0, sum = 0, waterNum = 0;
    boolean isStart = false; //是否开始计算
    int i = begin;
    while (i != maxPosition+step) {

      //查找start节点
      if (!isStart) {

        if ((i + step == orgVector.length) || (i + step < 0)) { //如果马上就越界了，就退出
          break;
        }
        if (orgVector[i] < orgVector[i + step]) {
          i = i + step;
          continue;
        } else {
          start = i;
          isStart = true;
          i = i + step;
          continue;
        }
      }

      //查找end节点
      if (isStart) {
        if (i == start + step) { //end节点不能是start的下一个节点
          sum += orgVector[i];
          i = i + step;
          continue;
        } else if (orgVector[i] < orgVector[start]) {
          sum += orgVector[i];
          i = i + step;
          continue;
        } else if (orgVector[i] >= orgVector[start]) {
          end = i;
          //水量=max(end值,start值)*(end位置-start位置-1)-sum(当中节点的值)
          waterNum += orgVector[start] * (step * end - step * start - 1) - sum;

          sum = 0;
          if ((i + step == orgVector.length) || (i + step < 0)) { //如果马上就越界了，就退出
            break;
          }
          if (orgVector[i] > orgVector[i + step]) { //判断当前end节点符不符合start节点的要求
            start = end;
            isStart = true;
          } else {
            isStart = false;
          }

        }
      }

      if (end == maxPosition) {
        break;
      }

      i = i + step;

    }

    return waterNum;

  }


  static int WaterCount(int[] orgVec) {
    int waterNum = 0;
    int maxPosition = searchMaxNode(orgVec);
    waterNum += count(orgVec, maxPosition, true); //从头
    waterNum += count(orgVec, maxPosition, false); //从尾
//    waterNum += count(orgVec, maxPosition, +1, 0); //从头
//    waterNum += count(orgVec, maxPosition, -1, orgVec.length - 1); //从尾
    return waterNum;
  }

  public static void main(String[] args) {
    int[] sampleVec = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    System.out.println(WaterCount(sampleVec));
    int[] sampleVec1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    System.out.println(WaterCount(sampleVec1));

    int[] sampleVec2 = {3, 2, 1, 1, 2, 3};
    System.out.println(WaterCount(sampleVec2));

    int[] sampleVec3 = {3, 2, 1, 1, 2, 3, 1, 1, 4};
    System.out.println(WaterCount(sampleVec3));

    int[] sampleVec4 = {3, 2, 1, 1, 1, 2, 4, 1, 1, 3, 2, 1};
    System.out.println(WaterCount(sampleVec4));

    int[] sampleVec5 = {1, 2, 3, 4, 5, 6};
    System.out.println(WaterCount(sampleVec5));

    int[] sampleVec6 = {6, 5, 4, 3, 2, 1};
    System.out.println(WaterCount(sampleVec6));
  }

}