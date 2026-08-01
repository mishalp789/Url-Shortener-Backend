package com.mishalp789.url_shortener.url.util;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AliasValidator {
    private static final Set<String> RESERVED_ALIASES = Set.of(
            "api",
            "auth",
            "admin",
            "swagger-ui",
            "swagger",
            "v3",
            "actuator",
            "health",
            "login",
            "register",
            "dashboard"
    );

    public boolean isReserved(String alias){
        return RESERVED_ALIASES.contains(alias.toLowerCase());
    }
}
