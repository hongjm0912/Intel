package com.example.demo.gesipan.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class GesipanImageController {

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("upload-dir", filename);

            System.out.println("이미지 요청: " + filename);
            System.out.println("이미지 경로: " + imagePath.toAbsolutePath());
            System.out.println("파일 존재 여부: " + Files.exists(imagePath));

            if (!Files.exists(imagePath)) {
                System.err.println("이미지 파일을 찾을 수 없습니다: " + imagePath);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(imagePath);

            // 파일 확장자에 따른 Content-Type 설정
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            System.out.println("이미지 서빙 성공: " + filename + ", Content-Type: " + contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (IOException e) {
            System.err.println("이미지 서빙 중 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

