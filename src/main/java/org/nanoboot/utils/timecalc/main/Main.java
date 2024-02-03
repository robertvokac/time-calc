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
public class Main {

    public static void main(String[] args) throws IOException {

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

}

