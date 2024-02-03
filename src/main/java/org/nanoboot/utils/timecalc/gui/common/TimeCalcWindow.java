package org.nanoboot.utils.timecalc.gui.common;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.HeadlessException;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class TimeCalcWindow extends JFrame {
    public TimeCalcWindow() throws HeadlessException {
        setFocusable(true);
    }

    public Component[] addAll(Component... comp) {
        for(Component c:comp) {
            add(c);
        }
        return comp;
    }

}
