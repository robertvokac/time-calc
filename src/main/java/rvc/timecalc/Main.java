package rvc.timecalc;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;

/**
 * @author Robert
 * @since 31.01.2024
 */
public class Main {

    public static void main(String[] args) throws IOException {
        while (true) {
            boolean test = new File("test.txt").exists();
            File starttimeTxt = new File("starttime.txt");
            File overtimeTxt = new File("overtime.txt");
            String lastStartTime = Utils.readTextFromFile(starttimeTxt);
            String lastOvertime = Utils.readTextFromFile(overtimeTxt);
            String startTime =
                    test ? "7:00" : (String) JOptionPane.showInputDialog(
                            null,
                            "Start Time:",
                            "Start Time",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            lastStartTime == null ? "7:00" : lastStartTime
                    );
            String overTime =
                    test ? "0:00" : (String) JOptionPane.showInputDialog(
                            null,
                            "Overtime:",
                            "Overtime",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            lastOvertime == null ? "0:00" : lastOvertime
                    );

            Utils.writeTextToFile(starttimeTxt, startTime);
            Utils.writeTextToFile(overtimeTxt, overTime);
            try {
                TimeCalcWindow timeCalc =
                        new TimeCalcWindow(startTime, overTime);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                        e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}

