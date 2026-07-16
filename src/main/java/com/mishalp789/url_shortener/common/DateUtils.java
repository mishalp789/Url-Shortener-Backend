package com.mishalp789.url_shortener.common;

import java.time.LocalDateTime;

public final class DateUtils {
    private DateUtils(){}
    public static LocalDateTime now(){
        return LocalDateTime.now();
    }
}
