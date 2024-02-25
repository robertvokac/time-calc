package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.TimeHM;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.InvalidationListener;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Calendar;
import java.util.Date;

//https://kodejava.org/how-do-i-write-a-simple-analog-clock-using-java-2d/
public class AnalogClock extends Widget {

    public static final Color COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED =
            new Color(246,
                    152, 51);
    public IntegerProperty startHourProperty =
            new IntegerProperty("startHourProperty");
    public IntegerProperty startMinuteProperty =
            new IntegerProperty("startMinuteProperty");
    public IntegerProperty endHourProperty =
            new IntegerProperty("endHourProperty");
    public IntegerProperty endMinuteProperty =
            new IntegerProperty("endMinuteProperty");
    public IntegerProperty yearProperty = new IntegerProperty("yearProperty");
    public IntegerProperty monthProperty = new IntegerProperty("monthProperty");
    public IntegerProperty dayProperty = new IntegerProperty("dayProperty");
    public IntegerProperty hourProperty = new IntegerProperty("hourProperty");
    public IntegerProperty minuteProperty =
            new IntegerProperty("minuteProperty");
    public IntegerProperty secondProperty =
            new IntegerProperty("secondProperty");
    public IntegerProperty millisecondProperty =
            new IntegerProperty("millisecondProperty");
    public IntegerProperty dayOfWeekProperty =
            new IntegerProperty("dayOfWeekProperty");
    public BooleanProperty minuteEnabledProperty =
            new BooleanProperty("minuteEnabledProperty", true);
    public BooleanProperty secondEnabledProperty =
            new BooleanProperty("secondEnabledProperty", true);
    public BooleanProperty millisecondEnabledProperty =
            new BooleanProperty("millisecondEnabledProperty", false);
    public BooleanProperty handsLongProperty =
            new BooleanProperty("handsLongProperty", true);
    public BooleanProperty handsBlackProperty =
            new BooleanProperty("handsBlackProperty", false);
    public final BooleanProperty borderVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_VISIBLE
                    .getKey());
    public final BooleanProperty borderOnlyHoursProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_ONLY_HOURS
                    .getKey());
    public final BooleanProperty numbersVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_NUMBERS_VISIBLE
                    .getKey());
    public final BooleanProperty circleVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_CIRCLE_VISIBLE
                    .getKey());
    public final BooleanProperty circleStrongBorderProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_CIRCLE_STRONG_BORDER
                    .getKey());

    public final BooleanProperty centreCircleVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_VISIBLE
                    .getKey());
    public final StringProperty centreCircleBorderColorProperty =
            new StringProperty(TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR
                    .getKey());
    public final BooleanProperty centreCircleBlackProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_BLACK
                    .getKey());
    private TimeHM startTime;
    private final TimeHM endTime;
    private int startAngle;
    private final int endAngle;
    private Color customCircleColor = null;

    public AnalogClock(TimeHM startTimeIn,
            TimeHM endTimeIn) {

        this.endTime = endTimeIn.cloneInstance();
        this.endAngle =
                (int) ((endTime.getHour() + endTime.getMinute() / 60d) / 12d
                       * 360d);
        if (endTime.getHour() > 12) {
            endTime.setHour(endTime.getHour() - 12);
        }
        this.startTime = startTimeIn.cloneInstance();
        this.startAngle =
                (int) ((startTime.getHour() + startTime.getMinute() / 60d) / 12d
                       * 360d);

        setPreferredSize(new Dimension(200, 200));

        centreCircleBorderColorProperty.addListener(property ->
                customCircleColor = SwingUtils.getColorFromString(centreCircleBorderColorProperty.getValue()));
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Analog Clock");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AnalogClock clock =
                new AnalogClock(new TimeHM("6:30"), new TimeHM("19:00"));
        window.add(clock);
        window.pack();
        window.setVisible(true);
        //        window.addKeyListener(new KeyAdapter() {
        //            // Key Pressed method
        //            public void keyPressed(KeyEvent e) {
        //                if (e.getKeyCode() == KeyEvent.VK_UP) {
        //                    clock.startAngle_++;
        //                }
        //                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        //                    clock.startAngle_--;
        //                }
        //
        //                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        //                    clock.arcAngle_++;
        //                }
        //                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        //                    clock.arcAngle_--;
        //                }
        //            }
        //        });
    }

    @Override
    public void paintWidget(Graphics g) {

        //System.out.println("clock.handsLongProperty=" + handsLongProperty.isEnabled());
        Visibility visibility =
                Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.side = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int millisecond = millisecondProperty.getValue();
        int second = secondProperty.getValue();
        int minute = minuteProperty.getValue();
        int hour = hourProperty.getValue();

        if(customCircleColor == null) {
            customCircleColor = SwingUtils.getColorFromString(centreCircleBorderColorProperty.getValue());
        }
        if (mouseOver && visibility.isStronglyColored()) {
            this.startTime = new TimeHM(hour, minute);
            this.startAngle =
                    (int) ((startTime.getHour() + startTime.getMinute() / 60d)
                           / 12d * 360d);

            Color currentColor = g2d.getColor();
            g2d.setColor(Color.YELLOW);
            g2d.fillArc(0, 0, side, side, -startAngle + 90,
                    startAngle - endAngle);

            //System.out.println("ANGLES: " + startAngle + " " + endAngle + " " + angleDiff );

            g2d.setColor(currentColor);
        }

        //
        if (millisecondEnabledProperty.isEnabled() && secondEnabledProperty.isEnabled() && minuteEnabledProperty.isEnabled()) {
            drawHand(g2d, side / 2 - 10, millisecond / 1000.0, 1.0f,
                    COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED, visibility);

            if (handsLongProperty.isEnabled()) {
                drawHand(g2d, (side / 2 - 10) / 4,
                        (millisecond > 500 ? millisecond - 500 :
                                millisecond + 500) / 1000.0, 1.0f,
                        COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED,
                        visibility);
            }
        }

        if (secondEnabledProperty.isEnabled() && minuteEnabledProperty.isEnabled()) {
            drawHand(g2d, side / 2 - 10, second / 60.0, 0.5f, Color.RED,
                    visibility);

            if (handsLongProperty.isEnabled()) {
                drawHand(g2d, (side / 2 - 10) / 4,
                        (second > 30 ? second - 30 : second + 30) / 60.0, 0.5f,
                        Color.RED, visibility);
            }
        }
        if (minuteEnabledProperty.isEnabled()) {
            double minutes = minute / 60.0 + second / 60.0 / 60.0;
            drawHand(g2d, side / 2 - 20, minutes, 2.0f,
                    Color.BLUE, visibility);
            if (handsLongProperty.isEnabled()) {
                drawHand(g2d, (side / 2 - 20) / 4,
                        minutes + minutes > 0.5 ? minutes - 0.5 :
                                minutes + (minutes > 0.5 ? (-1) : 1) * 0.5,
                        2.0f,
                        Color.BLUE, visibility);
            }
        }
        double hours = hour / 12.0 + minute / 60.0 / 12 + second / 60 / 60 / 12;
        drawHand(g2d, side / 2 - 40,
                hours, 4.0f,
                Color.BLACK, visibility);
        if (handsLongProperty.isEnabled()) {
            drawHand(g2d, (side / 2 - 40) / 4,
                    hours + hours > 0.5 ? hours - 0.5 :
                            hours + (hours > 0.5 ? (-1) : 1) * 0.5, 4.0f,
                    Color.BLACK, visibility);
        }
        if(borderVisibleProperty.isEnabled()) {
            for (int minuteI = 0; minuteI < 60; minuteI++) {
                if(borderOnlyHoursProperty.isEnabled() && minuteI %5 != 0) {
                    continue;
                }
                drawBorder(g2d, minuteI, minuteI % 5 == 0 ? (numbersVisibleProperty.isEnabled() ? 2f : 4f) : 1f,
                        Color.BLACK, visibility);
            }
        }
        if(centreCircleVisibleProperty.isEnabled()) {
            drawCentreCircle(g2d, centerX, centerY);
        }

        drawClockFace(g2d, centerX, centerY, side / 2 - 40, visibility);

    }

    private void drawCentreCircle(Graphics2D brush, int centerX, int centerY) {
        Color currentColor = brush.getColor();
        Visibility visibility =
                Visibility.valueOf(visibilityProperty.getValue());
        brush.setColor(visibility.isStronglyColored() || mouseOver ? (centreCircleBlackProperty.isEnabled() ? Color.BLACK : Color.RED) :
                FOREGROUND_COLOR);
        brush.fillOval(centerX - 3, centerY - 3, 8, 8);
        brush.setColor(currentColor);
    }

    private void drawBorder(Graphics2D brush, int forMinute,
            float stroke, Color color, Visibility visibility) {
        double value = ((double)forMinute) / 60d;
        boolean hourAngle = forMinute % 5 == 0;
        int length = side / (numbersVisibleProperty.isEnabled() ? 18 : (hourAngle ? 12 : 18));
        double angle = Math.PI * 2 * (value - 0.25);
        int startX = (int) (getWidth() / 2 + (side/2 - length) * Math.cos(angle));
        int startY = (int) (getHeight() / 2 + (side/2 - length) * Math.sin(angle));
        int endX = (int) (getWidth() / 2 + (side/2 - length * 0.50d * (hourAngle ? 0.25 : 1)) * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + (side/2 - length * 0.50d * (hourAngle ? 0.25 : 1)) * Math.sin(angle));

        brush.setColor((visibility.isStronglyColored() || mouseOver) ? color :
                FOREGROUND_COLOR);
        brush.setStroke(new BasicStroke(stroke));
        brush.drawLine(startX, startY, endX, endY);
    }
    private void drawHand(Graphics2D brush, int length, double value,
            float stroke, Color color, Visibility visibility) {
        length = length - 4;
        double angle = Math.PI * 2 * (value - 0.25);
        int endX = (int) (getWidth() / 2 + length * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + length * Math.sin(angle));

        brush.setColor((visibility.isStronglyColored() || mouseOver) ? (handsBlackProperty.isEnabled() ? Color.BLACK : color) :
                FOREGROUND_COLOR);
        brush.setStroke(new BasicStroke(stroke));
        brush.drawLine(getWidth() / 2, getHeight() / 2, endX, endY);
    }

    private void drawClockFace(Graphics2D brush, int centerX, int centerY,
            int radius, Visibility visibility) {
        brush.setStroke(new BasicStroke(2.0f));
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK :
                FOREGROUND_COLOR);
        if(circleVisibleProperty.isEnabled()) {
            Color currentColor = brush.getColor();
            if(visibility.isStronglyColored() || mouseOver) {
                brush.setColor(customCircleColor);
            }
            brush.drawOval(1, 1, centerX * 2 - 3, centerY * 2 - 3);
            brush.drawOval(2, 2, centerX * 2 - 3, centerY * 2 - 3);
            if(circleStrongBorderProperty.isEnabled()) {
                        brush.drawOval(3, 3, centerX * 2 - 6, centerY * 2 - 6);
                        brush.drawOval(4, 4, centerX * 2 - 8, centerY * 2 - 8);
                brush.drawOval(5, 5, centerX * 2 - 10, centerY * 2 - 10);
                brush.drawOval(6, 6, centerX * 2 - 12, centerY * 2 - 12);
            }
            brush.setColor(currentColor);
        }

        if (this.mouseOver) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, yearProperty.getValue());
            cal.set(Calendar.MONTH, monthProperty.getValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, dayProperty.getValue());
            Date date = cal.getTime();
            brush.drawString(DateFormats.DATE_TIME_FORMATTER_LONG.format(date),
                    ((int) (side * 0.25)),
                    ((int) (side * 0.35)));
            brush.drawString(DateFormats.DATE_TIME_FORMATTER_TIME.format(date),
                    ((int) (side * 0.25) + 30),
                    ((int) (side * 0.35)) + 60);
        }
        if(numbersVisibleProperty.isEnabled()) {
            for (int i = 1; i <= 12; i++) {
                double angle = Math.PI * 2 * (i / 12.0 - 0.25);
                int dx = centerX + (int) ((radius + 20) * Math.cos(angle)) - 4;
                int dy = centerY + (int) ((radius + 20) * Math.sin(angle)) + 4;

                brush.setFont(new Font("sans", Font.BOLD, 16));
                brush.drawString(Integer.toString(i), dx + (i == 12 ? -3 : 0),
                        dy + (i == 12 ? +3 : 0));
            }
        }
    }

    public int getTimerDelay() {
        return 20;
    }

}