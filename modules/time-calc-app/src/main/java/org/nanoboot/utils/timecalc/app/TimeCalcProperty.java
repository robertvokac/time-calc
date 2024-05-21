package org.nanoboot.utils.timecalc.app;

import lombok.Getter;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public enum TimeCalcProperty {
    VISIBILITY_DEFAULT("visibility.default", "Default Visibility"),
    VISIBILITY_SUPPORTED_COLORED("visibility.supported.colored",
            "Visibility : Supported : Colored"),
    //
    CLOCK_VISIBLE("clock.visible", "Clock"),
    CLOCK_HANDS_LONG_VISIBLE("clock.hands.long.visible", "Clock : Long Hands"),
    CLOCK_HANDS_COLORED("clock.hands.colored", "Clock : Colored Hands"),
    CLOCK_HANDS_HOUR_VISIBLE("clock.hands.hour.visible", "Clock : Hour hand"),
    CLOCK_HANDS_MINUTE_VISIBLE("clock.hands.minute.visible",
            "Clock : Minute hand"),
    CLOCK_HANDS_SECOND_VISIBLE("clock.hands.second.visible",
            "Clock : Second hand"),
    CLOCK_HANDS_MILLISECOND_VISIBLE("clock.hands.millisecond.visible",
            "Clock : Millisecond hand"),
    CLOCK_BORDER_VISIBLE("clock.border.visible", "Clock : Border"),
    CLOCK_BORDER_ONLY_HOURS("clock.border.only-hours",
            "Clock : Border : Only hours"),
    CLOCK_NUMBERS_VISIBLE("clock.numbers.visible", "Clock : Numbers"),
    CLOCK_CIRCLE_VISIBLE("clock.circle.visible", "Clock : Circle"),
    CLOCK_CIRCLE_STRONG_BORDER("clock.circle.strong-border",
            "Clock : Circle : Strong border"),
    CLOCK_CIRCLE_BORDER_COLOR("clock.circle.border-color",
            "Clock : Circle : Border color",
            Color.class),
    CLOCK_CENTRE_CIRCLE_VISIBLE("clock.centre-circle.visible",
            "Clock : Centre circle"),
    CLOCK_CENTRE_CIRCLE_COLORED("clock.centre-circle.colored",
            "Clock : Centre Circle is colored"),
    CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER(
            "clock.progress.visible-only-if-mouse-moving-over",
            "Clock : Progress visible only, if mouse moving over"),
    CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER(
            "clock.date.visible-only-if-mouse-moving-over",
            "Clock : Date visible only, if mouse moving over"),
    CLOCK_SMILEY_VISIBLE("clock.smiley.visible","Clock : Smiley : Visible"),
    CLOCK_PERCENT_PROGRESS_VISIBLE("clock.percent-progress.visible","Clock : Percent progress : Visible"),
    CLOCK_CIRCLE_PROGRESS_VISIBLE("clock.circle-progress.visible", "Clock : Circle Progress"),
    CLOCK_HIDDEN("clock.hidden", "Clock : Hidden"),
    //
    BATTERY_WAVES_VISIBLE("battery.waves.visible", "Battery : Waves"),
    BATTERY_CIRCLE_PROGRESS_VISIBLE("battery.circle-progress.visible",
            "Battery : Circle Progress"),
    BATTERY_PERCENT_PROGRESS_VISIBLE("battery.percent-progress.visible",
            "Battery : Percent Progress"),
    BATTERY_CHARGING_CHARACTER_VISIBLE("battery.charging-character.visible",
            "Battery : Charging Character"),
    BATTERY_NAME_VISIBLE("battery.name.visible", "Battery : Name"),
    BATTERY_LABEL_VISIBLE("battery.label.visible", "Battery : Label"),
    BATTERY_VISIBLE("battery.visible", "Battery"),
    BATTERY_MINUTE_VISIBLE("battery.minute.visible", "Battery : Minute"),
    BATTERY_HOUR_VISIBLE("battery.hour.visible", "Battery : Hour"),
    BATTERY_DAY_VISIBLE("battery.day.visible", "Battery : Day"),
    BATTERY_WEEK_VISIBLE("battery.week.visible", "Battery : Week"),
    BATTERY_MONTH_VISIBLE("battery.month.visible", "Battery : Month"),
    BATTERY_YEAR_VISIBLE("battery.year.visible", "Battery : Year"),
    BATTERY_MINUTE_HIDDEN("battery.minute.hidden", "Battery : Minute : Hidden"),
    BATTERY_HOUR_HIDDEN("battery.hour.hidden", "Battery : Hour : Hidden"),
    BATTERY_DAY_HIDDEN("battery.day.hidden", "Battery : Day : Hidden"),
    BATTERY_WEEK_HIDDEN("battery.week.hidden", "Battery : Week : Hidden"),
    BATTERY_MONTH_HIDDEN("battery.month.hidden", "Battery : Month : Hidden"),
    BATTERY_YEAR_HIDDEN("battery.year.hidden", "Battery : Year : Hidden"),
    BATTERY_BLINKING_IF_CRITICAL_LOW("battery.blinking-if-critical-low",
            "Battery : Blinking, if critical low"),
    BATTERY_QUARTER_ICON_VISIBLE("battery.quarter-icon.visible", "Battery : Quarter icon"),
    JOKES_VISIBLE("jokes.visible", "Jokes"),
    COMMANDS_VISIBLE("commands.visible", "Commands"),
    NOTIFICATIONS_VISIBLE("notifications.visible", "Notifications"),
    SMILEYS_VISIBLE("smileys.visible", "Smileys"),
    SMILEYS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER(
            "smileys.visible-only-if-mouse-moving-over",
            "Smileys : Visible only, if mouse moving over"),
    SMILEYS_COLORED("smileys.colored", "Smileys : Colored"),
    SQUARE_VISIBLE("square.visible", "Square"),
    SQUARE_TYPE("square.type", "Square : Type"),
    SQUARE_HIDDEN("square.hidden", "Square : Hidden"),
    DOT_VISIBLE("dot.visible", "Dot"),
    DOT_TYPE("dot.type", "Dot : Type"),
    DOT_HIDDEN("dot.hidden", "Dot : Hidden"),
    FUEL_VISIBLE("fuel.visible", "Fuel"),
    FUEL_TYPE("fuel.type", "Fuel : Type"),
    FUEL_HIDDEN("fuel.hidden", "Fuel : Hidden"),
    ROTATION_VISIBLE("rotation.visible", "Rotation"),
    ROTATION_TYPE("rotation.type", "Rotation : Type"),
    ROTATION_HIDDEN("rotation.hidden", "Rotation : Hidden"),
    FUEL_ICON_VISIBLE("fuel.icon.visible", "Fuel : Icon"),
    CIRCLE_VISIBLE("circle.visible", "Circle"),
    CIRCLE_TYPE("circle.type", "Circle : Type"),
    CIRCLE_HIDDEN("circle.hidden", "Circle : Hidden"),
    CIRCLE_INNER_CIRCLE_VISIBLE("circle.inner-circle.visible", "Circle : Inner circle"),
    CIRCLE_OUTER_CIRCLE_ONLY_BORDER("circle.outer-circle.only-border", "Circle : Outer circle : Only border"),
    //
    BAR_VISIBLE("bar.visible", "Bar"),
    BAR_TYPE("bar.type", "Bar : Type"),
    BAR_HIDDEN("bar.hidden", "Bar : Hidden"),
    BAR_HEIGHT("bar.height", "Bar : Height", Integer.class),
    //
    WATER_VISIBLE("water.visible", "Water"),
    WATER_TYPE("water.type", "Water : Type"),
    WATER_HIDDEN("water.hidden", "Water : Hidden"),
    WATER_COLORED("water.colored", "Water : Colored"),
    //
    COLOR_VISIBLE("color.visible", "Color"),
    COLOR_TYPE("color.type", "Color : Type"),
    COLOR_HIDDEN("color.hidden", "Color : Hidden"),
    COLOR_HEIGHT("color.height", "Color : Height", Integer.class),
    //
    WALKING_HUMAN_VISIBLE("walking-human.visible", "Walking Human"),
    WALKING_HUMAN_TYPE("walking-human.type", "Walking Human : Type"),
    WALKING_HUMAN_HIDDEN("walking-human.hidden", "Walking Human : Hidden"),
    SWING_VISIBLE("swing.visible", "Swing"),
    SWING_TYPE("swing.type", "Swing : Type"),
    SWING_HIDDEN("swing.hidden", "Swing : Hidden"),
    SWING_QUARTER_ICON_VISIBLE("swing.quarter-icon.visible", "Swing: Quarter icon"),
    LIFE_VISIBLE("life.visible", "Life"),
    LIFE_TYPE("life.type", "Life : Type"),
    LIFE_HIDDEN("life.hidden", "Life : Hidden"),
    LIFE_BIRTH_DATE("life.birth-date", "Life : Birth date"),
    MONEY_VISIBLE("money.visible", "Money"),
    MONEY_TYPE("money.type", "Money : Type"),
    MONEY_HIDDEN("money.hidden", "Money : Hidden"),
    MONEY_PER_MONTH("money.per-month", "Money : Per month", Integer.class),
    MONEY_CURRENCY("money.currency", "Money : Currency", String.class),
    WEATHER_VISIBLE("weather.visible", "Weather"),
    WEATHER_HIDDEN("weather.hidden", "Weather : Hidden"),
    MAIN_WINDOW_CUSTOM_TITLE("main-window.custom-title","Main Window : Custom Title"),
    PROFILE_NAME("profile.name", "Profile : Name"),
    TEST_ENABLED("test.enabled", "Test : Enabled", Boolean.class),
    TEST_CLOCK_CUSTOM_YEAR("test.clock.custom.year", "Test : Clock : Custom : Year", Integer.class),
    TEST_CLOCK_CUSTOM_MONTH("test.clock.custom.month", "Test : Clock : Custom : Month", Integer.class),
    TEST_CLOCK_CUSTOM_DAY("test.clock.custom.day", "Test : Clock : Custom : Day", Integer.class),
    TEST_CLOCK_CUSTOM_HOUR("test.clock.custom.hour", "Test : Clock : Custom : Hour", Integer.class),
    TEST_CLOCK_CUSTOM_MINUTE("test.clock.custom.minute", "Test : Clock : Custom : Minute", Integer.class),
    TEST_CLOCK_CUSTOM_SECOND("test.clock.custom.second", "Test : Clock : Custom : Second", Integer.class),
    TEST_CLOCK_CUSTOM_MILLISECOND("test.clock.custom.millisecond", "Test : Clock : Custom : Millisecond", Integer.class),
    ACTIVITY_NEEDED_FLAGS("activity.needed-flags", "Activity : Needed flags", String.class),
    SPEED("speed", "Speed", Integer.class),
    SPEED_NEGATIVE("speed.negative", "Speed : Negative", Integer.class),
    SPEED_FLOATING("speed.floating", "Speed : Floating"),
    TYPE_VISIBLE("type.visible", "Type : Visible");

    @Getter
    private final String key;
    @Getter
    private final String description;
    @Getter
    private final Class clazz;

    static {
        Set<String> uniqueKeys = new HashSet<>();
        for(TimeCalcProperty tcp:TimeCalcProperty.values()) {
            if(uniqueKeys.contains(tcp.getKey())) {
                String msg = "Fatal exception: TimeCalcProperty key must be unique: " + tcp.getKey();
                JOptionPane.showMessageDialog(null, msg);
                throw new TimeCalcException(msg);
            } else {
                uniqueKeys.add(tcp.getKey());
            }
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

    public static TimeCalcProperty forKey(String key) {
        Optional<TimeCalcProperty> timeCalcPropertyOptional
                = Arrays.stream(values()).filter(t -> t.getKey().equals(key))
                        .findFirst();
        if (timeCalcPropertyOptional.isPresent()) {
            return timeCalcPropertyOptional.get();
        } else {
            TimeCalcException e
                    = new TimeCalcException("There is no key: " + key);
            e.printStackTrace();
            throw e;
        }
    }

}
