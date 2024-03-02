package org.nanoboot.utils.timecalc.swing.progress;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class MonthBattery extends Battery {

    public MonthBattery(int x, int i, int i1) {
        super(MONTH, x, i, i1);
    }
    public static final String MONTH = "Month";

    public static double getMonthProgress(int weekDayWhenMondayIsOne,
            int workDaysDone, int workDaysTotal, double done) {
        if (done > 1) {
            done = 1;
        }
        return weekDayWhenMondayIsOne == 0
                || weekDayWhenMondayIsOne == 6
                        ? workDaysDone / workDaysTotal
                        : (workDaysDone + done) / workDaysTotal;
    }
}
