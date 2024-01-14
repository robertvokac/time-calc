package rvc.timecalc;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Robert
 * @since 08.02.2024
 */
public class TimeCalc {
    private static final String DEFAULT_OVERTIME = "0:00";
    private static final int WORKING_HOURS_LENGTH = 8;
    private static final int WORKING_MINUTES_LENGTH = 30;
    public static final String WALL = "||";
    private static final String NEW_LINE = "\n";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private final String startTime;
    private final String overTime;
    private final int startHour;
    private final int startMinute;
    private final int overtimeHour;
    private final int overtimeMinute;
    private final int totalMinutes;
    private int endHour;
    private int endMinute;
    private final Set<String> alreadyShownTimes = new HashSet<>();
    private boolean stopBeforeEnd = false;

    public TimeCalc(String startTimeIn, String overTimeIn) {
        this.startTime = startTimeIn;
        this.overTime = (overTimeIn == null || overTimeIn.isEmpty()) ? DEFAULT_OVERTIME : overTimeIn;

        this.startHour = Integer.valueOf(startTime.split(":")[0]);
        this.startMinute = Integer.valueOf(startTime.split(":")[1]);

        this.overtimeHour = Integer.valueOf(overTime.split(":")[0]);
        this.overtimeMinute = Integer.valueOf(overTime.split(":")[1]);


        this.endHour = startHour + WORKING_HOURS_LENGTH - overtimeHour;
        this.endMinute = startMinute + WORKING_MINUTES_LENGTH - overtimeMinute;
        while (endMinute >= 60) {
            endMinute = endMinute - 60;
            endHour = endHour + 1;
        }
        this.totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);

        NumberFormat formatter = new DecimalFormat("#0.00");




        JFrame window=new JFrame();

        JButton restartButton=new JButton("Restart");
        JButton exitButton=new JButton("Exit");

        restartButton.setBounds(280,260,100, 30);
        exitButton.setBounds(390,260,100, 30);

        window.add(restartButton);
        window.add(exitButton);
        JTextPane text = new JTextPane ();
        text.setBounds(10,10,540, 250);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        text.setForeground(Color.GRAY);
        text.setBackground(new Color(238,238,238));
        window.add(text);

        window.setSize(550,350);
        window.setLayout(null);
        window.setVisible(true);
        window.setTitle("Time Calc");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        restartButton.addActionListener(e -> {window.setVisible(false); stopBeforeEnd = true;});


        StringBuilder sb = null;
        while (true) {
            if(stopBeforeEnd) {
                window.setVisible(false);
                window.dispose();
                break;
            }
            sb = new StringBuilder();
            LocalDateTime now = LocalDateTime.now();
            String nowString = DATE_TIME_FORMATTER.format(now);
            if (alreadyShownTimes.contains(nowString)) {
                //nothing to do
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                continue;
            } else {
                alreadyShownTimes.add(nowString);
            }
            int hourNow = Integer.parseInt(nowString.split(":")[0]);
            int minuteNow = Integer.parseInt(nowString.split(":")[1]);

            int hourRemains = endHour - hourNow;
            int minuteRemains = endMinute - minuteNow;
            if (minuteRemains < 0) {
                minuteRemains = minuteRemains + 60;
                hourRemains = hourRemains - 1;
            }
            int hourDone = WORKING_HOURS_LENGTH - overtimeHour - hourRemains;
            int minutesDone = 30 - overtimeMinute - minuteRemains;
            int totalMinutesDone = hourDone * 60 + minutesDone;
            double done = ((double)totalMinutesDone)/((double)totalMinutes);

            String msg = "Done=" + formatter.format(done * 100) + "% Remains=" + String.format("%02d", hourRemains) + ":" + String
                    .format("%02d", minuteRemains) + " (" + String.format("%03d", (hourRemains * 60 + minuteRemains)) + " minute" + ((hourRemains * 60 + minuteRemains) > 1 ? "s" : " ") + ")" + " End=" + String
                                 .format("%02d", endHour) + ":" + String
                                 .format("%02d", endMinute);

            //if(System.getProperty("progress")!=null) {
                printPercentToAscii(done, msg, sb);
//            } else {
//                sb.append(msg);
//            }
            if(hourRemains <= 0 && minuteRemains <= 0) {
                sb.append("\nCongratulation :-) It is the time to go home.");
                //System.out.println(sb.toString());
                text.setText(sb.toString());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }
                while(!stopBeforeEnd) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                }
            } else {
                //System.out.println(sb.toString());
                text.setText(sb.toString());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
        window.setVisible(false);
        window.dispose();
    }
    private static void printPercentToAscii(double percent,
            String msg, StringBuilder sb) {
        NumberFormat formatter = new DecimalFormat("#00.00");
        String s = formatter.format(percent * 100);
        s = s.replace(",","");

        int percentInt = (int)(percent * 100);
        int i1 = percentInt > 20 ? 10 : (int) (percentInt / 2);
        int i2 = percentInt > 40 ? 10 : (int) ((percentInt - 20) / 2);
        int i3 = percentInt > 60 ? 10 : (int) ((percentInt - 40) / 2);
        int i4 = percentInt > 80 ? 10 : (int) ((percentInt - 60) / 2);
        int i5 = (int) ((percentInt - 80) / 2);
        int[] array = new int[]{i1,i2,i3,i4,i5};

        int index = 0;
        for(int i:array) {

            if(i < 0) {
                i = 0;
            }
            sb.append(index == 2 ? (msg + createSpaces((percentInt < 0 ? -1 : 0) + 9 + (percentInt<10 ? 1: 0) + (percentInt==100 ? -1: 0))) : createSpaces(58));
            for(int j = 1; j <= i; j++) {
                sb.append("#");
            }
            for(int j = 10; j > i; j--) {
                sb.append(".");
            }
            sb.append("\n");
            index++;
        }

        sb.append(createSpaces(58) + "||======||\n");
        int spacesTotal = 52;
        int spacesDone = (int) (percent * 52);
        int spacesTodo = spacesTotal - (spacesDone < 0 ? 0 : spacesDone);
        sb.append(
                WALL + createSpaces(spacesDone) + " () " + createSpaces(spacesTodo) + WALL + (spacesTodo == 0 ? "  GO  " :"XXXXXX") + WALL + NEW_LINE +
                WALL + createSpaces(spacesDone) + "/||\\"  + createSpaces(spacesTodo) + WALL + (spacesTodo == 0 ? " HOME " :"XXXXXX") + WALL + NEW_LINE +
                WALL + createSpaces(spacesDone) + " /\\ "  + createSpaces(spacesTodo) + WALL + (spacesTodo == 0 ? "  !!  " :"XXXXXX") + WALL + NEW_LINE +
                "====================================================================" + NEW_LINE
        );

    }
    private static final String createSpaces(int spaceCount) {
        return create(spaceCount, ' ');
    }
    private static final String create(int count, char ch) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

}
