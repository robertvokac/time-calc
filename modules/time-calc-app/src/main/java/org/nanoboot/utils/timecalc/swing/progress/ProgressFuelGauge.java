package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.common.Brush;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.swing.windows.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.property.Property;

import javax.swing.JMenuItem;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class ProgressFuelGauge extends Widget implements GetProperty {

    public static final Color BLACK2 = new Color(64, 64,64);
    public static final Color LIGHT_GRAY2 = new Color(160,160,160);

    private List<JMenuItem> menuItems = null;

    public ProgressFuelGauge() {

        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        setFocusable(false);
        setForeground(Color.GRAY);
        setBackground(MainWindow.BACKGROUND_COLOR);
        this.typeProperty.setValue(WidgetType.DAY.name().toLowerCase(Locale.ROOT));

        new Timer(100, e -> {
            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());
            setForeground(
                    visibility.isStronglyColored()
                    || mouseOver
                            ? Color.BLACK : Color.LIGHT_GRAY);
        }).start();

    }

    public int getTimerDelay() {
        return 100;
    }

    @Override
    public void paintWidget(Graphics brush) {

        ((Graphics2D)brush).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());

        brush.setColor(visibility.isStronglyColored() ? Color.BLACK
                : visibility.isWeaklyColored() ? Color.GRAY
                : Color.LIGHT_GRAY);
        brush.setFont(SwingUtils.MEDIUM_MONOSPACE_FONT);
        brush.fillRect(1, 1, getWidth(), getHeight());
        brush.setColor(visibility.isStronglyColored() ? BLACK2
                : visibility.isWeaklyColored() ? Color.LIGHT_GRAY
                : LIGHT_GRAY2);
        int ovalWidth = (int) (getWidth() * 0.4);
        brush.fillOval(
                (int)((getWidth() - ovalWidth) /2d),
                (int) (getHeight() * 0.6),
                ovalWidth,
                ovalWidth
        );
        brush.setColor(Color.WHITE);

            int startX = getWidth() / 2;
            int startY = getHeight() - (ovalWidth / 2);
            int length = (int) (ovalWidth * 1.5d);
            double angle = Math.PI * 2 * (donePercent() * 0.25d + 2.5d * 0.25d);
            int endX = (int) (startX + length * Math.cos(angle));
            int endY = (int) (startY + length * Math.sin(angle));

            ((Graphics2D)brush).setStroke(new BasicStroke(3f));
            brush.drawLine(startX, startY, endX, endY);
            //
            length = (int) (ovalWidth / 2d * 0.9d);
            angle = Math.PI * 2 * (donePercent() * 0.25d + 2.5d * 0.25d + 0.5d);
            endX = (int) (startX + length * Math.cos(angle));
            endY = (int) (startY + length * Math.sin(angle));

            brush.drawLine(startX, startY, endX, endY);
        //
        ((Graphics2D)brush).setStroke(new BasicStroke(1f));
        int length_ = (int) (ovalWidth * 1.5d);
        //brush.drawOval(startX - length_, startY - length_, (int)(length_ * 2d), (int)(length_ * 2d));
        //brush.drawArc(startX - length_, startY - length_ - 2, (int)(length_ * 2d), (int)(length_ * 2d), 180 + 45, 180 + 45 + 90);
        //brush.drawArc(startX - length_, startY - length_ + 6, (int)(length_ * 2d), (int)(length_ * 2d), 180 + 45, 180 + 45 + 90);
        Brush tBrush = new Brush((Graphics2D) brush);

//        tBrush.drawOval(startX, startY, length_ * 2, length_ * 2);
//        tBrush.drawOval(startX, startY + 8, length_ * 2, length_ * 2);
        tBrush.drawArc(startX, startY - 1 , length_ * 2, length_ * 2, -45, 90);
        tBrush.drawArc(startX, startY + 5, length_ * 2, length_ * 2, -40, 80);
        brush.setColor(visibility.isStronglyColored() ? Color.BLACK
                : visibility.isWeaklyColored() ? Color.GRAY
                : Color.LIGHT_GRAY);
        int width_ = (int) (ovalWidth / 4d);
        brush.fillOval(
                (int) (startX - width_ / 2d),
                (int) (startY - width_ / 2d),
                width_,
                width_
        );
        brush.setColor(Color.WHITE);

        Function<Double, Integer> getAngle = (d -> (int)(-45 + 90 * d));
        brush.setColor(visibility.isStronglyColored() ? Color.RED : (visibility.isWeaklyColored() ?
                Battery.ULTRA_LIGHT_RED : Color.GRAY));
        ((Graphics2D) brush).setStroke(new BasicStroke(6.5f));
        tBrush.drawArc(startX, startY + 2, length_ * 2, length_ * 2,
                (int) (45d / 10d * 7.5d),
                (int) (90d / 4d / 5d * 1.7d));
        brush.setColor(Color.WHITE);
        tBrush.drawBorder(startX, startY, 5, length_ - 4, getAngle.apply(0.25d / 5d * 3d), 2f, brush.getColor());
        tBrush.drawBorder(startX, startY, 5, length_ - 4, getAngle.apply(0.25d), 2f, brush.getColor());
        tBrush.drawBorder(startX, startY, 6, length_ - 7, getAngle.apply(0.5d), 7f, brush.getColor());
        tBrush.drawBorder(startX, startY, 5, length_ - 4, getAngle.apply(0.75d), 2f, brush.getColor());
        tBrush.drawBorder(startX, startY, 6, length_ - 7, getAngle.apply(1d), 7f, brush.getColor());

        //tBrush.drawBorder(startX, startY, 10, length_ - 4 - 5, getAngle.apply(donePercent()), 3f, brush.getColor());
        this.setToolTipText(NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(donePercent() * 100d) + "%");
    }
    
    //private boolean w = false;
    
    @Override
    public Property getVisibilityProperty() {
        return visibilityProperty;
    }

    @Override
    public Property getVisibilitySupportedColoredProperty() {
        return visibilitySupportedColoredProperty;
    }

    @Override
    public List<JMenuItem> createAdditionalMenus() {
        if (this.menuItems == null) {
            menuItems = new ArrayList<>();

        }
        return this.menuItems;
    }
}
