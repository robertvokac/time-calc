package org.nanoboot.utils.timecalc.main;

import org.nanoboot.utils.timecalc.gui.common.ComponentRegistry;
import org.nanoboot.utils.timecalc.gui.common.TimeCalcButton;
import org.nanoboot.utils.timecalc.gui.common.TimeCalcWindow;
import org.nanoboot.utils.timecalc.gui.common.Toaster;
import org.nanoboot.utils.timecalc.gui.common.WeatherWindow;
import org.nanoboot.utils.timecalc.gui.progress.AnalogClock;
import org.nanoboot.utils.timecalc.gui.progress.Battery;
import org.nanoboot.utils.timecalc.gui.progress.ProgressCircle;
import org.nanoboot.utils.timecalc.gui.progress.ProgressSquare;
import org.nanoboot.utils.timecalc.gui.progress.WalkingHumanProgressAsciiArt;
import org.nanoboot.utils.timecalc.utils.Constants;
import org.nanoboot.utils.timecalc.utils.DateFormats;
import org.nanoboot.utils.timecalc.utils.Jokes;
import org.nanoboot.utils.timecalc.utils.NumberFormats;
import org.nanoboot.utils.timecalc.utils.TimeHoursMinutes;
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
import java.util.HashSet;
import java.util.Set;

import static org.nanoboot.utils.timecalc.utils.FileConstants.FOCUS_TXT;

/**
 * @author pc00289
 * @since 08.02.2024
 */
public class TimeCalcManager {
    private static final int MARGIN = 10;

    private final String windowTitle;
    private final int totalMinutes;

    private final TimeHoursMinutes startTime;
    private final TimeHoursMinutes overtime;
    private final TimeHoursMinutes endTime;
    private boolean stopBeforeEnd = false;
    private boolean vtipyShown = false;

    public TimeCalcManager(String startTimeIn, String overTimeIn) {
        Utils.everythingHidden
                .set(TimeCalcConf.getInstance().isEverythingHidden());
        Utils.toastsAreEnabled
                .set(TimeCalcConf.getInstance().areToastsEnabled());

        overTimeIn = (overTimeIn == null || overTimeIn.isEmpty()) ?
                Constants.DEFAULT_OVERTIME : overTimeIn;

        this.startTime = new TimeHoursMinutes(startTimeIn);
        this.overtime = new TimeHoursMinutes(overTimeIn);

        this.endTime = new TimeHoursMinutes(startTime.getHour() + Constants.WORKING_HOURS_LENGTH + overtime.getHour(),
                startTime.getMinute() + Constants.WORKING_MINUTES_LENGTH + overtime.getMinute());

        this.totalMinutes = TimeHoursMinutes.countDiffInMinutes(startTime, endTime);
        int totalSeconds = totalMinutes * TimeHoursMinutes.SECONDS_PER_MINUTE;
        int totalMilliseconds = totalSeconds * TimeHoursMinutes.MILLISECONDS_PER_SECOND;

        TimeCalcWindow window = new TimeCalcWindow();

        TimeCalcButton focusButton = new TimeCalcButton("F");
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
        walkingHumanProgressAsciiArt.setBounds(MARGIN, MARGIN + 210 + MARGIN, 500, 250);

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
                exitButton.getX() + 3 * MARGIN + exitButton.getWidth() + 20,
                MARGIN, 60, aboutButton.getHeight());

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

        Battery batteryForDay = new Battery();
        batteryForDay.setBounds(progressCircle.getBounds().x,
                progressCircle.getY() + MARGIN + progressCircle.getHeight(), 140);
        window.add(batteryForDay);

        Battery batteryForWeek = new Battery();
        batteryForWeek.setBounds(
                batteryForDay.getBounds().x + batteryForDay.getWidth(),
                batteryForDay.getY(), 140);
        window.add(batteryForWeek);

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

        //        System.out.println("workDaysDone" + workDaysDone);
        //        System.out.println("workDaysTodo" + workDaysTodo);
        //        System.out.println("currentDayOfMonth" + currentDayOfMonth);

        Battery batteryForMonth = new Battery();
        batteryForMonth.setBounds(
                batteryForDay.getBounds().x + batteryForDay.getWidth(),
                batteryForDay.getY() + batteryForWeek.getHeight() + MARGIN, 140);
        window.add(batteryForMonth);

        Battery batteryForHour = new Battery();
        batteryForHour.setBounds(batteryForMonth.getBounds().x,
                batteryForMonth.getY() + batteryForMonth.getHeight() + MARGIN, 140);
        window.add(batteryForHour);
        Rectangle hourRectangle = batteryForHour.getBounds();
        Rectangle dayRectangle = batteryForDay.getBounds();
        Rectangle weekRectangle = batteryForWeek.getBounds();
        Rectangle monthRectangle = batteryForMonth.getBounds();
        batteryForHour.setBounds(dayRectangle);
        batteryForDay.setBounds(weekRectangle);
        batteryForWeek.setBounds(monthRectangle);
        batteryForMonth.setBounds(hourRectangle);

        ComponentRegistry componentRegistry = new ComponentRegistry();
        componentRegistry.addAll(
                walkingHumanProgressAsciiArt,
                progressSquare,
                progressCircle,
                analogClock,
                batteryForDay,
                batteryForWeek,
                batteryForMonth,
                batteryForHour,
                jokeButton,
                focusButton,
                commandButton,
                restartButton,
                exitButton
        );
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
            //            if (alreadyShownTimes.contains(nowString)) {
            //                //nothing to do
            //                try {
            //                    Thread.sleep(100);
            //                } catch (InterruptedException e) {
            //
            //                }
            //                continue;
            //            } else {
            //                alreadyShownTimes.add(nowString);
            //            }
            int hourNow = Integer.parseInt(nowString.split(":")[0]);
            int minuteNow = Integer.parseInt(nowString.split(":")[1]);
            int secondNow = Integer.parseInt(nowString.split(":")[2]);
            int millisecondNow = Integer.parseInt(nowString.split(":")[3]);
            int hourRemains = endTime.getHour() - hourNow;
            int minuteRemains = endTime.getMinute() - minuteNow;
            if (minuteRemains < 0) {
                minuteRemains = minuteRemains + 60;
                hourRemains = hourRemains - 1;
            }
            int secondsRemains = 60 - secondNow;
            int millisecondsRemains = 1000 - millisecondNow;

            int hourDone = Constants.WORKING_HOURS_LENGTH + overtime.getHour() - hourRemains;
            int minutesDone = Constants.WORKING_MINUTES_LENGTH + overtime.getMinute() - minuteRemains;
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
            batteryForDay.setDonePercent(done);

            int weekDayWhenMondayIsOne = calNow.get(Calendar.DAY_OF_WEEK) - 1;
            batteryForWeek.setDonePercent((weekDayWhenMondayIsOne == 0
                                           || weekDayWhenMondayIsOne == 6) ?
                    100 : ((weekDayWhenMondayIsOne - 1) * 0.20 + done * 0.20));
            batteryForWeek.setLabel(
                    nowIsWeekend ? "5/5" : (weekDayWhenMondayIsOne + "/5"));

            batteryForMonth.setDonePercent(weekDayWhenMondayIsOne == 0
                                           || weekDayWhenMondayIsOne == 6 ?
                    workDaysDone / workDaysTotal :
                    (workDaysDone + done) / workDaysTotal);
            batteryForMonth.setLabel(
                    (nowIsWeekend ? workDaysDone : workDaysDone + 1) + "/"
                    + (workDaysTotal));

            double minutesRemainsD = minuteRemains;
            double secondsRemainsD = secondsRemains;
            double millisecondsRemainsD = millisecondsRemains;
            minutesRemainsD = minutesRemainsD + secondsRemainsD / 60d;
            minutesRemainsD =
                    minutesRemainsD + millisecondsRemainsD / 1000d / 60d;
            if (secondsRemainsD > 0) {
                minutesRemainsD = minutesRemainsD - 1d;
            }
            if (millisecondsRemainsD > 0) {
                minutesRemainsD = minutesRemainsD - 1d / 1000d;
            }
            batteryForHour.setDonePercent(
                    done >= 1 ? 1 : (1 - ((minutesRemainsD % 60d) / 60d)));
            if (!nowIsWeekend) {
                int hoursForLabel =
                        (minuteRemains == 0 ? minuteRemains / 60 + 1 :
                                minuteRemains / 60);
                batteryForHour.setLabel(
                        ((totalMinutes / 60) - hoursForLabel) + "/" + (
                                totalMinutes / 60));
            }

            int totalSecondsRemains =
                    (hourRemains * 60 * 60 + minuteRemains * 60
                     + secondsRemains);
            int totalMillisecondsRemains =
                    totalSecondsRemains * 1000 + millisecondsRemains;
            double totalSecondsRemainsDouble =
                    ((double) totalMillisecondsRemains) / 1000;


            //if(System.getProperty("progress")!=null) {



            //            } else {
            //                sb.append(msg);
            //            }
            if (hourRemains == 0 && minuteRemains == 1 && !vtipyShown) {
                vtipyShown = true;
                Jokes.showRandom();
            }
            if (hourRemains == 0 && minuteRemains <= 3) {
                Utils.highlighted.set(true);
                walkingHumanProgressAsciiArt.setForeground(Color.BLUE);
            }

            if (hourRemains <= 0 && minuteRemains <= 0) {
                Toaster toasterManager = new Toaster();
                toasterManager.setDisplayTime(30000);
                toasterManager.showToaster(
                        "Congratulation :-) It is the time to go home.");
                walkingHumanProgressAsciiArt.printPercentToAscii(done, hourRemains, minuteRemains, done,totalSecondsRemainsDouble, endTime);
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
                walkingHumanProgressAsciiArt.printPercentToAscii(done, hourRemains, minuteRemains, done,totalSecondsRemainsDouble, endTime);
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
