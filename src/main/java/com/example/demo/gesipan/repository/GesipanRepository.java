package com.example.demo.gesipan.repository;

import com.example.demo.gesipan.entity.GesipanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GesipanRepository extends JpaRepository<GesipanEntity, Long> {
    List<GesipanEntity> findAllByTitle(String title);
}
