package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class BooleanReadOnlyProperty extends ReadOnlyProperty<Boolean> {

    public BooleanReadOnlyProperty(Boolean valueIn) {
        super(valueIn);
    }
    public BooleanReadOnlyProperty(Property<Boolean> property) {
        super(property);
    }

    public boolean isEnabled() {
        return getValue();
    }
    public boolean isDisabled() {
        return !getValue();
    }

}
