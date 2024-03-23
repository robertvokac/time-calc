package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.windows.MainWindow;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.Property;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class ProgressWeather extends Widget implements GetProperty {

    private final Time time;

    public ProgressWeather(Time time) {
        this.time = time;
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

    private static Map<Integer, List<WeatherForecast>> forecastsForYears = new HashMap<>();
    @Override
    public void paintWidget(Graphics brush) {

        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());

        brush.setColor(visibility.isStronglyColored() ? Color.BLUE
                : visibility.isWeaklyColored() ? Color.GRAY
                : Color.LIGHT_GRAY);
        brush.setFont(SwingUtils.MEDIUM_MONOSPACE_FONT);
        int year = time.yearProperty.getValue();
        if(!forecastsForYears.containsKey(year)) {
            File weatherFile = new File(FileConstants.WEATHER_TXT.getParentFile(),
                    String.valueOf(year) + '_' + FileConstants.WEATHER_TXT.getName());
            System.out.println(weatherFile.getAbsolutePath());
            if (!weatherFile.exists()) {
                List<WeatherForecast> forecasts = WeatherForecast.createForecast();
                Utils.writeTextToFile(weatherFile,
                        forecasts.stream().map(w -> w.toCsv()).collect(
                                Collectors.joining("\n")));
            }
            List<WeatherForecast> forecasts = new ArrayList<>();
            try {
                Arrays.stream(Utils.readTextFromFile(weatherFile).split("\n")).filter(line -> !line.isEmpty()).forEach(line -> forecasts.add(new WeatherForecast().fromCsv(line)));
            } catch (IOException e) {
                System.out.println("Loading file failed: " + weatherFile + " " + e
                        .getMessage());
                e.printStackTrace();
                return;
            }
            forecastsForYears.put(year, forecasts);
        }
        List<WeatherForecast> forecasts = forecastsForYears.get(year);


        int currentSecond = time.secondProperty.getValue();
        Calendar cal = time.asCalendar();
        int add = (currentSecond % 14);
        switch (add) {
            case 0:
                add = 0; break;
            case 1:
                add = 0; break;
            case 2:
                add = 1; break;
            case 3:
                add = 1; break;
            case 4:
                add = 2; break;
            case 5:
                add = 2; break;
            case 6:
                add = 3; break;
            case 7:
                add = 3; break;
            case 8:
                add = 4; break;
            case 9:
                add = 4; break;
            case 10:
                add = 5; break;
            case 11:
                add = 5; break;
            case 12:
                add = 6; break;
            case 13:
                add = 6; break;
            default:
                add = 7; break;

        }

        add = add / 2;
        if (add > 0) {
            cal.add(Calendar.DAY_OF_MONTH, add);
        }
        int currentDayOfWeek = time.dayOfWeekProperty.getValue();
        int forecastDayOfWeek = currentDayOfWeek + add;
//        String dayOfWeekAsString = DayOfWeekTC.forNumber(forecastDayOfWeek).name();
//        dayOfWeekAsString = dayOfWeekAsString.substring(0,1).toUpperCase(Locale.ROOT) + dayOfWeekAsString.substring(1, dayOfWeekAsString.length());
        WeatherForecast weatherForecast = forecasts.stream().filter(f -> f.getDate().equals(DateFormats.DATE_TIME_FORMATTER_YYYYMMDD.format(cal.getTime()))).findFirst().orElse(new WeatherForecast());

        int rowHeight = 25;
        int row = 10;
        brush.drawString(DateFormats.DATE_TIME_FORMATTER_YYYYMMDD.format(cal.getTime()), SwingUtils.MARGIN, SwingUtils.MARGIN + row);
        row = row + rowHeight;
        brush.drawString(DateFormats.DATE_TIME_FORMATTER_DAY_OF_WEEK.format(cal.getTime()), SwingUtils.MARGIN, SwingUtils.MARGIN + row);
        row = row + rowHeight;
        brush.drawString(weatherForecast.asPrettyString(new TTime(time.hourProperty.getValue(),
                time.minuteProperty.getValue(),
                time.secondProperty.getValue(),
                time.millisecondProperty.getValue()).toTotalMilliseconds() / 1000d / 60d / 60d), SwingUtils.MARGIN, SwingUtils.MARGIN + row);
        row = row + rowHeight;

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
