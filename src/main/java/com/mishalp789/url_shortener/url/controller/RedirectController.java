package com.mishalp789.url_shortener.url.controller;

import com.mishalp789.url_shortener.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "URL Redirect")
@RestController
@RequiredArgsConstructor
public class RedirectController {
    private final UrlService urlService;
    @Operation(summary = "Redirect to Original URL")
    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginalUrl(shortCode);
        urlService.incrementClickCount(shortCode);


        return ResponseEntity
                .status(302)
                .header(HttpHeaders.LOCATION,originalUrl)
                .build();
    }
}
