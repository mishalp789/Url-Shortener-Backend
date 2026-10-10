package com.mishalp789.urlshortener.repository;

import com.mishalp789.urlshortener.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
}
