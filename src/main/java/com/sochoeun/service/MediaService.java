package com.sochoeun.service;

import com.sochoeun.model.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    Media createMedia(Media request);
    List<Media> getAll();
    List<Media> getAllByContentId(Integer contentId);
    List<Media> getAllByMediaType(String type);
    List<Media> getAllFilterByContentIdOrMediaType(Integer contentId, String mediaType);
    Media getMedia(Integer id);

    String uploadMedia(Integer mediaId, MultipartFile file);

    void deleteMedia(Integer mediaId);
}
