package org.nanoboot.utils.timecalc.utils.property;

import org.nanoboot.utils.timecalc.app.TimeCalcException;

/**
 * @author Robert
 * @since 23.02.2024
 */
public class WriteOnlyProperty<T> extends Property<T> {
    private Property<T> innerProperty;
    public WriteOnlyProperty(String name, T valueIn) {
        super(name, valueIn);
        throw new TimeCalcException("This constructor is forbidden in class " + getClass().getName() + ".");
    }
    public WriteOnlyProperty(Property<T> property) {
        super(property.getName(), null);
        this.innerProperty = property;
    }
    public final void setValue(T valueIn) {
        this.innerProperty.setValue(valueIn);
    }

    public final T getValue(T valueIn) {
        throw new TimeCalcException("This is a write only property. Current value cannot be read.");
    }
    public final void unBound() {
        throw new TimeCalcException("This is a write only property. Unbounding is forbiden.");
    }
    public final void bindTo(Property<T> anotherProperty) {
        throw new TimeCalcException("This is a write only property. Bounding to another property is forbiden.");
    }


}
