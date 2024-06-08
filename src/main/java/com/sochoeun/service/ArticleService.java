package com.sochoeun.service;

import com.sochoeun.model.Article;
import com.sochoeun.model.request.ArticleRequest;

import java.util.List;

public interface ArticleService {
    Article createArticle(ArticleRequest request);
    List<Article> getAllArticle();
    Article getArticle(Integer articleId);
    Article updateArticle(Integer articleId,ArticleRequest request);
    void deleteArticle(Integer articleId);

    List<Article> getAllByCategoryId(Integer categoryId);
}
