package org.nanoboot.utils.timecalc.persistence.api;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.entity.Activity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface ActivityRepositoryApi {

    default double getProgressForDay(int year, int month, int day, TimeCalcConfiguration timeCalcConfiguration) {
        List<Activity> list = list(year, month, day);
        double done = 0d;
        double todo = 8d;

        String neededFlags = timeCalcConfiguration.activityNeededFlagsProperty.getValue();
        System.out.println("neededFlags=" + neededFlags);
        neededFlags.replace(",", ":");
        String[] neededFlagsArray = neededFlags.split(":");
        Set<String> neededFlagsSet = Arrays.stream(neededFlagsArray).filter(f -> !f.isEmpty()).collect(
                Collectors.toSet());

        loopName:
        for(Activity a:list) {
            Set<String> flags = a.flagsAsSet();

            if(!neededFlagsSet.isEmpty()) {
                for(String f:neededFlagsSet) {
                    if(!flags.contains(f)) {
                        continue loopName;
                    }
                }
            }
            double now = a.getSpentHours() + a.getSpentMinutes() / 60d;
            done = done + now;
            todo = todo - now;
        }
        double progress = done / 8d;
        return progress;
    }
    void create(Activity activity);

    List<Activity> list(int year, int month, int day);

    List<Activity> list(String ticket);

    void update(Activity activity);

    Activity read(String id);
    
    boolean delete(String id);
    
    List<String> getYears();

    void putToClipboard(Activity activity);

    Activity getFromClipboard();

    default int getSortkeySpace() {
        return 1000;
    }

    int getNextSortkey(int year, int month, int day);

}
