package dev.mikan.altairkit.utils;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtils {

    public String next(String datetime){

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ZonedDateTime parsed = LocalDateTime.parse(datetime, formatter)
                .atZone(ZoneId.of("Europe/Rome"));
        ZonedDateTime future = dateTime
                .plusYears(parsed.getYear())
                .plusMonths(parsed.getMonth().getValue())
                .plusDays(parsed.getDayOfMonth())
                .plusHours(parsed.getHour())
                .plusMinutes(parsed.getMinute())
                .plusSeconds(parsed.getSecond());

        return formatter.format(future);
    }

    public String next(
                       int years,
                       int months,
                       int days,
                       int hours,
                       int minutes,
                       int seconds){

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        ZonedDateTime parsed = LocalDateTime.parse(date, formatter)
                .atZone(ZoneId.of("Europe/Rome"));

        return current.isAfter(parsed);
    }

    public String remaining(String startDatetime, String endDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        ZonedDateTime start = LocalDateTime.parse(startDatetime, formatter)
                .atZone(ZoneId.of("Europe/Rome"));
        ZonedDateTime end = LocalDateTime.parse(endDatetime, formatter)
                .atZone(ZoneId.of("Europe/Rome"));

        if (end.isBefore(start)) {
            return "0000-00-00 00:00:00";
        }

        return calculateFullDifference(start, end);
    }

    public String remaining(String endDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        ZonedDateTime end = LocalDateTime.parse(endDatetime, formatter)
                .atZone(ZoneId.of("Europe/Rome"));

        if (now.isAfter(end)) {
            return "0000-00-00 00:00:00";
        }

        return calculateFullDifference(now, end);
    }


    public String add(String difference) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Parsa la differenza (formato: "0000-00-00 00:00:09")
        String[] parts = difference.split(" ");
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        int years = Integer.parseInt(dateParts[0]);
        int months = Integer.parseInt(dateParts[1]);
        int days = Integer.parseInt(dateParts[2]);
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        // Aggiungi al tempo corrente
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        ZonedDateTime result = now
                .plusYears(years)
                .plusMonths(months)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);

        return formatter.format(result);
    }

    private String calculateFullDifference(ZonedDateTime start, ZonedDateTime end) {
        // Calcola differenza in anni/mesi/giorni
        Period period = Period.between(start.toLocalDate(), end.toLocalDate());

        // Calcola differenza in ore/minuti/secondi
        Duration duration = Duration.between(
                start.toLocalTime(),
                end.toLocalTime()
        );

        // Se la parte time è negativa, aggiusta Period e Duration
        if (duration.isNegative()) {
            duration = duration.plusDays(1);
            period = period.minusDays(1);
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d/%02d/%04d %02d:%02d:%02d",
                period.getDays(),
                period.getMonths(),
                period.getYears(),
                hours,
                minutes,
                seconds);
    }

    public String timeLeft(String datetime){
        String date = TimeUtils.remaining(datetime);
        String[] tokens  = date.split(" ");
        String day = tokens[0].split("/")[0];
        String[] time = tokens[1].split(":");
        String hour = time[0];
        String minute = time[1];
        String second = time[2];
        return day+"g " + hour + "h " + minute + "m " + second + "s";
    }

    public String formatDatetime(String datetime, boolean timeLeft){
        return timeLeft ? TimeUtils.remaining(datetime) : datetime;
    }


//    public String formatFullDatetime(String datetime, boolean timeLeft){
//        String fullDatetime = timeLeft ? TimeUtils.remaining(datetime) : datetime;
//        String[] tokens  = fullDatetime.split(" ");
//        String[] date = tokens[0].split("/");
//        String year = date[2];
//        String month = date[1];
//        String day = date[0];
//        String[] time = tokens[1].split(":");
//        String hour = time[0];
//        String minute = time[1];
//        String second = time[2];
//        return day+"/" +month+"/" +year+" " + hour + ":" + minute + ":" + second;
//    }

    public String current(){
        ZonedDateTime current = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return formatter.format(current);
    }
}
