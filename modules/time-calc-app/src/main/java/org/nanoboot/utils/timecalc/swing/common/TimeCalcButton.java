package org.nanoboot.utils.timecalc.swing.common;

import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Color;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class TimeCalcButton extends JButton {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;
    private Color originalBackground;
    private Color originalForeground;

    public TimeCalcButton(String label) {
        super(label);
    }

    public void setBounds(int x, int y) {
        setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.originalBackground = getBackground();
        this.originalForeground = getForeground();
        new Timer(100, e -> repaint()).start();
    }
    public void setOriginalBackground() {
        this.setBackground(originalBackground);;
    }

    public void setOriginalForeground() {
        this.setForeground(originalForeground);;
    }
}
