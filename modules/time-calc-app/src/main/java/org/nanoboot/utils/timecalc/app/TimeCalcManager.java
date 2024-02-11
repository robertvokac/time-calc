package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.AboutButton;
import org.nanoboot.utils.timecalc.swing.common.ComponentRegistry;
import org.nanoboot.utils.timecalc.swing.common.ConfigWindow;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcButton;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcWindow;
import org.nanoboot.utils.timecalc.swing.common.Toaster;
import org.nanoboot.utils.timecalc.swing.common.WeatherWindow;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.progress.AnalogClock;
import org.nanoboot.utils.timecalc.swing.progress.Battery;
import org.nanoboot.utils.timecalc.swing.progress.DayBattery;
import org.nanoboot.utils.timecalc.swing.progress.HourBattery;
import org.nanoboot.utils.timecalc.swing.progress.MonthBattery;
import org.nanoboot.utils.timecalc.swing.progress.ProgressCircle;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSquare;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.swing.progress.WalkingHumanProgressAsciiArt;
import org.nanoboot.utils.timecalc.swing.progress.WeekBattery;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TimeHM;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;

import javax.swing.JComponent;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Robert Vokac
 * @since 08.02.2024
 */
public class TimeCalcManager {
    public static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    public static final Color FOREGROUND_COLOR = new Color(210, 210, 210);
    private ConfigWindow configWindow = null;

    private boolean stopBeforeEnd = false;
    private final TimeCalcConfiguration timeCalcConfiguration =
            new TimeCalcConfiguration();

    public TimeCalcManager(String startTimeIn, String overTimeIn,
            TimeCalcApp timeCalcApp) {
        timeCalcConfiguration
                .setFromTimeCalcProperties(TimeCalcProperties.getInstance());

        overTimeIn = (overTimeIn == null || overTimeIn.isEmpty()) ?
                Constants.DEFAULT_OVERTIME : overTimeIn;

        TimeHM startTime = new TimeHM(startTimeIn);
        TimeHM overtime = new TimeHM(overTimeIn);

        TimeHM endTime = new TimeHM(
                startTime.getHour() + Constants.WORKING_HOURS_LENGTH + overtime
                        .getHour(),
                startTime.getMinute() + Constants.WORKING_MINUTES_LENGTH
                + overtime.getMinute());

        int totalMinutes = TimeHM.countDiffInMinutes(startTime, endTime);
        int totalSeconds = totalMinutes * TimeHM.SECONDS_PER_MINUTE;
        int totalMilliseconds = totalSeconds * TimeHM.MILLISECONDS_PER_SECOND;

        TimeCalcWindow window = new TimeCalcWindow();

        TimeCalcButton configButton = new TimeCalcButton("Config");
        TimeCalcButton commandButton = new TimeCalcButton("Command");
        TimeCalcButton weatherButton = new TimeCalcButton("Weather");
        TimeCalcButton jokeButton = new TimeCalcButton("Joke");
        TimeCalcButton restartButton = new TimeCalcButton("Restart");
        TimeCalcButton exitButton = new TimeCalcButton("Exit");
        AboutButton aboutButton = new AboutButton();

        //window.add(weatherButton);
        window.addAll(configButton, commandButton, jokeButton, restartButton,
                exitButton);

        if(timeCalcConfiguration.visibilityOnlyGreyOrNoneEnabledProperty.isEnabled()) {
            timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
        }
        TimeCalcKeyAdapter timeCalcKeyAdapter = new TimeCalcKeyAdapter(timeCalcConfiguration, timeCalcApp, commandButton, window);
        window.addKeyListener(timeCalcKeyAdapter);

        AnalogClock analogClock = new AnalogClock(startTime, endTime);
        analogClock.setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 200);
        window.add(analogClock);

        ProgressSquare progressSquare = new ProgressSquare();
        progressSquare
                .setBounds(analogClock.getX() + analogClock.getWidth() + SwingUtils.MARGIN, analogClock.getY(),
                        200);
        window.add(progressSquare);

        ProgressCircle progressCircle = new ProgressCircle();
        progressCircle
                .setBounds(
                        progressSquare.getX() + progressSquare.getWidth() + SwingUtils.MARGIN, progressSquare.getY(), 80);
        window.add(progressCircle);

        WalkingHumanProgressAsciiArt walkingHumanProgressAsciiArt =
                new WalkingHumanProgressAsciiArt(analogClock.getX(), analogClock.getY() + analogClock.getHeight() + SwingUtils.MARGIN, 420, 180);
        window.add(walkingHumanProgressAsciiArt);
        weatherButton
                .setBounds(SwingUtils.MARGIN, walkingHumanProgressAsciiArt.getY()
                                              + walkingHumanProgressAsciiArt.getHeight()
                                              + SwingUtils.MARGIN);

        configButton.setBoundsFromTop(walkingHumanProgressAsciiArt);
        commandButton.setBoundsFromLeft(configButton);

        jokeButton.setBoundsFromLeft(commandButton);
        restartButton.setBoundsFromLeft(jokeButton);
        exitButton.setBoundsFromLeft(restartButton);
        aboutButton.setBounds(exitButton.getX(),
                exitButton.getY() + exitButton.getHeight() + SwingUtils.MARGIN);

        window.setLayout(null);
        window.setVisible(true);

        String windowTitle = "Time Calc " + Utils.getVersion();
        window.setTitle(windowTitle);

        weatherButton
                .addActionListener(e -> new WeatherWindow().setVisible(true));
        commandButton.addActionListener(new CommandActionListener(timeCalcApp, timeCalcConfiguration));

        jokeButton.addActionListener(e -> {
            for (int i = 1; i <= 1; i++) {
                Jokes.showRandom();
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        restartButton.addActionListener(e -> {
            window.setVisible(false);
            stopBeforeEnd = true;
        });

        configButton.addActionListener(e -> {
            if(configWindow == null) {
                this.configWindow = new ConfigWindow(timeCalcConfiguration);
            }
            configWindow.setVisible(true);
        });
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date());

        Properties testProperties = new Properties();
        File testPropertiesFile = new File("test.txt");
        try {
            if (testPropertiesFile.exists()) {
                testProperties.load(new FileInputStream(testPropertiesFile));
            }
        } catch (IOException ex) {
            Logger.getLogger(TimeCalcManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        Time time = new Time();

        bindToIfPropertyMissing(testProperties, "test.current.day", calNow, Calendar.DAY_OF_MONTH, analogClock.dayProperty, time.dayProperty);
        bindToIfPropertyMissing(testProperties, "test.current.month", calNow, Calendar.MONTH, analogClock.monthProperty, time.monthProperty);
        bindToIfPropertyMissing(testProperties, "test.current.year", calNow, Calendar.YEAR, analogClock.yearProperty, time.yearProperty);
        bindToIfPropertyMissing(testProperties, "test.current.hour", calNow, Calendar.HOUR, analogClock.hourProperty, time.hourProperty);
        bindToIfPropertyMissing(testProperties, "test.current.minute", calNow, Calendar.MINUTE, analogClock.minuteProperty, time.minuteProperty);
        bindToIfPropertyMissing(testProperties, "test.current.second", calNow, Calendar.SECOND, analogClock.secondProperty, time.secondProperty);
        bindToIfPropertyMissing(testProperties, "test.current.millisecond", calNow, Calendar.MILLISECOND, analogClock.millisecondProperty, time.millisecondProperty);

        if (testProperties.containsKey("test.current.year") || testProperties
                .containsKey("test.current.month") || testProperties
                    .containsKey("test.current.day")) {
            analogClock.dayOfWeekProperty
                    .setValue(calNow.get(Calendar.DAY_OF_WEEK));
        } else {
            analogClock.dayOfWeekProperty.bindTo(time.dayOfWeek);
        }

        analogClock.millisecondEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandMillisecondEnabledProperty);
        analogClock.secondEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandSecondEnabledProperty);
        analogClock.minuteEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandMinuteEnabledProperty);
        analogClock.handsLongProperty
                .bindTo(timeCalcConfiguration.clockHandLongProperty);

        Battery hourBattery = new HourBattery(progressCircle.getBounds().x,
                progressCircle.getY() + SwingUtils.MARGIN + progressCircle.getHeight(),
                140);
        window.add(hourBattery);

        Battery dayBattery = new DayBattery(hourBattery.getBounds().x + hourBattery.getWidth() + SwingUtils.MARGIN,
                hourBattery.getY(),
                140);
        window.add(dayBattery);

        Battery weekBattery = new WeekBattery(
                hourBattery.getBounds().x,
                dayBattery.getY() + dayBattery.getHeight() + SwingUtils.MARGIN, 140);
        window.add(weekBattery);

        int currentDayOfMonth = analogClock.dayProperty.getValue();

        int workDaysDone = 0;
        int workDaysTodo = 0;
        int workDaysTotal;
        for (int dayOfMonth = 1;
             dayOfMonth <= calNow.getActualMaximum(Calendar.DAY_OF_MONTH);
             dayOfMonth++) {
            DayOfWeek dayOfWeek = LocalDate.of(analogClock.yearProperty.getValue(),
                    analogClock.monthProperty.getValue(), dayOfMonth).getDayOfWeek();
            boolean weekend =
                    dayOfWeek.toString().equals("SATURDAY") || dayOfWeek
                            .toString().equals("SUNDAY");
            if (dayOfMonth < currentDayOfMonth && !weekend) {
                ++workDaysDone;
            }
            if (dayOfMonth > currentDayOfMonth && !weekend) {
                ++workDaysTodo;
            }
        }
        String currentDayOfWeekAsString = LocalDate
                .of(calNow.get(Calendar.YEAR), calNow.get(Calendar.MONTH) + 1,
                        currentDayOfMonth).getDayOfWeek().toString();
        boolean nowIsWeekend = currentDayOfWeekAsString.equals("SATURDAY")
                               || currentDayOfWeekAsString.equals("SUNDAY");
        workDaysTotal = workDaysDone + (nowIsWeekend ? 0 : 1) + workDaysTodo;

        Battery monthBattery = new MonthBattery(
                dayBattery.getBounds().x,
                weekBattery.getY(), 140);
        window.add(monthBattery);

        ComponentRegistry<JComponent> componentRegistry = new ComponentRegistry();
        componentRegistry.addAll(
                walkingHumanProgressAsciiArt,
                progressSquare,
                progressCircle,
                analogClock,
                dayBattery,
                weekBattery,
                monthBattery,
                hourBattery,
                configButton,
                jokeButton,
                commandButton,
                restartButton,
                exitButton
        );
        ComponentRegistry<TimeCalcButton> buttonRegistry = new ComponentRegistry();
        componentRegistry.getSet().stream().filter(c-> c instanceof TimeCalcButton).forEach(c->
                buttonRegistry.add((TimeCalcButton)c));
        componentRegistry.getSet().stream().filter(c ->
            GetProperty.class.isAssignableFrom(c.getClass())).forEach(c->
                ((GetProperty<String>)c).getProperty().bindTo(timeCalcApp.visibilityProperty));

        componentRegistry.getSet().stream().filter(c-> c instanceof Battery).forEach(c ->
            ((Battery)c).wavesProperty.bindTo(timeCalcConfiguration.batteryWavesEnabledProperty));

        componentRegistry.getSet().stream().filter(c-> c instanceof Widget).forEach(c ->
                ((Widget)c).smileysColoredProperty.bindTo(timeCalcConfiguration.smileysColoredProperty));
        window.setSize(dayBattery.getX() + dayBattery.getWidth() + 3 * SwingUtils.MARGIN,
                exitButton.getY() + 3 * exitButton.getHeight() + SwingUtils.MARGIN);
        while (true) {
            //System.out.println("timeCalcConfiguration.handsLongProperty=" + timeCalcConfiguration.clockHandLongProperty.isEnabled());
            Visibility currentVisibility = Visibility
                    .valueOf(timeCalcApp.visibilityProperty.getValue());
            if(timeCalcConfiguration.visibilityOnlyGreyOrNoneEnabledProperty.isEnabled() && currentVisibility.isColored() ){
                timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
            }
            if (stopBeforeEnd) {
                if(configWindow != null) {configWindow.setVisible(false);configWindow.dispose();}
                window.setVisible(false);
                window.dispose();

                break;
            }

            componentRegistry.setVisible(currentVisibility.isNotNone());

            jokeButton.setVisible(
                    TimeCalcProperties.getInstance().areJokesEnabled()
                    && !currentVisibility.isNone());

            window.setTitle(currentVisibility.isNone() ? "" : windowTitle);

            int hourNow = analogClock.hourProperty.getValue();
            int minuteNow = analogClock.minuteProperty.getValue();

            int secondNow = analogClock.secondProperty.getValue();
            int millisecondNow = analogClock.millisecondProperty.getValue();
            TimeHM timeRemains = new TimeHM(endTime.getHour() - hourNow,
                    endTime.getMinute() - minuteNow);

            int secondsRemains = 60 - secondNow;
            int millisecondsRemains = 1000 - millisecondNow;

            int hourDone = Constants.WORKING_HOURS_LENGTH + overtime.getHour()
                           - timeRemains.getHour();
            int minutesDone =
                    Constants.WORKING_MINUTES_LENGTH + overtime.getMinute()
                    - timeRemains.getMinute();
            int secondsDone = secondNow;
            int millisecondsDone = millisecondNow;

            int totalMinutesDone = hourDone * 60 + minutesDone;
            int totalSecondsDone = totalMinutesDone * 60 + secondsDone;
            int totalMillisecondsDone =
                    totalSecondsDone * 1000 + millisecondsDone;

            double done = ((double) totalMillisecondsDone)
                          / ((double) totalMilliseconds);
            progressSquare.setDonePercent(done);
            progressCircle.setDonePercent(done);
            dayBattery.setDonePercent(done);

            int weekDayWhenMondayIsOne = calNow.get(Calendar.DAY_OF_WEEK) - 1;
            weekBattery.setDonePercent(
                    WeekBattery.getWeekProgress(weekDayWhenMondayIsOne, done));
            weekBattery.setLabel(
                    nowIsWeekend ? "5/5" : (weekDayWhenMondayIsOne + "/5"));

            monthBattery.setDonePercent(MonthBattery
                    .getMonthProgress(weekDayWhenMondayIsOne, workDaysDone,
                            workDaysTotal, done));
            monthBattery.setLabel(
                    (nowIsWeekend ? workDaysDone : workDaysDone + 1) + "/"
                    + (workDaysTotal));

            hourBattery.setDonePercent(
                    HourBattery.getHourProgress(timeRemains, secondsRemains,
                            millisecondsRemains));
            if (!nowIsWeekend) {
                hourBattery.setLabel(
                        hourDone + "/" + (
                                totalMinutes / 60));
            }

            int totalSecondsRemains =
                    (timeRemains.getHour() * 60 * 60
                     + timeRemains.getMinute() * 60
                     + secondsRemains);
            int totalMillisecondsRemains =
                    totalSecondsRemains * 1000 + millisecondsRemains;
            double totalSecondsRemainsDouble =
                    ((double) totalMillisecondsRemains) / 1000;

            if (timeRemains.getHour() <= 0 && timeRemains.getMinute() <= 0) {
                Toaster toasterManager = new Toaster();
                toasterManager.setDisplayTime(30000);
                toasterManager.showToaster(
                        "Congratulation :-) It is the time to go home.");
                walkingHumanProgressAsciiArt
                        .printPercentToAscii(done, timeRemains.getHour(),
                                timeRemains.getMinute(), done,
                                totalSecondsRemainsDouble, endTime);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }
                while (!stopBeforeEnd) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            } else {
                walkingHumanProgressAsciiArt
                        .printPercentToAscii(done, timeRemains.getHour(),
                                timeRemains.getMinute(), done,
                                totalSecondsRemainsDouble, endTime);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        if(configWindow != null) {configWindow.setVisible(false);configWindow.dispose();}
        window.setVisible(false);
        window.dispose();
    }

    private void bindToIfPropertyMissing(Properties properties, String key, Calendar cal, int timeUnit, IntegerProperty firstProperty, Property secondProperty) {
        if (properties.containsKey(key)) {
            cal.set(timeUnit, Integer.parseInt(
                    (String) properties.get(key)) + (timeUnit == Calendar.MONTH ? -1 : 0));
            firstProperty.setValue(Integer.valueOf(
                    (String) properties.get(key)));
        } else {
            firstProperty.bindTo(secondProperty);
        }
    }

}
