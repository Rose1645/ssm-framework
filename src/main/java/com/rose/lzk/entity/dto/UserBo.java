package com.rose.lzk.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rose.lzk.entity.valid.ValueRange;
import java.time.LocalDate;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 实体类添加校验规则示例
 */
public class UserBo {

  /**
   * 用户名，长度在6-16个字符之间，必须参数
   */
  @NotBlank(message = "用户名不能为空")
  @Length(min = 6, max = 16, message = "用户名长度必须在6-16个字符之间")
  private String username;

  /**
   * 出生日期，格式为 yyyy-MM-dd，必须为过去的日期，不必须参数
   */
  @Past(message = "出生日期必须早于当前日期")
  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  private LocalDate birthday;

  /**
   * 等级，整数，0-5之间，必须参数
   */
  @NotNull(message = "用户等级不能为空")
  @Min(value = 0, message = "用户等级最小为0")
  @Max(value = 5, message = "用户等级最大为5")
  @Digits(integer = 1, fraction = 0, message = "用户等级必须为整数")
  private Integer level;

  /**
   * 限定性别的值只能是 0、1、2
   */
  @ValueRange(values = {"0", "1", "2"})
  private Integer sex;
}