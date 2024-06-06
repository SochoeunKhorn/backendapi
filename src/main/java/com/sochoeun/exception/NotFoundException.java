package com.sochoeun.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String name,Integer id){
        super("%s ID: '%s' not found".formatted(name,id));
    }
}
