package com.sochoeun.controller;

import com.sochoeun.model.Article;
import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.request.ArticleRequest;
import com.sochoeun.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private BaseResponse baseResponse;

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleRequest request){
        Article article = articleService.createArticle(request);
        baseResponse = new BaseResponse();
        baseResponse.success(article);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllArticles(){
        List<Article> allArticle = articleService.getAllArticle();
        baseResponse = new BaseResponse();
        baseResponse.success(allArticle);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getAllArticles(@PathVariable Integer articleId){
        Article article = articleService.getArticle(articleId);
        baseResponse = new BaseResponse();
        baseResponse.success(article);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<?> updateArticles(@PathVariable Integer articleId,@RequestBody ArticleRequest request){
        Article updated = articleService.updateArticle(articleId,request);
        baseResponse = new BaseResponse();
        baseResponse.success(updated);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticles(@PathVariable Integer articleId){
        articleService.deleteArticle(articleId);
        baseResponse = new BaseResponse();
        baseResponse.success("Article ID: %s deleted".formatted(articleId));
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getAllArticlesByCategoryId(@PathVariable Integer categoryId){
        List<Article> allArticle = articleService.getAllByCategoryId(categoryId);
        baseResponse = new BaseResponse();
        baseResponse.success(allArticle);
        return ResponseEntity.ok(baseResponse);
    }
}
