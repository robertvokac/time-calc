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

    public ActivityForStats() {
    }

    public List<ActivityForStats> createList(List<Activity> list) {
        return null;//todo
    }
}
