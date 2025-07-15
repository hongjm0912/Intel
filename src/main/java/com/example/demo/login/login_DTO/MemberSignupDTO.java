package com.example.demo.login.login_DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignupDTO {
    private String email;
    private String password;
    private String verificationCode;
}
