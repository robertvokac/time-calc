package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.AboutButton;
import org.nanoboot.utils.timecalc.swing.common.ComponentRegistry;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcButton;
import org.nanoboot.utils.timecalc.swing.common.TimeCalcWindow;
import org.nanoboot.utils.timecalc.swing.common.Toaster;
import org.nanoboot.utils.timecalc.swing.common.WeatherWindow;
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
import org.nanoboot.utils.timecalc.utils.common.DateFormats;
import org.nanoboot.utils.timecalc.utils.common.Jokes;
import org.nanoboot.utils.timecalc.utils.common.TimeHM;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private static final int MARGIN = 10;
    public static final Color BG = new Color(238, 238, 238);
    public static final Color FG = new Color(210, 210, 210);

    private final String windowTitle;
    private final int totalMinutes;

    private final TimeHM startTime;
    private final TimeHM overtime;
    private final TimeHM endTime;
    private final TimeCalcApp timeCalcApp;
    private boolean stopBeforeEnd = false;
    private Time time = new Time();
    private TimeCalcConfiguration timeCalcConfiguration = new TimeCalcConfiguration();

    public TimeCalcManager(String startTimeIn, String overTimeIn,
            TimeCalcApp timeCalcApp) {
        this.timeCalcApp = timeCalcApp;
        timeCalcConfiguration.setFromTimeCalcProperties(TimeCalcProperties.getInstance());
//        Utils.everythingHidden
//                .setValue(TimeCalcConf.getInstance().isEverythingHidden());
//        Utils.toastsAreEnabled
//                .setValue(TimeCalcConf.getInstance().areToastsEnabled());

        overTimeIn = (overTimeIn == null || overTimeIn.isEmpty()) ?
                Constants.DEFAULT_OVERTIME : overTimeIn;

        this.startTime = new TimeHM(startTimeIn);
        this.overtime = new TimeHM(overTimeIn);

        this.endTime = new TimeHM(startTime.getHour() + Constants.WORKING_HOURS_LENGTH + overtime.getHour(),
                startTime.getMinute() + Constants.WORKING_MINUTES_LENGTH + overtime.getMinute());

        this.totalMinutes = TimeHM.countDiffInMinutes(startTime, endTime);
        int totalSeconds = totalMinutes * TimeHM.SECONDS_PER_MINUTE;
        int totalMilliseconds = totalSeconds * TimeHM.MILLISECONDS_PER_SECOND;

        TimeCalcWindow window = new TimeCalcWindow();

        TimeCalcButton commandButton = new TimeCalcButton("Command");
        TimeCalcButton weatherButton = new TimeCalcButton("Weather");
        TimeCalcButton jokeButton = new TimeCalcButton("Joke");
        TimeCalcButton restartButton = new TimeCalcButton("Restart");
        TimeCalcButton exitButton = new TimeCalcButton("Exit");
        AboutButton aboutButton = new AboutButton();

        //window.add(weatherButton);
        window.addAll(commandButton, jokeButton, restartButton,
                exitButton);
        window.addKeyListener(new KeyAdapter() {
            // Key Pressed method
            public void keyPressed(KeyEvent e) {
                Visibility visibility = Visibility.valueOf(timeCalcApp.visibilityProperty.getValue());
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    timeCalcApp.visibilityProperty.setValue(Visibility.STRONGLY_COLORED.name());
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    timeCalcApp.visibilityProperty.setValue(Visibility.NONE.name());
                }
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    if(visibility.isNone()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.STRONGLY_COLORED.name());
                    } else {
                        timeCalcApp.visibilityProperty.setValue(Visibility.NONE.name());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    if(visibility.isGray()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.WEAKLY_COLORED.name());
                    } else {
                        timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_C) {
                    if(visibility.isStronglyColored()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.WEAKLY_COLORED.name());
                    } else {
                        timeCalcApp.visibilityProperty.setValue(Visibility.STRONGLY_COLORED.name());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_V) {
                    if(visibility.isNone()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.STRONGLY_COLORED.name());
                    } else {
                        timeCalcApp.visibilityProperty.setValue(Visibility.NONE.name());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if(visibility.isStronglyColored()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.WEAKLY_COLORED.name());
                    }
                    if(visibility.isWeaklyColored()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.GRAY.name());
                    }
                    if(visibility.isGray()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.NONE.name());
                    }
                    if(visibility.isNone()) {
                        timeCalcApp.visibilityProperty.setValue(Visibility.STRONGLY_COLORED.name());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    commandButton.doClick();
                }
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    Utils.toastsAreEnabled.flip();
                }

                window.repaint();
            }
        });
        WalkingHumanProgressAsciiArt walkingHumanProgressAsciiArt = new WalkingHumanProgressAsciiArt();
        walkingHumanProgressAsciiArt.setBounds(MARGIN, MARGIN + 210 + MARGIN, 450, 180);

        window.add(walkingHumanProgressAsciiArt);
        weatherButton
                .setBounds(20, walkingHumanProgressAsciiArt.getY() + walkingHumanProgressAsciiArt.getHeight() + MARGIN);
        commandButton.setBounds(20, walkingHumanProgressAsciiArt.getY() + walkingHumanProgressAsciiArt.getHeight() + MARGIN);

        jokeButton.setBounds(140, walkingHumanProgressAsciiArt.getY() + walkingHumanProgressAsciiArt.getHeight() + MARGIN);
        restartButton
                .setBounds(280, walkingHumanProgressAsciiArt.getY() + walkingHumanProgressAsciiArt.getHeight() + MARGIN);
        exitButton.setBounds(390, walkingHumanProgressAsciiArt.getY() + walkingHumanProgressAsciiArt.getHeight() + MARGIN);
        aboutButton.setBounds(exitButton.getX(),
                exitButton.getY() + exitButton.getHeight() + MARGIN);

        window.setLayout(null);

        window.setVisible(true);

        this.windowTitle = createWindowTitle();
        window.setTitle(windowTitle);

        weatherButton
                .addActionListener(e -> new WeatherWindow().setVisible(true));
        commandButton
                .addActionListener(e ->
                {
                    String commands = (String) JOptionPane.showInputDialog(
                            null,
                            "Run a command:",
                            "Command launching",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "test"
                    );
                    String[] commandsAsArray = commands.split(" ");
                    switch (commandsAsArray[0]) {
                        case "test":
                            JOptionPane.showMessageDialog(null, "Test");
                            break;
                        case "color":
                            timeCalcApp.visibilityProperty.setValue(commandsAsArray[1].equals("1") ? Visibility.STRONGLY_COLORED.name() : Visibility.WEAKLY_COLORED.name());
                            break;
                        case "gray":
                            timeCalcApp.visibilityProperty.setValue(commandsAsArray[1].equals("1") ? Visibility.GRAY.name() : Visibility.WEAKLY_COLORED.name());
                            break;
                        case "waves":
                            timeCalcConfiguration.batteryWavesEnabledProperty.setValue(commandsAsArray[1].equals("1"));
                            break;
                        case "uptime":
                            JOptionPane.showMessageDialog(null,
                                    timeCalcApp.getCountOfMinutesSinceAppStarted()
                                    + " minutes");
                            break;
                        case "toast":
                            Toaster t = new Toaster();
                            t.setToasterWidth(800);
                            t.setToasterHeight(800);
                            t.setDisplayTime(60000 * 5);
                            t.setToasterColor(Color.GRAY);
                            Font font = new Font("sans", Font.PLAIN, 12);
                            t.setToasterMessageFont(font);
                            t.setDisplayTime(5000);
                            t.showToaster(commands.substring(6));
                            break;
                        case "toasts":
                            Utils.toastsAreEnabled
                                    .setValue(commandsAsArray[1].equals("1"));
                            break;
                        default:
                            JOptionPane.showMessageDialog(null,
                                    "Unknown command: " + commandsAsArray[0]);
                    }
                });

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

                Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date());

        AnalogClock analogClock = new AnalogClock(startTime, endTime);
        analogClock.setBounds(MARGIN, MARGIN, 200);
        
        Properties testProperties = new Properties();
        File testPropertiesFile = new File("test.txt");
        try {
            if(testPropertiesFile.exists()) {
                testProperties.load(new FileInputStream(testPropertiesFile));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TimeCalcManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException rex) {
            Logger.getLogger(TimeCalcManager.class.getName()).log(Level.SEVERE, null, rex);
        }

        if(testProperties.containsKey("current.day")) {
            calNow.set(Calendar.DAY_OF_MONTH, Integer.parseInt((String) testProperties.get("current.day")));
            analogClock.dayProperty.setValue(Integer.valueOf((String) testProperties.get("current.day")));
        } else {
            analogClock.dayProperty.bindTo(time.dayProperty);
        }
        if(testProperties.containsKey("current.month")) {
            calNow.set(Calendar.MONTH, Integer.parseInt((String) testProperties.get("current.month")) - 1);
            analogClock.monthProperty.setValue(Integer.valueOf((String) testProperties.get("current.month")));
        } else {
            analogClock.monthProperty.bindTo(time.monthProperty);
        }
        if(testProperties.containsKey("current.year")) {
            calNow.set(Calendar.YEAR, Integer.parseInt((String) testProperties.get("current.year")));
            analogClock.yearProperty.setValue(Integer.valueOf((String) testProperties.get("current.year")));
        } else {
            analogClock.yearProperty.bindTo(time.yearProperty);
        }
        if(testProperties.containsKey("current.year") || testProperties.containsKey("current.month") ||testProperties.containsKey("current.day")) {
            analogClock.dayOfWeekProperty.setValue(calNow.get(Calendar.DAY_OF_WEEK));
        } else {
            analogClock.dayOfWeekProperty.bindTo(time.dayOfWeek);
        }
        if(testProperties.containsKey("current.hour")) {
            calNow.set(Calendar.HOUR, Integer.parseInt((String) testProperties.get("current.hour")));
            analogClock.hourProperty.setValue(Integer.valueOf((String) testProperties.get("current.hour")));
        } else {
            analogClock.hourProperty.bindTo(time.hourProperty);
        }
        if(testProperties.containsKey("current.minute")) {
            calNow.set(Calendar.MINUTE, Integer.parseInt((String) testProperties.get("current.minute")));
            analogClock.minuteProperty.setValue(Integer.valueOf((String) testProperties.get("current.minute")));
        } else {
            analogClock.minuteProperty.bindTo(time.minuteProperty);
        }
        if(testProperties.containsKey("current.second")) {
            calNow.set(Calendar.SECOND, Integer.parseInt((String) testProperties.get("current.second")));
            analogClock.secondProperty.setValue(Integer.valueOf((String) testProperties.get("current.second")));
        } else {
            analogClock.secondProperty.bindTo(time.secondProperty);
        }
        
        if(testProperties.containsKey("current.millisecond")) {
            calNow.set(Calendar.MILLISECOND, Integer.parseInt((String) testProperties.get("current.millisecond")));
            analogClock.millisecondProperty.setValue(Integer.valueOf((String) testProperties.get("current.millisecond")));
        } else {
            analogClock.millisecondProperty.bindTo(time.millisecondProperty);
        }
        analogClock.millisecondEnabledProperty.bindTo(timeCalcConfiguration.clockHandMillisecondEnabledProperty);
        analogClock.secondEnabledProperty.bindTo(timeCalcConfiguration.clockHandSecondEnabledProperty);
        analogClock.handsLongProperty.bindTo(timeCalcConfiguration.clockHandLongProperty);

        window.add(analogClock);

        ProgressSquare progressSquare = new ProgressSquare();
        progressSquare
                .setBounds(MARGIN + analogClock.getWidth() + MARGIN, MARGIN,
                        200);

        window.add(progressSquare);

        ProgressCircle progressCircle = new ProgressCircle();
        progressCircle
                .setBounds(
                        MARGIN + progressSquare.getBounds().x + progressSquare
                                .getWidth() + MARGIN, MARGIN, 80);

        window.add(progressCircle);

        Battery dayBattery = new DayBattery(progressCircle.getBounds().x,
                progressCircle.getY() + MARGIN + progressCircle.getHeight(), 140);
        window.add(dayBattery);


        Battery weekBattery = new WeekBattery(
                dayBattery.getBounds().x + dayBattery.getWidth() + MARGIN * 2,
                dayBattery.getY(), 140);
        window.add(weekBattery);

        int currentDayOfMonth = calNow.get(Calendar.DAY_OF_MONTH);

        int workDaysDone = 0;
        int workDaysTodo = 0;
        int workDaysTotal;
        for (int dayOfMonth = 1;
             dayOfMonth <= calNow.getActualMaximum(Calendar.DAY_OF_MONTH);
             dayOfMonth++) {
            DayOfWeek dayOfWeek = LocalDate.of(calNow.get(Calendar.YEAR),
                    calNow.get(Calendar.MONTH) + 1, dayOfMonth).getDayOfWeek();
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
                dayBattery.getBounds().x + dayBattery.getWidth(),
                dayBattery.getY() + weekBattery.getHeight() + MARGIN, 140);
        window.add(monthBattery);

        Battery hourBattery = new HourBattery(monthBattery.getBounds().x,
                monthBattery.getY() + monthBattery.getHeight() + MARGIN, 140);
        window.add(hourBattery);

        Rectangle dayRectangle = dayBattery.getBounds();
        hourBattery.setBounds(dayRectangle);
        hourBattery.setBounds(hourBattery.getX() + 2 * MARGIN, hourBattery.getY(), hourBattery.getWidth(), hourBattery.getHeight());
        dayBattery.setBounds(hourBattery.getX() + hourBattery.getWidth() + MARGIN, hourBattery.getY(), hourBattery.getWidth(), hourBattery.getHeight());
        weekBattery.setBounds(hourBattery.getX(), hourBattery.getY() + hourBattery.getHeight() + MARGIN, hourBattery.getWidth(), hourBattery.getHeight());
        monthBattery.setBounds(hourBattery.getX() + hourBattery.getWidth() + MARGIN, hourBattery.getY() + hourBattery.getHeight() + MARGIN, hourBattery.getWidth(), hourBattery.getHeight());

        hourBattery.wavesProperty.bindTo(timeCalcConfiguration.batteryWavesEnabledProperty);
        dayBattery.wavesProperty.bindTo(timeCalcConfiguration.batteryWavesEnabledProperty);
        weekBattery.wavesProperty.bindTo(timeCalcConfiguration.batteryWavesEnabledProperty);
        monthBattery.wavesProperty.bindTo(timeCalcConfiguration.batteryWavesEnabledProperty);

        ComponentRegistry componentRegistry = new ComponentRegistry();
        componentRegistry.addAll(
                walkingHumanProgressAsciiArt,
                progressSquare,
                progressCircle,
                analogClock,
                dayBattery,
                weekBattery,
                monthBattery,
                hourBattery,
                jokeButton,
                commandButton,
                restartButton,
                exitButton
        );
        walkingHumanProgressAsciiArt.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        progressSquare.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        progressCircle.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        analogClock.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        dayBattery.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        weekBattery.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        monthBattery.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        hourBattery.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        jokeButton.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        commandButton.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        restartButton.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);
        exitButton.visibilityProperty.bindTo(timeCalcApp.visibilityProperty);

        jokeButton.setVisible(!Visibility.valueOf(jokeButton.visibilityProperty.getValue()).isNone());
        commandButton.setVisible(!Visibility.valueOf(commandButton.visibilityProperty.getValue()).isNone());
        restartButton.setVisible(!Visibility.valueOf(restartButton.visibilityProperty.getValue()).isNone());
        exitButton.setVisible(!Visibility.valueOf(exitButton.visibilityProperty.getValue()).isNone());

//        timeCalcApp.visibilityProperty.addListener((Property<String> p, String oldValue, String newValue)-> {
//            System.out.println("Visibility of timeCalcApp was changed FROM " + oldValue + " TO " + newValue);
//        } );
//        analogClock.visibilityProperty.addListener((Property<String> p, String oldValue, String newValue)-> {
//            System.out.println("Visibility of analogClock was changed FROM " + oldValue + " TO " + newValue);
//        } );
        window.setSize(520 + 20 + 100, exitButton.getY() + 3 * exitButton.getHeight() + MARGIN);
        while (true) {
            //time.writeString();
            if(Math.random() > 0.95) {
                window.requestFocus();
            }
            if (stopBeforeEnd) {
                window.setVisible(false);
                window.dispose();
                break;
            }

            Visibility visibility = Visibility.valueOf(timeCalcApp.visibilityProperty.getValue());
            componentRegistry.setVisible(visibility.isNotNone());
            if (!visibility.isStronglyColored() || visibility.isGray()) {
                jokeButton.setBackground(BG);
                commandButton.setBackground(BG);
                restartButton.setBackground(BG);
                exitButton.setBackground(BG);

                jokeButton.setForeground(FG);
                commandButton.setForeground(FG);
                restartButton.setForeground(FG);
                exitButton.setForeground(FG);
            } else {
                jokeButton.setOriginalBackground();
                commandButton.setOriginalBackground();
                restartButton.setOriginalBackground();
                exitButton.setOriginalBackground();
                //
                jokeButton.setOriginalForeground();
                commandButton.setOriginalForeground();
                restartButton.setOriginalForeground();
                exitButton.setOriginalForeground();
            }
            jokeButton.setVisible(
                    TimeCalcProperties.getInstance().areJokesEnabled()
                    && !visibility.isNone());

            window.setTitle(visibility.isNone() ? "" : windowTitle);

            LocalDateTime now = LocalDateTime.now();
            String nowString =
                    DateFormats.DATE_TIME_FORMATTER_HHmmssSSS.format(now);

            int hourNow = Integer.parseInt(nowString.split(":")[0]);
            int minuteNow = Integer.parseInt(nowString.split(":")[1]);

            int secondNow = Integer.parseInt(nowString.split(":")[2]);
            int millisecondNow = Integer.parseInt(nowString.split(":")[3]);
            TimeHM timeRemains = new TimeHM(endTime.getHour() - hourNow, endTime.getMinute() - minuteNow);

            int secondsRemains = 60 - secondNow;
            int millisecondsRemains = 1000 - millisecondNow;

            int hourDone = Constants.WORKING_HOURS_LENGTH + overtime.getHour() - timeRemains.getHour();
            int minutesDone = Constants.WORKING_MINUTES_LENGTH + overtime.getMinute() - timeRemains.getMinute();
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
            weekBattery.setDonePercent(WeekBattery.getWeekProgress(weekDayWhenMondayIsOne, done));
            weekBattery.setLabel(
                    nowIsWeekend ? "5/5" : (weekDayWhenMondayIsOne + "/5"));

            monthBattery.setDonePercent(MonthBattery.getMonthProgress(weekDayWhenMondayIsOne, workDaysDone, workDaysTotal, done));
            monthBattery.setLabel(
                    (nowIsWeekend ? workDaysDone : workDaysDone + 1) + "/"
                    + (workDaysTotal));

            hourBattery.setDonePercent(HourBattery.getHourProgress(timeRemains, secondsRemains,
                    millisecondsRemains));
            if (!nowIsWeekend) {
                hourBattery.setLabel(
                        hourDone + "/" + (
                                totalMinutes / 60));
            }

            int totalSecondsRemains =
                    (timeRemains.getHour() * 60 * 60 + timeRemains.getMinute() * 60
                     + secondsRemains);
            int totalMillisecondsRemains =
                    totalSecondsRemains * 1000 + millisecondsRemains;
            double totalSecondsRemainsDouble =
                    ((double) totalMillisecondsRemains) / 1000;

//            if (timeRemains.getHour() == 0 && timeRemains.getMinute() <= 3) {
//                Utils.highlighted.set(true);
//                walkingHumanProgressAsciiArt.setForeground(Color.BLUE);
//            }

            if (timeRemains.getHour() <= 0 && timeRemains.getMinute() <= 0) {
                Toaster toasterManager = new Toaster();
                toasterManager.setDisplayTime(30000);
                toasterManager.showToaster(
                        "Congratulation :-) It is the time to go home.");
                walkingHumanProgressAsciiArt.printPercentToAscii(done, timeRemains.getHour(), timeRemains.getMinute(), done,totalSecondsRemainsDouble, endTime);
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
                walkingHumanProgressAsciiArt.printPercentToAscii(done, timeRemains.getHour(), timeRemains.getMinute(), done,totalSecondsRemainsDouble, endTime);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }

            walkingHumanProgressAsciiArt.setForeground(
                    visibility.isStronglyColored() || walkingHumanProgressAsciiArt
                            .getClientProperty("mouseEntered").equals("true") ?
                            Color.BLACK : Color.LIGHT_GRAY);
        }
        window.setVisible(false);
        window.dispose();
    }

    private String createWindowTitle() {
        return "Time Calc " + Utils.getVersion();
    }

}
