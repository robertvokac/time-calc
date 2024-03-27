package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Toaster;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.windows.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class ProgressLife extends Widget implements GetProperty {

    private final Time time;
    public StringProperty birthDateProperty
            = new StringProperty("life.birthDateProperty");

    public ProgressLife(Time time) {
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
    public int getTimerDelay() {
        return 100;
    }

    @Override
    public void paintWidget(Graphics brush) {
        if (birthDateProperty.getValue().isEmpty()) {
            //nothing to do
            return;
        } else {
            Calendar birthDateCal = Calendar.getInstance();
            String[] array = birthDateProperty.getValue().split("-");
            birthDateCal.set(Calendar.YEAR, Integer.valueOf(array[0]));
            birthDateCal.set(Calendar.MONTH, Integer.valueOf(array[1]) - 1);
            birthDateCal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(array[2]));
            Date birthDate = birthDateCal.getTime();
            Date now = time.asCalendar().getTime();
            long diff = now.getTime() - birthDate.getTime();
            Date result = new Date(
                    (long) (birthDate.getTime() + diff * donePercent()));

            String date =
                    DateFormats.DATE_TIME_FORMATTER_YYYYMMDD.format(result);
            String time =
                    DateFormats.DATE_TIME_FORMATTER_HHmmssSSS.format(result);

            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());

            brush.setColor(visibility.isStronglyColored() ? Color.BLUE
                    : visibility.isWeaklyColored() ? Color.GRAY
                    : Color.LIGHT_GRAY);
            //            if(mouseOver) {
            //                brush.drawRect(1,1,getWidth() - 2, getHeight() - 2);
            //            }
            brush.setFont(SwingUtils.MEDIUM_MONOSPACE_FONT);

            brush.drawString(date, SwingUtils.MARGIN, SwingUtils.MARGIN + 5);
            brush.drawString(time, SwingUtils.MARGIN,
                    (int) (2.5 * SwingUtils.MARGIN) + 5);
            brush.drawString(typeProperty.getValue(), SwingUtils.MARGIN, 4 * SwingUtils.MARGIN + 5);

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
