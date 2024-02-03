package org.nanoboot.utils.timecalc.gui.progress;

import org.nanoboot.utils.timecalc.utils.Utils;

import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Robert
 * @since 21.02.2024
 */
public class WalkingHumanProgressAsciiArt extends JTextPane {
    public WalkingHumanProgressAsciiArt() {
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Utils.highlighted.flip();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                putClientProperty("mouseEntered", "true");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                putClientProperty("mouseEntered", "false");
            }
        });
    }
}
