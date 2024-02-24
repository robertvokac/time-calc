package org.nanoboot.utils.timecalc.swing.progress;

import java.util.Calendar;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class YearBattery extends Battery {
    public YearBattery(int x, int i, int i1) {
        super("Year", x, i, i1);
    }

    public static double getYearProgress(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Integer millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);

        double seconds = second + millisecond / 1000d;
        double minutes = minute + seconds / 60d;
        double hours = hour + minutes / 60d;
        double days = cal.get(Calendar.DAY_OF_YEAR) + hours / 24d;
//        System.out.println("millisecond=" + millisecond);
//        System.out.println("seconds=" + seconds);
//        System.out.println("minutes=" + minutes);
//        System.out.println("hours=" + hours);
//        System.out.println("days=" + days);
//        System.out.println("cal.get(Calendar.DAY_OF_YEAR)=" + cal.get(Calendar.DAY_OF_YEAR));

        double totalCountOfDaysInAYear = getTotalCountOfDaysInAYear(year);
        return days / totalCountOfDaysInAYear;
    }

    private static double getTotalCountOfDaysInAYear(Integer year) {
        boolean leapYear = isLeapYear(year);
        double daysInYear = 365d;
        if(leapYear) {
            daysInYear++;
        }
        return daysInYear;
    }

    private static boolean isLeapYear(Integer year) {
        return year % 4 == 0;
    }

    public static double getYearProgress(AnalogClock analogClock) {
        return getYearProgress(
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
