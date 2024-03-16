package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
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

    private final String year;
    private final String month;
    private final String day;

    private final Map<String, DayPanel> map = new HashMap<>();
    private final ActivityRepositoryApi activityRepository;
    private JButton loadButton;
    private JScrollPane scrollPane;
    private JPanel panelInsideScrollPane;

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
        //buttons.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons.setAlignmentX(LEFT_ALIGNMENT);
        JButton newButton = new JButton("New");
        JButton pasteButton = new JButton("Paste");

        JButton reviewButton = new JButton("Review");;
        buttons.add(newButton);
        buttons.add(pasteButton);
        buttons.add(reviewButton);
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
        for (Activity a : activityRepository.list(
                Integer.valueOf(year),
                Integer.valueOf(month),
                Integer.valueOf(day))) {

            ActivityPanel comp =
                    new ActivityPanel(activityRepository, a, this);
            comp.setMaximumSize(new Dimension(1300, 40));
            panelInsideScrollPane.add(comp);

        }
        new Timer(100, e -> {
            List<Component>
                    list = Arrays.stream(panelInsideScrollPane.getComponents()).filter(c-> c instanceof ActivityPanel).filter(c-> ((ActivityPanel)c).isDeleted()).collect(
                    Collectors.toList());

            if(!list.isEmpty()) {
                list.forEach(c->panelInsideScrollPane.remove(c));
            }
            revalidate();
        }).start();
        revalidate();
        newButton.addActionListener(e-> {
            Activity newActivity = new Activity(UUID.randomUUID().toString(), Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), "", "", "", 0, 0, "", 1 + activityRepository.getLargestSortkey(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
            ActivityPanel comp =
                    new ActivityPanel(activityRepository, newActivity, this);
            comp.setMaximumSize(new Dimension(1200, 40));
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
                    afc.getName(), afc.getComment(), afc.getTicket(), 0, 0, "", 1 + activityRepository.getLargestSortkey(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
            ActivityPanel comp =
                    new ActivityPanel(activityRepository, newActivity, this);
            comp.setMaximumSize(new Dimension(1200, 40));
            add(comp);
            activityRepository.create(newActivity);

            panelInsideScrollPane.add(comp);

            revalidate();
        });
        reviewButton.addActionListener(e->{
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(Arrays
                    .stream(panelInsideScrollPane.getComponents())
                    .filter(c-> c instanceof ActivityPanel)
                    .map(ap->((ActivityPanel) ap).getActivity())
                    .map(a->a.createTotalComment())
                    .collect(
                            Collectors.joining("\n"))), null);
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
                .filter(c-> ((ActivityPanel) c).getActivity().equals(a))
                .findFirst();
        if(optional.isPresent()) {
            return (ActivityPanel) optional.get();
        } else {
            return null;
        }
    }
    public void sortActivityPanels() {
        List<ActivityPanel> list = new ArrayList<>();
        Arrays
                .stream(panelInsideScrollPane.getComponents())
                .filter(c-> c instanceof ActivityPanel).forEach(e-> list.add((ActivityPanel) e));
        Collections.sort(list);
        for(ActivityPanel ap:list) {
            panelInsideScrollPane.remove(ap);
        }
        double done = 0d;
        double todo = 8d;
        for(ActivityPanel ap:list) {
            panelInsideScrollPane.add(ap);
            double now = ap.getActivity().getSpentHours() + ap.getActivity().getSpentMinutes() / 60d;
            done = done + now;
            todo = todo - now;
            ap.today.setText(TTime.ofMilliseconds((int)(done * 60d * 60d * 1000d)).toString().substring(0,5));
            ap.remains.setText(TTime.ofMilliseconds((int)(todo * 60d * 60d * 1000d)).toString().substring(0,5));
        }
        revalidate();
    }

}
