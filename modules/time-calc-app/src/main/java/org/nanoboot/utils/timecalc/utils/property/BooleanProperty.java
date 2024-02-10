package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class BooleanProperty extends Property<Boolean> {

    public BooleanProperty(String name) {
        super(name, Boolean.FALSE);
    }
    public BooleanProperty(String name, boolean valueIn) {
        this(name, Boolean.valueOf(valueIn));
    }
    public BooleanProperty(String name, Boolean valueIn) {
        super(name, valueIn);
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
