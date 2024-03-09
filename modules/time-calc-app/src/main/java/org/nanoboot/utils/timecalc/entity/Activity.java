package org.nanoboot.utils.timecalc.entity;

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
    private int name;
    private int comment;
    private int ticket;
    private int spentHours;
    private int spentMinutes;
    private boolean jira;
    private boolean bugzilla;

    public String createSubject() {
        return ticket + SUBJECT_FIELD_SEPARATOR + name;
    }

    public String createBugzillaComment() {
        return ticket + SUBJECT_FIELD_SEPARATOR + year + "-" + month + "-" + day
               + SUBJECT_FIELD_SEPARATOR + ((spentHours + spentMinutes / 60d)
                                            + "h") + SUBJECT_FIELD_SEPARATOR
               + comment;
    }

}
