package com.example.demo.login.login_DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginDTO {
    private String email;
    private String password;

}
