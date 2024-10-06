package org.v1.job_coach.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String getDateNow(LocalDateTime now) {
        return now.format(FORMATTER);
    }
}
