package com.example.demo.login.login_controller;

import com.example.demo.login.login_DTO.MemberLoginDTO;
import com.example.demo.login.login_DTO.MemberLoginResponseDTO;
import com.example.demo.login.login_DTO.MemberSignupDTO;
import com.example.demo.login.login_service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/injeung")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody MemberSignupDTO dto) {
        try {
            memberService.hwewongaip(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDTO> login(@RequestBody @Valid MemberLoginDTO dto) {
        String token = memberService.login(dto);
        return ResponseEntity.ok(new MemberLoginResponseDTO(token));
    }
}
