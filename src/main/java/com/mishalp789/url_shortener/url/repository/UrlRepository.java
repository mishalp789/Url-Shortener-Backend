package com.mishalp789.url_shortener.url.repository;

import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.url.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<Url> findByShortCodeAndActiveTrue(String shortCode);
    List<Url> findAllByUserOrderByCreatedAtDesc(User user);
    Optional<Url> findByIdAndUser(Long id, User user);

}
