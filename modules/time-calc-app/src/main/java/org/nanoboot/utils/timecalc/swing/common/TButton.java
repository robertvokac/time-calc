package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TButton extends JButton implements GetProperty {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;
    public final BooleanProperty visibilitySupportedColoredProperty
            = new BooleanProperty("visibilitySupportedColoredProperty", true);
    public final BooleanProperty visibleProperty
            = new BooleanProperty("visibleProperty", true);
    public StringProperty visibilityProperty
            = new StringProperty("visibilityProperty",
                    Visibility.STRONGLY_COLORED.name());
    private Color originalBackground;
    private Color originalForeground;
    private int customWidth = 0;
    private int customHeight = 0;

    public TButton(String label) {
        this(label, 0);
    }
    public TButton(String label, int customWidth) {
        this(label, customWidth, 0);
    }
    public TButton(String label, int customWidth, int customHeight) {
        super(label);
        this.customWidth = customWidth;
        this.customHeight = customHeight;
        new Timer(100, e -> {
            if (!MainWindow.hideShowFormsCheckBox.isSelected()) {
                setVisible(false);
                return;
            } else {
                setVisible(true);
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
        setBounds(x, y, customWidth == 0 ? BUTTON_WIDTH : customWidth, customHeight == 0 ? BUTTON_HEIGHT : customHeight);
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
        setBounds(jComponent.getX() + jComponent.getWidth() + SwingUtils.MARGIN,
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

    @Override
    public Property getVisibilityProperty() {
        return visibilityProperty;
    }

    @Override
    public Property getVisibilitySupportedColoredProperty() {
        return visibilitySupportedColoredProperty;
    }
}
