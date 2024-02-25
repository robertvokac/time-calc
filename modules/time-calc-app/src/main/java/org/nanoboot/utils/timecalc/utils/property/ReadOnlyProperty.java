package org.nanoboot.utils.timecalc.utils.property;

import org.nanoboot.utils.timecalc.app.TimeCalcException;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class ReadOnlyProperty<T> extends Property<T> {

    private final Property<T> innerProperty;

    public ReadOnlyProperty(String name, T valueIn) {
        super(name, valueIn);
        throw new TimeCalcException(
                "This constructor is forbidden in class " + getClass().getName()
                + ".");
    }

    public ReadOnlyProperty(Property<T> property) {
        super(property.getName(), null);
        this.innerProperty = property;
        this.innerProperty.addListener((Property p) -> {
            markInvalid();
        });
    }

    public final T getValue() {
        return innerProperty.getValue();
    }

    public final void setValue(T newValue) {
        throw new TimeCalcException(
                "This is a read only property. New value cannot be set.");
    }

    public final void unBound() {
        throw new TimeCalcException(
                "This is a write only property. Unbounding is forbiden.");
    }

    public final void bindTo(Property<T> anotherProperty) {
        throw new TimeCalcException(
                "This is a write only property. Bounding to another property is forbiden.");
    }

    public boolean isValid() {
        return this.innerProperty.isValid();
    }

}
