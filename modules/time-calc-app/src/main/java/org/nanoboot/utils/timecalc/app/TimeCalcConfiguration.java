package org.nanoboot.utils.timecalc.app;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class TimeCalcConfiguration {
    public final BooleanProperty clockHandLongProperty =
            new BooleanProperty("clockHandLongProperty", true);
    public final BooleanProperty clockHandMinuteEnabledProperty =
            new BooleanProperty("clockHandMinuteEnabledProperty", true);
    public final BooleanProperty clockHandSecondEnabledProperty =
            new BooleanProperty("clockHandSecondEnabledProperty", true);
    public final BooleanProperty clockHandMillisecondEnabledProperty =
            new BooleanProperty("clockHandMillisecondEnabledProperty", false);
    public final BooleanProperty batteryWavesEnabledProperty =
            new BooleanProperty("batteryWavesEnabledProperty", true);
    public final StringProperty
            defaultVisibilityProperty =
            new StringProperty("defaultVisibilityProperty",
                    Visibility.STRONGLY_COLORED.name());
    public final BooleanProperty visibilityOnlyGreyOrNoneEnabledProperty =
            new BooleanProperty("visibilityOnlyGreyOrNoneEnabledProperty",
                    false);
    public final BooleanProperty jokesEnabledProperty =
            new BooleanProperty("jokesEnabledProperty", true);
    public final BooleanProperty commandsEnabledProperty =
            new BooleanProperty("commandsEnabledProperty", true);
    public final BooleanProperty toastsEnabledProperty =
            new BooleanProperty("toastsEnabledProperty", true);

    public TimeCalcConfiguration() {

    }

    public void setFromTimeCalcProperties(
            TimeCalcProperties timeCalcProperties) {
        clockHandLongProperty.setValue(timeCalcProperties.areClockHandsLong());
        clockHandMinuteEnabledProperty
                .setValue(timeCalcProperties.isMinuteEnabled());
        clockHandSecondEnabledProperty
                .setValue(timeCalcProperties.isSecondEnabled());
        clockHandMillisecondEnabledProperty
                .setValue(timeCalcProperties.isMillisecondEnabled());
        batteryWavesEnabledProperty
                .setValue(timeCalcProperties.areBatteryWavesEnabled());
        defaultVisibilityProperty
                .setValue(timeCalcProperties.getDefaultVisibility().name());
        visibilityOnlyGreyOrNoneEnabledProperty.setValue(
                timeCalcProperties.isVisibilityOnlyGreyOrNoneEnabled());
        jokesEnabledProperty.setValue(timeCalcProperties.areJokesEnabled());
        commandsEnabledProperty
                .setValue(timeCalcProperties.areCommandsEnabled());
        toastsEnabledProperty.setValue(timeCalcProperties.areToastsEnabled());
    }

}
