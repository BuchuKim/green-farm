package com.buchu.greenfarm.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeDuration {
    public static String generateTimeDuration(LocalDateTime dateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (ChronoUnit.HOURS.between(dateTime,currentTime)<1) {
            // n분 전
            return ChronoUnit.MINUTES.between(dateTime,currentTime) + "분 전";
        } else if (ChronoUnit.DAYS.between(dateTime,currentTime)<1) {
            // n시간 전
            return ChronoUnit.HOURS.between(dateTime,currentTime) + "시간 전";
        } else if (ChronoUnit.MONTHS.between(dateTime,currentTime)<1) {
            // n일 전
            return ChronoUnit.DAYS.between(dateTime,currentTime) + "일 전";
        } else {
            // yy.MM
            return dateTime.format(DateTimeFormatter.ofPattern("yy.MM HH"));
        }
    }
}
