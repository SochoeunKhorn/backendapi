package com.sochoeun.service;

import com.sochoeun.model.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    Media createMedia(Media request);
    List<Media> getAllByContentId(Integer contentId);

    Media getMedia(Integer id);

    List<Media> getAllByMediaType(String type);

    String uploadMedia(Integer mediaId, MultipartFile file);

    void deleteMedia(Integer mediaId);
}
