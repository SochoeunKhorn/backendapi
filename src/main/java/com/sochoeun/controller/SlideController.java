package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Slide;
import com.sochoeun.service.SlideService;
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
@RequestMapping("/api/slides")
@RequiredArgsConstructor
public class SlideController {
    private final SlideService slideService;
    private BaseResponse baseResponse;

    @Value("${application.upload.server.path}"+"/slides/")
    String clientPath;

    @PostMapping
    public ResponseEntity<?> createSlide(@RequestBody Slide request){
        Slide slide = slideService.createSlide(request);
        baseResponse = new BaseResponse();
        baseResponse.success(slide);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllSlides(){
        List<Slide> allSlides = slideService.getAllSlides();
        baseResponse = new BaseResponse();
        baseResponse.success(allSlides);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{slideId}")
    public ResponseEntity<?> getSlide(@PathVariable Integer slideId){
        Slide slide = slideService.getSlide(slideId);
        baseResponse = new BaseResponse();
        baseResponse.success(slide);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{slideId}")
    public ResponseEntity<?> updateSlide(@PathVariable Integer slideId,@RequestBody Slide slide){
        Slide updated = slideService.updateSlide(slideId, slide);
        baseResponse = new BaseResponse();
        baseResponse.success(updated);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{slideId}")
    public ResponseEntity<?> deleteSlide(@PathVariable Integer slideId){
        slideService.deleteSlide(slideId);
        baseResponse = new BaseResponse();
        baseResponse.success("Deleted");
        return ResponseEntity.ok(baseResponse);
    }


    @PutMapping(value = "/upload/photo")
    public ResponseEntity<String> updateUserProfile(
            @RequestParam Integer slideId,
            @RequestParam MultipartFile file){
        String profile = slideService.uploadPhoto(slideId, file);
        return ResponseEntity.ok().body(profile);
    }

    @GetMapping(path = "/photo/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] getProfile(@PathVariable("filename") String filename) throws Exception{
        return Files.readAllBytes(Paths.get(clientPath + filename));
    }
}
