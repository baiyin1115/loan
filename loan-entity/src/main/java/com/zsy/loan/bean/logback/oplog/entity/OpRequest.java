package com.zsy.loan.bean.logback.oplog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author:liufei
 * @create_time:2018/6/15
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OpRequest {

  private String source;
  private String level;
  private String name;
  private String desc;
  private String arguments;
  private String method;
  private String url;
}
