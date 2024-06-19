package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Media;
import com.sochoeun.service.ContentService;
import com.sochoeun.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.*;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final ContentService contentService;
    private BaseResponse baseResponse;

    @Value("${application.upload.server.path}"+"/media/")
    String serverPath;
    @PostMapping
    public ResponseEntity<?> createMedia(@RequestBody Media request){
        contentService.getContent(request.getContentId());
        Media newMedia = mediaService.createMedia(request);
        baseResponse = new BaseResponse();
        baseResponse.success(newMedia);
        return ResponseEntity.ok(baseResponse);
    }

    /*
    @GetMapping("/content/{contentId}")
    public ResponseEntity<?> getAllMediaByContentId(@PathVariable Integer contentId){
        List<Media> allByContentId = mediaService.getAllByContentId(contentId);
        baseResponse = new BaseResponse();
        baseResponse.success(allByContentId);
        return ResponseEntity.ok(allByContentId);
    }

    @GetMapping("/mediaType/{type}")
    public ResponseEntity<?> getAllMediaByMediaType(@PathVariable String type){
        List<Media> allByMediaType = mediaService.getAllByMediaType(type);
        baseResponse = new BaseResponse();
        baseResponse.success(allByMediaType);
        return ResponseEntity.ok(baseResponse);
    }*/

    @GetMapping()
    public ResponseEntity<?> getAllByContentIdAndMediaType(
            @RequestParam(required = false) Integer contentId,
            @RequestParam(required = false) String mediaType
            ){
        List<Media> getAll ;
        if (contentId == null){
            if (mediaType!= null){
                getAll = mediaService.getAllByMediaType(mediaType);
            }else {
                getAll = mediaService.getAll();
            }
        }else {
            if (mediaType != null){
                getAll = mediaService.getAllFilterByContentIdOrMediaType(contentId,mediaType);
            }else {
                getAll = mediaService.getAllByContentId(contentId);
            }
        }
        baseResponse = new BaseResponse();
        baseResponse.success(getAll);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(value = "/upload/image",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadMedia(
            @RequestParam Integer mediaId,
            @RequestParam MultipartFile file){
        String profile = mediaService.uploadMedia(mediaId, file);
        return ResponseEntity.ok(profile);
    }

    @GetMapping(path = "/image/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE,ALL_VALUE})
    public byte[] getProfile(@PathVariable("filename") String filename) throws Exception{
        return Files.readAllBytes(Paths.get(serverPath + filename));
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable Integer mediaId){
        mediaService.deleteMedia(mediaId);
        baseResponse = new BaseResponse();
        baseResponse.success("Media ID: %s deleted".formatted(mediaId));
        return ResponseEntity.ok(baseResponse);
    }
}
