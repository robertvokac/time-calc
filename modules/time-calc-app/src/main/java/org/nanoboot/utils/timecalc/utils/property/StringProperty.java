package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class StringProperty extends Property<String> {

    public StringProperty(String name, String valueIn) {
        super(name, valueIn);
    }

    public StringProperty(String name) {
        this(name, "");
    }

    public StringProperty() {
        this("");
    }
}
