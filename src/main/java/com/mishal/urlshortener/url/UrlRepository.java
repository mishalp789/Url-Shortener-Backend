package com.mishal.urlshortener.url;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UrlRepository {

    void save(Url url);
    Url find(String shortCode);
    boolean exists(String shortCode);
    Page<Url> findAll(Pageable pageable);
    Page<Url> findByUserUsername(String username, Pageable pageable);
    
} 
