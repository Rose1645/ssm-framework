package com.rose.lzk.dao;

import com.rose.lzk.entity.domain.UmsAdminRoleRelation;
import com.rose.lzk.entity.domain.UmsPermission;
import com.rose.lzk.entity.domain.UmsRole;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UmsAdminRoleRelationMapper extends Mapper<UmsAdminRoleRelation> {

  /**
   * 批量插入用户角色关系
   */
  int insertList(@Param("list") List<UmsAdminRoleRelation> adminRoleRelationList);

  /**
   * 获取用于所有角色
   */
  List<UmsRole> getRoleList(@Param("adminId") Long adminId);

  /**
   * 获取用户所有角色权限
   */
  List<UmsPermission> getRolePermissionList(@Param("adminId") Long adminId);

  /**
   * 获取用户所有权限(包括+-权限)
   */
  List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);
}