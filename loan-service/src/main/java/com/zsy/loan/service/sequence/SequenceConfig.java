package com.zsy.loan.service.sequence;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MaXuewen on 2017/11/13.
 */
@Data
@Component
@ConfigurationProperties
public class SequenceConfig {

  @Value("${common.sequence.workerId}")
  private long workId=1;

  @Value("${common.sequence.datacenterId}")
  private long dataCenterId=1;

}
