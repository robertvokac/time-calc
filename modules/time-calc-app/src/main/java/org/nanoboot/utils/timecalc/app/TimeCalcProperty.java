package org.nanoboot.utils.timecalc.app;

import lombok.Getter;
import org.nanoboot.utils.timecalc.entity.Visibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public enum TimeCalcProperty {
    VISIBILITY_DEFAULT("visibility.default", "Default Visibility"),
    VISIBILITY_SUPPORTED_COLORED("visibility.supported.colored", "Visibility : Supported : Colored"),
    //
    CLOCK_HANDS_LONG_VISIBLE("clock.hands.long.visible", "Clock : Hands are long"),
    CLOCK_HANDS_MINUTE_VISIBLE("clock.hands.minute.visible", "Clock : Minute hand"),
    CLOCK_HANDS_SECOND_VISIBLE("clock.hands.second.visible", "Clock : Second hand"),
    CLOCK_HANDS_MILLISECOND_VISIBLE("clock.hands.millisecond.visible", "Clock : Millisecond hand"),
    //
    BATTERY_WAVES_VISIBLE("battery.waves.visible", "Battery : Waves"),
    JOKES_VISIBLE("jokes.visible", "Jokes"),
    COMMANDS_VISIBLE("commands.visible", "Commands"),
    NOTIFICATIONS_VISIBLE("notifications.visible", "Notifications"),
    SMILEYS_COLORED("smileys.colored", "Smileys : Colored");

    @Getter
    private final String key;
    @Getter
    private final String description;
    @Getter
    private final Class clazz;

    public static TimeCalcProperty forKey(String key) {
        Optional<TimeCalcProperty>
                timeCalcPropertyOptional = Arrays.stream(values()).filter(t -> t.getKey().equals(key)).findFirst();
        if(timeCalcPropertyOptional.isPresent()) {
            return timeCalcPropertyOptional.get();
        } else {
            TimeCalcException e =
                    new TimeCalcException("There is no key: " + key);
            e.printStackTrace();
            throw e;
        }
    }

    TimeCalcProperty(String key, String description, Class clazz) {
        this.key = key;
        this.description = description;
        this.clazz = clazz;
    }
    TimeCalcProperty(String key, String description) {
        this(key, description, Boolean.class);
    }

}
