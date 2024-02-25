package org.nanoboot.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkDayForStats extends WorkDay {

    private int departureHour;
    private int departureMinute;
    private int dayOfWeek;
    private int remainingOvertimeHours;
    private int remainingOvertimeMinutes;
    private double arrivalTimeMovingAverage7Days;
    private double arrivalTimeMovingAverage14Days;
    private double arrivalTimeMovingAverage28Days;
    private double arrivalTimeMovingAverage56Days;

    public WorkDayForStats() {
    }
}
