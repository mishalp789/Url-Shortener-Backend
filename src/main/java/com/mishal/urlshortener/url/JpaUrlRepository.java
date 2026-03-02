package com.mishal.urlshortener.url;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUrlRepository  extends JpaRepository<Url,String> {
    Page<Url> findByUserUsername(String username, Pageable pageable);
}


