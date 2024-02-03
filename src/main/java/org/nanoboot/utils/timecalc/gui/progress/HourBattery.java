package org.nanoboot.utils.timecalc.gui.progress;

import org.nanoboot.utils.timecalc.utils.TimeHM;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class HourBattery extends Battery{
    public HourBattery(int x, int i, int i1) {
        super("Hour", x, i, i1);
    }
    public static double getHourProgress(TimeHM timeRemains, int secondsRemains,
            int millisecondsRemains) {
        double minutesRemainsD = timeRemains.getMinute();
        double secondsRemainsD = secondsRemains;
        double millisecondsRemainsD = millisecondsRemains;
        minutesRemainsD = minutesRemainsD + secondsRemainsD / 60d;
        minutesRemainsD =
                minutesRemainsD + millisecondsRemainsD / 1000d / 60d;
        if (secondsRemainsD > 0) {
            minutesRemainsD = minutesRemainsD - 1d;
        }
        if (millisecondsRemainsD > 0) {
            minutesRemainsD = minutesRemainsD - 1d / 1000d;
        }
        double hourProgress = 1 - ((minutesRemainsD % 60d) / 60d);
        return hourProgress;
    }
}
