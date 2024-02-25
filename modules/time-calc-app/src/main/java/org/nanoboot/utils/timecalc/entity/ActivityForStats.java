package org.nanoboot.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityForStats extends Activity {

    private int todaySpentHours;
    private int todaySpentMinutes;
    private int todayRemainsHours;
    private int todayRemainsMinutes;

    public ActivityForStats() {
    }
}
