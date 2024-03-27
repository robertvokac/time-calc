package org.nanoboot.utils.timecalc.utils.common;

import java.io.File;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class FileConstants {

    public static final File TC_DIRECTORY = new File(".tc");
    public static final File TIME_CALC_PROFILES_TXT_FILE
            = new File(TC_DIRECTORY, "time-calc-profiles.txt");
    public static final File TIME_CALC_CURRENT_PROFILE_TXT_FILE
            = new File(TC_DIRECTORY, "time-calc-current-profile.txt");
    public static final File FILE_WITHOUT_ANY_PROFILE
            = new File(TC_DIRECTORY, "timecalc.conf");
    public static final File JOKES_TXT = new File(TC_DIRECTORY, "time-calc-jokes.txt");
    public static final File DB_FILE =  new File(FileConstants.TC_DIRECTORY.getAbsolutePath() + "/" + "time-calc.sqlite3");
    public static final File CLIMATE_TXT = new File(TC_DIRECTORY, "climate.txt");
    public static final File WEATHER_CSV = new File(TC_DIRECTORY, "weather.csv");
    public static final File BASIC_TXT = new File(TC_DIRECTORY, "basic.txt");
    private FileConstants() {
        //Not meant to be instantiated.
    }
}
