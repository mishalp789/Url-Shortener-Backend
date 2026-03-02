package com.mishal.urlshortener.common;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String shortCode){
        super("Short URL not found: " + shortCode);
    }
    
}
