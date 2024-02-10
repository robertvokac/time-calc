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

}
