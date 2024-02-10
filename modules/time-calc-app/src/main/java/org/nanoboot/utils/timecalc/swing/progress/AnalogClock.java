package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.app.TimeCalcConf;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.TimeHM;

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
import java.util.GregorianCalendar;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;

//https://kodejava.org/how-do-i-write-a-simple-analog-clock-using-java-2d/
public class AnalogClock extends Widget {

    public static final Color COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED = new Color(246,
            152, 51);
    private TimeHM startTime;
    private TimeHM endTime;
    private int startAngle;
    private int endAngle;
    
    public IntegerProperty startHourProperty = new IntegerProperty("startHourProperty");
    public IntegerProperty startMinuteProperty = new IntegerProperty("startMinuteProperty");
    public IntegerProperty endHourProperty = new IntegerProperty("endHourProperty");
    public IntegerProperty endMinuteProperty = new IntegerProperty("endMinuteProperty");
    public IntegerProperty yearProperty = new IntegerProperty("yearProperty");
    public IntegerProperty monthProperty = new IntegerProperty("monthProperty");
    public IntegerProperty dayProperty = new IntegerProperty("dayProperty");
    public IntegerProperty hourProperty = new IntegerProperty("hourProperty");
    public IntegerProperty minuteProperty = new IntegerProperty("minuteProperty");
    public IntegerProperty secondProperty = new IntegerProperty("secondProperty");
    public IntegerProperty millisecondProperty = new IntegerProperty("millisecondProperty");
    public IntegerProperty dayOfWeekProperty = new IntegerProperty("dayOfWeek");

    public AnalogClock(TimeHM startTimeIn,
            TimeHM endTimeIn) {

        this.endTime = endTimeIn.cloneInstance();
        this.endAngle =
                (int) ((endTime.getHour() + endTime.getMinute() / 60d) / 12d * 360d);
        if(endTime.getHour() >12) {
            endTime.setHour(endTime.getHour() - 12);
        }
        this.startTime = startTimeIn.cloneInstance();
        this.startAngle =
                (int) ((startTime.getHour() + startTime.getMinute() / 60d) / 12d * 360d);



        setPreferredSize(new Dimension(200, 200));

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

        Visibility visibility = Visibility.valueOf(visibilityProperty.getValue());
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

        if(mouseOver && visibility.isStronglyColored()) {
        this.startTime = new TimeHM(hour, minute);
        this.startAngle =
                (int) ((startTime.getHour() + startTime.getMinute() / 60d) / 12d * 360d);

            Color currentColor = g2d.getColor();
            g2d.setColor(Color.YELLOW);
            g2d.fillArc(0, 0, side, side, -startAngle +90, startAngle - endAngle);

        //System.out.println("ANGLES: " + startAngle + " " + endAngle + " " + angleDiff );

            g2d.setColor(currentColor);
        }

        // Draw clock numbers and circle
        drawClockFace(g2d, centerX, centerY, side / 2 - 40, visibility);

        //
        drawHand(g2d, side / 2 - 10, millisecond / 1000.0, 1.0f,
                COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED, visibility);

        if (TimeCalcConf.getInstance().areClockHandsLong()) {
            drawHand(g2d, (side / 2 - 10) / 4,
                    (millisecond > 500 ? millisecond - 500 : millisecond + 500) / 1000.0, 1.0f,
                    COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED, visibility);
        }
        //
        drawHand(g2d, side / 2 - 10, second / 60.0, 0.5f, Color.RED, visibility);

        if (TimeCalcConf.getInstance().areClockHandsLong()) {
            drawHand(g2d, (side / 2 - 10) / 4,
                    (second > 30 ? second - 30 : second + 30) / 60.0, 0.5f,
                    Color.RED, visibility);
        }
        //
        double minutes = minute / 60.0 + second / 60.0 / 60.0;
        drawHand(g2d, side / 2 - 20, minutes, 2.0f,
                Color.BLUE, visibility);
        if (TimeCalcConf.getInstance().areClockHandsLong()) {
            drawHand(g2d, (side / 2 - 20) / 4,
                    minutes + minutes > 0.5 ? minutes - 0.5 :
                            minutes + (minutes > 0.5 ? (-1) : 1) * 0.5, 2.0f,
                    Color.BLUE, visibility);
        }
        //
        double hours = hour / 12.0 + minute / 60.0 / 12 + second / 60 / 60 / 12;
        drawHand(g2d, side / 2 - 40,
                hours, 4.0f,
                Color.BLACK, visibility);
        if (TimeCalcConf.getInstance().areClockHandsLong()) {
            drawHand(g2d, (side / 2 - 40) / 4,
                    hours + hours > 0.5 ? hours - 0.5 :
                            hours + (hours > 0.5 ? (-1) : 1) * 0.5, 4.0f,
                    Color.BLACK, visibility);
        }
        drawCentre(g2d, centerX, centerY);

    }

    private void drawCentre(Graphics2D g2d, int centerX, int centerY) {
        Color currentColor = g2d.getColor();
        Visibility visibility = Visibility.valueOf(visibilityProperty.getValue());
        g2d.setColor(visibility.isStronglyColored() || mouseOver ? Color.RED :
                FOREGROUND_COLOR);
        g2d.fillOval(centerX - 3, centerY - 3, 8, 8);
        g2d.setColor(currentColor);
    }

    private void drawHand(Graphics2D g2d, int length, double value,
            float stroke, Color color, Visibility visibility) {
        length = length - 4;
        double angle = Math.PI * 2 * (value - 0.25);
        int endX = (int) (getWidth() / 2 + length * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + length * Math.sin(angle));

        g2d.setColor((visibility.isStronglyColored() || mouseOver) ? color :
                FOREGROUND_COLOR);
        g2d.setStroke(new BasicStroke(stroke));
        g2d.drawLine(getWidth() / 2, getHeight() / 2, endX, endY);
    }

    private void drawClockFace(Graphics2D g2d, int centerX, int centerY,
            int radius, Visibility visibility) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK :
                FOREGROUND_COLOR);
        //        System.out.println("centerX=" + centerX);
        //        System.out.println("centerY=" + centerY);
        //        System.out.println("radius=" + radius);
        g2d.drawOval(1, 1, centerX * 2 - 4, centerY * 2 - 4);
        g2d.drawOval(2, 2, centerX * 2 - 4, centerY * 2 - 4);

        //        g2d.drawOval(3, 3, centerX * 2 - 6, centerY * 2 - 6);
        //        g2d.drawOval(4, 4, centerX * 2 - 8, centerY * 2 - 8);
        if(this.mouseOver)
        {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, yearProperty.getValue());
            cal.set(Calendar.MONTH, monthProperty.getValue() -1);
            cal.set(Calendar.DAY_OF_MONTH, dayProperty.getValue());
            Date date = cal.getTime();
            g2d.drawString(DateFormats.DATE_TIME_FORMATTER_LONG.format(date), ((int) (side * 0.25)),
                    ((int) (side * 0.35)));
            g2d.drawString(DateFormats.DATE_TIME_FORMATTER_TIME.format(date),
                    ((int) (side * 0.25) + 30),
                    ((int) (side * 0.35)) + 60);
        }
        for (int i = 1; i <= 12; i++) {
            double angle = Math.PI * 2 * (i / 12.0 - 0.25);
            int dx = centerX + (int) ((radius + 20) * Math.cos(angle)) - 4;
            int dy = centerY + (int) ((radius + 20) * Math.sin(angle)) + 4;

            g2d.setFont(new Font("sans", Font.BOLD, 16));
            g2d.drawString(Integer.toString(i), dx, dy);
        }

    }

    public int getTimerDelay() {
        return 20;
    }

}