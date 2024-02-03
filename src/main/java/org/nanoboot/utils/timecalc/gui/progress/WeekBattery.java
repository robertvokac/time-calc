package org.nanoboot.utils.timecalc.gui.progress;

import org.nanoboot.utils.timecalc.utils.TimeHM;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class WeekBattery extends Battery{
    public WeekBattery(int x, int i, int i1) {
        super("Week", x, i, i1);
    }
    public static double getWeekProgress(int weekDayWhenMondayIsOne, double done) {
return weekDayWhenMondayIsOne == 0
       || weekDayWhenMondayIsOne == 6 ?
        100 : ((weekDayWhenMondayIsOne - 1) * 0.20 + done * 0.20);
    }
}