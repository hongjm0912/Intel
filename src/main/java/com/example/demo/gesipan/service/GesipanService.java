package com.example.demo.gesipan.service;

import com.example.demo.gesipan.DTO.GesipanRequestUpdateDTO;
import com.example.demo.gesipan.DTO.GesipanResponseDataDTO;
import com.example.demo.gesipan.DTO.GesipanResponseTitleDTO;
import com.example.demo.gesipan.DTO.GesipanRequestDTO;
import com.example.demo.gesipan.entity.GesipanEntity;
import com.example.demo.gesipan.repository.GesipanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GesipanService {

    private final GesipanRepository gesipanRepository;

    public void savePost(GesipanRequestDTO gesipanResponseDTO) {
        GesipanEntity gesipanEntity = GesipanEntity.builder()
                .title(gesipanResponseDTO.getTitle())
                .content(gesipanResponseDTO.getContent())
                .build();
        gesipanRepository.save(gesipanEntity);
    }

    public List<GesipanResponseTitleDTO> findList() {
        return gesipanRepository.findAll().stream()
                .map(post -> new GesipanResponseTitleDTO(
                        post.getId(),
                        post.getTitle()
                ))
                .collect(Collectors.toList());
    }

    public GesipanResponseDataDTO readPost(Long id) {
        GesipanEntity post = gesipanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return new GesipanResponseDataDTO(
                post.getTitle(),
                post.getContent(),
                post.getImageUrl()
        );
    }

    public void sakjeByTitle(String title) {
        List<GesipanEntity> posts = gesipanRepository.findAllByTitle(title);

        if (posts.isEmpty()) {
            throw new IllegalArgumentException("해당 제목의 게시글이 존재하지 않습니다.");
        }
        gesipanRepository.deleteById(posts.get(0).getId());
    }

    public GesipanRequestUpdateDTO st_sujeong(Long id) {
        GesipanEntity post = gesipanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return new GesipanRequestUpdateDTO(post.getTitle(), post.getContent());
    }

    public void sujeong(Long id, GesipanRequestUpdateDTO dto) {
        GesipanEntity post = gesipanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        gesipanRepository.save(post);
    }

    public void sajin(String title, String content, String imageUrl) {
        GesipanEntity post = GesipanEntity.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();

        gesipanRepository.save(post);
    }


}

