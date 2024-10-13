package com.robertvokac.utils.timecalc.swing.controls;

import com.robertvokac.utils.timecalc.utils.property.IntegerProperty;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TWindow extends JFrame {

    public IntegerProperty widthProperty = new IntegerProperty("widthProperty");
    public IntegerProperty heightProperty = new IntegerProperty("heightProperty");
    public TWindow() throws HeadlessException {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                widthProperty.setValue(getWidth());
                heightProperty.setValue(getHeight());
            }
        });
        widthProperty.addListener(e -> {
            if (widthProperty.getValue() > 100 /*&& widthProperty.getValue() % 10 == 0*/) {
                setSize(widthProperty.getValue(), getHeight());
            }
        });
        heightProperty.addListener(e -> {
            if (heightProperty.getValue() > 100 /*&& widthProperty.getValue() % 10 == 0*/) {
                setSize(getWidth(), heightProperty.getValue());
            }
        });
    }

    public Component[] addAll(Component... comp) {
        for (Component c : comp) {
            add(c);
        }
        return comp;
    }

}
