package org.nanoboot.utils.timecalc.swing.progress.weather;

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
import javax.swing.JMenuItem;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class ProgressWeather extends Widget implements GetProperty {

    private final Time time;
    private int lastAdd = 0;

    private List<JMenuItem> menuItems = null;
    private boolean information;

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
        if (!forecastsForYears.containsKey(year)) {
            File weatherFile = new File(FileConstants.WEATHER_CSV.getParentFile(),
                    String.valueOf(year) + '_' + FileConstants.WEATHER_CSV.getName());
            System.out.println(weatherFile.getAbsolutePath());
            if (!weatherFile.exists()) {
                List<WeatherForecast> forecasts = WeatherForecast.createForecast(year);

                Utils.writeTextToFile(
                        weatherFile,
                        "Date\tMinC\tMaxC\tMinTime\tMaxTime\tWind\tCloudiness\tThunder\tRain\tSnow\tHumidity\tHearOrColdWave\tStartC\tEndC\n"
                        + forecasts.stream().map(w -> w.toCsv()).collect(
                                Collectors.joining("\n")));
            }
            List<WeatherForecast> forecasts = new ArrayList<>();
            try {
                Arrays.stream(Utils.readTextFromFile(weatherFile).split("\n"))
                        .filter(line -> !line.startsWith("Date"))
                        .filter(line -> !line.isEmpty()).forEach(line -> forecasts.add(new WeatherForecast().fromCsv(line)));
            } catch (IOException e) {
                System.out.println("Loading file failed: " + weatherFile + " " + e
                        .getMessage());
                e.printStackTrace();
                return;
            }
            forecastsForYears.put(year, forecasts);
            
        }
        List<WeatherForecast> forecasts = forecastsForYears.get(year);

//        if(!w){
//            w= true;
//            List<Double> temperature = new ArrayList<>();
//            forecasts.stream().forEach(f -> {
//
//                for (int hour = 0; hour < 24; hour++) {
//                    //for (int minute = 0; minute < 60; minute = minute + 30) {
//                    int minute = 0;
//                        double forHour = ((double)(new TTime(hour, minute).toTotalMilliseconds() / 1000d / 60d / 60d));
//                        temperature.add(f.getCelsiusForHour(forHour));
//                    //}
//                }
//            });
//            StringBuilder sb = new StringBuilder();
//            temperature.stream().forEach(t -> sb.append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(t)).append("\n"));
//            Utils.writeTextToFile(new File("t.csv"), sb.toString());
//        }
        int currentSecond = time.secondProperty.getValue();
        Calendar cal = time.asCalendar();
        int add = (currentSecond % 21);
        if (paused) {
            add = lastAdd;
        } else {
            if (add < 3) {
                add = 0;
            } else if (add < 6) {
                add = 1;
            } else if (add < 9) {
                add = 2;
            } else if (add < 12) {
                add = 3;
            } else if (add < 15) {
                add = 4;
            } else if (add < 18) {
                add = 5;
            } else if (add < 21) {
                add = 6;
            }
            this.lastAdd = add;
        }
        if (add > 0) {
            cal.add(Calendar.DAY_OF_MONTH, add);
        }
        int currentDayOfWeek = time.dayOfWeekProperty.getValue();
        int forecastDayOfWeek = currentDayOfWeek + add;
//        String dayOfWeekAsString = DayOfWeekTC.forNumber(forecastDayOfWeek).name();
//        dayOfWeekAsString = dayOfWeekAsString.substring(0,1).toUpperCase(Locale.ROOT) + dayOfWeekAsString.substring(1, dayOfWeekAsString.length());
        WeatherForecast wf = forecasts.stream().filter(f -> f.getDate().equals(DateFormats.DATE_TIME_FORMATTER_YYYYMMDD.format(cal.getTime()))).findFirst().orElse(new WeatherForecast());

        if(information) {
            information = false;
            StringBuilder sb = new StringBuilder();
            sb.append("Date: ").append(wf.getDate()).append("\n");
            sb
                    .append("Start: ")
                    .append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(wf.getStartCelsius()))
                    .append(" °C at 00:00")
                    .append("\n");
            sb
                    .append("Minimum: ")
                    .append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(wf.getMinimumCelsius()))
                    .append(" °C at ").append(TTime.ofMilliseconds((int) (wf.getMinimumCelsiusTime() * 1000d * 60d * 60d)).toString().substring(0, 5))
                    .append("\n");
            sb
                    .append("Maximum: ")
                    .append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(wf.getMaximumCelsius()))
                    .append(" °C at ").append(TTime.ofMilliseconds((int) (wf.getMinimumCelsiusTime() * 1000d * 60d * 60d)).toString().substring(0, 5))
                    .append("\n");
            sb
                    .append("End: ")
                    .append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(wf.getEndCelsius()))
                    .append(" °C at 24:00")
                    .append("\n");
            sb.append("Wind: ").append((int)wf.getWind()).append(" m/s\n");
            sb.append("Cloudiness: ").append(Cloudiness.forValue(wf.getCloudiness()).getDescription().toLowerCase()).append("\n");
            if(wf.getThunder() > 0d) {
                sb.append("Thunder strenth: ").append(NumberFormats.FORMATTER_ZERO_DECIMAL_PLACES.format(wf.getThunder() * 100d)).append("%\n");
            }
            sb.append("Rain: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(wf.getRain())).append(" mm\n");
            if(wf.getSnow() > 0d) {
                sb.append("Snow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(wf.getSnow())).append(" mm\n");
            }
            sb.append("Humidity: ").append((int)(wf.getHumidity() * 100d * 0.6)).append(" %\n");
            if(wf.getHeatOrColdWave() < 0.5d) {
                sb.append("Cold Wave: ").append(NumberFormats.FORMATTER_ZERO_DECIMAL_PLACES.format(100d - (wf.getHeatOrColdWave() * 2 * 100d))).append(" %\n");
            } else {
                sb.append("Heat Wave: ").append(NumberFormats.FORMATTER_ZERO_DECIMAL_PLACES.format((wf.getHeatOrColdWave() - 0.5d) * 2 * 100d)).append(" %\n");
            }
            WeatherForecast f1 = forecasts.get(forecasts.indexOf(wf) + 1);
            WeatherForecast f2 = forecasts.get(forecasts.indexOf(wf) + 2);
            WeatherForecast f3 = forecasts.get(forecasts.indexOf(wf) + 3);
            WeatherForecast f4 = forecasts.get(forecasts.indexOf(wf) + 4);
            WeatherForecast f5 = forecasts.get(forecasts.indexOf(wf) + 5);
            WeatherForecast f6 = forecasts.get(forecasts.indexOf(wf) + 6);
            sb.append("Tomorrow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(f1.getMaximumCelsius())).append(" °C\n");
            sb.append("Day after tomorrow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(f2.getMaximumCelsius())).append(" °C\n");
            sb.append(" 2 days after tomorrow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(f3.getMaximumCelsius())).append(" °C\n");
            sb.append(" 3 days after tomorrow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(f4.getMaximumCelsius())).append(" °C\n");
            sb.append(" 4 days after tomorrow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(f5.getMaximumCelsius())).append(" °C\n");
            sb.append(" 5 days after tomorrow: ").append(NumberFormats.FORMATTER_ONE_DECIMAL_PLACE.format(f6.getMaximumCelsius())).append(" °C\n");

            Utils.showNotification(sb.toString(), 30000, 400);
        }
        String[] array = wf.asPrettyString(add > 0 ? -1 : new TTime(time.hourProperty.getValue(),
                time.minuteProperty.getValue(),
                time.secondProperty.getValue(),
                time.millisecondProperty.getValue()).toTotalMilliseconds() / 1000d / 60d / 60d).split(WeatherForecast.DOUBLE_COLON);
        List<String> list = null;
        for (String a : array) {
            if (a.length() >= 14) {
                list = new ArrayList<>();
                break;
            }
        }
        if (list != null) {
            for (String a : array) {
                if (a.length() >= 14) {
                    list.add(a.substring(0, 14));
                    list.add(a.substring(14, a.length()));
                } else {
                    list.add(a);
                }
            }
            array = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
        }

        int rowHeight = 12;
        int row = 0;
        brush.drawString(DateFormats.DATE_TIME_FORMATTER_YYYYMMDD.format(cal.getTime()), 0, SwingUtils.MARGIN + row);
        row = row + 12;
        brush.drawString(DateFormats.DATE_TIME_FORMATTER_DAY_OF_WEEK.format(cal.getTime()), 0, SwingUtils.MARGIN + row);
        row = row + rowHeight + 10;
        brush.drawString(array[0], 0, SwingUtils.MARGIN + row);
        row = row + rowHeight;
        brush.drawString(array.length < 2 ? "" : array[1], 0, SwingUtils.MARGIN + row);
        row = row + rowHeight;
        brush.drawString(array.length < 3 ? "" : array[2], 0, SwingUtils.MARGIN + row);
        row = row + rowHeight;
        if (array.length >= 4) {
            brush.drawString(array[3], 0, SwingUtils.MARGIN + row);
            row = row + rowHeight;
        }
        if (array.length >= 5) {
            brush.drawString(array[4], 0, SwingUtils.MARGIN + row);
            row = row + rowHeight;
        }
        this.setToolTipText(wf.toString());
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
            JMenuItem pauseResume = new JMenuItem("Pause/Resume");
            pauseResume.addActionListener(e -> paused = !paused);
            menuItems.add(pauseResume);
            
            JMenuItem now = new JMenuItem("Now");
            now.addActionListener(e -> {
                lastAdd = 0;
                paused = true;
            });
            menuItems.add(now);
            
            JMenuItem info = new JMenuItem("Info");
            info.addActionListener(e -> {
                information = true;
            });
            menuItems.add(info);

        }
        return this.menuItems;
    }
    private boolean paused = false;
}
