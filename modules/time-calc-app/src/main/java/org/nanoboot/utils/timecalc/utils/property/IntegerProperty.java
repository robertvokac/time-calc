package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert
 * @since 16.02.2024
 */
public class IntegerProperty extends Property<Integer> {

    public IntegerProperty(String name, Integer valueIn) {
        super(name, valueIn);
    }

    public IntegerProperty(String name) {
        this(name, 0);
    }
}
