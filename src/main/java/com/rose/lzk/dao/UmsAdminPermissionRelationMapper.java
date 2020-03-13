package com.rose.lzk.dao;

import com.rose.lzk.entity.domain.UmsAdminPermissionRelation;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UmsAdminPermissionRelationMapper extends Mapper<UmsAdminPermissionRelation> {

  int insertList(@Param("list") List<UmsAdminPermissionRelation> list);

}