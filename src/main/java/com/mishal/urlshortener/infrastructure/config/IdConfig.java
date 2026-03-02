package com.mishal.urlshortener.infrastructure.config;

import com.mishal.urlshortener.infrastructure.id.SnowFlakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdConfig {

    @Bean
    public SnowFlakeIdGenerator idGenerator(){
        return new SnowFlakeIdGenerator(1);
    }

}
