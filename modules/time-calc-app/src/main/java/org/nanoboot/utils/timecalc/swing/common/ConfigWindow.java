package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.entity.Visibility;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ConfigWindow extends JFrame {
    private final TimeCalcConfiguration timeCalcConfiguration;
    private JCheckBox clockHandLongProperty = new JCheckBox("Visibility : Clock : Hands are long", true);
    private JCheckBox clockHandMinuteEnabledProperty = new JCheckBox("Visibility : Clock : Minute hand", true);
    private JCheckBox clockHandSecondEnabledProperty = new JCheckBox("Visibility : Clock : Second hand", true);
    private JCheckBox clockHandMillisecondEnabledProperty = new JCheckBox("Visibility : Clock : Millisecond hand", false);
    private JCheckBox batteryWavesEnabledProperty = new JCheckBox("Visibility : Battery : Waves", true);
    public final JTextField defaultVisibilityProperty = new JTextField(Visibility.STRONGLY_COLORED.name());

    private JCheckBox visibilityOnlyGreyOrNoneEnabledProperty = new JCheckBox("Visibility : Only GREY or NONE",
                    false);
    private JCheckBox jokesEnabledProperty = new JCheckBox("Visibility : Jokes", true);
    private JCheckBox commandsEnabledProperty = new JCheckBox("Visibility : Commands", true);
    private JCheckBox toastsEnabledProperty = new JCheckBox("Visibility : Toasts", true);
    private JCheckBox smileysColoredProperty = new JCheckBox("Visibility : Smileys", true);


    public ConfigWindow(TimeCalcConfiguration timeCalcConfiguration) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        setTitle("Configuration");
        this.setSize(800, 600);
        LayoutManager flowLayout = new FlowLayout(FlowLayout.LEFT);
        setLayout(flowLayout);

        add(clockHandLongProperty);
        add(clockHandMinuteEnabledProperty);
        add(clockHandSecondEnabledProperty);
        add(clockHandMillisecondEnabledProperty);
        add(batteryWavesEnabledProperty);
        add(new JLabel("Visibility : Default"));
        add(defaultVisibilityProperty);

        clockHandLongProperty.addActionListener(e -> {
            timeCalcConfiguration.clockHandLongProperty.setValue(clockHandLongProperty.isSelected());
        });
        clockHandMinuteEnabledProperty.addActionListener(e -> {
            timeCalcConfiguration.clockHandMinuteEnabledProperty.setValue(clockHandMinuteEnabledProperty.isSelected());
        });
        clockHandSecondEnabledProperty.addActionListener(e -> {
            timeCalcConfiguration.clockHandSecondEnabledProperty.setValue(clockHandSecondEnabledProperty.isSelected());
        });
        clockHandMillisecondEnabledProperty.addActionListener(e -> {
            timeCalcConfiguration.clockHandMillisecondEnabledProperty.setValue(clockHandMillisecondEnabledProperty.isSelected());
        });
        batteryWavesEnabledProperty.addActionListener(e -> {
            timeCalcConfiguration.batteryWavesEnabledProperty.setValue(batteryWavesEnabledProperty.isSelected());
        });

        defaultVisibilityProperty.addActionListener(e -> {
            timeCalcConfiguration.defaultVisibilityProperty.setValue(defaultVisibilityProperty.getText());
        });


    }
}
