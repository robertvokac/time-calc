package org.nanoboot.utils.timecalc.utils.property;

import lombok.Getter;
import org.nanoboot.utils.timecalc.app.TimeCalcException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class Property<T> {

    @Getter
    private final String name;
    private final List<InvalidationListener> invalidationListeners
            = new ArrayList<>();
    private final List<ChangeListener<T>> changeListeners
            = new ArrayList<ChangeListener<T>>();
    private boolean valid = true;
    private T value;
    private Property<T> boundToProperty = null;
    private ChangeListener<T> boundChangeListener = null;

    public Property(String name, T valueIn) {
        this.name = name;
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
        if (!isBound()) {
            throw new TimeCalcException("No bound property");
        }
        this.value = boundToProperty.value;
        this.boundToProperty.removeListener(this.boundChangeListener);
        this.boundChangeListener = null;
        this.boundToProperty = null;
    }

    public void bindTo(Property<T> anotherProperty) {
        if (this == anotherProperty) {
            new RuntimeException().printStackTrace();
            throw new TimeCalcException("Cannot bind to self: " + getName());
        }
        fireValueChangedEvent(value);
        this.boundToProperty = anotherProperty;
        this.boundChangeListener
                = (Property<T> p, T oldValue, T newValue) -> {
                    this.markInvalid();
                    this.fireValueChangedEvent(oldValue);
                    //System.out.println("bindTo markInvalid " + p.getName() + " " + p.getValue());
                };
        this.boundToProperty
                .addListener(boundChangeListener);

    }

    public T getValue() {
        return isBound() ? this.boundToProperty.getValue() : value;
    }

    public void setValue(T newValue) {
        if (isBound()) {
            this.boundToProperty.setValue(newValue);
        } else {
            T oldValue = value;
            this.value = newValue;
            if (!oldValue.equals(newValue)) {
                fireValueChangedEvent(oldValue);
                markInvalid();
            }
        }
    }

    public boolean isValid() {
        return isBound() ? this.boundToProperty.isValid() : valid;
    }

    protected void markInvalid() {
        //        System.out.println(name + " is invalid now");
        valid = false;
        for (InvalidationListener listener : invalidationListeners) {
            listener.invalidated(this);
        }
    }

    public void fireValueChangedEvent(T oldValue) {
//        if (isBound()) {
//            System.out.println("Property " + getName() + " calling boundProperty fire event for: " + this.boundToProperty.getName());
//            this.boundToProperty.fireValueChangedEvent(oldValue);
//            return;
//        }
        //        System.out.println(name + " was changed");
        for (ChangeListener listener : changeListeners) {
            listener.changed(this, oldValue, value);
        }

    }

    public void addListener(InvalidationListener listener) {
        this.invalidationListeners.add(listener);
    }

    public void addListener(ChangeListener<T> listener) {
        this.changeListeners.add(listener);
    }
    public void removeListener(InvalidationListener listener) {
        this.invalidationListeners.remove(listener);
    }
    public void removeListener(ChangeListener<T> listener) {
        this.changeListeners.remove(listener);
    }

    public String toString() {
        return String.valueOf(getValue());
    }

}
