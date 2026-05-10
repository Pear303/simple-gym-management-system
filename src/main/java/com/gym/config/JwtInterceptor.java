package com.gym.config;

import com.gym.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        // 判断是否存在且格式正确 (Bearer <token>)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token无效\"}");
            return false; // 拦截请求
        }

        // 验证 Token
        String token = authHeader.substring(7);  // 去掉"Bearer "前缀，否则JWT库无法解析无法识别，永远返回401错误！！
        if (!JwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token已过期或无效\"}");
            return false; // 拦截请求
        }

        // 将用户信息存入 request，供 Controller 使用
        Long userId = JwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true; // 放行
    }
}