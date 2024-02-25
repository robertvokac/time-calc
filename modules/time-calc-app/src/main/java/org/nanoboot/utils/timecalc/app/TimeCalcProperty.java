package org.nanoboot.utils.timecalc.app;

import lombok.Getter;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public enum TimeCalcProperty {
    VISIBILITY_DEFAULT("visibility.default", "Default Visibility"),
    VISIBILITY_SUPPORTED_COLORED("visibility.supported.colored", "Visibility : Supported : Colored"),
    //
    CLOCK_HANDS_LONG_VISIBLE("clock.hands.long.visible", "Clock : Hands are long"),
    CLOCK_HANDS_COLORED("clock.hands.colored", "Clock : Hands are colored"),
    CLOCK_HANDS_MINUTE_VISIBLE("clock.hands.minute.visible", "Clock : Minute hand"),
    CLOCK_HANDS_SECOND_VISIBLE("clock.hands.second.visible", "Clock : Second hand"),
    CLOCK_HANDS_MILLISECOND_VISIBLE("clock.hands.millisecond.visible", "Clock : Millisecond hand"),
    CLOCK_BORDER_VISIBLE("clock.border.visible", "Clock : Border"),
    CLOCK_BORDER_ONLY_HOURS("clock.border.only-hours", "Clock : Border : Only hours"),
    CLOCK_NUMBERS_VISIBLE("clock.numbers.visible", "Clock : Numbers"),
    CLOCK_CIRCLE_VISIBLE("clock.circle.visible", "Clock : Circle"),
    CLOCK_CIRCLE_STRONG_BORDER("clock.circle.strong-border", "Clock : Circle : Strong border"),
    CLOCK_CIRCLE_BORDER_COLOR("clock.circle.border-color", "Clock : Circle : Border color",
            Color.class),
    CLOCK_CENTRE_CIRCLE_VISIBLE("clock.centre-circle.visible", "Clock : Centre circle"),
    CLOCK_CENTRE_CIRCLE_BLACK("clock.centre-circle.black", "Clock : Centre Circle is black"),
    CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER("clock.progress.visible-only-if-mouse-moving-over", "Clock : Progress visible only, if mouse moving over"),
    CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER("clock.date.visible-only-if-mouse-moving-over", "Clock : Date visible only, if mouse moving over"),
    
    //
    BATTERY_WAVES_VISIBLE("battery.waves.visible", "Battery : Waves"),
    BATTERY_CIRCLE_PROGRESS_VISIBLE("battery.circle-progress.visible", "Battery : Circle Progress"),
    BATTERY_PERCENT_PROGRESS_VISIBLE("battery.percent-progress.visible", "Battery : Percent Progress"),
    
    BATTERY_CHARGING_CHARACTER_VISIBLE("battery.charging-character.visible", "Battery : Charging Character"),
    BATTERY_NAME_VISIBLE("battery.name.visible", "Battery : Name"),
    BATTERY_LABEL_VISIBLE("battery.label.visible", "Battery : Label"),
    
    JOKES_VISIBLE("jokes.visible", "Jokes"),
    COMMANDS_VISIBLE("commands.visible", "Commands"),
    NOTIFICATIONS_VISIBLE("notifications.visible", "Notifications"),
    SMILEYS_COLORED("smileys.colored", "Smileys : Colored"),
    SMILEYS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER("smileys.visible-only-if-mouse-moving-over", "Smileys : Visible only, if mouse moving over"),
    
    SQUARE_VISIBLE("square.visible", "Square");

    @Getter
    private final String key;
    @Getter
    private final String description;
    @Getter
    private final Class clazz;

    public static TimeCalcProperty forKey(String key) {
        Optional<TimeCalcProperty> timeCalcPropertyOptional = Arrays.stream(values()).filter(t -> t.getKey().equals(key)).findFirst();
        if (timeCalcPropertyOptional.isPresent()) {
            return timeCalcPropertyOptional.get();
        } else {
            TimeCalcException e
                    = new TimeCalcException("There is no key: " + key);
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