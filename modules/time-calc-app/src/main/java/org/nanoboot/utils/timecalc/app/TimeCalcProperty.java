package org.nanoboot.utils.timecalc.app;

import lombok.Getter;
import org.nanoboot.utils.timecalc.utils.common.Utils;

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
    CLOCK_CENTRE_CIRCLE_BLACK("clock.centre-circle.black",
            "Clock : Centre Circle is black"),
    CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER(
            "clock.progress.visible-only-if-mouse-moving-over",
            "Clock : Progress visible only, if mouse moving over"),
    CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER(
            "clock.date.visible-only-if-mouse-moving-over",
            "Clock : Date visible only, if mouse moving over"),
    CLOCK_SMILEY_VISIBLE("clock.smiley.visible","Clock : Smiley : Visible"),
    CLOCK_PERCENT_PROGRESS_VISIBLE("clock.percent-progress.visible","Clock : Percent progress : Visible"),
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
    CIRCLE_VISIBLE("circle.visible", "Circle"),
    CIRCLE_TYPE("circle.type", "Circle : Type"),
    WALKING_HUMAN_VISIBLE("walking-human.visible", "Walking Human"),
    WALKING_HUMAN_TYPE("walking-human.type", "Walking Human : Type"),
    SWING_VISIBLE("swing.visible", "Swing"),
    SWING_TYPE("swing.type", "Swing : Type"),
    SWING_QUARTER_ICON_VISIBLE("swing.quarter-icon.visible", "Swing: Quarter icon"),
    LIFE_VISIBLE("life.visible", "Life"),
    LIFE_TYPE("life.type", "Life : Type"),
    LIFE_BIRTH_DATE("life.birth-date", "Life : Birth date"),
    MONEY_VISIBLE("money.visible", "Money"),
    MONEY_TYPE("money.type", "Money : Type"),
    MONEY_PER_MONTH("money.per-month", "Money : Per month", Integer.class),
    MONEY_CURRENCY("money.currency", "Money : Currency", String.class),
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
    ACTIVITY_NEEDED_FLAGS("activity.needed-flags", "Activity : Needed flags", String.class);

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
