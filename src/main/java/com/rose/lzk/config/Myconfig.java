package com.rose.lzk.config;

import com.rose.lzk.component.RestAuthenticationEntryPoint;
import com.rose.lzk.component.RestfulAccessDeniedHandler;
import com.rose.lzk.component.filter.JwtAuthenticationTokenFilter;
import com.rose.lzk.utils.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Myconfig {

}
