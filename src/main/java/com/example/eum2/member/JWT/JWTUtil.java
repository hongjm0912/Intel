package com.example.eum2.member.JWT;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.eum2.member.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JWTUtil {

    // 비밀 키 (환경변수로 분리하는 것이 좋음)
    @Value("${jwt.secret}")
    private String SECRET_KEY;


    // 토큰 만료 시간 (1시간)
    private static final long EXPIRATION_TIME = 3600_000;




    // JWT 토큰 생성 메서드
    public String generateToken(UserEntity user) {
        Date now = new Date();
        System.out.println("시크릿 키 : " + SECRET_KEY);

        return JWT.create()
                .withIssuer("myServer") // 발급자
                .withSubject(String.valueOf(user.getStudent_id())) // 사용자 식별자
                .withClaim("name", user.getStudent_name()) // 이름 포함
                .withIssuedAt(now) // 발급 시간
                .withExpiresAt(new Date(now.getTime() + EXPIRATION_TIME)) // 만료 시간
                .sign(Algorithm.HMAC256(SECRET_KEY)); // HMAC 서명
    }

    // (선택) 토큰 유효성 검사 및 파싱
    public String validateAndGetSubject(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withIssuer("myServer")
                .build()
                .verify(token)
                .getSubject(); // → student_id 반환
    }
}
