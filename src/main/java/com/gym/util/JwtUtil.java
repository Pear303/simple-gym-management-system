package com.gym.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    // ⚠️⚠️⚠️ 密钥：实际项目中应配置在 application.yml 中，这里为了演示硬编码
    // 长度必须足够长（至少32字节），否则报错
    private static final String SECRET_KEY = "mySuperSecretKeyForGymManagementSystem1234567890!@#$%^&*";

    // Token 有效期 2 小时
    private static final long EXPIRATION_TIME = 60*60*1000 * 2;

    // 生成 SecretKey对象
    private static SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 生成 Token
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT字符串
     */
    public static String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)       // 存入自定义数据
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // 签名算法
                .compact();
    }

    /**
     * 解析 Token
     * Claims：有效载荷；Token：加密的JWT字符串
     * @param token JWT字符串
     * @return Claims对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证 Token 是否有效
     * @param token JWT字符串
     * @return true/false
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 中获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        try {
            if(!validateToken(token)){
                throw new Exception("Token 无效");
            } else if (token == null) {
                throw new Exception("Token 为空");
            }
            Claims claims = parseToken(token);
            return Long.valueOf(claims.get("userId").toString());
        } catch (Exception e) {
            return null;
        }
    }

}
