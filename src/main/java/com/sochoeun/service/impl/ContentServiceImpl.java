package com.sochoeun.service.impl;

import com.sochoeun.exception.NotFoundException;
import com.sochoeun.repository.ContentRepository;
import com.sochoeun.model.Article;
import com.sochoeun.model.Content;
import com.sochoeun.model.Media;
import com.sochoeun.model.request.ContentRequest;
import com.sochoeun.service.ArticleService;
import com.sochoeun.service.ContentService;
import com.sochoeun.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final MediaService mediaService;
    private final ArticleService articleService;
    @Override
    public Content createContent(ContentRequest request) {
        Article article = articleService.getArticle(request.getArticleId());
        Content content = Content.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .article(article)
                .build();
        return contentRepository.save(content);
    }

    @Override
    public List<Content> getAllContent() {
        List<Content> contentList = contentRepository.findAll();
        List<Content> collect = contentList.stream()
                .map(content -> {
                    Integer contentId = content.getId();
                    List<Media> allByContentId = mediaService.getAllByContentId(contentId);
                    content.setMediaList(allByContentId);
                    return content;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Content getContent(Integer contentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new NotFoundException("Content", contentId));
        content.setMediaList(mediaService.getAllByContentId(contentId));
        return content;
    }

    @Override
    public Content updateContent(Integer contentId, ContentRequest request) {
        Article article = articleService.getArticle(request.getArticleId());
        Content content = getContent(contentId);
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setArticle(article);
        return contentRepository.save(content);
    }

    @Override
    public void deleteContent(Integer contentId) {
        getContent(contentId);
        contentRepository.deleteById(contentId);
    }

    @Override
    public List<Content> getAllByArticelId(Integer articleId) {
        articleService.getArticle(articleId);
        return contentRepository.findAllByArticle_Id(articleId);
    }
}
