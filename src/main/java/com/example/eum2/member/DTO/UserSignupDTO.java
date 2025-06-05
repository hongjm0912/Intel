package com.example.eum2.member.DTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignupDTO {
    private Long student_id;
    private String student_name; // 사용자 이름
    private String email;       // 이메일
    private String password;    // 비밀번호
}