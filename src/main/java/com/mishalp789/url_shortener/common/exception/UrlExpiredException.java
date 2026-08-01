package com.mishalp789.url_shortener.common.exception;

public class UrlExpiredException extends RuntimeException{

    public UrlExpiredException(String message){
        super(message);
    }
}
