package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class TimeCalcConfiguration {
    public final StringProperty visibilityDefaultProperty = new StringProperty(TimeCalcProperty.VISIBILITY_DEFAULT
            .getKey());
    public final BooleanProperty visibilitySupportedColoredProperty =
            new BooleanProperty(TimeCalcProperty.VISIBILITY_SUPPORTED_COLORED
                    .getKey());
    //
    public final BooleanProperty clockHandsLongVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_LONG_VISIBLE
                    .getKey());
    public final BooleanProperty clockHandsMinuteVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_MINUTE_VISIBLE
                    .getKey());
    public final BooleanProperty clockHandsSecondVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_SECOND_VISIBLE
                    .getKey());
    public final BooleanProperty clockHandsMillisecondVisibleProperty =
            new BooleanProperty(TimeCalcProperty.CLOCK_HANDS_MILLISECOND_VISIBLE
                    .getKey());
    //
    public final BooleanProperty batteryWavesVisibleProperty =
            new BooleanProperty(TimeCalcProperty.BATTERY_WAVES_VISIBLE
                    .getKey());

    public final BooleanProperty jokesVisibleProperty =
            new BooleanProperty(TimeCalcProperty.JOKES_VISIBLE
                    .getKey());
    public final BooleanProperty commandsVisibleProperty =
            new BooleanProperty(TimeCalcProperty.COMMANDS_VISIBLE
                    .getKey());
    public final BooleanProperty notificationsVisibleProperty =
            new BooleanProperty(TimeCalcProperty.NOTIFICATIONS_VISIBLE
                    .getKey());
    public final BooleanProperty smileysColoredProperty =
            new BooleanProperty(TimeCalcProperty.SMILEYS_COLORED.getKey());

    private final Map<TimeCalcProperty, Property> mapOfProperties = new HashMap<>();
    private List<Property> allProperties = new ArrayList<>();
    public TimeCalcConfiguration() {
        for(Property p:new Property[] {
                visibilityDefaultProperty,
                visibilitySupportedColoredProperty,
                clockHandsLongVisibleProperty,
                clockHandsSecondVisibleProperty,
                clockHandsMillisecondVisibleProperty,
                batteryWavesVisibleProperty,
                jokesVisibleProperty,
                commandsVisibleProperty,
                notificationsVisibleProperty,
                smileysColoredProperty,
        }) {
            allProperties.add(p);
        }
        allProperties.stream().forEach(p -> mapOfProperties.put(TimeCalcProperty.forKey(p.getName()), p));
    }

    public Property getProperty(TimeCalcProperty timeCalcProperty) {
        if(!mapOfProperties.containsKey(timeCalcProperty)) {
            throw new TimeCalcException("There is no property for : " + timeCalcProperty);
        }
        return mapOfProperties.get(timeCalcProperty);
    }

    public void setFromTimeCalcProperties(
            TimeCalcProperties timeCalcProperties) {
        for(Property p:allProperties) {
            if(p instanceof BooleanProperty) {
                ((BooleanProperty)p).setValue(timeCalcProperties.getBooleanProperty(TimeCalcProperty.forKey(p.getName())));
                continue;
            }

            if(p.getName().equals(TimeCalcProperty.VISIBILITY_DEFAULT.name())) {
                visibilityDefaultProperty.setValue(timeCalcProperties.getStringProperty(TimeCalcProperty.VISIBILITY_DEFAULT));
                continue;
            }
            if(p instanceof StringProperty) {
                ((StringProperty)p).setValue(timeCalcProperties.getStringProperty(TimeCalcProperty.forKey(p.getName())));
                continue;
            }
            throw new TimeCalcException("Unsupported Property class: " + p.getClass().getName());

        }
    }

}
