package com.mishalp789.url_shortener.url.repository;

import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.url.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<Url> findByShortCodeAndActiveTrue(String shortCode);
    Page<Url> findAllByUser(User user,Pageable pageable);
    Page<Url> findByUserAndOriginalUrlContainingIgnoreCaseOrUserAndShortCodeContainingIgnoreCase(
            User user1,
            String originalUrl,
            User user2,
            String shortCode,
            Pageable pageable
    );
    Optional<Url> findByIdAndUser(Long id, User user);

}
