package com.zsy.loan.bean.logback.oplog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:liufei
 * @create_time:2018/6/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpLogInfo {

  //访问的uri地址
  private String uri;
  //调用类
  private String className;
  //调用的方法名
  private String methodName;
  //返回的状态码
  private String respCode;
  //耗时
  private long timeConsuming;
  //请求参数
  private String arguments;
  //响应结果
  private String response;
}
