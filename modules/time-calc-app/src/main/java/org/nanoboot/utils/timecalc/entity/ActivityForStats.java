package org.nanoboot.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActivityForStats extends Activity {

    private int todaySpentHours;
    private int todaySpentMinutes;
    private int todayRemainsHours;
    private int todayRemainsMinutes;

    public ActivityForStats(String id, int year, int month, int day,
            String name,
            String comment, String ticket, int spentHours, int spentMinutes,
            String flags, int sortkey,
            int todaySpentHours,
            int todaySpentMinutes,
            int todayRemainsHours,
            int todayRemainsMinutes) {
        super(id, year, month, day, name, comment, ticket, spentHours,
                spentMinutes,
                flags, sortkey);
        this.todaySpentHours = todaySpentHours;
        this.todaySpentMinutes = todaySpentMinutes;
        this.todayRemainsHours = todayRemainsHours;
        this.todayRemainsMinutes = todayRemainsMinutes;
    }

    public List<ActivityForStats> createList(List<Activity> list) {
        return null;//todo
    }
}
