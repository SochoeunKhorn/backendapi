package com.sochoeun.exception;

public class ArticleException extends RuntimeException{
    public ArticleException(Integer article_Id){
        super("Article ID: %s can not delete".formatted(article_Id));
    }
}
