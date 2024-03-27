package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcApp;
import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.app.TimeCalcProperties;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.windows.MainWindow;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;

import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import lombok.Setter;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

/**
 * @author Robert Vokac
 * @since 26.02.2024
 */
public class TimeCalcKeyAdapter extends KeyAdapter {

    private static final int[] ALLOWED_KEY_CODES =
            {KeyEvent.VK_R, KeyEvent.VK_X, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_Z,KeyEvent.VK_O};
    private static final String EXTENDED_FEATURES_ARE_DISABLED =
            "Extended features are disabled";
    private final TimeCalcConfiguration timeCalcConfiguration;
    private final TimeCalcApp timeCalcApp;
    private final MainWindow mainWindow;
    private final Time time;
    private boolean changeByFiveMinutes = false;
    @Setter
    private int msToAdd = 1;

    public TimeCalcKeyAdapter(
            TimeCalcConfiguration timeCalcConfiguration,
            TimeCalcApp timeCalcApp,
            MainWindow mainWindow,
            Time time
    ) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        this.timeCalcApp = timeCalcApp;
        this.mainWindow = mainWindow;
        this.time = time;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        boolean shiftDown = e.isShiftDown();
        boolean ctrlDown = e.isControlDown();
        boolean altDown = e.isAltDown();
        //boolean metaDown = e.isMetaDown();
        if (!shiftDown && !ctrlDown && !altDown /*&& !metaDown*/) {
            processKeyCode(keyCode);
        } else //if (!metaDown)
        {
            processShifCtrlAltModeKeyCodes(keyCode, shiftDown, ctrlDown,
                    altDown);
        }
//        else {
//            processMetaKeyCodes(keyCode);
//        }
        //meta key ???
    }

    public void processShifCtrlAltModeKeyCodes(int keyCode, boolean shiftDown,
            boolean ctrlDown, boolean altDown) {
        if (shiftDown && ctrlDown) {
            Utils.showNotification("Following key shortcut is not supported: SHIFT + CTRL");
            return;
        }
        if (shiftDown && altDown) {
            Utils.showNotification("Following key shortcut is not supported: SHIFT + ALT");
            return;
        }
        if (ctrlDown && altDown) {
            Utils.showNotification("Following key shortcut is not supported: CTRL + ALT");
            return;
        }
        boolean increase = shiftDown;
        boolean decrease = ctrlDown;
        boolean reset = altDown;
        TTime changeTTime = changeByFiveMinutes ? TTime.T_TIME_FIVE_MINUTES : TTime.T_TIME_ONE_MINUTE;
        switch (keyCode) {
            case KeyEvent.VK_Y: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " year.");
                updateProperty(timeCalcConfiguration.testYearCustomProperty, increase, decrease, reset,
                        Calendar.YEAR);
                break;
            }
            case KeyEvent.VK_N: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " month.");
                updateProperty(timeCalcConfiguration.testMonthCustomProperty, increase, decrease, reset,
                        Calendar.MONTH);
                break;
            }
            case KeyEvent.VK_D: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " day.");
                updateProperty(timeCalcConfiguration.testDayCustomProperty, increase, decrease, reset,
                        Calendar.DAY_OF_MONTH);
                break;
            }
            case KeyEvent.VK_H: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " hour.");
                updateProperty(timeCalcConfiguration.testHourCustomProperty, increase, decrease, reset,
                        Calendar.HOUR_OF_DAY);
                break;
            }
            case KeyEvent.VK_M: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " minute.");
                updateProperty(timeCalcConfiguration.testMinuteCustomProperty, increase, decrease, reset,
                        Calendar.MINUTE);
                break;
            }
            case KeyEvent.VK_S: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " second.");
                updateProperty(timeCalcConfiguration.testSecondCustomProperty, increase, decrease, reset,
                        Calendar.SECOND);
                break;
            }
            case KeyEvent.VK_I: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " millisecond.");
                updateProperty(timeCalcConfiguration.testMillisecondCustomProperty, increase, decrease, reset,
                        Calendar.MILLISECOND);
                break;
            }

            case KeyEvent.VK_U: {
                int totalMs = msToAdd;
                boolean negative = false;
                if (totalMs < 0) {
                    totalMs = Math.abs(totalMs);
                    negative = true;
                }
                //System.out.println("going to add ms:" +msToAdd);
                int ms_ = totalMs % 1000;
                totalMs = totalMs - ms_;
                int s_ = totalMs / 1000 % 60;
                totalMs = totalMs - s_ * 1000;
                int m_ = totalMs / 1000 / 60 % 60;
                totalMs = totalMs - m_ * 1000 * 60;
                int h_ = totalMs / 1000 / 60 / 60 % 60;
                totalMs = totalMs - h_ * 1000 * 60 * 60;
                int d_ = totalMs / 1000 / 60 / 60 / 24;
                totalMs = totalMs - d_ * 1000 * 60 * 60 * 24;
                if (negative && (increase || decrease)) {
                    increase = false;
                    decrease = true;
                }
                
                updateProperty(timeCalcConfiguration.testDayCustomProperty, increase, decrease, reset,
                        Calendar.DAY_OF_MONTH, d_);
                updateProperty(timeCalcConfiguration.testHourCustomProperty, increase, decrease, reset,
                        Calendar.HOUR_OF_DAY, h_);
                updateProperty(timeCalcConfiguration.testMinuteCustomProperty, increase, decrease, reset,
                        Calendar.MINUTE, m_);
                updateProperty(timeCalcConfiguration.testSecondCustomProperty, increase, decrease, reset,
                        Calendar.SECOND, s_);
                updateProperty(timeCalcConfiguration.testMillisecondCustomProperty, increase, decrease, reset,
                        Calendar.MILLISECOND, ms_);
                break;
            }
            case KeyEvent.VK_K: {
                //Utils.showNotification((increase ? "Increasing" : (decrease ? "Decreasing" : "Reseting")) + " millisecond.");
                for (int i = 1; i <= 7; i++) {
                    updateProperty(timeCalcConfiguration.testDayCustomProperty, increase, decrease, reset,
                            Calendar.DAY_OF_MONTH);
                }
                break;
            }
            case KeyEvent.VK_Q: {
                if(mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {
                    showWarningExtendedFeaturesAreDisabled();
                    return;
                }
                double oldSpeed = this.mainWindow.getSpeed();
                if (oldSpeed < 0) {
                    oldSpeed = 1.0d;
                }
                if (increase) {
                    this.mainWindow.increaseSpeed();
                }
                if (decrease) {
                    this.mainWindow.decreaseSpeed();
                }
                if (reset) {
                    this.mainWindow.resetSpeed();
                }
                final double newSpeed = this.mainWindow.getSpeed();

                if (oldSpeed != newSpeed) {
                    final double msDouble = Math.pow(2, newSpeed) * 1000;
                    TTime t = TTime.ofMilliseconds(((int) msDouble));
                    Utils.showNotification("Speed was changed from "
                            + ((int) oldSpeed)
                            + " to: " + ((int) newSpeed) + " (" + (NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(Math.pow(2, newSpeed))) + ") ("
                            + (newSpeed <= -10 ? (NumberFormats.FORMATTER_SIX_DECIMAL_PLACES.format(msDouble) + "ms") : (/*newSpeed <=21*/t.getHour() < 24 ? t : ((long) (msDouble / 1000d / 60d / 60d / 24d) + " days")))
                            + " /1s)");
                } else {

                    if (decrease) {
                        Utils.showNotification("Current speed cannot be decreased: "
                                + NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(oldSpeed));
                    }
                    if (increase) {
                        Utils.showNotification("Current speed cannot be increased: "
                                + NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(oldSpeed));
                    }
                }

                break;
            }

            case KeyEvent.VK_A: {

                if (increase) {
                    mainWindow.increaseArrival(changeTTime);
                }
                if (decrease) {
                    mainWindow.decreaseArrival(changeTTime);
                }
                break;
            }
            case KeyEvent.VK_O: {

                if (increase) {
                    mainWindow.increaseOvertime(changeTTime);
                }
                if (decrease) {
                    mainWindow.decreaseOvertime(changeTTime);
                }
                break;
            }
            case KeyEvent.VK_W: {
                if (increase) {
                    mainWindow.increaseWork(changeTTime);
                }
                if (decrease) {
                    mainWindow.decreaseWork(changeTTime);
                }
                break;
            }
            case KeyEvent.VK_P: {
                if(mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {
                    showWarningExtendedFeaturesAreDisabled();
                    break;
                }
                if (increase) {
                    mainWindow.increasePause(changeTTime);
                }
                if (decrease) {
                    mainWindow.decreasePause(changeTTime);
                }
                break;
            }
            case KeyEvent.VK_C: {
                this.changeByFiveMinutes = increase;
                Utils.showNotification("Time will be changed by " + (increase ? "5 minutes" : "1 minute") + ".");
                break;
            }
            case KeyEvent.VK_E: {
                if (ctrlDown) {
                    mainWindow.doSaveButtonClick();
                }
                break;
            }
            default:
//                Utils.showNotification(
//                        "Unsupported key was pressed. There is no key shortcut for this key: "
//                        + keyCode);
        }
    }

    private void showWarningExtendedFeaturesAreDisabled() {

        Utils.showNotification(EXTENDED_FEATURES_ARE_DISABLED);
    }

    //    private void processMetaKeyCodes(int keyCode) {
//
//        switch (keyCode) {
//            case KeyEvent.VK_S: {
//
//                break;
//            }
//            default:
//        }
//    }
    private void updateProperty(IntegerProperty integerProperty,
            boolean increase, boolean decrease, boolean reset, int timeUnit) {
        updateProperty(integerProperty, increase, decrease, reset, timeUnit, 1);
    }

    private void updateProperty(IntegerProperty integerProperty,
            boolean increase, boolean decrease, boolean reset, int timeUnit, int value) {
        if(mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {
            showWarningExtendedFeaturesAreDisabled();
            return;
        }
        if (value == 0) {
            //nothing to do
            return;
        }
        int currentValue = integerProperty.getValue();

        if ((increase || decrease) && currentValue == Integer.MAX_VALUE) {
            switch (timeUnit) {
                case Calendar.YEAR:
                    integerProperty.setValue(time.yearProperty.getValue());
                    break;
                case Calendar.MONTH:
                    integerProperty.setValue(time.monthProperty.getValue());
                    break;
                case Calendar.DAY_OF_MONTH:
                    integerProperty.setValue(time.dayProperty.getValue());
                    break;
                case Calendar.HOUR_OF_DAY:
                    integerProperty.setValue(time.hourProperty.getValue());
                    break;
                case Calendar.MINUTE:
                    integerProperty.setValue(time.minuteProperty.getValue());
                    break;
                case Calendar.SECOND:
                    integerProperty.setValue(time.secondProperty.getValue());
                    break;
                case Calendar.MILLISECOND:
                    integerProperty.setValue(time.millisecondProperty.getValue());
                    break;
                default:
                    throw new TimeCalcException("Unsupported time unit: " + timeUnit);
            }
        }
        Calendar cal = time.asCalendar();
        int oldYear = cal.get(Calendar.YEAR);
        int oldMonth = cal.get(Calendar.MONTH) + 1;
        int oldDay = cal.get(Calendar.DAY_OF_MONTH);
        int oldHour = cal.get(Calendar.HOUR_OF_DAY);
        int oldMinute = cal.get(Calendar.MINUTE);
        int oldSecond = cal.get(Calendar.SECOND);
        int oldMillisecond = cal.get(Calendar.MILLISECOND);
        cal.add(timeUnit, increase ? value : (-value));
        int newValue = cal.get(timeUnit);
        if (Calendar.MONTH == timeUnit) {
            newValue++;
        }
        if (reset) {
            newValue = Integer.MAX_VALUE;
        }
        integerProperty.setValue(newValue);
        int newYear = cal.get(Calendar.YEAR);
        int newMonth = cal.get(Calendar.MONTH) + 1;
        int newDay = cal.get(Calendar.DAY_OF_MONTH);
        int newHour = cal.get(Calendar.HOUR_OF_DAY);
        int newMinute = cal.get(Calendar.MINUTE);
        int newSecond = cal.get(Calendar.SECOND);
        int newMillisecond = cal.get(Calendar.MILLISECOND);
        if (increase) {
            switch (timeUnit) {
                case Calendar.YEAR:
                    break;
                case Calendar.MONTH:
                    if (oldYear != newYear) {
                        updateProperty(timeCalcConfiguration.testYearCustomProperty, increase, decrease, reset, Calendar.YEAR);
                    }
                    break;
                case Calendar.DAY_OF_MONTH:
                    if (oldMonth != newMonth) {
                        updateProperty(timeCalcConfiguration.testMonthCustomProperty, increase, decrease, reset, Calendar.MONTH);
                    }
                    break;
                case Calendar.HOUR_OF_DAY:
                    if (oldDay != newDay) {
                        updateProperty(timeCalcConfiguration.testDayCustomProperty, increase, decrease, reset, Calendar.DAY_OF_MONTH);
                    }
                    break;
                case Calendar.MINUTE:
                    if (oldHour != newHour) {
                        updateProperty(timeCalcConfiguration.testHourCustomProperty, increase, decrease, reset, Calendar.HOUR_OF_DAY);
                    }
                    break;
                case Calendar.SECOND:
                    if (oldMinute != newMinute) {
                        updateProperty(timeCalcConfiguration.testMinuteCustomProperty, increase, decrease, reset, Calendar.MINUTE);
                    }
                    break;
                case Calendar.MILLISECOND:
                    if (oldSecond != newSecond) {
                        updateProperty(timeCalcConfiguration.testSecondCustomProperty, increase, decrease, reset, Calendar.SECOND);
                    }
                    break;
                default:
                    throw new TimeCalcException("Unsupported time unit: " + timeUnit);
            }
        }
        if (decrease) {
            switch (timeUnit) {
                case Calendar.YEAR:
                    break;
                case Calendar.MONTH:
                    if (oldYear != newYear) {
                        updateProperty(timeCalcConfiguration.testYearCustomProperty, increase, decrease, reset, Calendar.YEAR);
                    }
                    break;
                case Calendar.DAY_OF_MONTH:
                    if (oldMonth != newMonth) {
                        updateProperty(timeCalcConfiguration.testMonthCustomProperty, increase, decrease, reset, Calendar.MONTH);
                    }
                    break;
                case Calendar.HOUR_OF_DAY:
                    if (oldDay != newDay) {
                        updateProperty(timeCalcConfiguration.testDayCustomProperty, increase, decrease, reset, Calendar.DAY_OF_MONTH);
                    }
                    break;
                case Calendar.MINUTE:
                    if (oldHour != newHour) {
                        updateProperty(timeCalcConfiguration.testHourCustomProperty, increase, decrease, reset, Calendar.HOUR_OF_DAY);
                    }
                    break;
                case Calendar.SECOND:
                    if (oldMinute != newMinute) {
                        updateProperty(timeCalcConfiguration.testMinuteCustomProperty, increase, decrease, reset, Calendar.MINUTE);
                    }
                    break;
                case Calendar.MILLISECOND:
                    if (oldSecond != newSecond) {
                        updateProperty(timeCalcConfiguration.testSecondCustomProperty, increase, decrease, reset, Calendar.SECOND);
                    }
                    break;
                default:
                    throw new TimeCalcException("Unsupported time unit: " + timeUnit);
            }
        }

    }

    public void processKeyCode(int keyCode) {
        boolean onlyGreyOrNone
                = timeCalcConfiguration.visibilitySupportedColoredProperty
                        .isDisabled();
        Visibility visibility = Visibility
                .valueOf(timeCalcApp.visibilityProperty.getValue());
        boolean numberKeyWasPressed = keyCode == KeyEvent.VK_0
                || keyCode == KeyEvent.VK_1
                || keyCode == KeyEvent.VK_2
                || keyCode == KeyEvent.VK_3
                || keyCode == KeyEvent.VK_4
                || keyCode == KeyEvent.VK_5
                || keyCode == KeyEvent.VK_6
                || keyCode == KeyEvent.VK_7
                || keyCode == KeyEvent.VK_8
                || keyCode == KeyEvent.VK_9;

        if (numberKeyWasPressed && !FileConstants.TIME_CALC_PROFILES_TXT_FILE
                .exists()) {
            Utils.showNotification(
                    "Warning: There is no profile assigned to Key with number, you pressed.");
        }

        if(mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {
            if(!Arrays.stream(ALLOWED_KEY_CODES).filter(k->k== keyCode).findFirst().isPresent()){
                showWarningExtendedFeaturesAreDisabled();
                return;
            }
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

            case KeyEvent.VK_G: {
                if (visibility.isGray() && !onlyGreyOrNone) {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.WEAKLY_COLORED.name());
                } else {
                    timeCalcApp.visibilityProperty
                            .setValue(Visibility.GRAY.name());
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
            case KeyEvent.VK_V:

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
                mainWindow.doCommand();
                break;
            }
            case KeyEvent.VK_R: {
                mainWindow.doRestart();
                break;
            }
            case KeyEvent.VK_N: {
                timeCalcConfiguration.notificationsVisibleProperty.flip();
                break;
            }
            case KeyEvent.VK_W: {
                mainWindow.openWorkDaysWindow();
                break;
            }
            case KeyEvent.VK_A: {
                mainWindow.openActivitiesWindow();
                break;
            }
            case KeyEvent.VK_X: {
                mainWindow.doExit();
                break;
            }

            case KeyEvent.VK_S: {
                mainWindow.openConfigWindow();
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
                mainWindow.openHelpWindow();
                break;
            }
            case KeyEvent.VK_U: {
                mainWindow.doEnableEverything();
                break;

            }
            case KeyEvent.VK_I: {
                mainWindow.doDisableAlmostEverything();
                break;
            }
            case KeyEvent.VK_E: {
                timeCalcConfiguration.batteryWavesVisibleProperty.flip();
                break;
            }

            case KeyEvent.VK_B: {
                MainWindow.hideShowFormsCheckBox
                        .setSelected(!MainWindow.hideShowFormsCheckBox.isSelected());
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
                if (timeCalcConfiguration.smileysVisibleOnlyIfMouseMovingOverProperty.isDisabled() && timeCalcConfiguration.smileysVisibleProperty.isDisabled()) {
                    timeCalcConfiguration.smileysVisibleProperty.enable();
                }
                break;
            }
            case KeyEvent.VK_M: {
                timeCalcConfiguration.walkingHumanVisibleProperty.flip();
                break;
            }
            case KeyEvent.VK_LEFT: {
                switchProfile(true, false);
                break;
            }
            case KeyEvent.VK_RIGHT: {
                switchProfile(false, true);
                break;
            }
            case KeyEvent.VK_D: {
                timeCalcConfiguration.testYearCustomProperty.setValue(Integer.MAX_VALUE);
                timeCalcConfiguration.testMonthCustomProperty.setValue(Integer.MAX_VALUE);
                timeCalcConfiguration.testDayCustomProperty.setValue(Integer.MAX_VALUE);
                timeCalcConfiguration.testHourCustomProperty.setValue(Integer.MAX_VALUE);
                timeCalcConfiguration.testMinuteCustomProperty.setValue(Integer.MAX_VALUE);
                timeCalcConfiguration.testSecondCustomProperty.setValue(Integer.MAX_VALUE);
                timeCalcConfiguration.testMillisecondCustomProperty.setValue(Integer.MAX_VALUE);
                Utils.showNotification(timeCalcConfiguration.print(), 15000, 400);
                this.mainWindow.resetSpeed();
                break;
            }

            case KeyEvent.VK_K: {
                if (timeCalcConfiguration.clockVisibleProperty.isEnabled()) {
                    timeCalcConfiguration.clockVisibleProperty.disable();
                } else {
                    timeCalcConfiguration.clockVisibleProperty.enable();
                    timeCalcConfiguration.clockHandsLongVisibleProperty.enable();
                    timeCalcConfiguration.clockHandsColoredProperty.enable();
                    timeCalcConfiguration.clockHandsHourVisibleProperty.enable();
                    timeCalcConfiguration.clockHandsMinuteVisibleProperty.enable();
                    timeCalcConfiguration.clockHandsSecondVisibleProperty.enable();
                    timeCalcConfiguration.clockHandsMillisecondVisibleProperty.enable();
                    timeCalcConfiguration.clockBorderVisibleProperty.enable();
                    timeCalcConfiguration.clockBorderOnlyHoursProperty.disable();
                    timeCalcConfiguration.clockNumbersVisibleProperty.enable();
                    timeCalcConfiguration.clockCircleVisibleProperty.enable();
                    timeCalcConfiguration.clockCircleStrongBorderProperty.disable();
                    timeCalcConfiguration.clockCircleBorderColorProperty.setValue("0,0,255");
                    timeCalcConfiguration.clockCentreCircleVisibleProperty.enable();
                    timeCalcConfiguration.clockCentreCircleBlackProperty.disable();
                    timeCalcConfiguration.clockProgressVisibleOnlyIfMouseMovingOverProperty.disable();
                    timeCalcConfiguration.clockDateVisibleOnlyIfMouseMovingOverProperty.disable();
                }
                break;
            }
            case KeyEvent.VK_Z: {
                String newForgetOvertime = (String) JOptionPane.showInputDialog(
                        null,
                        "Set new forget overtime. Current value is: " + mainWindow.getForgetOvertime(),
                        "New forget overtime",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        mainWindow.getForgetOvertime()
                );
                int newForgetOvertimeInt = -1;
                if (newForgetOvertime != null) {
                    if (newForgetOvertime.contains(":")) {
                        newForgetOvertimeInt = new TTime(newForgetOvertime).toTotalMilliseconds() / 1000 / 60;
                    } else {
                        try {
                            newForgetOvertimeInt
                                    = Integer.parseInt(newForgetOvertime);
                        } catch (Exception e) {
                            Utils.showNotification(e);
                        }
                    }
                }
                if (newForgetOvertimeInt >= 0) {
                    mainWindow.setForgetOvertime(newForgetOvertimeInt);
                } else {
                    Utils.showNotification("Error:Forget overtime must not be less than zero.");
                }
                break;
            }

            case KeyEvent.VK_SLASH: {
                if (timeCalcConfiguration.testEnabledProperty.isDisabled()) {
                    if (!Utils.askYesNo(null, "Do you really want to enable \"Test mode\"? If yes, then you will be allowed to set custom time.", "Enabling \"Test mode\"")) {
                        break;
                    }
                }
                timeCalcConfiguration.testEnabledProperty.flip();
                Utils.showNotification((timeCalcConfiguration.testEnabledProperty.isEnabled() ? "Enabled" : "Disabled") + " \"Test mode\".");
                break;
            }
            case KeyEvent.VK_O: {
                if (mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {
                    Utils.showNotification("Extended features are already disabled.");
                } else {
                    String question =
                            "Do you really want to disable the extended features of this application? Only the basic features will be enabled.";
                    if (!Utils.askYesNo(null, question, "Disabling \"Extended features\" of this app")) {
                        break;
                    }
                    try {
                        FileConstants.BASIC_TXT.createNewFile();

                        String msg = "Warning: " + "You disabled the extended features of this application. Only the basic features are enabled. To enable the extended features, please: 1. stop this application 2. delete manually this file: " + FileConstants.BASIC_TXT.getAbsolutePath() + " 3. start this application again.";
                        Utils.showNotification(msg, 30000, 400);

                        this.mainWindow.doRestart();
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                    break;
                }

                break;
            }
            case KeyEvent.VK_COMMA: {
                timeCalcConfiguration.speedNegativeProperty.flip();
                break;
            }
            
            case KeyEvent.VK_T: {
                timeCalcConfiguration.speedFloatingProperty.flip();
                break;
            }
            default:
                if (!numberKeyWasPressed) {
                    Utils.showNotification(
                            "Unsupported key was pressed. There is no key shortcut for this key: "
                            + keyCode);
                }

        }
        boolean profileSwitch =
                numberKeyWasPressed && FileConstants.TIME_CALC_PROFILES_TXT_FILE
                        .exists();
        if(profileSwitch && mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {
            showWarningExtendedFeaturesAreDisabled();
        }

        if (profileSwitch && !mainWindow.allowOnlyBasicFeaturesProperty.getValue()) {

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
                    Utils.showNotification("Info: Changing profile to: #" + profileNumber + " " + ((profileName.isEmpty() ? "{Default profile}"
                            : profileName)), 5000);
                    timeCalcConfiguration.saveToTimeCalcProperties();
                    TimeCalcProperties.getInstance().loadProfile(profileName);
                    timeCalcConfiguration.loadFromTimeCalcProperties(
                            TimeCalcProperties.getInstance());
                    Utils.writeTextToFile(FileConstants.TIME_CALC_CURRENT_PROFILE_TXT_FILE, profileName);
                }

            } else {
                Utils.showNotification(
                        "Warning: There is no profile assigned to Key "
                        + profileNumber, 5000);
            }
        }

        mainWindow.repaint();
    }

    private void switchProfile(boolean previous, boolean next) {
        if ((previous && next) || (!previous && !next)) {
            //nothing to do
            return;
        }
        Properties profiles = new Properties();
        try {
            final String readTextFromFile = Utils.readTextFromFile(FileConstants.TIME_CALC_PROFILES_TXT_FILE);
            if (readTextFromFile == null || readTextFromFile.isEmpty()) {
                return;
            }

            try {
                profiles.load(new FileInputStream(
                        FileConstants.TIME_CALC_PROFILES_TXT_FILE));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (profiles.size() == 1) {
                return;
            }

        } catch (IOException ioException) {

            ioException.printStackTrace();
            Utils.showNotification(ioException);
            return;
        }

        String currentProfileName = null;
        try {
            currentProfileName = Utils.readTextFromFile(FileConstants.TIME_CALC_CURRENT_PROFILE_TXT_FILE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Utils.showNotification(ioException);
            return;
        }

        int numberOfCurrentProfile = next ? -1 : 9;
        for (Object number : profiles.keySet()) {
            String profileName = (String) profiles.get(number);
            if (profileName.equals(currentProfileName)) {
                numberOfCurrentProfile = Integer.valueOf(
                        (String) number);
                break;
            }
        }
        for (int i = (numberOfCurrentProfile + (next ? 1 : -1)); next ? i <= 9 : i >= 0; i = next ? (i + 1) : (i - 1)) {
            String number = String.valueOf(i);
            if (!profiles.containsKey(number)) {
                continue;
            }

            System.out.println("switching profile from " + numberOfCurrentProfile + " to profile " + number);
            processKeyCode(getKeyCodeForNumber(Integer.parseInt(number)));
            break;

        }
    }

    private int getKeyCodeForNumber(int number) {
        switch (number) {
            case 0:
                return KeyEvent.VK_0;
            case 1:
                return KeyEvent.VK_1;
            case 2:
                return KeyEvent.VK_2;
            case 3:
                return KeyEvent.VK_3;
            case 4:
                return KeyEvent.VK_4;
            case 5:
                return KeyEvent.VK_5;
            case 6:
                return KeyEvent.VK_6;
            case 7:
                return KeyEvent.VK_7;
            case 8:
                return KeyEvent.VK_8;
            case 9:
                return KeyEvent.VK_9;
            default:
                Utils.showNotification("Unsupported key: " + number);
                return 0;
        }
    }

}
