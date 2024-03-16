package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author robertvokac
 */
public class YearPanel extends JPanel {

    private final String year;
    private final Map<String, MonthPanel> months;
    private final TTabbedPane tp;

    public YearPanel(String yearIn, ActivityRepositoryApi activityRepository) {
        super();
        this.year = yearIn;
        this.months = new HashMap<>();
        setLayout(null);
        this.tp = new TTabbedPane();
        add(tp);
        tp.setBounds(0, 0, 1450, 700);

        ChangeListener changeListener = new ChangeListener() {
            private boolean secondOrLaterChange = false;
            public void stateChanged(ChangeEvent changeEvent) {
                if(!secondOrLaterChange) {
                    secondOrLaterChange = true;
                    return;
                }
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();

                MonthPanel monthPanel =
                        months.get(sourceTabbedPane.getTitleAt(index));
                monthPanel.load();
                monthPanel.getDayPanel("1").load();
            }
        };
        tp.addChangeListener(changeListener);

        for (int month = 1; month <= 12; month++) {
            final String monthS = String.valueOf(month);
            MonthPanel monthPanel = new MonthPanel(year, String.valueOf(month), activityRepository);
            tp.add(String.valueOf(month), monthPanel);
            months.put(monthS, monthPanel);
        }
    }

    public void setSelectedMonth(String month) {
        tp.switchTo(month);
    }

    public MonthPanel getMonthPanel(String month) {
        return months.get(month);
    }

}
