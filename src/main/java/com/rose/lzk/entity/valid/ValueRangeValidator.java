package com.rose.lzk.entity.valid;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueRangeValidator implements ConstraintValidator<ValueRange, Object> {

  private String[] values;

  @Override
  public void initialize(ValueRange constraintAnnotation) {
    values = constraintAnnotation.values();
  }

  /**
   * 校验函数
   */
  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
    if (o == null) {
      return true;
    }
    for (String s : values) {
      if (Objects.equals(s, o)) {
        return true;
      }
    }
    return false;
  }
}
