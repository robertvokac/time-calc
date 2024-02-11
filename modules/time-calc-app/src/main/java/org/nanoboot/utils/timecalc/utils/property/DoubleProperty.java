package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class DoubleProperty extends Property<Double> {

    public DoubleProperty(String name, Double valueIn) {
        super(name, valueIn);
    }

    public DoubleProperty(String name) {
        this(name, 0d);
    }
}
