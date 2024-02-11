package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ConfigWindow extends TimeCalcWindow {
    public static final int WIDTH1 = 600;
    public static final int HEIGHT1 = 30;
    public static final String CLIENT_PROPERTY_KEY = TimeCalcProperty.class.getName();
    private final TimeCalcConfiguration timeCalcConfiguration;
    private int currentY = SwingUtils.MARGIN;
    private List<JComponent> propertiesList = new ArrayList<>();
    private Map<TimeCalcProperty, JComponent> propertiesMap = new HashMap<>();
    public final JComboBox visibilityDefaultProperty = new JComboBox(Arrays.stream(Visibility.values()).map(v -> v.name()).collect(
                    Collectors.toList()).toArray());

    private JCheckBox visibilitySupportedColoredProperty =
            new JCheckBox("visibility.supported.colored");
    private JCheckBox clockHandsLongVisibleProperty =
            new JCheckBox("clock.hands.long.visible");
    private JCheckBox clockHandsMinuteVisibleProperty =
            new JCheckBox("clock.hands.minute.visible");
    private JCheckBox clockHandsSecondVisibleProperty =
            new JCheckBox("clock.hands.second.visible");
    private JCheckBox clockHandsMillisecondVisibleProperty =
            new JCheckBox("clock.hands.millisecond.visible");
    private JCheckBox batteryWavesVisibleProperty =
            new JCheckBox("battery.waves.visible");

    private JCheckBox jokesVisibleProperty =
            new JCheckBox("jokes.visible");
    private JCheckBox commandsVisibleProperty =
            new JCheckBox("commands.visible");
    private JCheckBox notificationsVisibleProperty =
            new JCheckBox("notifications.visible");
    private JCheckBox smileysColoredProperty =
            new JCheckBox("smileys.colored");

    public ConfigWindow(TimeCalcConfiguration timeCalcConfiguration) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        setTitle("Configuration");
        this.setSize(800, WIDTH1);
        setLayout(null);

        propertiesList.addAll(Arrays.asList(visibilityDefaultProperty,
                visibilitySupportedColoredProperty,
                clockHandsLongVisibleProperty,
                clockHandsMinuteVisibleProperty,
                clockHandsSecondVisibleProperty,
                clockHandsMillisecondVisibleProperty,
                batteryWavesVisibleProperty,
                jokesVisibleProperty,
                commandsVisibleProperty,
                notificationsVisibleProperty,
                smileysColoredProperty));
        //
        propertiesList.stream().forEach(p -> {
            if(p == visibilityDefaultProperty) {
                p.putClientProperty(CLIENT_PROPERTY_KEY, TimeCalcProperty.VISIBILITY_DEFAULT.getKey());
                addToNextRow(new JLabel(TimeCalcProperty.VISIBILITY_DEFAULT.getDescription()));
            }
            if(p instanceof JComboBox) {
                JComboBox jComboBox = ((JComboBox)p);
                jComboBox.setMaximumSize(new Dimension(150, 25));

                String timeCalcPropertyKey = (String) jComboBox.getClientProperty(
                        CLIENT_PROPERTY_KEY);
                TimeCalcProperty timeCalcProperty =
                        TimeCalcProperty.forKey(timeCalcPropertyKey);
                jComboBox.addActionListener(e -> {
                    ((StringProperty) timeCalcConfiguration.getProperty(timeCalcProperty))
                            .setValue(
                            (String) jComboBox.getSelectedItem());
                    System.out.println("configWindow.visibilityCurrentProperty="
                                       + visibilityDefaultProperty.getSelectedItem());
                });
            }

            if(p instanceof JCheckBox) {
                JCheckBox checkBox = ((JCheckBox)p);
                String timeCalcPropertyKey = checkBox.getText();
                checkBox.putClientProperty(CLIENT_PROPERTY_KEY, timeCalcPropertyKey);
                TimeCalcProperty timeCalcProperty =
                        TimeCalcProperty.forKey(timeCalcPropertyKey);
                checkBox.setText(timeCalcProperty.getDescription());
                checkBox.addActionListener(e -> {
                    ((BooleanProperty)timeCalcConfiguration.getProperty(timeCalcProperty))
                            .setValue(checkBox.isSelected());
                });
            }
            propertiesMap.put(TimeCalcProperty.forKey(
                    (String) p.getClientProperty(CLIENT_PROPERTY_KEY)),p);
            addToNextRow(p);
        });

        Arrays.stream(getComponents()).forEach(c->c.getClass().getName());

    }

    private void addToNextRow(JComponent jComponent) {
        jComponent.setBounds(SwingUtils.MARGIN, currentY, WIDTH1,
                HEIGHT1);
        nextRow();
    }

    private void nextRow() {
        currentY = currentY + 3 * SwingUtils.MARGIN;
    }
}
