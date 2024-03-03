package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

        int keyCode = e.getKeyCode();

        boolean numberKeyWasPressed = keyCode == KeyEvent.VK_0 ||
                                      keyCode == KeyEvent.VK_1 ||
                                      keyCode == KeyEvent.VK_2 ||
                                      keyCode == KeyEvent.VK_3 ||
                                      keyCode == KeyEvent.VK_4 ||
                                      keyCode == KeyEvent.VK_5 ||
                                      keyCode == KeyEvent.VK_6 ||
                                      keyCode == KeyEvent.VK_7 ||
                                      keyCode == KeyEvent.VK_8 ||
                                      keyCode == KeyEvent.VK_9;

        if (numberKeyWasPressed && !FileConstants.TIME_CALC_PROFILES_TXT_FILE
                .exists()) {
            Utils.showNotification(
                    "Warning: There is no profile assigned to Key with number, you pressed.");
        }

        switch (keyCode) {

            case KeyEvent.VK_UP: {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                : Visibility.STRONGLY_COLORED.name());
                break;
            }

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_PERIOD: {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.NONE.name());
                break;
            }

            case KeyEvent.VK_H: {
                if (visibility.isNone()) {
                    timeCalcApp.visibilityProperty
                            .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                    : Visibility.STRONGLY_COLORED.name());
                } else {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.NONE.name());
                }
                break;
            }
            case KeyEvent.VK_G: {
                if (visibility.isGray() && !onlyGreyOrNone) {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.WEAKLY_COLORED.name());
                } else {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.GRAY.name());
                    MainWindow.hideShowCheckBox.setSelected(false);
                }
                break;
            }
            case KeyEvent.VK_C: {
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
                break;
            }
            case KeyEvent.VK_V: {
                if (visibility.isNone()) {
                    timeCalcApp.visibilityProperty
                            .setValue(onlyGreyOrNone ? Visibility.GRAY.name()
                                    : Visibility.STRONGLY_COLORED.name());
                } else {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.NONE.name());
                }
                break;
            }
            case KeyEvent.VK_SPACE: {
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
                break;
            }
            case KeyEvent.VK_F2: {
                window.doCommand();
                break;
            }
            case KeyEvent.VK_R: {
                window.doRestart();
                break;
            }
            case KeyEvent.VK_N: {
                timeCalcConfiguration.notificationsVisibleProperty.flip();
                break;
            }
            case KeyEvent.VK_W: {
                window.openWorkDaysWindow();
                break;
            }
            case KeyEvent.VK_A: {
                window.openActivitiesWindow();
                break;
            }
            case KeyEvent.VK_X: {
                window.doExit();
                break;
            }

            case KeyEvent.VK_S: {
                window.openConfigWindow();
                break;
            }

            case KeyEvent.VK_J: {
                if (timeCalcConfiguration.jokesVisibleProperty.isEnabled()) {
                    Jokes.showRandom();
                    break;
                }

            }

            case KeyEvent.VK_P:
            case KeyEvent.VK_F1: {
                window.openHelpWindow();
                break;
            }
            case KeyEvent.VK_U: {
                window.doEnableEverything();
                break;

            }
            case KeyEvent.VK_I: {
                window.doDisableAlmostEverything();
                break;
            }
            case KeyEvent.VK_E: {
                timeCalcConfiguration.batteryWavesVisibleProperty.flip();
                break;
            }

            case KeyEvent.VK_B: {
                MainWindow.hideShowCheckBox
                        .setSelected(!MainWindow.hideShowCheckBox.isSelected());
                break;
            }
            case KeyEvent.VK_F: {

                if (FileConstants.TIME_CALC_PROFILES_TXT_FILE.exists()) {
                    try {
                        Utils.showNotification(Utils.readTextFromFile(
                                FileConstants.TIME_CALC_PROFILES_TXT_FILE),
                                15000, 200);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        Utils.showNotification(
                                "Error: " + ioException.getMessage());
                    }
                } else {
                    Utils.showNotification(
                            "Warning: There are no numbers assigned to profiles. Update file: "
                            + FileConstants.TIME_CALC_PROFILES_TXT_FILE
                                    .getAbsolutePath() + ".");
                }
                break;
            }
            case KeyEvent.VK_Q: {
                timeCalcConfiguration.squareVisibleProperty.flip();
                break;
            }
            case KeyEvent.VK_L: {
                timeCalcConfiguration.circleVisibleProperty.flip();
                break;
            }
            case KeyEvent.VK_Y: {
                timeCalcConfiguration.smileysVisibleOnlyIfMouseMovingOverProperty
                        .flip();
                break;
            }
            case KeyEvent.VK_M: {
                timeCalcConfiguration.walkingHumanVisibleProperty.flip();
                break;
            }
            default:
                Utils.showNotification(
                        "Unsupported key was pressed. There is no key shortcut for this key.");

        }
        if (numberKeyWasPressed && FileConstants.TIME_CALC_PROFILES_TXT_FILE
                .exists()) {

            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(
                        FileConstants.TIME_CALC_PROFILES_TXT_FILE));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            int profileNumber = 0;
            switch (keyCode) {
                case KeyEvent.VK_0:
                    profileNumber = 0;
                    break;
                case KeyEvent.VK_1:
                    profileNumber = 1;
                    break;
                case KeyEvent.VK_2:
                    profileNumber = 2;
                    break;
                case KeyEvent.VK_3:
                    profileNumber = 3;
                    break;
                case KeyEvent.VK_4:
                    profileNumber = 4;
                    break;
                case KeyEvent.VK_5:
                    profileNumber = 5;
                    break;
                case KeyEvent.VK_6:
                    profileNumber = 6;
                    break;
                case KeyEvent.VK_7:
                    profileNumber = 7;
                    break;
                case KeyEvent.VK_8:
                    profileNumber = 8;
                    break;
                case KeyEvent.VK_9:
                    profileNumber = 9;
                    break;
            }
            String key = String.valueOf(profileNumber);
            if (properties.containsKey(key)) {
                String profileName = (String) properties.get(key);
                if (profileName
                        .equals(timeCalcConfiguration.profileNameProperty)) {
                    Utils.showNotification("Profile \"" + profileName
                                           + "\" is already active. Nothing to do",
                            5000);
                } else {
                    Utils.showNotification("Info: Changing profile to: " + ((
                            profileName.isEmpty() ? "{Default profile}" :
                                    profileName)), 5000);
                    TimeCalcProperties.getInstance().loadProfile(profileName);
                    timeCalcConfiguration.loadFromTimeCalcProperties(
                            TimeCalcProperties.getInstance());
                }

            } else {
                Utils.showNotification(
                        "Warning: There is no profile assigned to Key "
                        + profileNumber, 5000);
            }
        }

        window.repaint();
    }

}
