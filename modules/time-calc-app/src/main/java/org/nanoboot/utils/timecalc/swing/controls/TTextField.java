package org.nanoboot.utils.timecalc.swing.controls;

import lombok.Getter;
import lombok.Setter;
import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.windows.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.ChangeListener;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Insets;

import static org.nanoboot.utils.timecalc.swing.common.SwingUtils.INSETS;

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
    @Getter
    @Setter
    private ChangeListener vetoableChangeListener = null;

    @Getter
    @Setter
    private boolean editingOnlyInDialog = false;
    public TTextField() {
        this("", 0);
    }

    public TTextField(String s) {
        this(s, 0);
    }

    public TTextField(String s, int customWidth) {
        this(s, customWidth, false);
    }

    public TTextField(String s, int customWidth, boolean editingOnlyInDialog) {
        this(s, customWidth, editingOnlyInDialog, null);
    }
    public TTextField(String s, int customWidth, boolean editingOnlyInDialog, ChangeListener vetoableChangeListener) {
        super(s);

        setMargin(INSETS);
        setEditingOnlyInDialog(editingOnlyInDialog);
        setVetoableChangeListener(vetoableChangeListener);
        this.customWidth = customWidth;
        valueProperty.setValue(s);
        new Timer(100, e -> {
            if (editingOnlyInDialog) {
                setEditable(false);
            }
        }).start();
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

        addMouseListener((MouseClickedListener) f -> {
            if(editingOnlyInDialog) {
                String result = (String) JOptionPane.showInputDialog(
                        null,
                        "Select new value",
                        "New value",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        getText()
                );
                if (result != null) {
                    String oldText = getText();
                    boolean vetoed = false;
                    if(vetoableChangeListener != null) {
                        try {
                            vetoableChangeListener
                                    .changed(null, oldText, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e.getMessage());
                            vetoed = true;
                        }
                    }
                    if(!vetoed) {
                        TTime tTime = new TTime(result);
                        result = tTime.toString().substring(0, tTime.isNegative() ? 6 : 5);
                        setText(result);
                        valueProperty.setValue(result);
                    }
                }
            }});

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
    public void setBoundsFromLeftWithAdditionalX(JComponent jComponent, int additionalX) {
        setBounds(jComponent.getX() + jComponent.getWidth() + SwingUtils.MARGIN + additionalX,
                    jComponent.getY() + 0);
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
        if(!autoManageForeground) {
            //nothing to do
            return;
        }
        this.setForeground(originalForeground);
    }
    @Getter @Setter
    private boolean autoManageForeground = true;

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
