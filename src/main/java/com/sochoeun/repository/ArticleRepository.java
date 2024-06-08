package com.sochoeun.repository;

import com.sochoeun.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    // findAllArticleByCategoryId
    List<Article> findAllByCategory_Id(Integer categoryId);
    //
}
