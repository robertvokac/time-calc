package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Activity;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

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
        setSize(1050, 600);

        this.setLayout(null);
        this.loadButton = new JButton("Load");
        this.loadButton.setBounds(10, 10, 200, 30);

        this.loadButton.addActionListener(e -> load(activityRepository));
        add(loadButton);

    }

    private void load(ActivityRepositoryApi activityRepository) {
        if (this.loadButton.isVisible()) {
            this.loadButton.setVisible(false);
            this.loadButton = null;
        }

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setAlignmentX(LEFT_ALIGNMENT);
        this.setLayout(boxLayout);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons.setAlignmentX(LEFT_ALIGNMENT);
        JButton newButton = new JButton("New");
        JButton pasteButton = new JButton("Paste");
        buttons.add(newButton);
        buttons.add(pasteButton);
        add(buttons);
        for (int i = 0; i < 10; i++) {
            add(new ActivityPanel(activityRepository,
                    new Activity("aaa", 2000, 7, 7, "a", "b", "c", 2, 30,
                            "a b c", null)));
        }
    }

}
