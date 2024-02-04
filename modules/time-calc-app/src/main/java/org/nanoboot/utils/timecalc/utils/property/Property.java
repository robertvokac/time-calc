package org.nanoboot.utils.timecalc.utils.property;

import lombok.Getter;
import lombok.Setter;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.utils.property.ReadOnlyProperty;
import org.nanoboot.utils.timecalc.utils.property.WriteOnlyProperty;

/**
 * @author Robert
 * @since 23.02.2024
 */
public class Property <T>{
    private T value;
    private Property<T> boundToProperty = null;
    public Property(T valueIn) {
        this.value = valueIn;
    }
    public ReadOnlyProperty<T> asReadOnlyProperty() {
        return new ReadOnlyProperty<>(this);
    }
    public WriteOnlyProperty<T> asWriteOnlyProperty() {
        return new WriteOnlyProperty<>(this);
    }
    public boolean isBound() {
        return boundToProperty != null;
    }
    public void unBound() {
        if(!isBound()) {
            throw new TimeCalcException("No bound property");
        }
        this.value = boundToProperty.value;
        this.boundToProperty = null;
    }
    public void bindTo(Property<T> anotherProperty) {
        this.boundToProperty = anotherProperty;
    }
    public T getValue() {
        return isBound() ? this.boundToProperty.getValue() : value;
    }

    public void setValue(T value) {
        if(isBound()) {
            throw new TimeCalcException("Cannot set value, because property is bound.");
        } else {
            this.value = value;
        }
    }
}
