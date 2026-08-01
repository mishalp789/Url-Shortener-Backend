package com.mishalp789.url_shortener.url.util;

import com.mishalp789.url_shortener.common.constants.AppConstants;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {

    private static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int LENGTH = AppConstants.SHORT_CODE_LENGTH;

    private final SecureRandom random = new SecureRandom();

    public String generate() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < AppConstants.SHORT_CODE_LENGTH; i++) {
            builder.append(
                    CHARACTERS.charAt(
                            random.nextInt(CHARACTERS.length())
                    )
            );
        }

        return builder.toString();
    }
}
