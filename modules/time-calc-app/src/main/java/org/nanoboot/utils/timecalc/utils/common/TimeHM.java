package org.nanoboot.utils.timecalc.utils.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TimeHM {

    public static final int MINUTES_PER_HOUR = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;

    @Getter
    @Setter
    private Integer hour;
    @Getter
    @Setter
    private Integer minute;

    public TimeHM(String string) {
        boolean isNegative = string.startsWith("-");
        if (isNegative) {
            string = string.replace("-", "");
        }
        String[] array = string.split(":");
        this.hour = (isNegative ? (-1) : 1) * Integer.valueOf(array[0]);
        this.minute = (isNegative ? (-1) : 1) * Integer.valueOf(array[1]);
    }

    public TimeHM(int hourIn, int minuteIn) {
        this.hour = hourIn;
        this.minute = minuteIn;
        while (minute >= MINUTES_PER_HOUR) {
            minute = minute - MINUTES_PER_HOUR;
            hour = hour + 1;
        }
        if (minute < 0) {
            minute = minute + 60;
            hour = hour - 1;
        }
    }

    public static int countDiffInMinutes(TimeHM startTime, TimeHM endTime) {
        return (endTime.getHour() * TimeHM.MINUTES_PER_HOUR + endTime
                .getMinute()) - (startTime.getHour() * TimeHM.MINUTES_PER_HOUR
                                 + startTime.getMinute());
    }

    public TimeHM cloneInstance() {
        return new TimeHM(hour, minute);
    }
}
