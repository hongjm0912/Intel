package com.example.demo.login.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // ✅ 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets; // ✅ 추가
import java.security.Key; // ✅ 추가
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2시간

    // ✅ 문자열 키를 안전한 Key 객체로 변환
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ signWith에 문자열 대신 Key 객체 사용
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 🔄 변경됨
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ parser에 문자열 대신 Key 객체 사용
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()) // 🔄 변경됨
                .parseClaimsJws(token)
                .getBody();
    }
}
