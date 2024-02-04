package org.nanoboot.utils.timecalc.gui.common;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.HeadlessException;

/**
 * @author pc00289
 * @since 21.02.2024
 */
public class TimeCalcWindow extends JFrame {
    public TimeCalcWindow() throws HeadlessException {
        setFocusable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public Component[] addAll(Component... comp) {
        for(Component c:comp) {
            add(c);
        }
        return comp;
    }

}
