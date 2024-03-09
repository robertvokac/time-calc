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

}
