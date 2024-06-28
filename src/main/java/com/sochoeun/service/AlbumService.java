package com.sochoeun.service;

import com.sochoeun.model.Album;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AlbumService {
    void saveAlbum(Integer contentId,MultipartFile[] files);

    List<Album> listAlbums(Integer contentId);

    Album getAlbum(String albumId);

    Album updateAlbum(String albumId, Album album);

    void deleteAlbum(String albumId);

}
