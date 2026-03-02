package com.mishal.urlshortener.analytics;

public class UrlClickedEvent {
    private final String shortCode;

    public UrlClickedEvent(String shortCode){
        this.shortCode = shortCode;
    }

    public String getShortCode(){
        return shortCode;
    }
}
