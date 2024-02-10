package org.nanoboot.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
@Getter
@Setter
@ToString
public class WorkingDay {
    private int year;
    private int month;
    private int day;
    private int arrivalHour;
    private int arrivalMinute;
    private int departureHour;
    private int departureMinute;
    private String note;
    private int overtimeHoursThisDay;
    private int overtimeMinutesThisDay;
    private int compensatoryTimeOffHoursThisDay;
    private int compensatoryTimeOffMinutesThisDay;
    private int overtimeHoursToBeCompensatedUntilThisDay;
    private int overtimeMinutesToBeCompensatedUntilThisDay;


}
