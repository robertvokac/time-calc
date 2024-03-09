package org.nanoboot.utils.timecalc.persistence.api;

import org.nanoboot.utils.timecalc.entity.WorkingDay;

import java.util.List;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface WorkingDayRepositoryApi {
    void create(WorkingDay workingDay);
    List<WorkingDay> list(int year, int month);
    void update(WorkingDay workingDay);
    WorkingDay read(int year, int month, int day);

}
