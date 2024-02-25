package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import java.awt.Color;

/**
 * @author Robert
 * @since 26.02.2024
 */
public class SwingUtils {
    private SwingUtils() {
        //Not meant to be instantiated.
    }
    public static final int MARGIN = 10;
    public static final Color
            CLOSE_BUTTON_BACKGROUND_COLOR = new Color(127,127,127);
    public static final Color getColorFromString(String s) {
        if(s.isEmpty()) {
            System.out.println("error: empty string for color");
            return Color.ORANGE;
        }
        String[] colorAsStringArray = s.split(",");
        int red =  Integer.valueOf(colorAsStringArray[0]);
        int green =  Integer.valueOf(colorAsStringArray[1]);
        int blue =  Integer.valueOf(colorAsStringArray[2]);
        return new Color(red, green, blue);
    }
}
