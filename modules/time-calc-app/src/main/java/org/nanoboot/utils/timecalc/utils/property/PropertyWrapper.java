package org.nanoboot.utils.timecalc.utils.property;

import org.nanoboot.utils.timecalc.app.TimeCalcException;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public class PropertyWrapper<T> {

    private Property<T> innerProperty;

    public final void unBound() {
        throw new TimeCalcException(
                "This is a write only property. Unbounding is forbiden.");
    }

    public final void bindTo(Property<T> anotherProperty) {
        throw new TimeCalcException(
                "This is a write only property. Bounding to another property is forbiden.");
    }

}
