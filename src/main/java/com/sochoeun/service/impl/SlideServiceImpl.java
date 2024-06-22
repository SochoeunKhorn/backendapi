package com.sochoeun.service.impl;

import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.repository.SlideRepository;
import com.sochoeun.model.Slide;
import com.sochoeun.service.SlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.sochoeun.constant.constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
public class SlideServiceImpl implements SlideService {
    private final SlideRepository slideRepository;

    //@Value("${application.upload.server.path}"+"/slide/")
    String serverPath=PHOTO_DIRECTORY;
    // CRUD
    @Override
    public Slide createSlide(Slide request) {
        return slideRepository.save(request);
    }

    @Override
    public List<Slide> getAllSlides() {
        return slideRepository.findAll();
    }

    @Override
    public Slide getSlide(Integer slideId) {
        return slideRepository.findById(slideId).orElseThrow(()->new ResourceNotFoundException("Slide ID: %s not found.".formatted(slideId)));
    }

    @Override
    public Slide updateSlide(Integer slideId, Slide request) {
        // id = 1 -> update
        Slide updateSlide = getSlide(slideId);  // ->updateSlide: id=1
        updateSlide.setName(request.getName());
        updateSlide.setDescription(request.getDescription());
        updateSlide.setImageUrl(request.getImageUrl());

        return slideRepository.save(updateSlide);
    }

    @Override
    public void deleteSlide(Integer slideId) {
        getSlide(slideId);
        slideRepository.deleteById(slideId);
    }

    @Override
    public String uploadPhoto(Integer slideId, MultipartFile file) {
        Slide slide = getSlide(slideId);
        String photoName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String photoUrl = photoFunction.apply(photoName,file);
        slide.setImageUrl(photoUrl);
        slideRepository.save(slide);
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
                    .path("/api/slides/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        }catch (Exception e){
            throw new RuntimeException("Unable to save image");
        }
    };

}
