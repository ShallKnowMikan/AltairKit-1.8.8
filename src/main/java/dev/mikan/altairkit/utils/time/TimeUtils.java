package dev.mikan.altairkit.utils.time;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtils {

    private final ZoneId ZONE = ZoneId.of("Europe/Rome");
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public DateTime current() {
        return new DateTime(FORMATTER.format(ZonedDateTime.now(ZONE)));
    }

    public DateTime add(DateTime baseDatetime, int years, int months, int days, int hours, int minutes, int seconds) {
        ZonedDateTime base = LocalDateTime.parse(baseDatetime.toString(), FORMATTER).atZone(ZONE);
        ZonedDateTime result = base
                .plusYears(years)
                .plusMonths(months)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);
        return new DateTime(FORMATTER.format(result));
    }

    public Time timeLeft(DateTime fromDatetime, DateTime toDatetime) {
        ZonedDateTime from = LocalDateTime.parse(fromDatetime.toString(), FORMATTER).atZone(ZONE);
        ZonedDateTime to = LocalDateTime.parse(toDatetime.toString(), FORMATTER).atZone(ZONE);

        if (to.isBefore(from)) return new Time(0,0,0);

        Duration duration = Duration.between(from, to);

        long totalSeconds = duration.getSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return new Time(hours,minutes,seconds);
    }


}
