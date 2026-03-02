package com.mishal.urlshortener.analytics;

import com.mishal.urlshortener.url.Url;
import com.mishal.urlshortener.url.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final UrlRepository urlRepository;

    public AnalyticsService(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }

    @Async
    @EventListener
    public void handleUrlClicked(UrlClickedEvent event){
       Url url = urlRepository.find(event.getShortCode());
       if(url!=null){
           url.incrementClickCount();
           urlRepository.save(url);

           log.debug("Event processed for {}",event.getShortCode());
       }
    }
}
