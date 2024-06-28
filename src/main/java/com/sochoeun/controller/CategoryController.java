package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Category;
import com.sochoeun.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "CATEGORY")
public class CategoryController {
    private final CategoryService categoryService;
    private BaseResponse baseResponse;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category request){
            Category category = categoryService.createCategory(request);
            baseResponse = new BaseResponse();
            baseResponse.success(category);
            return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategory(){
            List<Category> allCategory = categoryService.getAllCategory();
            baseResponse = new BaseResponse();
            baseResponse.success(allCategory);
            return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Integer categoryId){

            Category category = categoryService.getCategory(categoryId);
            baseResponse = new BaseResponse();
            baseResponse.success(category);
            return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId,@RequestBody Category request){

            Category category = categoryService.updateCategory(categoryId, request);
            baseResponse = new BaseResponse();
            baseResponse.success(category);
            return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
            categoryService.deleteCategory(categoryId);
            baseResponse = new BaseResponse();
            baseResponse.success("Deleted");
            return ResponseEntity.ok(baseResponse);
    }
}
