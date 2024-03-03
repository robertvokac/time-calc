package org.nanoboot.utils.timecalc.swing.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static org.nanoboot.utils.timecalc.swing.common.Widget.CLOSE_BUTTON_SIDE;

/**
 * @author Robert Vokac
 * @since 26.02.2024
 */
public class SwingUtils {

    public static void paintCloseIcon(Graphics brush, int width) {
                    brush.setColor(SwingUtils.CLOSE_BUTTON_BACKGROUND_COLOR);
            brush.fillOval(width- CLOSE_BUTTON_SIDE - 1, 0 + 1, CLOSE_BUTTON_SIDE, CLOSE_BUTTON_SIDE);
            brush.setColor(Color.LIGHT_GRAY);
            Graphics2D brush2d = (Graphics2D) brush;
            brush2d.setStroke(new BasicStroke(2f));
            brush.drawLine(width - CLOSE_BUTTON_SIDE - 1 + 2, 0 + 1 + 2, width - 0 * CLOSE_BUTTON_SIDE - 1 - 2, 0 + CLOSE_BUTTON_SIDE + 1 - 2);
            brush.drawLine(width - CLOSE_BUTTON_SIDE - 1 + 2, 0 + CLOSE_BUTTON_SIDE + 1 - 2, width - 0 * CLOSE_BUTTON_SIDE - 1 - 2, 0 + 1 + 2);
    }

    private SwingUtils() {
        //Not meant to be instantiated.
    }
    public static final int MARGIN = 10;
    public static final Color CLOSE_BUTTON_BACKGROUND_COLOR = new Color(127, 127, 127);
    
    public static final Font SMALL_FONT = new Font("sans", Font.BOLD, 10);
    public static final Color getColorFromString(String s) {
        if (s.isEmpty()) {
            System.out.println("error: empty string for color");
            return Color.ORANGE;
        }
        String[] colorAsStringArray = s.split(",");
        int red = Integer.valueOf(colorAsStringArray[0]);
        int green = Integer.valueOf(colorAsStringArray[1]);
        int blue = Integer.valueOf(colorAsStringArray[2]);
        return new Color(red, green, blue);
    }
}
