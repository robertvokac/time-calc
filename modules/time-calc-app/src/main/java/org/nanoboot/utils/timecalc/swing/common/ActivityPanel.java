package org.nanoboot.utils.timecalc.swing.common;

import lombok.Getter;
import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.utils.common.TTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityPanel extends JPanel implements Comparable<ActivityPanel> {

    public static final Dimension PREFERRED_SIZE = new Dimension(200, 40);
    public static final Dimension PREFERRED_SIZE1 = new Dimension(80, 40);
    public static final Dimension PREFERRED_SIZE3 = new Dimension(60, 40);
    public static final Dimension PREFERRED_SIZE4 = new Dimension(40, 40);
    public static final Dimension PREFERRED_SIZE2 = new Dimension(100, 40);
    private final ActivityRepositoryApi activityRepository;
    @Getter
    private final Activity activity;

    private TTextField sortkey = new TTextField("1");
    private TTextField name = new TTextField("");
    private TTextField comment = new TTextField("");
    private TTextField ticket = new TTextField("");
    private TTextField spentTime = new TTextField("00:00");

    private TTextField flags = new TTextField("Flags");
    private TTextField subject = new TTextField("");
    private TTextField totalComment = new TTextField("");
    public TTextField today = new TTextField("00:00");
    public TTextField remains = new TTextField("00:00");
    @Getter
    private boolean deleted;

    public ActivityPanel(ActivityRepositoryApi activityRepository,
            Activity activity, DayPanel dayPanel) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.activity = activity;

        add(sortkey);
        add(name);
        add(comment);
        add(ticket);
        add(spentTime);

        add(flags);
        add(subject);
        add(totalComment);
        add(today);
        add(remains);

//        JButton moveThisButton = new SmallTButton("Move ");
//        JButton moveBeforeButton = new SmallTButton("Here");
        JButton copyButton = new SmallTButton("Copy");
        JButton deleteButton = new SmallTButton("Delete");
        JButton subjectButton = new SmallTButton("Sub");
        JButton totalCommentButton = new SmallTButton("TotCom");
//        add(moveThisButton);
//        add(moveBeforeButton);
        add(copyButton);
        add(deleteButton);
        add(subjectButton);
        add(totalCommentButton);
//        moveThisButton.setFont(SwingUtils.SMALL_FONT);
//        moveBeforeButton.setFont(SwingUtils.SMALL_FONT);
        copyButton.setFont(SwingUtils.SMALL_FONT);
        deleteButton.setFont(SwingUtils.SMALL_FONT);
        subjectButton.setFont(SwingUtils.SMALL_FONT);
        totalCommentButton.setFont(SwingUtils.SMALL_FONT);

        sortkey.setPreferredSize(PREFERRED_SIZE1);
        name.setPreferredSize(PREFERRED_SIZE);
        comment.setPreferredSize(PREFERRED_SIZE);
        ticket.setPreferredSize(PREFERRED_SIZE1);
        spentTime.setPreferredSize(PREFERRED_SIZE1);

        flags.setPreferredSize(PREFERRED_SIZE2);
        subject.setPreferredSize(PREFERRED_SIZE2);
        totalComment.setPreferredSize(PREFERRED_SIZE2);
        today.setPreferredSize(PREFERRED_SIZE3);
        remains.setPreferredSize(PREFERRED_SIZE3);

//        moveThisButton.setPreferredSize(PREFERRED_SIZE4);
//        moveBeforeButton.setPreferredSize(PREFERRED_SIZE4);
        copyButton.setPreferredSize(PREFERRED_SIZE4);
        deleteButton.setPreferredSize(PREFERRED_SIZE4);
        subjectButton.setPreferredSize(PREFERRED_SIZE4);
        totalCommentButton.setPreferredSize(PREFERRED_SIZE3);
        this.setPreferredSize(new Dimension(getWidth(), 40));

        sortkey.setEditable(false);
        name.setEditable(false);
        comment.setEditable(false);
        ticket.setEditable(false);
        spentTime.setEditable(false);

        flags.setEditable(false);
        subject.setEditable(false);
        totalComment.setEditable(false);
        today.setEditable(false);
        remains.setEditable(false);

        sortkey.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        comment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ticket.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        spentTime.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        flags.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        subject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        totalComment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        today.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        remains.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        sortkey.addMouseListener((MouseClickedListener) e -> {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new sortkey",
                    "New sortkey",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    sortkey.getText()
            );
            if (result != null) {
                activity.setSortkey(Integer.valueOf(result));
                activityRepository.update(activity);
                sortkey.setText(result);
                dayPanel.sortActivityPanels();
            }
        });

        name.addMouseListener((MouseClickedListener) e -> {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new name",
                    "New name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    name.getText()
            );
            if (result != null) {
                activity.setName(result);
                activityRepository.update(activity);
                name.setText(result);
                subject.setText(activity.createSubject());
            }
        });
        comment.addMouseListener((MouseClickedListener) e -> {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new comment",
                    "New comment",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    comment.getText()
            );
            if (result != null) {
                activity.setComment(result);
                activityRepository.update(activity);
                comment.setText(result);
                totalComment.setText(activity.createTotalComment());
            }
        });
        ticket.addMouseListener((MouseClickedListener) e -> {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new ticket",
                    "New ticket",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ticket.getText()
            );
            if (result != null) {
                activity.setTicket(result);
                activityRepository.update(activity);
                ticket.setText(result);
                subject.setText(activity.createSubject());
                totalComment.setText(activity.createTotalComment());
            }
        });
        spentTime.addMouseListener((MouseClickedListener) e -> {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new spent time",
                    "New spent time",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    spentTime.getText()
            );
            if (result != null) {
                TTime spentTimeTTime = new TTime(result);
                activity.setSpentHours(spentTimeTTime.getHour());
                activity.setSpentMinutes(spentTimeTTime.getMinute());
                activityRepository.update(activity);
                spentTime.setText(result);
                totalComment.setText(activity.createTotalComment());
                dayPanel.sortActivityPanels();
            }
        });

        flags.addMouseListener((MouseClickedListener) e -> {
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new flags",
                    "New flags",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    flags.getText()
            );
            if (result != null) {
                activity.setFlags(result);
                activityRepository.update(activity);
                flags.setText(result);

            }
        });
        name.setText(activity.getName());
        comment.setText(activity.getComment());
        ticket.setText(activity.getTicket());
        spentTime.setText(activity.getSpentTimeAsString());
        flags.setText(activity.getFlags());
        subject.setText(activity.createSubject());
        totalComment.setText(activity.createTotalComment());
        sortkey.setText(String.valueOf(activity.getSortkey()));
        this.activityRepository = activityRepository;
        //this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
        setAlignmentX(LEFT_ALIGNMENT);
//        moveThisButton.addActionListener(e-> {
//            //dayPanel.switchPositionsForThisActivityAndThePreviousActivity(getActivity());
//            //dayPanel.markActivityAsToBeMoved(getActivity());
//        });
//
//        moveBeforeButton.addActionListener(e-> {
//            //dayPanel.moveMarkedActivityBeforeThisActivity(getActivity());
//        });
        deleteButton.addActionListener(e -> {
            activityRepository.delete(this.activity.getId());
            deleted = true;
        });
        copyButton.addActionListener(e -> {
            activityRepository.putToClipboard(this.activity);
        });
        subjectButton.addActionListener(e-> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(subject.getText()), null);
        });
        totalCommentButton.addActionListener(e-> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(totalComment.getText()), null);
        });
        dayPanel.sortActivityPanels();
    }

    @Override
    public int compareTo(ActivityPanel o) {
        return this.getActivity().compareTo(o.getActivity());
    }
}
