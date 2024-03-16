package org.nanoboot.utils.timecalc.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Activity implements Comparable<Activity> {

    private static final String SUBJECT_FIELD_SEPARATOR = " : ";

    private String id;
    private int year;
    private int month;
    private int day;
    private String name;
    private String comment;
    private String ticket;
    private int spentHours;
    private int spentMinutes;
    private String flags;
    private String nextActivityId;

    public String createSubject() {
        return ticket + SUBJECT_FIELD_SEPARATOR + name;
    }

    public String createTotalComment() {
        return ticket + SUBJECT_FIELD_SEPARATOR + year + "-" + month + "-" + day
                + SUBJECT_FIELD_SEPARATOR + ((spentHours + spentMinutes / 60d)
                + "h") + SUBJECT_FIELD_SEPARATOR
                + comment;
    }
    public Set<String> flagsAsSet() {
        Set<String> set = new HashSet<>();
        for(String flag:flags.split(":")) {
            set.add(flag);
        }
        return set;
    }
    public void addFlag(String flag) {
        Set<String> set = flagsAsSet();
        set.add(flag);
        this.flags = set.stream().collect(Collectors.joining(":"));
    }
    public void removeFlag(String flag) {
        Set<String> set = flagsAsSet();
        set.remove(flag);
        this.flags = set.stream().collect(Collectors.joining(":"));
    }

    @Override
    public int compareTo(Activity o) {

        int result = Integer.valueOf(year).compareTo(Integer.valueOf(o.year));
        if(result != 0) {
            return result;
        }
        result = Integer.valueOf(month).compareTo(Integer.valueOf(o.month));
        if(result != 0) {
            return result;
        }
        result = Integer.valueOf(day).compareTo(Integer.valueOf(o.day));
        if(result != 0) {
            return result;
        }
        if(this.nextActivityId != null && this.nextActivityId.equals(o.getId())) {
            return -1;
        }
        if(o.nextActivityId != null && o.nextActivityId.equals(o.getId())) {
            return 1;
        }
        return 0;
    }
}
