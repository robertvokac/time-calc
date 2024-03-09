package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
public class ConfigWindow extends TWindow {

    public static final int WIDTH1 = 600;
    public static final int HEIGHT1 = 30;
    public static final String CLIENT_PROPERTY_KEY =
            TimeCalcProperty.class.getName();
    public static final String THREE_DASHES = "---";
    private static final Font BIG_FONT = new Font("sans", Font.BOLD, 24);
    private static final String FIVE_SPACES = "     ";
    public final JComboBox visibilityDefaultProperty = new JComboBox(
            Arrays.stream(Visibility.values()).map(v -> v.name()).collect(
                    Collectors.toList()).toArray());
    private final TimeCalcConfiguration timeCalcConfiguration;
    private final JPanel panelInsideScrollPane;
    private int currentY = SwingUtils.MARGIN;
    private final List<JComponent> propertiesList = new ArrayList<>();
    private final Map<TimeCalcProperty, JComponent> propertiesMap = new HashMap<>();
    private final TButton enableAsMuchAsPossible =
            new TButton("Enable as much as possible");
    private final TButton disableAsMuchAsPossible =
            new TButton("Disable as much as possible");
    private final JCheckBox visibilitySupportedColoredProperty
            = new JCheckBox(
            TimeCalcProperty.VISIBILITY_SUPPORTED_COLORED.getKey());
    private final JCheckBox clockVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_VISIBLE.getKey());
    private final JCheckBox clockHandsLongVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_HANDS_LONG_VISIBLE.getKey());
    private final JCheckBox clockHandsColoredProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_HANDS_COLORED.getKey());
    private final JCheckBox clockHandsHourVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_HANDS_HOUR_VISIBLE.getKey());
    private final JCheckBox clockHandsMinuteVisibleProperty
            =
            new JCheckBox(TimeCalcProperty.CLOCK_HANDS_MINUTE_VISIBLE.getKey());
    private final JCheckBox clockHandsSecondVisibleProperty
            =
            new JCheckBox(TimeCalcProperty.CLOCK_HANDS_SECOND_VISIBLE.getKey());
    private final JCheckBox clockHandsMillisecondVisibleProperty
            = new JCheckBox(
            TimeCalcProperty.CLOCK_HANDS_MILLISECOND_VISIBLE.getKey());
    private final JCheckBox clockBorderVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_BORDER_VISIBLE.getKey());
    private final JCheckBox clockBorderOnlyHoursProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_BORDER_ONLY_HOURS.getKey());
    private final JCheckBox clockNumbersVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_NUMBERS_VISIBLE.getKey());
    private final JCheckBox clockCircleVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_CIRCLE_VISIBLE.getKey());
    private final JCheckBox clockCircleStrongBorderProperty
            =
            new JCheckBox(TimeCalcProperty.CLOCK_CIRCLE_STRONG_BORDER.getKey());
    private final JColorChooser clockCircleBorderColorProperty
            = new JColorChooser(Color.BLACK);
    private final JCheckBox clockCentreCircleVisibleProperty
            = new JCheckBox(
            TimeCalcProperty.CLOCK_CENTRE_CIRCLE_VISIBLE.getKey());
    private final JCheckBox clockCentreCircleBlackProperty
            =
            new JCheckBox(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_BLACK.getKey());
    private final JCheckBox clockProgressVisibleOnlyIfMouseMovingOverProperty
            = new JCheckBox(
            TimeCalcProperty.CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                    .getKey());
    private final JCheckBox clockDateVisibleOnlyIfMouseMovingOverProperty
            = new JCheckBox(
            TimeCalcProperty.CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                    .getKey());
    //
    private final JCheckBox batteryWavesVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_WAVES_VISIBLE.getKey());
    private final JCheckBox batteryCircleProgressVisibleProperty
            = new JCheckBox(
            TimeCalcProperty.BATTERY_CIRCLE_PROGRESS_VISIBLE.getKey());
    private final JCheckBox batteryPercentProgressProperty
            = new JCheckBox(
            TimeCalcProperty.BATTERY_PERCENT_PROGRESS_VISIBLE.getKey());
    private final JCheckBox batteryChargingCharacterVisibleProperty
            = new JCheckBox(
            TimeCalcProperty.BATTERY_CHARGING_CHARACTER_VISIBLE.getKey());
    private final JCheckBox batteryNameVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_NAME_VISIBLE.getKey());
    private final JCheckBox batteryLabelVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_LABEL_VISIBLE.getKey());
    private final JCheckBox batteryVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_VISIBLE.getKey());
    private final JCheckBox batteryMinuteVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_MINUTE_VISIBLE.getKey());
    private final JCheckBox batteryHourVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_HOUR_VISIBLE.getKey());
    private final JCheckBox batteryDayVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_DAY_VISIBLE.getKey());
    private final JCheckBox batteryWeekVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_WEEK_VISIBLE.getKey());
    private final JCheckBox batteryMonthVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_MONTH_VISIBLE.getKey());
    private final JCheckBox batteryYearVisibleProperty =
            new JCheckBox(TimeCalcProperty.BATTERY_YEAR_VISIBLE.getKey());
    private final JCheckBox batteryBlinkingIfCriticalLowVisibleProperty =
            new JCheckBox(
                    TimeCalcProperty.BATTERY_BLINKING_IF_CRITICAL_LOW.getKey());
    private final JCheckBox jokesVisibleProperty
            = new JCheckBox(TimeCalcProperty.JOKES_VISIBLE.getKey());
    private final JCheckBox commandsVisibleProperty
            = new JCheckBox(TimeCalcProperty.COMMANDS_VISIBLE.getKey());
    private final JCheckBox notificationsVisibleProperty
            = new JCheckBox(TimeCalcProperty.NOTIFICATIONS_VISIBLE.getKey());
    private final JCheckBox smileysVisibleOnlyIfMouseMovingOverProperty
            = new JCheckBox(
            TimeCalcProperty.SMILEYS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                    .getKey());
    private final JCheckBox smileysVisibleProperty
            = new JCheckBox(TimeCalcProperty.SMILEYS_VISIBLE.getKey());
    private final JCheckBox smileysColoredProperty
            = new JCheckBox(TimeCalcProperty.SMILEYS_COLORED.getKey());
    private final JCheckBox squareVisibleProperty
            = new JCheckBox(TimeCalcProperty.SQUARE_VISIBLE.getKey());
    private final JCheckBox circleVisibleProperty
            = new JCheckBox(TimeCalcProperty.CIRCLE_VISIBLE.getKey());
    private final JCheckBox walkingHumanVisibleProperty
            = new JCheckBox(TimeCalcProperty.WALKING_HUMAN_VISIBLE.getKey());
    private final JTextField mainWindowCustomTitleProperty
            = new JTextField();
    private final JTextField profileNameProperty
            = new JTextField();
    private final JTextField testClockCustomYearProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_YEAR.getKey());
    private final JTextField testClockCustomMonthProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_MONTH.getKey());
    private final JTextField testClockCustomDayProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_DAY.getKey());
    private final JTextField testClockCustomHourProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_HOUR.getKey());
    private final JTextField testClockCustomMinuteProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_MINUTE.getKey());
    private final JTextField testClockCustomSecondProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_SECOND.getKey());
    private final JTextField testClockCustomMillisecondProperty
            = new JTextField(TimeCalcProperty.TEST_CLOCK_CUSTOM_MILLISECOND.getKey());
    public ConfigWindow(TimeCalcConfiguration timeCalcConfiguration) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        setTitle("Configuration");
        this.setSize(800, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                getHeight() - 6 * SwingUtils.MARGIN));
        this.panelInsideScrollPane = new JPanel();
        final BoxLayout boxLayout =
                new BoxLayout(panelInsideScrollPane, BoxLayout.Y_AXIS);
        panelInsideScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        mainPanel.setAlignmentX(LEFT_ALIGNMENT);

        panelInsideScrollPane.setLayout(boxLayout);
        panelInsideScrollPane
                .setMinimumSize(new Dimension(Integer.MAX_VALUE, 400));

        JScrollPane scrollPane = new JScrollPane(panelInsideScrollPane);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(
                new Dimension(getWidth() - 50, getHeight() - 100));
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setBorder(null);
        //        mainPanel.setBackground(Color.red);
        //        scrollPane.setBackground(Color.green);
        //        panelInsideScrollPane.setBackground(Color.blue);

        add(mainPanel);
        //mainPanel.setLayout(null);
        mainPanel.add(enableAsMuchAsPossible);
        enableAsMuchAsPossible
                .setBounds(SwingUtils.MARGIN, SwingUtils.MARGIN, 250,
                        HEIGHT1);

        mainPanel.add(disableAsMuchAsPossible);
        disableAsMuchAsPossible.setBounds(
                enableAsMuchAsPossible.getX() + enableAsMuchAsPossible
                        .getWidth() + SwingUtils.MARGIN, SwingUtils.MARGIN, 250,
                HEIGHT1);
        scrollPane.setBounds(enableAsMuchAsPossible.getX(),
                enableAsMuchAsPossible.getY() + enableAsMuchAsPossible
                        .getHeight() + SwingUtils.MARGIN, Integer.MAX_VALUE,
                Integer.MAX_VALUE);

        mainPanel.add(scrollPane);

        for (boolean enable : new boolean[] {true, false}) {
            TButton button =
                    enable ? enableAsMuchAsPossible : disableAsMuchAsPossible;

            button.addActionListener(e -> {
                visibilityDefaultProperty
                        .setSelectedItem(Visibility.STRONGLY_COLORED.name());
                clockVisibleProperty.setSelected(true);
                clockHandsHourVisibleProperty.setSelected(enable);
                clockHandsMinuteVisibleProperty.setSelected(enable);
                clockHandsSecondVisibleProperty.setSelected(enable);
                clockHandsMillisecondVisibleProperty.setSelected(enable);
                clockHandsLongVisibleProperty.setSelected(enable);
                clockHandsColoredProperty.setSelected(enable);
                clockBorderVisibleProperty.setSelected(enable);
                clockBorderOnlyHoursProperty.setSelected(!enable);
                clockNumbersVisibleProperty.setSelected(enable);
                clockCircleVisibleProperty.setSelected(enable);
                clockCircleStrongBorderProperty.setSelected(!enable);

                clockCircleBorderColorProperty
                        .setColor(enable ? Color.BLUE : Color.BLACK);
                timeCalcConfiguration.clockCircleBorderColorProperty
                        .setValue(enable ? "0,0,255" : "0,0,0");

                clockCentreCircleVisibleProperty.setSelected(enable);
                clockCentreCircleBlackProperty.setSelected(!enable);
                clockProgressVisibleOnlyIfMouseMovingOverProperty
                        .setSelected(!enable);
                clockDateVisibleOnlyIfMouseMovingOverProperty
                        .setSelected(false);
                batteryVisibleProperty.setSelected(true);
                batteryWavesVisibleProperty.setSelected(enable);
                batteryCircleProgressVisibleProperty.setSelected(enable);
                batteryPercentProgressProperty.setSelected(enable);
                batteryChargingCharacterVisibleProperty.setSelected(enable);
                batteryNameVisibleProperty.setSelected(enable);
                batteryLabelVisibleProperty.setSelected(enable);
                batteryMinuteVisibleProperty.setSelected(enable);
                batteryHourVisibleProperty.setSelected(enable);
                batteryDayVisibleProperty.setSelected(true);
                batteryWeekVisibleProperty.setSelected(true);
                batteryMonthVisibleProperty.setSelected(enable);
                batteryYearVisibleProperty.setSelected(enable);
                batteryBlinkingIfCriticalLowVisibleProperty.setSelected(enable);
                //
                jokesVisibleProperty.setSelected(true);
                commandsVisibleProperty.setSelected(enable);
                notificationsVisibleProperty.setSelected(enable);
                smileysVisibleProperty.setSelected(enable);
                smileysColoredProperty.setSelected(enable);
                smileysVisibleOnlyIfMouseMovingOverProperty
                        .setSelected(!enable);
                squareVisibleProperty.setSelected(enable);
                circleVisibleProperty.setSelected(enable);
                walkingHumanVisibleProperty.setSelected(enable);
                MainWindow.hideShowCheckBox.setSelected(enable);
            });
        }

        propertiesList.addAll(Arrays.asList(visibilityDefaultProperty,
                visibilitySupportedColoredProperty,
                clockVisibleProperty,
                clockHandsHourVisibleProperty,
                clockHandsMinuteVisibleProperty,
                clockHandsSecondVisibleProperty,
                clockHandsMillisecondVisibleProperty,
                clockHandsLongVisibleProperty,
                clockHandsColoredProperty,
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
                batteryVisibleProperty,
                batteryWavesVisibleProperty,
                batteryCircleProgressVisibleProperty,
                batteryPercentProgressProperty,
                batteryChargingCharacterVisibleProperty,
                batteryNameVisibleProperty,
                batteryLabelVisibleProperty,
                batteryMinuteVisibleProperty,
                batteryHourVisibleProperty,
                batteryDayVisibleProperty,
                batteryWeekVisibleProperty,
                batteryMonthVisibleProperty,
                batteryYearVisibleProperty,
                batteryBlinkingIfCriticalLowVisibleProperty,
                //
                smileysVisibleProperty,
                smileysVisibleOnlyIfMouseMovingOverProperty,
                smileysColoredProperty,
                testClockCustomYearProperty,
                testClockCustomMonthProperty,
                testClockCustomDayProperty,
                testClockCustomHourProperty,
                testClockCustomMinuteProperty,
                testClockCustomSecondProperty,
                testClockCustomMillisecondProperty,
                jokesVisibleProperty,
                commandsVisibleProperty,
                notificationsVisibleProperty,
                squareVisibleProperty,
                circleVisibleProperty,
                walkingHumanVisibleProperty,
                mainWindowCustomTitleProperty,
                profileNameProperty));
        //
        propertiesList.stream().forEach(p -> {
            p.setAlignmentX(LEFT_ALIGNMENT);
            if (p == visibilityDefaultProperty) {
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.VISIBILITY_DEFAULT.getKey());
                addToNextRow(new JLabel(
                        TimeCalcProperty.VISIBILITY_DEFAULT.getDescription()));
            }
            if (p == clockCircleBorderColorProperty) {
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR.getKey());
                addToNextRow(new JLabel(
                        TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR
                                .getDescription().replace("Clock : ", "")));
            }
            if (p == mainWindowCustomTitleProperty) {
                addToNextRow(new JLabel(
                        TimeCalcProperty.MAIN_WINDOW_CUSTOM_TITLE
                                .getDescription()));
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.MAIN_WINDOW_CUSTOM_TITLE.getKey());
            }
            if (p == profileNameProperty) {
                addToNextRow(new JLabel(
                        TimeCalcProperty.PROFILE_NAME.getDescription()));
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.PROFILE_NAME.getKey());
            }
            if (p == testClockCustomYearProperty) {
                JLabel label = new JLabel("Test");
                label.setFont(BIG_FONT);
                addToNextRow(label);
            }

            if (p instanceof JComboBox) {
                JComboBox jComboBox = ((JComboBox) p);
                jComboBox.setMaximumSize(new Dimension(150, 25));

                String timeCalcPropertyKey =
                        (String) jComboBox.getClientProperty(
                                CLIENT_PROPERTY_KEY);
                TimeCalcProperty timeCalcProperty
                        = TimeCalcProperty.forKey(timeCalcPropertyKey);
                jComboBox.setSelectedItem(
                        timeCalcConfiguration.getProperty(timeCalcProperty)
                                .getValue());
                jComboBox.addActionListener(e -> {
                    timeCalcConfiguration
                            .getProperty(timeCalcProperty)
                            .setValue(
                                    (String) jComboBox.getSelectedItem());
                });
            }

            if (p instanceof JCheckBox) {
                JCheckBox checkBox = ((JCheckBox) p);
                String timeCalcPropertyKey = checkBox.getText();
                checkBox.putClientProperty(CLIENT_PROPERTY_KEY,
                        timeCalcPropertyKey);
                TimeCalcProperty timeCalcProperty
                        = TimeCalcProperty.forKey(timeCalcPropertyKey);

                checkBox.setText(timeCalcProperty.getDescription());

                BooleanProperty property
                        = (BooleanProperty) timeCalcConfiguration
                        .getProperty(timeCalcProperty);
                property.addListener(
                        e -> checkBox.setSelected(property.isEnabled()));
                checkBox.setSelected(property.isEnabled());
                checkBox.addItemListener(e -> {
                    property
                            .setValue(checkBox.isSelected());
                });
                String[] array = checkBox.getText().split(" : ");
                String groupName = array[0];
                if (groupName.equals("Clock") || groupName.equals("Battery")
                    || groupName.equals("Smileys")
                    || groupName.equals("Test")) {

                    checkBox.setText(array.length > 1 ? (checkBox.getText()
                            .substring(groupName.length() + 3)) : "Visible");
                    if (array.length == 1) {
                        panelInsideScrollPane
                                .add(new JSeparator(SwingConstants.VERTICAL));
                        JLabel label = new JLabel(groupName);
                        label.setFont(BIG_FONT);
                        panelInsideScrollPane.add(label);
                    }
                }
                if (
                        timeCalcProperty == TimeCalcProperty.VISIBILITY_DEFAULT
                        ||
                        timeCalcProperty == TimeCalcProperty.JOKES_VISIBLE
                ) {
                    JLabel label = new JLabel("Misc");
                    label.setFont(BIG_FONT);
                    panelInsideScrollPane.add(label);
                }
            }
            if (p instanceof JColorChooser) {
                JColorChooser colorChooser = ((JColorChooser) p);
                //jColorChooser.setMaximumSize(new Dimension(150, 25));

                String timeCalcPropertyKey =
                        (String) colorChooser.getClientProperty(
                                CLIENT_PROPERTY_KEY);
                TimeCalcProperty timeCalcProperty
                        = TimeCalcProperty.forKey(timeCalcPropertyKey);

                String currentColor = ((StringProperty) timeCalcConfiguration
                        .getProperty(timeCalcProperty)).getValue();
                String[] currentColorAsStringArray = currentColor.split(",");
                int red = Integer.valueOf(currentColorAsStringArray[0]);
                int green = Integer.valueOf(currentColorAsStringArray[1]);
                int blue = Integer.valueOf(currentColorAsStringArray[2]);
                Color color = new Color(red, green, blue);
                colorChooser.setColor(color);
                colorChooser.setMaximumSize(new Dimension(200, HEIGHT1));
                colorChooser.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Color selectedColor = colorChooser.getSelectionModel()
                                .getSelectedColor();
                        selectedColor = JColorChooser
                                .showDialog(null, "Choose a color", color);
                        if (selectedColor != null) {
                            colorChooser.setColor(selectedColor);
                            timeCalcConfiguration
                                    .getProperty(timeCalcProperty)
                                    .setValue(
                                            selectedColor.getRed() + ","
                                            + selectedColor.getGreen() + ","
                                            + selectedColor.getBlue());
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });

            }
            if (p instanceof JTextField) {
                JTextField textField = ((JTextField) p);
                if(textField.getText().startsWith("test.clock.custom.")) {
                    String key = textField.getText();
                    textField.setText("");
                    textField.putClientProperty(CLIENT_PROPERTY_KEY, key);
                    addToNextRow(new JLabel(TimeCalcProperty.forKey(key).getDescription()));
                }
                textField.setMaximumSize(new Dimension(150, 25));

                String timeCalcPropertyKey =
                        (String) textField.getClientProperty(
                                CLIENT_PROPERTY_KEY);
                TimeCalcProperty timeCalcProperty
                        = TimeCalcProperty.forKey(timeCalcPropertyKey);
                boolean isInteger = Integer.class == timeCalcProperty.getClazz();
                timeCalcConfiguration
                        .getProperty(timeCalcProperty).addListener(e -> {
                    System.out.println("JTextField was changed: " + timeCalcPropertyKey);
                    textField.setText(isInteger
                            ?
                            String.valueOf(timeCalcConfiguration
                                    .getProperty(timeCalcProperty).getValue())
                            :
                            (String) timeCalcConfiguration
                                    .getProperty(timeCalcProperty).getValue());
                });
                textField.setText(isInteger
                        ?
                        String.valueOf(timeCalcConfiguration
                                .getProperty(timeCalcProperty).getValue())
                        :
                        (String) timeCalcConfiguration
                        .getProperty(timeCalcProperty).getValue());

                textField.getDocument()
                        .addDocumentListener(new DocumentListener() {
                            public void changedUpdate(DocumentEvent e) {
                                System.out.println("changedUpdate");
                            }

                            public void removeUpdate(DocumentEvent e) {
                                System.out.println("removeUpdate");
                            }

                            public void insertUpdate(DocumentEvent e) {
                                System.out.println("insertUpdate");
                                update(e);
                            }
                            private void update(DocumentEvent e) {
                                String text = textField.getText();
                                boolean isInteger = Integer.class == timeCalcProperty.getClazz();
                                timeCalcConfiguration
                                        .getProperty(timeCalcProperty)
                                        .setValue(isInteger ? Integer.valueOf(text):
                                                text);
                            }
                        });
            }

            propertiesMap.put(TimeCalcProperty.forKey(
                    (String) p.getClientProperty(CLIENT_PROPERTY_KEY)), p);
            addToNextRow(p);
        });

        Arrays.stream(getComponents()).forEach(c -> c.getClass().getName());
        ConfigWindow configWindow = this;
        //        class ConfigThread implements Runnable {
        //            public final AtomicBoolean stopped = new AtomicBoolean();
        //
        //            public void run() {
        //                while (true) {
        //                    if (stopped.get()) {
        //                        System.out.println("stopping thread");
        //                        break;
        //                    }
        //                    if (!configWindow.visibilitySupportedColoredProperty
        //                                .isSelected()
        //                        && configWindow.visibilityDefaultProperty.isEnabled()) {
        //                        configWindow.visibilityDefaultProperty.disable();
        //                    }
        //                    if (configWindow.visibilitySupportedColoredProperty
        //                            .isSelected()
        //                        && !configWindow.visibilityDefaultProperty
        //                            .isEnabled()) {
        //                        configWindow.visibilityDefaultProperty.enable();
        //                    }
        //                }
        //                try {
        //                    Thread.sleep(100);
        //                } catch (InterruptedException e) {
        //                    e.printStackTrace();
        //                    System.out.println(e);
        //                }
        //            }
        //        }
        //        ConfigThread configThread = new ConfigThread();
        //        Thread thread = new Thread(configThread);
        //
        //        thread.start();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                //configThread.stopped.set(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }
        });
    }

    private void addToNextRow(JComponent jComponent) {
        panelInsideScrollPane.add(jComponent);
        jComponent.setBounds(SwingUtils.MARGIN, currentY, 200,
                HEIGHT1);
        panelInsideScrollPane.add(Box.createRigidArea(new Dimension(5, 10)));
        nextRow();
    }

    private void nextRow() {
        currentY = (int) (currentY + 3.0d * SwingUtils.MARGIN);
    }

    public void doEnableEverything() {
        this.enableAsMuchAsPossible.doClick();
    }

    public void doDisableAlmostEverything() {
        this.disableAsMuchAsPossible.doClick();
    }

}
