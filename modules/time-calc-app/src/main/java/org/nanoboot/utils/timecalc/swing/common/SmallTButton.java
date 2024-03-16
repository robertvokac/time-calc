package org.nanoboot.utils.timecalc.swing.common;

import java.awt.Insets;

/**
 * @author Robert
 * @since 11.03.2024
 */
public class SmallTButton extends TButton {

    private static final Insets INSETS = new Insets(1, 1, 1, 1);

    public SmallTButton(String s) {
        super(s, 15, 15);
        //setFont(SwingUtils.SMALL_FONT);
        setMargin(INSETS);
    }

    public SmallTButton(char character) {
        this(String.valueOf(character));
    }
}
