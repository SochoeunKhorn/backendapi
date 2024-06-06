package com.sochoeun.repository;

import com.sochoeun.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media,Integer> {
    // findAllByMediaType: Image or Video
    List<Media> findAllByMediaTypeContainingIgnoreCase(String type);
    // findAllByContentId
    List<Media> findAllByContentId(Integer contentId);
}
