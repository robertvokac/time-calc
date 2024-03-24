package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class TimeCalcConfiguration {

    public final StringProperty visibilityDefaultProperty
            = new StringProperty(TimeCalcProperty.VISIBILITY_DEFAULT
                    .getKey());
    public final BooleanProperty visibilitySupportedColoredProperty
            = new BooleanProperty(TimeCalcProperty.VISIBILITY_SUPPORTED_COLORED
                    .getKey());
    //
    public final BooleanProperty clockVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_VISIBLE
                    .getKey());
    public final BooleanProperty clockHiddenProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_HIDDEN
                    .getKey());
    public final BooleanProperty clockHandsLongVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_LONG_VISIBLE
                    .getKey());
    public final BooleanProperty clockHandsColoredProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_COLORED
                    .getKey());
    public final BooleanProperty clockHandsHourVisibleProperty
            = new BooleanProperty(
                    TimeCalcProperty.CLOCK_HANDS_HOUR_VISIBLE.getKey());
    public final BooleanProperty clockHandsMinuteVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_MINUTE_VISIBLE
                    .getKey());
    public final BooleanProperty clockHandsSecondVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_SECOND_VISIBLE
                    .getKey());
    public final BooleanProperty clockHandsMillisecondVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_MILLISECOND_VISIBLE
                    .getKey());
    public final BooleanProperty clockBorderVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_VISIBLE
                    .getKey());
    public final BooleanProperty clockBorderOnlyHoursProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_BORDER_ONLY_HOURS
                    .getKey());

    public final BooleanProperty clockNumbersVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_NUMBERS_VISIBLE
                    .getKey());
    public final BooleanProperty clockCircleVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CIRCLE_VISIBLE
                    .getKey());
    public final BooleanProperty clockCircleStrongBorderProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CIRCLE_STRONG_BORDER
                    .getKey());
    public final StringProperty clockCircleBorderColorProperty
            = new StringProperty(TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR
                    .getKey());
    public final BooleanProperty clockCentreCircleVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_VISIBLE
                    .getKey());
    public final BooleanProperty clockCentreCircleBlackProperty
            = new BooleanProperty(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_BLACK
                    .getKey());
    public final BooleanProperty clockProgressVisibleOnlyIfMouseMovingOverProperty
            = new BooleanProperty(
                    TimeCalcProperty.CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    public final BooleanProperty clockDateVisibleOnlyIfMouseMovingOverProperty
            = new BooleanProperty(
                    TimeCalcProperty.CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    public final BooleanProperty clockSmileyVisibleProperty
            = new BooleanProperty(
            TimeCalcProperty.CLOCK_SMILEY_VISIBLE
                    .getKey());
    public final BooleanProperty clockPercentProgressVisibleProperty
            = new BooleanProperty(
            TimeCalcProperty.CLOCK_PERCENT_PROGRESS_VISIBLE
                    .getKey());
    public final BooleanProperty clockCircleProgressVisibleProperty
            = new BooleanProperty(
            TimeCalcProperty.CLOCK_CIRCLE_PROGRESS_VISIBLE
                    .getKey());
    //
    public final BooleanProperty batteryWavesVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_WAVES_VISIBLE
                    .getKey());
    public final BooleanProperty batteryCircleProgressProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_CIRCLE_PROGRESS_VISIBLE
                    .getKey());
    public final BooleanProperty batteryPercentProgressProperty
            = new BooleanProperty(
                    TimeCalcProperty.BATTERY_PERCENT_PROGRESS_VISIBLE
                            .getKey());
    public final BooleanProperty batteryChargingCharacterVisibleProperty
            = new BooleanProperty(
                    TimeCalcProperty.BATTERY_CHARGING_CHARACTER_VISIBLE
                            .getKey());
    public final BooleanProperty batteryNameVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_NAME_VISIBLE
                    .getKey());
    public final BooleanProperty batteryLabelVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_LABEL_VISIBLE
                    .getKey());
    public final BooleanProperty batteryVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_VISIBLE
                    .getKey());
    public final BooleanProperty batteryMinuteVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_MINUTE_VISIBLE
                    .getKey());
    public final BooleanProperty batteryHourVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_HOUR_VISIBLE
                    .getKey());
    public final BooleanProperty batteryDayVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_DAY_VISIBLE
                    .getKey());
    public final BooleanProperty batteryWeekVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_WEEK_VISIBLE
                    .getKey());
    public final BooleanProperty batteryMonthVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_MONTH_VISIBLE
                    .getKey());
    public final BooleanProperty batteryYearVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_YEAR_VISIBLE
                    .getKey());
    public final BooleanProperty batteryMinuteHiddenProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_MINUTE_HIDDEN
                    .getKey());
    public final BooleanProperty batteryHourHiddenProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_HOUR_HIDDEN
                    .getKey());
    public final BooleanProperty batteryDayHiddenProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_DAY_HIDDEN
                    .getKey());
    public final BooleanProperty batteryWeekHiddenProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_WEEK_HIDDEN
                    .getKey());
    public final BooleanProperty batteryMonthHiddenProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_MONTH_HIDDEN
                    .getKey());
    public final BooleanProperty batteryYearHiddenProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_YEAR_HIDDEN
                    .getKey());
    public final BooleanProperty batteryBlinkingIfCriticalLowVisibleProperty
            = new BooleanProperty(
                    TimeCalcProperty.BATTERY_BLINKING_IF_CRITICAL_LOW
                            .getKey());
    public final BooleanProperty batteryQuarterIconVisibleProperty
            = new BooleanProperty(
            TimeCalcProperty.BATTERY_QUARTER_ICON_VISIBLE
                    .getKey());
    public final BooleanProperty jokesVisibleProperty
            = new BooleanProperty(TimeCalcProperty.JOKES_VISIBLE
                    .getKey());
    public final BooleanProperty commandsVisibleProperty
            = new BooleanProperty(TimeCalcProperty.COMMANDS_VISIBLE
                    .getKey());
    public final BooleanProperty notificationsVisibleProperty
            = new BooleanProperty(TimeCalcProperty.NOTIFICATIONS_VISIBLE
                    .getKey());
    public final BooleanProperty smileysVisibleProperty
            = new BooleanProperty(TimeCalcProperty.SMILEYS_VISIBLE.getKey());
    public final BooleanProperty smileysVisibleOnlyIfMouseMovingOverProperty
            = new BooleanProperty(
                    TimeCalcProperty.SMILEYS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    public final BooleanProperty smileysColoredProperty
            = new BooleanProperty(TimeCalcProperty.SMILEYS_COLORED.getKey());

    public final BooleanProperty squareVisibleProperty
            = new BooleanProperty(TimeCalcProperty.SQUARE_VISIBLE.getKey());
    public final BooleanProperty squareHiddenProperty
            = new BooleanProperty(TimeCalcProperty.SQUARE_HIDDEN.getKey());
    public final StringProperty squareTypeProperty
            = new StringProperty(TimeCalcProperty.SQUARE_TYPE.getKey());
    public final BooleanProperty dotVisibleProperty
            = new BooleanProperty(TimeCalcProperty.DOT_VISIBLE.getKey());
    public final BooleanProperty dotHiddenProperty
            = new BooleanProperty(TimeCalcProperty.DOT_HIDDEN.getKey());
    public final StringProperty dotTypeProperty
            = new StringProperty(TimeCalcProperty.DOT_TYPE.getKey());
    public final BooleanProperty circleVisibleProperty
            = new BooleanProperty(TimeCalcProperty.CIRCLE_VISIBLE.getKey());
    public final BooleanProperty circleHiddenProperty
            = new BooleanProperty(TimeCalcProperty.CIRCLE_HIDDEN.getKey());
    public final StringProperty circleTypeProperty
            = new StringProperty(TimeCalcProperty.CIRCLE_TYPE.getKey());
    public final BooleanProperty walkingHumanVisibleProperty
            = new BooleanProperty(
                    TimeCalcProperty.WALKING_HUMAN_VISIBLE.getKey());
    public final BooleanProperty walkingHumanHiddenProperty
            = new BooleanProperty(
                    TimeCalcProperty.WALKING_HUMAN_HIDDEN.getKey());
    public final StringProperty walkingHumanTypeProperty
            = new StringProperty(TimeCalcProperty.WALKING_HUMAN_TYPE.getKey());
    public final BooleanProperty swingVisibleProperty
            = new BooleanProperty(TimeCalcProperty.SWING_VISIBLE.getKey());
    public final BooleanProperty swingHiddenProperty
            = new BooleanProperty(TimeCalcProperty.SWING_HIDDEN.getKey());
    public final StringProperty swingTypeProperty
            = new StringProperty(TimeCalcProperty.SWING_TYPE.getKey());
    public final BooleanProperty swingQuarterIconVisibleProperty
            = new BooleanProperty(TimeCalcProperty.SWING_QUARTER_ICON_VISIBLE.getKey());

    public final BooleanProperty lifeVisibleProperty
            = new BooleanProperty(TimeCalcProperty.LIFE_VISIBLE.getKey());
    public final BooleanProperty lifeHiddenProperty
            = new BooleanProperty(TimeCalcProperty.LIFE_HIDDEN.getKey());
    public final StringProperty lifeTypeProperty
            = new StringProperty(TimeCalcProperty.LIFE_TYPE.getKey());
    public final StringProperty lifeBirthDateProperty
            = new StringProperty(TimeCalcProperty.LIFE_BIRTH_DATE.getKey());
    public final BooleanProperty moneyVisibleProperty
            = new BooleanProperty(TimeCalcProperty.MONEY_VISIBLE.getKey());
    public final BooleanProperty moneyHiddenProperty
            = new BooleanProperty(TimeCalcProperty.MONEY_HIDDEN.getKey());
    public final StringProperty moneyTypeProperty
            = new StringProperty(TimeCalcProperty.MONEY_TYPE.getKey());
    public final IntegerProperty moneyPerMonthProperty
            = new IntegerProperty(TimeCalcProperty.MONEY_PER_MONTH.getKey());
    public final StringProperty moneyCurrencyProperty
            = new StringProperty(TimeCalcProperty.MONEY_CURRENCY.getKey());
    public final BooleanProperty weatherVisibleProperty
            = new BooleanProperty(TimeCalcProperty.WEATHER_VISIBLE.getKey());
    public final BooleanProperty weatherHiddenProperty
            = new BooleanProperty(TimeCalcProperty.WEATHER_HIDDEN.getKey());
    public final StringProperty mainWindowCustomTitleProperty
            = new StringProperty(
                    TimeCalcProperty.MAIN_WINDOW_CUSTOM_TITLE.getKey());
    public final StringProperty profileNameProperty
            = new StringProperty(TimeCalcProperty.PROFILE_NAME.getKey());
    public final StringProperty activityNeededFlagsProperty
            = new StringProperty(TimeCalcProperty.ACTIVITY_NEEDED_FLAGS.getKey());
    public final BooleanProperty testEnabledProperty = new BooleanProperty(TimeCalcProperty.TEST_ENABLED.getKey(), false);
    public final IntegerProperty testYearCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_YEAR
            .getKey(), Integer.MAX_VALUE);
    public final IntegerProperty testMonthCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_MONTH
            .getKey(), Integer.MAX_VALUE);
    public final IntegerProperty testDayCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_DAY
            .getKey(), Integer.MAX_VALUE);
    public final IntegerProperty testHourCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_HOUR
            .getKey(), Integer.MAX_VALUE);
    public final IntegerProperty testMinuteCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_MINUTE
            .getKey(), Integer.MAX_VALUE);
    public final IntegerProperty testSecondCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_SECOND
            .getKey(), Integer.MAX_VALUE);
    public final IntegerProperty testMillisecondCustomProperty = new IntegerProperty(TimeCalcProperty.TEST_CLOCK_CUSTOM_MILLISECOND
            .getKey(), Integer.MAX_VALUE);
    
    public final IntegerProperty speedProperty = new IntegerProperty(TimeCalcProperty.SPEED
            .getKey(), 1);

    //
    private final Map<TimeCalcProperty, Property> mapOfProperties
            = new HashMap<>();
    private final List<Property> allProperties = new ArrayList<>();

    private TimeCalcProperties timeCalcProperties;

    public TimeCalcConfiguration() {
        for (Property p : new Property[]{
            visibilityDefaultProperty,
            visibilitySupportedColoredProperty,
            clockVisibleProperty,
            clockHandsLongVisibleProperty,
            clockHandsColoredProperty,
            clockHandsHourVisibleProperty,
            clockHandsMinuteVisibleProperty,
            clockHandsSecondVisibleProperty,
            clockHandsMillisecondVisibleProperty,
            clockBorderVisibleProperty,
            clockBorderOnlyHoursProperty,
            clockNumbersVisibleProperty,
            clockCircleVisibleProperty,
            clockCircleStrongBorderProperty,
            clockCircleBorderColorProperty,
            clockCentreCircleVisibleProperty,
            clockCentreCircleBlackProperty,
            clockProgressVisibleOnlyIfMouseMovingOverProperty,
            clockDateVisibleOnlyIfMouseMovingOverProperty,
            clockSmileyVisibleProperty,
            clockPercentProgressVisibleProperty,
            clockCircleProgressVisibleProperty,
            batteryWavesVisibleProperty,
            batteryCircleProgressProperty,
            batteryPercentProgressProperty,
            batteryChargingCharacterVisibleProperty,
            batteryNameVisibleProperty,
            batteryLabelVisibleProperty,
            batteryVisibleProperty,
            batteryMinuteVisibleProperty,
            batteryHourVisibleProperty,
            batteryDayVisibleProperty,
            batteryWeekVisibleProperty,
            batteryMonthVisibleProperty,
            batteryYearVisibleProperty,
            batteryBlinkingIfCriticalLowVisibleProperty,
            batteryQuarterIconVisibleProperty,
            jokesVisibleProperty,
            commandsVisibleProperty,
            notificationsVisibleProperty,
            smileysVisibleProperty,
            smileysVisibleOnlyIfMouseMovingOverProperty,
            smileysColoredProperty,
            squareVisibleProperty,
            squareTypeProperty,
            dotVisibleProperty,
            dotTypeProperty,
            circleVisibleProperty,
            circleTypeProperty,
            swingVisibleProperty,
            swingTypeProperty,
            swingQuarterIconVisibleProperty,
            walkingHumanVisibleProperty,
            walkingHumanTypeProperty,
            lifeVisibleProperty,
            lifeTypeProperty,
            lifeBirthDateProperty,
            moneyVisibleProperty,
            moneyTypeProperty,
            moneyPerMonthProperty,
            moneyCurrencyProperty,
            weatherVisibleProperty,
            mainWindowCustomTitleProperty,
            profileNameProperty,
            activityNeededFlagsProperty,
            speedProperty,
            clockHiddenProperty,
            batteryMinuteHiddenProperty,
            batteryHourHiddenProperty,
            batteryDayHiddenProperty,
            batteryWeekHiddenProperty,
            batteryMonthHiddenProperty,
            batteryYearHiddenProperty,
            squareHiddenProperty,
            circleHiddenProperty,
            walkingHumanHiddenProperty,
            swingHiddenProperty,
            lifeHiddenProperty,
            moneyHiddenProperty,
            weatherHiddenProperty,
            dotHiddenProperty,
            testEnabledProperty,
            testYearCustomProperty,
            testMonthCustomProperty,
            testDayCustomProperty,
            testHourCustomProperty,
            testMinuteCustomProperty,
            testSecondCustomProperty,
            testMillisecondCustomProperty,}) {
            allProperties.add(p);
        }
        allProperties.stream().forEach(p -> mapOfProperties
                .put(TimeCalcProperty.forKey(p.getName()), p));
        batteryVisibleProperty.addListener(e -> {

            batteryMinuteVisibleProperty
                    .setValue(batteryVisibleProperty.getValue());
            batteryHourVisibleProperty
                    .setValue(batteryVisibleProperty.getValue());
            batteryDayVisibleProperty
                    .setValue(batteryVisibleProperty.getValue());
            batteryWeekVisibleProperty
                    .setValue(batteryVisibleProperty.getValue());
            batteryMonthVisibleProperty
                    .setValue(batteryVisibleProperty.getValue());
            batteryYearVisibleProperty
                    .setValue(batteryVisibleProperty.getValue());
        });
    }

    public Property getProperty(TimeCalcProperty timeCalcProperty) {
        if (!mapOfProperties.containsKey(timeCalcProperty)) {
            throw new TimeCalcException(
                    "There is no property for : " + timeCalcProperty);
        }
        return mapOfProperties.get(timeCalcProperty);
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        for (Property p : allProperties) {
            if (!p.getName().startsWith("test")) {
                continue;
            }
            sb.append(p.getName()).append("=").append(p.getValue())
                    .append("\n");
        }
        return sb.toString();
    }

    public void saveToTimeCalcProperties() {
        System.out.println("Going to save properties.");
        if (timeCalcProperties == null) {
            throw new TimeCalcException(
                    "Cannot save properties, because timeCalcProperties is null.");
        }
        Properties properties = new Properties();
        this.allProperties.stream()
                .forEach(p -> properties.put(p.getName(), p.getValue()));
        this.timeCalcProperties
                .save(properties, this.profileNameProperty.getValue());

    }

    public void loadFromTimeCalcProperties(
            TimeCalcProperties timeCalcProperties) {
        this.timeCalcProperties = timeCalcProperties;
        for (Property p : allProperties) {
            if (p instanceof BooleanProperty) {
                p.setValue(timeCalcProperties
                        .getBooleanProperty(
                                TimeCalcProperty.forKey(p.getName())));
                continue;
            }

            if (p.getName()
                    .equals(TimeCalcProperty.VISIBILITY_DEFAULT.name())) {
                visibilityDefaultProperty.setValue(timeCalcProperties
                        .getStringProperty(
                                TimeCalcProperty.VISIBILITY_DEFAULT));
                continue;
            }
            if (p instanceof StringProperty) {
                p.setValue(timeCalcProperties
                        .getStringProperty(
                                TimeCalcProperty.forKey(p.getName())));
                continue;
            }
            if (p instanceof IntegerProperty) {
                p.setValue(timeCalcProperties
                        .getIntegerProperty(
                                TimeCalcProperty.forKey(p.getName())));
                continue;
            }
            throw new TimeCalcException(
                    "Unsupported Property class: " + p.getClass().getName());

        }
        //        for(Property p:allProperties) {
        //            System.out.println(p.getName() + "=" + p.getValue());
        //        }
    }

}
