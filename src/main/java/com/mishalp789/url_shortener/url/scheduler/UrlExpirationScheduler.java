package com.mishalp789.url_shortener.url.scheduler;

import com.mishalp789.url_shortener.url.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class UrlExpirationScheduler {

    private final UrlRepository urlRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void disableExpiredUrls() {

        int updated = urlRepository.disableExpiredUrls();

        log.info("Disabled {} expired URLs", updated);

    }

}
