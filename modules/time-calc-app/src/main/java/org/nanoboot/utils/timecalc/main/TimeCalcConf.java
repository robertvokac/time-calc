package org.nanoboot.utils.timecalc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author pc00289
 * @since 20.02.2024
 */
public class TimeCalcConf {
    private static final String CLOCK_HANDS_LONG = "clock.hands.long";
    private static final String JOKE_VISIBLE = "jokes.visible";
    private static final String BATTERY_WAVES_ENABLED = "battery.waves.enabled";
    private static final String EVERYTHING_HIDDEN = "everything-hidden";
    private static final String TOASTS_ENABLED = "toasts.enabled";

    private static TimeCalcConf INSTANCE;
    private final Properties properties = new Properties();

    private TimeCalcConf() {
        if (!new File("timecalc.conf").exists()) {
            //nothing to do;
            return;
        }
        try {
            this.properties.load(new FileInputStream("timecalc.conf"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static TimeCalcConf getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeCalcConf();
        }
        return INSTANCE;
    }

    public boolean areClockHandsLong() {
        return getBooleanProperty(CLOCK_HANDS_LONG, true);
    }

    public boolean isJokeVisible() {
        return getBooleanProperty(JOKE_VISIBLE, true);
    }

    public boolean areBatteryWavesEnabled() {
        return getBooleanProperty(BATTERY_WAVES_ENABLED, true);
    }

    public boolean isEverythingHidden() {
        return getBooleanProperty(EVERYTHING_HIDDEN, false);
    }

    public boolean areToastsEnabled() {
        return getBooleanProperty(TOASTS_ENABLED, true);
    }

    private boolean getBooleanProperty(String key, boolean defaultValue) {
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return properties.get(key).equals("true");
    }

}
