package dev.mikan.altairkit.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtils {

    public String next(
                       int years,
                       int months,
                       int days,
                       int hours,
                       int minutes,
                       int seconds){

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ZonedDateTime future = dateTime
                .plusYears(years)
                .plusMonths(months)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);

        return formatter.format(future);
    }

    public boolean isExpired(String date){
        ZonedDateTime current = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ZonedDateTime parsed = LocalDateTime.parse(date, formatter)
                .atZone(ZoneId.of("Europe/Rome"));

        return current.isAfter(parsed);
    }


    public String current(){
        ZonedDateTime current = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return formatter.format(current);
    }
}
