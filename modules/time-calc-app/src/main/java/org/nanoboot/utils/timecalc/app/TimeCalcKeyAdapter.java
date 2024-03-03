package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.MainWindow;
import org.nanoboot.utils.timecalc.swing.common.Toaster;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Robert Vokac
 * @since 26.02.2024
 */
public class TimeCalcKeyAdapter extends KeyAdapter {

    private final TimeCalcConfiguration timeCalcConfiguration;
    private final TimeCalcApp timeCalcApp;
    private final MainWindow window;

    public TimeCalcKeyAdapter(
            TimeCalcConfiguration timeCalcConfiguration,
            TimeCalcApp timeCalcApp,
            MainWindow window
    ) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        this.timeCalcApp = timeCalcApp;
        this.window = window;
    }

    public void keyPressed(KeyEvent e) {
        boolean onlyGreyOrNone
                = timeCalcConfiguration.visibilitySupportedColoredProperty
                        .isDisabled();
        Visibility visibility = Visibility
                .valueOf(timeCalcApp.visibilityProperty.getValue());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            timeCalcApp.visibilityProperty
                    .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                            : Visibility.STRONGLY_COLORED.name());
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            timeCalcApp.visibilityProperty
                    .setValue(Visibility.NONE.name());
        }

        if (e.getKeyCode() == KeyEvent.VK_H) {
            if (visibility.isNone()) {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                : Visibility.STRONGLY_COLORED.name());
            } else {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.NONE.name());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            if (visibility.isGray() && !onlyGreyOrNone) {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.WEAKLY_COLORED.name());
            } else {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.GRAY.name());
                MainWindow.hideShowCheckBox.setSelected(false);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {
            if (!onlyGreyOrNone) {
                if (visibility.isStronglyColored()) {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.WEAKLY_COLORED.name());
                } else {
                    timeCalcApp.visibilityProperty
                            .setValue(
                                    Visibility.STRONGLY_COLORED.name());
                }
            } else {
                timeCalcApp.visibilityProperty.setValue(Visibility.GRAY
                        .name());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_V) {
            if (visibility.isNone()) {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                : Visibility.STRONGLY_COLORED.name());
            } else {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.NONE.name());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (visibility.isStronglyColored()) {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                : Visibility.WEAKLY_COLORED.name());
            }
            if (visibility.isWeaklyColored()) {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.GRAY.name());
            }
            if (visibility.isGray()) {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.NONE.name());
            }
            if (visibility.isNone()) {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                : Visibility.STRONGLY_COLORED.name());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            window.doCommand();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            window.doRestart();
        }
        if (e.getKeyCode() == KeyEvent.VK_N) {
            timeCalcConfiguration.notificationsVisibleProperty.flip();
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            window.openWorkDaysWindow();
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            window.openActivitiesWindow();
        }
        if (e.getKeyCode() == KeyEvent.VK_X) {
            window.doExit();
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            window.openConfigWindow();
        }

        if (e.getKeyCode() == KeyEvent.VK_J) {
            if (timeCalcConfiguration.jokesVisibleProperty.isEnabled()) {
                Jokes.showRandom();
            }

        }

        if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_F1) {
            window.openHelpWindow();
        }
        if (e.getKeyCode() == KeyEvent.VK_U) {
            window.doEnableEverything();
            
        }
        if (e.getKeyCode() == KeyEvent.VK_I) {
            window.doDisableAlmostEverything();
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            timeCalcConfiguration.batteryWavesVisibleProperty.flip();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_B) {
            MainWindow.hideShowCheckBox.setSelected(!MainWindow.hideShowCheckBox.isSelected());
        }

        boolean numberKeyWasPressed = e.getKeyCode() == KeyEvent.VK_0 ||
                    e.getKeyCode() == KeyEvent.VK_1 ||
                    e.getKeyCode() == KeyEvent.VK_2 ||
                    e.getKeyCode() == KeyEvent.VK_3 ||
                    e.getKeyCode() == KeyEvent.VK_4 ||
                    e.getKeyCode() == KeyEvent.VK_5 ||
                    e.getKeyCode() == KeyEvent.VK_6 ||
                    e.getKeyCode() == KeyEvent.VK_7 ||
                    e.getKeyCode() == KeyEvent.VK_8 ||
                    e.getKeyCode() == KeyEvent.VK_9;

        if(numberKeyWasPressed && !FileConstants.TIME_CALC_PROFILES_TXT_FILE.exists()) {
            JOptionPane.showMessageDialog(null, "Warning: There is no profile assigned to Key with number, you pressed.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        if (numberKeyWasPressed  && FileConstants.TIME_CALC_PROFILES_TXT_FILE.exists()) {

            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(FileConstants.TIME_CALC_PROFILES_TXT_FILE));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            int profileNumber = 0;
            Toaster toaster = new Toaster();
            toaster.setDisplayTime(5000);
            switch(e.getKeyCode()) {
                case KeyEvent.VK_0: profileNumber = 0;break;
                case KeyEvent.VK_1: profileNumber = 1;break;
                case KeyEvent.VK_2: profileNumber = 2;break;
                case KeyEvent.VK_3: profileNumber = 3;break;
                case KeyEvent.VK_4: profileNumber = 4;break;
                case KeyEvent.VK_5: profileNumber = 5;break;
                case KeyEvent.VK_6: profileNumber = 6;break;
                case KeyEvent.VK_7: profileNumber = 7;break;
                case KeyEvent.VK_8: profileNumber = 8;break;
                case KeyEvent.VK_9: profileNumber = 9;break;
            }
            String key = String.valueOf(profileNumber);
            if(properties.containsKey(key)) {
                String profileName = (String) properties.get(key);
                if(profileName.equals( timeCalcConfiguration.profileNameProperty)) {
                    toaster.showToaster("Profile \"" + profileName + "\" is already active. Nothing to do");
                } else {
                    toaster.showToaster("Info: Changing profile to: " + ((
                            profileName.isEmpty() ? "{Default profile}" :
                                    profileName)));
                    TimeCalcProperties.getInstance().loadProfile(profileName);
                    timeCalcConfiguration.loadFromTimeCalcProperties(
                            TimeCalcProperties.getInstance());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Warning: There is no profile assigned to Key " + profileNumber, "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {

            if(FileConstants.TIME_CALC_PROFILES_TXT_FILE.exists()) {
                try {
                    Utils.showNotification(Utils.readTextFromFile(FileConstants.TIME_CALC_PROFILES_TXT_FILE), 200);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    Utils.showNotification("Error: " + ioException.getMessage());
                }
            } else {
                Utils.showNotification("Warning: There are no numbers assigned to profiles. Update file: " + FileConstants.TIME_CALC_PROFILES_TXT_FILE.getAbsolutePath() + ".");
            }

        }
        window.repaint();
    }

}
