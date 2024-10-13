package com.robertvokac.utils.timecalc.swing.progress;

import com.robertvokac.utils.timecalc.app.TimeCalcProperty;
import com.robertvokac.utils.timecalc.entity.Visibility;
import com.robertvokac.utils.timecalc.entity.WidgetType;
import com.robertvokac.utils.timecalc.swing.common.SwingUtils;
import com.robertvokac.utils.timecalc.swing.common.Widget;
import com.robertvokac.utils.timecalc.swing.controls.TMenuItem;
import com.robertvokac.utils.timecalc.swing.progress.battery.Battery;
import com.robertvokac.utils.timecalc.utils.common.DateFormats;
import com.robertvokac.utils.timecalc.utils.common.TTime;
import com.robertvokac.utils.timecalc.utils.property.BooleanProperty;
import com.robertvokac.utils.timecalc.utils.property.IntegerProperty;
import com.robertvokac.utils.timecalc.utils.property.StringProperty;

import javax.swing.JFrame;
import javax.swing.JMenu;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.JMenuItem;
import com.robertvokac.utils.timecalc.entity.Progress;

//https://kodejava.org/how-do-i-write-a-simple-analog-clock-using-java-2d/
public class AnalogClock extends Widget {

    public static final Color COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED
            = new Color(226,
            126, 19);
    public final BooleanProperty borderVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_VISIBLE
                    .getKey());
    public final BooleanProperty borderOnlyHoursProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_ONLY_HOURS
                    .getKey());
    public final BooleanProperty numbersVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_NUMBERS_VISIBLE
                    .getKey());
    public final BooleanProperty circleVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CIRCLE_VISIBLE
                    .getKey());
    public final BooleanProperty circleStrongBorderProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CIRCLE_STRONG_BORDER
                    .getKey());
    public final BooleanProperty centreCircleVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_VISIBLE
                    .getKey());
    public final StringProperty centreCircleBorderColorProperty
            = new StringProperty(TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR
                    .getKey());
    public final BooleanProperty centreCircleColoredProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_COLORED
                    .getKey());
    public final BooleanProperty progressVisibleOnlyIfMouseMovingOverProperty
            = new BooleanProperty(
                    TimeCalcProperty.CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    public final BooleanProperty dateVisibleOnlyIfMouseMovingOverProperty
            = new BooleanProperty(
                    TimeCalcProperty.CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());

    public BooleanProperty circleProgressVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_CIRCLE_PROGRESS_VISIBLE
                    .getKey(), true);

    public IntegerProperty startHourProperty
            = new IntegerProperty("startHourProperty");
    public IntegerProperty startMinuteProperty
            = new IntegerProperty("startMinuteProperty");
    public IntegerProperty endHourProperty
            = new IntegerProperty("endHourProperty");
    public IntegerProperty endMinuteProperty
            = new IntegerProperty("endMinuteProperty");
    public IntegerProperty yearProperty = new IntegerProperty("yearProperty");
    public IntegerProperty monthProperty = new IntegerProperty("monthProperty");
    public IntegerProperty dayProperty = new IntegerProperty("dayProperty");
    public IntegerProperty hourProperty = new IntegerProperty("hourProperty");
    public IntegerProperty minuteProperty
            = new IntegerProperty("minuteProperty");
    public IntegerProperty secondProperty
            = new IntegerProperty("secondProperty");
    public IntegerProperty millisecondProperty
            = new IntegerProperty("millisecondProperty");
    public IntegerProperty dayOfWeekProperty
            = new IntegerProperty("dayOfWeekProperty");
    public BooleanProperty hourEnabledProperty
            = new BooleanProperty("hourEnabledProperty", true);
    public BooleanProperty minuteEnabledProperty
            = new BooleanProperty("minuteEnabledProperty", true);
    public BooleanProperty secondEnabledProperty
            = new BooleanProperty("secondEnabledProperty", true);
    public BooleanProperty millisecondEnabledProperty
            = new BooleanProperty("millisecondEnabledProperty", false);
    public BooleanProperty handsLongProperty
            = new BooleanProperty("handsLongProperty", true);
    public BooleanProperty handsColoredProperty
            = new BooleanProperty("handsColoredProperty", true);
    public final BooleanProperty smileyVisibleProperty = new BooleanProperty("smileyVisibleProperty");
    public final BooleanProperty percentProgressVisibleProperty = new BooleanProperty("percentProgressVisibleProperty");
    private Color customCircleColor = null;
    private double dayProgress;
    private TMenuItem millisecondHandMenuItem;
    private TMenuItem secondHandMenuItem;
    private TMenuItem minuteHandMenuItem;
    private TMenuItem hourHandMenuItem;
    private List<JMenuItem> menuItems = null;

    public AnalogClock() {
        typeProperty.setValue(WidgetType.DAY.name().toLowerCase(Locale.ROOT));

        setPreferredSize(new Dimension(200, 200));

        centreCircleBorderColorProperty.addListener(property
                -> customCircleColor = SwingUtils.getColorFromString(
                        centreCircleBorderColorProperty.getValue()));
    }

    private int computeStartAngle() {
        return computeAngle(hourProperty.getValue(), minuteProperty.getValue());
    }

    private int computeEndAngle() {
        return computeAngle(endHourProperty.getValue(), endMinuteProperty.getValue());
    }

    private int computeAngle(TTime TTime) {
        if (TTime.getHour() > 12) {
            TTime.setHour(TTime.getHour() - 12);
        }
        return computeAngle(TTime.getHour(), TTime.getMinute());
    }

    private int computeAngle(int hour, int minute) {
        return (int) ((hour + minute / 60d) / 12d
                * 360d);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Analog Clock");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AnalogClock clock
                = new AnalogClock();
        clock.startHourProperty.setValue(6);
        clock.startMinuteProperty.setValue(30);
        clock.endHourProperty.setValue(19);
        clock.endMinuteProperty.setValue(0);
        window.add(clock);
        window.pack();
        Time time = new Time();
        time.allowCustomValuesProperty.setValue(true);
        clock.dayProperty.bindTo(time.dayProperty);
        clock.monthProperty.bindTo(time.monthProperty);
        clock.yearProperty.bindTo(time.yearProperty);
        clock.hourProperty.bindTo(time.hourProperty);
        clock.minuteProperty.bindTo(time.minuteProperty);
        clock.secondProperty.bindTo(time.secondProperty);
        clock.millisecondProperty.bindTo(time.millisecondProperty);
        clock.dayOfWeekProperty.bindTo(time.dayOfWeekProperty);
        clock.visibilityProperty.setValue(Visibility.GRAY.name());
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

        TTime start = new TTime(this.startHourProperty.getValue(), this.startMinuteProperty.getValue());
        TTime end = new TTime(this.endHourProperty.getValue(), this.endMinuteProperty.getValue());
        TTime now = new TTime(this.hourProperty.getValue(), this.minuteProperty.getValue());
        int startMS = start.toTotalMilliseconds();
        int endMS = end.toTotalMilliseconds();
        int nowMS = now.toTotalMilliseconds();
        int total = endMS - startMS;
        int done = nowMS - startMS;
        double progress_ = ((double) done) / ((double) total);
        this.dayProgress = progress_;

        //System.out.println("clock.handsLongProperty=" + handsLongProperty.isEnabled());
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.side = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int millisecond = millisecondProperty.getValue();
        int second = secondProperty.getValue();
        int minute = minuteProperty.getValue();
        int hour = hourProperty.getValue();

        if (customCircleColor == null) {
            customCircleColor = SwingUtils.getColorFromString(
                    centreCircleBorderColorProperty.getValue());
        }
        if ((mouseOver || progressVisibleOnlyIfMouseMovingOverProperty
                .isDisabled()) && visibility.isStronglyColored()) {

            Color currentColor = brush.getColor();

            brush.setColor(Battery.getColourForProgress(progress_, Visibility.WEAKLY_COLORED, mouseOver));
            int startAngle = computeStartAngle();
            brush.fillArc(0, 0, side, side, -startAngle + 90,
                    startAngle - computeEndAngle());

            //System.out.println("ANGLES: " + startAngle + " " + endAngle + " " + angleDiff );
            brush.setColor(currentColor);
        }

        //
        if (millisecondEnabledProperty.isEnabled() && secondEnabledProperty
                .isEnabled() && minuteEnabledProperty.isEnabled()
                && hourEnabledProperty.isEnabled()) {
            drawHand(brush, side / 2 - 10, millisecond / 1000.0, 1.0f,
                    COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED, visibility);

            if (handsLongProperty.isEnabled()) {
                drawHand(brush, (side / 2 - 10) / 4,
                        (millisecond > 500 ? millisecond - 500
                                : millisecond + 500) / 1000.0, 1.0f,
                        COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED,
                        visibility);
            }
        }

        if (secondEnabledProperty.isEnabled() && minuteEnabledProperty
                .isEnabled() && hourEnabledProperty.isEnabled()) {
            drawHand(brush, side / 2 - 10, second / 60.0, 0.5f, Color.RED,
                    visibility);

            if (handsLongProperty.isEnabled()) {
                drawHand(brush, (side / 2 - 10) / 4,
                        (second > 30 ? second - 30 : second + 30) / 60.0, 0.5f,
                        Color.RED, visibility);
            }
        }
        if (minuteEnabledProperty.isEnabled() && hourEnabledProperty
                .isEnabled()) {
            double minutes = minute / 60.0 + second / 60.0 / 60.0;
            drawHand(brush, side / 2 - 20, minutes, 2.0f,
                    Color.BLUE, visibility);
            if (handsLongProperty.isEnabled()) {
                drawHand(brush, (side / 2 - 20) / 4,
                        minutes + minutes > 0.5 ? minutes - 0.5
                                : minutes + (minutes > 0.5 ? (-1) : 1) * 0.5,
                        2.0f,
                        Color.BLUE, visibility);
            }
        }
        if (hourEnabledProperty.isEnabled()) {
            double hours
                    = hour / 12.0 + minute / 60.0 / 12 + second / 60 / 60 / 12;
            drawHand(brush, side / 2 - 40,
                    hours, 4.0f,
                    Color.BLACK, visibility);
            if (handsLongProperty.isEnabled()) {
                drawHand(brush, (side / 2 - 40) / 4,
                        hours + hours > 0.5 ? hours - 0.5
                                : hours + (hours > 0.5 ? (-1) : 1) * 0.5, 4.0f,
                        Color.BLACK, visibility);
            }
        }
        if (borderVisibleProperty.isEnabled()) {
            for (int minuteI = 0; minuteI < 60; minuteI++) {
                if (borderOnlyHoursProperty.isEnabled() && minuteI % 5 != 0) {
                    continue;
                }
                drawBorder(brush, minuteI, minuteI % 5 == 0
                        ? (numbersVisibleProperty.isEnabled() ? 2f : 4f) : 1f,
                        Color.BLACK, visibility);
            }
        }
        if (centreCircleVisibleProperty.isEnabled()) {
            drawCentreCircle(brush, centerX, centerY);
        }

        drawClockFace(brush, centerX, centerY, side / 2 - 40, visibility);

        if (progress == null) {
            progress = new Progress();
        }
        progress.set(WidgetType.DAY, (hour * 60 * 60 + minute * 60 + second) / (24d * 60d * 60d));
        if (circleProgressVisibleProperty.isEnabled()) {
            paintCircleProgress(brush, visibility, getWidth(), getHeight());
        }


    }

    private void drawCentreCircle(Graphics2D brush, int centerX, int centerY) {
        Color currentColor = brush.getColor();
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        brush.setColor(visibility.isStronglyColored() || mouseOver
                ? (centreCircleColoredProperty.isEnabled() ? Color.RED : Color.BLACK)
                : FOREGROUND_COLOR);
        brush.fillOval(centerX - 3, centerY - 3, 8, 8);
        brush.setColor(currentColor);
    }

    private void drawBorder(Graphics2D brush, int forMinute,
            float stroke, Color color, Visibility visibility) {
        double value = ((double) forMinute) / 60d;
        boolean hourAngle = forMinute % 5 == 0;
        int length = side / (numbersVisibleProperty.isEnabled() ? 18
                : (hourAngle ? 12 : 18));
        double angle = Math.PI * 2 * (value - 0.25);
        int startX
                = (int) (getWidth() / 2 + (side / 2 - length) * Math.cos(angle));
        int startY
                = (int) (getHeight() / 2 + (side / 2 - length) * Math.sin(angle));
        int endX = (int) (getWidth() / 2
                + (side / 2 - length * 0.50d * (hourAngle ? 0.25 : 1))
                * Math.cos(angle));
        int endY = (int) (getHeight() / 2
                + (side / 2 - length * 0.50d * (hourAngle ? 0.25 : 1))
                * Math.sin(angle));

        brush.setColor((visibility.isStronglyColored() || mouseOver) ? color
                : FOREGROUND_COLOR);
        brush.setStroke(new BasicStroke(stroke));
        brush.drawLine(startX, startY, endX, endY);
    }

    private void drawHand(Graphics2D brush, int length, double value,
            float stroke, Color color, Visibility visibility) {
        length = length - 4;
        double angle = Math.PI * 2 * (value - 0.25);
        int endX = (int) (getWidth() / 2 + length * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + length * Math.sin(angle));

        brush.setColor((visibility.isStronglyColored() || mouseOver)
                ? (handsColoredProperty.isEnabled() ? color : Color.BLACK)
                : FOREGROUND_COLOR);
        brush.setStroke(new BasicStroke(stroke));
        brush.drawLine(getWidth() / 2, getHeight() / 2, endX, endY);
    }

    private void drawClockFace(Graphics2D brush, int centerX, int centerY,
            int radius, Visibility visibility) {
        brush.setStroke(new BasicStroke(2.0f));
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK
                : FOREGROUND_COLOR);
        if (circleVisibleProperty.isEnabled()) {
            Color currentColor = brush.getColor();
            if (visibility.isStronglyColored() || mouseOver) {
                brush.setColor(customCircleColor);
            }
            brush.drawOval(1, 1, centerX * 2 - 3, centerY * 2 - 3);
            brush.drawOval(2, 2, centerX * 2 - 3, centerY * 2 - 3);
            if (circleStrongBorderProperty.isEnabled()) {
                brush.drawOval(3, 3, centerX * 2 - 6, centerY * 2 - 6);
                brush.drawOval(4, 4, centerX * 2 - 8, centerY * 2 - 8);
                brush.drawOval(5, 5, centerX * 2 - 10, centerY * 2 - 10);
                brush.drawOval(6, 6, centerX * 2 - 12, centerY * 2 - 12);
            }
            brush.setColor(currentColor);
        }

        if (this.mouseOver || dateVisibleOnlyIfMouseMovingOverProperty
                .isDisabled()) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, yearProperty.getValue());
            cal.set(Calendar.MONTH, monthProperty.getValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, dayProperty.getValue());
            cal.set(Calendar.HOUR_OF_DAY, hourProperty.getValue());
            cal.set(Calendar.MINUTE, minuteProperty.getValue());
            cal.set(Calendar.SECOND, secondProperty.getValue());
            cal.set(Calendar.MILLISECOND, millisecondProperty.getValue());
            Date date = cal.getTime();
            brush.drawString(DateFormats.DATE_TIME_FORMATTER_LONG.format(date),
                    ((int) (side * 0.25)),
                    ((int) (side * 0.35)));
            brush.drawString(DateFormats.DATE_TIME_FORMATTER_TIME.format(date),
                    ((int) (side * 0.25) + 30),
                    ((int) (side * 0.35)) + 60);
        }

        if (percentProgressVisibleProperty.isEnabled()) {
            int d = (int) Math.floor(dayProgress * 100);
            if(d < 0) {
                d = 0;
            }
            if(d > 100) {
                d = 100;
            }
            brush.drawString(d + "%",
                    ((int) (side * 0.25)),
                    ((int) (side * 0.35)) + 35);
        }

        if (smileyVisibleProperty.isEnabled()) {
            paintSmiley(visibility, brush, ((int) (side * 0.25) + 90),
                    ((int) (side * 0.35)) + 20);
        } else {
            if(this.smileyIcon != null) {
                remove(this.smileyIcon);
                this.smileyIcon = null;
            }
            if(this.smileyIcon2 != null) {
                remove(this.smileyIcon2);
                this.smileyIcon2 = null;
            }
        }

        if (numbersVisibleProperty.isEnabled()) {
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
        return 100;
    }

    @Override
    public List<JMenuItem> createAdditionalMenus() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();
            JMenu hands = new JMenu("Hands");
            menuItems.add(hands);

            this.millisecondHandMenuItem = new TMenuItem("Millisecond");
            this.secondHandMenuItem = new TMenuItem("Second");
            this.minuteHandMenuItem = new TMenuItem("Minute");
            this.hourHandMenuItem = new TMenuItem("Hour");

            if (millisecondEnabledProperty.isEnabled()) {
                millisecondHandMenuItem.enableMenuItem();
            }
            if (secondEnabledProperty.isEnabled()) {
                secondHandMenuItem.enableMenuItem();
            }
            if (minuteEnabledProperty.isEnabled()) {
                minuteHandMenuItem.enableMenuItem();
            }
            if (hourEnabledProperty.isEnabled()) {
                hourHandMenuItem.enableMenuItem();
            }
            millisecondEnabledProperty.addListener(
                    (property, oldValue, newValue) -> millisecondHandMenuItem
                            .setEnabledMenuItem(newValue));
            secondEnabledProperty.addListener(
                    (property, oldValue, newValue) -> secondHandMenuItem
                            .setEnabledMenuItem(newValue));
            minuteEnabledProperty.addListener(
                    (property, oldValue, newValue) -> minuteHandMenuItem
                            .setEnabledMenuItem(newValue));
            hourEnabledProperty.addListener(
                    (property, oldValue, newValue) -> hourHandMenuItem
                            .setEnabledMenuItem(newValue));

            hands.add(millisecondHandMenuItem);
            hands.add(secondHandMenuItem);
            hands.add(minuteHandMenuItem);
            hands.add(hourHandMenuItem);

            millisecondHandMenuItem
                    .addActionListener(e -> millisecondEnabledProperty.flip());
            secondHandMenuItem
                    .addActionListener(e -> secondEnabledProperty.flip());
            minuteHandMenuItem
                    .addActionListener(e -> minuteEnabledProperty.flip());
            hourHandMenuItem.addActionListener(e -> hourEnabledProperty.flip());
        }
        return this.menuItems;
    }

    protected Consumer<Object> createRefreshConsumer() {
        return (o) -> {
            millisecondHandMenuItem.disableMenuItem();
            secondHandMenuItem.disableMenuItem();
            minuteHandMenuItem.disableMenuItem();
            hourHandMenuItem.disableMenuItem();
            if (millisecondEnabledProperty.isEnabled()) {
                millisecondHandMenuItem.enableMenuItem();
            }
            if (secondEnabledProperty.isEnabled()) {
                secondHandMenuItem.enableMenuItem();
            }
            if (minuteEnabledProperty.isEnabled()) {
                minuteHandMenuItem.enableMenuItem();
            }
            if (hourEnabledProperty.isEnabled()) {
                hourHandMenuItem.enableMenuItem();
            }
        };
    }
}
