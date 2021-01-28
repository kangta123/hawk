package com.oc.hawk.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static final String HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final long HOUR_8_MS = 8 * 60 * 60 ;

    public static long toLong(LocalDateTime target) {
        return target.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static long timestampUtcToCst(long utc) {
        return utc + HOUR_8_MS;
    }

}
