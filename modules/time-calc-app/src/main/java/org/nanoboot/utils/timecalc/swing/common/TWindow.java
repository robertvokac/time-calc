package org.nanoboot.utils.timecalc.swing.common;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.HeadlessException;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TWindow extends JFrame {

    public TWindow() throws HeadlessException {

    }

    public Component[] addAll(Component... comp) {
        for (Component c : comp) {
            add(c);
        }
        return comp;
    }

}
