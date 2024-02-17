package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcButton;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcWindow;
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
    private final TimeCalcButton commandButton;
    private final TimeCalcWindow window;

    public TimeCalcKeyAdapter(
            TimeCalcConfiguration timeCalcConfiguration,
            TimeCalcApp timeCalcApp,
            TimeCalcButton commandButton,
            TimeCalcWindow window
    ) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        this.timeCalcApp = timeCalcApp;
        this.commandButton = commandButton;
        this.window = window;
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("Key was pressed: " + e);
        boolean onlyGreyOrNone =
                timeCalcConfiguration.visibilitySupportedColoredProperty
                        .isEnabled();
        Visibility visibility = Visibility
                .valueOf(timeCalcApp.visibilityProperty.getValue());
        System.out.println("visibility=" + visibility);
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("Key UP was pressed: " + e);
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
            System.out.println("Key V was pressed: " + e);
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
        if (e.getKeyCode() == KeyEvent.VK_R) {
            commandButton.doClick();
        }
        if (e.getKeyCode() == KeyEvent.VK_T) {
            Utils.toastsAreEnabled.flip();
        }

        window.repaint();
    }

}
