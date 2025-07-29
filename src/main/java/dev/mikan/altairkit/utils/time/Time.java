package dev.mikan.altairkit.utils.time;

import lombok.Getter;

@Getter
public class Time {

    private final long hours;
    private final long minutes;
    private final long seconds;

    private final boolean valid;
    private final boolean zero;

    public Time(long hours, long minutes, long seconds) {
        this.valid = hours >= 0 && minutes >= 0 && seconds >= 0;
        this.zero = hours != 0 && minutes != 0 && seconds != 0;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }

}
