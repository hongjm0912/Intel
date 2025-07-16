package com.example.demo.login.login_controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // templates/signup.html
    }

    @GetMapping("/gesipan")
    public String showGesipanPage() {
        return "gesipan"; // ⇒ resources/templates/gesipan.html (Thymeleaf) 또는 static/gesipan.html
    }

    // 루트 페이지 요청 처리
    @GetMapping("/")
    public String root() {
        return "redirect:/login"; // 또는 return "index"; 도 가능
    }

}

