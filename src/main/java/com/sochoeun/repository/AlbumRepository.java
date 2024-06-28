package com.sochoeun.repository;

import com.sochoeun.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
    List<Album> findAllByContentId(Integer contentId);
}
