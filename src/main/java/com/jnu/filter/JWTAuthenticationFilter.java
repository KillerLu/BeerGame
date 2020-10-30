package com.jnu.filter;

import com.jnu.util.TokenUtil;
import com.jnu.view.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午11:24
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 从Header中拿到token
        String token = request.getHeader("Authorization");
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = parseToken(token);
        if (authentication == null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication parseToken(String token) {
        User user = TokenUtil.parseToken(token);
        // 返回验证令牌
        return user != null ?
                new UsernamePasswordAuthenticationToken(user, null, null) :
                null;
    }


}
