package com.jnu.config;

import com.jnu.filter.JWTAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午11:19
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll();
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }
}
