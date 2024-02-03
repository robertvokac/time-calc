package org.nanoboot.utils.timecalc.utils;

import java.time.format.DateTimeFormatter;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class DateFormats {
    public final static DateTimeFormatter DATE_TIME_FORMATTER_HHmmssSSS =
            DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    private DateFormats() {
        //Not meant to be instantiated.
    }
}
