package org.nanoboot.utils.timecalc.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

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
    private int sortkey;

    public String createSubject() {
        return ticket + SUBJECT_FIELD_SEPARATOR + name;
    }

    public String createTotalComment() {
        return ticket + SUBJECT_FIELD_SEPARATOR + year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day
                + SUBJECT_FIELD_SEPARATOR + (
                       NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(spentHours + spentMinutes / 60d)
                       + "h") + SUBJECT_FIELD_SEPARATOR
                + comment;
    }
    public Set<String> flagsAsSet() {
        Set<String> set = new HashSet<>();
        for(String flag:flags.split(":")) {
            if(flag.isEmpty()) {
                //nothing to do
                continue;
            }
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

        return Integer.valueOf(sortkey).compareTo(Integer.valueOf(o.sortkey));
    }

    public String getSpentTimeAsString() {
        return (getSpentHours() < 10 ? "0" : "") +  getSpentHours() + ":" + (getSpentMinutes() < 10 ? "0" : "") + getSpentMinutes();
    }
}
