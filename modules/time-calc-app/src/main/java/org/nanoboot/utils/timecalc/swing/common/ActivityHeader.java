package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.Main;
import org.nanoboot.utils.timecalc.swing.controls.TTextField;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityHeader extends JPanel {
    private static final Font FONT = new Font("sans", Font.BOLD, 12);

    public static final Dimension PREFERRED_SIZE = new Dimension(200, 40);
    public static final Dimension PREFERRED_SIZE1 = new Dimension(80, 40);
    public static final Dimension PREFERRED_SIZE3 = new Dimension(60, 40);
    public static final Dimension PREFERRED_SIZE4 = new Dimension(40, 40);
    public static final Dimension PREFERRED_SIZE2 = new Dimension(100, 40);

    private TTextField sortkey = new TTextField("Sortkey");
    private TTextField name = new TTextField("Name");
    private TTextField comment = new TTextField("Comment");
    private TTextField ticket = new TTextField("Ticket");
    private TTextField spentTime = new TTextField("Spent time");

    private TTextField flags = new TTextField("Flags");
    private TTextField subject = new TTextField("Subject");
    private TTextField totalComment = new TTextField("Total comment");
    private TTextField done = new TTextField("Done");
    private TTextField todo = new TTextField("Todo");

    public ActivityHeader() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        if(Main.ACTIVITIES_WINDOW_SHOW_SORTKEY) add(sortkey);
        add(name);
        add(comment);
        add(ticket);
        add(spentTime);

        add(flags);
        add(subject);
        add(totalComment);
        add(done);
        add(todo);

        sortkey.setPreferredSize(PREFERRED_SIZE1);
        name.setPreferredSize(PREFERRED_SIZE);
        comment.setPreferredSize(PREFERRED_SIZE);
        ticket.setPreferredSize(PREFERRED_SIZE1);
        spentTime.setPreferredSize(PREFERRED_SIZE1);

        flags.setPreferredSize(PREFERRED_SIZE2);
        subject.setPreferredSize(PREFERRED_SIZE2);
        totalComment.setPreferredSize(PREFERRED_SIZE2);
        done.setPreferredSize(PREFERRED_SIZE3);
        todo.setPreferredSize(PREFERRED_SIZE3);

        sortkey.setEditable(false);
        name.setEditable(false);
        comment.setEditable(false);
        ticket.setEditable(false);
        spentTime.setEditable(false);

        flags.setEditable(false);
        subject.setEditable(false);
        totalComment.setEditable(false);
        done.setEditable(false);
        todo.setEditable(false);

        sortkey.setFont(FONT);
        name.setFont(FONT);
        comment.setFont(FONT);
        ticket.setFont(FONT);
        spentTime.setFont(FONT);

        flags.setFont(FONT);
        subject.setFont(FONT);
        totalComment.setFont(FONT);
        done.setFont(FONT);
        todo.setFont(FONT);

        sortkey.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        comment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ticket.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        spentTime.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        flags.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        subject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        totalComment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        done.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        todo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        setAlignmentX(LEFT_ALIGNMENT);

        sortkey.setVisible(false);
    }
}
