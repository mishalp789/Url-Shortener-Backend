package com.mishal.urlshortener.url;

import java.net.URI;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/shorten")
    public ResponseEntity<Map<String,String>> shorten(@Valid @RequestBody ShortenRequest request){
        String shortCode = urlService.createShortUrl(
            request.getUrl(),
            request.getExpiresAt(),
            request.getCustomAlias()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "shortCode",shortCode,
            "originalUrl",request.getUrl()
        ));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginalUrl(shortCode);
        

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @GetMapping("/urls/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(@PathVariable String shortCode){
        Url url = urlService.getUrl(shortCode);

        UrlStatsResponse response = new UrlStatsResponse(
            url.getShortCode(),
            url.getOriginalUrl(),
            url.getClickCount(),
            url.getCreatedAt(),
            url.getExpiresAt()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/urls")
    public ResponseEntity<Page<UrlStatsResponse>> getAllUrls(Pageable pageable){
        Page<Url> urls = urlService.getAllUrls(pageable);

        Page<UrlStatsResponse> response = urls.map(url->
                                            new UrlStatsResponse(
                                                    url.getShortCode(),
                                                    url.getOriginalUrl(),
                                                    url.getClickCount(),
                                                    url.getCreatedAt(),
                                                    url.getExpiresAt()
                                            )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public Page<UrlResponse> getMyUrls(Pageable pageable) {
        return urlService.getMyUrls(pageable);
    }
    
    
}
