package com.rose.lzk.entity.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 自定义校验参数规则注解步骤：
 *  1、创建一个注解，其中 @Constraint 用于指向该注册将使用的自定义校验类
 *  2、创建自定义校验类实现ConstraintValidator接口
 *  3、自定义校验类中写校验规则  -isVaild（）
 *  4、使用自定义注解
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValueRangeValidator.class)
public @interface ValueRange {

  String[] values();

  String message() default "值不正确";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}
