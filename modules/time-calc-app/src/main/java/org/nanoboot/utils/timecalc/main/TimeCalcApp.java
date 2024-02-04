package org.nanoboot.utils.timecalc.main;

import org.nanoboot.utils.timecalc.utils.Constants;
import org.nanoboot.utils.timecalc.utils.FileConstants;
import org.nanoboot.utils.timecalc.utils.Utils;

import javax.swing.JOptionPane;
import java.io.IOException;

/**
 * @author Robert
 * @since 31.01.2024
 */
public class TimeCalcApp {

    private long startNanoTime = 0l;

    public void start(String[] args) throws IOException {

        if(startNanoTime != 0l) {
            throw new TimeCalcException("TimeCalcApp was already started.");
        }
        startNanoTime = System.nanoTime();
        while (true) {
            boolean test = FileConstants.TEST_TXT.exists();
            String oldStartTime = Utils.readTextFromFile(
                    FileConstants.STARTTIME_TXT);
            String oldOvertime = Utils.readTextFromFile(
                    FileConstants.OVERTIME_TXT);
            String newStartTime =
                    test ? (oldStartTime != null ? oldStartTime :
                            Constants.DEFAULT_START_TIME) :
                            (String) JOptionPane.showInputDialog(
                                    null,
                                    "Start Time:",
                                    "Start Time",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    null,
                                    oldStartTime == null ?
                                            Constants.DEFAULT_START_TIME :
                                            oldStartTime
                            );
            String newOvertime =
                    test ? (oldOvertime != null ? oldOvertime :
                            Constants.DEFAULT_OVERTIME) :
                            (String) JOptionPane.showInputDialog(
                                    null,
                                    "Overtime:",
                                    "Overtime",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    null,
                                    oldOvertime == null ?
                                            Constants.DEFAULT_OVERTIME :
                                            oldOvertime
                            );

            Utils.writeTextToFile(FileConstants.STARTTIME_TXT, newStartTime);
            Utils.writeTextToFile(FileConstants.OVERTIME_TXT, newOvertime);
            try {
                TimeCalcManager timeCalc =
                        new TimeCalcManager(newStartTime, newOvertime);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                        e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public long getCountOfMinutesSinceAppStarted() {
        return getCountOfSecondsSinceAppStarted() / 60l;
    }
    public long getCountOfSecondsSinceAppStarted() {
        return getCountOfMillisecondsSinceAppStarted() / 1000000000l;
    }
    public long getCountOfMillisecondsSinceAppStarted() {
        if(startNanoTime == 0l) {
            throw new TimeCalcException("App was not yet started.");
        }
        return System.nanoTime() - startNanoTime;
    }

}

