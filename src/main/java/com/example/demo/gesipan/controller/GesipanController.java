package com.example.demo.gesipan.controller;

import com.example.demo.gesipan.DTO.GesipanRequestUpdateDTO;
import com.example.demo.gesipan.DTO.GesipanResponseDataDTO;
import com.example.demo.gesipan.DTO.GesipanResponseTitleDTO;
import com.example.demo.gesipan.DTO.GesipanRequestDTO;
import com.example.demo.gesipan.service.GesipanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gesipan")
@RequiredArgsConstructor
public class GesipanController {

    private final GesipanService gesipanService;

    @PostMapping("/posting")
    public ResponseEntity<String> write(@RequestBody GesipanRequestDTO dto) {
        gesipanService.savePost(dto);
        return ResponseEntity.status(201).body("작성 완료");
    }

    @GetMapping("/list")
    public ResponseEntity<List<GesipanResponseTitleDTO>> read() {
        List<GesipanResponseTitleDTO> list = gesipanService.findList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<GesipanResponseDataDTO> reading(@PathVariable Long id) {
        GesipanResponseDataDTO post = gesipanService.readPost(id);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByTitle(@RequestParam String title) {
        gesipanService.sakjeByTitle(title);
        return ResponseEntity.ok("이 게시글은 지민홍이 처리했으니 안심하라구!");
    }

    // 게시글 열람 (수정폼 채우기용)
    @GetMapping("/update/{id}")
    public ResponseEntity<GesipanRequestUpdateDTO> st_sujeong(@PathVariable Long id) {
        return ResponseEntity.ok(gesipanService.st_sujeong(id));
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<String> sujeong(
            @PathVariable Long id,
            @RequestBody GesipanRequestUpdateDTO dto) {
        gesipanService.sujeong(id, dto);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @PostMapping("/image")
    public ResponseEntity<String> postArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestPart(required = false) MultipartFile image) {

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            // 파일 확장자 검증
            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || !isValidImageFile(originalFilename)) {
                return ResponseEntity.status(400).body("유효하지 않은 이미지 파일입니다.");
            }

            try {
                // upload-dir 디렉토리 생성
                Path uploadDir = Paths.get("upload-dir");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String fileName = UUID.randomUUID() + "_" + originalFilename;
                Path path = uploadDir.resolve(fileName);

                // 파일 저장
                Files.write(path, image.getBytes());
                imageUrl = "/images/" + fileName; // 클라이언트에서 접근할 경로

                System.out.println("이미지 저장 완료: " + path.toAbsolutePath());
                System.out.println("이미지 URL: " + imageUrl);

            } catch (IOException e) {
                System.err.println("이미지 저장 실패: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(500).body("이미지 저장 실패: " + e.getMessage());
            }
        }

        // 서비스로 위임
        gesipanService.sajin(title, content, imageUrl);

        return ResponseEntity.status(201).body("등록 완료");
    }

    // 이미지 파일 확장자 검증 헬퍼 메서드
    private boolean isValidImageFile(String filename) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        String lowerFilename = filename.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerFilename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
