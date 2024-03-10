package org.nanoboot.utils.timecalc.utils.common;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class TTime implements Comparable<TTime> {

    public static final int MINUTES_PER_HOUR = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    @Getter
    @Setter
    private boolean negative;

    @Getter
    @Setter
    private Integer hour;
    @Getter
    @Setter
    private Integer minute;
    @Getter
    @Setter
    private Integer second;
    @Getter
    @Setter
    private Integer millisecond;

    public TTime(long hour, long minute, long second, long millisecond) {
        this((int) hour, (int) minute, (int) second, (int) millisecond);
    }

    public TTime(int hour, int minute) {
        this(hour, minute, 0, 0);
    }

    public static TTime computeTimeDiff(TTime tTime1, TTime tTime2) {
        return tTime2.remove(tTime1);
    }

    public static String printDuration(Duration duration) {
        long hour = duration.get(ChronoUnit.HOURS);
        long minute = duration.get(ChronoUnit.MINUTES);
        long second = duration.get(ChronoUnit.SECONDS);
        long millisecond = duration.get(ChronoUnit.MILLIS);
        StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(minute).append(":");
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute).append(":");
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second).append(":");
        if (millisecond < 10) {
            sb.append("00");
        } else {
            if (millisecond < 100) {
                sb.append("0");
            }
        }
        return sb.toString();

    }

    public static TTime of(Calendar cal) {
        return new TTime(
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.MILLISECOND));
    }

    public static TTime of(Duration duration) {
        return new TTime(
                duration.get(ChronoUnit.HOURS),
                duration.get(ChronoUnit.MINUTES),
                duration.get(ChronoUnit.SECONDS),
                duration.get(ChronoUnit.MILLIS));
    }

    public TTime(String string) {
        this.negative = string.startsWith("-");
        if (negative) {
            string = string.replace("-", "");
        }
        String[] array = string.split(":");
        this.hour = (negative ? (1) : 1) * Integer.valueOf(array[0]);
        this.minute = (negative ? (1) : 1) * Integer.valueOf(array[1]);
        this.second = array.length >= 3 ? ((negative ? (1) : 1) * Integer.valueOf(array[2])) : 0;
        this.millisecond = array.length >= 4 ? ((negative ? (1) : 1) * Integer.valueOf(array[3])) : 0;
    }

    public TTime(int hourIn, int minuteIn, int secondIn, int millisecondIn) {
        this(hourIn < 0 || minuteIn < 0 || secondIn < 0 || millisecondIn < 0, Math.abs(hourIn), Math.abs(minuteIn), Math.abs(secondIn), Math.abs(millisecondIn));
    }

    public TTime(boolean negative, int hourIn, int minuteIn, int secondIn, int millisecondIn) {
        this.hour = Math.abs(hourIn);
        this.minute = Math.abs(minuteIn);
        this.second = Math.abs(secondIn);
        this.millisecond = Math.abs(millisecondIn);
        this.negative = negative;
        while (minute >= MINUTES_PER_HOUR) {
            minute = minute - MINUTES_PER_HOUR;
            hour = hour + 1;
        }
        if (minute < 0) {
            minute = minute + 60;
            hour = hour - 1;
        }
    }

    public TTime remove(TTime tTimeToBeRemoved) {
        int s1 = this.toTotalMilliseconds();
        int s2 = tTimeToBeRemoved.toTotalMilliseconds();
        int s = s1 - s2;
        TTime result = TTime.ofMilliseconds(s);
        return result;
    }
    public static TTime ofMinutes(int s) {
        return ofMilliseconds(s * 1000 * 60);
    }
    public static TTime ofSeconds(int s) {
        return ofMilliseconds(s * 1000);
    }
    public static TTime ofMilliseconds(int s) {
        int hours = s / 60 / 60 / 1000;
        int milliseconds = s - hours * 60 * 60 * 1000;
        int minutes = milliseconds / 60 / 1000;
        milliseconds = milliseconds - minutes * 60 * 1000;
        int seconds = milliseconds / 1000;
        milliseconds = milliseconds - seconds * 1000;
        return new TTime(s < 0, Math.abs(hours), Math.abs(minutes), Math.abs(seconds), Math.abs(milliseconds));

    }

    public TTime add(TTime tTimeToBeAdded) {
        TTime time1 = this;
        TTime time2 = tTimeToBeAdded;
        int totalMilliseconds1 = time1.toTotalMilliseconds();
        int totalMilliseconds2 = time2.toTotalMilliseconds();
        int result = totalMilliseconds1 + totalMilliseconds2;
        return TTime.ofMilliseconds(result);

    }

    public static int countDiffInMinutes(TTime startTime, TTime endTime) {
        return (endTime.getHour() * TTime.MINUTES_PER_HOUR + endTime
                .getMinute()) - (startTime.getHour() * TTime.MINUTES_PER_HOUR
                + startTime.getMinute());
    }

    public TTime cloneInstance() {
        return new TTime(negative, hour, minute, second, millisecond);
    }

    public String toString() {
        return (negative ? "-" : "") + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second + ":" + (millisecond < 10 ? "00" : (millisecond < 100 ? "0" : "")) + millisecond;
    }

    public Calendar asCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        return cal;
    }

    public Date asDate() {
        return asCalendar().getTime();
    }

    public int toTotalMilliseconds() {
        return ((negative ? (-1) : 1)) * (hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000 + millisecond);
    }

    @Override
    public int compareTo(TTime o) {
        int result = Integer.valueOf(toTotalMilliseconds()).compareTo(o.toTotalMilliseconds());
        if (this.isNegative()) {
            System.out.println("this.toTotalMilliseconds()=" + this.toTotalMilliseconds());
            System.out.println("o.toTotalMilliseconds()=" + o.toTotalMilliseconds());
            System.out.println("comparing: " + this + " " + o + " = " + result);
        }
        return result;
    }
}
