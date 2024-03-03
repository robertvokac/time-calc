package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.CommandActionListener;
import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.app.TimeCalcApp;
import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.app.TimeCalcKeyAdapter;
import org.nanoboot.utils.timecalc.app.TimeCalcProperties;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.progress.AnalogClock;
import org.nanoboot.utils.timecalc.swing.progress.Battery;
import org.nanoboot.utils.timecalc.swing.progress.DayBattery;
import org.nanoboot.utils.timecalc.swing.progress.HourBattery;
import org.nanoboot.utils.timecalc.swing.progress.MinuteBattery;
import org.nanoboot.utils.timecalc.swing.progress.MonthBattery;
import org.nanoboot.utils.timecalc.swing.progress.ProgressCircle;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSquare;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.swing.progress.WalkingHumanProgress;
import org.nanoboot.utils.timecalc.swing.progress.WeekBattery;
import org.nanoboot.utils.timecalc.swing.progress.YearBattery;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TimeHM;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Component;
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
public class MainWindow extends TWindow {

    public static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    public static final Color FOREGROUND_COLOR = new Color(210, 210, 210);
    private final TButton workDaysButton;
    private final TButton activitiesButton;
    private final TButton exitButton;
    private final TButton configButton;
    private final TButton restartButton;
    private final TButton focusButton;
    private final TButton helpButton;
    private final TButton weatherButton;
    private final TButton commandButton;
    private final TButton jokeButton;
    private final AboutButton aboutButton;
    public static final JCheckBox hideShowCheckBox = new JCheckBox();
    private HelpWindow helpWindow = null;
    private ConfigWindow configWindow = null;
    private ActivitiesWindow activitiesWindow = null;
    private WorkDaysWindow workDaysWindow = null;

    private boolean stopBeforeEnd = false;
    private final TimeCalcConfiguration timeCalcConfiguration
            = new TimeCalcConfiguration();

    public MainWindow(String startTimeIn, String overTimeIn,
            TimeCalcApp timeCalcApp) {
        setFocusable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                timeCalcConfiguration.saveToTimeCalcProperties();
                System.exit(0);
            }
        });
        timeCalcConfiguration
                .loadFromTimeCalcProperties(TimeCalcProperties.getInstance());
        timeCalcConfiguration.mainWindowCustomTitleProperty.addListener(e -> {
            setTitle(getWindowTitle());
        });

        overTimeIn = (overTimeIn == null || overTimeIn.isEmpty())
                ? Constants.DEFAULT_OVERTIME : overTimeIn;

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

        this.configButton = new TButton("Config");
        this.workDaysButton = new TButton("Work Days");
        this.activitiesButton = new TButton("Activities"
                + "");
        this.restartButton = new TButton("Restart");
        this.exitButton = new TButton("Exit");
        this.focusButton = new TButton("Focus");
        this.helpButton = new TButton("Help");
        this.weatherButton = new TButton("Weather");
        this.commandButton = new TButton("Command");
        this.jokeButton = new TButton("Joke");
        this.aboutButton = new AboutButton();

        //window.add(weatherButton);
        addAll(configButton, workDaysButton, activitiesButton, restartButton,
                exitButton, focusButton, helpButton, commandButton, jokeButton, hideShowCheckBox);

        timeCalcApp.visibilityProperty.bindTo(timeCalcConfiguration.visibilityDefaultProperty);
        if (!timeCalcConfiguration.visibilitySupportedColoredProperty.isEnabled()) {
            timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
        }
        TimeCalcKeyAdapter timeCalcKeyAdapter = new TimeCalcKeyAdapter(timeCalcConfiguration, timeCalcApp, this);
        addKeyListener(timeCalcKeyAdapter);

        AnalogClock analogClock = new AnalogClock(startTime, endTime);
        analogClock.setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 200);
        add(analogClock);

        ProgressSquare progressSquare = new ProgressSquare();
        progressSquare
                .setBounds(analogClock.getX() + analogClock.getWidth() + SwingUtils.MARGIN, analogClock.getY(),
                        200);
        add(progressSquare);
        progressSquare.visibleProperty.bindTo(timeCalcConfiguration.squareVisibleProperty);

        ProgressCircle progressCircle = new ProgressCircle();
        progressCircle
                .setBounds(
                        progressSquare.getX() + progressSquare.getWidth() + SwingUtils.MARGIN, progressSquare.getY(), 80);
        add(progressCircle);
        progressCircle.visibleProperty.bindTo(timeCalcConfiguration.circleVisibleProperty);
        
        WalkingHumanProgress walkingHumanProgress
                = new WalkingHumanProgress();
        walkingHumanProgress.setBounds(analogClock.getX(), analogClock.getY() + analogClock.getHeight() + SwingUtils.MARGIN, 420, 180);
        add(walkingHumanProgress);
        walkingHumanProgress.visibleProperty.bindTo(timeCalcConfiguration.walkingHumanVisibleProperty);
        
        weatherButton
                .setBounds(SwingUtils.MARGIN, walkingHumanProgress.getY()
                                              + walkingHumanProgress.getHeight()
                                              + SwingUtils.MARGIN);

        configButton.setBoundsFromTop(walkingHumanProgress);
        workDaysButton.setBoundsFromLeft(configButton);
        activitiesButton.setBoundsFromLeft(workDaysButton);
        restartButton.setBoundsFromLeft(activitiesButton);
        exitButton.setBoundsFromLeft(restartButton);

        //
        helpButton.setBoundsFromTop(exitButton, 2);
        focusButton.setBoundsFromLeft(helpButton);
        commandButton.setBoundsFromLeft(focusButton);
        jokeButton.setBoundsFromLeft(commandButton);
        hideShowCheckBox.setSelected(true);
        hideShowCheckBox.setBounds(jokeButton.getX() + jokeButton.getWidth() + SwingUtils.MARGIN, jokeButton.getY(), 20, 20);
        //
        aboutButton.setBounds(exitButton.getX(),
                exitButton.getY() + exitButton.getHeight() + SwingUtils.MARGIN);

        setLayout(null);
        setVisible(true);

        setTitle(getWindowTitle());

        weatherButton
                .addActionListener(e -> new WeatherWindow().setVisible(true));
        commandButton.addActionListener(new CommandActionListener(timeCalcApp, timeCalcConfiguration));

        jokeButton.addActionListener(e -> {
            for (int i = 1; i <= 1; i++) {
                Jokes.showRandom();
            }
        });
        hideShowCheckBox.addItemListener(e -> 
        {
        });
        exitButton.addActionListener(e
                -> {
            timeCalcConfiguration.saveToTimeCalcProperties();
            System.exit(0);
        });
        focusButton.addActionListener(e -> requestFocus(true));
        restartButton.addActionListener(e -> {
            setVisible(false);
            timeCalcConfiguration.saveToTimeCalcProperties();
            stopBeforeEnd = true;
        });
        workDaysButton.addActionListener(e -> {
            if (workDaysWindow == null) {
                this.workDaysWindow = new WorkDaysWindow();
            }
            workDaysWindow.setVisible(true);
        });
        activitiesButton.addActionListener(e -> {
            if (activitiesWindow == null) {
                this.activitiesWindow = new ActivitiesWindow();
            }
            activitiesWindow.setVisible(true);
        });

        configButton.addActionListener(e -> {
            if (configWindow == null) {
                this.configWindow = new ConfigWindow(timeCalcConfiguration);
            }
            configWindow.setVisible(true);
            configWindow.setLocationRelativeTo(this);
            configWindow.setLocation(100, 100);
        });

        helpButton.addActionListener(e -> {
            if (helpWindow == null) {
                this.helpWindow = new HelpWindow();
            }
            helpWindow.setVisible(true);
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
            Logger.getLogger(MainWindow.class.getName())
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
                .bindTo(timeCalcConfiguration.clockHandsMillisecondVisibleProperty);
        analogClock.secondEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsSecondVisibleProperty);
        analogClock.minuteEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsMinuteVisibleProperty);
        analogClock.hourEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsHourVisibleProperty);
        analogClock.handsLongProperty
                .bindTo(timeCalcConfiguration.clockHandsLongVisibleProperty);
        analogClock.borderVisibleProperty.bindTo(timeCalcConfiguration.clockBorderVisibleProperty);
        analogClock.borderOnlyHoursProperty.bindTo(timeCalcConfiguration.clockBorderOnlyHoursProperty);
        analogClock.numbersVisibleProperty.bindTo(timeCalcConfiguration.clockNumbersVisibleProperty);
        analogClock.circleVisibleProperty.bindTo(timeCalcConfiguration.clockCircleVisibleProperty);
        analogClock.circleStrongBorderProperty.bindTo(timeCalcConfiguration.clockCircleStrongBorderProperty);
        analogClock.centreCircleVisibleProperty.bindTo(timeCalcConfiguration.clockCentreCircleVisibleProperty);
        analogClock.centreCircleBorderColorProperty.bindTo(timeCalcConfiguration.clockCircleBorderColorProperty);
        analogClock.handsColoredProperty.bindTo(timeCalcConfiguration.clockHandsColoredProperty);
        analogClock.centreCircleBlackProperty.bindTo(timeCalcConfiguration.clockCentreCircleBlackProperty);
        analogClock.progressVisibleOnlyIfMouseMovingOverProperty.bindTo(timeCalcConfiguration.clockProgressVisibleOnlyIfMouseMovingOverProperty);
        analogClock.dateVisibleOnlyIfMouseMovingOverProperty.bindTo(timeCalcConfiguration.clockDateVisibleOnlyIfMouseMovingOverProperty);
        analogClock.visibleProperty.bindTo(timeCalcConfiguration.clockVisibleProperty);
        
        MinuteBattery minuteBattery = new MinuteBattery(progressCircle.getBounds().x,
                progressCircle.getY() + SwingUtils.MARGIN + progressCircle.getHeight(), 140);
        add(minuteBattery);
        Battery hourBattery = new HourBattery(minuteBattery.getBounds().x + minuteBattery.getWidth() + SwingUtils.MARGIN,
                minuteBattery.getY(),
                140);
        add(hourBattery);

        Battery dayBattery = new DayBattery(hourBattery.getBounds().x + hourBattery.getWidth() + SwingUtils.MARGIN,
                hourBattery.getY(),
                140);
        add(dayBattery);

        Battery weekBattery = new WeekBattery(
                minuteBattery.getBounds().x,
                dayBattery.getY() + dayBattery.getHeight() + SwingUtils.MARGIN, 140);
        add(weekBattery);

        int currentDayOfMonth = analogClock.dayProperty.getValue();

        int workDaysDone = 0;
        int workDaysTodo = 0;
        int workDaysTotal;
        for (int dayOfMonth = 1;
                dayOfMonth <= calNow.getActualMaximum(Calendar.DAY_OF_MONTH);
                dayOfMonth++) {
            DayOfWeek dayOfWeek = LocalDate.of(analogClock.yearProperty.getValue(),
                    analogClock.monthProperty.getValue(), dayOfMonth).getDayOfWeek();
            boolean weekend
                    = dayOfWeek.toString().equals("SATURDAY") || dayOfWeek
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
                weekBattery.getBounds().x + weekBattery.getWidth() + SwingUtils.MARGIN,
                weekBattery.getY(), 140);
        add(monthBattery);
        Battery yearBattery = new YearBattery(
                monthBattery.getBounds().x + monthBattery.getWidth() + SwingUtils.MARGIN,
                monthBattery.getY(), 140);
        add(yearBattery);

        ComponentRegistry<Component> componentRegistry = new ComponentRegistry();
        componentRegistry.addAll(this.getContentPane().getComponents());

        ComponentRegistry<TButton> buttonRegistry = new ComponentRegistry();
        componentRegistry.getSet().stream().filter(c -> c instanceof TButton).forEach(c
                -> {
            buttonRegistry.add((TButton) c);
            
        });
//        commandButton.visibleProperty.bindTo(timeCalcConfiguration.commandsVisibleProperty);
//        jokeButton.visibleProperty.bindTo(timeCalcConfiguration.jokesVisibleProperty);
        componentRegistry.getSet().stream().filter(c
                -> GetProperty.class.isAssignableFrom(c.getClass())).forEach(c
                -> {
            ((GetProperty) c).getVisibilityProperty().bindTo(timeCalcApp.visibilityProperty);
            ((GetProperty) c).getVisibilitySupportedColoredProperty().bindTo(timeCalcConfiguration.visibilitySupportedColoredProperty);
        });

        componentRegistry.getSet().stream().filter(c -> c instanceof Battery).forEach(c
                -> {
            Battery battery = ((Battery) c);
            battery.wavesVisibleProperty.bindTo(timeCalcConfiguration.batteryWavesVisibleProperty);
            battery.circleProgressVisibleProperty.bindTo(timeCalcConfiguration.batteryCircleProgressProperty);
            battery.percentProgressVisibleProperty.bindTo(timeCalcConfiguration.batteryPercentProgressProperty);
            battery.chargingCharacterVisibleProperty.bindTo(timeCalcConfiguration.batteryChargingCharacterVisibleProperty);
            battery.nameVisibleProperty.bindTo(timeCalcConfiguration.batteryNameVisibleProperty);
            battery.labelVisibleProperty.bindTo(timeCalcConfiguration.batteryLabelVisibleProperty);
            battery.blinkingIfCriticalLowVisibleProperty.bindTo(timeCalcConfiguration.batteryBlinkingIfCriticalLowVisibleProperty);
            switch(battery.getName()) {
                case MinuteBattery.MINUTE : battery.visibleProperty.bindTo(timeCalcConfiguration.batteryMinuteVisibleProperty);break;
                case HourBattery.HOUR : battery.visibleProperty.bindTo(timeCalcConfiguration.batteryHourVisibleProperty);break;
                case DayBattery.DAY : battery.visibleProperty.bindTo(timeCalcConfiguration.batteryDayVisibleProperty);break;
                case WeekBattery.WEEK : battery.visibleProperty.bindTo(timeCalcConfiguration.batteryWeekVisibleProperty);break;
                case MonthBattery.MONTH : battery.visibleProperty.bindTo(timeCalcConfiguration.batteryMonthVisibleProperty);break;
                case YearBattery.YEAR : battery.visibleProperty.bindTo(timeCalcConfiguration.batteryYearVisibleProperty);break;
                default: {}
            };
        });
        

        componentRegistry.getSet().stream().filter(c -> c instanceof Widget).forEach(c
                -> {
            Widget widget = (Widget) c;
            widget.smileysVisibleProperty.bindTo(timeCalcConfiguration.smileysVisibleProperty);
            widget.smileysVisibleOnlyIfMouseMovingOverProperty.bindTo(timeCalcConfiguration.smileysVisibleOnlyIfMouseMovingOverProperty);
            widget.smileysColoredProperty.bindTo(timeCalcConfiguration.smileysColoredProperty);
        }
        );
        setSize(dayBattery.getX() + dayBattery.getWidth() + 3 * SwingUtils.MARGIN,
                focusButton.getY() + focusButton.getHeight() + SwingUtils.MARGIN + focusButton.getHeight() + 2 * SwingUtils.MARGIN);
        
        while (true) {
            //System.out.println("timeCalcConfiguration.handsLongProperty=" + timeCalcConfiguration.clockHandLongProperty.isEnabled());

            Visibility currentVisibility = Visibility
                    .valueOf(timeCalcApp.visibilityProperty.getValue());
            if (!timeCalcConfiguration.visibilitySupportedColoredProperty.isEnabled() && currentVisibility.isColored()) {
                timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
            }
            if (stopBeforeEnd) {
                if (configWindow != null) {
                    configWindow.setVisible(false);
                    configWindow.dispose();
                }
                if (workDaysWindow != null) {
                    workDaysWindow.setVisible(false);
                    workDaysWindow.dispose();
                }
                if (activitiesWindow != null) {
                    activitiesWindow.setVisible(false);
                    activitiesWindow.dispose();
                }
                if (helpWindow != null) {
                    helpWindow.setVisible(false);
                    helpWindow.dispose();
                }

                timeCalcConfiguration.saveToTimeCalcProperties();
                setVisible(false);
                dispose();

                break;
            }

            componentRegistry.setVisible(c -> (c instanceof Widget ? ((Widget)c).visibleProperty.isEnabled()
                    : true) /*|| (c instanceof TButton ? ((Widget)c).visibleProperty.isEnabled()
                    : true)*/, currentVisibility.isNotNone());

            jokeButton.setVisible(
                    TimeCalcProperties.getInstance().getBooleanProperty(
                            TimeCalcProperty.JOKES_VISIBLE)
                    && !currentVisibility.isNone() && MainWindow.hideShowCheckBox.isSelected());

            setTitle(currentVisibility.isNone() ? "" : getWindowTitle());

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
            int minutesDone
                    = Constants.WORKING_MINUTES_LENGTH + overtime.getMinute()
                    - timeRemains.getMinute();
            int secondsDone = secondNow;
            int millisecondsDone = millisecondNow;

            int totalMinutesDone = hourDone * 60 + minutesDone;
            int totalSecondsDone = totalMinutesDone * 60 + secondsDone;
            int totalMillisecondsDone
                    = totalSecondsDone * 1000 + millisecondsDone;

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
                        hourDone + "/" + (totalMinutes / 60));
            }
            minuteBattery.setDonePercent(MinuteBattery.getMinuteProgress(secondNow, millisecondNow));
            yearBattery.setDonePercent(YearBattery.getYearProgress(analogClock));
            yearBattery.setLabel("");
            //yearBattery.setDonePercent(YearBattery.getYearProgress(2024,0,1,0,0,0,0));
            int totalSecondsRemains
                    = (timeRemains.getHour() * 60 * 60
                    + timeRemains.getMinute() * 60
                    + secondsRemains);
            int totalMillisecondsRemains
                    = totalSecondsRemains * 1000 + millisecondsRemains;
            double totalSecondsRemainsDouble
                    = ((double) totalMillisecondsRemains) / 1000;

            if (timeRemains.getHour() <= 0 && timeRemains.getMinute() <= 0) {
                Toaster toasterManager = new Toaster();
                toasterManager.setDisplayTime(30000);
                toasterManager.showToaster(
                        "Congratulation :-) It is the time to go home.");
                walkingHumanProgress
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
                walkingHumanProgress
                        .printPercentToAscii(done, timeRemains.getHour(),
                                timeRemains.getMinute(), done,
                                totalSecondsRemainsDouble, endTime);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        if (configWindow != null) {
            configWindow.setVisible(false);
            configWindow.dispose();
        }
        if (workDaysWindow != null) {
            workDaysWindow.setVisible(false);
            workDaysWindow.dispose();
        }
        if (activitiesWindow != null) {
            activitiesWindow.setVisible(false);
            activitiesWindow.dispose();
        }
        if (helpWindow != null) {
            helpWindow.setVisible(false);
            helpWindow.dispose();
        }

        timeCalcConfiguration.saveToTimeCalcProperties();
        setVisible(false);
        dispose();
    }

    private String getWindowTitle() {
        if (timeCalcConfiguration.mainWindowCustomTitleProperty.getValue()
                .equals(ConfigWindow.THREE_DASHES)) {
            return "Time Calc " + Utils.getVersion();

        } else {
            return String.valueOf(
                    timeCalcConfiguration.mainWindowCustomTitleProperty
                            .getValue());
        }

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

    public void openWorkDaysWindow() {
        workDaysButton.doClick();
    }

    public void openActivitiesWindow() {
        activitiesButton.doClick();
    }

    public void doExit() {
        exitButton.doClick();
    }

    public void openConfigWindow() {
        configButton.doClick();
    }

    public void doRestart() {
        restartButton.doClick();
    }

    public void doCommand() {
        commandButton.doClick();
    }

    public void openHelpWindow() {
        helpButton.doClick();
    }
    
    public void doEnableEverything() {
        if(this.configWindow == null) {
            openConfigWindow();
            this.configWindow.setVisible(false);
        }
        
        this.configWindow.doEnableEverything();
    }
    public void doDisableAlmostEverything() {
        
        if(this.configWindow == null) {
            openConfigWindow();
            this.configWindow.setVisible(false);
        }
        
        this.configWindow.doDisableAlmostEverything();
    }
}
