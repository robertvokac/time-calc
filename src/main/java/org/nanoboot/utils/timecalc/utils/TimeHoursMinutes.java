package org.nanoboot.utils.timecalc.utils;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class TimeHoursMinutes {

    private final Integer hour;
    private final Integer minute;

    public TimeHoursMinutes(String string) {
        String[] array = string.split(":");
        this.hour = Integer.valueOf(array[0]);
        this.minute = Integer.valueOf(array[1]);
    }
}
