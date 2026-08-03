package com.mishalp789.url_shortener.url.repository;

import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.url.entity.Url;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
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

    @Modifying
    @Query("""
    UPDATE Url u
    SET u.clickCount = u.clickCount + 1
    WHERE u.shortCode = :shortCode
    """)
    void incrementClickCount(@Param("shortCode") String shortCode);
    boolean existsByCustomAlias(String customAlias);
    Optional<Url> findByCustomAlias(String customAlias);
    @Query("""
    SELECT COUNT(u)
    FROM Url u
    WHERE u.user = :user
    AND u.expiresAt IS NOT NULL
    AND u.expiresAt < CURRENT_TIMESTAMP
    """)
    long countExpiredUrls(@Param("user") User user);

    @Modifying
    @Transactional
        @Query("""
    UPDATE Url u
    SET u.active = false
    WHERE u.active = true
    AND u.expiresAt IS NOT NULL
    AND u.expiresAt < CURRENT_TIMESTAMP
    """)
    int disableExpiredUrls();

    long count();
    long countByActiveTrue();
    long countByActiveFalse();

    @Query("""
    SELECT COUNT(u)
    FROM Url u
    WHERE u.expiresAt IS NOT NULL
    AND u.expiresAt < CURRENT_TIMESTAMP
    """)
    long countExpiredUrls();
    @Query("""
    SELECT COALESCE(SUM(u.clickCount),0)
    FROM Url u
    """)
    long totalClicks();
    Page<Url> findAll(Pageable pageable);

    @Query("""
    SELECT u
    FROM Url u
    WHERE
    LOWER(u.originalUrl) LIKE LOWER(CONCAT('%', :search, '%'))
    OR
    LOWER(u.shortCode) LIKE LOWER(CONCAT('%', :search, '%'))
    OR
    LOWER(COALESCE(u.customAlias,'')) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Url> searchAllUrls(
            @Param("search") String search,
            Pageable pageable
    );
}
