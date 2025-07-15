package com.example.demo.login.login_config;

import com.example.demo.login.login_service.CustomOAuth2UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ Spring Security 6 방식
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/signup", "/oauth2/**",
                                "/css/**", "/js/**", "/static/**", "/images/**", "/favicon.ico"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                         // ✅ 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/login")               // ✅ 로그인 폼 action
                        .defaultSuccessUrl("/gesipan", true)        // ✅ 로그인 성공 시 이동
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")                         // ✅ OAuth도 동일한 커스텀 로그인 페이지 사용
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/gesipan", true)        // ✅ OAuth 로그인 성공 시 이동
                        .failureHandler((request, response, exception) -> {
                            System.out.println("❌ OAuth2 로그인 실패!");
                            exception.printStackTrace();
                            response.sendRedirect("/login?error");
                        })
                );

        return http.build();
    }

    @PostConstruct
    public void checkEnv() {
        System.out.println("✅ GOOGLE CLIENT_ID: " + System.getenv("ID"));
        System.out.println("✅ GOOGLE CLIENT_SECRET: " + System.getenv("clients_password"));
    }
}




