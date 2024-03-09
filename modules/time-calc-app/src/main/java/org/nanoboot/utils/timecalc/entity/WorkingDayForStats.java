package org.nanoboot.utils.timecalc.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import org.nanoboot.utils.timecalc.utils.common.TTime;

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
    private final TTime arrival;
    private final TTime overtime;
    private final TTime work;
    private final TTime pause;
    private final TTime departure;

    public static void fillStatisticsColumns(List<WorkingDayForStats> list) {
        //todo
    }
    public static List<WorkingDayForStats> createList(List<WorkingDay> list) {
        List<WorkingDayForStats> result = new ArrayList<>();
        for (WorkingDay wd : list) {
            WorkingDayForStats wdfs = new WorkingDayForStats(wd);
            result.add(wdfs);
        }
        return result;
    }

    public WorkingDayForStats(WorkingDay workingDay) {
        this(workingDay.getId(),
                workingDay.getYear(),
                workingDay.getMonth(),
                workingDay.getDay(),
                workingDay.getArrivalHour(),
                workingDay.getArrivalMinute(),
                workingDay.getOvertimeHour(),
                workingDay.getOvertimeMinute(),
                workingDay.getWorkingTimeInMinutes(),
                workingDay.getPauseTimeInMinutes(),
                workingDay.getNote(),
                workingDay.isTimeOff());
    }

    public WorkingDayForStats(String id, int year, int month, int day, int arrivalHour, int arrivalMinute, int overtimeHour, int overtimeMinute, int workingTimeInMinutes, int pauseTimeInMinutes, String note, boolean timeOff) {
        super(id, year, month, day, arrivalHour, arrivalMinute, overtimeHour, overtimeMinute, workingTimeInMinutes, pauseTimeInMinutes, note, timeOff);
        this.arrival = this.isThisDayTimeOff() ? null : new TTime(arrivalHour, arrivalMinute);
        this.overtime = this.isThisDayTimeOff() ? null : new TTime(overtimeHour, overtimeMinute);
        this.work = this.isThisDayTimeOff() ? null : TTime.ofMinutes(workingTimeInMinutes);
        this.pause = this.isThisDayTimeOff() ? null : TTime.ofMinutes(pauseTimeInMinutes);
        this.departure = this.isThisDayTimeOff() ? null : this.arrival.add(work).add(pause).add(overtime);
        this.departureHour = this.isThisDayTimeOff() ? -1 : departure.getHour();
        this.departureMinute = this.isThisDayTimeOff() ? -1 : departure.getMinute();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        this.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        this.dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
    }

    public String getDayOfWeekAsString() {
        return LocalDate.of(getYear(), getMonth() ,getDay()).getDayOfWeek().toString();
    }
}
