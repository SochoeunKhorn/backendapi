package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Content;
import com.sochoeun.model.request.ContentRequest;
import com.sochoeun.service.ContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Tag(name = "CONTENTS")
public class ContentController {
    private final ContentService contentService;
    private BaseResponse baseResponse;

    @Value("${application.upload.server.path}"+"/content/")
    String serverPath;

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

    @GetMapping("/status")
    public ResponseEntity<?> getAllContentByStatus(){
        List<Content> allContent = contentService.getAllContentByStats("PUBLISHED");
        baseResponse = new BaseResponse();
        baseResponse.success(allContent);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/update/status/{contentId}")
    public ResponseEntity<?> updateContentStatus(@PathVariable Integer contentId,@RequestParam String status){
        contentService.updateStatus(contentId,status);
        baseResponse = new BaseResponse();
        baseResponse.success("Content ID: %s updated to %s".formatted(contentId,status));
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
        List<Content> allContent = contentService.getAllByArticleId(articleId);
        baseResponse = new BaseResponse();
        baseResponse.success(allContent);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(value = "/upload/image", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(
            @RequestParam Integer contentId,
            @RequestParam MultipartFile file
    ){
        String uploadPhoto = contentService.uploadPhoto(contentId, file);
        return ResponseEntity.ok(uploadPhoto);
    }

    @GetMapping(path = "/image/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] getProfile(@PathVariable("filename") String filename) throws Exception{
        return Files.readAllBytes(Paths.get(serverPath + filename));
    }
}
