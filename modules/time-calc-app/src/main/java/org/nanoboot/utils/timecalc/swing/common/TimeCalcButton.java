package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcManager;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TimeCalcButton extends JButton {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;
    public StringProperty visibilityProperty =
            new StringProperty("visibilityProperty",
                    Visibility.STRONGLY_COLORED.name());
    private Color originalBackground;
    private Color originalForeground;

    public TimeCalcButton(String label) {
        super(label);
        new Timer(100, e -> repaint()).start();
    }

    public void setBounds(int x, int y) {
        setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.originalBackground = getBackground();
        this.originalForeground = getForeground();
    }

    public void setOriginalBackground() {
        this.setBackground(originalBackground);
    }

    public void setOriginalForeground() {
        this.setForeground(originalForeground);
    }

    public void setBoundsFromLeft(JComponent jComponent) {
        setBounds(jComponent.getX() + jComponent.getWidth() + TimeCalcManager.MARGIN, jComponent.getY());
    }
    public void setBoundsFromTop(JComponent jComponent) {
        setBounds(TimeCalcManager.MARGIN, jComponent.getY()
                + jComponent.getHeight()
                + TimeCalcManager.MARGIN);
    }
}
