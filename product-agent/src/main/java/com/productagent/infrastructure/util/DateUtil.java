package com.productagent.infrastructure.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public static String getCurrentDateTime() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss", Locale.KOREA));
    }

    public static String getCurrentDate() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA));
    }
}

