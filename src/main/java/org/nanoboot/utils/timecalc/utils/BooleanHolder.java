package org.nanoboot.utils.timecalc.utils;

/**
 * @author pc00289
 * @since 16.02.2024
 */
public class BooleanHolder {
    private boolean b;

    public BooleanHolder(boolean b) {
        this.b = b;
    }

    public BooleanHolder() {
        this(false);
    }

    public void set(boolean b) {
        this.b = b;
    }

    public boolean get() {
        return b;
    }

    public void flip() {
        this.b = !b;
    }
}
