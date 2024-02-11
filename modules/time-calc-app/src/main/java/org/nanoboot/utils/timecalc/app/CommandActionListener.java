package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Toaster;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Robert
 * @since 26.02.2024
 */
public class CommandActionListener
        implements ActionListener {
    private final TimeCalcApp timeCalcApp;
    private final TimeCalcConfiguration timeCalcConfiguration;

    public CommandActionListener(TimeCalcApp timeCalcApp, TimeCalcConfiguration timeCalcConfiguration) {
        this.timeCalcApp = timeCalcApp;
        this.timeCalcConfiguration = timeCalcConfiguration;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            String commands = (String) JOptionPane.showInputDialog(
                    null,
                    "Run a command:",
                    "Command launching",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "test"
            );
            String[] commandsAsArray = commands.split(" ");
            switch (commandsAsArray[0]) {
                case "test":
                    JOptionPane.showMessageDialog(null, "Test");
                    break;
                case "color":
                    timeCalcApp.visibilityProperty.setValue(
                            commandsAsArray[1].equals("1") ?
                                    Visibility.STRONGLY_COLORED.name() :
                                    Visibility.WEAKLY_COLORED.name());
                    break;
                case "gray":
                    timeCalcApp.visibilityProperty.setValue(
                            commandsAsArray[1].equals("1") ?
                                    Visibility.GRAY.name() :
                                    Visibility.WEAKLY_COLORED.name());
                    break;
                case "waves":
                    timeCalcConfiguration.batteryWavesEnabledProperty
                            .setValue(commandsAsArray[1].equals("1"));
                    break;
                case "uptime":
                    JOptionPane.showMessageDialog(null,
                            timeCalcApp
                                    .getCountOfMinutesSinceAppStarted()
                            + " minutes");
                    break;
                case "toast":
                    Toaster t = new Toaster();
                    t.setToasterWidth(800);
                    t.setToasterHeight(800);
                    t.setDisplayTime(60000 * 5);
                    t.setToasterColor(Color.GRAY);
                    Font font = new Font("sans", Font.PLAIN, 12);
                    t.setToasterMessageFont(font);
                    t.setDisplayTime(5000);
                    t.showToaster(commands.substring(6));
                    break;
                case "toasts":
                    Utils.toastsAreEnabled
                            .setValue(commandsAsArray[1].equals("1"));
                    break;
                default:
                    JOptionPane.showMessageDialog(null,
                            "Unknown command: " + commandsAsArray[0]);
        }
    }
}
