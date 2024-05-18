package org.nanoboot.utils.timecalc.swing.windows;

import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.app.TimeCalcException;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.common.SwingUtils;
import org.nanoboot.utils.timecalc.swing.controls.MouseClickedListener;
import org.nanoboot.utils.timecalc.swing.controls.TButton;
import org.nanoboot.utils.timecalc.swing.controls.TTabbedPane;
import org.nanoboot.utils.timecalc.swing.controls.TWindow;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Robert Vokac
 * @since 16.02.2024
 */
public class ConfigWindow extends TWindow {

    public static final int WIDTH1 = 600;
    public static final int HEIGHT1 = 30;
    public static final String CLIENT_PROPERTY_KEY
            = TimeCalcProperty.class.getName();
    public static final String THREE_DASHES = "---";
    private static final Font BIG_FONT = new Font("sans", Font.BOLD, 24);
    private static final String FIVE_SPACES = "     ";
    private static final String EDITABLE_ONLY_IN_DIALOG = "editableOnlyInDialog";
    public final JComboBox visibilityDefaultProperty = new JComboBox(
            Arrays.stream(Visibility.values()).map(v -> v.name()).collect(
                    Collectors.toList()).toArray());
    private final TimeCalcConfiguration timeCalcConfiguration;
    private final JPanel panelInsideScrollPaneClock;
    private final JPanel panelInsideScrollPaneBattery;
    private final JPanel panelInsideScrollPaneLife;
    private final JPanel panelInsideScrollPaneMoney;
    private final JPanel panelInsideScrollPaneSmileys;
    private final JPanel panelInsideScrollPaneProgress;
    private final JPanel panelInsideScrollPaneTest;
    private final JPanel panelInsideScrollPaneOther;
    private final int[] currentY = new int[]{SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN};
    private final int[] currentX = new int[]{SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN, SwingUtils.MARGIN};

    private final List<JComponent> propertiesList = new ArrayList<>();
    private final Map<TimeCalcProperty, JComponent> propertiesMap = new HashMap<>();
    private final TButton enableAsMuchAsPossible
            = new TButton("Enable as much as possible");
    private final TButton disableAsMuchAsPossible
            = new TButton("Disable as much as possible");
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
            = new JCheckBox(TimeCalcProperty.CLOCK_HANDS_MINUTE_VISIBLE.getKey());
    private final JCheckBox clockHandsSecondVisibleProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_HANDS_SECOND_VISIBLE.getKey());
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
            = new JCheckBox(TimeCalcProperty.CLOCK_CIRCLE_STRONG_BORDER.getKey());
    private final JColorChooser clockCircleBorderColorProperty
            = new JColorChooser(Color.BLACK);
    private final JCheckBox clockCentreCircleVisibleProperty
            = new JCheckBox(
                    TimeCalcProperty.CLOCK_CENTRE_CIRCLE_VISIBLE.getKey());
    private final JCheckBox clockCentreCircleColoredProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_CENTRE_CIRCLE_COLORED.getKey());
    private final JCheckBox clockProgressVisibleOnlyIfMouseMovingOverProperty
            = new JCheckBox(
                    TimeCalcProperty.CLOCK_PROGRESS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    private final JCheckBox clockDateVisibleOnlyIfMouseMovingOverProperty
            = new JCheckBox(
                    TimeCalcProperty.CLOCK_DATE_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    private final JCheckBox clockSmileyVisibleProperty
            = new JCheckBox(
                    TimeCalcProperty.CLOCK_SMILEY_VISIBLE
                            .getKey());
    private final JCheckBox clockPercentProgressVisibleProperty
            = new JCheckBox(
                    TimeCalcProperty.CLOCK_PERCENT_PROGRESS_VISIBLE
                            .getKey());
    private final JCheckBox clockCircleProgressVisibleProperty
            = new JCheckBox(
                    TimeCalcProperty.CLOCK_CIRCLE_PROGRESS_VISIBLE
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
    private final JCheckBox batteryLabelVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_LABEL_VISIBLE.getKey());
    private final JCheckBox batteryVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_VISIBLE.getKey());
    private final JCheckBox batteryMinuteVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_MINUTE_VISIBLE.getKey());
    private final JCheckBox batteryHourVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_HOUR_VISIBLE.getKey());
    private final JCheckBox batteryDayVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_DAY_VISIBLE.getKey());
    private final JCheckBox batteryWeekVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_WEEK_VISIBLE.getKey());
    private final JCheckBox batteryMonthVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_MONTH_VISIBLE.getKey());
    private final JCheckBox batteryYearVisibleProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_YEAR_VISIBLE.getKey());
    private final JCheckBox batteryBlinkingIfCriticalLowVisibleProperty
            = new JCheckBox(
                    TimeCalcProperty.BATTERY_BLINKING_IF_CRITICAL_LOW.getKey());
    private final JCheckBox batteryQuarterIconVisibleProperty
            = new JCheckBox(
                    TimeCalcProperty.BATTERY_QUARTER_ICON_VISIBLE.getKey());
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
    private final JTextField squareTypeProperty
            = new JTextField(TimeCalcProperty.SQUARE_TYPE.getKey());
    private final JCheckBox dotVisibleProperty
            = new JCheckBox(TimeCalcProperty.DOT_VISIBLE.getKey());
    private final JTextField dotTypeProperty
            = new JTextField(TimeCalcProperty.DOT_TYPE.getKey());
    private final JCheckBox circleVisibleProperty
            = new JCheckBox(TimeCalcProperty.CIRCLE_VISIBLE.getKey());
    private final JTextField circleTypeProperty
            = new JTextField(TimeCalcProperty.CIRCLE_TYPE.getKey());
    private final JCheckBox circleInnerCircleVisibleProperty
            = new JCheckBox(TimeCalcProperty.CIRCLE_INNER_CIRCLE_VISIBLE.getKey());
    private final JCheckBox circleOuterCircleVisibleProperty
            = new JCheckBox(TimeCalcProperty.CIRCLE_OUTER_CIRCLE_ONLY_BORDER.getKey());
    //
    private final JCheckBox barVisibleProperty
            = new JCheckBox(TimeCalcProperty.BAR_VISIBLE.getKey());
    private final JTextField barTypeProperty
            = new JTextField(TimeCalcProperty.BAR_TYPE.getKey());
    private final JCheckBox barHiddenProperty
            = new JCheckBox(TimeCalcProperty.BAR_HIDDEN.getKey());
    private final JTextField barHeightProperty
            = new JTextField(TimeCalcProperty.BAR_HEIGHT.getKey());
    //
    private final JCheckBox waterVisibleProperty
            = new JCheckBox(TimeCalcProperty.WATER_VISIBLE.getKey());
    private final JTextField waterTypeProperty
            = new JTextField(TimeCalcProperty.WATER_TYPE.getKey());
    private final JCheckBox waterHiddenProperty
            = new JCheckBox(TimeCalcProperty.WATER_HIDDEN.getKey());
    //
    private final JCheckBox colorVisibleProperty
            = new JCheckBox(TimeCalcProperty.COLOR_VISIBLE.getKey());
    private final JTextField colorTypeProperty
            = new JTextField(TimeCalcProperty.COLOR_TYPE.getKey());
    private final JCheckBox colorHiddenProperty
            = new JCheckBox(TimeCalcProperty.COLOR_HIDDEN.getKey());
    private final JTextField colorHeightProperty
            = new JTextField(TimeCalcProperty.COLOR_HEIGHT.getKey());
    //
    private final JCheckBox swingVisibleProperty
            = new JCheckBox(TimeCalcProperty.SWING_VISIBLE.getKey());
    private final JTextField swingTypeProperty
            = new JTextField(TimeCalcProperty.SWING_TYPE.getKey());
    private final JCheckBox swingQuarterIconVisibleProperty
            = new JCheckBox(TimeCalcProperty.SWING_QUARTER_ICON_VISIBLE.getKey());
    private final JCheckBox walkingHumanVisibleProperty
            = new JCheckBox(TimeCalcProperty.WALKING_HUMAN_VISIBLE.getKey());
    private final JTextField walkingHumanTypeProperty
            = new JTextField(TimeCalcProperty.WALKING_HUMAN_TYPE.getKey());
    public final JCheckBox lifeVisibleProperty
            = new JCheckBox(TimeCalcProperty.LIFE_VISIBLE.getKey());
    public final JTextField lifeTypeProperty
            = new JTextField(TimeCalcProperty.LIFE_TYPE.getKey());
    public final JTextField lifeBirthDateProperty
            = new JTextField(TimeCalcProperty.LIFE_BIRTH_DATE.getKey());
    public final JCheckBox moneyVisibleProperty
            = new JCheckBox(TimeCalcProperty.MONEY_VISIBLE.getKey());
    public final JTextField moneyTypeProperty
            = new JTextField(TimeCalcProperty.MONEY_TYPE.getKey());
    public final JTextField moneyPerMonthProperty
            = new JTextField(TimeCalcProperty.MONEY_PER_MONTH.getKey());
    public final JTextField moneyCurrencyProperty
            = new JTextField(TimeCalcProperty.MONEY_CURRENCY.getKey());
    public final JCheckBox weatherVisibleProperty
            = new JCheckBox(TimeCalcProperty.WEATHER_VISIBLE.getKey());
    private final JTextField mainWindowCustomTitleProperty
            = new JTextField();
    private final JTextField profileNameProperty
            = new JTextField();
    public final JTextField activityNeededFlagsProperty
            = new JTextField(TimeCalcProperty.ACTIVITY_NEEDED_FLAGS.getKey());
    public final JTextField speedProperty
            = new JTextField(TimeCalcProperty.SPEED.getKey());
    public final JCheckBox speedNegativeProperty
            = new JCheckBox(TimeCalcProperty.SPEED_NEGATIVE.getKey());
    public final JCheckBox speedFloatingProperty
            = new JCheckBox(TimeCalcProperty.SPEED_FLOATING.getKey());
    //
    private final JCheckBox fuelVisibleProperty
            = new JCheckBox(TimeCalcProperty.FUEL_VISIBLE.getKey());
    private final JTextField fuelTypeProperty =
            new JTextField(TimeCalcProperty.FUEL_TYPE.getKey());
    private final JCheckBox fuelHiddenProperty
            = new JCheckBox(TimeCalcProperty.FUEL_HIDDEN.getKey());
    private final JCheckBox fuelIconVisibleProperty
            = new JCheckBox(TimeCalcProperty.FUEL_ICON_VISIBLE.getKey());
    //
    private final JCheckBox rotationVisibleProperty
            = new JCheckBox(TimeCalcProperty.ROTATION_VISIBLE.getKey());
    private final JTextField rotationTypeProperty =
            new JTextField(TimeCalcProperty.ROTATION_TYPE.getKey());
    private final JCheckBox rotationHiddenProperty
            = new JCheckBox(TimeCalcProperty.ROTATION_HIDDEN.getKey());
    //
    public final JCheckBox clockHiddenProperty
            = new JCheckBox(TimeCalcProperty.CLOCK_HIDDEN.getKey());
    public final JCheckBox batteryMinuteHiddenProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_MINUTE_HIDDEN.getKey());
    public final JCheckBox batteryHourHiddenProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_HOUR_HIDDEN.getKey());
    public final JCheckBox batteryDayHiddenProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_DAY_HIDDEN.getKey());
    public final JCheckBox batteryWeekHiddenProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_WEEK_HIDDEN.getKey());
    public final JCheckBox batteryMonthHiddenProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_MONTH_HIDDEN.getKey());
    public final JCheckBox batteryYearHiddenProperty
            = new JCheckBox(TimeCalcProperty.BATTERY_YEAR_HIDDEN.getKey());
    public final JCheckBox squareHiddenProperty
            = new JCheckBox(TimeCalcProperty.SQUARE_HIDDEN.getKey());
    public final JCheckBox circleHiddenProperty
            = new JCheckBox(TimeCalcProperty.CIRCLE_HIDDEN.getKey());
    public final JCheckBox walkingHumanHiddenProperty
            = new JCheckBox(TimeCalcProperty.WALKING_HUMAN_HIDDEN.getKey());
    public final JCheckBox swingHiddenProperty
            = new JCheckBox(TimeCalcProperty.SWING_HIDDEN.getKey());
    public final JCheckBox lifeHiddenProperty
            = new JCheckBox(TimeCalcProperty.LIFE_HIDDEN.getKey());
    public final JCheckBox moneyHiddenProperty
            = new JCheckBox(TimeCalcProperty.MONEY_HIDDEN.getKey());
    public final JCheckBox weatherHiddenProperty
            = new JCheckBox(TimeCalcProperty.WEATHER_HIDDEN.getKey());
    public final JCheckBox dotHiddenProperty
            = new JCheckBox(TimeCalcProperty.DOT_HIDDEN.getKey());
    public final JCheckBox typeVisibleProperty
            = new JCheckBox(TimeCalcProperty.TYPE_VISIBLE.getKey());

    //
    private final JCheckBox testEnabledProperty
            = new JCheckBox(TimeCalcProperty.TEST_ENABLED.getKey());
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
    private final TTabbedPane tp;

    public ConfigWindow(TimeCalcConfiguration timeCalcConfiguration) {
        this.timeCalcConfiguration = timeCalcConfiguration;
        setTitle("Configuration");
        this.setSize(780, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setMaximumSize(new Dimension(750,
                getHeight() - 6 * SwingUtils.MARGIN));
        this.tp = new TTabbedPane();
        //tp.setBackground(Color.red);
        this.panelInsideScrollPaneClock = new JPanel();
        this.panelInsideScrollPaneBattery = new JPanel();
        this.panelInsideScrollPaneLife = new JPanel();
        this.panelInsideScrollPaneMoney = new JPanel();
        this.panelInsideScrollPaneSmileys = new JPanel();
        this.panelInsideScrollPaneProgress = new JPanel();
        this.panelInsideScrollPaneTest = new JPanel();
        this.panelInsideScrollPaneOther = new JPanel();

        List<JPanel> panelsInsideScrollPane = Stream.of(
                panelInsideScrollPaneClock,
                panelInsideScrollPaneBattery,
                panelInsideScrollPaneLife,
                panelInsideScrollPaneMoney,
                panelInsideScrollPaneSmileys,
                panelInsideScrollPaneProgress,
                panelInsideScrollPaneTest,
                panelInsideScrollPaneOther).collect(Collectors.toList());
        panelsInsideScrollPane.forEach(p -> {
            final BoxLayout boxLayout = new BoxLayout(p, BoxLayout.Y_AXIS);
            p.setLayout(boxLayout);
            p.setAlignmentX(LEFT_ALIGNMENT);
            //p.setMinimumSize(new Dimension(300, 400));
            p.setMaximumSize(new Dimension(300, 400));
            //p.setBackground(Color.blue);
        });

        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(LEFT_ALIGNMENT);

        JScrollPane scrollPaneClock = new JScrollPane(panelInsideScrollPaneClock);
        JScrollPane scrollPaneBattery = new JScrollPane(panelInsideScrollPaneBattery);
        JScrollPane scrollPaneLife = new JScrollPane(panelInsideScrollPaneLife);
        JScrollPane scrollPaneMoney = new JScrollPane(panelInsideScrollPaneMoney);
        JScrollPane scrollPaneSmileys = new JScrollPane(panelInsideScrollPaneSmileys);
        JScrollPane scrollPaneProgress = new JScrollPane(panelInsideScrollPaneProgress);
        JScrollPane scrollPaneTest = new JScrollPane(panelInsideScrollPaneTest);
        JScrollPane scrollPaneOther = new JScrollPane(panelInsideScrollPaneOther);

        List<JScrollPane> scrollPanes = Stream.of(
                scrollPaneClock,
                scrollPaneBattery,
                scrollPaneLife,
                scrollPaneMoney,
                scrollPaneSmileys,
                scrollPaneProgress,
                scrollPaneTest,
                scrollPaneOther
        ).collect(Collectors.toList());

        tp.add("Clock", scrollPaneClock);
        tp.add("Battery", scrollPaneBattery);
        tp.add("Life", scrollPaneLife);
        tp.add("Money", scrollPaneMoney);
        tp.add("Smileys", scrollPaneSmileys);
        tp.add("Progress", scrollPaneProgress);
        tp.add("Test", scrollPaneTest);
        tp.add("Other", scrollPaneOther);
        scrollPanes.forEach(s
                -> {
            s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            s.setPreferredSize(
                    new Dimension(720, getHeight() - 100));
            s.setWheelScrollingEnabled(true);
            s.setBorder(null);
//            s.setBackground(Color.green);
        });

        add(mainPanel);
        //mainPanel.setBackground(Color.ORANGE);
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

        scrollPanes.stream().forEach(s -> s.setBounds(10, 10, Integer.MAX_VALUE, Integer.MAX_VALUE));

        mainPanel.add(tp);

        for (boolean enable : new boolean[]{true, false}) {
            TButton button
                    = enable ? enableAsMuchAsPossible : disableAsMuchAsPossible;

            button.addActionListener(e -> {
                visibilityDefaultProperty
                        .setSelectedItem(enable ? Visibility.STRONGLY_COLORED.name() : Visibility.GRAY.name());
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
                clockCentreCircleColoredProperty.setSelected(!enable);
                clockProgressVisibleOnlyIfMouseMovingOverProperty
                        .setSelected(!enable);
                clockDateVisibleOnlyIfMouseMovingOverProperty
                        .setSelected(false);
                clockSmileyVisibleProperty.setSelected(enable);
                clockPercentProgressVisibleProperty.setSelected(enable);
                clockCircleProgressVisibleProperty.setSelected(enable);
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
                batteryQuarterIconVisibleProperty.setSelected(enable);
                //
                jokesVisibleProperty.setSelected(true);
                commandsVisibleProperty.setSelected(enable);
                notificationsVisibleProperty.setSelected(enable);
                smileysVisibleProperty.setSelected(enable);
                smileysColoredProperty.setSelected(enable);
                smileysVisibleOnlyIfMouseMovingOverProperty
                        .setSelected(!enable);
                squareVisibleProperty.setSelected(enable);
                dotVisibleProperty.setSelected(enable);
                lifeVisibleProperty.setSelected(enable);
                moneyVisibleProperty.setSelected(enable);
                if(enable && moneyPerMonthProperty.getText().isEmpty()) {
                    moneyPerMonthProperty.setText("1000000");
                }
                if(enable && moneyCurrencyProperty.getText().isEmpty()) {
                    moneyPerMonthProperty.setText("$TT");
                }
                weatherVisibleProperty.setSelected(enable);
                circleVisibleProperty.setSelected(enable);
                circleInnerCircleVisibleProperty.setSelected(enable);
                circleOuterCircleVisibleProperty.setSelected(!enable);
                //
                barVisibleProperty.setSelected(enable);
                barHeightProperty.setText("50");
                waterVisibleProperty.setSelected(enable);
                colorVisibleProperty.setSelected(enable);
                colorHeightProperty.setText("50");
                swingVisibleProperty.setSelected(enable);
                swingQuarterIconVisibleProperty.setSelected(enable);
                walkingHumanVisibleProperty.setSelected(enable);
                fuelVisibleProperty.setSelected(enable);
                fuelIconVisibleProperty.setSelected(enable);
                rotationVisibleProperty.setSelected(enable);
                typeVisibleProperty.setSelected(enable);
//                clockHiddenProperty.setSelected(!enable);
//                batteryMinuteHiddenProperty.setSelected(!enable);
//                batteryHourHiddenProperty.setSelected(!enable);
//                batteryDayHiddenProperty.setSelected(!enable);
//                batteryWeekHiddenProperty.setSelected(!enable);
//                batteryMonthHiddenProperty.setSelected(!enable);
//                batteryYearHiddenProperty.setSelected(!enable);
//                squareHiddenProperty.setSelected(!enable);
//                circleHiddenProperty.setSelected(!enable);
//                walkingHumanHiddenProperty.setSelected(!enable);
//                swingHiddenProperty.setSelected(!enable);
//                lifeHiddenProperty.setSelected(!enable);
//                moneyHiddenProperty.setSelected(!enable);
//                weatherHiddenProperty.setSelected(!enable);
//                dotHiddenProperty.setSelected(!enable);
                if (enable) {
                    speedProperty.setText("0");
                }
                if (!enable) {
                    speedProperty.setText(String.valueOf(Integer.MAX_VALUE));
                    speedFloatingProperty.setSelected(false);
                }
                MainWindow.hideShowFormsCheckBox.setSelected(enable);
            });
        }

        propertiesList.addAll(Arrays.asList(
                clockVisibleProperty,
                clockHiddenProperty,
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
                clockCentreCircleColoredProperty,
                clockProgressVisibleOnlyIfMouseMovingOverProperty,
                clockDateVisibleOnlyIfMouseMovingOverProperty,
                clockSmileyVisibleProperty,
                clockPercentProgressVisibleProperty,
                clockCircleProgressVisibleProperty,
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
                batteryMinuteHiddenProperty,
                batteryHourHiddenProperty,
                batteryDayHiddenProperty,
                batteryWeekHiddenProperty,
                batteryMonthHiddenProperty,
                batteryYearHiddenProperty,
                batteryBlinkingIfCriticalLowVisibleProperty,
                batteryQuarterIconVisibleProperty,
                //
                smileysVisibleProperty,smileysVisibleOnlyIfMouseMovingOverProperty,smileysColoredProperty,
                testEnabledProperty,
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
                squareVisibleProperty,squareHiddenProperty,squareTypeProperty,
                circleVisibleProperty,circleHiddenProperty,circleTypeProperty,circleInnerCircleVisibleProperty,circleOuterCircleVisibleProperty,
                barVisibleProperty, barHiddenProperty, barTypeProperty, barHeightProperty,
                waterVisibleProperty, waterHiddenProperty, waterTypeProperty,
                colorVisibleProperty, colorHiddenProperty, colorTypeProperty, colorHeightProperty,
                dotVisibleProperty,dotHiddenProperty,dotTypeProperty,
                fuelVisibleProperty,fuelTypeProperty,fuelHiddenProperty,fuelIconVisibleProperty,
                rotationVisibleProperty, rotationTypeProperty, rotationHiddenProperty,
                swingVisibleProperty,swingHiddenProperty,swingTypeProperty,swingQuarterIconVisibleProperty,
                walkingHumanVisibleProperty,walkingHumanHiddenProperty,walkingHumanTypeProperty,
                lifeVisibleProperty,lifeHiddenProperty,lifeTypeProperty,lifeBirthDateProperty,
                moneyVisibleProperty,moneyHiddenProperty,moneyTypeProperty,moneyPerMonthProperty,moneyCurrencyProperty,
                weatherVisibleProperty,weatherHiddenProperty,
                mainWindowCustomTitleProperty,
                profileNameProperty,
                activityNeededFlagsProperty,
                speedProperty,
                speedNegativeProperty,
                speedFloatingProperty,
                typeVisibleProperty,
                visibilityDefaultProperty,
                visibilitySupportedColoredProperty));
        //
        //panelInsideScrollPane
        propertiesList.stream().forEach(p -> {
            p.setAlignmentX(LEFT_ALIGNMENT);
            if (p == visibilityDefaultProperty) {
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.VISIBILITY_DEFAULT.getKey());
                final JLabel jLabel = new JLabel(
                        TimeCalcProperty.VISIBILITY_DEFAULT.getDescription());
                jLabel.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.VISIBILITY_DEFAULT.getKey());
                addToNextRow(jLabel);
            }
            if (p == clockCircleBorderColorProperty) {
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR.getKey());
                JComponent label = new JLabel(
                        TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR
                                .getDescription().replace("Clock : ", ""));
                label.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.CLOCK_CIRCLE_BORDER_COLOR.getKey());
                addToNextRow(label);
            }
            if (p == mainWindowCustomTitleProperty) {
//                final JLabel jLabel = new JLabel(
//                        TimeCalcProperty.MAIN_WINDOW_CUSTOM_TITLE
//                                .getDescription());
//                jLabel.putClientProperty(CLIENT_PROPERTY_KEY,
//                        TimeCalcProperty.MAIN_WINDOW_CUSTOM_TITLE.getKey());
//                addToNextRow(jLabel);
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.MAIN_WINDOW_CUSTOM_TITLE.getKey());
            }
            if (p == profileNameProperty) {
//                final JLabel jLabel = new JLabel(
//                        TimeCalcProperty.PROFILE_NAME.getDescription());
//                jLabel.putClientProperty(CLIENT_PROPERTY_KEY,
//                        TimeCalcProperty.PROFILE_NAME.getKey());
//                addToNextRow(jLabel);
                p.putClientProperty(CLIENT_PROPERTY_KEY,
                        TimeCalcProperty.PROFILE_NAME.getKey());
            }
            if (p == squareTypeProperty) {
                addLabelToNextRow(TimeCalcProperty.SQUARE_TYPE);
            }
            if (p == circleTypeProperty) {
                addLabelToNextRow(TimeCalcProperty.CIRCLE_TYPE);
            }
            if (p == walkingHumanTypeProperty) {
                addLabelToNextRow(TimeCalcProperty.WALKING_HUMAN_TYPE);
            }
            if (p == swingTypeProperty) {
                addLabelToNextRow(TimeCalcProperty.SWING_TYPE);
            }
            if (p == lifeTypeProperty) {
                addLabelToNextRow(TimeCalcProperty.LIFE_TYPE);
            }
            if (p == lifeBirthDateProperty) {
                addLabelToNextRow(TimeCalcProperty.LIFE_BIRTH_DATE);
            }
            if (p == activityNeededFlagsProperty) {
                addLabelToNextRow(TimeCalcProperty.ACTIVITY_NEEDED_FLAGS);
            }
            if (p == moneyTypeProperty) {
                addLabelToNextRow(TimeCalcProperty.MONEY_TYPE);
            }
            if (p == moneyPerMonthProperty) {
                addLabelToNextRow(TimeCalcProperty.MONEY_PER_MONTH);
            }
            if (p == moneyCurrencyProperty) {
                addLabelToNextRow(TimeCalcProperty.MONEY_CURRENCY);
            }

            if (p == testClockCustomYearProperty) {
                JLabel label = new JLabel("Test");
                label.setFont(BIG_FONT);
                addToNextRow(label);
            }

            if (p instanceof JComboBox) {
                JComboBox jComboBox = ((JComboBox) p);
                jComboBox.setMaximumSize(new Dimension(150, 25));

                String timeCalcPropertyKey
                        = (String) jComboBox.getClientProperty(
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

                //System.out.println(((JCheckBox) p).getText());
                //System.out.println(timeCalcProperty);
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
                        || groupName.equals("Test")
                    || groupName.equals("Money")
                    || groupName.equals("Life")) {

                    checkBox.setText(array.length > 1 ? (checkBox.getText()
                            .substring(groupName.length() + 3)) : "Visible");
//                    if (array.length == 1) {
//                        panelInsideScrollPane
//                                .add(new JSeparator(SwingConstants.VERTICAL));
//                        JLabel label = new JLabel(groupName);
//                        label.setFont(BIG_FONT);
//                        panelInsideScrollPane.add(label);
//                    }
                }
//                if (timeCalcProperty == TimeCalcProperty.VISIBILITY_DEFAULT
//                        || timeCalcProperty == TimeCalcProperty.JOKES_VISIBLE) {
//                    JLabel label = new JLabel("Misc");
//                    label.setFont(BIG_FONT);
//                    panelInsideScrollPane.add(label);
//                }
            }
            if (p instanceof JColorChooser) {
                JColorChooser colorChooser = ((JColorChooser) p);
                //jColorChooser.setMaximumSize(new Dimension(150, 25));

                String timeCalcPropertyKey
                        = (String) colorChooser.getClientProperty(
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
                if (textField.getText().startsWith("test.clock.custom.")) {
                    String key = textField.getText();
                    textField.setText("");
                    textField.putClientProperty(CLIENT_PROPERTY_KEY, key);
//                    JComponent label = new JLabel(TimeCalcProperty.forKey(key).getDescription());
//                    label.putClientProperty(CLIENT_PROPERTY_KEY, key);
//                    addToNextRow(label);
                }
                textField.setMaximumSize(new Dimension(400, 25));
                textField.setMinimumSize(new Dimension(100, 25));

                String timeCalcPropertyKey
                        = (String) textField.getClientProperty(
                                CLIENT_PROPERTY_KEY);
                if (timeCalcPropertyKey == null) {
                    timeCalcPropertyKey = textField.getText();
                    textField.putClientProperty(CLIENT_PROPERTY_KEY, timeCalcPropertyKey);
                }
                TimeCalcProperty timeCalcProperty
                        = TimeCalcProperty.forKey(timeCalcPropertyKey);
                boolean isInteger = Integer.class == timeCalcProperty.getClazz();

                {
                    textField.setEditable(false);
                    textField.setBackground(Color.WHITE);
                    textField.putClientProperty(
                            EDITABLE_ONLY_IN_DIALOG, "");
                    textField
                            .addMouseListener((MouseClickedListener) f -> {

                                String result
                                        = (String) JOptionPane.showInputDialog(
                                                null,
                                                "Select new value",
                                                "New value",
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                null,
                                                textField
                                                        .getText()
                                        );
                                if (result != null) {
                                    if (timeCalcProperty.name().contains("TYPE")) {
                                        try {
                                            WidgetType widgetType
                                                    = WidgetType.valueOf(result.toUpperCase(
                                                            Locale.ROOT));
                                        } catch (Exception e) {
                                            throw new TimeCalcException("Invalid format. Only these values are allowed: " + Arrays
                                                    .stream(WidgetType.values()).map(WidgetType::name).map(String::toLowerCase).collect(
                                                    Collectors.joining(", ")));
                                        }
                                    }
                                    textField.setText(result);
                                    timeCalcConfiguration.getProperty(timeCalcProperty).setValue(timeCalcProperty.getClazz() == Integer.class ? Integer.valueOf(result) : result);
                                }
                            });

                }
                timeCalcConfiguration
                        .getProperty(timeCalcProperty).addListener(e -> {

                    textField.setText(isInteger
                            ? String.valueOf(timeCalcConfiguration
                                    .getProperty(timeCalcProperty).getValue())
                            : (String) timeCalcConfiguration
                                    .getProperty(timeCalcProperty).getValue());
                });
                textField.setText(isInteger
                        ? String.valueOf(timeCalcConfiguration
                                .getProperty(timeCalcProperty).getValue())
                        : (String) timeCalcConfiguration
                                .getProperty(timeCalcProperty).getValue());

                textField.getDocument()
                        .addDocumentListener(new DocumentListener() {
                            public void changedUpdate(DocumentEvent e) {
                            }

                            public void removeUpdate(DocumentEvent e) {
                            }

                            public void insertUpdate(DocumentEvent e) {
                                update(e);
                            }

                            private void update(DocumentEvent e) {
                                if (textField.getClientProperty(EDITABLE_ONLY_IN_DIALOG) != null) {
                                    return;
                                }
                                String text = textField.getText();
                                boolean isInteger = Integer.class == timeCalcProperty.getClazz();
                                timeCalcConfiguration
                                        .getProperty(timeCalcProperty)
                                        .setValue(isInteger ? Integer.valueOf(text)
                                                : text);
                            }
                        });
            }

            if(timeCalcConfiguration.speedProperty.getValue() == Integer.MIN_VALUE) {
                timeCalcConfiguration.speedProperty.setValue(Integer.MAX_VALUE);
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

    private void addLabelToNextRow(TimeCalcProperty timeCalcProperty) {
//        final JLabel jLabel = new JLabel(
//                timeCalcProperty.getDescription());
//        jLabel.putClientProperty(CLIENT_PROPERTY_KEY,timeCalcProperty.getKey());
//        addToNextRow(jLabel, false);

    }

    private void addToNextRow(JComponent jComponent) {
        addToNextRow(jComponent, true);
    }

    private void addToNextRow(JComponent jComponent, boolean nextRow) {
        int index = 7;
        String key = (String) jComponent.getClientProperty(CLIENT_PROPERTY_KEY);
        if (key == null) {
            //nothing to do
            return;
        }
        if (key.startsWith("clock")) {
            index = 0;
        }
        if (key.startsWith("battery")) {
            index = 1;
        }
        if (key.startsWith("life")) {
            index = 2;
        }
        if (key.startsWith("money")) {
            index = 3;
        }
        if (key.startsWith("smileys")) {
            index = 4;
        }
        if (key.startsWith("test")) {
            index = 5;
        }

        if (
                key.startsWith("square.") ||
                key.startsWith("circle.") ||
                key.startsWith("dot.") ||
                key.startsWith("swing.") ||
                key.startsWith("walking-human.") ||
                key.startsWith("fuel.") ||
                key.startsWith("rotation.") ||
                key.startsWith("bar.") ||
                key.startsWith("color.") ||
                key.startsWith("water.")
                ) {
            index = 6;
        }

        JPanel panel = null;
        switch (index) {
            case 0:
                panel = panelInsideScrollPaneClock;
                break;
            case 1:
                panel = panelInsideScrollPaneBattery;
                break;
            case 2:
                panel = panelInsideScrollPaneLife;
                break;
            case 3:
                panel = panelInsideScrollPaneMoney;
                break;
            case 4:
                panel = panelInsideScrollPaneSmileys;
                break;
            case 5:
                panel = panelInsideScrollPaneTest;
                break;
            case 6:
                panel = panelInsideScrollPaneProgress;
                break;
            default:
                panel = panelInsideScrollPaneOther;
        }

        if(index == 6 && !key.startsWith("test.") && !key.startsWith("square.") && key.endsWith(".visible") && !key.endsWith(".quarter-icon.visible")) {
            panel.add(Box.createRigidArea(new Dimension(5, 20)));
        }

        if (jComponent instanceof JTextField) {
            JPanel p = new JPanel();

            JLabel label = new JLabel(TimeCalcProperty.forKey(key).getDescription() + ": ");
            p.add(label);
            p.add(jComponent);
            label.setBounds(10, 0, 200, 25);
            if (label.getText().startsWith("Life")) {
                label.setText(label.getText().substring("Life".length() + 3));
            }
            if (label.getText().startsWith("Money")) {
                label.setText(label.getText().substring("Money".length() + 3));
            }
            if (label.getText().startsWith("Test")) {
                label.setText(label.getText().substring("Test".length() + 3));
            }


            jComponent.setBounds(220, 0, 200, 25);
            LayoutManager flowLayout = new FlowLayout(FlowLayout.LEFT);
            p.setLayout(flowLayout);
            p.setAlignmentX(LEFT_ALIGNMENT);

            label.setPreferredSize(new Dimension(key.startsWith("test.") ? 240 : 200, 25));
            jComponent.setPreferredSize(new Dimension(200, 25));

            p.setMaximumSize(new Dimension(600, 30));
            panel.add(p);

            //jComponent.setMinimumSize(new Dimension(60, 30));
            //label.setBounds(0,0,100, 30);
        } else {
            panel.add(jComponent);
        }

        jComponent.setBounds(currentX[index], currentY[index], 200,
                HEIGHT1);
        panel.add(Box.createRigidArea(new Dimension(5, 4)));

        if (nextRow) {
            nextRow(index);

        } else {
            currentX[index] = currentX[index] + SwingUtils.MARGIN + jComponent.getWidth();
        }
    }

    private void nextRow(int index) {
        currentY[index] = (int) (currentY[index] + 3.0d * SwingUtils.MARGIN);
        currentX[index] = SwingUtils.MARGIN;
    }

    public void doEnableEverything() {
        this.enableAsMuchAsPossible.doClick();
    }

    public void doDisableAlmostEverything() {
        this.disableAsMuchAsPossible.doClick();
    }

}
