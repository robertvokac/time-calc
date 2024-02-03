package org.nanoboot.utils.timecalc.gui.progress;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class MonthBattery extends Battery{
    public MonthBattery(int x, int i, int i1) {
        super("Month", x, i, i1);
    }
    public static double getMonthProgress(int weekDayWhenMondayIsOne, double done) {
return weekDayWhenMondayIsOne == 0
       || weekDayWhenMondayIsOne == 6 ?
        100 : ((weekDayWhenMondayIsOne - 1) * 0.20 + done * 0.20);
    }
}
