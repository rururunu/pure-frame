package com.runu.web_server.config.security;

import com.alibaba.fastjson.JSON;
import com.runu.web_server.tool.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private JedisPool jedisPool;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId;
        try {
            userId = JwtUtil.getJSONObject(token).getStr("uid");
        } catch (Exception e) {
            log.error("token解析失败", e);
            throw new RuntimeException("token解析失败");
        }
        String redisKey = "pure:jwt:" + userId;
        if (!jedisPool.getResource().exists(redisKey)) {
            throw new RuntimeException(userId + "token已过期");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        JSON.parseObject(
                                jedisPool.getResource().get(redisKey),
                                SecurityUserDetail.class
                        ).getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
