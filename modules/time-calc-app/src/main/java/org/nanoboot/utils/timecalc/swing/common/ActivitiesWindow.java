package org.nanoboot.utils.timecalc.swing.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.swing.progress.Time;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ActivitiesWindow extends TWindow {

    private final ActivityRepositoryApi activityRepository;
    private final Map<String, YearPanel> years;

    public ActivitiesWindow(ActivityRepositoryApi activityRepositoryApiIn, Time time) {
        setSize(1200, 800);
        setTitle("Activities");
        this.activityRepository = activityRepositoryApiIn;

        this.years = new HashMap<>();
        int currentYear = time.yearProperty.getValue();
        int currentMonth = time.monthProperty.getValue();
        int currentDay = time.dayProperty.getValue();
        String currentYearS = String.valueOf(currentYear);
        String currentMonthS = String.valueOf(currentMonth);
        String currentDayS = String.valueOf(currentDay);
        this.setLayout(null);

        ActivitiesWindow activitiesWindow = this;

        List<String> yearsList = activityRepository.getYears();

        TTabbedPane tp = new TTabbedPane();

        JButton addYearButton = new JButton("Add year");
        addYearButton.setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 150, 30);
        add(addYearButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(SwingUtils.MARGIN + addYearButton.getWidth() + SwingUtils.MARGIN, addYearButton.getY(), 150, 30);
        add(exitButton);
        exitButton.addActionListener(e -> activitiesWindow.setVisible(false));

        tp.setBounds(addYearButton.getX(), addYearButton.getY() + addYearButton.getHeight() + SwingUtils.MARGIN, 1180, 600);
        yearsList.forEach(y -> {
            final YearPanel yearPanel = new YearPanel(y);
            tp.add(y, yearPanel);
            years.put(y, yearPanel);
        }
        );
        if (!yearsList.contains(currentYearS)) {
            YearPanel yearPanel = new YearPanel(currentYearS);
            tp.add(currentYearS, yearPanel);
            years.put(currentYearS, yearPanel);
        }
        addYearButton.addActionListener(e -> {
            String year_ = JOptionPane.showInputDialog(null,
                    "Please enter year.");

            try {
                Integer.parseInt(year_);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: this is not year: " + currentYear);
                throw ex;
            }
            for (int i = 0; i < tp.getTabCount(); i++) {
                if (tp.getTitleAt(i).equals(year_)) {
                    String msg = "Error: this year already exists.: " + currentYear;
                    JOptionPane.showMessageDialog(this, msg);
                    throw new TimeCalcException(msg);
                }
            }
            YearPanel yearPanel = new YearPanel(year_);
            tp.add(year_, yearPanel);
            years.put(currentYearS, yearPanel);

        });
        add(tp);
        
        getYearPanel(currentYearS).setSelectedMonth(currentMonthS);
        getYearPanel(currentYearS).getMonthPanel(currentMonthS).setSelectedDay(currentDayS);
    }

    public YearPanel getYearPanel(String year) {
        return years.get(year);
    }
}
