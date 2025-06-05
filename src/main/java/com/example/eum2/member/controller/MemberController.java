package com.example.eum2.member.controller;

import com.example.eum2.member.DTO.LoginResponseDTO;
import com.example.eum2.member.DTO.UserLoginDTO;
import com.example.eum2.member.DTO.UserSignupDTO;
import com.example.eum2.member.JWT.JWTUtil;
import com.example.eum2.member.entity.UserEntity;
import com.example.eum2.member.repository.UserRepository;
import com.example.eum2.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342")
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MemberController {

    private final UserRepository userRepository;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    // 회원가입 폼 페이지
    @GetMapping("/save")
    public String saveForm() {
        return "save"; // templates/save.html
    }

    // HTML Form 회원가입 처리
    @PostMapping("/save")
    public String save(@ModelAttribute UserSignupDTO dto) {
        UserEntity user = new UserEntity();
        user.setStudent_name(dto.getStudent_name());
        user.setEmail(dto.getEmail().toLowerCase());  // 이메일 소문자 저장 필수
        user.setPassword(dto.getPassword());
        userRepository.save(user);
        return "index"; // templates/index.html
    }

    // API 회원가입 (JSON 요청)
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody UserSignupDTO dto) {
        try {
            dto.setEmail(dto.getEmail().toLowerCase());
            memberService.signup(dto);
            return ResponseEntity.status(201).body("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        String email = dto.getEmail().toLowerCase();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (user.getPassword().equals(dto.getPassword())) {
                String token = jwtUtil.generateToken(user); // JWT 발급
                LoginResponseDTO responseDTO = new LoginResponseDTO(token);
                return ResponseEntity.ok().body(responseDTO);
            } else {
                return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.status(404).body("존재하지 않는 이메일입니다.");
        }
    }
}



