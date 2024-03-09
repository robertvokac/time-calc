package org.nanoboot.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class IntegerProperty extends Property<Integer> {

    public IntegerProperty(String name, Integer valueIn) {
        super(name, valueIn);
    }

    public IntegerProperty(String name) {
        this(name, 0);
    }

    public void increment() {this.setValue(getValue() + 1);}
    public void decrement() {this.setValue(getValue() - 1);}
}
