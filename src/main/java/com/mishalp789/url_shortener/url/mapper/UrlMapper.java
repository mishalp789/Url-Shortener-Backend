package com.mishalp789.url_shortener.url.mapper;


import com.mishalp789.url_shortener.url.dto.UrlAnalyticsResponse;
import com.mishalp789.url_shortener.url.dto.UrlResponse;
import com.mishalp789.url_shortener.url.entity.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlResponse toResponse(Url url){
        return UrlResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .shortUrl(baseUrl + "/r/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .build();
    }

    public UrlAnalyticsResponse toAnalytics(Url url) {

        return UrlAnalyticsResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .shortUrl(baseUrl + "/r/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .createdAt(url.getCreatedAt())
                .updatedAt(url.getUpdatedAt())
                .build();
    }


}
