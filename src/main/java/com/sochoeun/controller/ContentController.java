package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Content;
import com.sochoeun.model.request.ContentRequest;
import com.sochoeun.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private BaseResponse baseResponse;

    @PostMapping
    public ResponseEntity<?> createContent(@RequestBody ContentRequest request){
        Content content = contentService.createContent(request);
        baseResponse = new BaseResponse();
        baseResponse.success(content);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllContent(){
        List<Content> allContent = contentService.getAllContent();
        baseResponse = new BaseResponse();
        baseResponse.success(allContent);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> getContent(@PathVariable Integer contentId){
        Content content = contentService.getContent(contentId);
        baseResponse = new BaseResponse();
        baseResponse.success(content);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<?> updateContent(@PathVariable Integer contentId,@RequestBody ContentRequest request){
        Content content = contentService.updateContent(contentId,request);
        baseResponse = new BaseResponse();
        baseResponse.success(content);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deleteContent(@PathVariable Integer contentId){
        contentService.deleteContent(contentId);
        baseResponse = new BaseResponse();
        baseResponse.success("Content ID: %s deleted".formatted(contentId));
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> getAllContent(@PathVariable Integer articleId){
        List<Content> allContent = contentService.getAllByArticelId(articleId);
        baseResponse = new BaseResponse();
        baseResponse.success(allContent);
        return ResponseEntity.ok(baseResponse);
    }
}
