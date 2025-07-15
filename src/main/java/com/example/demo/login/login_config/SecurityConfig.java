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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login/**", "/oauth2/**", "/gesipan.html", "/css/**", "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/gesipan.html", true) // ✅ 정적 파일로 리디렉션
                        .failureHandler((request, response, exception) -> {
                            System.out.println("❌ OAuth2 로그인 실패!");
                            exception.printStackTrace(); // 콘솔에 자세한 로그
                            response.sendRedirect("/login?error"); // 필요 시 수정
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



