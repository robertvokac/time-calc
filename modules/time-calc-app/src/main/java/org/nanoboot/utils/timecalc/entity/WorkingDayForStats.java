package org.nanoboot.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkingDayForStats extends WorkingDay {

    private int departureHour;
    private int departureMinute;
    private int dayOfWeek;
    private int remainingOvertimeHours;
    private int remainingOvertimeMinutes;
    private double arrivalTimeMovingAverage7Days;
    private double arrivalTimeMovingAverage14Days;
    private double arrivalTimeMovingAverage28Days;
    private double arrivalTimeMovingAverage56Days;

    public WorkingDayForStats() {
    }

    public List<WorkingDayForStats> createList(List<WorkingDay> list) {
        return null;//todo
    }
}
