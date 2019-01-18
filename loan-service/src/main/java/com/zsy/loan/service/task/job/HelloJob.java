package com.zsy.loan.service.task.job;

import com.alibaba.fastjson.JSON;
import com.zsy.loan.service.task.JobExecuter;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * HelloJob
 *
 * @author zt
 * @version 2018/12/30 0030
 */
@Component
public class HelloJob extends JobExecuter {

  @Override
  public void execute(Map<String, Object> dataMap) throws Exception {
    System.out.println("hello :" + JSON.toJSONString(dataMap));
  }
}
