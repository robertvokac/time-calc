package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.ReadOnlyProperty;

import javax.swing.Timer;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

public class Time extends Thread {

    private IntegerProperty yearReadWriteProperty = new IntegerProperty("yearProperty");
    private IntegerProperty monthReadWriteProperty = new IntegerProperty("monthProperty");
    private IntegerProperty dayReadWriteProperty = new IntegerProperty("dayProperty");
    private IntegerProperty hourReadWriteProperty = new IntegerProperty("hourProperty");
    private IntegerProperty minuteReadWriteProperty = new IntegerProperty("minuteProperty");
    private IntegerProperty secondReadWriteProperty = new IntegerProperty("secondProperty");
    private IntegerProperty millisecondReadWriteProperty = new IntegerProperty("millisecondProperty");
    private IntegerProperty dayOfWeekReadWriteProperty = new IntegerProperty("dayOfWeek");
    public ReadOnlyProperty<Integer> yearProperty = yearReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> monthProperty = monthReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> dayProperty = dayReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> hourProperty = hourReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> minuteProperty = minuteReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> secondProperty = secondReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> millisecondProperty = millisecondReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> dayOfWeek = dayOfWeekReadWriteProperty.asReadOnlyProperty();
    //private long lastUpdateNanoTime = 0l;

    public Time() {
        this.setDaemon(true);
        start();
    }
    public void run() {

        while(true) {
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
                Thread.sleep(1);
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