package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.windows.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class ProgressMoney extends Widget implements GetProperty {

    private final Time time;
    public IntegerProperty perMonthProperty
            = new IntegerProperty("money.perMonthProperty");
    public StringProperty currencyProperty
            = new StringProperty("money.currencyProperty");

    public ProgressMoney(Time time) {
        this.time = time;
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        setFocusable(false);
        setForeground(Color.GRAY);
        setBackground(MainWindow.BACKGROUND_COLOR);

        new Timer(100, e -> {
            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());
            setForeground(
                    visibility.isStronglyColored()
                    || mouseOver
                            ? Color.BLACK : Color.LIGHT_GRAY);
        }).start();

    }

    @Override
    public void paintWidget(Graphics brush) {
        double perMonth = this.perMonthProperty.getValue();
        if (perMonth == 0) {
            //nothing to do
            return;
        } else {
            if(progress == null) {
                //nothing to do
                return;
            }
            double workDaysInMonth = progress.getWorkDaysInMonth();
            boolean isWeekend = progress.isWeekend();
            double perDay = perMonth / workDaysInMonth;
            double value = 0;
            switch(WidgetType.valueOf(this.typeProperty.getValue().toUpperCase(
                    Locale.ROOT))) {
                case MINUTE: value = isWeekend ? 0d : perDay / 8d / 60d * progress.get(WidgetType.MINUTE);break;
                case HOUR: value = isWeekend ? 0d : perDay / 8d * progress.get(WidgetType.HOUR);break;
                case DAY: value = isWeekend ? 0d : perDay * progress.get(WidgetType.DAY);break;
                case WEEK: value = perDay * 5d * progress.get(WidgetType.WEEK);break;
                case MONTH: value = perMonth * progress.get(WidgetType.MONTH);break;
                case YEAR: value = perMonth * 12 * progress.get(WidgetType.YEAR);break;
            }

            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());

            brush.setColor(visibility.isStronglyColored() ? Color.BLUE
                    : visibility.isWeaklyColored() ? Color.GRAY
                    : Color.LIGHT_GRAY);
            brush.setFont(SwingUtils.MEDIUM_MONOSPACE_FONT);

            NumberFormat formatter = value >= 10000d ? NumberFormats.FORMATTER_TWO_DECIMAL_PLACES : NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES;
            String valueFinal = formatter.format(value) + " " + this.currencyProperty.getValue();
            brush.drawString(valueFinal, SwingUtils.MARGIN, SwingUtils.MARGIN + 10);
            brush.drawString(typeProperty.getValue(), SwingUtils.MARGIN, SwingUtils.MARGIN + 25);

        }
    }

    @Override
    public Property getVisibilityProperty() {
        return visibilityProperty;
    }

    @Override
    public Property getVisibilitySupportedColoredProperty() {
        return visibilitySupportedColoredProperty;
    }
}
