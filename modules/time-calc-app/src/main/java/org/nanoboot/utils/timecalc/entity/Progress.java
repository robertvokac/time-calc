package org.nanoboot.utils.timecalc.entity;

import lombok.Getter;
import lombok.Setter;
import org.nanoboot.utils.timecalc.swing.progress.AnalogClock;
import org.nanoboot.utils.timecalc.utils.common.TTime;

import java.util.Calendar;
import java.util.Date;

/**
 * @author pc00289
 * @since 21.03.2024
 */
public class Progress {
    private final double[] array = new double[6];
    @Getter
    @Setter
    private int workDaysInMonth;

    @Getter
    @Setter
    private boolean weekend;

    public void set(WidgetType type, double value) {
        array[type.getIndex()] = value > 1 ? 1 : (value < 0 ? 0 : value);
    }

    public static WidgetType getWidgetType(WidgetType type) {
        if (type == WidgetType.PRESENTATION) {

            long currentTime = new Date().getTime() / 1000l;
            long l = currentTime % 30;
            if (l >= 0 && l < 5) {
                type = WidgetType.MINUTE;
            }
            if (l >= 5 && l < 10) {
                type = WidgetType.HOUR;
            }
            if (l >= 10 && l < 15) {
                type = WidgetType.DAY;
            }
            if (l >= 15 && l < 20) {
                type = WidgetType.WEEK;
            }
            if (l >= 20 && l < 25) {
                type = WidgetType.MONTH;
            }
            if (l >= 25 && l < 30) {
                type = WidgetType.YEAR;
            }

        }
        return type;
    }

    public double getDonePercent(WidgetType type) {
        type = getWidgetType(type);

        return array[type.getIndex()];
    }

    public static double getMinuteProgress(int secondNow, int millisecondNow) {
        return millisecondNow / 60d / 1000d + secondNow / 60d;
    }

    public static double getHourProgress(TTime timeRemains, int secondsRemains,
            int millisecondsRemains) {
        if (secondsRemains < 0 || millisecondsRemains < 0
            || timeRemains.getHour() < 0 || timeRemains.getMinute() < 0) {
            return 1;
        }
        double minutesRemainsD = timeRemains.getMinute();
        double secondsRemainsD = secondsRemains;
        double millisecondsRemainsD = millisecondsRemains;
        minutesRemainsD = minutesRemainsD + secondsRemainsD / 60d;
        minutesRemainsD
                = minutesRemainsD + millisecondsRemainsD / 1000d / 60d;
        if (secondsRemainsD > 0) {
            minutesRemainsD = minutesRemainsD - 1d;
        }
        if (millisecondsRemainsD > 0) {
            minutesRemainsD = minutesRemainsD - 1d / 1000d;
        }
        double hourProgress = 1 - ((minutesRemainsD % 60d) / 60d);
        return hourProgress;
    }

    public static double getWeekProgress(int weekDayWhenMondayIsOne,
            double done) {
        if (done > 1) {
            done = 1;
        }
        return weekDayWhenMondayIsOne == 0
               || weekDayWhenMondayIsOne == 6
                ? 100 : ((weekDayWhenMondayIsOne - 1) * 0.20 + done * 0.20);
    }

    public static double getMonthProgress(int weekDayWhenMondayIsOne,
            int workDaysDone, int workDaysTotal, double done) {

        if (done > 1) {
            done = 1;
        }
        double result = weekDayWhenMondayIsOne == 6
                        || weekDayWhenMondayIsOne == 7
                ? (double) workDaysDone / workDaysTotal
                : (workDaysDone + done) / workDaysTotal;
        //        System.out.println("result=" + result);
        return result;
    }

    public static double getYearProgress(double monthProgress, Integer year,
            Integer month,
            Integer day, Integer hour, Integer minute, Integer second,
            Integer millisecond) {
        double totalCountOfDaysInAYear = getTotalCountOfDaysInAYear(year);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (month == 1) {
            return (daysInMonth * monthProgress) / totalCountOfDaysInAYear;
        } else {
            cal.set(Calendar.MONTH, month - 2);
            cal.set(Calendar.DAY_OF_MONTH,
                    cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            int totalDaysUntilLastDayOfLastMonth =
                    cal.get(Calendar.DAY_OF_YEAR);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);
            return (totalDaysUntilLastDayOfLastMonth + (daysInMonth
                                                        * monthProgress))
                   / totalCountOfDaysInAYear;
        }
    }

    //    public static double getYearProgress(Integer year, Integer month,
    //            Integer day, Integer hour, Integer minute, Integer second,
    //            Integer millisecond) {
    //        Calendar cal = Calendar.getInstance();
    //        cal.set(Calendar.YEAR, year);
    //        cal.set(Calendar.MONTH, month - 1);
    //        cal.set(Calendar.DAY_OF_MONTH, day);
    //        cal.set(Calendar.HOUR, hour);
    //        cal.set(Calendar.MINUTE, minute);
    //        cal.set(Calendar.SECOND, second);
    //        cal.set(Calendar.MILLISECOND, millisecond);
    //
    //        double seconds = second + millisecond / 1000d;
    //        double minutes = minute + seconds / 60d;
    //        double hours = hour + minutes / 60d;
    //        double days = cal.get(Calendar.DAY_OF_YEAR) + hours / 24d;
    //        //        System.out.println("millisecond=" + millisecond);
    //        //        System.out.println("seconds=" + seconds);
    //        //        System.out.println("minutes=" + minutes);
    //        //        System.out.println("hours=" + hours);
    //        //        System.out.println("days=" + days);
    //        //        System.out.println("cal.get(Calendar.DAY_OF_YEAR)=" + cal.get(Calendar.DAY_OF_YEAR));
    //
    //        double totalCountOfDaysInAYear = getTotalCountOfDaysInAYear(year);
    //        return days / totalCountOfDaysInAYear;
    //    }

    private static double getTotalCountOfDaysInAYear(Integer year) {
        boolean leapYear = isLeapYear(year);
        double daysInYear = 365d;
        if (leapYear) {
            daysInYear++;
        }
        return daysInYear;
    }

    private static boolean isLeapYear(Integer year) {
        return year % 4 == 0;
    }

    public static double getYearProgress(AnalogClock analogClock,
            double monthProgress) {
        return getYearProgress(monthProgress,
                analogClock.yearProperty.getValue(),
                analogClock.monthProperty.getValue(),
                analogClock.dayProperty.getValue(),
                analogClock.hourProperty.getValue(),
                analogClock.minuteProperty.getValue(),
                analogClock.secondProperty.getValue(),
                analogClock.millisecondProperty.getValue()
        );
    }

}
