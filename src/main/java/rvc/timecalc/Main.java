package rvc.timecalc;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Robert
 * @since 31.01.2024
 */
public class Main {
    public static void main(String[] args) {
        while(true) {run(args);
            System.out.println(("Do you want to continue (y/n)?"));
            Scanner scanner = new Scanner(System.in);
            if(!scanner.nextLine().equals("y")) {
                System.out.println("Exiting \"Time Calc\".");
                break;
            }
        }

    }
        public static void run(String[] args) {
        System.out.println("*** Time Calc ***");
        System.out.println("-----------------\n");
        int EXPECTED_ARG_COUNT = 2;
        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            args = new String[2];
            System.out.print("Arguments: ");
            String returned = scanner.nextLine();
            args[0] = returned.split(" ")[0];
            if (returned.contains(" ")) {
                args[1] = returned.split(" ")[1];
            } else {
                args[1] = "0:00";
            }
        }
        if(args.length == 1) {
            args = new String[]{args[0], "0:00"};
        }
        if (args.length < EXPECTED_ARG_COUNT) {
            throw new RuntimeException(
                    "Expected arg count is " + EXPECTED_ARG_COUNT
                    + ", but count of found args is: " + args.length);
        }
        String startTime = args[0];
        String overtimeUsed = args[1];
        int startHour = Integer.valueOf(startTime.split(":")[0]);
        int startMinute = Integer.valueOf(startTime.split(":")[1]);
        //        System.out.println("startHour=" + startHour);
        //        System.out.println("startMinute=" + startMinute);
        int overtimeUsedHour = Integer.valueOf(overtimeUsed.split(":")[0]);
        int overtimeUsedMinute = Integer.valueOf(overtimeUsed.split(":")[1]);
        //        System.out.println("overtimeUsedHour=" + overtimeUsedHour);
        //        System.out.println("overtimeUsedMinute=" + overtimeUsedMinute);

        int endHour = startHour + 8 - overtimeUsedHour;
        int endMinute = startMinute + 30 - overtimeUsedMinute;
        while (endMinute >= 60) {
            endMinute = endMinute - 60;
            endHour = endHour + 1;
        }
        int totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
//        System.out.println("totalMinutes=" + totalMinutes);
        //        System.out.println("endHour=" + endHour);
        //        System.out.println("endMinute=" + endMinute);
        System.out.println();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        Set<String> alreadyShownTimes = new HashSet<>();
        NumberFormat formatter = new DecimalFormat("#0.00");

        while (true) {
            LocalDateTime now = LocalDateTime.now();
            String nowString = dtf.format(now);
            if (alreadyShownTimes.contains(nowString)) {
                //nothing to do
                continue;
            } else {
                alreadyShownTimes.add(nowString);
            }
            int hourNow = Integer.parseInt(nowString.split(":")[0]);
            int minuteNow = Integer.parseInt(nowString.split(":")[1]);

            //            System.out.println("hourNow=" + hourNow);
            //            System.out.println("minuteNow=" + minuteNow);
            int hourRemains = endHour - hourNow;
            int minuteRemains = endMinute - minuteNow;
            if (minuteRemains < 0) {
                minuteRemains = minuteRemains + 60;
                hourRemains = hourRemains - 1;
            }
            int hourDone = 8 - overtimeUsedHour - hourRemains;
            int minutesDone = 30 - overtimeUsedMinute - minuteRemains;
            int totalMinutesDone = hourDone * 60 + minutesDone;
            double done = ((double)totalMinutesDone)/((double)totalMinutes);
//            System.out.println("hourDone=" + hourDone);
//            System.out.println("minutesDone=" + minutesDone);
//            System.out.println("totalMinutesDone=" + totalMinutesDone);
            System.out.println("Progress: " + formatter.format(done * 100) + "% Remains " + String.format("%02d", hourRemains) + ":" + String
                    .format("%02d", minuteRemains) + " until end " + String
                                       .format("%02d", endHour) + ":" + String
                                       .format("%02d", endMinute));
            if(System.getProperty("progress")!=null) {
                printPercentToAscii(done);
            }
            if(hourRemains <= 0 && minuteRemains <= 0) {
                break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
        }
    }
    private static void printPercentToAscii(double percent) {
        NumberFormat formatter = new DecimalFormat("#00.00");
        String s = formatter.format(percent * 100);
        s = s.replace(",","");

        int percentInt = (int)(percent * 100);
//        System.out.println("percentInt=" + percentInt);
        int i1 = percentInt > 20 ? 10 : (int) (percentInt / 2);
        int i2 = percentInt > 40 ? 10 : (int) ((percentInt - 20) / 2);
        int i3 = percentInt > 60 ? 10 : (int) ((percentInt - 40) / 2);
        int i4 = percentInt > 80 ? 10 : (int) ((percentInt - 60) / 2);
        int i5 = (int) ((percentInt - 80) / 2);
        int[] array = new int[]{i1,i2,i3,i4,i5};

//        System.out.println(i1 +" " + i2 + " " + i3 + " " + i4);


        for(int i:array) {
            if(i < 0) {
                i = 0;
            }
            for(int j = 1; j <= i; j++) {
                System.out.print("#");
            }
            for(int j = 10; j > i; j--) {
                System.out.print(".");
            }
            System.out.println();

        }

        System.out.println("\n\n");

    }
}

