package org.nanoboot.utils.timecalc.swing.progress;

import lombok.Getter;
import org.nanoboot.utils.timecalc.app.TimeCalcException;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author pc00289
 * @since 22.03.2024
 */
public enum DayOfWeekTC {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    @Getter
    private final int number;

    public static DayOfWeekTC forNumber(int number) {
        Optional<DayOfWeekTC> result = Arrays.stream(DayOfWeekTC.values()).filter(d -> d.getNumber() == number).findFirst();
        if(result.isPresent()) {
            return result.get();
        } else {
            throw new TimeCalcException("There is noDayOfWeekTC with number: " + number);
        }
    }
    DayOfWeekTC(int number) {
        this.number = number;
    }
}
