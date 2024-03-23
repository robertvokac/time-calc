package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.swing.controls.TTabbedPane;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;

import java.util.Calendar;
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
public class MonthPanel extends JPanel {

    private final String year;
    private final String month;

    private final Map<String, DayPanel> days;
    private final TTabbedPane tp;
    private final ActivityRepositoryApi activityRepository;
    private final Calendar cal;
    private final TimeCalcConfiguration timeCalcConfiguration;

    private boolean loaded = false;
    public MonthPanel(String yearIn, String monthIn, ActivityRepositoryApi activityRepository, TimeCalcConfiguration timeCalcConfiguration) {
        super();
        this.activityRepository = activityRepository;
        this.timeCalcConfiguration = timeCalcConfiguration;

        this.year = yearIn;
        this.month = monthIn;

        this.days = new HashMap<>();
        setLayout(null);
        this.tp = new TTabbedPane();
        add(tp);
        tp.setBounds(0, 0, 1450, 650);

        ChangeListener changeListener = new ChangeListener() {
            private boolean secondOrLaterChange = false;
            public void stateChanged(ChangeEvent changeEvent) {
                if(!secondOrLaterChange) {
                    secondOrLaterChange = true;
                    return;
                }
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();

                days.get(sourceTabbedPane.getTitleAt(index)).load();
            }
        };
        tp.addChangeListener(changeListener);

        this.cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
    }

    public void load() {
        if(loaded) {
            //nothing to do
            return;
        }
        System.out.println("Loaded: " + year + month);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDay; day++) {
            String dayS = String.valueOf(day);
            DayPanel dayPanel = new DayPanel(year, month, dayS,
                    activityRepository, timeCalcConfiguration);
            tp.add(dayS, dayPanel);
            days.put(dayS, dayPanel);
        }
        loaded = true;
    }

    public void setSelectedDay(String day) {
        tp.switchTo(day);
    }

    public DayPanel getDayPanel(String day) {
        return days.get(day);
    }

}
