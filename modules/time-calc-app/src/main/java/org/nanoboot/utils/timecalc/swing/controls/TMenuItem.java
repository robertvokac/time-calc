package org.nanoboot.utils.timecalc.swing.controls;

import javax.swing.JMenuItem;

/**
 * @author pc00289
 * @since 21.03.2024
 */
public class TMenuItem extends JMenuItem {
    private static final String ENABLED = " (*)";
    private boolean enabledMenuItem = false;

    public TMenuItem(String text) {
        super(text);
    }

    public void disableMenuItem() {
        if(getText().endsWith(ENABLED)) {
            setText(getText().substring(0, getText().length() - ENABLED.length()));
        }
        enabledMenuItem = false;
    }
    public void enableMenuItem() {
        if(!getText().endsWith(ENABLED)) {
            setText(getText() + ENABLED);
        }
        enabledMenuItem = true;
    }
    public void setEnabledMenuItem(boolean value) {

        if(value) {enableMenuItem();} else {
            disableMenuItem();
        }

        enabledMenuItem = value;
    }
    public void flip() {
        setEnabledMenuItem(!enabledMenuItem);
    }
}
