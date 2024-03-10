package org.nanoboot.utils.timecalc.swing.common;

import java.awt.Color;
import java.awt.Font;

/**
 * @author Robert Vokac
 * @since 26.02.2024
 */
public class SwingUtils {

    public static final int MARGIN = 10;
    public static final Font SMALL_FONT = new Font("sans", Font.BOLD, 10);
    public static final Font VERY_SMALL_FONT = new Font("sans", Font.PLAIN, 9);
    public static final Font MEDIUM_MONOSPACE_FONT
            = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    private SwingUtils() {
        //Not meant to be instantiated.
    }

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
