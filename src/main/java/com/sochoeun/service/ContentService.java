package com.sochoeun.service;

import com.sochoeun.model.Content;
import com.sochoeun.model.request.ContentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentService {
    /*  CRUD */
    Content createContent(ContentRequest request);
    List<Content> getAllContent();
    Content getContent(Integer contentId);
    Content updateContent(Integer contentId,ContentRequest request);


    /*add new */
    List<Content> getAllContentByStats(String status);
    void updateStatus(Integer contentId, String status);

    void deleteContent(Integer contentId);

    List<Content> getAllByArticleId(Integer articleId);

    String uploadPhoto(Integer contentId, MultipartFile file);
}
