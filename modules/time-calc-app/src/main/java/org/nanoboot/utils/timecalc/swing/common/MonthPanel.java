package org.nanoboot.utils.timecalc.swing.common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author robertvokac
 */
public class MonthPanel extends JPanel {

    private final String year;
    private final String month;

    private final Map<String, DayPanel> days;
    private final TTabbedPane tp;

    public MonthPanel(String yearIn, String monthIn) {
        super();

        this.year = yearIn;
        this.month = monthIn;

        this.days = new HashMap<>();
        setLayout(null);
        this.tp = new TTabbedPane();
        add(tp);
        tp.setBounds(0, 0, 1100, 650);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDay; day++) {
            String dayS = String.valueOf(day);
            DayPanel dayPanel = new DayPanel(year, month, dayS);
            tp.add(dayS, dayPanel);
            days.put(dayS, dayPanel);
        }
    }

    public void setSelectedDay(String day) {
        tp.switchTo(day);
    }

    public DayPanel getDayPanel(String day) {
        return days.get(day);
    }

}
