package com.example.demo.gesipan.DTO;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GesipanRequestDTO {
    private Long id;
    private String title;
    private String content;
}
