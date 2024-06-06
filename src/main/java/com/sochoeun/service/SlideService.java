package com.sochoeun.service;

import com.sochoeun.model.Slide;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SlideService {
    Slide createSlide(Slide request);
    List<Slide> getAllSlides();
    Slide getSlide(Integer slideId);
    Slide updateSlide(Integer slideId,Slide request);
    void deleteSlide(Integer slideId);

    String uploadPhoto(Integer slideId, MultipartFile file);
}
