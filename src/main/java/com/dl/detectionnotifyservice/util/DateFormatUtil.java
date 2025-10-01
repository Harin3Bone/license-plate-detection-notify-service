package com.dl.detectionnotifyservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateFormatUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static String zonedDateTimeToString(ZonedDateTime in, ZoneId zoneId) {
        return in.withZoneSameInstant(zoneId).format(dateTimeFormatter);
    }

}
