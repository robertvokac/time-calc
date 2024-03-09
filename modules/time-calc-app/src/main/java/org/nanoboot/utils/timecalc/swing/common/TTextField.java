package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TTextField extends JTextField implements GetProperty {

    private static final int WIDTH = 50;
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
    public final StringProperty valueProperty = new StringProperty();

    public TTextField() {
        this("", 0);
    }

    public TTextField(String s) {
        this(s, 0);
    }

    public TTextField(String s, int customWidth) {
        super(s);
        this.customWidth = customWidth;
        valueProperty.setValue(s);
        getDocument()
                .addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        update(e);
                    }

                    public void removeUpdate(DocumentEvent e) {
                        update(e);
                    }

                    public void insertUpdate(DocumentEvent e) {
                        update(e);
                    }

                    private void update(DocumentEvent e) {
                        valueProperty.setValue(getText());
                    }

                });
        valueProperty.addListener(e
                -> {
            if (!valueProperty.getValue().equals(getText())) {
                setText(valueProperty.getValue());
            }

        });
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

    public void setBoundsFromLeft(JComponent jComponent, int additionalY) {
        setBounds(jComponent.getX() + jComponent.getWidth() + SwingUtils.MARGIN,
                jComponent.getY() + additionalY);
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

    public TTime asTTime() {
        return new TTime(valueProperty.getValue());
    }

}
