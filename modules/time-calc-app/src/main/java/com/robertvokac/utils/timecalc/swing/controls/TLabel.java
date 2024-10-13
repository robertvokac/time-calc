package com.robertvokac.utils.timecalc.swing.controls;

import com.robertvokac.utils.timecalc.app.GetProperty;
import com.robertvokac.utils.timecalc.entity.Visibility;
import com.robertvokac.utils.timecalc.utils.property.BooleanProperty;
import com.robertvokac.utils.timecalc.utils.property.Property;
import com.robertvokac.utils.timecalc.utils.property.StringProperty;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Color;
import com.robertvokac.utils.timecalc.app.TimeCalcApp;
import com.robertvokac.utils.timecalc.swing.windows.MainWindow;
import com.robertvokac.utils.timecalc.swing.common.SwingUtils;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TLabel extends JLabel implements GetProperty {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    private int customWidth = 0;
    private Color originalBackground;
    private Color originalForeground;
    public final BooleanProperty visibilitySupportedColoredProperty
            = new BooleanProperty("visibilitySupportedColoredProperty", true);
    public final BooleanProperty visibleProperty
            = new BooleanProperty("visibleProperty", true);
    public StringProperty visibilityProperty
            = new StringProperty("visibilityProperty",
                    Visibility.STRONGLY_COLORED.name());

    public TLabel(String text) {
        this(text, 0);
    }

    public TLabel(String text, int customWidth) {
        super(text);
        this.customWidth = customWidth;
        if(TimeCalcApp.IS_RUNNING_ON_LINUX) {
            setFont(SwingUtils.LINUX_FONT);
        }
        new Timer(100, e -> {
            if (!MainWindow.hideShowFormsCheckBox.isSelected()) {
                setVisible(false);
                return;
            } else {
                //setVisible(true);
            }
            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());
            setVisible(visibility.isNotNone() && visibleProperty.isEnabled());
            if (!visibility.isStronglyColored() || visibility.isGray()) {
                setBackground(MainWindow.BACKGROUND_COLOR);
                setForeground(MainWindow.FOREGROUND_COLOR);
            } else {
                setOriginalBackground();
                setOriginalForeground();
            }
        }).start();
    }

    public void setBounds(int x, int y) {
        setBounds(x, y, customWidth == 0 ? WIDTH : customWidth, HEIGHT);
        this.originalBackground = getBackground();
        this.originalForeground = getForeground();
    }

    public void setBoundsFromLeft(JComponent jComponent) {
        setBoundsFromLeft(jComponent, 0);
    }
    public void setBoundsFromLeft(JComponent jComponent, int additionalX) {
        setBounds(jComponent.getX() + jComponent.getWidth() + SwingUtils.MARGIN + additionalX,
                jComponent.getY());
    }

    public void setBoundsFromTop(JComponent jComponent) {
        setBoundsFromTop(jComponent, 1);
    }

    public void setBoundsFromTop(JComponent jComponent, int marginCount) {
        setBounds(SwingUtils.MARGIN, jComponent.getY()
                + jComponent.getHeight()
                + marginCount * SwingUtils.MARGIN);
    }

    public void setOriginalBackground() {
        this.setBackground(originalBackground);
    }

    public void setOriginalForeground() {
        this.setForeground(originalForeground);
    }

    @Override
    public Property getVisibilityProperty() {
        return visibilityProperty;
    }

    @Override
    public Property getVisibilitySupportedColoredProperty() {
        return visibilitySupportedColoredProperty;
    }

}
