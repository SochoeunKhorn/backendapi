package com.sochoeun.service.impl;

import com.sochoeun.exception.NotFoundException;
import com.sochoeun.repository.CategoryRepository;
import com.sochoeun.model.Category;
import com.sochoeun.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(Category request) {
        return categoryRepository.save(request);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()->new NotFoundException("Category",categoryId));

    }

    @Override
    public Category updateCategory(Integer categoryId, Category request) {
        // category != null
        Category updated = getCategory(categoryId);
        updated.setNameEn(request.getNameEn());
        updated.setNameKh(request.getNameKh());

        return categoryRepository.save(updated);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        getCategory(categoryId); // get not found -> response: not found
        categoryRepository.deleteById(categoryId);
    }
}
