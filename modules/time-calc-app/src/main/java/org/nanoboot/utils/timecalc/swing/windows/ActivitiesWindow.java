package org.nanoboot.utils.timecalc.swing.windows;

import org.nanoboot.utils.timecalc.swing.controls.TWindow;
import org.nanoboot.utils.timecalc.swing.controls.TTabbedPane;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.swing.common.MonthPanel;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.YearPanel;
import org.nanoboot.utils.timecalc.swing.progress.Time;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ActivitiesWindow extends TWindow {

    private final ActivityRepositoryApi activityRepository;
    private final Map<String, YearPanel> years;

    public ActivitiesWindow(ActivityRepositoryApi activityRepositoryApiIn, Time time) {
        setSize(1600, 800);
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

        tp.setBounds(addYearButton.getX(), addYearButton.getY() + addYearButton.getHeight() + SwingUtils.MARGIN, 1500, 750);
        yearsList.forEach(y -> {
            final YearPanel yearPanel = new YearPanel(y, activityRepository);
            tp.add(y, yearPanel);
            years.put(y, yearPanel);
        }
        );
        if (!yearsList.contains(currentYearS)) {
            YearPanel yearPanel = new YearPanel(currentYearS, activityRepository);
            tp.add(currentYearS, yearPanel);
            years.put(currentYearS, yearPanel);
        }
        addYearButton.addActionListener(e -> {
            String year_ = JOptionPane.showInputDialog(null,
                    "Please enter year.");

            try {
                Integer.parseInt(year_);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: this is not year: " + year_);
                throw ex;
            }
            for (int i = 0; i < tp.getTabCount(); i++) {
                if (tp.getTitleAt(i).equals(year_)) {
                    String msg = "Error: this year already exists.: " + currentYear;
                    JOptionPane.showMessageDialog(this, msg);
                    throw new TimeCalcException(msg);
                }
            }
            YearPanel yearPanel = new YearPanel(year_, activityRepository);
            tp.add(year_, yearPanel);
            years.put(currentYearS, yearPanel);

        });
        add(tp);
        ChangeListener changeListener = new ChangeListener() {
            private boolean secondOrLaterChange = false;
            public void stateChanged(ChangeEvent changeEvent) {
                if(!secondOrLaterChange) {
                    secondOrLaterChange = true;
                    return;
                }
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();

                YearPanel yearPanel =
                        years.get(sourceTabbedPane.getTitleAt(index));
                yearPanel.load();
                MonthPanel monthPanel = yearPanel.getMonthPanel("1");
                monthPanel.load();
                monthPanel.getDayPanel("1").load();
            }
        };
        tp.addChangeListener(changeListener);
        tp.switchTo(currentYearS);
        YearPanel yearPanel = getYearPanel(currentYearS);
        yearPanel.load();
        yearPanel.setSelectedMonth(currentMonthS);
        MonthPanel monthPanel = yearPanel.getMonthPanel(currentMonthS);
        monthPanel.load();
        monthPanel.setSelectedDay(currentDayS);
        monthPanel.getDayPanel(currentDayS).load();
    }

    public YearPanel getYearPanel(String year) {
        return years.get(year);
    }
}
