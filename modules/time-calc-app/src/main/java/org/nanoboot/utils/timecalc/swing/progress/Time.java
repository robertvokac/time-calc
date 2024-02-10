package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.ReadOnlyProperty;

import java.util.Calendar;
import java.util.Date;

public class Time extends Thread {

    private final IntegerProperty yearReadWriteProperty =
            new IntegerProperty("yearProperty");
    public ReadOnlyProperty<Integer> yearProperty =
            yearReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty monthReadWriteProperty =
            new IntegerProperty("monthProperty");
    public ReadOnlyProperty<Integer> monthProperty =
            monthReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty dayReadWriteProperty =
            new IntegerProperty("dayProperty");
    public ReadOnlyProperty<Integer> dayProperty =
            dayReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty hourReadWriteProperty =
            new IntegerProperty("hourProperty");
    public ReadOnlyProperty<Integer> hourProperty =
            hourReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty minuteReadWriteProperty =
            new IntegerProperty("minuteProperty");
    public ReadOnlyProperty<Integer> minuteProperty =
            minuteReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty secondReadWriteProperty =
            new IntegerProperty("secondProperty");
    public ReadOnlyProperty<Integer> secondProperty =
            secondReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty millisecondReadWriteProperty =
            new IntegerProperty("millisecondProperty");
    public ReadOnlyProperty<Integer> millisecondProperty =
            millisecondReadWriteProperty.asReadOnlyProperty();
    private final IntegerProperty dayOfWeekReadWriteProperty =
            new IntegerProperty("dayOfWeek");
    public ReadOnlyProperty<Integer> dayOfWeek =
            dayOfWeekReadWriteProperty.asReadOnlyProperty();
    //private long lastUpdateNanoTime = 0l;

    public Time() {
        this.setDaemon(true);
        start();
    }

    public void run() {

        while (true) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            yearReadWriteProperty.setValue(cal.get(Calendar.YEAR));
            monthReadWriteProperty.setValue(cal.get(Calendar.MONTH) + 1);
            dayReadWriteProperty.setValue(cal.get(Calendar.DAY_OF_MONTH));
            hourReadWriteProperty.setValue(cal.get(Calendar.HOUR_OF_DAY));
            minuteReadWriteProperty.setValue(cal.get(Calendar.MINUTE));
            secondReadWriteProperty.setValue(cal.get(Calendar.SECOND));
            millisecondReadWriteProperty
                    .setValue(cal.get(Calendar.MILLISECOND));
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            dayOfWeekReadWriteProperty
                    .setValue(dayOfWeek == 1 ? 7 : dayOfWeek - 1);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public void writeString() {
        System.out.println(
                yearProperty.getValue() + " " +
                monthProperty.getValue() + " " +
                dayProperty.getValue() + " " +
                hourProperty.getValue() + " " +
                minuteProperty.getValue() + " " +
                secondProperty.getValue() + " " +
                millisecondProperty.getValue() + " " +
                dayOfWeek.getValue() + " "

        );
    }
}