package com.rose.lzk.entity.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name="teacher")
@Getter
@Setter
public class Teacher extends BaseDomain {

  @Id
  @Column(name = "tno")
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
  private String tno;
  private  String tname;
  private String tsex;
  private Date tbitthday;
  private String prof; //教师职称
  private  String depart;
}
