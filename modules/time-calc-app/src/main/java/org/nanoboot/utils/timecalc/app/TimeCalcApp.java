package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JOptionPane;
import java.io.IOException;

/**
 * @author Robert Vokac
 * @since 31.01.2024
 */
public class TimeCalcApp {

    public StringProperty visibilityProperty
            = new StringProperty("timeCalcApp.visibilityProperty",
                    Visibility.WEAKLY_COLORED.name());
    private long startNanoTime = 0l;

    public void start(String[] args) throws IOException {
        if (startNanoTime != 0l) {
            throw new TimeCalcException("TimeCalcApp was already started.");
        }
        startNanoTime = System.nanoTime();
        while (true) {
            boolean test = FileConstants.TEST_TXT.exists();
            String oldStartTime = Utils.readTextFromFile(
                    FileConstants.STARTTIME_TXT);
            String oldOvertime = Utils.readTextFromFile(
                    FileConstants.OVERTIME_TXT);
            String newStartTime
                    = test ? (oldStartTime != null ? oldStartTime
                                    : Constants.DEFAULT_START_TIME)
                            : (String) JOptionPane.showInputDialog(
                                    null,
                                    "Start Time:",
                                    "Start Time",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    null,
                                    oldStartTime == null
                                            ? Constants.DEFAULT_START_TIME
                                            : oldStartTime
                            );
            String newOvertime
                    = test ? (oldOvertime != null ? oldOvertime
                                    : Constants.DEFAULT_OVERTIME)
                            : (String) JOptionPane.showInputDialog(
                                    null,
                                    "Overtime:",
                                    "Overtime",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    null,
                                    oldOvertime == null
                                            ? Constants.DEFAULT_OVERTIME
                                            : oldOvertime
                            );

            Utils.writeTextToFile(FileConstants.STARTTIME_TXT, newStartTime);
            Utils.writeTextToFile(FileConstants.OVERTIME_TXT, newOvertime);
            try {
                MainWindow timeCalc
                        = new MainWindow(newStartTime, newOvertime, this);
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
        if (startNanoTime == 0l) {
            throw new TimeCalcException("TimeCalcApp was not yet started.");
        }
        return System.nanoTime() - startNanoTime;
    }

}
