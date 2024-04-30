package org.nanoboot.utils.timecalc.swing.common;

import lombok.Getter;
import org.nanoboot.utils.timecalc.app.Main;
import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
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

    private static final Dimension MAXIMUM_SIZE = new Dimension(1300, 40);
    private final String year;
    private final String month;
    private final String day;

    private final Map<String, DayPanel> map = new HashMap<>();
    private final ActivityRepositoryApi activityRepository;
    private final TimeCalcConfiguration timeCalcConfiguration;
    private JButton loadButton;
    private JScrollPane scrollPane;
    private JPanel panelInsideScrollPane;
    @Getter
    private ActivityPanel markActivityPanelToBeMoved = null;

    public DayPanel(String yearIn, String monthIn, String dayIn,
            ActivityRepositoryApi activityRepository, TimeCalcConfiguration timeCalcConfiguration) {
        super();

        this.year = yearIn;
        this.month = monthIn;
        this.day = dayIn;
        this.activityRepository = activityRepository;
        this.timeCalcConfiguration = timeCalcConfiguration;
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
        JButton addFlagToAllActivitiesButton = new JButton("Add flag to all activities");
        buttons.add(newButton);
        buttons.add(pasteButton);
        buttons.add(reviewButton);
        buttons.add(statusButton);
        buttons.add(addFlagToAllActivitiesButton);
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
//            String neededFlags = timeCalcConfiguration.activityNeededFlagsProperty.getValue();
//            System.out.println("neededFlags=" + neededFlags);
//            neededFlags.replace(",", ":");
//            String[] neededFlagsArray = neededFlags.split(":");
//            Set<String> neededFlagsSet = Arrays.stream(neededFlagsArray).filter(f -> !f.isEmpty()).collect(Collectors.toSet());
            List<ActivityPanel> activityPanels = new ArrayList<>();
            Arrays
                    .stream(panelInsideScrollPane.getComponents())
                    .filter(c-> c instanceof ActivityPanel).forEach(f-> activityPanels.add((ActivityPanel) f));
            Collections.sort(activityPanels);

//            double done = 0d;
//            double todo = 8d;
//            loopName:
//            for(ActivityPanel ap:activityPanels) {
//                Set<String> flags = ap.getActivity().flagsAsSet();
//                if(!neededFlagsSet.isEmpty()) {
//                    for(String f:neededFlagsSet) {
//                        if(!flags.contains(f)) {
//                            continue loopName;
//                        }
//                    }
//                }
//                double now = ap.getActivity().getSpentHours() + ap.getActivity().getSpentMinutes() / 60d;
//                done = done + now;
//                todo = todo - now;
//            }
//            double progress = done / 8d;
            double progress = activityRepository.getProgressForDay(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), timeCalcConfiguration);

            double doneHours = progress * 8d;
            double todoHours = (8d - doneHours);
            Utils.showNotification("Current status: done=" + NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(doneHours) + "h, todo="+ NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(todoHours));

        });
        addFlagToAllActivitiesButton.addActionListener(e -> {
            String newFlag = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new flag",
                    "New flag",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""
            );
            if(newFlag != null && !newFlag.trim().isEmpty()) {
                getActivityPanels().forEach(a->
                {
                    a.getActivity().addFlag(newFlag);
                    a.flags.setText(a.getActivity().getFlags());
                    activityRepository.update(a.getActivity());
                }
                );
                revalidate();
            }
        });
        sortActivityPanels();
    }
    public List<ActivityPanel> getActivityPanels() {
        return Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> c instanceof ActivityPanel)
                .map(ap->((ActivityPanel) ap)).collect(Collectors.toList());
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
                if(Main.ACTIVITIES_WINDOW_SHOW_SORTKEY) ap.getSortkeyTTextField().setText(String.valueOf(sortkey));

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

            panelInsideScrollPane.add(ap);
            ap.setVisible(false);
            ap.setVisible(true);
            ap.revalidate();
        }


        revalidate();
    }

    public void markActivityPanelToBeMoved(ActivityPanel activityPanel) {
        boolean deletion = this.markActivityPanelToBeMoved == activityPanel;
        this.markActivityPanelToBeMoved = deletion ? null : activityPanel;
        this.markActivityPanelToBeMoved = activityPanel;
    }

}
