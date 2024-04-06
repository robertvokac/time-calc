package org.nanoboot.utils.timecalc.swing.windows;

import org.nanoboot.utils.timecalc.app.CommandActionListener;
import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.app.TimeCalcApp;
import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcKeyAdapter;
import org.nanoboot.utils.timecalc.app.TimeCalcProperties;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Progress;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.entity.WorkingDay;
import org.nanoboot.utils.timecalc.persistence.api.ActivityRepositoryApi;
import org.nanoboot.utils.timecalc.persistence.impl.sqlite.ActivityRepositorySQLiteImpl;
import org.nanoboot.utils.timecalc.persistence.impl.sqlite.WorkingDayRepositorySQLiteImpl;
import org.nanoboot.utils.timecalc.swing.common.AboutButton;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.common.Toaster;
import org.nanoboot.utils.timecalc.swing.common.WeekStatistics;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.controls.ComponentRegistry;
import org.nanoboot.utils.timecalc.swing.controls.SmallTButton;
import org.nanoboot.utils.timecalc.swing.controls.TButton;
import org.nanoboot.utils.timecalc.swing.controls.TCheckBox;
import org.nanoboot.utils.timecalc.swing.controls.TLabel;
import org.nanoboot.utils.timecalc.swing.controls.TTextField;
import org.nanoboot.utils.timecalc.swing.controls.TWindow;
import org.nanoboot.utils.timecalc.swing.progress.AnalogClock;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.swing.progress.battery.DayBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.HourBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.MinuteBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.MonthBattery;
import org.nanoboot.utils.timecalc.swing.progress.ProgressCircle;
import org.nanoboot.utils.timecalc.swing.progress.ProgressLife;
import org.nanoboot.utils.timecalc.swing.progress.ProgressMoney;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSquare;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSwing;
import org.nanoboot.utils.timecalc.swing.progress.ProgressFuelGauge;
import org.nanoboot.utils.timecalc.swing.progress.weather.ProgressWeather;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.swing.progress.WalkingHumanProgress;
import org.nanoboot.utils.timecalc.swing.progress.battery.WeekBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.YearBattery;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.ChangeListener;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Timer;
import org.nanoboot.utils.timecalc.swing.progress.ProgressDot;
import org.nanoboot.utils.timecalc.utils.property.ReadOnlyProperty;

/**
 * @author Robert Vokac
 * @since 08.02.2024
 */
public class MainWindow extends TWindow {

    public static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    public static final Color FOREGROUND_COLOR = new Color(210, 210, 210);
    public static final JCheckBox hideShowFormsCheckBox = new JCheckBox();
    private static final int BATTERY_WIDTH = 140;
    private static final String BACKUP = "_backup";
    private static final String BASIC_FEATURE__ = "__only_basic_features__";
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
    private final TTextField workingTimeTextField;
    private final TTextField pauseTimeTextField;
    private final TTextField noteTextField;
    private final TCheckBox timeOffCheckBox = new TCheckBox("Time off");
    private final TTextField departureTextField;
    private final TTextField elapsedTextField;
    private final TTextField remainingTextField;
    private final TButton saveButton;
    private final ProgressLife progressLife;
    private final ProgressMoney progressMoney;
    private final ProgressWeather progressWeather;
    private final ProgressFuelGauge progressFuelGauge;
    private HelpWindow helpWindow = null;
    private ConfigWindow configWindow = null;
    private ActivitiesWindow activitiesWindow = null;
    private WorkingDaysWindow workingDaysWindow = null;
    private boolean stopBeforeEnd = false;
    private final WorkingDayRepositorySQLiteImpl workingDayRepository;
    private final ActivityRepositoryApi activityRepository;

    private final IntegerProperty forgetOvertimeProperty = new IntegerProperty("forgetOvertimeProperty", 0);
    private WeekStatistics weekStatistics = null;
    private final ProgressDot progressDot;
    
    private final TimeCalcKeyAdapter timeCalcKeyAdapter;
    private double msRemaining = 0d;
    private final Time timeAlwaysReal;

    public final ReadOnlyProperty<Boolean> allowOnlyBasicFeaturesProperty;

    {
        ChangeListener valueMustBeTime
                = (property, oldValue, newValue) -> new TTime((String) newValue);
        this.arrivalTextField = new TTextField(Constants.DEFAULT_ARRIVAL_TIME, 40, true, valueMustBeTime);
        this.overtimeTextField = new TTextField(Constants.DEFAULT_OVERTIME, 40, true, valueMustBeTime);
        this.workingTimeTextField = new TTextField("08:00", 40, true, valueMustBeTime);
        this.pauseTimeTextField = new TTextField("00:30", 40, true, valueMustBeTime);

        this.noteTextField = new TTextField("", 100);
        this.departureTextField = new TTextField();
        this.elapsedTextField = new TTextField("", 100);
        this.remainingTextField = new TTextField("", 100);
    }

    public MainWindow(TimeCalcApp timeCalcApp) {
//        ToolTipManager ttm = ToolTipManager.sharedInstance();
//        ttm.setInitialDelay(0);
//        ttm.setDismissDelay(10000);
        allowOnlyBasicFeaturesProperty = timeCalcApp.allowOnlyBasicFeaturesProperty;
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
        Time time = new Time();
        this.timeAlwaysReal = new Time();
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
        time.allowCustomValuesProperty.bindTo(timeCalcConfiguration.testEnabledProperty);

        arrivalTextField.addVetoableChangeListener(e -> {
            String newValue = (String) e.getNewValue();
            if (newValue.isEmpty()) {
                throw new PropertyVetoException("Arrival must not be empty.", new PropertyChangeEvent(e.getSource(), e.getPropertyName(), e.getOldValue(), e.getNewValue()));
            }
        });

        overtimeTextField.addVetoableChangeListener(e -> {
            String newValue = (String) e.getNewValue();
            if (newValue.isEmpty()) {
                throw new PropertyVetoException("Overtime must not be empty.", new PropertyChangeEvent(e.getSource(), e.getPropertyName(), e.getOldValue(), e.getNewValue()));
            }
        });
        this.configButton = new TButton("Config");
        configButton.addActionListener(e -> {
            if (configWindow == null) {
                this.configWindow = new ConfigWindow(timeCalcConfiguration);
            }
            configWindow.setVisible(true);
            configWindow.setLocationRelativeTo(this);
            configWindow.setLocation(100, 100);
        });
        if(allowOnlyBasicFeaturesProperty.getValue()) {
            if(!timeCalcConfiguration.profileNameProperty.getValue().equals(BASIC_FEATURE__)) {
                timeCalcConfiguration.profileNameProperty.setValue(
                        timeCalcConfiguration.profileNameProperty.getValue()
                        + BACKUP);
                timeCalcConfiguration.saveToTimeCalcProperties();

                timeCalcConfiguration.profileNameProperty.setValue(BASIC_FEATURE__);
                timeCalcConfiguration.saveToTimeCalcProperties();
            }
            timeCalcConfiguration.profileNameProperty.setValue(BASIC_FEATURE__);
            timeCalcConfiguration.mainWindowCustomTitleProperty.setValue("Report");
            this.doDisableAlmostEverything();
            //
            //timeCalcConfiguration.batteryNameVisibleProperty.enable();
            timeCalcConfiguration.batteryPercentProgressProperty.enable();
            //timeCalcConfiguration.batteryCircleProgressProperty.enable();
            //
//            timeCalcConfiguration.clockHandsHourVisibleProperty.setValue(true);
//            timeCalcConfiguration.clockHandsMinuteVisibleProperty.setValue(true);
            timeCalcConfiguration.clockVisibleProperty.setValue(true);
//            timeCalcConfiguration.clockHandsLongVisibleProperty.setValue(true);
            timeCalcConfiguration.notificationsVisibleProperty.setValue(true);
                    //
            timeCalcConfiguration.saveToTimeCalcProperties();
        }
        this.workDaysButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Work Days");
        this.activitiesButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Activities");
        this.restartButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Restart");
        this.exitButton = new TButton("Exit");
        this.focusButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Focus");
        this.helpButton = new TButton("Help");
        this.weatherButton = new TButton("Weather");
        this.commandButton = new TButton("Command");
        this.jokeButton = new TButton("Joke");
        this.aboutButton = new AboutButton();

        //window.add(weatherButton);
        addAll(workDaysButton, activitiesButton, restartButton,focusButton);
        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            addAll(configButton, commandButton,jokeButton,helpButton,
                    exitButton,
                    hideShowFormsCheckBox);
        }
        if(allowOnlyBasicFeaturesProperty.getValue()) {
            timeOffCheckBox.setText("");
        }

            timeCalcApp.visibilityProperty
                .bindTo(timeCalcConfiguration.visibilityDefaultProperty);
        if (!timeCalcConfiguration.visibilitySupportedColoredProperty
                .isEnabled()) {
            timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
        }
        if (Toaster.notificationsVisibleProperty.isBound()) {
            Toaster.notificationsVisibleProperty.unBound();
        }
        Toaster.notificationsVisibleProperty.bindTo(timeCalcConfiguration.notificationsVisibleProperty);

        this.timeCalcKeyAdapter
                = new TimeCalcKeyAdapter(timeCalcConfiguration, timeCalcApp,
                        this, time);
        addKeyListener(timeCalcKeyAdapter);

        AnalogClock clock = new AnalogClock();

        {
            arrivalTextField.valueProperty.addListener(e -> {
                if (!arrivalTextField.valueProperty.getValue().isEmpty()) {
                    TTime startTime_ = arrivalTextField.asTTime();
                    clock.startHourProperty
                            .setValue(startTime_.getHour());
                    clock.startMinuteProperty
                            .setValue(startTime_.getMinute());
                }

            });
            departureTextField.valueProperty.addListener(e -> {
                if (!departureTextField.valueProperty.getValue().isEmpty() && arrivalTextField.valueProperty.getValue().isEmpty()) {

                    try {
                        TTime endTime = arrivalTextField.asTTime();
                        clock.endHourProperty
                                .setValue(endTime.getHour());
                        clock.endMinuteProperty
                                .setValue(endTime.getMinute());
                    } catch (Exception e2) {

                    }

                }
            });
        }
        clock.setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 200);
        add(clock);

        MinuteBattery minuteBattery
                = new MinuteBattery(clock.getBounds().x + clock.getWidth() + SwingUtils.MARGIN,
                        clock.getY(), BATTERY_WIDTH);
        add(minuteBattery);
        Battery hourBattery = new HourBattery(
                minuteBattery.getBounds().x + minuteBattery.getWidth()
                + SwingUtils.MARGIN,
                minuteBattery.getY(),
                BATTERY_WIDTH);
        add(hourBattery);

        Battery dayBattery = new DayBattery(
                hourBattery.getBounds().x + hourBattery.getWidth()
                + SwingUtils.MARGIN,
                hourBattery.getY(),
                BATTERY_WIDTH);
        add(dayBattery);

        Battery weekBattery = new WeekBattery(
                dayBattery.getBounds().x + dayBattery.getWidth() + SwingUtils.MARGIN,
                dayBattery.getY(),
                BATTERY_WIDTH);
        add(weekBattery);

        Battery monthBattery = new MonthBattery(
                weekBattery.getBounds().x + weekBattery.getWidth()
                + SwingUtils.MARGIN,
                weekBattery.getY(), BATTERY_WIDTH);
        add(monthBattery);
        Battery yearBattery = new YearBattery(
                monthBattery.getBounds().x + monthBattery.getWidth()
                + SwingUtils.MARGIN,
                monthBattery.getY(), BATTERY_WIDTH);
        add(yearBattery);

        WalkingHumanProgress walkingHumanProgress
                = new WalkingHumanProgress(this);
        walkingHumanProgress.setBounds(minuteBattery.getX(),
                minuteBattery.getY() + minuteBattery.getHeight(), 400, 80);
        add(walkingHumanProgress);
        walkingHumanProgress.visibleProperty
                .bindTo(timeCalcConfiguration.walkingHumanVisibleProperty);
        weatherButton
                .setBounds(SwingUtils.MARGIN, walkingHumanProgress.getY()
                        + walkingHumanProgress.getHeight());

        //
        ProgressSquare progressSquare = new ProgressSquare();
        progressSquare
                .setBounds(yearBattery.getX() + yearBattery.getWidth() + 4 * SwingUtils.MARGIN, yearBattery.getY(),
                        100);
        add(progressSquare);
        progressSquare.visibleProperty
                .bindTo(timeCalcConfiguration.squareVisibleProperty);

        ProgressCircle progressCircle = new ProgressCircle();
        progressCircle
                .setBounds(
                        progressSquare.getX(), progressSquare.getY() + progressSquare.getHeight()
                        + SwingUtils.MARGIN, 100);
        add(progressCircle);
        progressCircle.visibleProperty
                .bindTo(timeCalcConfiguration.circleVisibleProperty);
        //
//        PlaceHolderWidget placeHolderWidget = new PlaceHolderWidget();
//        add(placeHolderWidget);
//        placeHolderWidget.setBounds(progressSquare.getX() + progressSquare.getWidth() + SwingUtils.MARGIN, SwingUtils.MARGIN, 50, 50);

        ProgressSwing progressSwing = new ProgressSwing();
        progressSwing.setBounds(clock.getX(), walkingHumanProgress.getY() + walkingHumanProgress.getHeight() + SwingUtils.MARGIN,
                200, 100);
        add(progressSwing);
        progressSwing.visibleProperty
                .bindTo(timeCalcConfiguration.swingVisibleProperty);
        progressSwing.quarterIconVisibleProperty.bindTo(timeCalcConfiguration.swingQuarterIconVisibleProperty);

        this.progressLife = new ProgressLife(time);
        progressLife.setBounds(progressSwing.getX() + progressSwing.getWidth() + SwingUtils.MARGIN, progressSwing.getY(),
                100, 50);

        add(progressLife);

        this.progressMoney
                = new ProgressMoney();
        progressMoney.setBounds(progressLife.getX(), progressSwing.getY() + progressLife.getHeight() + SwingUtils.MARGIN,
                100, 50);

        progressMoney.visibleProperty
                .bindTo(timeCalcConfiguration.moneyVisibleProperty);
        progressMoney.typeProperty
                .bindTo(timeCalcConfiguration.moneyTypeProperty);
        progressMoney.perMonthProperty
                .bindTo(timeCalcConfiguration.moneyPerMonthProperty);
        progressMoney.currencyProperty
                .bindTo(timeCalcConfiguration.moneyCurrencyProperty);
        add(progressMoney);

        this.progressWeather
                = new ProgressWeather(time);
        progressWeather.setBounds(progressLife.getX() + progressLife.getWidth() + SwingUtils.MARGIN, progressLife.getY(),
                100, 100);

        progressWeather.visibleProperty
                .bindTo(timeCalcConfiguration.weatherVisibleProperty);
        add(progressWeather);

        this.progressDot
                = new ProgressDot();
        progressDot.setBounds(progressWeather.getX() + progressWeather.getWidth() + SwingUtils.MARGIN, progressWeather.getY(),
                100, 100);

        add(progressDot);

        this.progressFuelGauge = new ProgressFuelGauge();
        progressFuelGauge.setBounds(progressDot.getX() + progressDot.getWidth() + SwingUtils.MARGIN, progressDot.getY(),
                100, 100);

        progressFuelGauge.visibleProperty
                .bindTo(timeCalcConfiguration.fuelVisibleProperty);
        progressFuelGauge.typeProperty
                .bindTo(timeCalcConfiguration.fuelTypeProperty);
        progressFuelGauge.hiddenProperty
                .bindTo(timeCalcConfiguration.fuelHiddenProperty);
        progressFuelGauge.fuelIconVisibleProperty
                .bindTo(timeCalcConfiguration.fuelIconVisibleProperty);

        add(progressFuelGauge);

        {
            progressSquare.typeProperty
                    .bindTo(timeCalcConfiguration.squareTypeProperty);
            progressDot.typeProperty
                    .bindTo(timeCalcConfiguration.dotTypeProperty);
            progressDot.visibleProperty
                    .bindTo(timeCalcConfiguration.dotVisibleProperty);
            progressCircle.typeProperty
                    .bindTo(timeCalcConfiguration.circleTypeProperty);
            walkingHumanProgress.typeProperty
                    .bindTo(timeCalcConfiguration.walkingHumanTypeProperty);
            progressSwing.typeProperty
                    .bindTo(timeCalcConfiguration.swingTypeProperty);
            progressLife.typeProperty
                    .bindTo(timeCalcConfiguration.lifeTypeProperty);
            progressLife.birthDateProperty
                    .bindTo(timeCalcConfiguration.lifeBirthDateProperty);
            progressLife.visibleProperty
                    .bindTo(timeCalcConfiguration.lifeVisibleProperty);
        }

        {
            clock.hiddenProperty.bindTo(timeCalcConfiguration.clockHiddenProperty);
            minuteBattery.hiddenProperty.bindTo(timeCalcConfiguration.batteryMinuteHiddenProperty);
            hourBattery.hiddenProperty.bindTo(timeCalcConfiguration.batteryHourHiddenProperty);
            dayBattery.hiddenProperty.bindTo(timeCalcConfiguration.batteryDayHiddenProperty);
            weekBattery.hiddenProperty.bindTo(timeCalcConfiguration.batteryWeekHiddenProperty);
            monthBattery.hiddenProperty.bindTo(timeCalcConfiguration.batteryMonthHiddenProperty);
            yearBattery.hiddenProperty.bindTo(timeCalcConfiguration.batteryYearHiddenProperty);
            progressSquare.hiddenProperty
                    .bindTo(timeCalcConfiguration.squareHiddenProperty);
            progressDot.hiddenProperty
                    .bindTo(timeCalcConfiguration.dotHiddenProperty);
            progressCircle.hiddenProperty
                    .bindTo(timeCalcConfiguration.circleHiddenProperty);
            walkingHumanProgress.hiddenProperty
                    .bindTo(timeCalcConfiguration.walkingHumanHiddenProperty);
            progressSwing.hiddenProperty
                    .bindTo(timeCalcConfiguration.swingHiddenProperty);
            progressLife.hiddenProperty
                    .bindTo(timeCalcConfiguration.lifeHiddenProperty);
            progressMoney.hiddenProperty.bindTo(timeCalcConfiguration.moneyHiddenProperty);
            progressWeather.hiddenProperty.bindTo(timeCalcConfiguration.weatherHiddenProperty);
        }
        TLabel arrivalTextFieldLabel = new TLabel("Arrival:", 70);
        arrivalTextFieldLabel.setBoundsFromTop(progressSwing, 3);

        arrivalTextField.setBoundsFromLeft(arrivalTextFieldLabel);
        TButton arrivalIncreaseButton = new SmallTButton('+');
        TButton arrivalDecreaseButton = new SmallTButton('-');
        arrivalIncreaseButton.setBounds(arrivalTextField.getX() + arrivalTextField.getWidth(), arrivalTextField.getY(), 15, 15);
        arrivalDecreaseButton.setBounds(arrivalTextField.getX() + arrivalTextField.getWidth(), arrivalTextField.getY() + 15, 15, 15);

        //
        TLabel overtimeTextFieldLabel = new TLabel("Overtime:");
        overtimeTextFieldLabel.setBoundsFromLeft(arrivalTextField, 15);

        overtimeTextField.setBoundsFromLeft(overtimeTextFieldLabel);
        TButton overtimeIncreaseButton = new SmallTButton('+');
        TButton overtimeDecreaseButton = new SmallTButton('-');
        overtimeIncreaseButton.setBounds(overtimeTextField.getX() + overtimeTextField.getWidth(), overtimeTextField.getY(), 15, 15);
        overtimeDecreaseButton.setBounds(overtimeTextField.getX() + overtimeTextField.getWidth(), overtimeTextField.getY() + 15, 15, 15);

        //
        TLabel workingTimeInMinutesTextFieldLabel = new TLabel("Work:", 40);
        workingTimeInMinutesTextFieldLabel.setBoundsFromLeft(overtimeTextField, 15);

        workingTimeTextField.setBoundsFromLeft(workingTimeInMinutesTextFieldLabel);
        TButton workingIncreaseButton = new SmallTButton('+');
        TButton workingDecreaseButton = new SmallTButton('-');
        workingIncreaseButton.setBounds(workingTimeTextField.getX() + workingTimeTextField.getWidth(), workingTimeTextField.getY(), 15, 15);
        workingDecreaseButton.setBounds(workingTimeTextField.getX() + workingTimeTextField.getWidth(), workingTimeTextField.getY() + 15, 15, 15);

        //
        TLabel pauseTimeInMinutesFieldLabel = new TLabel("Pause:", 40);
        pauseTimeInMinutesFieldLabel.setBoundsFromLeft(workingTimeTextField, 15);

        pauseTimeTextField.setBoundsFromLeft(pauseTimeInMinutesFieldLabel);
        TButton pauseIncreaseButton = new SmallTButton('+');
        TButton pauseDecreaseButton = new SmallTButton('-');
        pauseIncreaseButton.setBounds(pauseTimeTextField.getX() + pauseTimeTextField.getWidth(), pauseTimeTextField.getY(), 15, 15);
        pauseDecreaseButton.setBounds(pauseTimeTextField.getX() + pauseTimeTextField.getWidth(), pauseTimeTextField.getY() + 15, 15, 15);

        //
        TLabel noteTextFieldLabel = new TLabel("Note:", 40);
        noteTextFieldLabel.setBoundsFromLeft(pauseTimeTextField, 10);

        noteTextField.setBoundsFromLeft(noteTextFieldLabel);
        timeOffCheckBox.setBoundsFromLeft(noteTextField);
        //

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(arrivalTextFieldLabel);
        add(arrivalTextField);
        add(arrivalIncreaseButton);
        add(arrivalDecreaseButton);

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(overtimeTextFieldLabel);
        add(overtimeTextField);
        add(overtimeIncreaseButton);
        add(overtimeDecreaseButton);

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(workingTimeInMinutesTextFieldLabel);
        add(workingTimeTextField);
        add(workingIncreaseButton);
        add(workingDecreaseButton);

        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(pauseTimeInMinutesFieldLabel);
            add(pauseTimeTextField);
            add(pauseIncreaseButton);
            add(pauseDecreaseButton);
        }

        arrivalIncreaseButton.addActionListener(e -> increaseArrival(TTime.T_TIME_ONE_MINUTE));
        arrivalDecreaseButton.addActionListener(e -> decreaseArrival(TTime.T_TIME_ONE_MINUTE));
        overtimeIncreaseButton.addActionListener(e -> increaseOvertime(TTime.T_TIME_ONE_MINUTE));
        overtimeDecreaseButton.addActionListener(e -> decreaseOvertime(TTime.T_TIME_ONE_MINUTE));
        workingIncreaseButton.addActionListener(e -> increaseWork(TTime.T_TIME_ONE_MINUTE));
        workingDecreaseButton.addActionListener(e -> decreaseWork(TTime.T_TIME_ONE_MINUTE));
        pauseIncreaseButton.addActionListener(e -> increasePause(TTime.T_TIME_ONE_MINUTE));
        pauseDecreaseButton.addActionListener(e -> decreasePause(TTime.T_TIME_ONE_MINUTE));

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(noteTextFieldLabel);
        add(noteTextField);
        add(timeOffCheckBox);
        //
        TLabel departureTextFieldLabel = new TLabel("Departure:", 70);
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
        this.saveButton = new TButton("Save", 180);
        saveButton.setBoundsFromLeft(remainingTextField);
        //

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(departureTextFieldLabel);
        add(departureTextField);
        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(elapsedTextFieldLabel);
            add(elapsedTextField);
            add(remainingTextFieldLabel);
            add(remainingTextField);
        }
        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(saveButton);
        }
        this.workingDayRepository = new WorkingDayRepositorySQLiteImpl(timeCalcApp.getSqliteConnectionFactory());
        this.activityRepository = new ActivityRepositorySQLiteImpl(timeCalcApp.getSqliteConnectionFactory());

        //
        configButton.setBoundsFromTop(departureTextFieldLabel);
        workDaysButton.setBoundsFromLeft(configButton);
        activitiesButton.setBoundsFromLeft(workDaysButton);

        exitButton.setBounds(saveButton.getX() + saveButton.getWidth() - activitiesButton.getWidth(), workDaysButton.getY(), activitiesButton.getWidth(), activitiesButton.getHeight());
        restartButton.setBounds(exitButton.getX() - SwingUtils.MARGIN - activitiesButton.getWidth(), activitiesButton.getY(), activitiesButton.getWidth(), activitiesButton.getHeight());

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
            saveButton.doClick();
            timeCalcConfiguration.saveToTimeCalcProperties();
            System.exit(0);
        });
        focusButton.addActionListener(e -> requestFocus(true));
        restartButton.addActionListener(e -> {
            setVisible(false);
            saveButton.doClick();
            timeCalcConfiguration.saveToTimeCalcProperties();
            stopBeforeEnd = true;
        });
        workDaysButton.addActionListener(e -> {
            if (workingDaysWindow == null) {
                this.workingDaysWindow = new WorkingDaysWindow(workingDayRepository, time, 7d, allowOnlyBasicFeaturesProperty.getValue());
            }
            workingDaysWindow.setVisible(true);
        });
        activitiesButton.addActionListener(e -> {
            if (activitiesWindow == null) {
                this.activitiesWindow = new ActivitiesWindow(this.activityRepository, time,
                        timeCalcConfiguration);
            }
            activitiesWindow.setVisible(true);
        });

        helpButton.addActionListener(e -> {
            if (helpWindow == null) {
                this.helpWindow = new HelpWindow();
            }
            helpWindow.setVisible(true);
        });

        clock.dayProperty.bindTo(time.dayProperty);
        clock.monthProperty.bindTo(time.monthProperty);
        clock.yearProperty.bindTo(time.yearProperty);
        clock.hourProperty.bindTo(time.hourProperty);
        clock.minuteProperty.bindTo(time.minuteProperty);
        clock.secondProperty.bindTo(time.secondProperty);
        clock.millisecondProperty.bindTo(time.millisecondProperty);

        clock.dayOfWeekProperty.bindTo(time.dayOfWeekProperty);

        clock.millisecondEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsMillisecondVisibleProperty);
        clock.secondEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsSecondVisibleProperty);
        clock.minuteEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsMinuteVisibleProperty);
        clock.hourEnabledProperty
                .bindTo(timeCalcConfiguration.clockHandsHourVisibleProperty);
        clock.handsLongProperty
                .bindTo(timeCalcConfiguration.clockHandsLongVisibleProperty);
        clock.borderVisibleProperty
                .bindTo(timeCalcConfiguration.clockBorderVisibleProperty);
        clock.borderOnlyHoursProperty
                .bindTo(timeCalcConfiguration.clockBorderOnlyHoursProperty);
        clock.numbersVisibleProperty
                .bindTo(timeCalcConfiguration.clockNumbersVisibleProperty);
        clock.circleVisibleProperty
                .bindTo(timeCalcConfiguration.clockCircleVisibleProperty);
        clock.circleStrongBorderProperty
                .bindTo(timeCalcConfiguration.clockCircleStrongBorderProperty);
        clock.centreCircleVisibleProperty
                .bindTo(timeCalcConfiguration.clockCentreCircleVisibleProperty);
        clock.centreCircleBorderColorProperty
                .bindTo(timeCalcConfiguration.clockCircleBorderColorProperty);
        clock.handsColoredProperty
                .bindTo(timeCalcConfiguration.clockHandsColoredProperty);
        clock.centreCircleColoredProperty
                .bindTo(timeCalcConfiguration.clockCentreCircleColoredProperty);
        clock.progressVisibleOnlyIfMouseMovingOverProperty
                .bindTo(timeCalcConfiguration.clockProgressVisibleOnlyIfMouseMovingOverProperty);
        clock.dateVisibleOnlyIfMouseMovingOverProperty
                .bindTo(timeCalcConfiguration.clockDateVisibleOnlyIfMouseMovingOverProperty);
        clock.visibleProperty
                .bindTo(timeCalcConfiguration.clockVisibleProperty);
        clock.percentProgressVisibleProperty.bindTo(timeCalcConfiguration.clockPercentProgressVisibleProperty);
        clock.smileyVisibleProperty.bindTo(timeCalcConfiguration.clockSmileyVisibleProperty);
        clock.circleProgressVisibleProperty.bindTo(timeCalcConfiguration.clockCircleProgressVisibleProperty);

        ComponentRegistry<Component> componentRegistry
                = new ComponentRegistry();
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
                            .bindTo(timeCalcConfiguration.batteryQuarterIconVisibleProperty);
                    battery.quarterIconVisibleProperty.bindTo(
                            timeCalcConfiguration.batteryQuarterIconVisibleProperty);
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
                    widget.typeVisibleProperty.bindTo(timeCalcConfiguration.typeVisibleProperty);
                }
                );
        setSize(progressSquare.getX() + progressSquare.getWidth()
                + 3 * SwingUtils.MARGIN,
                focusButton.getY() + focusButton.getHeight() + SwingUtils.MARGIN
                + focusButton.getHeight() + 2 * SwingUtils.MARGIN);

        saveButton.addActionListener(e -> {

            TTime arrival_ = new TTime(arrivalTextField.getText());
            TTime overtime_ = new TTime(overtimeTextField.getText());
            TTime work_ = new TTime(workingTimeTextField.getText());
            TTime pause_ = new TTime(pauseTimeTextField.getText());

            Calendar cal = time.asCalendar();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            timeCalcConfiguration.saveToTimeCalcProperties();
            WorkingDay workingDay = workingDayRepository.read(time.asCalendar());
            if (workingDay == null) {
                workingDay = new WorkingDay();
                workingDay.setId(WorkingDay.createId(year, month, day));
                workingDay.setYear(year);
                workingDay.setMonth(month);
                workingDay.setDay(day);
            }
            workingDay.setArrivalHour(arrival_.getHour());
            workingDay.setArrivalMinute(arrival_.getMinute());
            workingDay.setOvertimeHour(overtime_.getHour() * (overtime_.isNegative() ? (-1) : 1));
            workingDay.setOvertimeMinute(overtime_.getMinute() * (overtime_.isNegative() ? (-1) : 1));
            workingDay.setWorkingTimeInMinutes(work_.toTotalMilliseconds() / 1000 / 60);
            workingDay.setPauseTimeInMinutes(pause_.toTotalMilliseconds() / 1000 / 60);
            workingDay.setNote(noteTextField.getText());
            workingDay.setTimeOff(timeOffCheckBox.isSelected());
            workingDay.setForgetOvertime(forgetOvertimeProperty.getValue());
            workingDayRepository.update(workingDay);

            if (workingDaysWindow != null) {
                workingDaysWindow.doReloadButtonClick();
            }
        });

        WorkingDay wd = workingDayRepository.read(time.asCalendar());

        if (wd != null) {
            arrivalTextField.valueProperty.setValue(new TTime(wd.getArrivalHour(), wd.getArrivalMinute()).toString().substring(0, 5));
            TTime overtime = new TTime(wd.getOvertimeHour(), wd.getOvertimeMinute());
            overtimeTextField.valueProperty.setValue(overtime.toString().substring(0, overtime.isNegative() ? 6 : 5));
            //
            workingTimeTextField.valueProperty.setValue(TTime.ofMilliseconds(wd.getWorkingTimeInMinutes() * 60 * 1000).toString().substring(0, 5));
            pauseTimeTextField.valueProperty.setValue(TTime.ofMilliseconds(wd.getPauseTimeInMinutes() * 60 * 1000).toString().substring(0, 5));
            noteTextField.valueProperty.setValue(wd.getNote());
            timeOffCheckBox.setSelected(wd.isTimeOff());
            forgetOvertimeProperty.setValue(wd.getForgetOvertime());
        } else {
            Calendar cal = time.asCalendar();
            wd = new WorkingDay();
            {
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(cal.getTime());
                List<WorkingDay> arrivals = new ArrayList<>();
                for (int i = 1; i <= 90; i++) {
                    cal2.add(Calendar.DAY_OF_MONTH, -1);
                    WorkingDay wd_ = workingDayRepository.read(cal2);
                    if (wd_ == null || wd_.isThisDayTimeOff()) {
                        continue;
                    }
                    arrivals.add(wd_);
                    if (arrivals.size() == 20) {
                        break;
                    }
                }

                if (!arrivals.isEmpty()) {
//                    double averageArrival = arrivals.size() == 1 ? arrivals.get(0) : arrivals.stream().mapToDouble(Double::doubleValue).sorted().average().getAsDouble();
                    double medianArrival = arrivals.stream().map(a -> a.getArrivalAsDouble())
                            .sorted()
                            .collect(Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    a -> (a.size() % 2 == 0) ? ((a.get(a.size() / 2 - 1) + a.get(a.size() / 2)) / 2) : (a.get(a.size() / 2))));

                    TTime arrivalTTime = TTime.ofMilliseconds((int) (medianArrival * 60 * 60 * 1000));
                    while (arrivalTTime.getMinute() % 5 != 0) {
                        arrivalTTime = arrivalTTime.add(new TTime(0, 1));
                    }
                    arrivalTextField.valueProperty.setValue(arrivalTTime.toString().substring(0, 5));
                    wd.setArrivalHour(arrivalTTime.getHour());
                    wd.setArrivalMinute(arrivalTTime.getMinute());

                } else {
                    wd.setArrivalHour(7);
                    wd.setArrivalMinute(0);
                }

            }
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            wd.setId(WorkingDay.createId(year, month, day));
            wd.setYear(year);
            wd.setMonth(month);
            wd.setDay(day);
            wd.setOvertimeHour(0);
            wd.setOvertimeMinute(0);
            wd.setWorkingTimeInMinutes(480);
            wd.setPauseTimeInMinutes(30);
            wd.setNote("");
            wd.setTimeOff(false);
        }

        workingDayRepository.update(wd);

        new Timer(100, e -> {
            IntegerProperty speed = timeCalcConfiguration.speedProperty;

            if (speed.getValue() == Integer.MIN_VALUE) {
                timeCalcConfiguration.speedProperty.setValue(Integer.MAX_VALUE);
            }
            if (speed.getValue() == Integer.MAX_VALUE) {
                //timeCalcConfiguration.testEnabledProperty.setValue(false);
                if (timeCalcConfiguration.speedFloatingProperty.isEnabled()) {
                    speed.setValue(0);
                } else {
                    return;
                }

            }
            double r = Math.pow(2, speed.getValue() + 6);
            if (speed.getValue() < -6 && Math.random() > r) {
//                System.out.println(NumberFormats.FORMATTER_EIGHT_DECIMAL_PLACES.format(r));
                return;

            }
            if (timeCalcConfiguration.testEnabledProperty.isDisabled()) {
                timeCalcConfiguration.testEnabledProperty.enable();
            }

            if (time.yearCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.yearCustomProperty.setValue(time.yearProperty.getValue());
            }
            if (time.monthCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.monthCustomProperty.setValue(time.monthProperty.getValue());
            }
            if (time.dayCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.dayCustomProperty.setValue(time.dayProperty.getValue());
            }
            if (time.hourCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.hourCustomProperty.setValue(time.hourProperty.getValue());
            }
            if (time.minuteCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.minuteCustomProperty.setValue(time.minuteProperty.getValue());
            }
            if (time.secondCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.secondCustomProperty.setValue(time.secondProperty.getValue());
            }
            if (time.millisecondCustomProperty.getValue() == Integer.MAX_VALUE) {
                time.millisecondCustomProperty.setValue(time.millisecondProperty.getValue());
            }

            if (timeCalcConfiguration.speedFloatingProperty.isEnabled()) {
                long nowTime = timeAlwaysReal.asCalendar().getTime().getTime();
                long fakeTime = time.asCalendar().getTime().getTime();
                long diff = fakeTime - nowTime;
                boolean forewardMs = diff > 0;
                boolean backwardMs = !forewardMs;
                if (forewardMs && diff > 60000 && Math.random() > 0.95) {
                    speed.setValue(-2);
                }
                if (forewardMs && diff <= 60000 && Math.random() > 0.95) {
                    speed.setValue(1);
                }
                if (backwardMs && diff < -60000 && Math.random() > 0.95) {
                    speed.setValue(2);
                }
                if (backwardMs && diff >= -60000 && Math.random() > 0.95) {
                    speed.setValue(-1);
                }
                {
                    if (forewardMs && diff > 600000 && Math.random() > 0.95) {
                        speed.setValue(-4);
                    }
                    if (backwardMs && diff < -600000 && Math.random() > 0.95) {
                        speed.setValue(4);
                    }
                }
                if (Math.random() > 0.95) {
                    speed.setValue((int) (-8d + Math.random() * 8d));
                }
                if(timeCalcConfiguration.speedNegativeProperty.isDisabled()){
                if(Math.random() > 0.95 && Math.abs(diff) > 360000d * 6d) {
                    speed.setValue((int) (backwardMs ? (5d + Math.random() * 5d) : (-5d - Math.random() * 3d)));
                }
                }
                if(Math.random() > 0.999 ) {
                    speed.setValue(10);
                }
                if(Math.random() < 0.001 ) {
                    speed.setValue(-6);
                }
                if(diff >  360000 * 3) {
                    timeCalcConfiguration.speedNegativeProperty.setValue(true);
                } else {
                    timeCalcConfiguration.speedNegativeProperty.setValue(false);
                }

//                if(Math.random() > 0.98) {
//                    boolean add = Math.random() > 0.5;
//                    this.timeCalcKeyAdapter.setMsToAdd((int) ((add ? (1) : (-1)) * Math.random() * 360000d));
//                    this.timeCalcKeyAdapter.processShifCtrlAltModeKeyCodes(KeyEvent.VK_U, add, !add, false);
//                    
//                }

            }

            double msShouldBeAdded = speed.getValue() < -6 ? 1 : (Math.pow(2, speed.getValue()) * 100d) + this.msRemaining;
            int msShouldBeAddedInt = (int) Math.floor(msShouldBeAdded);
            this.msRemaining = msShouldBeAdded - msShouldBeAddedInt;
            this.timeCalcKeyAdapter.setMsToAdd((timeCalcConfiguration.speedNegativeProperty.isEnabled() ? (-1) : 1) * msShouldBeAddedInt);
            this.timeCalcKeyAdapter.processShifCtrlAltModeKeyCodes(KeyEvent.VK_U, true, false, false);

        }).start();

        if(allowOnlyBasicFeaturesProperty.getValue() && !FileConstants.BASIC_TXT.exists()) {
            String msg =
                    "You deleted the file " + FileConstants.BASIC_TXT
                    + ", extended features are disabled until the next stop and start of this application. Please, stop and start this application to enable extended features.";
            Utils.showNotification(msg, 30000);
        }
        if(allowOnlyBasicFeaturesProperty.getValue()) {
            String msg = "Warning: " + "You disabled the extended features of this application. Only the basic features are enabled. To enable the extended features, please: 1. stop this application 2. delete manually this file: " + FileConstants.BASIC_TXT.getAbsolutePath() + " 3. start this application again.";
            Utils.showNotification(msg, 30000, 200);
        }

        while (true) {

            if(allowOnlyBasicFeaturesProperty.getValue()) {
                if(timeCalcConfiguration.batteryDayVisibleProperty.isDisabled()) {
                    timeCalcConfiguration.batteryDayVisibleProperty.enable();
                }

                if(timeCalcConfiguration.batteryWeekVisibleProperty.isDisabled()) {
                    timeCalcConfiguration.batteryDayVisibleProperty.enable();
                }
            }
            if (Math.random() > 0.99) {
                File dbFileBackup = new File(
                        FileConstants.DB_FILE.getAbsolutePath() + ".backup."
                        + DateFormats.DATE_TIME_FORMATTER_SHORT
                                .format(new Date()).substring(0, 10)
                        + ".sqlite3");
                if (FileConstants.DB_FILE.exists() && !dbFileBackup.exists()) {
                    try {
                        Files.copy(FileConstants.DB_FILE.toPath(),
                                dbFileBackup.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (Math.random() > 0.9999) {
                for (File file : FileConstants.TC_DIRECTORY.listFiles()) {
                    if (file.getName().startsWith(FileConstants.DB_FILE.getName() + ".backup")) {
                        try {
                            long now = System.currentTimeMillis();
                            long diff = now - Files.getLastModifiedTime(file.toPath()).toMillis();
                            int fileAgeInDays = (int) (diff / 1000 / 60 / 60 / 24);
                            System.out.println("Found backup file " + file.getName() + " with age: " + fileAgeInDays);
                            if (fileAgeInDays > 14) {
                                file.delete();
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            System.err.println("Deleting old backups failed: " + ex.getMessage());
                            break;
                        }

                    }
                }
            }

            if (updateWindow(timeCalcApp, time, clock, minuteBattery, hourBattery,
                    dayBattery,
                    weekBattery, monthBattery, yearBattery,
                    walkingHumanProgress,
                    progressSquare, progressCircle, progressSwing, componentRegistry, arrivalTextFieldLabel)) {
                break;
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

        //timeCalcConfiguration.saveToTimeCalcProperties();
        setVisible(false);
        dispose();
    }

    private boolean updateWindow(TimeCalcApp timeCalcApp, Time time,
            AnalogClock clock, MinuteBattery minuteBattery, Battery hourBattery,
            Battery dayBattery, Battery weekBattery, Battery monthBattery,
            Battery yearBattery, WalkingHumanProgress walkingHumanProgress,
            ProgressSquare progressSquare, ProgressCircle progressCircle,
            ProgressSwing progressSwing,
            ComponentRegistry<Component> componentRegistry,
            TLabel arrivalTextFieldLabel) {
        //System.out.println("timeCalcConfiguration.handsLongProperty=" + timeCalcConfiguration.clockHandLongProperty.isEnabled());

        if (!departureTextField.valueProperty.getValue().isEmpty() && !arrivalTextField.valueProperty.getValue().isEmpty()) {
            TTime startTime = arrivalTextField.asTTime();
            TTime endTime = departureTextField.asTTime();
            clock.startHourProperty.setValue(startTime.getHour());
            clock.startMinuteProperty.setValue(startTime.getMinute());
            clock.endHourProperty.setValue(endTime.getHour());
            clock.endMinuteProperty.setValue(endTime.getMinute());
        }
        {

            TTime startTime = null;
            TTime overtime = null;
            TTime workDuration = new TTime(workingTimeTextField.valueProperty.getValue());
            TTime pauseDuration = new TTime(pauseTimeTextField.valueProperty.getValue());
            try {
                startTime = arrivalTextField.asTTime();
                overtime = overtimeTextField.asTTime();
            } catch (Exception e) {

            }
            if (startTime == null || overtime == null || workDuration == null || pauseDuration == null) {
                return false;
            }

            TTime newDeparture = startTime.add(workDuration).add(pauseDuration);
            if (overtime.isNegative()) {
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

            //timeCalcConfiguration.saveToTimeCalcProperties();
            setVisible(false);
            dispose();

            return true;
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

        int secondNow = clock.secondProperty.getValue();
        int millisecondNow = clock.millisecondProperty.getValue();

        if (arrivalTextField.getText().isEmpty() || departureTextField.getText().isEmpty()) {
            return false;
        }
        TTime startTime = arrivalTextField.asTTime();
        TTime endTime = departureTextField.asTTime();
        TTime nowTime = TTime.of(time.asCalendar());
        TTime timeElapsed = TTime
                .computeTimeDiff(startTime, nowTime);

        TTime timeRemains = TTime.computeTimeDiff(nowTime, endTime);
        TTime timeTotal = TTime.computeTimeDiff(startTime, endTime);
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

        int totalMillisecondsDone
                = timeElapsed.toTotalMilliseconds();
        int totalHoursDone = totalMillisecondsDone / 1000 / 60 / 60;

        int totalMilliseconds = timeTotal.toTotalMilliseconds();
        int totalMinutes = totalMilliseconds / 1000 / 60;

        double done = ((double) totalMillisecondsDone)
                / ((double) totalMilliseconds);
        if (done < 0) {
            done = 0;
        }
        if (done > 1) {
            done = 1;
        }
        Progress progress = new Progress();
        progress.set(WidgetType.DAY, done);

        try {
            WeekStatistics weekStatisticsTmp = new WeekStatistics(clock, time);
            weekStatistics = weekStatisticsTmp;
        } catch (DateTimeException e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            //            return false;
        }
        final boolean nowIsWeekend = weekStatistics.isNowIsWeekend();
        final int workDaysDone = weekStatistics.getWorkDaysDone();
        final int workDaysTotal = weekStatistics.getWorkDaysTotal();

        progress.setWorkDaysInMonth(workDaysTotal);
        progress.setWeekend(nowIsWeekend);
        int weekDayWhenMondayIsOne = clock.dayOfWeekProperty.getValue();
        double weekProgress = Progress.getWeekProgress(weekDayWhenMondayIsOne, done);
        weekBattery.setProgress(progress);
        weekBattery.setLabel(
                nowIsWeekend ? "5/5" : (weekDayWhenMondayIsOne + "/5"));

        double monthProgress = Progress
                .getMonthProgress(weekDayWhenMondayIsOne, workDaysDone,
                        workDaysTotal, done);
        progress.set(WidgetType.MONTH, monthProgress);
        double hourProgress
                = Progress.getHourProgress(timeRemains, secondsRemains,
                        millisecondsRemains);
        double minuteProgress
                = Progress.getMinuteProgress(secondNow, millisecondNow);
        double yearProgress = Progress.getYearProgress(clock, monthProgress);
        progress.set(WidgetType.HOUR, hourProgress);
        progress.set(WidgetType.WEEK, weekProgress);
        progress.set(WidgetType.MINUTE, minuteProgress);
        progress.set(WidgetType.YEAR, yearProgress);
        progressSquare.setProgress(progress);
        progressCircle.setProgress(progress);
        progressSwing.setProgress(progress);
        progressLife.setProgress(progress);
        progressMoney.setProgress(progress);
        progressDot.setProgress(progress);
        progressFuelGauge.setProgress(progress);
        dayBattery.setProgress(progress);

        monthBattery.setProgress(progress);
        monthBattery.setLabel(
                (nowIsWeekend ? workDaysDone : workDaysDone + 1) + "/"
                + (workDaysTotal));

        hourBattery.setProgress(progress);

        if (!nowIsWeekend) {
            hourBattery.setLabel(
                    totalHoursDone + "/" + (totalMinutes / 60));
        }

        minuteBattery.setProgress(progress);

        yearBattery.setProgress(progress);
        yearBattery.setLabel("");

        if (timeRemains.getHour() <= 0 && timeRemains.getMinute() <= 0 && timeRemains.getSecond() <= 0 && timeRemains.getMillisecond() <= 0) {
            Toaster toasterManager = new Toaster();
            toasterManager.setDisplayTime(30000);
            toasterManager.showToaster(
                    "Congratulation :-) It is the time to go home.");
            walkingHumanProgress
                    .setProgress(progress);
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
            walkingHumanProgress.setProgress(progress);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        return false;
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

    public void increaseArrival(TTime tTime) {
        TTime oldTime
                = new TTime(this.arrivalTextField.valueProperty.getValue());
        TTime newTime = oldTime.add(tTime);
//        System.out.println("oldTime=" + oldTime);
//        System.out.println("newTime=" + newTime);
//        System.out.println("tTime=" + tTime);
        arrivalTextField.valueProperty.setValue(
                newTime.toString().substring(0, 5));
    }

    public void decreaseArrival(TTime tTime) {
        arrivalTextField.valueProperty.setValue(new TTime(this.arrivalTextField.valueProperty.getValue()).remove(tTime).toString().substring(0, 5));
    }

    public void increaseOvertime(TTime tTime) {
        TTime newOvertime = new TTime(this.overtimeTextField.valueProperty.getValue()).add(tTime);
        overtimeTextField.valueProperty.setValue(newOvertime.toString().substring(0, newOvertime.isNegative() ? 6 : 5));
    }

    public void decreaseOvertime(TTime tTime) {
        TTime newOvertime = new TTime(this.overtimeTextField.valueProperty.getValue()).remove(tTime);
        overtimeTextField.valueProperty.setValue(newOvertime.toString().substring(0, newOvertime.isNegative() ? 6 : 5));
    }

    public void increaseWork(TTime tTime) {
        workingTimeTextField.valueProperty.setValue(new TTime(this.workingTimeTextField.valueProperty.getValue()).add(tTime).toString().substring(0, 5));
    }

    public void decreaseWork(TTime tTime) {
        workingTimeTextField.valueProperty.setValue(new TTime(this.workingTimeTextField.valueProperty.getValue()).remove(tTime).toString().substring(0, 5));
    }

    public void increasePause(TTime tTime) {
        pauseTimeTextField.valueProperty.setValue(new TTime(this.pauseTimeTextField.valueProperty.getValue()).add(tTime).toString().substring(0, 5));
    }

    public void decreasePause(TTime tTime) {
        pauseTimeTextField.valueProperty.setValue(
                new TTime(this.pauseTimeTextField.valueProperty.getValue())
                        .remove(tTime).toString().substring(0, 5));
    }

    public void doSaveButtonClick() {
        this.saveButton.doClick();
    }

    public int getForgetOvertime() {
        return this.forgetOvertimeProperty.getValue();
    }

    public void setForgetOvertime(int minutes) {
        this.forgetOvertimeProperty.setValue(minutes);
    }

    public void increaseSpeed() {
        IntegerProperty speed = timeCalcConfiguration.speedProperty;
        if (speed.getValue() == Integer.MIN_VALUE || speed.getValue() == Integer.MAX_VALUE) {
            speed.setZero();
        }
        if (speed.getValue() == MAX_SPEED) {
            //nothing to do
            return;
        }
        speed.increment();
    }

    public void decreaseSpeed() {
        IntegerProperty speed = timeCalcConfiguration.speedProperty;
        if (speed.getValue() == Integer.MIN_VALUE || speed.getValue() == Integer.MAX_VALUE) {
            speed.setZero();
        }
        if (speed.getValue() == MIN_SPEED) {
            //nothing to do
            return;
        }
        speed.decrement();
    }
    public static final int MIN_SPEED = -10;
    public static final int MAX_SPEED = 25;

    public int getSpeed() {
        return timeCalcConfiguration.speedProperty.getValue();
    }

    public void resetSpeed() {
        timeCalcConfiguration.speedProperty.setValue(Integer.MAX_VALUE);
    }

    public void enableFloatingTime() {
        this.timeCalcConfiguration.speedFloatingProperty.enable();
    }

    public void disableFloatingTieme() {
        this.timeCalcConfiguration.speedFloatingProperty.disable();
    }
}
