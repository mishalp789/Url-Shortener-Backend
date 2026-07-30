package com.mishalp789.url_shortener.url.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private long totalUrls;
    private long activeUrls;
    private long inactiveUrls;
    private long totalClicks;
}
