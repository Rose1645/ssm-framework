package com.rose.lzk.component;

import com.rose.lzk.entity.api.CommonResult;
import com.rose.lzk.entity.api.ResultCode;
import com.rose.lzk.exception.BussinessException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = {"com.rose.lzk"}) //定义统一的异常处理类,basePackage定义扫描哪些包可不配置
@Log4j2
public class GlobalExceptionHandler {

  /**
   * 处理自定义的业务异常
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(value = {BussinessException.class}) //用来定义函数针对的异常处理类型，可传入多个异常类
  @ResponseBody //返回json数据或对象需添加
  public CommonResult busExceptionHandler(HttpServletRequest req, BussinessException e){
    log.error("发生业务异常！原因是：{}",e.getIErrorCode().getMessage());
    return CommonResult.failed(e.getIErrorCode());
  }

  /**
   * 处理空指针的异常
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(value =NullPointerException.class)
  @ResponseBody
  public CommonResult exceptionHandler(HttpServletRequest req, NullPointerException e){
    log.error("发生空指针异常！原因是:",e);
    return CommonResult.failed(ResultCode.BODY_NOT_MATCH);
  }


  /**
   * 处理其他异常
   * @param req
   * @param e
   * @return
   */
  @ExceptionHandler(value =Exception.class)
  @ResponseBody
  public CommonResult exceptionHandler(HttpServletRequest req, Exception e){
    log.error("未知异常！原因是:",e);
    return CommonResult.failed(ResultCode.INTERNAL_SERVER_ERROR);
  }
}
