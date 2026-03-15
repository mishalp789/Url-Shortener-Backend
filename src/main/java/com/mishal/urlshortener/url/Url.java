package com.mishal.urlshortener.url;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mishal.urlshortener.auth.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(
        name = "urls",
        indexes = {
                @Index(name = "idx_short_code", columnList = "shortCode"),
                @Index(name = "idx_expiry", columnList = "expiresAt"),
                @Index(name = "idx_created_at", columnList = "createdAt")
        }
)
public class Url {
    @Id
    private String shortCode;
    @Column(nullable = false,length = 2048)
    private String originalUrl;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private long clickCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    protected Url(){}

    public Url(String shortCode,String originalUrl,LocalDateTime expiresAt,User user){
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
        this.clickCount = 0;
        this.user = user;
    }



    public String getShortCode(){
        return shortCode;
    }

    public String getOriginalUrl(){
        return originalUrl;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public long getClickCount(){
        return clickCount;
    }
    public void incrementClickCount(){
        this.clickCount++;
    }

    public boolean isExpired(){
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public User getUser() {
        return user;
    }
}
