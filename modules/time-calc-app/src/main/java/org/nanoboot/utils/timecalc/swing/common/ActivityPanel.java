package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityPanel extends JPanel {
    private final ActivityRepositoryApi activityRepository;
    private final Activity activity;
    private TTextField name = new TTextField("");
    private TTextField comment = new TTextField("");
    private TTextField ticket = new TTextField("");
    private TTextField spentHours = new TTextField("");
    private TTextField spentMinutes = new TTextField("");
    private TTextField flags = new TTextField("");
    private String nextActivityId;

    public ActivityPanel(ActivityRepositoryApi activityRepository, Activity activity) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.activity = activity;
        add(name);
        add(comment);
        add(ticket);
        add(spentHours);
        add(spentMinutes);
        add(flags);
        name.setPreferredSize(new Dimension(400, 40));
        comment.setPreferredSize(new Dimension(400, 40));
        ticket.setPreferredSize(new Dimension(80, 40));
        spentHours.setPreferredSize(new Dimension(25, 40));
        spentMinutes.setPreferredSize(new Dimension(25, 40));
        flags.setPreferredSize(new Dimension(100, 40));
this.setPreferredSize(new Dimension(getWidth(), 40));
        name.setEditable(false);
        comment.setEditable(false);
        ticket.setEditable(false);
        spentHours.setEditable(false);
        spentMinutes.setEditable(false);
        flags.setEditable(false);

        name.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        comment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ticket.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        spentHours.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        spentMinutes.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        flags.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        name.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String result = (String) JOptionPane.showInputDialog(
                        null,
                        "Select new name",
                        "New name",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        name.getText()
                );
                if(result != null) {
                    activity.setName(result);
                    activityRepository.update(activity);
                    name.setText(result);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        name.setText(activity.getName());
        comment.setText(activity.getComment());
        ticket.setText(activity.getTicket());
        spentHours.setText(String.valueOf(activity.getSpentHours()));
        spentMinutes.setText(String.valueOf(activity.getSpentMinutes()));
        flags.setText(activity.getFlags());
        this.activityRepository = activityRepository;
this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        setAlignmentX(LEFT_ALIGNMENT);

    }
}
