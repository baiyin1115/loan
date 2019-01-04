package com.zsy.loan.admin.core.intercept;


import com.zsy.loan.utils.logback.ThreadLocalUtil;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by liufei on 2017/12/1.
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

  public static final String SERVICE_NAME = "business-name";

  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o) {
    log.info("来自 {} 服务访问开始，访问url：{} ", httpServletRequest.getHeader(SERVICE_NAME),
        httpServletRequest.getRequestURL());
    log.info("访问详细信息:{}", getRequestInfo(httpServletRequest, o));
    ThreadLocalUtil.requestTime.remove();
    ThreadLocalUtil.requestTime.set(System.currentTimeMillis());
    //MDC.clear();
    return true;// 只有返回true才会继续向下执行，返回false取消当前请求
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o,
      ModelAndView modelAndView) {

  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object handler, Exception e) {
    long startRequestTime = ThreadLocalUtil.requestTime.get();
    long endRequestTime = System.currentTimeMillis();

    log.info("服务 {} 访问结束, {} request to {} ,耗时：{} ms",
        httpServletRequest.getHeader(SERVICE_NAME),
        httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI(), (endRequestTime - startRequestTime));
    ThreadLocalUtil.requestTime.remove();
    MDC.clear();
  }

  public LogInterceptor.EventParameter getRequestInfo(HttpServletRequest request, Object o) {
    LogInterceptor.EventParameter eventParameter = new LogInterceptor.EventParameter();
    eventParameter.setSource(o.toString());
    eventParameter.setArguments(getRequestParam(request));
    eventParameter.setMethod(request.getMethod());
    eventParameter.setUrl(request.getRequestURI());
    return eventParameter;
  }

  public String getRequestParam(HttpServletRequest request) {

    StringBuilder param = new StringBuilder();
    param.append("{");
    Iterator iterator = request.getParameterMap().entrySet().iterator();
    while (iterator.hasNext()) {
      Entry entry = (Entry) iterator.next();
      param.append(entry.getKey() + ":");
      param.append(StringUtils.join((String[]) entry.getValue()));
      param.append(",");
    }
    param.append("}");
    return param.toString();
  }

  public static class EventParameter {

    private String source;
    private String arguments;
    private String method;
    private String url;

    EventParameter() {
    }


    void setSource(String source) {
      this.source = source;
    }

    void setArguments(String arguments) {
      this.arguments = arguments;
    }

    void setMethod(String method) {
      this.method = method;
    }

    void setUrl(String url) {
      this.url = url;
    }


    @Override
    public String toString() {
      return "EventParameter{" +
          "source='" + source + '\'' +
          ", arguments='" + arguments + '\'' +
          ", method='" + method + '\'' +
          ", url='" + url + '\'' +
          '}';
    }
  }
}
