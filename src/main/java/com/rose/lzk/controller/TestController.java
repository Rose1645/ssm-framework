package com.rose.lzk.controller;

import com.rose.lzk.entity.api.CommonResult;
import com.rose.lzk.entity.api.ResultCode;
import com.rose.lzk.entity.dto.UserBo;
import com.rose.lzk.exception.BussinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Api(tags = "test接口模块")
@Log4j2
public class TestController {

  @ApiOperation(value = "接口的功能介绍",notes = "提示接口使用者注意事项",httpMethod = "GET")
  @ApiImplicitParam(dataType = "string",name = "name",value = "姓名",required = true)
  @RequestMapping(value = "/")
  public String index(String name) {
    log.info("hello world.");
    log.error("sss");
    return "hello "+ name;
  }

  @ApiOperation(value = "测试异常抛出返回信息",notes = "无",httpMethod = "GET")
  @ApiImplicitParam(dataType = "string",name = "param",value = "参数",required = true)
  @RequestMapping(value = "exception")
  //@PreAuthorize("hasAuthority('pms:brand:read')")   //springsecurity注解  -授予权限
  public CommonResult testException(String param){
    if("1".equals(param)){
      throw new BussinessException(ResultCode.BUS_EXCEPTION);
    }else{
      throw new NullPointerException();
    }
  }

  @ApiOperation(value = "测试参数验证",notes = "无",httpMethod = "POST")
  @ApiImplicitParam(dataType = "UserBo",name = "user",value = "用户",required = true)
  @PostMapping(value = "valid")
  /**
   *  使用 BindingResult 接收校验结果，自行组织输出内容
   */
  public CommonResult validTest(@Valid @RequestBody UserBo user, BindingResult result){

    if (result.hasErrors()) {
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrors = result.getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        sb.append(fieldError.getDefaultMessage());
        sb.append(",");
      }
      log.debug(sb.toString());
      return CommonResult.failed(sb.toString());
    }
    return CommonResult.success("成功");
  }
}
