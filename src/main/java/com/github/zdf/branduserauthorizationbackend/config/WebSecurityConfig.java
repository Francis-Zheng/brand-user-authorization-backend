package com.github.zdf.branduserauthorizationbackend.config;

import com.github.zdf.branduserauthorizationbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别的权限控制，在需要权限控制的类上添加
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean(name = "passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().mvcMatchers("**").permitAll()
                .mvcMatchers("/swagger-ui.html").permitAll()
                .and().formLogin();
    }
}
