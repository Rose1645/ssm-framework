package com.rose.lzk.exception;

import com.rose.lzk.entity.api.IErrorCode;
import com.rose.lzk.entity.api.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BussinessException extends RuntimeException {


  /**
   * 业务异常枚举
   */
  protected IErrorCode iErrorCode;
}
