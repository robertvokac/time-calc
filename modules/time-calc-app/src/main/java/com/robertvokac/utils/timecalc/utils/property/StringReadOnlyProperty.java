package com.robertvokac.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class StringReadOnlyProperty extends ReadOnlyProperty<String> {

    public StringReadOnlyProperty(String name, String valueIn) {
        super(name, valueIn);
    }

    public StringReadOnlyProperty(Property<String> property) {
        super(property);
    }
}
