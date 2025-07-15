package com.example.demo.login.login_service;

import com.example.demo.login.JWT.JWTUtil;
import com.example.demo.login.login_DTO.MemberLoginDTO;
import com.example.demo.login.login_DTO.MemberSignupDTO;
import com.example.demo.login.login_entity.MemberEntity;
import com.example.demo.login.login_repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final JavaMailSender mailSender;
    private final EmailService emailService;

    // 회원가입
    public void hwewongaip(MemberSignupDTO dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        boolean isVerified = emailService.verifyCode(dto.getEmail(), dto.getVerificationCode()); // ✅ 이메일 인증 확인
        if (!isVerified) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        MemberEntity member = MemberEntity.builder()
                .email(dto.getEmail())
                .password(dto.getPassword()) // 실무에서는 BCrypt 암호화!
                .build();

        memberRepository.save(member);
    }

    // 로그인
    public String login(MemberLoginDTO dto) {
        MemberEntity member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(member.getEmail()); // JWT 발급
    }
}
