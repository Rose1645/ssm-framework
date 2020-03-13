package com.rose.lzk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.rose.lzk.dao.UmsAdminLoginLogMapper;
import com.rose.lzk.dao.UmsAdminMapper;
import com.rose.lzk.dao.UmsAdminPermissionRelationMapper;
import com.rose.lzk.dao.UmsAdminRoleRelationMapper;
import com.rose.lzk.entity.bo.AdminUserDetails;
import com.rose.lzk.entity.domain.UmsAdmin;
import com.rose.lzk.entity.domain.UmsAdminLoginLog;
import com.rose.lzk.entity.domain.UmsAdminPermissionRelation;
import com.rose.lzk.entity.domain.UmsAdminRoleRelation;
import com.rose.lzk.entity.domain.UmsPermission;
import com.rose.lzk.entity.domain.UmsRole;
import com.rose.lzk.entity.form.UmsAdminParam;
import com.rose.lzk.entity.form.UpdateAdminPasswordParam;
import com.rose.lzk.service.UmsAdminService;
import com.rose.lzk.utils.JwtTokenUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Log4j2
public class UmsAdminServiceImpl implements UmsAdminService {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UmsAdminMapper umsAdminMapper;
  @Autowired
  private UmsAdminRoleRelationMapper adminRoleRelationMapper;
  @Autowired
  private UmsAdminRoleRelationMapper adminRoleRelationDao;
  @Autowired
  private UmsAdminPermissionRelationMapper adminPermissionRelationMapper;
  @Autowired
  private UmsAdminPermissionRelationMapper adminPermissionRelationDao;
  @Autowired
  private UmsAdminLoginLogMapper loginLogMapper;

  @Override
  public UmsAdmin getAdminByUsername(String username) {
    UmsAdmin umsAdmin = new UmsAdmin();
    umsAdmin.setUsername(username);
    UmsAdmin result = umsAdminMapper.selectOne(umsAdmin);
    return result;
  }

  @Override
  public UmsAdmin register(UmsAdminParam umsAdminParam) {
    UmsAdmin umsAdmin = new UmsAdmin();
    BeanUtils.copyProperties(umsAdminParam, umsAdmin);
    umsAdmin.setCreateTime(new Date());
    umsAdmin.setStatus(1);
    //查询是否有相同用户名的用户
    // 创建Example
    Example example = new Example(UmsAdmin.class);
    //// 创建Criteria
    Criteria criteria = example.createCriteria();
    criteria.andEqualTo("username",umsAdmin.getUsername());
    List<UmsAdmin> umsAdminList = umsAdminMapper.selectByExample(example);
    if (umsAdminList.size() > 0) {
      return null;
    }
    //将密码进行加密操作
    String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
    umsAdmin.setPassword(encodePassword);
    umsAdminMapper.insert(umsAdmin);
    return umsAdmin;
  }

  @Override
  public String login(String username, String password) {
    String token = null;
    //密码需要客户端加密后传递
    try {
      UserDetails userDetails = loadUserByUsername(username);
      if(!passwordEncoder.matches(password,userDetails.getPassword())){
        throw new BadCredentialsException("密码不正确");
      }
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
      token = jwtTokenUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
      insertLoginLog(username);
    } catch (AuthenticationException e) {
      log.warn("登录异常:{}", e.getMessage());
    }
    return token;
  }

  /**
   * 添加登录记录
   * @param username 用户名
   */
  private void insertLoginLog(String username) {
    UmsAdmin admin = getAdminByUsername(username);
    UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
    loginLog.setAdminId(admin.getId());
    loginLog.setCreateTime(new Date());
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    loginLog.setIp(request.getRemoteAddr());
    loginLogMapper.insert(loginLog);
  }

  /**
   * 根据用户名修改登录时间
   */
  private void updateLoginTimeByUsername(String username) {
    UmsAdmin record = new UmsAdmin();
    record.setLoginTime(new Date());
    Example example = new Example(UmsAdmin.class);
    Criteria criteria = example.createCriteria();
    criteria.andEqualTo("username",username);
    umsAdminMapper.updateByExampleSelective(record, example);
  }

  @Override
  public String refreshToken(String oldToken) {
    return jwtTokenUtil.refreshHeadToken(oldToken);
  }

  @Override
  public UmsAdmin getItem(Long id) {
    return umsAdminMapper.selectByPrimaryKey(id);
  }

  @Override
  public List<UmsAdmin> list(String name, Integer pageSize, Integer pageNum) {
    PageHelper.startPage(pageNum, pageSize);
    Example example = new Example(UmsAdmin.class);
    Criteria criteria = example.createCriteria();
    if (!StringUtils.isEmpty(name)) {
      criteria.andLike("username","%" + name + "%");
      example.or(criteria.andLike("nickName","%" + name + "%"));
    }
    return umsAdminMapper.selectByExample(example);
  }

  @Override
  public int update(Long id, UmsAdmin admin) {
    admin.setId(id);
    //密码已经加密处理，需要单独修改
    admin.setPassword(null);
    return umsAdminMapper.updateByPrimaryKeySelective(admin);
  }

  @Override
  public int delete(Long id) {
    return umsAdminMapper.deleteByPrimaryKey(id);
  }

  @Override
  public int updateRole(Long adminId, List<Long> roleIds) {
    int count = roleIds == null ? 0 : roleIds.size();
    //先删除原来的关系
    Example example = new Example(UmsAdminRoleRelation.class);
    Criteria criteria = example.createCriteria();
    criteria.andEqualTo("adminId",adminId);
    adminRoleRelationMapper.deleteByExample(example);
    //建立新关系
    if (!CollectionUtils.isEmpty(roleIds)) {
      List<UmsAdminRoleRelation> list = new ArrayList<>();
      for (Long roleId : roleIds) {
        UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
        roleRelation.setAdminId(adminId);
        roleRelation.setRoleId(roleId);
        list.add(roleRelation);
      }
      adminRoleRelationDao.insertList(list);
    }
    return count;
  }

  @Override
  public List<UmsRole> getRoleList(Long adminId) {
    return adminRoleRelationDao.getRoleList(adminId);
  }

  @Override
  public int updatePermission(Long adminId, List<Long> permissionIds) {
    //删除原所有权限关系
    Example example = new Example(UmsAdminPermissionRelation.class);
    Criteria criteria = example.createCriteria();
    criteria.andEqualTo("adminId",adminId);
    adminPermissionRelationMapper.deleteByExample(example);
    //获取用户所有角色权限
    List<UmsPermission> permissionList = adminRoleRelationDao.getRolePermissionList(adminId);
    List<Long> rolePermissionList = permissionList.stream().map(UmsPermission::getId).collect(
        Collectors.toList());
    if (!CollectionUtils.isEmpty(permissionIds)) {
      List<UmsAdminPermissionRelation> relationList = new ArrayList<>();
      //筛选出+权限
      List<Long> addPermissionIdList = permissionIds.stream().filter(permissionId -> !rolePermissionList.contains(permissionId)).collect(Collectors.toList());
      //筛选出-权限
      List<Long> subPermissionIdList = rolePermissionList.stream().filter(permissionId -> !permissionIds.contains(permissionId)).collect(Collectors.toList());
      //插入+-权限关系
      relationList.addAll(convert(adminId,1,addPermissionIdList));
      relationList.addAll(convert(adminId,-1,subPermissionIdList));
      return adminPermissionRelationDao.insertList(relationList);
    }
    return 0;
  }

  /**
   * 将+-权限关系转化为对象
   */
  private List<UmsAdminPermissionRelation> convert(Long adminId,Integer type,List<Long> permissionIdList) {
    List<UmsAdminPermissionRelation> relationList = permissionIdList.stream().map(permissionId -> {
      UmsAdminPermissionRelation relation = new UmsAdminPermissionRelation();
      relation.setAdminId(adminId);
      relation.setType(type);
      relation.setPermissionId(permissionId);
      return relation;
    }).collect(Collectors.toList());
    return relationList;
  }

  @Override
  public List<UmsPermission> getPermissionList(Long adminId) {
    return adminRoleRelationDao.getPermissionList(adminId);
  }

  @Override
  public int updatePassword(UpdateAdminPasswordParam param) {
    if(StrUtil.isEmpty(param.getUsername())
        ||StrUtil.isEmpty(param.getOldPassword())
        ||StrUtil.isEmpty(param.getNewPassword())){
      return -1;
    }
    Example example = new Example(UmsAdmin.class);
    Criteria criteria = example.createCriteria();
    criteria.andEqualTo("username",param.getUsername());
    List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
    if(CollUtil.isEmpty(adminList)){
      return -2;
    }
    UmsAdmin umsAdmin = adminList.get(0);
    if(!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
      return -3;
    }
    umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
    umsAdminMapper.updateByPrimaryKey(umsAdmin);
    return 1;
  }

  @Override
  public UserDetails loadUserByUsername(String username){
    //获取用户信息
    UmsAdmin admin = getAdminByUsername(username);
    if (admin != null) {
      List<UmsPermission> permissionList = getPermissionList(admin.getId());
      return new AdminUserDetails(admin,permissionList);
    }
    throw new UsernameNotFoundException("用户名或密码错误");
  }
}
