package org.nanoboot.utils.timecalc.utils;

/**
 * @author Robert
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

    public boolean isEnabled() {
        return get();
    }
    public boolean isDisabled() {
        return !get();
    }
    public boolean get() {
        return b;
    }

    public void flip() {
        this.b = !b;
    }
    public void enable() {
        set(true);
    }
    public void disable() {
        set(false);
    }
}
