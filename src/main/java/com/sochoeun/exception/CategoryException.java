package com.sochoeun.exception;

public class CategoryException extends RuntimeException{
    public CategoryException(Integer categoryId){
        super("Category ID: %s can not delete".formatted(categoryId));
    }
}
