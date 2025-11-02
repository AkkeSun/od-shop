package com.common.infrastructure.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static String getCurrentDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }

    public static String getCurrentDate() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT, Locale.KOREA));
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT, Locale.KOREA));
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime,
            DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT, Locale.KOREA));
    }

    public static LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date,
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT, Locale.KOREA));
    }
}
