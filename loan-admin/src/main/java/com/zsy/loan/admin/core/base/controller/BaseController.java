package com.zsy.loan.admin.core.base.controller;

import com.zsy.loan.admin.core.base.tips.SuccessTip;
import com.zsy.loan.admin.core.util.FileUtil;
import com.zsy.loan.bean.enumeration.BizExceptionEnum;
import com.zsy.loan.bean.exception.LoanException;
import com.zsy.loan.service.wrapper.BaseControllerWrapper;
import com.zsy.loan.utils.HttpKit;
import com.zsy.loan.utils.factory.Page;
import com.zsy.loan.admin.core.page.PageInfoBT;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class BaseController {

  protected static String SUCCESS = "SUCCESS";
  protected static String ERROR = "ERROR";

  protected static String REDIRECT = "redirect:";
  protected static String FORWARD = "forward:";

  protected static SuccessTip SUCCESS_TIP = new SuccessTip();

  protected HttpServletRequest getHttpServletRequest() {
    return HttpKit.getRequest();
  }

  protected HttpServletResponse getHttpServletResponse() {
    return HttpKit.getResponse();
  }

  protected HttpSession getSession() {
    return HttpKit.getRequest().getSession();
  }

  protected HttpSession getSession(Boolean flag) {
    return HttpKit.getRequest().getSession(flag);
  }

  protected String getPara(String name) {
    return HttpKit.getRequest().getParameter(name);
  }

  protected void setAttr(String name, Object value) {
    HttpKit.getRequest().setAttribute(name, value);
  }

  protected Integer getSystemInvokCount() {
    return (Integer) this.getHttpServletRequest().getServletContext().getAttribute("systemCount");
  }

  /**
   * 把service层的分页信息，封装为bootstrap table通用的分页封装
   */
  protected <T> PageInfoBT<T> packForBT(Page<T> page) {
    return new PageInfoBT<T>(page);
  }

  /**
   * 包装一个list，让list增加额外属性
   */
  protected Object wrapObject(BaseControllerWrapper wrapper) {
    return wrapper.wrap();
  }

  /**
   * 删除cookie
   */
  protected void deleteCookieByName(String cookieName) {
    Cookie[] cookies = this.getHttpServletRequest().getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(cookieName)) {
        Cookie temp = new Cookie(cookie.getName(), "");
        temp.setMaxAge(0);
        this.getHttpServletResponse().addCookie(temp);
      }
    }
  }

  /**
   * 返回前台文件流
   *
   * @author fengshuonan
   * @date 2017年2月28日 下午2:53:19
   */
  protected ResponseEntity<byte[]> renderFile(String fileName, String filePath) {
    byte[] bytes = FileUtil.toByteArray(filePath);
    return renderFile(fileName, bytes);
  }

  /**
   * 返回前台文件流
   *
   * @author fengshuonan
   * @date 2017年2月28日 下午2:53:19
   */
  protected ResponseEntity<byte[]> renderFile(String fileName, byte[] fileBytes) {
    String dfileName = null;
    try {
      dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", dfileName);
    return new ResponseEntity<byte[]>(fileBytes, headers, HttpStatus.CREATED);
  }

  protected void exportErr(BindingResult error) {
    List<ObjectError> errors = error.getAllErrors();
    if (CollectionUtils.isNotEmpty(errors)) {
      StringBuilder errorMsg = new StringBuilder();
      errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
      throw new LoanException(BizExceptionEnum.REQUEST_NULL, errorMsg.toString());
    }
  }
}
