package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public DayPanel(String yearIn, String monthIn, String dayIn,
            ActivityRepositoryApi activityRepository) {
        super();

        this.year = yearIn;
        this.month = monthIn;
        this.day = dayIn;
        this.activityRepository = activityRepository;
        setSize(1350, 600);

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
        buttons.add(newButton);
        buttons.add(pasteButton);
        add(buttons);
        ActivityHeader activityHeader = new ActivityHeader();
        add(activityHeader);
        activityHeader.setMaximumSize(new Dimension(1200, 40));
        buttons.setMaximumSize(new Dimension(1000, 40));
        for (Activity a : activityRepository.list(
                Integer.valueOf(year),
                Integer.valueOf(month),
                Integer.valueOf(day))) {

            ActivityPanel comp =
                    new ActivityPanel(activityRepository, a);
            comp.setMaximumSize(new Dimension(1200, 40));
            add(comp);

        }
        revalidate();
        newButton.addActionListener(e-> {
            Activity newActivity = new Activity(UUID.randomUUID().toString(), Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), "", "", "", 0, 0, "", null);
            ActivityPanel comp =
                    new ActivityPanel(activityRepository, newActivity);
            comp.setMaximumSize(new Dimension(1200, 40));
            add(comp);
            activityRepository.create(newActivity);
            revalidate();
        });
//        for (int i = 0; i < 10; i++) {
//            add(new ActivityPanel(activityRepository,
//                    new Activity("id", 2000, 7, 7, "name", "comment", "ticket", 2, 30,
//                            "a b c", null)));
//        }
    }

}
