package com.example.demo.gesipan.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GesipanRequestUpdateDTO {
    private String title;
    private String content;
}
