package dev.mikan.altairkit.utils.time;

import lombok.Getter;

@Getter
public final class DateTime {
    private final int year;
    private final int month;
    private final int day;
    private Time time;

    private final boolean valid;

    public DateTime(final String datetime) {
        // datetime must be formatted as: "dd/MM/yyyy HH:mm:ss"
        if (datetime == null || datetime.isEmpty() || !datetime.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
            this.valid = false;
            this.day = -1;
            this.month = -1;
            this.year = -1;
            this.time = new Time(-1,-1,-1);
            return;
        }
        this.valid = true;
        String[] parts = datetime.split(" ");
        String[] dateParts = parts[0].split("/");
        String[] timeParts = parts[1].split(":");

        this.day = Integer.parseInt(dateParts[0]);
        this.month = Integer.parseInt(dateParts[1]);
        this.year = Integer.parseInt(dateParts[2]);

        final long hour = Long.parseLong(timeParts[0]);
        final long minute = Long.parseLong(timeParts[1]);
        final long second = Long.parseLong(timeParts[2]);

        this.time = new Time(hour,minute,second);
    }

    public DateTime setTime(final Time time){
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return valid ?
                String.format("%02d/%02d/%04d %02d:%02d:%02d", day, month, year, this.time.getHours(), this.time.getMinutes(), this.time.getSeconds())
                : "" ;
    }

}
