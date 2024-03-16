package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityHeader extends JPanel {
    private static final Font FONT = new Font("sans", Font.BOLD, 12);
    private TTextField name = new TTextField("Name");
    private TTextField comment = new TTextField("Comment");
    private TTextField ticket = new TTextField("Ticket");
    private TTextField spentTime = new TTextField("Spent time");

    private TTextField flags = new TTextField("Flags");
    private TTextField subject = new TTextField("Subject");
    private TTextField totalComment = new TTextField("Total comment");
    private TTextField today = new TTextField("Today");
    private TTextField remains = new TTextField("Remains");

    public ActivityHeader() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        add(name);
        add(comment);
        add(ticket);
        add(spentTime);

        add(flags);
        add(subject);
        add(totalComment);
        add(today);
        add(remains);

        name.setPreferredSize(new Dimension(200, 40));
        comment.setPreferredSize(new Dimension(200, 40));
        ticket.setPreferredSize(new Dimension(80, 40));
        spentTime.setPreferredSize(new Dimension(80, 40));

        flags.setPreferredSize(new Dimension(100, 40));
        subject.setPreferredSize(new Dimension(100, 40));
        totalComment.setPreferredSize(new Dimension(100, 40));
        today.setPreferredSize(new Dimension(80, 40));
        remains.setPreferredSize(new Dimension(80, 40));

        name.setEditable(false);
        comment.setEditable(false);
        ticket.setEditable(false);
        spentTime.setEditable(false);

        flags.setEditable(false);
        subject.setEditable(false);
        totalComment.setEditable(false);
        today.setEditable(false);
        remains.setEditable(false);

        name.setFont(FONT);
        comment.setFont(FONT);
        ticket.setFont(FONT);
        spentTime.setFont(FONT);

        flags.setFont(FONT);
        subject.setFont(FONT);
        totalComment.setFont(FONT);
        today.setFont(FONT);
        remains.setFont(FONT);

        name.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        comment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ticket.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        spentTime.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        flags.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        subject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        totalComment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        today.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        remains.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        setAlignmentX(LEFT_ALIGNMENT);

    }
}
