package rvc.timecalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Robert
 * @since 20.02.2024
 */
public class TimeCalcConf {
    private static final String CLOCK_COLORFUL = "clock.colorful";
    private static final String CLOCK_HANDS_LONG = "clock.hands.long";
    private static final String JOKE_VISIBLE = "jokes.visible";
    private static final String BATTERY_WAVES_ENABLED = "battery.waves.enabled";

    private static TimeCalcConf INSTANCE;
    private Properties properties = new Properties();
    public static TimeCalcConf getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TimeCalcConf();
        }
        return INSTANCE;
    }
    private TimeCalcConf() {
        if(!new File("timecalc.conf").exists()) {
            //nothing to do;
            return;
        }
        try {
            this.properties.load(new FileInputStream("timecalc.conf"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public boolean isClockColorful() {
        if(!properties.containsKey(CLOCK_COLORFUL)) {
            return false;
        }
        return properties.get(CLOCK_COLORFUL).equals("true");
    }

    public boolean areClockHandsLong() {
        if(!properties.containsKey(CLOCK_HANDS_LONG)) {
            return true;
        }
        return properties.get(CLOCK_HANDS_LONG).equals("true");
    }
    public boolean isJokeVisible() {
        if(!properties.containsKey(JOKE_VISIBLE)) {
            return true;
        }
        return properties.get(JOKE_VISIBLE).equals("true");
    }
    public boolean areBatteryWavesEnabled() {
        if(!properties.containsKey(BATTERY_WAVES_ENABLED)) {
            return true;
        }
        return properties.get(BATTERY_WAVES_ENABLED).equals("true");
    }

}
