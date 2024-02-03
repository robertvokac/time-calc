package org.nanoboot.utils.timecalc.gui.common;

import javax.swing.JButton;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class TimeCalcButton extends JButton {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;

    public TimeCalcButton(String label) {
        super(label);
    }

    public void setBounds(int x, int y) {
        setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }
}
