package com.mishalp789.url_shortener.admin.service;

import com.mishalp789.url_shortener.admin.dto.AdminUserResponse;
import com.mishalp789.url_shortener.admin.dto.PlatformDashboardResponse;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.url.dto.PageResponse;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UrlRepository urlRepository;

    public PlatformDashboardResponse dashboard() {

        return PlatformDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalUrls(urlRepository.count())
                .activeUrls(urlRepository.countByActiveTrue())
                .inactiveUrls(urlRepository.countByActiveFalse())
                .expiredUrls(urlRepository.countExpiredUrls())
                .totalClicks(urlRepository.totalClicks())
                .build();
    }

    private AdminUserResponse map(User user){
        return AdminUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();

    }

    public PageResponse<AdminUserResponse> getUsers(Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);

        return PageResponse.<AdminUserResponse>builder()
                .content(
                        page.getContent()
                                .stream()
                                .map(this::map)
                                .toList()
                )
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
