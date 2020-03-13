package com.rose.lzk.entity.bo;

import com.rose.lzk.entity.domain.UmsAdmin;
import com.rose.lzk.entity.domain.UmsPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * SpringSecurity需要的用户详情
 *  UserDetails -- SpringSecurity定义用于封装用户信息的类（主要是用户信息和权限），需要自行实现；
 */
public class AdminUserDetails implements UserDetails {
    private UmsAdmin umsAdmin;
    private List<UmsPermission> permissionList;

    public AdminUserDetails(UmsAdmin umsAdmin) {
        this.umsAdmin = umsAdmin;
    }

    public AdminUserDetails(UmsAdmin umsAdmin,List<UmsPermission> permissionList) {
        this.umsAdmin = umsAdmin;
        if(null == permissionList){
            permissionList = new ArrayList<UmsPermission>();
        }
        this.permissionList = permissionList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return permissionList.stream()
            .filter(permission -> permission.getValue()!=null)
            .map(permission ->new SimpleGrantedAuthority(permission.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus().equals(1);
    }
}
