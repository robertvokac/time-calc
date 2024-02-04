package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class StringReadOnlyProperty extends ReadOnlyProperty<String> {

    public StringReadOnlyProperty(String valueIn) {
        super(valueIn);
    }
    public StringReadOnlyProperty(Property<String> property) {
        super(property);
    }
}
