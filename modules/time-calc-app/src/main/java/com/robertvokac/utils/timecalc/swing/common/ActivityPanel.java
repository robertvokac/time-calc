package com.robertvokac.utils.timecalc.swing.common;

import com.robertvokac.utils.timecalc.app.Main;
import com.robertvokac.utils.timecalc.swing.controls.MouseClickedListener;
import com.robertvokac.utils.timecalc.swing.controls.SmallTButton;
import com.robertvokac.utils.timecalc.swing.controls.TTextField;
import lombok.AccessLevel;
import lombok.Getter;
import com.robertvokac.utils.timecalc.entity.Activity;
import com.robertvokac.utils.timecalc.persistence.api.ActivityRepositoryApi;
import com.robertvokac.utils.timecalc.utils.common.TTime;

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
import java.util.List;
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
            } else {
                addMouseListener((MouseClickedListener) e -> {
                    Clipboard
                            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(getText()), null);
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

    public final TTextField flags;

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
        if(Main.ACTIVITIES_WINDOW_SHOW_SORTKEY) add(sortkey);
        add(name);
        add(comment);
        add(ticket);
        add(spentTime);

        add(flags);
        add(subject);
        add(totalComment);
        add(today);
        add(remains);
        sortkey.setVisible(false);
        name.setHorizontalAlignment(JTextField.LEFT);
        comment.setHorizontalAlignment(JTextField.LEFT);
        ticket.setHorizontalAlignment(JTextField.LEFT);

        JButton copyButton = new SmallTButton("Copy");
        JButton deleteButton = new SmallTButton("Delete");
        JButton moveThisButton = new SmallTButton("Move this");
        JButton moveBeforeButton = new SmallTButton("Move before");

        add(copyButton);
        add(deleteButton);
        add(moveThisButton);
        add(moveBeforeButton);

        copyButton.setFont(SwingUtils.SMALL_FONT);
        deleteButton.setFont(SwingUtils.SMALL_FONT);
        moveThisButton.setFont(SwingUtils.SMALL_FONT);
        moveBeforeButton.setFont(SwingUtils.SMALL_FONT);

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
        moveThisButton.setPreferredSize(PREFERRED_SIZE1);
        moveBeforeButton.setPreferredSize(PREFERRED_SIZE1);
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
            this.deleted = activityRepository.delete(this.activity.getId());
            if(deleted) {
                this.setVisible(false);
                deleted = true;
            }

        });
        copyButton.addActionListener(e -> {
            activityRepository.putToClipboard(this.activity);
        });
        moveThisButton.addActionListener(e-> {
            this.dayPanel.markActivityPanelToBeMoved(this);
        });

        moveBeforeButton.addActionListener(e-> {
            if(dayPanel.getMarkActivityPanelToBeMoved() == null) {
                //nothing to do
                return;
            }
            List<Activity> list = dayPanel.getActivities();
            Activity activityToBeMoved = activityRepository.read(dayPanel.getMarkActivityPanelToBeMoved().getActivity().getId());
            Activity activityTarget = activityRepository.read(activity.getId());
            int activityTargetSortkey = activityTarget.getSortkey();
            int newSortKey = activityToBeMoved.getSortkey();
            for(int i = 0; i < list.size(); i++) {
                if(activityToBeMoved.getSortkey() == activityTarget.getSortkey()) {
                    //nothing to do
                    break;
                }
                if(i >= 1  && activityToBeMoved.getSortkey() == activityTarget.getSortkey()) {
                    //nothing to do
                    break;
                }
                if(list.get(i).getId().equals(activityTarget.getId())) {
                    Activity activityBefore = i == 0 ? null : list.get(i - 1);
                    if(activityBefore != null && activityBefore.getId().equals(activityToBeMoved.getId())) {
                        //nothing to do
                        break;
                    }
                    int activityBeforeSortkey = activityBefore == null ? activityTargetSortkey : activityBefore.getSortkey();
                    int start = activityBeforeSortkey + 1;
                    int end = activityTargetSortkey - 1;
                    if(start > end) {
                        start = end;
                    }
                    if(start == end) {
                        newSortKey = end;
                        break;
                    }
                    newSortKey = start + (end - start) / 2;
                    if(newSortKey > activityTargetSortkey) {
                        newSortKey = activityTargetSortkey;
                    }
                    break;
                }

            }
            int oldSortkey = activityToBeMoved.getSortkey();
            if(oldSortkey != newSortKey) {
                activityToBeMoved.setSortkey(newSortKey);
            }
            ActivityPanel activityPanelForActivity =
                    dayPanel.getActivityPanelForActivity(activityToBeMoved);
            activityPanelForActivity.getActivity().setSortkey(newSortKey);
            activityPanelForActivity.getSortkeyTTextField().setText(
                    String.valueOf(newSortKey));
            activityRepository.update(activityToBeMoved);
            dayPanel.sortActivityPanels();


        });
    }

    @Override
    public int compareTo(ActivityPanel o) {
        return this.getActivity().compareTo(o.getActivity());
    }
    public TTextField getSortkeyTTextField() {
        return sortkey;
    }
}
