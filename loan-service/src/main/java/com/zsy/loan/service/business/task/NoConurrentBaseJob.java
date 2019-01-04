package com.zsy.loan.service.business.task;

import com.zsy.loan.service.business.task.BaseJob;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class NoConurrentBaseJob extends BaseJob {

}
