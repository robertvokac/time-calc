package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.entity.Visibility;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ConfigWindow extends JFrame {
    public static final int WIDTH1 = 600;
    public static final int HEIGHT1 = 30;
    private final TimeCalcConfiguration timeCalcConfiguration;
    private int currentY = SwingUtils.MARGIN;;
    private JCheckBox clockHandLongProperty = new JCheckBox("Visibility : Clock : Hands are long", true);
    private JCheckBox clockHandMinuteEnabledProperty = new JCheckBox("Visibility : Clock : Minute hand", true);
    private JCheckBox clockHandSecondEnabledProperty = new JCheckBox("Visibility : Clock : Second hand", true);
    private JCheckBox clockHandMillisecondEnabledProperty = new JCheckBox("Visibility : Clock : Millisecond hand", false);
    private JCheckBox batteryWavesEnabledProperty = new JCheckBox("Visibility : Battery : Waves", true);
    public final JComboBox visibilityCurrentProperty = new JComboBox(
            Arrays.stream(Visibility.values()).map(v->v.name()).collect(
                    Collectors.toList()).toArray());

    private JCheckBox visibilityOnlyGreyOrNoneEnabledProperty = new JCheckBox("Visibility : Only GREY or NONE",
                    false);
    private JCheckBox jokesEnabledProperty = new JCheckBox("Visibility : Jokes", true);
    private JCheckBox commandsEnabledProperty = new JCheckBox("Visibility : Commands", true);
    private JCheckBox toastsEnabledProperty = new JCheckBox("Visibility : Toasts", true);
    private JCheckBox smileysColoredProperty = new JCheckBox("Visibility : Smileys", true);


    public ConfigWindow(TimeCalcConfiguration timeCalcConfiguration) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        setTitle("Configuration");
        this.setSize(800, WIDTH1);

        setLayout(null);

        add(clockHandLongProperty);
        clockHandLongProperty.setBounds(SwingUtils.MARGIN, currentY, WIDTH1,
                HEIGHT1);
        nextRow();

        add(clockHandMinuteEnabledProperty);
        clockHandMinuteEnabledProperty.setBounds(SwingUtils.MARGIN, currentY,
                WIDTH1, HEIGHT1);
        nextRow();

        add(clockHandSecondEnabledProperty);
        clockHandSecondEnabledProperty.setBounds(SwingUtils.MARGIN, currentY,
                WIDTH1, HEIGHT1);
        nextRow();

        add(clockHandMillisecondEnabledProperty);
        clockHandMillisecondEnabledProperty.setBounds(SwingUtils.MARGIN, currentY,
                WIDTH1, HEIGHT1);
        nextRow();

        add(batteryWavesEnabledProperty);
        batteryWavesEnabledProperty.setBounds(SwingUtils.MARGIN, currentY,
                WIDTH1, HEIGHT1);
        nextRow();
        JLabel visibilityCurrentLabel = new JLabel("Visibility : Current");

        add(visibilityCurrentLabel);
        visibilityCurrentLabel.setBounds(SwingUtils.MARGIN, currentY, WIDTH1,
                HEIGHT1);
        nextRow();

        add(visibilityCurrentProperty);
        visibilityCurrentProperty.setMaximumSize(new Dimension(150, 25));
        visibilityCurrentProperty.setBounds(SwingUtils.MARGIN, currentY, WIDTH1,
                HEIGHT1);
        nextRow();

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

        visibilityCurrentProperty.addActionListener(e -> {
            timeCalcConfiguration.visibilityCurrentProperty.setValue(
                    (String) visibilityCurrentProperty.getSelectedItem());
            System.out.println("configWindow.visibilityCurrentProperty=" + visibilityCurrentProperty.getSelectedItem());
        });

    }

    private void nextRow() {
        currentY = currentY + 3 * SwingUtils.MARGIN;
    }
}
