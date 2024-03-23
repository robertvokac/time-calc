package org.nanoboot.utils.timecalc.swing.common;

import lombok.Getter;
import org.nanoboot.utils.timecalc.swing.progress.AnalogClock;
import org.nanoboot.utils.timecalc.swing.progress.Time;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * @author Robert Vokac
 * @since 06.03.2024
 */
@Getter
public class WeekStatistics {

    private final boolean nowIsWeekend;
    private final int workDaysDone;
    private final int workDaysTotal;

    public WeekStatistics(AnalogClock analogClock, Time time) {
        int workDaysDoneTmp = 0;
        int workDaysTodoTmp = 0;
        int workDaysTotalTmp;
        {
            int currentDayOfMonth = time.dayProperty.getValue();

            for (int dayOfMonth = 1;
                    dayOfMonth <= time.asCalendar()
                            .getActualMaximum(Calendar.DAY_OF_MONTH);
                    dayOfMonth++) {
                DayOfWeek dayOfWeek = LocalDate.of(time.yearProperty.getValue(),
                        time.monthProperty.getValue(),
                                dayOfMonth)
                                .getDayOfWeek();
                boolean weekend
                        = dayOfWeek.toString().equals("SATURDAY") || dayOfWeek
                        .toString().equals("SUNDAY");
                if (dayOfMonth < currentDayOfMonth && !weekend) {
                    ++workDaysDoneTmp;
                }
                if (dayOfMonth > currentDayOfMonth && !weekend) {
                    ++workDaysTodoTmp;
                }
            }

        }
        String currentDayOfWeekAsString = LocalDate
                .of(analogClock.yearProperty.getValue(),
                        analogClock.monthProperty.getValue(),
                        analogClock.dayProperty.getValue()).getDayOfWeek()
                .toString();
        this.nowIsWeekend = currentDayOfWeekAsString.equals("SATURDAY")
                || currentDayOfWeekAsString.equals("SUNDAY");
        workDaysTotalTmp
                = workDaysDoneTmp + (nowIsWeekend ? 0 : 1) + workDaysTodoTmp;
        this.workDaysDone = workDaysDoneTmp;
        this.workDaysTotal = workDaysTotalTmp;
//        System.out.println("currentDayOfWeekAsString=" + currentDayOfWeekAsString);
//        System.out.println("workDaysDone=" + workDaysDone);
//        System.out.println("workDaysTotal=" + workDaysTotal);
    }
}
