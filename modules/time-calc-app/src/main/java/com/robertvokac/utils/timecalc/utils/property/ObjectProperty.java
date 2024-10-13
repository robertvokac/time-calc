package com.robertvokac.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ObjectProperty extends Property<Object> {

    public ObjectProperty(String name, Object valueIn) {
        super(name, valueIn);
    }

    public ObjectProperty() {
        this("", null);
    }
}
