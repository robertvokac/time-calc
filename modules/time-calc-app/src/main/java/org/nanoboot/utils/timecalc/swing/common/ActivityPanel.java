package org.nanoboot.utils.timecalc.swing.common;

import lombok.AccessLevel;
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
import java.util.function.BiConsumer;

/**
 * @author Robert
 * @since 13.03.2024
 */
public class ActivityPanel extends JPanel implements Comparable<ActivityPanel> {

    @Getter
    private final DayPanel dayPanel;
    private boolean mouseOver;
    class TTextFieldForActivityPanel extends TTextField {

        public TTextFieldForActivityPanel(String s, String name) {
            this(s, name, null);
        }
        public TTextFieldForActivityPanel(String s, String name, BiConsumer<Activity, String> additionalAction) {
            this(s, name, additionalAction, false);
        }
        public TTextFieldForActivityPanel(String s, String name, BiConsumer<Activity, String> additionalAction, boolean sort) {
            super(s);
            setEditable(false);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            setCaretPosition(0);
            //setToolTipText(s);

            if(additionalAction != null) {
                addMouseListener((MouseClickedListener) e -> {
                    String result = (String) JOptionPane.showInputDialog(
                            null,
                            "Select new " + name,
                            "New " + name,
                            name.equals("comment") ? JOptionPane.QUESTION_MESSAGE : JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            getText()
                    );
                    if (result != null) {
                        additionalAction.accept(activity, result);
                        activityRepository.update(activity);
                        setText(result);
                        if(sort) {
                            dayPanel.sortActivityPanels();
                        }
                    }
                });
            }

        }

        public void setText(String text) {
            super.setText(text);
            //if(!text.isEmpty()) {
                //setToolTipText(text);
                setCaretPosition(0);
            //} else {
//                setToolTipText(null);
//            }
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

    private final TTextField sortkey;
    private final TTextField name;
    private final TTextField comment;
    private final TTextField ticket;

    private final TTextField spentTime;

    private final TTextField flags;

    @Getter(AccessLevel.PRIVATE)
    private final TTextField subject;
    @Getter(AccessLevel.PRIVATE)
    private final TTextField totalComment;

    public final TTextField today;
    public final TTextField remains;

    @Getter
    private boolean deleted;

    public ActivityPanel(ActivityRepositoryApi activityRepository,
            Activity activity, DayPanel dayPanel) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.activity = activity;

        {
            this.subject = new TTextFieldForActivityPanel("", "subject");
            this.totalComment = new TTextFieldForActivityPanel("", "totalComment");
            this.sortkey = new TTextFieldForActivityPanel("1", "sortkey", (a, r)->a.setSortkey(Integer.parseInt(r)), true);
            this.name = new TTextFieldForActivityPanel("", "name", (a, r)->{a.setName(r);getSubject().setText(a.createSubject());}, false);
            this.comment = new TTextFieldForActivityPanel("", "comment",  (a, r)->{a.setComment(r);getTotalComment().setText(a.createTotalComment());}, false);
            this.ticket = new TTextFieldForActivityPanel("", "ticket",
                    (a, r) -> {a.setTicket(r);
                        getSubject().setText(a.createSubject());
                        getTotalComment().setText(a.createTotalComment());}, false);

            this.spentTime =
                    new TTextFieldForActivityPanel("00:00", "spentTime", (a, r) -> {
                        TTime spentTimeTTime = new TTime(r);
                        getActivity().setSpentHours(spentTimeTTime.getHour());
                        getActivity().setSpentMinutes(spentTimeTTime.getMinute());
                        getTotalComment().setText(getActivity().createTotalComment());
                    }, true);

            this.flags = new TTextFieldForActivityPanel("Flags", "flags", (a, r)->a.setFlags(r), false);
            this.today = new TTextFieldForActivityPanel("00:00", "today");
            this.remains = new TTextFieldForActivityPanel("00:00", "remains");
        }

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
        JButton moveButton = new SmallTButton("Move");

        add(copyButton);
        add(deleteButton);
        add(moveButton);

        copyButton.setFont(SwingUtils.SMALL_FONT);
        deleteButton.setFont(SwingUtils.SMALL_FONT);
        moveButton.setFont(SwingUtils.SMALL_FONT);

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
        moveButton.setPreferredSize(PREFERRED_SIZE4);
        this.setPreferredSize(new Dimension(getWidth(), 40));

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
        moveButton.addActionListener(e-> {
            this.dayPanel.markActivityPanelToBeMoved(this);
        });
        dayPanel.sortActivityPanels();
    }

    @Override
    public int compareTo(ActivityPanel o) {
        return this.getActivity().compareTo(o.getActivity());
    }
}
