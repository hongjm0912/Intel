package com.example.demo.login.login_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/gesipan")
    public String showGesipanPage() {
        return "gesipan"; // ⇒ resources/templates/gesipan.html (Thymeleaf) 또는 static/gesipan.html
    }
}
