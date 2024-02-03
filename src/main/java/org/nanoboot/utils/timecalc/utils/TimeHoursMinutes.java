package org.nanoboot.utils.timecalc.utils;

import lombok.Getter;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class TimeHoursMinutes {

    @Getter
    private final Integer hour;
    @Getter
    private final Integer minute;

    public TimeHoursMinutes(String string) {
        boolean isNegative = string.startsWith("-");
        if (isNegative) {
            string = string.replace("-", "");
        }
        String[] array = string.split(":");
        this.hour = (isNegative ? (-1) : 1) * Integer.valueOf(array[0]);
        this.minute = (isNegative ? (-1) : 1) * Integer.valueOf(array[1]);
    }
}
