package com.sochoeun.service.impl;

import com.sochoeun.exception.ArticleException;
import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.model.Content;
import com.sochoeun.repository.ArticleRepository;
import com.sochoeun.model.Article;
import com.sochoeun.model.Category;
import com.sochoeun.model.request.ArticleRequest;
import com.sochoeun.repository.ContentRepository;
import com.sochoeun.service.ArticleService;
import com.sochoeun.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;
    private final ContentRepository contentRepository;
    @Override
    public Article createArticle(ArticleRequest request) {
        Category category = categoryService.getCategory(request.getCategoryId());

        Article getRequest = Article.builder()
                .name(request.getName())
                .category(category)
                .build();
        return articleRepository.save(getRequest);
    }

    @Override
    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }

    @Override
    public Article getArticle(Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(()->new ResourceNotFoundException("Article ID:%s not found.".formatted(articleId)));
    }

    @Override
    public Article updateArticle(Integer articleId, ArticleRequest request) {
        Article article = getArticle(articleId);
        Category category = categoryService.getCategory(request.getCategoryId());

        article.setName(request.getName());
        article.setCategory(category);
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Integer articleId) {
        getArticle(articleId);
        List<Content> allByArticleId = contentRepository.findAllByArticle_Id(articleId);
        if (!allByArticleId.isEmpty()){
            throw new ArticleException(articleId);
        }
        articleRepository.deleteById(articleId);
    }

    @Override
    public List<Article> getAllByCategoryId(Integer categoryId) {
        categoryService.getCategory(categoryId);
        return articleRepository.findAllByCategory_Id(categoryId);
    }
}
