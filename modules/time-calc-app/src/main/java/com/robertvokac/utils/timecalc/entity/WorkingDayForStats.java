package com.robertvokac.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;
import com.robertvokac.utils.timecalc.app.TimeCalcException;
import com.robertvokac.utils.timecalc.swing.common.ArrivalChartData;
import com.robertvokac.utils.timecalc.utils.common.TTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
public class WorkingDayForStats extends WorkingDay {

    private int departureHour;
    private int departureMinute;
    private int pauseEndHour;
    private int pauseEndMinute;
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
        if (list.isEmpty()) {
            //nothing to do
            return;
        }
        Map<String, WorkingDayForStats> map = new HashMap<>();
        Set<Integer> years = new HashSet<>();
        list.forEach(w -> {
                    if (!years.isEmpty() && !years.contains(w.getYear())) {
                        throw new TimeCalcException(
                                "Cannot create statistics, if there are work days for more than one year.");
                    }
                    years.add(w.getYear());
                    map.put(w.getId(), w);
                }
        );
        int year = years.stream().findFirst().orElseThrow(
                () -> new TimeCalcException("Set years is empty."));

        Calendar cal7DaysAgo = Calendar.getInstance();
        Calendar cal14DaysAgo = Calendar.getInstance();
        Calendar cal28DaysAgo = Calendar.getInstance();
        Calendar cal56DaysAgo = Calendar.getInstance();

        List<WorkingDayForStats> list7 = new ArrayList<>();
        List<WorkingDayForStats> list14 = new ArrayList<>();
        List<WorkingDayForStats> list28 = new ArrayList<>();
        List<WorkingDayForStats> list56 = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            int dayMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int day = 1; day <= dayMaximum; day++) {
                String id = WorkingDay.createId(year, month, day);
                if (!map.containsKey(id)) {
                    //nothing to do
                    continue;
                }
                cal.set(Calendar.DAY_OF_MONTH, day);
                cal7DaysAgo.setTime(cal.getTime());
                cal14DaysAgo.setTime(cal.getTime());
                cal28DaysAgo.setTime(cal.getTime());
                cal56DaysAgo.setTime(cal.getTime());
                cal7DaysAgo.add(Calendar.DAY_OF_MONTH, -7);
                cal14DaysAgo.add(Calendar.DAY_OF_MONTH, -14);
                cal28DaysAgo.add(Calendar.DAY_OF_MONTH, -28);
                cal56DaysAgo.add(Calendar.DAY_OF_MONTH, -56);
                String id7 = WorkingDay.createId(cal7DaysAgo);
                String id14 = WorkingDay.createId(cal14DaysAgo);
                String id28 = WorkingDay.createId(cal28DaysAgo);
                String id56 = WorkingDay.createId(cal56DaysAgo);

//                System.out.println("id7=" + id7);
//                System.out.println("id14=" + id14);
//                System.out.println("id28=" + id28);
//                System.out.println("id56=" + id56);
                if (map.containsKey(id7)) {
                    list7.remove(map.get(id7));
                }
                if (map.containsKey(id14)) {
                    list14.remove(map.get(id14));
                }
                if (map.containsKey(id28)) {
                    list28.remove(map.get(id28));
                }
                if (map.containsKey(id56)) {
                    list56.remove(map.get(id56));
                }
                WorkingDayForStats wd = map.get(id);
                if (!wd.isThisDayTimeOff()) {
                    Stream.of(list7, list14, list28, list56).forEach(l -> {
                        l.add(wd);
                    });
                }
                wd.setArrivalTimeMovingAverage7Days(list7.stream()
                        .map(WorkingDay::getArrivalAsDouble)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0));
                wd.setArrivalTimeMovingAverage14Days(list14.stream()
                        .map(WorkingDay::getArrivalAsDouble)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0));
                wd.setArrivalTimeMovingAverage28Days(list28.stream()
                        .map(WorkingDay::getArrivalAsDouble)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0));
                wd.setArrivalTimeMovingAverage56Days(list56.stream()
                        .map(WorkingDay::getArrivalAsDouble)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0));
//                System.out.println(
//                        WorkingDay.createId(cal) + " 1 :: " + list7.size());
//                System.out.println(
//                        WorkingDay.createId(cal) + " 2 :: " + list14.size());
//                System.out.println(
//                        WorkingDay.createId(cal) + " 3 :: " + list28.size());
//                System.out.println(
//                        WorkingDay.createId(cal) + " 4 :: " + list56.size());
            }

        }

    }

    public static List<WorkingDayForStats> createList(List<WorkingDay> list) {
        List<WorkingDayForStats> result = new ArrayList<>();
        for (WorkingDay wd : list) {
            WorkingDayForStats wdfs = new WorkingDayForStats(wd);
            result.add(wdfs);
        }
        return result;
    }

    public static ArrivalChartData toArrivalChartData(
            List<WorkingDayForStats> list, double target, String startDate, String endDate) {
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        while(list.get(list.size() - 1).isThisDayTimeOff()) {
            list.remove(list.size() - 1);
        }
        String[] days = new String[list.size()];
        double[] ma7 = new double[list.size()];
        double[] ma14 = new double[list.size()];
        double[] ma28 = new double[list.size()];
        double[] ma56 = new double[list.size()];
        double[] arrival = new double[list.size()];
        ArrivalChartData acd = new ArrivalChartData();
        acd.setDays(days);
        acd.setArrival(arrival);
        acd.setTarget(target);
        acd.setMa7(ma7);
        acd.setMa14(ma14);
        acd.setMa28(ma28);
        acd.setMa56(ma56);
        if(startDate != null && !startDate.isEmpty()) {
            acd.setStartDate(startDate);
        }
        if(endDate != null && !endDate.isEmpty()) {
            acd.setEndDate(endDate);
        }
        for(int i = 0; i < list.size(); i++) {
            WorkingDayForStats wdfs = list.get(i);
            days[i] = wdfs.getId();

            arrival[i] = (wdfs.isThisDayTimeOff() ? wdfs.getArrivalTimeMovingAverage7Days() : wdfs.getArrivalAsDouble()) - target;
            ma7[i] = wdfs.getArrivalTimeMovingAverage7Days() - target;
            ma14[i] = wdfs.getArrivalTimeMovingAverage14Days() - target;
            ma28[i] = wdfs.getArrivalTimeMovingAverage28Days() - target;
            ma56[i] = wdfs.getArrivalTimeMovingAverage56Days() - target;
        }
        return acd;
    }

    public WorkingDayForStats(WorkingDay wd) {
        this(wd.getId(),
                wd.getYear(),
                wd.getMonth(),
                wd.getDay(),
                wd.getArrivalHour(),
                wd.getArrivalMinute(),
                wd.getOvertimeHour(),
                wd.getOvertimeMinute(),
                wd.getPauseStartHour(),
                wd.getPauseStartMinute(),
                wd.getWorkingTimeInMinutes(),
                wd.getPauseTimeInMinutes(),
                wd.getNote(),
                wd.isTimeOff(),
                wd.getForgetOvertime());
    }

    public WorkingDayForStats(String id, int year, int month, int day,
            int arrivalHour, int arrivalMinute, int overtimeHour,
            int overtimeMinute, int pauseStartHour, int pauseStartMinute,
            int workingTimeInMinutes,
            int pauseTimeInMinutes, String note, boolean timeOff, int forgetOvertime) {
        super(id, year, month, day, arrivalHour, arrivalMinute, overtimeHour,
                overtimeMinute, pauseStartHour, pauseStartMinute, workingTimeInMinutes, pauseTimeInMinutes, note,
                timeOff, forgetOvertime);
        this.arrival = this.isThisDayTimeOff() ? null :
                new TTime(arrivalHour, arrivalMinute);
        this.overtime = this.isThisDayTimeOff() ? null :
                new TTime(overtimeHour, overtimeMinute);
        this.work = this.isThisDayTimeOff() ? null :
                TTime.ofMinutes(workingTimeInMinutes);
        this.pause = this.isThisDayTimeOff() ? null :
                TTime.ofMinutes(pauseTimeInMinutes);
        this.departure = this.isThisDayTimeOff() ? null :
                this.arrival.add(work).add(pause).add(overtime);
        this.departureHour = this.isThisDayTimeOff() ? -1 : departure.getHour();
        this.departureMinute =
                this.isThisDayTimeOff() ? -1 : departure.getMinute();
        TTime pauseStart = new TTime(this.getPauseStartHour(), this.getPauseStartMinute());
        TTime pauseEnd = null;
        if(pause == null) {
            pauseEnd = pauseStart.cloneInstance();
        } else {
            pauseEnd = pauseStart.add(pause);
        }
        this.pauseEndHour = pauseEnd.getHour();
        this.pauseEndMinute = pauseEnd.getMinute();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        this.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        this.dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
    }

    public String getDayOfWeekAsString() {
        return LocalDate.of(getYear(), getMonth(), getDay()).getDayOfWeek()
                .toString();
    }

}
