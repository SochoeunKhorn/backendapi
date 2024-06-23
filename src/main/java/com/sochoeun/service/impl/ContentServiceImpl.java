package com.sochoeun.service.impl;

import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.repository.ContentRepository;
import com.sochoeun.model.Article;
import com.sochoeun.model.Content;
import com.sochoeun.model.Media;
import com.sochoeun.model.request.ContentRequest;
import com.sochoeun.service.ArticleService;
import com.sochoeun.service.ContentService;
import com.sochoeun.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final MediaService mediaService;
    private final ArticleService articleService;
    @Value("${application.upload.server.path}"+"/content/")
    String serverPath;
    @Override
    public Content createContent(ContentRequest request) {
        Article article = articleService.getArticle(request.getArticleId());
        Content content = Content.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status("pending")
                .createdAt(LocalDateTime.now())
                .article(article)
                .build();
        return contentRepository.save(content);
    }

    @Override
    public List<Content> getAllContent() {
        List<Content> contentList = contentRepository.findAll();
        return contentList.stream()
                .peek(content -> {
                    Integer contentId = content.getId();
                    List<Media> allByContentId = mediaService.getAllByContentId(contentId);
                    content.setMediaList(allByContentId);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Content> getAllContentByStats(String status) {
        List<Content> allByStatus = contentRepository.findAllByStatus(status);
        return allByStatus.stream()
                .peek(content -> {
                    Integer contentId = content.getId();
                    List<Media> allByContentId = mediaService.getAllByContentId(contentId);
                    content.setMediaList(allByContentId);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Integer contentId, String status) {
        Content content = getContent(contentId);
        content.setStatus(status);
        contentRepository.save(content);
    }

    @Override
    public Content getContent(Integer contentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ResourceNotFoundException("Content ID: %s not found.".formatted(contentId)));
        content.setMediaList(mediaService.getAllByContentId(contentId));
        return content;
    }

    @Override
    public Content updateContent(Integer contentId, ContentRequest request) {
        Article article = articleService.getArticle(request.getArticleId());
        Content content = getContent(contentId);
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setThumbnail(request.getThumbnail());
        content.setArticle(article);
        return contentRepository.save(content);
    }

    @Override
    public void deleteContent(Integer contentId) {
        getContent(contentId);
        contentRepository.deleteById(contentId);
    }

    @Override
    public List<Content> getAllByArticleId(Integer articleId) {
        articleService.getArticle(articleId);
        return contentRepository.findAllByArticle_Id(articleId);
    }

    @Override
    public String uploadPhoto(Integer contentId, MultipartFile file) {
        String photoName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String photoUrl  =photoFunction.apply(photoName,file);
        Content content = getContent(contentId);
        content.setThumbnail(photoUrl);
        contentRepository.save(content);
        return photoUrl;
    }

    private final Function<String,String> fileExtension =
            filename -> Optional.of(filename)
                    .filter(name -> name.contains("."))
                    .map(name
                            -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile,String> photoFunction = (id, image) ->{
        try{
            Path fileStorageLocation = Paths.get(serverPath).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(
                    image.getInputStream(),
                    fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), // filename
                    REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath() // localhost:8080
                    .path("/api/contents/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        }catch (Exception e){
            throw new RuntimeException("Unable to save image");
        }
    };
}
