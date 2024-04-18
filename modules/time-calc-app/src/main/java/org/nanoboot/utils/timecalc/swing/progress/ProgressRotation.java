package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Progress;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.controls.TMenuItem;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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

//https://kodejava.org/how-do-i-write-a-simple-analog-clock-using-java-2d/
public class ProgressRotation extends Widget {

    public static final Color COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED
            = new Color(226,
            126, 19);
    public final BooleanProperty borderVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_VISIBLE
                    .getKey());
    public final BooleanProperty borderOnlyHoursProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_ONLY_HOURS
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

    public BooleanProperty circleProgressVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_CIRCLE_PROGRESS_VISIBLE
                    .getKey(), true);

    private Color customCircleColor = null;

    private List<JMenuItem> menuItems = null;

    public ProgressRotation() {
        typeProperty.setValue(WidgetType.DAY.name().toLowerCase(Locale.ROOT));

        setPreferredSize(new Dimension(100, 100));

        centreCircleBorderColorProperty.addListener(property
                -> customCircleColor = SwingUtils.getColorFromString(
                        centreCircleBorderColorProperty.getValue()));
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Analog Clock");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ProgressRotation rotation
                = new ProgressRotation();
        window.add(rotation);
        window.pack();
        rotation.visibilityProperty.setValue(Visibility.GRAY.name());
        window.setVisible(true);

    }

    private double angle = 0d;
    @Override
    public void paintWidget(Graphics g) {

        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.side = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        if (customCircleColor == null) {
            customCircleColor = SwingUtils.getColorFromString(
                    centreCircleBorderColorProperty.getValue());
        }

        //
        angle = angle + 0.001d * donePercent() * 50d;
        if(angle > 1.0d) {
            angle = 0.0d;
        }

        drawHand(brush, side / 2 - 10, angle, 1.0f,
                    COLOR_FOR_MILLISECOND_HAND_STRONGLY_COLORED, visibility);
        //brush.drawString(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES.format(angle), 15, 15);



        if (centreCircleVisibleProperty.isEnabled()) {
            drawCentreCircle(brush, centerX, centerY);
        }

        if (progress == null) {
            progress = new Progress();
        }

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

    private void drawHand(Graphics2D brush, int length, double value,
            float stroke, Color color, Visibility visibility) {
        length = length - 4;
        double angle = Math.PI * 2 * (value - 0.25);
        int endX = (int) (getWidth() / 2 + length * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + length * Math.sin(angle));

        brush.setColor((visibility.isStronglyColored() || mouseOver)
                ? Color.BLACK : FOREGROUND_COLOR);
        brush.setStroke(new BasicStroke(stroke));
        brush.drawLine(getWidth() / 2, getHeight() / 2, endX, endY);
    }

    public int getTimerDelay() {
        return 1;
    }

    @Override
    public List<JMenuItem> createAdditionalMenus() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();
        }
        return this.menuItems;
    }

    protected Consumer<Object> createRefreshConsumer() {
        return (o) -> {
        };
    }
}
