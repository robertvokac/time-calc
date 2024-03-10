package org.nanoboot.utils.timecalc.swing.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.nanoboot.utils.timecalc.entity.WorkingDay;
import org.nanoboot.utils.timecalc.entity.WorkingDayForStats;
import org.nanoboot.utils.timecalc.persistence.api.WorkingDayRepositoryApi;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class WorkingDaysWindow extends TWindow {
    private static final String NO = "NO";
    private static final String YES = "YES";
    private static final String THREE_DASHES = "---";
    //
    private static final Color RED = new Color(255,153,153);
    public static final String QUESTION_MARK = "?";

    private final WorkingDayRepositoryApi workingDayRepository;
    private final Time time;
    private final JButton reloadButton;
    private final double target;
    private final TTextField startTextField;
    private final TTextField endTextField;
    private ArrivalChart arrivalChart;

    private JTable table = null;
    private final JScrollPane scrollPane;

    public WorkingDaysWindow(WorkingDayRepositoryApi workingDayRepository, Time time, double target) {

        setTitle("Work Days");
        this.workingDayRepository = workingDayRepository;
        this.time = time;
        this.target = target;

        int year = time.yearProperty.getValue();
        this.setLayout(null);

        WorkingDaysWindow workingDaysWindow = this;

        List<String> yearsList = workingDayRepository.getYears();
        String[] yearsArray = new String[yearsList.size()];
        for(int i = 0; i< yearsList.size(); i++){
            yearsArray[i] = yearsList.get(i);
        }
        JComboBox years = new JComboBox(yearsArray);
        years.setMaximumSize(new Dimension(150, 25));

        years.setSelectedItem(String.valueOf(year));
        years.addActionListener(e -> {
            workingDaysWindow.loadYear(Integer.valueOf((String) years.getSelectedItem()), time);
        });

        add(years);
        years.setBounds(SwingUtils.MARGIN,SwingUtils.MARGIN, 100, 30);

        this.reloadButton = new JButton("Reload");
        reloadButton.addActionListener(e -> {
            workingDaysWindow.loadYear(Integer.valueOf((String) years.getSelectedItem()), time);
        });
        add(reloadButton);
        reloadButton.setBounds(years.getX() + years.getWidth() + SwingUtils.MARGIN, years.getY(), 100, 30);


        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            this.setVisible(false);
        });
        add(exitButton);
        exitButton.setBounds(reloadButton.getX() + reloadButton.getWidth() + SwingUtils.MARGIN, reloadButton.getY(), 100, 30);

        TLabel deleteLabel = new TLabel("Delete:");
        add(deleteLabel);
        deleteLabel.setBounds(exitButton.getX() + exitButton.getWidth() + SwingUtils.MARGIN, exitButton.getY(), 100, 30);
        TTextField deleteTextField = new TTextField();
        add(deleteTextField);
        deleteTextField.setBounds(deleteLabel.getX() + deleteLabel.getWidth() + SwingUtils.MARGIN, deleteLabel.getY(), 100, 30);
        deleteTextField.addActionListener(e -> {
            if(deleteTextField.getText().isEmpty()) {
                //nothing to do
                return;
            }
            if(!Utils.askYesNo(this, "Do you really want to delete this day: " + deleteTextField.getText(), "Day deletion")) {
                return;
            }
            workingDayRepository.delete(deleteTextField.getText());
            reloadButton.doClick();
        });


        TLabel startLabel = new TLabel("Start:");
        add(startLabel);
        startLabel.setBounds(deleteTextField.getX() + deleteTextField.getWidth() + SwingUtils.MARGIN, deleteTextField.getY(), 50, 30);
        this.startTextField = new TTextField();
        add(startTextField);
        startTextField.setBounds(startLabel.getX() + startLabel.getWidth() + SwingUtils.MARGIN, startLabel.getY(), 100, 30);
        startTextField.addActionListener(e -> {
            reloadButton.doClick();
        });


        TLabel endLabel = new TLabel("End:");
        add(endLabel);
        endLabel.setBounds(startTextField.getX() + startTextField.getWidth() + SwingUtils.MARGIN, startTextField.getY(), 50, 30);
        this.endTextField = new TTextField();
        add(endTextField);
        endTextField.setBounds(endLabel.getX() + endLabel.getWidth() + SwingUtils.MARGIN, endLabel.getY(), 100, 30);
        endTextField.addActionListener(e -> {
            reloadButton.doClick();
        });


        //
        ArrivalChartData acd = new ArrivalChartData(new String[]{}, new double[]{}, new double[]{}, new double[]{}, new double[]{}, new double[]{},7d, null, null);
        this.arrivalChart = new ArrivalChart(acd);
        arrivalChart.setBounds(SwingUtils.MARGIN, years.getY() + years.getHeight()+ SwingUtils.MARGIN,
                800,
                400);
        add(arrivalChart);
        //
        this.scrollPane
                = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(SwingUtils.MARGIN, arrivalChart.getY() + arrivalChart.getHeight()+ SwingUtils.MARGIN,
                1000,
                300);
        add(scrollPane);
        scrollPane.setViewportView(table);

        loadYear(year, time);

        setSize(scrollPane.getWidth() + 3 * SwingUtils.MARGIN, scrollPane.getY() + scrollPane.getHeight() + 4 * SwingUtils.MARGIN);
    }

    public void doReloadButtonClick() {
        this.reloadButton.doClick();
    }
    public void loadYear(int year, Time time) {

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
                    wd = new WorkingDay(WorkingDay.createId(year, month,day), year, month, day, -1, -1, -1, -1, -1, -1, "Fictive day", true);
                }
                workingDaysList.add(wd);

//        System.out.println("year=" + year);
//        System.out.println("month=" + month);
//        System.out.println("day=" + day);
                if (currentYear == year && currentMonth == month && currentDay == day) {
                    endBeforeYearEnd = true;
                    break;
                }
            }

            if (endBeforeYearEnd) {
                break;
            }
        }

        List<WorkingDayForStats> wdfsList = WorkingDayForStats.createList(workingDaysList);
        WorkingDayForStats.fillStatisticsColumns(wdfsList);

        List<List<String>> listForArray = new ArrayList<>();
        for (WorkingDayForStats wdfs : wdfsList) {
            ArrayList<String> list2 = new ArrayList<>();
            listForArray.add(list2);
            if (wdfs.isThisDayTimeOff()) {
                list2.add(wdfs.getDayOfWeekAsString());
                list2.add(wdfs.getDayOfWeek() == 6 || wdfs.getDayOfWeek() == 7 ? YES : NO);
                list2.add(wdfs.getId());
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(THREE_DASHES);
                list2.add(wdfs.getNote());
                list2.add(wdfs.isTimeOff() ? YES : NO);
                list2.add(QUESTION_MARK);
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage7Days() - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage14Days() - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage28Days() - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage56Days() - target));
            } else {
                list2.add(wdfs.getDayOfWeekAsString());
                TTime overtime = new TTime(wdfs.getOvertimeHour(), wdfs.getOvertimeMinute());
                list2.add(wdfs.getDayOfWeek() == 6 || wdfs.getDayOfWeek() == 7 ? YES : NO);
                list2.add(wdfs.getId());
                list2.add(new TTime(wdfs.getArrivalHour(), wdfs.getArrivalMinute()).toString().substring(0, 5) + " (" + NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalAsDouble())+ ")");
                list2.add(new TTime(wdfs.getDepartureHour(), wdfs.getDepartureMinute()).toString().substring(0, 5));
                list2.add(overtime.toString().substring(0, overtime.isNegative() ? 6 : 5));
                list2.add(TTime.ofMinutes(wdfs.getWorkingTimeInMinutes()).toString().substring(0, 5));
                list2.add(TTime.ofMinutes(wdfs.getPauseTimeInMinutes()).toString().substring(0, 5));
                list2.add(wdfs.getNote());
                list2.add(wdfs.isTimeOff() ? YES : NO);
                list2.add(QUESTION_MARK);
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage7Days() - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage14Days() - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage28Days() - target));
                list2.add(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(wdfs.getArrivalTimeMovingAverage56Days() - target));
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
        String[] columns = new String[] {"Day of Week", "Weekend", "Date","Arrival","Departure","Overtime","Working time","Pause time","Note", "Time off", "Total overtime", "Arrival MA7", "Arrival MA14", "Arrival MA28", "Arrival MA56"};
        
        if(table != null) {
            scrollPane.remove(table);
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            };
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
        //table.setDefaultRenderer(Object.class, new ColorRenderer());
        reloadChart(WorkingDayForStats.toArrivalChartData(wdfsList, 7d, startTextField.getText(), endTextField.getText()));
    }

    public void reloadChart(ArrivalChartData newArrivalChartData) {
        Rectangle bounds = this.arrivalChart.getBounds();
        remove(this.arrivalChart);
        this.arrivalChart = new ArrivalChart(newArrivalChartData);
        add(arrivalChart);
        arrivalChart.setBounds(bounds);
        add(arrivalChart);
    }
}
