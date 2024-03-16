package org.nanoboot.utils.timecalc.swing.common;

import lombok.Getter;
import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.utils.common.TTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.function.Consumer;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityPanel extends JPanel implements Comparable<ActivityPanel> {

    @Getter
    private final DayPanel dayPanel;

    class TTextFieldForActivityPanel extends TTextField {

        public TTextFieldForActivityPanel(String s, String name, Consumer<String> consumer, boolean sort) {
            super(s);
            setEditable(false);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            setCaretPosition(0);

            if(consumer != null) {
                addMouseListener((MouseClickedListener) e -> {
                    String result = (String) JOptionPane.showInputDialog(
                            null,
                            "Select new " + name,
                            "New " + name,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            getText()
                    );
                    if (result != null) {
                        consumer.accept(result);
                        activityRepository.update(activity);
                        setText(result);
                        if(sort) {
                            dayPanel.sortActivityPanels();
                        }
                        setCaretPosition(0);
                    }
                });
            }
        }

        public TTextFieldForActivityPanel(String s, String name) {
            this(s, name, null, false);
        }
    }
    public static final Dimension PREFERRED_SIZE = new Dimension(200, 40);
    public static final Dimension PREFERRED_SIZE1 = new Dimension(80, 40);
    public static final Dimension PREFERRED_SIZE3 = new Dimension(60, 40);
    public static final Dimension PREFERRED_SIZE4 = new Dimension(40, 40);
    public static final Dimension PREFERRED_SIZE2 = new Dimension(100, 40);
    private final ActivityRepositoryApi activityRepository;
    @Getter
    private final Activity activity;

    private TTextField sortkey = new TTextFieldForActivityPanel("1", "sortkey", (r)->getActivity().setSortkey(
            Integer.parseInt(r)), true);
    private TTextField name = new TTextFieldForActivityPanel("", "name");
    private TTextField comment = new TTextFieldForActivityPanel("", "comment");
    private TTextField ticket = new TTextFieldForActivityPanel("", "ticket");
    private TTextField spentTime = new TTextFieldForActivityPanel("00:00", "spentTime");

    private TTextField flags = new TTextFieldForActivityPanel("Flags", "flags");
    private TTextField subject = new TTextFieldForActivityPanel("", "subject");
    private TTextField totalComment = new TTextFieldForActivityPanel("", "totalComment");
    public TTextField today = new TTextFieldForActivityPanel("00:00", "today");
    public TTextField remains = new TTextFieldForActivityPanel("00:00", "remains");
    @Getter
    private boolean deleted;

    public ActivityPanel(ActivityRepositoryApi activityRepository,
            Activity activity, DayPanel dayPanel) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.activity = activity;

        this.dayPanel = dayPanel;
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
        name.setHorizontalAlignment(JTextField.LEFT);
        comment.setHorizontalAlignment(JTextField.LEFT);
        ticket.setHorizontalAlignment(JTextField.LEFT);

        JButton copyButton = new SmallTButton("Copy");
        JButton deleteButton = new SmallTButton("Delete");
        JButton subjectButton = new SmallTButton("Sub");
        JButton totalCommentButton = new SmallTButton("TotCom");
        add(copyButton);
        add(deleteButton);
        add(subjectButton);
        add(totalCommentButton);

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

        copyButton.setPreferredSize(PREFERRED_SIZE4);
        deleteButton.setPreferredSize(PREFERRED_SIZE4);
        subjectButton.setPreferredSize(PREFERRED_SIZE4);
        totalCommentButton.setPreferredSize(PREFERRED_SIZE3);
        this.setPreferredSize(new Dimension(getWidth(), 40));

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
        sortkey.setText(String.valueOf(activity.getSortkey()));
        name.setText(activity.getName());
        comment.setText(activity.getComment());
        ticket.setText(activity.getTicket());
        spentTime.setText(activity.getSpentTimeAsString());
        flags.setText(activity.getFlags());
        subject.setText(activity.createSubject());
        totalComment.setText(activity.createTotalComment());

        name.setFont(SwingUtils.VERY_SMALL_FONT);
        comment.setFont(SwingUtils.VERY_SMALL_FONT);
        ticket.setFont(SwingUtils.VERY_SMALL_FONT);
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
            this.setVisible(false);
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
