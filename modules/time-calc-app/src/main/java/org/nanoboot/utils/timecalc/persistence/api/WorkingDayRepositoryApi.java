package org.nanoboot.utils.timecalc.persistence.api;

import java.util.Calendar;
import org.nanoboot.utils.timecalc.entity.WorkingDay;

import java.util.List;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface WorkingDayRepositoryApi {

    void create(WorkingDay workingDay);

    default List<WorkingDay> list(int year, int month) {
        return list(year, month, 0);
    }
    List<WorkingDay> list(int year, int month, int day);

    void update(WorkingDay workingDay);

    WorkingDay read(int year, int month, int day);

    default WorkingDay read(Calendar cal) {
        return read(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
        );
    }
    public List<String> getYears();
}
