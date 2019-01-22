package com.zsy.loan.admin.core.intercept;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.bean.exception.LoanExceptionEnum;
import com.zsy.loan.bean.logback.oplog.entity.OpLogInfo;
import com.zsy.loan.bean.logback.oplog.entity.OpRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by liufei on 2017/10/27.
 */
@Slf4j
@Aspect
@Component
public class OpLogAspect {

  private static ObjectMapper mapper = new ObjectMapper();

  public OpLogAspect() {
  }

  @Pointcut("@annotation(com.zsy.loan.bean.logback.oplog.OpLog)")
  public void pointcut() {
  }

  @Before("pointcut()")
  public void printParams(JoinPoint joinPoint) {
    OpRequest opRequest = getRequestInfo(joinPoint);
    log.info("请求参数为：" + opRequest.toString());
  }

//  @Around("pointcut()")
//  public Object handle(ProceedingJoinPoint joinPoint) {
//    long startTime = 0;
//    long endTime = 0;
//    try {
//      startTime = System.currentTimeMillis();
//      Object returnVal = joinPoint.proceed();
//
//      endTime = System.currentTimeMillis();
//      logEvent(joinPoint, returnVal, endTime - startTime);
//      return returnVal;
//      //} catch (Exception ex) {
//      //  log.error("异常信息:{}", ex);
//    } catch (Throwable throwable) {
//      //log.error("异常信息：", throwable);
//      LoanException exception = null;
//      if (throwable instanceof LoanException) {
//        exception = (LoanException) throwable;
//      } else {
//        log.error("异常信息：", throwable);
//        exception = new LoanException(LoanExceptionEnum.SERVER_ERROR);
//      }
//
//      logEvent(joinPoint, exception, endTime - startTime);
//      throw exception;
//      //  log.error("异常信息：" + throwable);
//    }
//
//  }

  /*@AfterThrowing(
      pointcut = "pointcut()",
      throwing = "e"
  )
  public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
    try {
      ApplicationException exception = (ApplicationException) e;
      log.error(exception.getRespCode().getCode());
      OpRequest opRequest = getRequestInfo(joinPoint, "ERROR");
      //logEvent(opRequest, returnVal, timeConsuming);
    } catch (Exception ex) {
      log.error("异常信息:{}", ex);
    }

  }*/


  private void logEvent(ProceedingJoinPoint joinPoint, Object returnVal, long timeConsuming) {

    OpRequest opRequest = getRequestInfo(joinPoint);

    HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    opRequest.setUrl(req.getRequestURL().toString());

    OpLogInfo opLogInfo = OpLogInfo.builder()
        .uri(opRequest.getUrl())
        .className(opRequest.getSource())
        .methodName(opRequest.getMethod())
        //.respCode(returnVal.getRespCode().getCode())
        .timeConsuming(timeConsuming)
        .arguments(opRequest.getArguments())
        //.response(returnVal.toString())
        .build();

    try {
      log.info(mapper.writeValueAsString(opLogInfo));
    } catch (JsonProcessingException e) {
      log.error("序列化参数错误：" + e);
    }
  }

  private OpRequest getRequestInfo(JoinPoint joinPoint) {
    OpRequest opRequest = new OpRequest();
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();

    opRequest.setSource(targetName);
    opRequest.setMethod(methodName);
    opRequest.setArguments(StringUtils.join(arguments, ","));

    return opRequest;
  }


}
