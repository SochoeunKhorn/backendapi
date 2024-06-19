package com.sochoeun.service;

import com.sochoeun.model.Content;
import com.sochoeun.model.request.ContentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentService {
    Content createContent(ContentRequest request);
    List<Content> getAllContent();
    List<Content> getAllContentByStats(String status);
    Content getContent(Integer contentId);
    Content updateContent(Integer contentId,ContentRequest request);
    void deleteContent(Integer contentId);

    List<Content> getAllByArticelId(Integer articleId);

    String uploadPhoto(Integer contentId, MultipartFile file);
}
