package com.example.demo.login.login_service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    // 이메일 - 인증코드 저장소 (간단 구현: 메모리 Map)
    private final Map<String, String> verificationCodes = new HashMap<>();

    // 인증 코드 생성 및 이메일 발송
    public void sendVerificationCode(String email) {
        String code = generateCode();
        verificationCodes.put(email, code); // 저장

        // 이메일 메시지 구성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[이메일 인증] 회원가입 인증 코드");
        message.setText("아래 인증 코드를 입력해주세요:\n\n" + code);

        mailSender.send(message);
    }

    // 인증 코드 확인
    public boolean verifyCode(String email, String inputCode) {
        return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(inputCode);
    }

    // 6자리 인증 코드 생성
    private String generateCode() {
        Random random = new Random();
        int number = 100_000 + random.nextInt(900_000); // 100000 ~ 999999
        return String.valueOf(number);
    }
}
