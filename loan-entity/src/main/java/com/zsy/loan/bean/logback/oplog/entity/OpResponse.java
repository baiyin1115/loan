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
public class OpResponse {

  private Object response;
}
