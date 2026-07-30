package com.mishalp789.url_shortener.url.repository;

import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.url.entity.Url;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<Url> findByShortCodeAndActiveTrue(String shortCode);
    Page<Url> findAllByUser(User user,Pageable pageable);
    @Query("""
    SELECT u
    FROM Url u
    WHERE u.user = :user
      AND (
            LOWER(u.originalUrl) LIKE LOWER(CONCAT('%', :search, '%'))
         OR LOWER(u.shortCode) LIKE LOWER(CONCAT('%', :search, '%'))
      )
    """)
    Page<Url> searchUserUrls(
            @Param("user") User user,
            @Param("search") String search,
            Pageable pageable
    );

    Optional<Url> findByIdAndUser(Long id, User user);
    long countByUser(User user);
    long countByUserAndActiveTrue(User user);
    long countByUserAndActiveFalse(User user);

    @Query("""
    SELECT COALESCE(SUM(u.clickCount), 0)
    FROM Url u
    WHERE u.user = :user
    """)
    Long getTotalClicks(@Param("user") User user);

}
