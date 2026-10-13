package com.mishalp789.urlshortener.controller;

import com.mishalp789.urlshortener.dto.CreateUrlRequest;
import com.mishalp789.urlshortener.dto.UrlResponse;
import com.mishalp789.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    }

    @PostMapping
    public UrlResponse createShortUrl(@Valid @RequestBody CreateUrlRequest request){
        return urlService.createShortUrl(request);
    }
}
