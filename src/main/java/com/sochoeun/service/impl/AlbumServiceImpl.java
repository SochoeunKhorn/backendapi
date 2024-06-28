package com.sochoeun.service.impl;

import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.model.Album;
import com.sochoeun.repository.AlbumRepository;
import com.sochoeun.repository.ContentRepository;
import com.sochoeun.service.AlbumService;
import com.sochoeun.service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sochoeun.constant.constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    @Value("${application.upload.server.path}"+"/album/")
    String path;

    private final ContentRepository contentRepository;
    @Override
    public void saveAlbum(Integer contentId, MultipartFile[] files) {
        contentRepository.findById(contentId).orElseThrow(()-> new ResourceNotFoundException("Content Not Found."));
        List<String> photoUrls = new ArrayList<>();
        Arrays.asList(files).forEach(file ->{
                    String genName = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    photoUrls.add( photoFunction.apply(genName, file));
                }
                );

        // log.info("urls {}",photoUrls);

        List<Album> collect = photoUrls.stream()
                .map(url -> {
                    Album album = Album.builder()
                            .contentId(contentId)
                            .url(url)
                            .build();
                    albumRepository.save(album);
                    return album;
                }).collect(Collectors.toList());
        albumRepository.saveAll(collect);
    }

    @Override
    public List<Album> listAlbums(Integer contentId) {
        List<Album> allByContentId = albumRepository.findAllByContentId(contentId);
        if (allByContentId.isEmpty()) {
            return albumRepository.findAll();
        }else {
            return allByContentId;
        }
    }

    @Override
    public Album getAlbum(String albumId) {
        return albumRepository.findById(albumId).orElseThrow(() -> new ResourceNotFoundException("Album not found"));
    }

    @Override
    public Album updateAlbum(String albumId, Album album) {
        Album updated = getAlbum(albumId);
        updated.setContentId(album.getContentId());
        updated.setUrl(album.getUrl());
        return albumRepository.save(updated);
    }

    @Override
    public void deleteAlbum(String albumId) {
        getAlbum(albumId);
        albumRepository.deleteById(albumId);
    }

    private final Function<String,String> fileExtension =
            filename -> Optional.of(filename)
                    .filter(name -> name.contains("."))
                    .map(name
                            -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile,String> photoFunction = (id, image) ->{
        try{
            Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(
                    image.getInputStream(),
                    fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), // filename
                    REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath() // localhost:8080
                    .path("/api/albums/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        }catch (Exception e){
            throw new RuntimeException("Unable to save image");
        }
    };
}
