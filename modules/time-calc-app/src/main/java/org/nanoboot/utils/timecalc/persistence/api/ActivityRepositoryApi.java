package org.nanoboot.utils.timecalc.persistence.api;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.entity.WorkingDay;

import java.util.List;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface ActivityRepositoryApi {

    void create(Activity activity);

    List<Activity> list(int year, int month, int day);

    void update(Activity activity);

    WorkingDay read(String id);

}
