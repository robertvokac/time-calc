package org.nanoboot.utils.timecalc.utils.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class DateFormats {
    private DateFormats() {
        //Not meant to be instantiated.
    }
    public final static DateTimeFormatter DATE_TIME_FORMATTER_HHmmssSSS =
            DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    public static DateFormat DATE_TIME_FORMATTER_LONG =
            new SimpleDateFormat("EEEE : yyyy-MM-dd", Locale.ENGLISH);
    //
    public static DateFormat DATE_TIME_FORMATTER_TIME =
            new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    public static DateFormat DATE_TIME_FORMATTER_VERY_LONG =
            new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:EEEE", Locale.ENGLISH);
}
