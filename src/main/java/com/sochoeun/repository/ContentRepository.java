package com.sochoeun.repository;

import com.sochoeun.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content,Integer> {
    // findAllByArticleId
    List<Content> findAllByArticle_Id(Integer articleId);

    List<Content> findAllByStatus(String status);

}
