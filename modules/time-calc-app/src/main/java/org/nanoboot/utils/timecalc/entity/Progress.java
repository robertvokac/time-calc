package org.nanoboot.utils.timecalc.entity;

import org.nanoboot.utils.timecalc.app.TimeCalcException;

/**
 * @author pc00289
 * @since 21.03.2024
 */
public class Progress {
    private final double[] array = new double[6];
    public void set(WidgetType type, double value) {
        array[WidgetType.DAY.getIndex()] = value;
    }
    public double get(WidgetType type) {
        return array[WidgetType.DAY.getIndex()];
    }
}
