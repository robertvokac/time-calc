package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityPanel extends JPanel {
    private final ActivityRepositoryApi activityRepository;
    private String id;
    private int year;
    private int month;
    private int day;
    private TTextField name = new TTextField("");
    private TTextField comment = new TTextField("");
    private TTextField ticket = new TTextField("");
    private TTextField spentHours = new TTextField("");
    private TTextField spentMinutes = new TTextField("");
    private TTextField flags = new TTextField("");
    private String nextActivityId;

    public ActivityPanel(ActivityRepositoryApi activityRepository, Activity activity) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        add(name);
        add(comment);
        add(ticket);
        add(spentHours);
        add(spentMinutes);
        add(flags);
        name.setPreferredSize(new Dimension(100, 40));
        comment.setPreferredSize(new Dimension(100, 40));
        ticket.setPreferredSize(new Dimension(100, 40));
        spentHours.setPreferredSize(new Dimension(100, 40));
        spentMinutes.setPreferredSize(new Dimension(100, 40));
        flags.setPreferredSize(new Dimension(100, 40));

        name.setText(activity.getName());
        comment.setText(activity.getComment());
        ticket.setText(activity.getTicket());
        spentHours.setText(String.valueOf(activity.getSpentHours()));
        spentMinutes.setText(String.valueOf(activity.getSpentMinutes()));
        flags.setText(activity.getFlags());
        this.activityRepository = activityRepository;
        this.setBackground(Color.BLUE);
        this.setBorder(BorderFactory.createLineBorder(Color.green));
        setAlignmentX(LEFT_ALIGNMENT);

    }
}
