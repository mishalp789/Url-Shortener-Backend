package com.mishal.urlshortener.infrastructure.ratelimit;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component public class RateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private Bucket createBucket(){
        Bandwidth limit = Bandwidth.classic(10,
                Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
    private Bucket resolveBucket(String key){
        return buckets.computeIfAbsent(key,k->createBucket());
    }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        String path = request.getRequestURI();


        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator")) {

            filterChain.doFilter(request, response);
            return;
        }

        String user = request.getRemoteAddr();
        Bucket bucket = resolveBucket(user);
        if(bucket.tryConsume(1)){
            filterChain.doFilter(request,response);
        }
        else{
            response.setStatus(429);
            response.getWriter().write("Too many requests");
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth");
    }
}