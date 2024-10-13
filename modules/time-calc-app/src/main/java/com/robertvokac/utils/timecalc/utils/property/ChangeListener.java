package com.robertvokac.utils.timecalc.utils.property;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface ChangeListener<T> {

    void changed(Property<T> property, T oldValue, T newValue);
}
