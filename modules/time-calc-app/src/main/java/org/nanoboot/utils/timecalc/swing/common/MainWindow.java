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
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

/**
 * @author Robert Vokac
 * @since 08.02.2024
 */
public class MainWindow extends TWindow {

    public static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    public static final Color FOREGROUND_COLOR = new Color(210, 210, 210);
    public static final JCheckBox hideShowFormsCheckBox = new JCheckBox();
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
    private final TimeCalcConfiguration timeCalcConfiguration
            = new TimeCalcConfiguration();
    private final TTextField arrivalTextField;
    private final TTextField overtimeTextField;
    private final TCheckBox halfDayCheckBox;
    private final TTextField pauseTimeInMinutesTextField;
    private final TTextField noteTextField;
    private final TTextField departureTextField;
    private final TTextField elapsedTextField;
    private final TTextField remainingTextField;
    private HelpWindow helpWindow = null;
    private ConfigWindow configWindow = null;
    private ActivitiesWindow activitiesWindow = null;
    private WorkingDaysWindow workingDaysWindow = null;
    private boolean stopBeforeEnd = false;

    {
        this.arrivalTextField = new TTextField();
        this.overtimeTextField = new TTextField();
        this.halfDayCheckBox = new TCheckBox("Half day:", false);
        this.pauseTimeInMinutesTextField = new TTextField("30");
        this.noteTextField = new TTextField();
        this.departureTextField = new TTextField();
        this.elapsedTextField = new TTextField("", 100);
        this.remainingTextField = new TTextField("", 100);
    }
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

        arrivalTextField.valueProperty.setValue(startTimeIn);
        overtimeTextField.valueProperty.setValue(overTimeIn);

        arrivalTextField.addVetoableChangeListener(e -> {
            String newValue = (String) e.getNewValue();
            if(newValue.isEmpty()) {
                throw new PropertyVetoException("Arrival must not be empty.", new PropertyChangeEvent(e.getSource(), e.getPropertyName(), e.getOldValue(), e.getNewValue()));
            }
        });

        overtimeTextField.addVetoableChangeListener(e -> {
            String newValue = (String) e.getNewValue();
            if(newValue.isEmpty()) {
                throw new PropertyVetoException("Overtime must not be empty.", new PropertyChangeEvent(e.getSource(), e.getPropertyName(), e.getOldValue(), e.getNewValue()));
            }
        });
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
                exitButton, focusButton, helpButton, commandButton, jokeButton,
                hideShowFormsCheckBox);

        timeCalcApp.visibilityProperty
                .bindTo(timeCalcConfiguration.visibilityDefaultProperty);
        if (!timeCalcConfiguration.visibilitySupportedColoredProperty
                .isEnabled()) {
            timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
        }
        Time time = new Time();
        TimeCalcKeyAdapter timeCalcKeyAdapter =
                new TimeCalcKeyAdapter(timeCalcConfiguration, timeCalcApp,
                        this, time);
        addKeyListener(timeCalcKeyAdapter);

        AnalogClock analogClock = new AnalogClock();

        {
            arrivalTextField.valueProperty.addListener(e -> {
                if (!arrivalTextField.valueProperty.getValue().isEmpty()) {
                    TTime startTime_ = arrivalTextField.asTTime();
                    analogClock.startHourProperty
                            .setValue(startTime_.getHour());
                    analogClock.startMinuteProperty
                            .setValue(startTime_.getMinute());
                }

            });
            departureTextField.valueProperty.addListener(e -> {
                if (!departureTextField.valueProperty.getValue().isEmpty()) {
                    TTime endTime = arrivalTextField.asTTime();
                    analogClock.endHourProperty
                            .setValue(endTime.getHour());
                    analogClock.endMinuteProperty
                            .setValue(endTime.getMinute());
                }
            });
        }
        analogClock.setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 200);
        add(analogClock);

        ProgressSquare progressSquare = new ProgressSquare();
        progressSquare
                .setBounds(analogClock.getX() + analogClock.getWidth()
                           + SwingUtils.MARGIN, analogClock.getY(),
                        200);
        add(progressSquare);
        progressSquare.visibleProperty
                .bindTo(timeCalcConfiguration.squareVisibleProperty);

        ProgressCircle progressCircle = new ProgressCircle();
        progressCircle
                .setBounds(
                        progressSquare.getX() + progressSquare.getWidth()
                        + SwingUtils.MARGIN, progressSquare.getY(), 80);
        add(progressCircle);
        progressCircle.visibleProperty
                .bindTo(timeCalcConfiguration.circleVisibleProperty);

        WalkingHumanProgress walkingHumanProgress
                = new WalkingHumanProgress();
        walkingHumanProgress.setBounds(analogClock.getX(),
                analogClock.getY() + analogClock.getHeight()
                + SwingUtils.MARGIN, 420, 180);
        add(walkingHumanProgress);
        walkingHumanProgress.visibleProperty
                .bindTo(timeCalcConfiguration.walkingHumanVisibleProperty);

        weatherButton
                .setBounds(SwingUtils.MARGIN, walkingHumanProgress.getY()
                                              + walkingHumanProgress.getHeight()
                                              + SwingUtils.MARGIN);

        TLabel arrivalTextFieldLabel = new TLabel("Arrival:");
        arrivalTextFieldLabel.setBoundsFromTop(walkingHumanProgress);

        arrivalTextField.setBoundsFromLeft(arrivalTextFieldLabel);
        //
        TLabel overtimeTextFieldLabel = new TLabel("Overtime:");
        overtimeTextFieldLabel.setBoundsFromLeft(arrivalTextField);

        overtimeTextField.setBoundsFromLeft(overtimeTextFieldLabel);
        //

        halfDayCheckBox.setBoundsFromLeft(overtimeTextField);
        //
        TLabel pauseTimeInMinutesFieldLabel = new TLabel("Pause:");
        pauseTimeInMinutesFieldLabel.setBoundsFromLeft(halfDayCheckBox);

        pauseTimeInMinutesTextField.setBoundsFromLeft(pauseTimeInMinutesFieldLabel);
        //
        TLabel noteTextFieldLabel = new TLabel("Note:");
        noteTextFieldLabel.setBoundsFromLeft(pauseTimeInMinutesTextField);

        noteTextField.setBoundsFromLeft(noteTextFieldLabel);
        //half day, pause time in minutes, note
        arrivalTextField.setEditable(false);
        overtimeTextField.setEditable(false);

        add(arrivalTextFieldLabel);
        add(arrivalTextField);
        add(overtimeTextFieldLabel);
        add(overtimeTextField);
        add(halfDayCheckBox);

        add(pauseTimeInMinutesFieldLabel);
        add(pauseTimeInMinutesTextField);
        add(noteTextFieldLabel);
        add(noteTextField);
        //
        TLabel departureTextFieldLabel = new TLabel("Departure:");
        departureTextFieldLabel.setBoundsFromTop(arrivalTextFieldLabel);

        departureTextField.setBoundsFromLeft(departureTextFieldLabel);
        departureTextField.setEditable(false);
        //
        TLabel elapsedTextFieldLabel = new TLabel("Elapsed:");
        elapsedTextFieldLabel.setBoundsFromLeft(departureTextField);

        elapsedTextField.setBoundsFromLeft(elapsedTextFieldLabel);
        elapsedTextField.setEditable(false);
        elapsedTextField.setEditable(false);
        //
        TLabel remainingTextFieldLabel = new TLabel("Remaining:", 100);
        remainingTextFieldLabel.setBoundsFromLeft(elapsedTextField);

        remainingTextField.setBoundsFromLeft(remainingTextFieldLabel);
        remainingTextField.setEditable(false);
        TButton saveButton = new TButton("Save", 80);
        saveButton.setBoundsFromLeft(remainingTextField);
        //

        add(departureTextFieldLabel);
        add(departureTextField);
        add(elapsedTextFieldLabel);
        add(elapsedTextField);
        add(remainingTextFieldLabel);
        add(remainingTextField);
        add(saveButton);
        saveButton.addActionListener(e -> {
            TTime overtime_ =overtimeTextField.asTTime();
            Utils.writeTextToFile(FileConstants.STARTTIME_TXT, arrivalTextField.asTTime().toString().substring(0,5));
            Utils.writeTextToFile(FileConstants.OVERTIME_TXT, overtime_.toString().substring(0,overtime_.isNegative() ? 6 : 5));
        });
        //
        configButton.setBoundsFromTop(departureTextFieldLabel);
        workDaysButton.setBoundsFromLeft(configButton);
        activitiesButton.setBoundsFromLeft(workDaysButton);
        restartButton.setBoundsFromLeft(activitiesButton);
        exitButton.setBoundsFromLeft(restartButton);

        //
        helpButton.setBoundsFromTop(exitButton, 2);
        focusButton.setBoundsFromLeft(helpButton);
        commandButton.setBoundsFromLeft(focusButton);
        jokeButton.setBoundsFromLeft(commandButton);
        hideShowFormsCheckBox.setSelected(true);
        hideShowFormsCheckBox.setBounds(
                jokeButton.getX() + jokeButton.getWidth() + SwingUtils.MARGIN,
                jokeButton.getY(), 20, 20);
        //
        aboutButton.setBounds(exitButton.getX(),
                exitButton.getY() + exitButton.getHeight() + SwingUtils.MARGIN);

        setLayout(null);
        setVisible(true);

        setTitle(getWindowTitle());

        weatherButton
                .addActionListener(e -> new WeatherWindow().setVisible(true));
        commandButton.addActionListener(
                new CommandActionListener(timeCalcApp, timeCalcConfiguration));

        jokeButton.addActionListener(e -> {
            for (int i = 1; i <= 1; i++) {
                Jokes.showRandom();
            }
        });
        hideShowFormsCheckBox.addItemListener(e -> this.requestFocus());
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
            if (workingDaysWindow == null) {
                this.workingDaysWindow = new WorkingDaysWindow();
            }
            workingDaysWindow.setVisible(true);
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

        time.yearCustomProperty
                .bindTo(timeCalcConfiguration.testYearCustomProperty);
        time.monthCustomProperty
                .bindTo(timeCalcConfiguration.testMonthCustomProperty);
        time.dayCustomProperty
                .bindTo(timeCalcConfiguration.testDayCustomProperty);
        time.hourCustomProperty
                .bindTo(timeCalcConfiguration.testHourCustomProperty);
        time.minuteCustomProperty
                .bindTo(timeCalcConfiguration.testMinuteCustomProperty);
        time.secondCustomProperty
                .bindTo(timeCalcConfiguration.testSecondCustomProperty);
        time.millisecondCustomProperty
                .bindTo(timeCalcConfiguration.testMillisecondCustomProperty);
        time.allowCustomValuesProperty.setValue(true);
        analogClock.dayProperty.bindTo(time.dayProperty);
        analogClock.monthProperty.bindTo(time.monthProperty);
        analogClock.yearProperty.bindTo(time.yearProperty);
        analogClock.hourProperty.bindTo(time.hourProperty);
        analogClock.minuteProperty.bindTo(time.minuteProperty);
        analogClock.secondProperty.bindTo(time.secondProperty);
        analogClock.millisecondProperty.bindTo(time.millisecondProperty);

        analogClock.dayOfWeekProperty.bindTo(time.dayOfWeekProperty);

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
        analogClock.borderVisibleProperty
                .bindTo(timeCalcConfiguration.clockBorderVisibleProperty);
        analogClock.borderOnlyHoursProperty
                .bindTo(timeCalcConfiguration.clockBorderOnlyHoursProperty);
        analogClock.numbersVisibleProperty
                .bindTo(timeCalcConfiguration.clockNumbersVisibleProperty);
        analogClock.circleVisibleProperty
                .bindTo(timeCalcConfiguration.clockCircleVisibleProperty);
        analogClock.circleStrongBorderProperty
                .bindTo(timeCalcConfiguration.clockCircleStrongBorderProperty);
        analogClock.centreCircleVisibleProperty
                .bindTo(timeCalcConfiguration.clockCentreCircleVisibleProperty);
        analogClock.centreCircleBorderColorProperty
                .bindTo(timeCalcConfiguration.clockCircleBorderColorProperty);
        analogClock.handsColoredProperty
                .bindTo(timeCalcConfiguration.clockHandsColoredProperty);
        analogClock.centreCircleBlackProperty
                .bindTo(timeCalcConfiguration.clockCentreCircleBlackProperty);
        analogClock.progressVisibleOnlyIfMouseMovingOverProperty
                .bindTo(timeCalcConfiguration.clockProgressVisibleOnlyIfMouseMovingOverProperty);
        analogClock.dateVisibleOnlyIfMouseMovingOverProperty
                .bindTo(timeCalcConfiguration.clockDateVisibleOnlyIfMouseMovingOverProperty);
        analogClock.visibleProperty
                .bindTo(timeCalcConfiguration.clockVisibleProperty);

        MinuteBattery minuteBattery =
                new MinuteBattery(progressCircle.getBounds().x,
                        progressCircle.getY() + SwingUtils.MARGIN
                        + progressCircle.getHeight(), 140);
        add(minuteBattery);
        Battery hourBattery = new HourBattery(
                minuteBattery.getBounds().x + minuteBattery.getWidth()
                + SwingUtils.MARGIN,
                minuteBattery.getY(),
                140);
        add(hourBattery);

        Battery dayBattery = new DayBattery(
                hourBattery.getBounds().x + hourBattery.getWidth()
                + SwingUtils.MARGIN,
                hourBattery.getY(),
                140);
        add(dayBattery);

        Battery weekBattery = new WeekBattery(
                minuteBattery.getBounds().x,
                dayBattery.getY() + dayBattery.getHeight() + SwingUtils.MARGIN,
                140);
        add(weekBattery);

        Battery monthBattery = new MonthBattery(
                weekBattery.getBounds().x + weekBattery.getWidth()
                + SwingUtils.MARGIN,
                weekBattery.getY(), 140);
        add(monthBattery);
        Battery yearBattery = new YearBattery(
                monthBattery.getBounds().x + monthBattery.getWidth()
                + SwingUtils.MARGIN,
                monthBattery.getY(), 140);
        add(yearBattery);

        ComponentRegistry<Component> componentRegistry =
                new ComponentRegistry();
        componentRegistry.addAll(this.getContentPane().getComponents());

        ComponentRegistry<TButton> buttonRegistry = new ComponentRegistry();
        componentRegistry.getSet().stream().filter(c -> c instanceof TButton)
                .forEach(c
                        -> {
                    buttonRegistry.add((TButton) c);

                });
        //        commandButton.visibleProperty.bindTo(timeCalcConfiguration.commandsVisibleProperty);
        //        jokeButton.visibleProperty.bindTo(timeCalcConfiguration.jokesVisibleProperty);
        componentRegistry.getSet().stream().filter(c
                -> GetProperty.class.isAssignableFrom(c.getClass())).forEach(c
                -> {
            ((GetProperty) c).getVisibilityProperty()
                    .bindTo(timeCalcApp.visibilityProperty);
            ((GetProperty) c).getVisibilitySupportedColoredProperty()
                    .bindTo(timeCalcConfiguration.visibilitySupportedColoredProperty);
        });

        componentRegistry.getSet().stream().filter(c -> c instanceof Battery)
                .forEach(c
                        -> {
                    Battery battery = ((Battery) c);
                    battery.wavesVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryWavesVisibleProperty);
                    battery.circleProgressVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryCircleProgressProperty);
                    battery.percentProgressVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryPercentProgressProperty);
                    battery.chargingCharacterVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryChargingCharacterVisibleProperty);
                    battery.nameVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryNameVisibleProperty);
                    battery.labelVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryLabelVisibleProperty);
                    battery.blinkingIfCriticalLowVisibleProperty
                            .bindTo(timeCalcConfiguration.batteryBlinkingIfCriticalLowVisibleProperty);
                    switch (battery.getName()) {
                        case MinuteBattery.MINUTE:
                            battery.visibleProperty
                                    .bindTo(timeCalcConfiguration.batteryMinuteVisibleProperty);
                            break;
                        case HourBattery.HOUR:
                            battery.visibleProperty
                                    .bindTo(timeCalcConfiguration.batteryHourVisibleProperty);
                            break;
                        case DayBattery.DAY:
                            battery.visibleProperty
                                    .bindTo(timeCalcConfiguration.batteryDayVisibleProperty);
                            break;
                        case WeekBattery.WEEK:
                            battery.visibleProperty
                                    .bindTo(timeCalcConfiguration.batteryWeekVisibleProperty);
                            break;
                        case MonthBattery.MONTH:
                            battery.visibleProperty
                                    .bindTo(timeCalcConfiguration.batteryMonthVisibleProperty);
                            break;
                        case YearBattery.YEAR:
                            battery.visibleProperty
                                    .bindTo(timeCalcConfiguration.batteryYearVisibleProperty);
                            break;
                        default: {
                        }
                    }
                });

        componentRegistry.getSet().stream().filter(c -> c instanceof Widget)
                .forEach(c
                                -> {
                            Widget widget = (Widget) c;
                            widget.smileysVisibleProperty
                                    .bindTo(timeCalcConfiguration.smileysVisibleProperty);
                            widget.smileysVisibleOnlyIfMouseMovingOverProperty
                                    .bindTo(timeCalcConfiguration.smileysVisibleOnlyIfMouseMovingOverProperty);
                            widget.smileysColoredProperty
                                    .bindTo(timeCalcConfiguration.smileysColoredProperty);
                        }
                );
        setSize(dayBattery.getX() + dayBattery.getWidth()
                + 3 * SwingUtils.MARGIN,
                focusButton.getY() + focusButton.getHeight() + SwingUtils.MARGIN
                + focusButton.getHeight() + 2 * SwingUtils.MARGIN);


        while (true) {
            //System.out.println("timeCalcConfiguration.handsLongProperty=" + timeCalcConfiguration.clockHandLongProperty.isEnabled());
            if(!departureTextField.valueProperty.getValue().isEmpty()){
                TTime startTime = arrivalTextField.asTTime();
                TTime endTime = departureTextField.asTTime();
                analogClock.startHourProperty.setValue(startTime.getHour());
                analogClock.startMinuteProperty.setValue(startTime.getMinute());
                analogClock.endHourProperty.setValue(endTime.getHour());
                analogClock.endMinuteProperty.setValue(endTime.getMinute());
            }
            {

                TTime startTime = arrivalTextField.asTTime();
                TTime overtime = overtimeTextField.asTTime();
                TTime newDeparture = startTime.add(new TTime(8,30));
                if(overtime.isNegative()) {
                    TTime tmpTTime = overtime.cloneInstance();
                    tmpTTime.setNegative(false);
                    newDeparture = newDeparture.remove(tmpTTime);
                } else {
                    newDeparture = newDeparture.add(overtime);
                }
                departureTextField.valueProperty.setValue(newDeparture.toString().substring(0, 5));
            }
            Visibility currentVisibility = Visibility
                    .valueOf(timeCalcApp.visibilityProperty.getValue());
            if (!timeCalcConfiguration.visibilitySupportedColoredProperty
                    .isEnabled() && currentVisibility.isColored()) {
                timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
            }
            if (stopBeforeEnd) {
                if (configWindow != null) {
                    configWindow.setVisible(false);
                    configWindow.dispose();
                }
                if (workingDaysWindow != null) {
                    workingDaysWindow.setVisible(false);
                    workingDaysWindow.dispose();
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

            componentRegistry.setVisible(c -> (!(c instanceof Widget)
                                               || ((Widget) c).visibleProperty
                                                       .isEnabled()) /*|| (c instanceof TButton ? ((Widget)c).visibleProperty.isEnabled()
                    : true)*/, currentVisibility.isNotNone());

            jokeButton.setVisible(
                    TimeCalcProperties.getInstance().getBooleanProperty(
                            TimeCalcProperty.JOKES_VISIBLE)
                    && !currentVisibility.isNone()
                    && MainWindow.hideShowFormsCheckBox.isSelected());

            setTitle(currentVisibility.isNone() ? "" : getWindowTitle());

            int hourNow = analogClock.hourProperty.getValue();
            int minuteNow = analogClock.minuteProperty.getValue();

            int secondNow = analogClock.secondProperty.getValue();
            int millisecondNow = analogClock.millisecondProperty.getValue();

            TTime startTime = arrivalTextField.asTTime();
            TTime endTime = departureTextField.asTTime();
            TTime nowTime = TTime.of(time.asCalendar());
            TTime timeElapsed = TTime
                    .computeTimeDiff(startTime, nowTime);
            TTime timeRemains = TTime.computeTimeDiff(nowTime, endTime);
            String timeElapsedString = timeElapsed.toString();
            String timeRemainsString = timeRemains.toString();

            int secondsRemains = 60 - secondNow;
            int millisecondsRemains = 1000 - millisecondNow;
            if (!remainingTextField.valueProperty.getValue()
                    .equals(timeRemainsString)) {
                remainingTextField.valueProperty.setValue(timeRemainsString);
            }
            if (!elapsedTextField.valueProperty.getValue()
                    .equals(timeElapsedString)) {
                elapsedTextField.valueProperty.setValue(timeElapsedString);
            }
//            if (!elapsedTextField.valueProperty.getValue()
//                    .equals(timeElapsed.remove(new TimeHM(0,1)).toString())) {
//                String s = timeElapsed.remove(new TimeHM(0,1)).toString();
//                elapsedTextField.valueProperty.setValue(s + ":" + (secondNow < 10 ? "0" : "") + secondNow + ":" + (millisecondNow < 10 ? "00" : (millisecondNow < 100 ? "0" : millisecondNow)) + millisecondNow);
//            }


            TTime overtime = overtimeTextField.asTTime();
            int hourDone = (int) (Constants.WORKING_HOURS_LENGTH + overtime.getHour()
                                              - timeRemains.getHour());
            int minutesDone
                    = (int) (Constants.WORKING_MINUTES_LENGTH + overtime.getMinute()
                                                 - timeRemains.getMinute());
            int secondsDone = secondNow;
            int millisecondsDone = millisecondNow;

            int totalMinutesDone = hourDone * 60 + minutesDone;
            int totalSecondsDone = totalMinutesDone * 60 + secondsDone;
            int totalMillisecondsDone
                    = totalSecondsDone * 1000 + millisecondsDone;


            int totalMinutes = TTime.countDiffInMinutes(arrivalTextField.asTTime(),
                    departureTextField.asTTime());

            int totalSeconds = totalMinutes * TTime.SECONDS_PER_MINUTE;
            int totalMilliseconds = totalSeconds * TTime.MILLISECONDS_PER_SECOND;

            double done = ((double) totalMillisecondsDone)
                          / ((double) totalMilliseconds);
            progressSquare.setDonePercent(done);
            progressCircle.setDonePercent(done);
            dayBattery.setDonePercent(done);

            WeekStatistics weekStatistics = new WeekStatistics(analogClock, time);
            final boolean nowIsWeekend = weekStatistics.isNowIsWeekend();
            final int workDaysDone = weekStatistics.getWorkDaysDone();
            final  int workDaysTotal = weekStatistics.getWorkDaysTotal();

            int weekDayWhenMondayIsOne = analogClock.dayOfWeekProperty.getValue();
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
            minuteBattery.setDonePercent(
                    MinuteBattery.getMinuteProgress(secondNow, millisecondNow));
            yearBattery
                    .setDonePercent(YearBattery.getYearProgress(analogClock));
            yearBattery.setLabel("");
            //yearBattery.setDonePercent(YearBattery.getYearProgress(2024,0,1,0,0,0,0));
            int totalSecondsRemains
                    = (int) (timeRemains.getHour() * 60 * 60
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
        if (workingDaysWindow != null) {
            workingDaysWindow.setVisible(false);
            workingDaysWindow.dispose();
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
        if (this.configWindow == null) {
            openConfigWindow();
            this.configWindow.setVisible(false);
        }

        this.configWindow.doEnableEverything();
    }

    public void doDisableAlmostEverything() {

        if (this.configWindow == null) {
            openConfigWindow();
            this.configWindow.setVisible(false);
        }

        this.configWindow.doDisableAlmostEverything();
    }
    public void increaseArrivalByOneMinute() {
        arrivalTextField.valueProperty.setValue(new TTime(this.arrivalTextField.valueProperty.getValue()).add(new TTime(0, 1)).toString().substring(0, 5));
    }
    public void decreaseArrivalByOneMinute() {
        arrivalTextField.valueProperty.setValue(new TTime(this.arrivalTextField.valueProperty.getValue()).remove(new TTime(0, 1)).toString().substring(0, 5));
    }
    public void increaseOvertimeByOneMinute() {
        TTime newOvertime = new TTime(this.overtimeTextField.valueProperty.getValue()).add(new TTime(0, 1));
        overtimeTextField.valueProperty.setValue(newOvertime.toString().substring(0, newOvertime.isNegative() ? 6 :5));
    }
    public void decreaseOvertimeByOneMinute() {
        TTime newOvertime = new TTime(this.overtimeTextField.valueProperty.getValue()).remove(new TTime(0, 1));
        overtimeTextField.valueProperty.setValue(newOvertime.toString().substring(0, newOvertime.isNegative() ? 6 :5));
    }
}
