package org.nanoboot.utils.timecalc.entity;

import org.nanoboot.utils.timecalc.app.TimeCalcException;

/**
 * @author pc00289
 * @since 21.03.2024
 */
public enum WidgetType {
    MINUTE, HOUR, DAY, WEEK, MONTH, YEAR, LIFE, PRESENTATION;
    public int getIndex() {
        int i = 0;
        for(WidgetType wt:WidgetType.values()) {
            if(wt == this) {
                return i;
            }
            i++;
        }
        throw new TimeCalcException("This widget type is not supported: " + this);
    }
}
