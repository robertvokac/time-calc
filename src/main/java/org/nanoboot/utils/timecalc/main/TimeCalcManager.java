package org.nanoboot.utils.timecalc.main;

import org.nanoboot.utils.timecalc.gui.common.ComponentRegistry;
import org.nanoboot.utils.timecalc.gui.common.TimeCalcButton;
import org.nanoboot.utils.timecalc.gui.common.TimeCalcWindow;
import org.nanoboot.utils.timecalc.gui.common.Toaster;
import org.nanoboot.utils.timecalc.gui.common.WeatherWindow;
import org.nanoboot.utils.timecalc.gui.progress.AnalogClock;
import org.nanoboot.utils.timecalc.gui.progress.Battery;
import org.nanoboot.utils.timecalc.gui.progress.DayBattery;
import org.nanoboot.utils.timecalc.gui.progress.HourBattery;
import org.nanoboot.utils.timecalc.gui.progress.MonthBattery;
import org.nanoboot.utils.timecalc.gui.progress.ProgressCircle;
import org.nanoboot.utils.timecalc.gui.progress.ProgressSquare;
import org.nanoboot.utils.timecalc.gui.progress.WalkingHumanProgressAsciiArt;
import org.nanoboot.utils.timecalc.gui.progress.WeekBattery;
import org.nanoboot.utils.timecalc.utils.Constants;
import org.nanoboot.utils.timecalc.utils.DateFormats;
import org.nanoboot.utils.timecalc.utils.Jokes;
import org.nanoboot.utils.timecalc.utils.TimeHM;
import org.nanoboot.utils.timecalc.utils.Utils;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.nanoboot.utils.timecalc.utils.FileConstants.FOCUS_TXT;

/**
 * @author pc00289
 * @since 08.02.2024
 */
public class TimeCalcManager {
    private static final int MARGIN = 10;

    private final String windowTitle;
    private final int totalMinutes;

    private final TimeHM startTime;
    private final TimeHM overtime;
    private final TimeHM endTime;
    private boolean stopBeforeEnd = false;
    private boolean vtipyShown = false;

    public TimeCalcManager(String startTimeIn, String overTimeIn) {
        Utils.everythingHidden
                .set(TimeCalcConf.getInstance().isEverythingHidden());
        Utils.toastsAreEnabled
                .set(TimeCalcConf.getInstance().areToastsEnabled());

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

        TimeCalcButton focusButton = new TimeCalcButton("Focus");
        TimeCalcButton commandButton = new TimeCalcButton("Command");
        TimeCalcButton weatherButton = new TimeCalcButton("Weather");
        TimeCalcButton jokeButton = new TimeCalcButton("Joke");
        TimeCalcButton restartButton = new TimeCalcButton("Restart");
        TimeCalcButton exitButton = new TimeCalcButton("Exit");
        AboutButton aboutButton = new AboutButton();

        //window.add(weatherButton);
        window.addAll(commandButton, focusButton, jokeButton, restartButton,
                exitButton);
        window.addKeyListener(new KeyAdapter() {
            // Key Pressed method
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    Utils.everythingHidden.set(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    Utils.everythingHidden.set(true);
                }

                if (e.getKeyCode() == KeyEvent.VK_G) {
                    Utils.ultraLight.flip();
                }

                if (e.getKeyCode() == KeyEvent.VK_C) {
                    Utils.highlighted.flip();
                }
                if (e.getKeyCode() == KeyEvent.VK_V) {
                    Utils.everythingHidden.flip();
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

        focusButton.setBounds(
                exitButton.getX() + 2 * MARGIN + exitButton.getWidth() + 20,
                exitButton.getY(), 80, aboutButton.getHeight());

        window.setSize(520 + 20 + 100, 580 + MARGIN + aboutButton.getHeight());
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
                            Utils.highlighted
                                    .set(commandsAsArray[1].equals("1"));
                            break;
                        case "gray":
                            Utils.ultraLight
                                    .set(commandsAsArray[1].equals("1"));
                            break;
                        case "waves":
                            Battery.wavesOff = commandsAsArray[1].equals("0");
                            break;
                        case "uptime":
                            JOptionPane.showMessageDialog(null,
                                    Utils.getCountOfMinutesSinceAppStarted()
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
                                    .set(commandsAsArray[1].equals("1"));
                            break;
                        default:
                            JOptionPane.showMessageDialog(null,
                                    "Unknown command: " + commandsAsArray[0]);
                    }
                });

        focusButton.addActionListener(e -> {
            window.requestFocus();
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

        AnalogClock analogClock = new AnalogClock();
        analogClock.setBounds(MARGIN, MARGIN, 200);
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

        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date());

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
                focusButton,
                commandButton,
                restartButton,
                exitButton
        );
        window.setSize(window.getWidth(), exitButton.getY() + 3 * exitButton.getHeight() + MARGIN);
        while (true) {
            if (stopBeforeEnd) {
                window.setVisible(false);
                window.dispose();
                break;
            }

            if (Math.random() > 0.9) {
                if (FOCUS_TXT.exists()) {
                    window.requestFocus();
                    FOCUS_TXT.delete();
                }
            }
            if (Utils.highlighted.get()) {
                Utils.ultraLight.set(false);
            }

            componentRegistry.setVisible(!Utils.everythingHidden.get());
            jokeButton.setVisible(
                    TimeCalcConf.getInstance().isJokeVisible()
                    && !Utils.everythingHidden.get());

            window.setTitle(Utils.everythingHidden.get() ? "" : windowTitle);

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

            if (timeRemains.getHour() == 0 && timeRemains.getMinute() == 1 && !vtipyShown) {
                vtipyShown = true;
                Jokes.showRandom();
            }
            if (timeRemains.getHour() == 0 && timeRemains.getMinute() <= 3) {
                Utils.highlighted.set(true);
                walkingHumanProgressAsciiArt.setForeground(Color.BLUE);
            }

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
                    Utils.highlighted.get() || walkingHumanProgressAsciiArt
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
