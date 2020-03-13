package com.rose.lzk.entity.domain;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// @Table指定该实体类对应的表名,如表名为base_customer,类名为BaseCustomer可以不需要此注解
@Table(name = "t_base_customer")
@Getter
@Setter
public class Customer {

  // @Id表示该字段对应数据库表的主键id
  // @GeneratedValue中strategy表示使用数据库自带的主键生成策略.
  // @GeneratedValue中generator配置为"JDBC",在数据插入完毕之后,会自动将主键id填充到实体类中.类似普通mapper.xml中配置的selectKey标签
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private Long id;

  private String name;

  private String code;

  private String status;

  private String linkman;

  private String linkmanPhone;

  private String remark;

  private String attr01;

  private String attr02;

  private String attr03;

  private Date createDate;

  private Date lastUpdate;

  private Long creater;

  private Long lastUpdateMan;
}