package org.nanoboot.utils.timecalc.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
public class Activity {

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

    public String createSubject() {
        return ticket + SUBJECT_FIELD_SEPARATOR + name;
    }

    public String createBugzillaComment() {
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
    

}
