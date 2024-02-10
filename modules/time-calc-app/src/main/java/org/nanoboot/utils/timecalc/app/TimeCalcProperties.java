package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class TimeCalcProperties {
    private static final String CLOCK_HANDS_LONG = "clock.hands.long";
    private static final String CLOCK_HANDS_SECOND_ENABLED =
            "clock.hands.second.enabled";
    private static final String CLOCK_HANDS_MILLISECOND_ENABLED =
            "clock.hands.millisecond.enabled";
    private static final String BATTERY_WAVES_ENABLED = "battery.waves.enabled";
    private static final String DEFAULT_VISIBILITY = "default-visibility";
    private static final String VISIBILITY_ONLY_GREY_OR_NONE_ENABLED =
            "visibility.only-grey-or-none.enabled";
    private static final String JOKES_ENABLED = "jokes.enabled";
    private static final String COMMANDS_ENABLED = "commands-enabled";
    private static final String TOASTS_ENABLED = "toasts.enabled";

    private static TimeCalcProperties INSTANCE;
    private final Properties properties = new Properties();

    private TimeCalcProperties() {
        if (!new File("timecalc.conf").exists()) {
            //nothing to do;
            return;
        }
        try {
            this.properties.load(new FileInputStream("timecalc.conf"));
        } catch (IOException e) {
            System.err.println(e);
        }
        if (!isSecondEnabled() && isMillisecondEnabled()) {
            System.out.println(
                    "Sorry, seconds are disabled, millisecond must be disabled too.");
            this.properties.setProperty(
                    TimeCalcProperties.CLOCK_HANDS_MILLISECOND_ENABLED,
                    "false");
        }
    }

    public static TimeCalcProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeCalcProperties();
        }
        return INSTANCE;
    }

    public boolean areClockHandsLong() {
        return getBooleanProperty(CLOCK_HANDS_LONG, true);
    }

    public boolean isSecondEnabled() {
        return getBooleanProperty(CLOCK_HANDS_SECOND_ENABLED, true);
    }

    public boolean isMillisecondEnabled() {
        return getBooleanProperty(CLOCK_HANDS_MILLISECOND_ENABLED, false);
    }

    public boolean areJokesEnabled() {
        return getBooleanProperty(COMMANDS_ENABLED, true);
    }

    public boolean areBatteryWavesEnabled() {
        return getBooleanProperty(BATTERY_WAVES_ENABLED, true);
    }

    public boolean areToastsEnabled() {
        return getBooleanProperty(TOASTS_ENABLED, true);
    }

    public Visibility getDefaultVisibility() {
        if (!properties.containsKey(DEFAULT_VISIBILITY)) {
            return Visibility.STRONGLY_COLORED;
        }
        return Visibility.valueOf((String) properties.get(DEFAULT_VISIBILITY));
    }

    public boolean isVisibilityOnlyGreyOrNoneEnabled() {
        return getBooleanProperty(VISIBILITY_ONLY_GREY_OR_NONE_ENABLED, false);
    }

    public Boolean areCommandsEnabled() {
        return getBooleanProperty(COMMANDS_ENABLED, true);
    }

    private boolean getBooleanProperty(String key, boolean defaultValue) {
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return properties.get(key).equals("true");
    }

    public void load() {
        //to be implemented
    }

    public void save() {
        //to be implemented
    }
}
