package com.mishalp789.url_shortener.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlatformDashboardResponse {

    private long totalUsers;
    private long totalUrls;
    private long activeUrls;
    private long inactiveUrls;
    private long expiredUrls;
    private long totalClicks;
}
