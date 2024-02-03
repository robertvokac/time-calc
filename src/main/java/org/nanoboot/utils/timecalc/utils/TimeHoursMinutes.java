package org.nanoboot.utils.timecalc.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class TimeHoursMinutes {
    public static final int MINUTES_PER_HOUR = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;

    @Getter @Setter
    private Integer hour;
    @Getter @Setter
    private Integer minute;

    public TimeHoursMinutes(String string) {
        boolean isNegative = string.startsWith("-");
        if (isNegative) {
            string = string.replace("-", "");
        }
        String[] array = string.split(":");
        this.hour = (isNegative ? (-1) : 1) * Integer.valueOf(array[0]);
        this.minute = (isNegative ? (-1) : 1) * Integer.valueOf(array[1]);
    }

    public TimeHoursMinutes(int hourIn, int minuteIn) {
        this.hour = hourIn;
        this.minute = minuteIn;
        while (minute >= MINUTES_PER_HOUR) {
            minute = minute - MINUTES_PER_HOUR;
            hour = hour + 1;
        }
    }
}
