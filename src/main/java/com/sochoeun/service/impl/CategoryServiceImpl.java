package com.sochoeun.service.impl;

import com.sochoeun.exception.CategoryException;
import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.model.Article;
import com.sochoeun.repository.ArticleRepository;
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
    private final ArticleRepository articleRepository;
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
        return categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ID: %s not found.".formatted(categoryId)));

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
        getCategory(categoryId);// get not found -> response: not found
        List<Article> allByCategoryId = articleRepository.findAllByCategory_Id(categoryId);

        if (!allByCategoryId.isEmpty()){
            throw new CategoryException(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}
