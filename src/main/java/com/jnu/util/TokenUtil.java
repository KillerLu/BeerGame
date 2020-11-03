package com.jnu.util;

import com.jnu.view.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 上午11:34
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class TokenUtil {

    private static String signingKey = "killer";

    public static String createToken(Long userId, String name) {
        StringBuilder sb = new StringBuilder();
        String token = Jwts.builder()
                .claim("userId", userId)
                .claim("name", name)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();

        return token;
    }

    public static User parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
            Long userId = Long.parseLong(claims.get("userId").toString());

            User user = new User();
            user.setId(userId);
            if (claims.containsKey("name")) {
                String name = claims.get("name").toString();
                user.setName(name);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getCurrentUser() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }


    public static Long getCurrentUserId() {
        User currentUser=getCurrentUser();
        return currentUser==null?null:currentUser.getId();
    }

    public static void main(String[] args) {
        System.out.println(createToken(5l, "风叔"));
    }
}
