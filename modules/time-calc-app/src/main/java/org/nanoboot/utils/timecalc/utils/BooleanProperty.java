package org.nanoboot.utils.timecalc.utils;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class BooleanProperty extends Property<Boolean> {

    public BooleanProperty() {
        super(Boolean.FALSE);
    }
    public BooleanProperty(boolean valueIn) {
        this(Boolean.valueOf(valueIn));
    }
    public BooleanProperty(Boolean valueIn) {
        super(valueIn);
    }

    public boolean isEnabled() {
        return getValue();
    }
    public boolean isDisabled() {
        return !getValue();
    }


    public void flip() {
        setValue(!getValue());
    }
    public void inverse() {
        flip();
    }
    public void enable() {
        setValue(true);
    }
    public void disable() {
        setValue(false);
    }
}
