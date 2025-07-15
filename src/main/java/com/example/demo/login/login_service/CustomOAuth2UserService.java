package com.example.demo.login.login_service;

import com.example.demo.login.login_entity.MemberEntity;
import com.example.demo.login.login_repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String email = oAuth2User.getAttribute("email");

        // DB에 회원 없으면 저장 (회원가입)
        memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(MemberEntity.builder()
                        .email(email)
                        .password("") // 패스워드는 의미없음
                        .build()
                ));

        return oAuth2User;
    }
}
