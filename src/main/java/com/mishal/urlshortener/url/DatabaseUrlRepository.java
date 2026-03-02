package com.mishal.urlshortener.url;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class DatabaseUrlRepository implements UrlRepository {

    private final JpaUrlRepository jpaRepository;

    public DatabaseUrlRepository(JpaUrlRepository jpaUrlRepository){
        this.jpaRepository = jpaUrlRepository;
    }

    @Override
    public void save(Url url){
        jpaRepository.save(url);
    }

    @Override
    public Url find(String shortCode){
        return jpaRepository.findById(shortCode).orElse(null);
    }

    @Override
    public boolean exists(String shortCode){
        return jpaRepository.existsById(shortCode);
    }

    @Override
    public Page<Url> findAll(Pageable pageable){
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Page<Url> findByUserUsername(String username, Pageable pageable) {
        return jpaRepository.findByUserUsername(username, pageable);
    }
}
