package com.example.demo.gesipan.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GesipanResponseDataDTO {
    private String title;
    private String content;
    private String imageUrl;
}
