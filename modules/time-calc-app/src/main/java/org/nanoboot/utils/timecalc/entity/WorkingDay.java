package org.nanoboot.utils.timecalc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Calendar;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkingDay {

    private static final String NODATA = "nodata";

    public static String createId(Calendar cal) {
        return createId(cal.get(Calendar.YEAR), cal.get((Calendar.MONTH)) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }
    public static String createId(int year, int month, int day) {
        return (year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day);
    }
    
    private String id;
    private int year;
    private int month;
    private int day;
    private int arrivalHour;
    private int arrivalMinute;
    private int overtimeHour;
    private int overtimeMinute;

    private int workingTimeInMinutes;
    private int pauseTimeInMinutes;
    private String note;
    private boolean timeOff;
    private int forgetOvertime;

    public boolean isThisDayTimeOff() {
        return timeOff || this.note.equals(NODATA);
    }
    public double getArrivalAsDouble() {
        return ((double)arrivalHour) + ((double)arrivalMinute / 60d);
    }

}
