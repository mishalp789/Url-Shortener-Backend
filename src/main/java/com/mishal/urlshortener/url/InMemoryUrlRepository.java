package com.mishal.urlshortener.url;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUrlRepository implements UrlRepository {
    private final Map<String,Url> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Url url){
        storage.put(url.getShortCode(), url);
    }

    @Override
    public Url find(String shortCode){
        return storage.get(shortCode);
    }

    @Override
    public boolean exists(String shortCode){
        return storage.containsKey(shortCode);
    }

    @Override
    public Page<Url> findAll(Pageable pageable) {

        List<Url> urls = new ArrayList<>(storage.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), urls.size());

        List<Url> pageContent =
                (start > end) ? Collections.emptyList() : urls.subList(start, end);

        return new PageImpl<>(pageContent, pageable, urls.size());
    }

    @Override
    public Page<Url> findByUserUsername(String username, Pageable pageable) {

        List<Url> filtered = storage.values()
                .stream()
                .filter(url ->
                        url.getUser() != null &&
                                url.getUser().getUsername().equals(username))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        List<Url> pageContent =
                (start >= filtered.size())
                        ? Collections.emptyList()
                        : filtered.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filtered.size());
    }



}
