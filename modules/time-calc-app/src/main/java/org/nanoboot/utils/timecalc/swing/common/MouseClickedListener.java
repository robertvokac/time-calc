package org.nanoboot.utils.timecalc.swing.common;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Robert
 * @since 14.03.2024
 */
public interface MouseClickedListener extends MouseListener {

        @Override
        default void mousePressed(MouseEvent e) {

        }

        @Override
        default void mouseReleased(MouseEvent e) {

        }

        @Override
        default void mouseEntered(MouseEvent e) {

        }

        @Override
        default void mouseExited(MouseEvent e) {

        }
}
