package com.example.eum2.member.service;

import com.example.eum2.member.DTO.UserSignupDTO;
import com.example.eum2.member.entity.UserEntity;
import com.example.eum2.member.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final UserRepository userRepository;

    @Transactional
    public void signup(UserSignupDTO dto) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + dto.getEmail());
        }

        UserEntity user = new UserEntity();
        user.setStudent_name(dto.getStudent_name());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        System.out.println("👉 저장 전: " + user);
        userRepository.save(user);
        System.out.println("✅ 저장 완료");
    }
}
