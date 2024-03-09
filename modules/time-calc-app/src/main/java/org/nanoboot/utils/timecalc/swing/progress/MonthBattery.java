package org.nanoboot.utils.timecalc.swing.progress;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class MonthBattery extends Battery {

    public static final String MONTH = "Month";

    public MonthBattery(int x, int i, int i1) {
        super(MONTH, x, i, i1);
    }

    public static double getMonthProgress(int weekDayWhenMondayIsOne,
            int workDaysDone, int workDaysTotal, double done) {
//        System.out.println("weekDayWhenMondayIsOne=" + weekDayWhenMondayIsOne);
//        System.out.println("workDaysDone=" + workDaysDone);
//        System.out.println("workDaysTotal=" + workDaysTotal);
//        System.out.println("done=" + done);
        if (done > 1) {
            done = 1;
        }
        double result = weekDayWhenMondayIsOne == 0
                        || weekDayWhenMondayIsOne == 6
                ? (double) workDaysDone / workDaysTotal
                : (workDaysDone + done) / workDaysTotal;
//        System.out.println("result=" + result);
        return result;
    }
}
