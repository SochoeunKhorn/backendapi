package com.sochoeun.controller;

import com.sochoeun.model.Album;
import com.sochoeun.model.BaseResponse;
import com.sochoeun.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/albums")
public class AlbumController {
    private final AlbumService albumService;
    private BaseResponse baseResponse;
    @Value("${application.upload.server.path}"+"/album/")
    String path;
    @PostMapping(value = "/upload/{contentId}",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadAlbums(@PathVariable Integer contentId, @RequestPart MultipartFile[] albumFiles) {
        albumService.saveAlbum(contentId, albumFiles);
        baseResponse = new BaseResponse();
        baseResponse.success("Albums uploaded successfully");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
    @GetMapping(path = "/image/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] getProfile(@PathVariable("filename") String filename) throws Exception{
        return Files.readAllBytes(Paths.get(path + filename));
    }

    @GetMapping
    public ResponseEntity<?> getAlbums(@RequestParam(required = false) Integer contentId) {
        List<Album> albums = albumService.listAlbums(contentId);
        baseResponse = new BaseResponse();
        baseResponse.success(albums);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbum(@PathVariable String albumId) {
        Album album = albumService.getAlbum(albumId);
        baseResponse = new BaseResponse();
        baseResponse.success(album);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PutMapping("/{albumId}")
    public ResponseEntity<?> updated(@PathVariable String albumId,@RequestBody Album updatedAlbum) {
        Album album = albumService.updateAlbum(albumId,updatedAlbum);
        baseResponse = new BaseResponse();
        baseResponse.success(album);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<?> deleteAlbum(@PathVariable String albumId) {
        albumService.deleteAlbum(albumId);
        baseResponse = new BaseResponse();
        baseResponse.success("Album deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}
