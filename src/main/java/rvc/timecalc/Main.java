package rvc.timecalc;

import javax.swing.JOptionPane;

/**
 * @author Robert
 * @since 31.01.2024
 */
public class Main {



    public static void main(String[] args) {
        while(true) {
            String startTime = (String) JOptionPane.showInputDialog(
                    null,
                    "Start Time:",
                    "Start Time",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "7:00"
            );
            String overTime = (String) JOptionPane.showInputDialog(
                    null,
                    "Overtime:",
                    "Overtime",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "0:00"
            );
            try {
                TimeCalc timeCalc =
                        new TimeCalc(startTime, overTime);
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}

