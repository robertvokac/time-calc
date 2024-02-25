package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Robert
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
        boolean onlyGreyOrNone =
                timeCalcConfiguration.visibilitySupportedColoredProperty
                        .isDisabled();
        Visibility visibility = Visibility
                .valueOf(timeCalcApp.visibilityProperty.getValue());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            timeCalcApp.visibilityProperty
                    .setValue(onlyGreyOrNone ? Visibility.GRAY.name() :
                            Visibility.STRONGLY_COLORED.name());
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            timeCalcApp.visibilityProperty
                    .setValue(Visibility.NONE.name());
        }

        if (e.getKeyCode() == KeyEvent.VK_H) {
            if (visibility.isNone()) {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name() :
                                Visibility.STRONGLY_COLORED.name());
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
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name() :
                                Visibility.STRONGLY_COLORED.name());
            } else {
                timeCalcApp.visibilityProperty
                        .setValue(Visibility.NONE.name());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (visibility.isStronglyColored()) {
                timeCalcApp.visibilityProperty
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name() :
                                Visibility.WEAKLY_COLORED.name());
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
                        .setValue(onlyGreyOrNone ? Visibility.GRAY.name() :
                                Visibility.STRONGLY_COLORED.name());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            window.doCommand();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            window.doRestart();
        }
        if (e.getKeyCode() == KeyEvent.VK_T) {
            Utils.toastsAreEnabled.flip();
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


        if (e.getKeyCode() == KeyEvent.VK_P) {
            window.openHelpWindow();
        }

        window.repaint();
    }

}
