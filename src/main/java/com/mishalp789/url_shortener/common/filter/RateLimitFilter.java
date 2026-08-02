package com.mishalp789.url_shortener.common.filter;

import com.mishalp789.url_shortener.common.rate_limit.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/swagger")
                || path.startsWith("/v3")
                || path.startsWith("/actuator")) {

            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();

        Bucket bucket =
                rateLimitService.resolveBucket(ip + ":" + path);

        if (!bucket.tryConsume(1)) {

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

            response.getWriter()
                    .write("Too many requests");

            return;
        }

        filterChain.doFilter(request, response);
    }
}