package org.nanoboot.utils.timecalc.swing.windows;

import org.nanoboot.utils.timecalc.app.CommandActionListener;
import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.app.TimeCalcApp;
import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
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
import org.nanoboot.utils.timecalc.swing.common.TimeCalcKeyAdapter;
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
import org.nanoboot.utils.timecalc.swing.progress.ProgressBar;
import org.nanoboot.utils.timecalc.swing.progress.ProgressCircle;
import org.nanoboot.utils.timecalc.swing.progress.ProgressDot;
import org.nanoboot.utils.timecalc.swing.progress.ProgressFuelGauge;
import org.nanoboot.utils.timecalc.swing.progress.ProgressLife;
import org.nanoboot.utils.timecalc.swing.progress.ProgressMoney;
import org.nanoboot.utils.timecalc.swing.progress.ProgressRotation;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSquare;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSwing;
import org.nanoboot.utils.timecalc.swing.progress.ProgressWater;
import org.nanoboot.utils.timecalc.swing.progress.Time;
import org.nanoboot.utils.timecalc.swing.progress.WalkingHumanProgress;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.swing.progress.battery.DayBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.HourBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.MinuteBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.MonthBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.WeekBattery;
import org.nanoboot.utils.timecalc.swing.progress.battery.YearBattery;
import org.nanoboot.utils.timecalc.swing.progress.weather.ProgressWeather;
import org.nanoboot.utils.timecalc.utils.common.Constants;
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.FileConstants;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TTime;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.ChangeListener;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.ReadOnlyProperty;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.nanoboot.utils.timecalc.app.Main.ONLY_ACTIVITIES_WINDOW_IS_ALLOWED;

/**
 * @author Robert Vokac
 * @since 08.02.2024
 */
public class MainWindow extends TWindow {

    public static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    public static final Color FOREGROUND_COLOR = new Color(210, 210, 210);
    public static final JCheckBox hideShowFormsCheckBox = new JCheckBox();
    private static final int BATTERY_HEIGHT = 120;
    private static final String BACKUP = "_backup";
    private static final String BASIC_FEATURE__ = "__only_basic_features__";
    private static final String DASH = "-";
    public static final int HOUR_GLASS_ICON_HEIGHT = (int) (68d / 4d);
    public static final int HOUR_GLASS_ICON_WIDTH = (int) (99d / 4d);
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
    private final TTextField pauseStartTextField;
    private final TTextField noteTextField;
    private final TCheckBox timeOffCheckBox = new TCheckBox("Time off");
    private final TTextField departureTextField;
    private final TTextField elapsedDayTextField;
    private final TTextField remainingDayTextField;
    private final TTextField elapsedWeekTextField;
    private final TTextField remainingWeekTextField;
    private final TTextField elapsedPauseTextField;
    private final TTextField remainingPauseTextField;
    private final TTextField endPauseTextField;
    private final TButton saveButton;
    private final ProgressLife progressLife;
    private final ProgressMoney progressMoney;
    private final ProgressWeather progressWeather;
    private final ProgressFuelGauge progressFuelGauge;
    private final ProgressRotation progressRotation;
    private final ProgressBar progressBar;
    private final ProgressWater progressWater;
    private final JLabel hourGlassElapsedDayIcon;
    private final JLabel hourGlassRemainsDayIcon;
    private final JLabel hourGlassElapsedWeekIcon;
    private final JLabel hourGlassRemainsWeekIcon;
    private final JLabel hourGlassElapsedPauseIcon;
    private final JLabel hourGlassRemainsPauseIcon;
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
    private boolean pauseNotifications = false;

    {
        ChangeListener valueMustBeTime
                = (property, oldValue, newValue) -> new TTime((String) newValue);
        this.arrivalTextField = new TTextField(Constants.DEFAULT_ARRIVAL_TIME, 50, true, valueMustBeTime);
        this.overtimeTextField = new TTextField(Constants.DEFAULT_OVERTIME, 50, true, valueMustBeTime);
        this.workingTimeTextField = new TTextField("08:00", 50, true, valueMustBeTime);
        this.pauseTimeTextField = new TTextField("00:30", 50, true, valueMustBeTime);
        this.pauseStartTextField = new TTextField("11:00", 50, true, valueMustBeTime);

        this.noteTextField = new TTextField("", 100);
        this.departureTextField = new TTextField();
        this.elapsedDayTextField = new TTextField("", 80);
        this.remainingDayTextField = new TTextField("", 80);
        this.elapsedWeekTextField = new TTextField("", 80);
        this.remainingWeekTextField = new TTextField("", 80);
        this.elapsedPauseTextField = new TTextField("", 80);
        this.remainingPauseTextField = new TTextField("", 80);
        this.endPauseTextField = new TTextField("", 50);
        //
        this.elapsedDayTextField.setAutoManageForeground(false);
        this.remainingDayTextField.setAutoManageForeground(false);
        this.elapsedWeekTextField.setAutoManageForeground(false);
        this.remainingWeekTextField .setAutoManageForeground(false);
        this.elapsedPauseTextField.setAutoManageForeground(false);
        this.remainingPauseTextField.setAutoManageForeground(false);
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
        this.configButton = new TButton("Config", 60);
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
        this.workDaysButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Working Days", 100);
        this.activitiesButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Activities");
        this.restartButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Restart");
        this.exitButton = new TButton("Exit", 50);
        this.focusButton = new TButton(allowOnlyBasicFeaturesProperty.getValue() ? " " : "Focus", 70);
        this.helpButton = new TButton("Help", 60);
        this.weatherButton = new TButton("Weather");
        this.commandButton = new TButton("Run", 40);
        this.jokeButton = new TButton("Joke", 60);
        this.aboutButton = new AboutButton();

        Function<String, ImageIcon> loadImageIcon = (p) ->
                new ImageIcon(new javax.swing.ImageIcon(getClass()
                        .getResource(p)).getImage()
                        .getScaledInstance(
                                HOUR_GLASS_ICON_HEIGHT, HOUR_GLASS_ICON_WIDTH, Image.SCALE_SMOOTH));
        Supplier<JLabel> supplyHourglassElapsedImageIcon = () -> new JLabel(loadImageIcon.apply("/hourglass_elapsed.png"));
        Supplier<JLabel> supplyHourglassRemainsImageIcon = () -> new JLabel(loadImageIcon.apply("/hourglass_remains.png"));
        this.hourGlassElapsedDayIcon = supplyHourglassElapsedImageIcon.get();
        this.hourGlassRemainsDayIcon = supplyHourglassRemainsImageIcon.get();
        this.hourGlassElapsedWeekIcon = supplyHourglassElapsedImageIcon.get();
        this.hourGlassRemainsWeekIcon = supplyHourglassRemainsImageIcon.get();
        this.hourGlassElapsedPauseIcon = supplyHourglassElapsedImageIcon.get();
        this.hourGlassRemainsPauseIcon = supplyHourglassRemainsImageIcon.get();
        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            addAll(
                    hourGlassElapsedDayIcon,
                    hourGlassRemainsDayIcon,
                    hourGlassElapsedWeekIcon,
                    hourGlassRemainsWeekIcon,
                    hourGlassElapsedPauseIcon,
                    hourGlassRemainsPauseIcon
            );
        }

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
                        clock.getY(), BATTERY_HEIGHT);
        add(minuteBattery);
        Battery hourBattery = new HourBattery(
                minuteBattery.getBounds().x + minuteBattery.getWidth()
                + SwingUtils.MARGIN,
                minuteBattery.getY(),
                BATTERY_HEIGHT);
        add(hourBattery);

        Battery dayBattery = new DayBattery(
                hourBattery.getBounds().x + hourBattery.getWidth()
                + SwingUtils.MARGIN,
                hourBattery.getY(),
                BATTERY_HEIGHT);
        add(dayBattery);

        Battery weekBattery = new WeekBattery(
                dayBattery.getBounds().x + dayBattery.getWidth() + SwingUtils.MARGIN,
                dayBattery.getY(),
                BATTERY_HEIGHT);
        add(weekBattery);

        Battery monthBattery = new MonthBattery(
                weekBattery.getBounds().x + weekBattery.getWidth()
                + SwingUtils.MARGIN,
                weekBattery.getY(), BATTERY_HEIGHT);
        add(monthBattery);
        Battery yearBattery = new YearBattery(
                monthBattery.getBounds().x + monthBattery.getWidth()
                + SwingUtils.MARGIN,
                monthBattery.getY(), BATTERY_HEIGHT);
        add(yearBattery);

        WalkingHumanProgress walkingHumanProgress
                = new WalkingHumanProgress();
        walkingHumanProgress.setBounds(minuteBattery.getX(),
                minuteBattery.getY() + minuteBattery.getHeight() + SwingUtils.MARGIN, 400, 80);
        add(walkingHumanProgress);
        walkingHumanProgress.visibleProperty
                .bindTo(timeCalcConfiguration.walkingHumanVisibleProperty);
        weatherButton
                .setBounds(SwingUtils.MARGIN, walkingHumanProgress.getY()
                        + walkingHumanProgress.getHeight());

        //
        ProgressSquare progressSquare = new ProgressSquare();
        progressSquare
                .setBounds(yearBattery.getX() + yearBattery.getWidth() + SwingUtils.MARGIN, yearBattery.getY(),
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
        //
        this.progressRotation = new ProgressRotation();
        progressRotation.setBounds(progressFuelGauge.getX() + progressFuelGauge.getWidth() + SwingUtils.MARGIN, progressFuelGauge.getY(),
                100, 100);

        progressRotation.visibleProperty
                .bindTo(timeCalcConfiguration.rotationVisibleProperty);
        progressRotation.typeProperty
                .bindTo(timeCalcConfiguration.rotationTypeProperty);
        progressRotation.hiddenProperty
                .bindTo(timeCalcConfiguration.rotationHiddenProperty);

        add(progressRotation);
        //
        this.progressBar = new ProgressBar();
        progressBar.setBounds(progressSwing.getX(), progressSwing.getY() + progressSwing.getHeight() + SwingUtils.MARGIN,
                progressRotation.getX() + progressRotation.getWidth() - 2 * SwingUtils.MARGIN, 25);

        progressBar.visibleProperty
                .bindTo(timeCalcConfiguration.barVisibleProperty);
        progressBar.typeProperty
                .bindTo(timeCalcConfiguration.barTypeProperty);
        progressBar.hiddenProperty
                .bindTo(timeCalcConfiguration.barHiddenProperty);
        progressBar.heightProperty
                .bindTo(timeCalcConfiguration.barHeightProperty);

        add(progressBar);
        //

        this.progressWater = new ProgressWater();
        progressWater.setBounds(progressSquare.getX() + progressSquare.getWidth() + 4 * SwingUtils.MARGIN,
                progressSquare.getY(),
                100, 520 /*exitButton.getY() + exitButton.getHeight() - SwingUtils.MARGIN*/);

        progressWater.visibleProperty
                .bindTo(timeCalcConfiguration.waterVisibleProperty);
        progressWater.typeProperty
                .bindTo(timeCalcConfiguration.waterTypeProperty);
        progressWater.hiddenProperty
                .bindTo(timeCalcConfiguration.waterHiddenProperty);
        progressWater.coloredProperty
                .bindTo(timeCalcConfiguration.waterColoredProperty);

        add(progressWater);
        //

        {
            progressSquare.typeProperty
                    .bindTo(timeCalcConfiguration.squareTypeProperty);
            progressDot.typeProperty
                    .bindTo(timeCalcConfiguration.dotTypeProperty);
            progressDot.visibleProperty
                    .bindTo(timeCalcConfiguration.dotVisibleProperty);
            progressCircle.typeProperty
                    .bindTo(timeCalcConfiguration.circleTypeProperty);
            progressCircle.innerCircleVisibleProperty.bindTo(timeCalcConfiguration.circleInnerCircleVisibleProperty);
            progressCircle.outerCircleOnlyBorderProperty.bindTo(timeCalcConfiguration.circleOuterCircleOnlyBorderProperty);
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
        TLabel arrivalTextFieldLabel = new TLabel("Arrival:", 50);
        arrivalTextFieldLabel.setBoundsFromTop(progressBar, 2);

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
        TLabel pauseLenghtTimeInMinutesFieldLabel = new TLabel("Pause length:", 80);
        pauseLenghtTimeInMinutesFieldLabel.setBoundsFromLeft(workingTimeTextField, 15);

        pauseTimeTextField.setBoundsFromLeft(pauseLenghtTimeInMinutesFieldLabel);
        TButton pauseIncreaseButton = new SmallTButton('+');
        TButton pauseDecreaseButton = new SmallTButton('-');
        pauseIncreaseButton.setBounds(pauseTimeTextField.getX() + pauseTimeTextField.getWidth(), pauseTimeTextField.getY(), 15, 15);
        pauseDecreaseButton.setBounds(pauseTimeTextField.getX() + pauseTimeTextField.getWidth(), pauseTimeTextField.getY() + 15, 15, 15);

        //
        TLabel pauseStartTimeLabel = new TLabel("Pause start:", 70);
        pauseStartTimeLabel.setBoundsFromLeft(pauseIncreaseButton, 15);

        pauseStartTextField.setBoundsFromLeft(pauseStartTimeLabel);
        TButton pauseStartIncreaseButton = new SmallTButton('+');
        TButton pauseStartDecreaseButton = new SmallTButton('-');
        pauseStartIncreaseButton.setBounds(pauseStartTextField.getX() + pauseStartTextField.getWidth(), pauseStartTextField.getY(), 15, 15);
        pauseStartDecreaseButton.setBounds(pauseStartTextField.getX() + pauseStartTextField.getWidth(), pauseStartTextField.getY() + 15, 15, 15);




        //
        TLabel noteTextFieldLabel = new TLabel("Note:", 50);
        noteTextFieldLabel.setBoundsFromTop(arrivalTextFieldLabel);

        noteTextField.setBoundsFromLeft(noteTextFieldLabel);
        timeOffCheckBox.setBoundsFromLeft(noteTextField);

        this.saveButton = new TButton("Save", 80);
        saveButton.setBoundsFromLeft(timeOffCheckBox);
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
            add(pauseLenghtTimeInMinutesFieldLabel);
            add(pauseTimeTextField);
            add(pauseIncreaseButton);
            add(pauseDecreaseButton);
            add(pauseStartTimeLabel);
            add(pauseStartTextField);
            add(pauseStartIncreaseButton);
            add(pauseStartDecreaseButton);
        }

        arrivalIncreaseButton.addActionListener(e -> increaseArrival(TTime.T_TIME_ONE_MINUTE));
        arrivalDecreaseButton.addActionListener(e -> decreaseArrival(TTime.T_TIME_ONE_MINUTE));
        overtimeIncreaseButton.addActionListener(e -> increaseOvertime(TTime.T_TIME_ONE_MINUTE));
        overtimeDecreaseButton.addActionListener(e -> decreaseOvertime(TTime.T_TIME_ONE_MINUTE));
        workingIncreaseButton.addActionListener(e -> increaseWork(TTime.T_TIME_ONE_MINUTE));
        workingDecreaseButton.addActionListener(e -> decreaseWork(TTime.T_TIME_ONE_MINUTE));
        pauseIncreaseButton.addActionListener(e -> increasePauseLength(TTime.T_TIME_ONE_MINUTE));
        pauseDecreaseButton.addActionListener(e -> decreasePauseLength(TTime.T_TIME_ONE_MINUTE));
        pauseStartIncreaseButton.addActionListener(e -> increasePauseStart(TTime.T_TIME_ONE_MINUTE));
        pauseStartDecreaseButton.addActionListener(e -> decreasePauseStart(TTime.T_TIME_ONE_MINUTE));

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(noteTextFieldLabel);
        add(noteTextField);
        add(timeOffCheckBox);
        //
        TLabel departureTextFieldLabel = new TLabel("Departure:", 65);
        departureTextFieldLabel.setBoundsFromLeft(saveButton, 50);

        departureTextField.setBoundsFromLeft(departureTextFieldLabel);
        departureTextField.setEditable(false);
        //

        TLabel dayLabel = new TLabel("Day:", 50);
        dayLabel.setBoundsFromTop(noteTextFieldLabel);


        remainingDayTextField.setBoundsFromLeft(dayLabel);
        remainingDayTextField.setEditable(false);

        elapsedDayTextField.setBoundsFromLeftWithAdditionalX(remainingDayTextField, 15);
        elapsedDayTextField.setEditable(false);
        //

        if(!allowOnlyBasicFeaturesProperty.getValue()) add(departureTextFieldLabel);
        add(departureTextField);
        TLabel endPauseTextFieldLabel = new TLabel("Pause end:", 70);
        endPauseTextFieldLabel.setBounds(pauseStartTimeLabel.getX(), departureTextFieldLabel.getY());

        endPauseTextField.setBoundsFromLeft(endPauseTextFieldLabel);
        endPauseTextField.setEditable(false);
        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(dayLabel);
            add(elapsedDayTextField);
            add(remainingDayTextField);
        }
        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(saveButton);
        }
        this.workingDayRepository = new WorkingDayRepositorySQLiteImpl(timeCalcApp.getSqliteConnectionFactory());
        this.activityRepository = new ActivityRepositorySQLiteImpl(timeCalcApp.getSqliteConnectionFactory());

        ////////
//        TTextField field = new TTextField("");
//        field.setBoundsFromLeft(weekLabel);
//
//        field.setEditable(false);
//        field.setVisible(false);
        //

        //
        TLabel weekLabel = new TLabel("Week", 50);
        weekLabel.setBoundsFromLeft(elapsedDayTextField);

        remainingWeekTextField.setBoundsFromLeft(weekLabel);
        remainingWeekTextField.setEditable(false);
        //
        elapsedWeekTextField.setBoundsFromLeftWithAdditionalX(remainingWeekTextField, 15);
        elapsedWeekTextField.setEditable(false);
        //

        //

        //

        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(weekLabel);
            add(elapsedWeekTextField);
            add(remainingWeekTextField);
        }
        ////////
//        TTextField field2 = new TTextField("");
//        field2.setBoundsFromLeft(pauseLabel);
//
//        field2.setEditable(false);
//        field2.setVisible(false);
        //

        //
        TLabel pauseLabel = new TLabel("Pause:", 50);
        pauseLabel.setBoundsFromLeft(elapsedWeekTextField);

        remainingPauseTextField.setBoundsFromLeft(pauseLabel);
        remainingPauseTextField.setEditable(false);
        //
        elapsedPauseTextField.setBoundsFromLeftWithAdditionalX(remainingPauseTextField, 15);
        elapsedPauseTextField.setEditable(false);
        //

        //

        if(!allowOnlyBasicFeaturesProperty.getValue()) {
            add(pauseLabel);
            add(elapsedPauseTextField);
            add(remainingPauseTextField);
            add(endPauseTextFieldLabel);
            add(endPauseTextField);
        }

        BiConsumer<JLabel, JComponent> setBoundsForHourGlassIcon = (l, c) ->
            l.setBounds(c.getX() - HOUR_GLASS_ICON_WIDTH,c.getY(), HOUR_GLASS_ICON_HEIGHT, HOUR_GLASS_ICON_WIDTH);
        setBoundsForHourGlassIcon.accept(hourGlassElapsedDayIcon, elapsedDayTextField);
        setBoundsForHourGlassIcon.accept(hourGlassRemainsDayIcon, remainingDayTextField);
        setBoundsForHourGlassIcon.accept(hourGlassElapsedWeekIcon, elapsedWeekTextField);
        setBoundsForHourGlassIcon.accept(hourGlassRemainsWeekIcon, remainingWeekTextField);
        setBoundsForHourGlassIcon.accept(hourGlassElapsedPauseIcon, elapsedPauseTextField);
        setBoundsForHourGlassIcon.accept(hourGlassRemainsPauseIcon, remainingPauseTextField);

        ////////
        //

        workDaysButton.setBoundsFromTop(dayLabel);
        activitiesButton.setBoundsFromLeft(workDaysButton);
        configButton.setBoundsFromLeft(activitiesButton);


        //
        helpButton.setBoundsFromLeft(configButton);
        focusButton.setBoundsFromLeft(helpButton);
        commandButton.setBoundsFromLeft(focusButton);
        jokeButton.setBoundsFromLeft(commandButton);
        hideShowFormsCheckBox.setSelected(true);
        hideShowFormsCheckBox.setBounds(
                jokeButton.getX() + jokeButton.getWidth() + SwingUtils.MARGIN,
                jokeButton.getY(), 20, 20);
        //
        restartButton.setBoundsFromLeft(hideShowFormsCheckBox);
        exitButton.setBoundsFromLeft(restartButton);
        aboutButton.setBounds(exitButton.getX(),
                exitButton.getY() + exitButton.getHeight() + SwingUtils.MARGIN);

        setLayout(null);
        setVisible(ONLY_ACTIVITIES_WINDOW_IS_ALLOWED ? false : true);

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

        int standardWidth = progressSquare.getX() + progressSquare.getWidth()
                            + 5 * SwingUtils.MARGIN;
        setSize(standardWidth,
                focusButton.getY() + focusButton.getHeight() + SwingUtils.MARGIN
                + focusButton.getHeight() + 2 * SwingUtils.MARGIN);

        saveButton.addActionListener(e -> {

            TTime arrival_ = new TTime(arrivalTextField.getText());
            TTime overtime_ = new TTime(overtimeTextField.getText());
            TTime work_ = new TTime(workingTimeTextField.getText());
            TTime pause_ = new TTime(pauseTimeTextField.getText());
            TTime pauseStart = new TTime(pauseStartTextField.getText());

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
            workingDay.setPauseStartHour(pauseStart.getHour());
            workingDay.setPauseStartMinute(pauseStart.getMinute());
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
            pauseStartTextField.valueProperty.setValue(new TTime(wd.getPauseStartHour(), wd.getPauseStartMinute()).toString().substring(0,5));
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
            if(ONLY_ACTIVITIES_WINDOW_IS_ALLOWED) {
                break;
            }
            setSize(standardWidth + (timeCalcConfiguration.waterVisibleProperty.isEnabled() ? 120 : 0),
                    focusButton.getY() + focusButton.getHeight() + SwingUtils.MARGIN
                    + focusButton.getHeight() + 2 * SwingUtils.MARGIN);

            boolean stronglyColored = Visibility
                    .valueOf(timeCalcApp.visibilityProperty.getValue())
                    .isStronglyColored();
            if (stronglyColored) {
                remainingDayTextField.setForeground(Color.RED);
                elapsedDayTextField.setForeground(Color.GREEN);
                remainingWeekTextField.setForeground(Color.RED);
                elapsedWeekTextField.setForeground(Color.GREEN);
                remainingPauseTextField.setForeground(Color.RED);
                elapsedPauseTextField.setForeground(Color.GREEN);

            }

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
                            if (fileAgeInDays > 28) {
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
        if (!ONLY_ACTIVITIES_WINDOW_IS_ALLOWED && activitiesWindow != null) {
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

        if(ONLY_ACTIVITIES_WINDOW_IS_ALLOWED) {
            openActivitiesWindow();
        }
    }

    private final Set<Integer> alreadyShownPercents = new HashSet<>();

    private boolean updateWindow(TimeCalcApp timeCalcApp, Time time,
            AnalogClock clock, MinuteBattery minuteBattery, Battery hourBattery,
            Battery dayBattery, Battery weekBattery, Battery monthBattery,
            Battery yearBattery, WalkingHumanProgress walkingHumanProgress,
            ProgressSquare progressSquare, ProgressCircle progressCircle,
            ProgressSwing progressSwing,
            ComponentRegistry<Component> componentRegistry,
            TLabel arrivalTextFieldLabel) {
        //System.out.println("timeCalcConfiguration.handsLongProperty=" + timeCalcConfiguration.clockHandLongProperty.isEnabled());

        String newCurrentDay = time.yearProperty + DASH + time.monthProperty + DASH
                               + time.dayOfWeekProperty;
        if(!newCurrentDay.equals(currentDay)) {
            currentDay = newCurrentDay;
            newDay = true;
            alreadyShownPercents.clear();
        } else {

        }
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

        if(timeElapsed.toTotalMilliseconds() > 8.5 * 3600000) {
            timeElapsed = new TTime(8,30);
        }
        if(timeRemains.toTotalMilliseconds() > 8.5 * 3600000) {
            timeRemains = new TTime(8,30);
        }
        if(timeElapsed.toTotalMilliseconds() < 0) {
            timeElapsed = new TTime(0, 0);
        }
        if(timeRemains.toTotalMilliseconds() < 0) {
            timeRemains = new TTime(0, 0);
        }

        String timeElapsedString = timeElapsed.toString();
        String timeRemainsString = timeRemains.toString();

        int dayOfWeek = time.dayOfWeekProperty.getValue();

        TTime timeWeekElapsed = dayOfWeek < 6 ? timeElapsed.cloneInstance() : TTime.ofMinutes(
                (int) (5d * 8.5d * 60d));
        TTime timeWeekRemains = dayOfWeek < 6 ? timeRemains.cloneInstance() : TTime.ofMinutes(0);

        if(dayOfWeek < 6) {
            if (dayOfWeek > 1) {
                timeWeekElapsed = timeWeekElapsed.add(TTime.ofMinutes(
                        (int) ((dayOfWeek - 1) * 8.5d * 60d)));
            }
            if (dayOfWeek < 5) {
                timeWeekRemains = timeWeekRemains.add(TTime.ofMinutes(
                        (int) ((5 - dayOfWeek) * 8.5d * 60d)));
            }
        }

        int secondsRemains = 60 - secondNow;
        int millisecondsRemains = 1000 - millisecondNow;
        if (!remainingDayTextField.valueProperty.getValue()
                .equals(timeRemainsString)) {
            remainingDayTextField.valueProperty.setValue(timeRemainsString);
        }
        remainingWeekTextField.valueProperty.setValue(timeWeekRemains.toString());

        if (!elapsedDayTextField.valueProperty.getValue()
                .equals(timeElapsedString)) {
            elapsedDayTextField.valueProperty.setValue(timeElapsedString);
        }
        elapsedWeekTextField.valueProperty.setValue(timeWeekElapsed.toString());
        //            if (!elapsedTextField.valueProperty.getValue()
        //                    .equals(timeElapsed.remove(new TimeHM(0,1)).toString())) {
        //                String s = timeElapsed.remove(new TimeHM(0,1)).toString();
        //                elapsedTextField.valueProperty.setValue(s + ":" + (secondNow < 10 ? "0" : "") + secondNow + ":" + (millisecondNow < 10 ? "00" : (millisecondNow < 100 ? "0" : millisecondNow)) + millisecondNow);
        //            }
        ////
        TTime pauseStart = pauseStartTextField.asTTime();
        TTime pauseEnd = pauseStart.add(pauseTimeTextField.asTTime());
        boolean beforePause = nowTime.toTotalMilliseconds() < pauseStart.toTotalMilliseconds();
        boolean afterPause = nowTime.toTotalMilliseconds() > pauseEnd.toTotalMilliseconds();
        boolean duringPause = !beforePause && !afterPause;

        TTime pauseElapsed = beforePause ? TTime.ofMinutes(0) : (afterPause ? pauseTimeTextField.asTTime() : nowTime.remove(pauseStart));
        double pauseElapsedMilliseconds = pauseElapsed.toTotalMilliseconds();
        double pauseTotalMilliseconds = pauseTimeTextField.asTTime().toTotalMilliseconds();
        double pauseProgress = pauseElapsedMilliseconds / pauseTotalMilliseconds;
        TTime pauseRemains = afterPause ? TTime.ofMinutes(0) : (beforePause ? pauseTimeTextField.asTTime() : pauseEnd.remove(nowTime));
        endPauseTextField.valueProperty.setValue(pauseEnd.toString().substring(0,5));
        elapsedPauseTextField.valueProperty.setValue(pauseElapsed.toString());
        remainingPauseTextField.valueProperty.setValue(pauseRemains.toString());

        if(timeCalcConfiguration.testEnabledProperty.isDisabled()) {
            if (duringPause && !pauseNotifications) {
                pauseNotifications = true;
                Utils.showNotification(
                        "It is the time for pause. Please, eat something and do not work.",
                        remainingPauseTextField.asTTime().toTotalMilliseconds(),
                        400);
            }
            if (!duringPause && pauseNotifications) {
                pauseNotifications = false;
            }
        }

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
        String birthDate = timeCalcConfiguration.lifeBirthDateProperty.getValue();
        Calendar birthDateCal = Calendar.getInstance();
        if(birthDate != null&& !birthDate.isEmpty()) {
            String[] array = birthDate.split("-");
            if(array.length == 3) {
                try {
                    int year = Integer.valueOf(array[0]);
                    int month = Integer.valueOf(array[1]);
                    int day = Integer.valueOf(array[2]);
                    birthDateCal.set(Calendar.YEAR, year);
                    birthDateCal.set(Calendar.MONTH, month - 1);
                    birthDateCal.set(Calendar.DAY_OF_MONTH, day);
                } catch (Exception e) {
                    System.err.println("Parsing birth date failed: " + birthDate + " : " + birthDate);
                    birthDateCal = null;
                }
            }
        }
        double lifeProgress = Progress.getLifeProgress(
                birthDateCal == null ? null : birthDateCal.getTime(),
                time.asCalendar().getTime());
        progress.set(WidgetType.HOUR, hourProgress);
        progress.set(WidgetType.WEEK, weekProgress);
        progress.set(WidgetType.MINUTE, minuteProgress);
        progress.set(WidgetType.YEAR, yearProgress);
        progress.set(WidgetType.LIFE, lifeProgress);
        progress.set(WidgetType.PAUSE, pauseProgress);
        progressSquare.setProgress(progress);
        progressCircle.setProgress(progress);
        progressSwing.setProgress(progress);
        progressLife.setProgress(progress);
        progressMoney.setProgress(progress);
        progressDot.setProgress(progress);
        progressFuelGauge.setProgress(progress);
        progressRotation.setProgress(progress);
        progressBar.setProgress(progress);
        progressWater.setProgress(progress);
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

        {

            final int donePercentInt = (int) (Math.floor(progress.getDonePercent(WidgetType.DAY) * 100));

            int percentInt = donePercentInt;
            if (/*donePercentInt % 5 == 0 &&*/ !alreadyShownPercents.contains(donePercentInt) && timeCalcConfiguration.testEnabledProperty.isDisabled()) {
                alreadyShownPercents.add(donePercentInt);
                Toaster toasterManager = new Toaster();
                Font font = new Font("sans", Font.PLAIN, 16);
                toasterManager.setToasterMessageFont(font);
                toasterManager.setDisplayTime(10000);
                toasterManager.setToasterWidth(600);
                toasterManager.setToasterHeight(400);
                toasterManager.setToasterColor(new Color(255, 255, 204));

                byte[] btDataFile = Utils.decodeBase64ToByteArray(
                        "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");
                BufferedImage image = null;
                try {
                    image = percentInt == 100
                            ? ImageIO.read(new ByteArrayInputStream(btDataFile))
                            : null;
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (getSpeed() == Integer.MAX_VALUE) {
                    if (image != null) {
                        //                toasterManager.setToasterWidth(600);
                        //                toasterManager.setToasterHeight(400);
                        toasterManager.showToaster(new ImageIcon(image),
                                "Progress: " + (percentInt) + "%");
                        toasterManager.showToaster(
                                "Congratulation :-) It is the time to go home.");
                    } else {
                        toasterManager.setToasterHeight(200);
                        toasterManager.showToaster("Progress: " + (percentInt) + "%");
                    }
                }

            }
        }

        walkingHumanProgress.setProgress(progress);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        return false;
    }

    private String currentDay = "";
    private boolean newDay = true;
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

    public void increasePauseLength(TTime tTime) {
        pauseTimeTextField.valueProperty.setValue(new TTime(this.pauseTimeTextField.valueProperty.getValue()).add(tTime).toString().substring(0, 5));
    }

    public void decreasePauseLength(TTime tTime) {
        pauseTimeTextField.valueProperty.setValue(
                new TTime(this.pauseTimeTextField.valueProperty.getValue())
                        .remove(tTime).toString().substring(0, 5));
    }

    public void increasePauseStart(TTime tTime) {
        pauseStartTextField.valueProperty.setValue(new TTime(this.pauseStartTextField.valueProperty.getValue()).add(tTime).toString().substring(0, 5));
    }

    public void decreasePauseStart(TTime tTime) {
        pauseStartTextField.valueProperty.setValue(
                new TTime(this.pauseStartTextField.valueProperty.getValue())
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
