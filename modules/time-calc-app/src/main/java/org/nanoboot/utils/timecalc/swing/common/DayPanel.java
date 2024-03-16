package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author robertvokac
 */
public class DayPanel extends JPanel {

    private static final String FOR_ACTIVITY_ID = "for-activity-id";
    private static final Dimension MAXIMUM_SIZE = new Dimension(1300, 40);
    private static final Dimension MAXIMUM_SIZE_2 = new Dimension(1200, 20);
    private final String year;
    private final String month;
    private final String day;

    private final Map<String, DayPanel> map = new HashMap<>();
    private final ActivityRepositoryApi activityRepository;
    private JButton loadButton;
    private JScrollPane scrollPane;
    private JPanel panelInsideScrollPane;
    private ActivityPanel markActivityPanelToBeMoved = null;

    class MoveHereButton extends JButton {
        public MoveHereButton(String activityId) {
            setText("Move here");
            setMaximumSize(MAXIMUM_SIZE_2);
            putClientProperty(FOR_ACTIVITY_ID, activityId);
            setVisible(true);
            addActionListener(e-> {
                List<Activity> list = getActivities();
                Activity activityToBeMoved = activityRepository.read(markActivityPanelToBeMoved.getActivity().getId());
                Activity activityTarget = activityRepository.read(activityId);
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
                activityToBeMoved.setSortkey(newSortKey);
                ActivityPanel activityPanelForActivity =
                        getActivityPanelForActivity(activityToBeMoved);
                activityPanelForActivity.getActivity().setSortkey(newSortKey);
                activityPanelForActivity.getSortkeyTTextField().setText(
                        String.valueOf(newSortKey));
                activityRepository.update(activityToBeMoved);
                sortActivityPanels();


            });
        }
        public String getActivityId() {
            return (String) getClientProperty(FOR_ACTIVITY_ID);
        }
    }

    public DayPanel(String yearIn, String monthIn, String dayIn,
            ActivityRepositoryApi activityRepository) {
        super();

        this.year = yearIn;
        this.month = monthIn;
        this.day = dayIn;
        this.activityRepository = activityRepository;
        setSize(1450, 600);

        this.setLayout(null);
        this.loadButton = new JButton("Load");
        this.loadButton.setBounds(10, 10, 200, 30);

        this.loadButton.addActionListener(e -> load());
        add(loadButton);

    }

    public void load() {
        if(this.loadButton == null) {
            //nothing to do
            return;
        }
        System.out.println("Loaded: " + year + month + day);
        if (this.loadButton.isVisible()) {
            this.loadButton.setVisible(false);
            this.loadButton = null;
        } else {
            //already loaded
            return;
        }

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setAlignmentX(LEFT_ALIGNMENT);
        this.setLayout(boxLayout);

        JPanel buttons = new JPanel();

        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons.setAlignmentX(LEFT_ALIGNMENT);
        JButton newButton = new JButton("New");
        JButton pasteButton = new JButton("Paste");

        JButton reviewButton = new JButton("Copy all Total comments to clipboard");
        JButton statusButton = new JButton("Status");
        buttons.add(newButton);
        buttons.add(pasteButton);
        buttons.add(reviewButton);
        buttons.add(statusButton);
        add(buttons);

        this.scrollPane
                = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
        this.panelInsideScrollPane = new JPanel();
        panelInsideScrollPane.setLayout(new BoxLayout(panelInsideScrollPane, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(panelInsideScrollPane);
        ActivityHeader activityHeader = new ActivityHeader();
        panelInsideScrollPane.add(activityHeader);
        activityHeader.setMaximumSize(new Dimension(1200, 40));

        buttons.setMaximumSize(new Dimension(1000, 40));
        List<Activity> list = activityRepository.list(
                Integer.valueOf(year),
                Integer.valueOf(month),
                Integer.valueOf(day));
        Collections.sort(list);
        for (Activity a : list) {
            ActivityPanel comp =
                    new ActivityPanel(activityRepository, a, this);
            comp.setMaximumSize(MAXIMUM_SIZE);

            panelInsideScrollPane.add(comp);

        }

        revalidate();
        newButton.addActionListener(e-> {
            Activity newActivity = new Activity(UUID.randomUUID().toString(), Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), "", "", "", 0, 0, "", activityRepository.getNextSortkey(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
            ActivityPanel comp =
                    new ActivityPanel(activityRepository, newActivity, this);
            comp.setMaximumSize(new Dimension(1300, 40));
            add(comp);
            activityRepository.create(newActivity);
            if(this.markActivityPanelToBeMoved != null) {
                panelInsideScrollPane.add(new MoveHereButton(newActivity.getId()));
            } else {
                //comp.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            }
            panelInsideScrollPane.add(comp);

            revalidate();
        });
        pasteButton.addActionListener(e-> {
            Activity afc = activityRepository.getFromClipboard();
            if(afc == null) {
                Utils.showNotification("There is no activity in clipboard. Nothing to do.");
                return;
            }
            Activity newActivity = new Activity(UUID.randomUUID().toString(), Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day),
                    afc.getName(), afc.getComment(), afc.getTicket(), 0, 0, "", activityRepository.getNextSortkey(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
            ActivityPanel comp =
                    new ActivityPanel(activityRepository, newActivity, this);
            comp.setMaximumSize(new Dimension(1200, 40));
            add(comp);
            activityRepository.create(newActivity);
            if(this.markActivityPanelToBeMoved != null) {
                panelInsideScrollPane.add(new MoveHereButton(newActivity.getId()));
            } else {
                //comp.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            }
            panelInsideScrollPane.add(comp);

            revalidate();
        });
        reviewButton.addActionListener(e->{
            String comments = Arrays
                    .stream(panelInsideScrollPane.getComponents())
                    .filter(c-> c instanceof ActivityPanel)
                    .map(ap->((ActivityPanel) ap).getActivity())
                    .map(a->a.createTotalComment())
                    .collect(
                            Collectors.joining("\n"));
            JOptionPane
                    .showMessageDialog(null, comments, "All comments for: " + year + "-" + (month.length() < 2 ? "0" : "") + month + "-" + (day.length() < 2 ? "0" : "") + day , JOptionPane.INFORMATION_MESSAGE);

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(comments), null);
        });
        statusButton.addActionListener(e-> {
            List<ActivityPanel> activityPanels = new ArrayList<>();
            Arrays
                    .stream(panelInsideScrollPane.getComponents())
                    .filter(c-> c instanceof ActivityPanel).forEach(f-> activityPanels.add((ActivityPanel) f));
            Collections.sort(activityPanels);

            double done = 0d;
            double todo = 8d;
            for(ActivityPanel ap:activityPanels) {

                double now = ap.getActivity().getSpentHours() + ap.getActivity().getSpentMinutes() / 60d;
                done = done + now;
                todo = todo - now;
            }
            Utils.showNotification("Current status: done=" + NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(done) + "h, todo="+ NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(todo));

        });
        //        for (int i = 0; i < 10; i++) {
        //            add(new ActivityPanel(activityRepository,
        //                    new Activity("id", 2000, 7, 7, "name", "comment", "ticket", 2, 30,
        //                            "a b c", null)));
        //        }
    }
    public List<Activity> getActivities() {
        return Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> c instanceof ActivityPanel)
                .map(ap->((ActivityPanel) ap).getActivity()).collect(Collectors.toList());
    }

    public int getIndexForActivityPanel(Activity a) {
        for(int i = 0;i<panelInsideScrollPane.getComponentCount();i++) {
            Component c = panelInsideScrollPane.getComponent(i);
            if(c instanceof ActivityPanel) {
                if(((ActivityPanel)c).getActivity().equals(a)) {
                    return i;
                }
            }
        }
        return -1;
    }
    public ActivityPanel getActivityPanelForActivity(Activity a) {
        Optional<Component> optional = Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> c instanceof ActivityPanel)
                .filter(c-> ((ActivityPanel) c).getActivity().getId().equals(a.getId()))
                .findFirst();
        if(optional.isPresent()) {
            return (ActivityPanel) optional.get();
        } else {
            return null;
        }
    }
    public void sortActivityPanels() {
        System.out.println("sortActivityPanels()");
        List<ActivityPanel> list = new ArrayList<>();
        Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> c instanceof ActivityPanel).filter(c-> !((ActivityPanel)c).isDeleted()).forEach(e-> list.add((ActivityPanel) e));
        Collections.sort(list);
        Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> {return (c instanceof MoveHereButton);}).forEach(c-> panelInsideScrollPane.remove(c));

        //.filter(c -> getClientProperty( FOR_ACTIVITY_ID) != null)
        for(ActivityPanel ap:list) {
            panelInsideScrollPane.remove(ap);
        }
        double done = 0d;
        double todo = 8d;
        int lastSortkey = 0;
        boolean recalculateSortKeys = false;
        for(ActivityPanel ap:list) {
            if(ap.getActivity().getSortkey() - lastSortkey < 4) {
                recalculateSortKeys = true;
                break;
            } else {
                lastSortkey = ap.getActivity().getSortkey();
            }
        }
        int sortkey = 0;
        if(recalculateSortKeys) {
            int sortkeySpace = activityRepository.getSortkeySpace();

            for(ActivityPanel ap:list) {
                sortkey = sortkey + sortkeySpace;
                ap.getActivity().setSortkey(sortkey);
                activityRepository.update(ap.getActivity());
                ap.getSortkeyTTextField().setText(String.valueOf(sortkey));

            }
        }
        Collections.sort(list);
        for(ActivityPanel ap:list) {

            double now = ap.getActivity().getSpentHours() + ap.getActivity().getSpentMinutes() / 60d;
            done = done + now;
            todo = todo - now;
            TTime doneTTime =
                    TTime.ofMilliseconds((int) (done * 60d * 60d * 1000d));
            ap.today.setText(doneTTime.toString().substring(0,5));
            TTime todoTTime =
                    TTime.ofMilliseconds((int) (todo * 60d * 60d * 1000d));
            ap.remains.setText(todoTTime.toString().substring(0,todoTTime.isNegative() ? 6 : 5));
            {
                if(this.markActivityPanelToBeMoved != null) {
                    MoveHereButton mhb =
                            new MoveHereButton(ap.getActivity().getId());
                    panelInsideScrollPane.add(mhb);
                } else {
//                    Component mhb = new MoveHereButton(ap.getActivity().getId());
//                    panelInsideScrollPane.add(mhb);
                }
            }
            panelInsideScrollPane.add(ap);
            ap.setVisible(false);
            ap.setVisible(true);
            ap.revalidate();
        }


        revalidate();
    }

    public void markActivityPanelToBeMoved(ActivityPanel activityPanel) {
        boolean moveHereButtonsExist = Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> {return (c instanceof MoveHereButton);}).findFirst().isPresent();
        boolean deletion = this.markActivityPanelToBeMoved == activityPanel;
        boolean enabling = this.markActivityPanelToBeMoved == null && activityPanel != null;
        this.markActivityPanelToBeMoved = deletion ? null : activityPanel;
        if(moveHereButtonsExist && deletion) {
            sortActivityPanels();
        }
        if(!moveHereButtonsExist && enabling) {
            sortActivityPanels();
        }

    }

}
