package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.WorkingDay;
import org.nanoboot.utils.timecalc.entity.WorkingDayForStats;
import org.nanoboot.utils.timecalc.persistence.api.WorkingDayRepositoryApi;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.InvalidationListener;
import org.nanoboot.utils.timecalc.utils.property.Property;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class WorkingDaysWindow extends TWindow {
    private static final String NO = "NO";
    private static final String YES = "YES";
    private static final String THREE_DASHES = "---";
    //
    private static final Color RED = new Color(255, 153, 153);
    public static final String QUESTION_MARK = "?";

    private final WorkingDayRepositoryApi workingDayRepository;
    private final Time time;
    private final JButton reloadButton;
    private final double target;
    private final TTextField startTextField;
    private final TTextField endTextField;
    private final int chartWidth;
    private final JButton decreaseStart;
    private final JButton decreaseEnd;
    private final TTabbedPane tp;
    private final JComboBox years;
    private ArrivalChart arrivalChart;

    private JTable table = null;
    private final JScrollPane scrollPane;
    private int loadedYear;
    private InvalidationListener invalidationWidthListener;
    private InvalidationListener invalidationHeightListener;

    public WorkingDaysWindow(WorkingDayRepositoryApi workingDayRepository,
            Time time, double target) {

        setTitle("Work Days");
        this.workingDayRepository = workingDayRepository;
        this.time = time;
        this.target = target;

        int year = time.yearProperty.getValue();
        this.setLayout(null);

        WorkingDaysWindow workingDaysWindow = this;

        List<String> yearsList = workingDayRepository.getYears();
        String[] yearsArray = new String[yearsList.size()];
        for (int i = 0; i < yearsList.size(); i++) {
            yearsArray[i] = yearsList.get(i);
        }
        this.years = new JComboBox(yearsArray);
        years.setMaximumSize(new Dimension(150, 25));

        years.setSelectedItem(String.valueOf(year));
        years.addActionListener(e -> {
            workingDaysWindow
                    .loadYear(Integer.valueOf((String) years.getSelectedItem()),
                            time);
        });

        add(years);
        years.setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 100, 30);

        this.reloadButton = new JButton("Reload");
        reloadButton.addActionListener(e -> {
            workingDaysWindow
                    .loadYear(Integer.valueOf((String) years.getSelectedItem()),
                            time);
        });
        add(reloadButton);
        reloadButton
                .setBounds(years.getX() + years.getWidth() + SwingUtils.MARGIN,
                        years.getY(), 100, 30);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            this.setVisible(false);
        });
        add(exitButton);
        exitButton.setBounds(reloadButton.getX() + reloadButton.getWidth()
                             + SwingUtils.MARGIN, reloadButton.getY(), 100, 30);

        TLabel deleteLabel = new TLabel("Delete:");
        add(deleteLabel);
        deleteLabel.setBounds(
                exitButton.getX() + exitButton.getWidth() + SwingUtils.MARGIN,
                exitButton.getY(), 50, 30);
        TTextField deleteTextField = new TTextField();
        add(deleteTextField);
        deleteTextField.setBounds(
                deleteLabel.getX() + deleteLabel.getWidth() + SwingUtils.MARGIN,
                deleteLabel.getY(), 100, 30);
        deleteTextField.addActionListener(e -> {
            if (deleteTextField.getText().isEmpty()) {
                //nothing to do
                return;
            }
            if (!Utils.askYesNo(this,
                    "Do you really want to delete this day: " + deleteTextField
                            .getText(), "Day deletion")) {
                return;
            }
            workingDayRepository.delete(deleteTextField.getText());
            reloadButton.doClick();
        });

        TLabel startLabel = new TLabel("Start:");
        add(startLabel);
        startLabel.setBounds(deleteTextField.getX() + deleteTextField.getWidth()
                             + 6 * SwingUtils.MARGIN, deleteTextField.getY(),
                50, 30);
        this.startTextField = new TTextField();
        add(startTextField);
        startTextField.setBounds(
                startLabel.getX() + startLabel.getWidth() + SwingUtils.MARGIN,
                startLabel.getY(), 100, 30);
        startTextField.addActionListener(e -> {
            reloadButton.doClick();
        });

        TLabel endLabel = new TLabel("End:");
        add(endLabel);
        endLabel.setBounds(startTextField.getX() + startTextField.getWidth()
                           + SwingUtils.MARGIN, startTextField.getY(), 50, 30);
        this.endTextField = new TTextField();
        add(endTextField);
        endTextField.setBounds(
                endLabel.getX() + endLabel.getWidth() + SwingUtils.MARGIN,
                endLabel.getY(), 100, 30);
        endTextField.addActionListener(e -> {
            reloadButton.doClick();
        });
        this.decreaseStart = new JButton("Decrease start");
        this.decreaseEnd = new JButton("Decrease end");
        add(decreaseStart);
        add(decreaseEnd);
        decreaseStart.addActionListener(e -> {
            if (loadedYear == 0) {
                //nothing to do
                return;
            }
            String date = this.startTextField.getText();
            if (date.isEmpty()) {
                this.startTextField.setText(loadedYear + "-02-01");
                reloadButton.doClick();
                return;
            } else {
                String[] array = date.split("-");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, loadedYear);
                cal.set(Calendar.MONTH, Integer.valueOf(array[1]) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(array[2]));
                cal.add(Calendar.DAY_OF_MONTH, 32);
                if (cal.get(Calendar.YEAR) != loadedYear) {
                    return;
                }
                this.startTextField.setText(
                        DateFormats.DATE_TIME_FORMATTER_YYYYMMDD
                                .format(cal.getTime()));
                reloadButton.doClick();
            }

        });
        decreaseEnd.addActionListener(e -> {
            if (loadedYear == 0) {
                //nothing to do
                return;
            }
            String date = this.endTextField.getText();
            if (date.isEmpty()) {
                this.endTextField.setText(loadedYear + "-11-30");
                reloadButton.doClick();
                return;
            } else {
                String[] array = date.split("-");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, loadedYear);
                cal.set(Calendar.MONTH, Integer.valueOf(array[1]) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(array[2]));
                cal.add(Calendar.DAY_OF_MONTH, -32);
                if (cal.get(Calendar.YEAR) != loadedYear) {
                    return;
                }
                this.endTextField.setText(
                        DateFormats.DATE_TIME_FORMATTER_YYYYMMDD
                                .format(cal.getTime()));
                reloadButton.doClick();
            }

        });
        decreaseStart.setBounds(endTextField.getX() + endTextField.getWidth()
                                + SwingUtils.MARGIN, endTextField.getY(), 120,
                30);
        decreaseEnd.setBounds(decreaseStart.getX() + decreaseStart.getWidth()
                              + SwingUtils.MARGIN, decreaseStart.getY(), 120,
                30);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: decreaseStart.doClick();break;
                    case KeyEvent.VK_RIGHT: decreaseEnd.doClick();break;
                    default:
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.tp = new TTabbedPane();

        //
        ArrivalChartData acd =
                new ArrivalChartData(new String[] {}, new double[] {}, 7d,
                        new double[] {}, new double[] {}, new double[] {},
                        new double[] {}, null, null, false, false, false);
        this.arrivalChart = new ArrivalChart(acd, 1000);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.chartWidth = (int) screen.getWidth() - 60;
        arrivalChart.setBounds(SwingUtils.MARGIN,
                SwingUtils.MARGIN,
                (int) (screen.getWidth() - 50),
                400);

        //
        this.scrollPane
                = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tp.setBounds(years.getX(), years.getY() + years.getHeight() + SwingUtils.MARGIN, (int) (screen.getWidth() - 50),
                (int) (screen.getHeight() - 200));
        tp.add(scrollPane, "Table");
        tp.add(arrivalChart, "Chart");
        add(tp);

        scrollPane.setBounds(SwingUtils.MARGIN,
                SwingUtils.MARGIN,
                (int) (screen.getWidth() - 50),
                300);

        scrollPane.setViewportView(table);

        loadYear(year, time);

        setSize(tp.getWidth() + 3 * SwingUtils.MARGIN,
                tp.getY() + tp.getHeight()
                + 4 * SwingUtils.MARGIN);
    }

    public void doReloadButtonClick() {
        this.reloadButton.doClick();
    }

    public void loadYear(int year, Time time) {
        if (this.loadedYear != year) {
            this.startTextField.setText("");
            this.endTextField.setText("");
        }
        this.loadedYear = year;
        List<WorkingDay> workingDaysList = new ArrayList<>();
        Calendar now = time.asCalendar();
        final int currentYear = now.get(Calendar.YEAR);
        final int currentMonth = now.get(Calendar.MONTH) + 1;
        final int currentDay = now.get(Calendar.DAY_OF_MONTH);
        //        System.out.println("currentYear=" + currentYear);
        //        System.out.println("currentMonth=" + currentMonth);
        //        System.out.println("currentDay=" + currentDay);
        boolean endBeforeYearEnd = false;
        for (int month = 1; month <= 12; month++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            int dayMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int day = 1; day <= dayMaximum; day++) {

                WorkingDay wd = workingDayRepository.read(year, month, day);
                if (wd == null) {
                    wd = new WorkingDay(WorkingDay.createId(year, month, day),
                            year, month, day, -1, -1, -1, -1, -1, -1,
                            "Fictive day", true);
                }
                workingDaysList.add(wd);

                //        System.out.println("year=" + year);
                //        System.out.println("month=" + month);
                //        System.out.println("day=" + day);
                if (currentYear == year && currentMonth == month
                    && currentDay == day) {
                    endBeforeYearEnd = true;
                    break;
                }
            }

            if (endBeforeYearEnd) {
                break;
            }
        }

        List<WorkingDayForStats> wdfsList =
                WorkingDayForStats.createList(workingDaysList);
        WorkingDayForStats.fillStatisticsColumns(wdfsList);

        List<List<String>> listForArray = new ArrayList<>();
        for (WorkingDayForStats wdfs : wdfsList) {
            ArrayList<String> list2 = new ArrayList<>();
            listForArray.add(list2);
            if (wdfs.isThisDayTimeOff()) {
                list2.add(wdfs.getDayOfWeekAsString());
                list2.add(wdfs.getDayOfWeek() == 6 || wdfs.getDayOfWeek() == 7 ?
                        YES : NO);
                list2.add(wdfs.getId());
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(wdfs.getNote());
                list2.add(wdfs.isTimeOff() ? YES : NO);
                list2.add(QUESTION_MARK);
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage7Days()
                                - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage14Days()
                                - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage28Days()
                                - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage56Days()
                                - target));
            } else {
                list2.add(wdfs.getDayOfWeekAsString());
                TTime overtime = new TTime(wdfs.getOvertimeHour(),
                        wdfs.getOvertimeMinute());
                list2.add(wdfs.getDayOfWeek() == 6 || wdfs.getDayOfWeek() == 7 ?
                        YES : NO);
                list2.add(wdfs.getId());
                list2.add(new TTime(wdfs.getArrivalHour(),
                        wdfs.getArrivalMinute()).toString().substring(0, 5)
                          + " (" + NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                                  .format(wdfs.getArrivalAsDouble()) + ")");
                list2.add(new TTime(wdfs.getDepartureHour(),
                        wdfs.getDepartureMinute()).toString().substring(0, 5));
                list2.add(overtime.toString()
                        .substring(0, overtime.isNegative() ? 6 : 5));
                list2.add(TTime.ofMinutes(wdfs.getWorkingTimeInMinutes())
                        .toString().substring(0, 5));
                list2.add(
                        TTime.ofMinutes(wdfs.getPauseTimeInMinutes()).toString()
                                .substring(0, 5));
                list2.add(wdfs.getNote());
                list2.add(wdfs.isTimeOff() ? YES : NO);
                list2.add(QUESTION_MARK);
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage7Days()
                                - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage14Days()
                                - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage28Days()
                                - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(wdfs.getArrivalTimeMovingAverage56Days()
                                - target));
            }
        }

        String data[][] = new String[listForArray.size()][];
        int index = 0;
        for (List<String> list1 : listForArray) {
            String data2[] = new String[list1.size()];
            for (int i = 0; i < list1.size(); i++) {
                data2[i] = list1.get(i);
            }
            data[index] = data2;
            index++;
        }
        String[] columns =
                new String[] {"Day of Week", "Weekend", "Date", "Arrival",
                        "Departure", "Overtime", "Working time", "Pause time",
                        "Note", "Time off", "Total overtime", "Arrival MA7",
                        "Arrival MA14", "Arrival MA28", "Arrival MA56"};

        if (table != null) {
            scrollPane.remove(table);
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            ;
        };
        //        class ColorRenderer extends JLabel
        //                implements TableCellRenderer {
        //
        //            public ColorRenderer() {
        //                //super.setOpaque(true);
        //            }
        //
        //            public Component getTableCellRendererComponent(
        //                    JTable table, Object value,
        //                    boolean isSelected, boolean hasFocus,
        //                    int row, int column) {
        //
        //                if (value.equals("SATURDAY")) {
        //                    super.setForeground(Color.red);
        //                    //super.setForeground(Color.black);
        //
        //
        //                } else {
        //                    super.setForeground(Color.YELLOW);
        //                }
        //                setFont(new Font("Dialog", Font.BOLD, 12));
        //                return this;
        //
        //            }
        //        }

        this.table = new JTable(model) {
            //
            //            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            //
            //                Component c = super.prepareRenderer(renderer, row, column);
            //
            //                //  Alternate row color
            //                String value = data[row - 1][0];
            //                System.out.println("v=" + value);
            ////                if (value.equals("SATURDAY"))  {
            ////                    c.setBackground(RED);
            ////                } else c.setBackground(getBackground());
            //                c.setBackground(getBackground());
            //                return c;
            //            }
        };

        scrollPane.setViewportView(table);
        table.setBounds(30, 30, 750, 600);
        this.widthProperty.addListener(e-> {
            if(/*this.widthProperty.getValue() % 10 == 0 && */this.widthProperty.getValue() > 400) {
                tp.setBounds(years.getX(), years.getY() + years.getHeight() + SwingUtils.MARGIN, this.widthProperty.getValue() - 50,
                        tp.getHeight());
            }
        });
        this.heightProperty.addListener(e-> {
            if(/*this.widthProperty.getValue() % 10 == 0 && */this.heightProperty.getValue() > 400) {
                tp.setBounds(years.getX(), years.getY() + years.getHeight() + SwingUtils.MARGIN, tp.getWidth(),
                        getHeight() - years.getHeight() * 4);
            }
        });
        //table.setDefaultRenderer(Object.class, new ColorRenderer());
        ArrivalChartData acd = WorkingDayForStats
                .toArrivalChartData(wdfsList, 7d, startTextField.getText(),
                        endTextField.getText());
        acd.setMa14Enabled(acd.isMa14Enabled());
        acd.setMa28Enabled(acd.isMa28Enabled());
        acd.setMa56Enabled(acd.isMa56Enabled());
        reloadChart(acd);
    }

    public void reloadChart(ArrivalChartData newArrivalChartData) {
        Rectangle bounds = this.arrivalChart.getBounds();
        String currentTitle = tp.getTitleAt(tp.getSelectedIndex());
        this.tp.remove(this.arrivalChart);
        this.arrivalChart = new ArrivalChart(newArrivalChartData, chartWidth);
        if(this.invalidationWidthListener != null) {
            this.widthProperty.removeListener(invalidationWidthListener);
            this.invalidationWidthListener = null;
        }
        if(this.invalidationHeightListener != null) {
            this.heightProperty.removeListener(invalidationHeightListener);
            this.invalidationHeightListener = null;
        }
        this.invalidationWidthListener = property -> arrivalChart.widthProperty.setValue(tp.getWidth() - 10);
        this.invalidationHeightListener = property -> arrivalChart.heightProperty.setValue(tp.getHeight() - 10);
        this.widthProperty.addListener(invalidationWidthListener);
        this.heightProperty.addListener(invalidationHeightListener);
        arrivalChart.setBounds(bounds);
        tp.add(arrivalChart, "Chart");
        tp.switchTo(currentTitle);
        arrivalChart.widthProperty.setValue(tp.getWidth() - 30);
        arrivalChart.heightProperty.setValue(tp.getHeight()  - 10);
    }
}
