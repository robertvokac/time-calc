package com.robertvokac.utils.timecalc.swing.progress;

import com.robertvokac.utils.timecalc.utils.property.BooleanProperty;
import com.robertvokac.utils.timecalc.utils.property.IntegerProperty;
import com.robertvokac.utils.timecalc.utils.property.ReadOnlyProperty;

import java.util.Calendar;
import java.util.Date;

public class Time extends Thread {

    private final IntegerProperty yearReadWriteProperty
            = new IntegerProperty("yearProperty");
    private final IntegerProperty monthReadWriteProperty
            = new IntegerProperty("monthProperty");
    private final IntegerProperty dayReadWriteProperty
            = new IntegerProperty("dayProperty");
    private final IntegerProperty hourReadWriteProperty
            = new IntegerProperty("hourProperty");
    private final IntegerProperty minuteReadWriteProperty
            = new IntegerProperty("minuteProperty");
    private final IntegerProperty secondReadWriteProperty
            = new IntegerProperty("secondProperty");
    private final IntegerProperty millisecondReadWriteProperty
            = new IntegerProperty("millisecondProperty");
    private final IntegerProperty dayOfWeekReadWriteProperty
            = new IntegerProperty("dayOfWeek");
    public ReadOnlyProperty<Integer> yearProperty
            = yearReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> monthProperty
            = monthReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> dayProperty
            = dayReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> hourProperty
            = hourReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> minuteProperty
            = minuteReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> secondProperty
            = secondReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> millisecondProperty
            = millisecondReadWriteProperty.asReadOnlyProperty();
    public ReadOnlyProperty<Integer> dayOfWeekProperty
            = dayOfWeekReadWriteProperty.asReadOnlyProperty();
    public final IntegerProperty yearCustomProperty
            = new IntegerProperty("yearCustomProperty", Integer.MAX_VALUE);
    public final IntegerProperty monthCustomProperty
            = new IntegerProperty("monthCustomProperty", Integer.MAX_VALUE);
    public final IntegerProperty dayCustomProperty
            = new IntegerProperty("dayCustomProperty", Integer.MAX_VALUE);
    public final IntegerProperty hourCustomProperty
            = new IntegerProperty("hourCustomProperty", Integer.MAX_VALUE);
    public final IntegerProperty minuteCustomProperty
            = new IntegerProperty("minuteCustomProperty", Integer.MAX_VALUE);
    public final IntegerProperty secondCustomProperty
            = new IntegerProperty("secondCustomProperty", Integer.MAX_VALUE);
    public final IntegerProperty millisecondCustomProperty
            = new IntegerProperty("millisecondCustomProperty", Integer.MAX_VALUE);
    public final BooleanProperty allowCustomValuesProperty
            = new BooleanProperty("allowCustomValuesProperty", false);
    //private long lastUpdateNanoTime = 0l;

    public Time() {
        this.setDaemon(true);
        start();
    }

    public Calendar asCalendar() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, returnValueAsNeeded(yearProperty, yearCustomProperty, Calendar.YEAR));
        cal.set(Calendar.MONTH, returnValueAsNeeded(monthProperty, monthCustomProperty, Calendar.MONTH) - 1);
        cal.set(Calendar.DAY_OF_MONTH, returnValueAsNeeded(dayProperty, dayCustomProperty, Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, returnValueAsNeeded(hourProperty, hourCustomProperty, Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, returnValueAsNeeded(minuteProperty, minuteCustomProperty, Calendar.MINUTE));
        cal.set(Calendar.SECOND, returnValueAsNeeded(secondProperty, secondCustomProperty, Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, returnValueAsNeeded(millisecondProperty, millisecondCustomProperty, Calendar.MILLISECOND));
        return cal;
    }
       private int returnValueAsNeeded(ReadOnlyProperty<Integer> real, IntegerProperty custom, int timeUnit) {
        return returnValueAsNeeded(real.getValue(), custom.getValue(), timeUnit);
    }

    private int returnValueAsNeeded(int real, int custom, int timeUnit) {
        if (this.allowCustomValuesProperty.isDisabled()) {
            return real;
        }

        int month = monthProperty.getValue();

        if (timeUnit == Calendar.DAY_OF_MONTH && (month == 2 || month == 4
                                                  || month == 6 || month == 9
                                                  || month == 11)) {
            if (month == 2) {
                boolean leapYear = this.yearProperty.getValue() % 4 == 0;
                if (custom > (leapYear ? 29 : 28)) {
                    custom = (leapYear ? 29 : 28);
                }
            } else {
                if (custom > 30) {
                    custom = 30;
                }
            }
        }
        return custom == Integer.MAX_VALUE ? real : custom;
    }

    public void run() {

        while (true) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());

            yearReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.YEAR, yearCustomProperty, yearProperty));
            monthReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.MONTH, monthCustomProperty, monthProperty));
            dayReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.DAY_OF_MONTH, dayCustomProperty, dayProperty));
            hourReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.HOUR_OF_DAY, hourCustomProperty, hourProperty));
            minuteReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.MINUTE, minuteCustomProperty, minuteProperty));
            secondReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.SECOND, secondCustomProperty, secondProperty));
            millisecondReadWriteProperty.setValue(returnCustomValueIfNeeded(cal, Calendar.MILLISECOND, millisecondCustomProperty, millisecondProperty));

            boolean customDayOfWeek = yearCustomProperty.getValue() != Integer.MAX_VALUE || monthCustomProperty.getValue() != Integer.MAX_VALUE || dayCustomProperty.getValue() != Integer.MAX_VALUE;

            int dayOfWeek = customDayOfWeek ? asCalendar().get(Calendar.DAY_OF_WEEK) : cal.get(Calendar.DAY_OF_WEEK);
            dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
            dayOfWeekReadWriteProperty
                    .setValue(dayOfWeek);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public void writeString() {
        System.out.println(
                yearProperty.getValue() + " "
                + monthProperty.getValue() + " "
                + dayProperty.getValue() + " "
                + hourProperty.getValue() + " "
                + minuteProperty.getValue() + " "
                + secondProperty.getValue() + " "
                + millisecondProperty.getValue() + " "
                + dayOfWeekProperty.getValue() + " "
        );
    }
//    private int returnCustomValueIfNeeded(Calendar cal, int timeUnit,ReadOnlyProperty<Integer> real,IntegerProperty custom) {
//        return returnCustomValueIfNeeded(cal, timeUnit, real, custom);
//    }
    private int returnCustomValueIfNeeded(Calendar cal, int timeUnit,
            IntegerProperty custom,
            ReadOnlyProperty<Integer> real) {
        boolean allow = allowCustomValuesProperty.isEnabled();
        Integer customValue = custom.getValue();

        int month = monthProperty.getValue();
        if (allow && customValue != Integer.MAX_VALUE) {
            if(timeUnit == Calendar.DAY_OF_MONTH && (month == 2 || month ==4 || month == 6 || month == 9 || month == 11)) {
                if(cal.getActualMaximum(Calendar.DAY_OF_MONTH) < custom.getValue()) {
                    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
            }
            return custom.getValue();
        } else {
            return timeUnit == Calendar.MONTH ? (cal.get(timeUnit) + 1) : cal.get(timeUnit);
        }

    }
}
