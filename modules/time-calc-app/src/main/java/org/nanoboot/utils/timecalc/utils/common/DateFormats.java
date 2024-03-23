package org.nanoboot.utils.timecalc.utils.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class DateFormats {

    public final static DateFormat DATE_TIME_FORMATTER_HHmmssSSS
            = new SimpleDateFormat("HH:mm:ss:SSS", Locale.ENGLISH);
    public static DateFormat DATE_TIME_FORMATTER_LONG
            = new SimpleDateFormat("yyyy-MM-dd : EEEE", Locale.ENGLISH);

    public static DateFormat DATE_TIME_FORMATTER_YYYYMMDD
            = new SimpleDateFormat("yyyy-MM-dd");
    //
    public static DateFormat DATE_TIME_FORMATTER_TIME
            = new SimpleDateFormat("HH:mm:ss:SSS", Locale.ENGLISH);
    public static DateFormat DATE_TIME_FORMATTER_VERY_LONG
            = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:EEEE", Locale.ENGLISH);
    public static DateFormat DATE_TIME_FORMATTER_SHORT
            = new SimpleDateFormat("yyyyMMddHHmmss");

    private DateFormats() {
        //Not meant to be instantiated.
    }
}
