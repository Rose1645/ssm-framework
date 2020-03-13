package com.rose.lzk.component.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解控制层对外开放的接口是否需要经过jwt的token验证
 * --需要登录才能进行操作的注解UserLoginToken
 * @author 刘志凯
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
  boolean required() default true;
}
