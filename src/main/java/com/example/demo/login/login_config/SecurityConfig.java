package com.example.demo.login.login_config;

import com.example.demo.login.login_repository.MemberRepository;
import com.example.demo.login.login_service.CustomOAuth2UserService;
import com.example.demo.login.login_service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final MemberRepository memberRepository;

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (API 용도 또는 별도 설정 필요 시 조정)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/signup", "/form-login", "/oauth2/**",
                                "/css/**", "/js/**", "/static/**", "/images/**", "/favicon.ico"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")           // 커스텀 로그인 페이지 경로
                        .loginProcessingUrl("/form-login") // 로그인 처리 URL
                        .defaultSuccessUrl("/gesipan", true)
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")           // OAuth2 로그인 시에도 같은 로그인 페이지 사용
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 등록
                        )
                        .defaultSuccessUrl("/gesipan", true)
                        .failureHandler((request, response, exception) -> {
                            System.out.println("❌ OAuth2 로그인 실패!");
                            exception.printStackTrace();
                            response.sendRedirect("/login?error");
                        })
                );

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }

}





