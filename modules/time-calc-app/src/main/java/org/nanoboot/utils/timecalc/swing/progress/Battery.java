package org.nanoboot.utils.timecalc.swing.progress;

import lombok.Getter;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.common.Utils;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

public class Battery extends Widget {

    public static final Color LOW_WEAKLY_COLORED = new Color(253, 130, 130);
    public static final Color MEDIUM_WEAKLY_COLORED = new Color(255, 204, 153);
    public static final Color HIGH_WEAKLY_COLORED = new Color(204, 255, 204);
    public static final Color HIGHEST_WEAKLY_COLORED = new Color(153, 255, 153);
    public static final Color LOW_STRONGLY_COLORED = Color.red;
    public static final Color MEDIUM_STRONGLY_COLORED = Color.ORANGE;
    public static final Color HIGH_STRONGLY_COLORED = new Color(158, 227, 158);
    public static final Color HIGHEST_STRONGLY_COLORED = Color.green;
    public static final double CRITICAL_LOW_ENERGY = 0.10;
    public static final double LOW_ENERGY = 0.20;
    public static final double HIGH_ENERGY = 0.75;
    public static final double VERY_HIGH_ENERGY = 0.9;
    public static final Color LIGHT_RED = new Color(
            229, 168, 168);
    public static final Color ULTRA_LIGHT_RED = new Color(
            238, 196, 196);
    public static final String CHARCHING = "⚡";
    public static final String COLON = ":";
    @Getter
    private final String name;
    private final double[] randomDoubles
            = new double[]{1d, 1d, 1d, 1d, 1d, 1d, 1};
    private final BooleanProperty blinking = new BooleanProperty("blinking");

    public static Color getColourForProgress(double donePercent,
            Visibility visibility, boolean mouseOver) {
        if (visibility.isGray()) {
            return Utils.ULTRA_LIGHT_GRAY;
        }

        boolean stronglyColored = visibility.isStronglyColored() || mouseOver;
        Color low = stronglyColored ? LOW_STRONGLY_COLORED : LOW_WEAKLY_COLORED;
        Color medium = stronglyColored ? MEDIUM_STRONGLY_COLORED :
                MEDIUM_WEAKLY_COLORED;
        Color high = stronglyColored ? HIGH_STRONGLY_COLORED : HIGH_WEAKLY_COLORED;
        Color highest = stronglyColored ? HIGHEST_STRONGLY_COLORED :
                HIGHEST_WEAKLY_COLORED;
        Color result =  donePercent < LOW_ENERGY ? low
                    : (donePercent < HIGH_ENERGY
                    ? medium
                    : (donePercent < VERY_HIGH_ENERGY
                    ? high
                    : highest));

        return result;

//        if(donePercent < CRITICAL_LOW_ENERGY) {
//            return result;
//        }
//        if(donePercent > VERY_HIGH_ENERGY) {
//            return result;
//        }
//        double transition = 0d;
//        if (donePercent < LOW_ENERGY) {
//            transition = (donePercent - CRITICAL_LOW_ENERGY) / (LOW_ENERGY
//                                                                - CRITICAL_LOW_ENERGY);
//        } else {
//            if (donePercent < HIGH_ENERGY) {
//                transition =
//                        (donePercent - LOW_ENERGY) / (HIGH_ENERGY - LOW_ENERGY);
//            } else {
//                if (donePercent < VERY_HIGH_ENERGY) {
//                    transition = (donePercent - HIGH_ENERGY) / (VERY_HIGH_ENERGY
//                                                                - HIGH_ENERGY);
//                }
//            }
//        }
//
//        return getColorBetween(result, result == low ? medium : (result == medium ? high : highest), transition, donePercent);
    }
    private static Map<String, Color> colorCache = new HashMap<>();

//    private static Color getColorBetween(Color color1, Color color2, double transition, double progress) {
//        if(color1.equals(color2)) {
//            return color1;
//        }
//        int red1 = color1.getRed();
//        int green1 = color1.getGreen();
//        int blue1 = color1.getBlue();
//        int red2 = color2.getRed();
//        int green2 = color2.getGreen();
//        int blue2 = color2.getBlue();
//        int redDiff = Math.abs(red2-red1);
//        int greenDiff = Math.abs(green2-green1);
//        int blueDiff = Math.abs(blue2-blue1);
//        int red = (int) (Math.min(red1, red2) + ((double)redDiff) * transition);
//        int green  = (int) (Math.min(green1, green2) + ((double)greenDiff) * transition);
//        int blue = (int) (Math.min(blue1, blue2) + ((double)blueDiff) * transition);
//        String key = red + COLON + green + COLON + blue;
//
////        try {new Color(red, green, blue);} catch (Exception e) {
////            System.out.println(key);
////            System.out.println("\n\n\nred1=" + red1);
////            System.out.println("green1=" + green1);
////            System.out.println("blue1=" + blue1);
////            System.out.println("red2=" + red2);
////            System.out.println("green2=" + green2);
////            System.out.println("blue2=" + blue2);
////            System.out.println("redDiff=" + redDiff);
////            System.out.println("greenDiff=" + greenDiff);
////            System.out.println("blueDiff=" + blueDiff);
////            System.out.println("red=" + red);
////            System.out.println("green=" + green);
////            System.out.println("blue=" + blue);
////            System.out.println("transition=" + transition);
////            System.out.println("progress=" + progress);
////
////            return Color.LIGHT_GRAY;
////        }
//
//        if(!colorCache.containsKey(key)) {
//            colorCache.put(key, new Color(red, green, blue));
//        }
//        return colorCache.get(key);
//
//    }

    public BooleanProperty wavesVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_WAVES_VISIBLE
                    .getKey(), true);
    public BooleanProperty circleProgressVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_CIRCLE_PROGRESS_VISIBLE
                    .getKey(), true);
    public BooleanProperty percentProgressVisibleProperty = new BooleanProperty(
            TimeCalcProperty.BATTERY_PERCENT_PROGRESS_VISIBLE
                    .getKey(), true);
    public BooleanProperty chargingCharacterVisibleProperty
            = new BooleanProperty(
                    TimeCalcProperty.BATTERY_CHARGING_CHARACTER_VISIBLE
                            .getKey(), true);
    public BooleanProperty nameVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_NAME_VISIBLE
                    .getKey(), true);
    public BooleanProperty labelVisibleProperty
            = new BooleanProperty(TimeCalcProperty.BATTERY_LABEL_VISIBLE
                    .getKey(), true);
    public BooleanProperty blinkingIfCriticalLowVisibleProperty
            = new BooleanProperty(
                    TimeCalcProperty.BATTERY_BLINKING_IF_CRITICAL_LOW
                            .getKey(), true);
    private long tmpNanoTime = 0l;
    private int totalHeight = 0;
    private int totalWidth;
    private String label = null;

    protected Battery(String name) {
        this.name = name;
        setPreferredSize(new Dimension(40, 100));
    }

    protected Battery(String name, int i, int y, int height) {
        this(name);
        setBounds(i, y, height);
    }

    @Override
    public void paintWidget(Graphics g) {
        if (totalHeight == 0) {
            this.totalHeight = (int) (this.getHeight() / 10d * 7d);
            this.totalWidth = this.getWidth();
        }
        if (blinkingIfCriticalLowVisibleProperty.isEnabled()) {
            if (donePercent > 0 && donePercent <= CRITICAL_LOW_ENERGY
                    && (System.nanoTime() - tmpNanoTime) > (1000000000l) / 2l) {
                blinking.flip();
                tmpNanoTime = System.nanoTime();
            }
            if (donePercent > CRITICAL_LOW_ENERGY && blinking.isEnabled()) {
                blinking.disable();
            }
            if (donePercent <= 0 && blinking.getValue()) {
                blinking.setValue(false);
            }
        } else {
            if (blinking.isEnabled()) {
                blinking.disable();
            }
        }

        Graphics2D brush = (Graphics2D) g;

        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ? Color.YELLOW
                : FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!visibility.isGray()) {
            brush.fillRect(1, 1, totalWidth, totalHeight);
        }
        brush.setColor(getColourForProgress(donePercent, visibility, mouseOver));

        if (blinking.getValue()) {
            brush.setColor(BACKGROUND_COLOR);
        }
        int doneHeight = (int) (totalHeight * donePercent);
        int intX = 1;
        int todoHeight = totalHeight - doneHeight;
        double surfacePower
                = 1;//donePercent < 0.5 ? 0.5 : donePercent;// (donePercent * 100 - ((int)(donePercent * 100)));
        int waterSurfaceHeight
                = (int) (4 * surfacePower);//2 + (int) (Math.random() * 3);
        if (waterSurfaceHeight <= 2 || wavesVisibleProperty.isDisabled() || !changedInTheLastXMilliseconds(1000)) {
            waterSurfaceHeight = 0;
        }

        brush.fillRect(intX + 1,
                doneHeight < waterSurfaceHeight || donePercent >= 1
                        ? todoHeight : todoHeight + waterSurfaceHeight,
                totalWidth - 3,
                doneHeight < waterSurfaceHeight || donePercent >= 1
                        ? doneHeight : doneHeight - waterSurfaceHeight + 1);
        int pointCount = 8;
        if (doneHeight >= waterSurfaceHeight
                && donePercent < 1) {// && todoHeight > waterSurfaceHeight) {
            //g2d.fillArc(intX, intY, width_, intHeight - waterSurfaceHeight, 30, 60);

            brush.fillPolygon(
                    new int[]{intX,
                        (int) (intX + totalWidth / pointCount * 0.5),
                        intX + totalWidth / pointCount * 3,
                        intX + totalWidth / pointCount * 4,
                        intX + totalWidth / pointCount * 5,
                        intX + totalWidth / pointCount * 6,
                        intX + totalWidth / pointCount * 7,
                        intX + totalWidth / pointCount * 8},
                    new int[]{todoHeight + (waterSurfaceHeight * 1),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                0)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                1)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                2)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                3)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                4)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                5)),
                        todoHeight + (waterSurfaceHeight * 1)},
                    pointCount);

            brush.setColor(
                    (visibility.isGray() || !visibility.isStronglyColored())
                    && !mouseOver ? Utils.ULTRA_LIGHT_GRAY : Color.DARK_GRAY);

            brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            brush.drawPolyline(
                    new int[]{intX,
                        (int) (intX + totalWidth / pointCount * 0.5),
                        intX + totalWidth / pointCount * 3,
                        intX + totalWidth / pointCount * 4,
                        intX + totalWidth / pointCount * 5,
                        intX + totalWidth / pointCount * 6,
                        intX + totalWidth / pointCount * 7,
                        intX + totalWidth / pointCount * 8},
                    new int[]{todoHeight + (waterSurfaceHeight * 1),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                0, true)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                1, true)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                2, true)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                3, true)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                4, true)),
                        todoHeight + (int) (waterSurfaceHeight * getRandom(
                                5, true)),
                        todoHeight + (waterSurfaceHeight * 1)},
                    pointCount);
            brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK
                : Color.LIGHT_GRAY);

        if (donePercent < 1 && donePercent > 0) {
            {
                Font currentFont = brush.getFont();
                brush.setFont(BIG_FONT);
                if (chargingCharacterVisibleProperty.isEnabled() && changedInTheLastXMilliseconds(1000)) {
                    paintChargingCharacter(brush);
                }
                brush.setFont(currentFont);

            }

        }
        if (circleProgressVisibleProperty.isEnabled()) {
            paintCircleProgress(brush, visibility);
        }

        if (donePercent > 0) {
            Font currentFont = brush.getFont();
            brush.setFont(BIG_FONT);

            paintSmiley(visibility, brush, ((int) (totalWidth * 0.45)) + 15,
                    (donePercent < 0.5 ? totalHeight / 4 * 3
                            : (totalHeight / 4 * 1) + 10) + 8 - 16);
            brush.setFont(currentFont);
        }

        if (percentProgressVisibleProperty.isEnabled()) {
            brush.drawString(
                    (donePercent == 1 ? 100 : (NumberFormats.FORMATTER_THREE_DECIMAL_PLACES
                                    .format(donePercent * 100))) + "%",
                    ((int) (totalWidth * 0.15)),
                    donePercent > 0.5 ? totalHeight / 4 * 3
                            : totalHeight / 4 * 1);
        }

        if (labelVisibleProperty.isEnabled() && label != null && !label
                .isEmpty()) {
            brush.drawString(
                    label,
                    ((int) (totalWidth * 0.15)),
                    (donePercent > 0.5 ? totalHeight / 4 * 3
                            : totalHeight / 4 * 1) + 20);
        }
        if (nameVisibleProperty.isEnabled() && name != null && !name
                .isEmpty()) {
            brush.drawString(
                    name,
                    ((int) (totalWidth * 0.10)),
                    (totalHeight / 4 * 3) + 20 + 20);
        }
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK
                : Color.LIGHT_GRAY);
        brush.drawRect(1, 1, totalWidth - 2, totalHeight);

    }

    public void paintChargingCharacter(Graphics2D brush) {
        brush.drawString(
                CHARCHING, ((int) (totalWidth * 0.45)),
                (donePercent < 0.5 ? totalHeight / 4 * 3
                        : (totalHeight / 4 * 1) + 10) + 10
        );
    }

    private void paintCircleProgress(Graphics2D brush, Visibility visibility) {
        Color currentColor = brush.getColor();
        brush.setColor(
                visibility.isStronglyColored() ? HIGH_STRONGLY_COLORED
                : (visibility.isWeaklyColored() ? HIGH_WEAKLY_COLORED
                : Color.lightGray));

        double angleDouble = donePercent * 360;

        brush.fillArc(((int) (totalWidth * 0.45)) + 15,
                totalHeight / 4 * 3 + 28,
                15, 15, 90, -(int) angleDouble);
        brush.setColor(
                visibility.isStronglyColored() ? LIGHT_RED
                : visibility.isWeaklyColored() ? ULTRA_LIGHT_RED
                : BACKGROUND_COLOR);
        brush.fillArc(((int) (totalWidth * 0.45)) + 15,
                totalHeight / 4 * 3 + 28,
                15, 15, 90, +(int) (360 - angleDouble));

        brush.setColor(currentColor);
    }

    private double getRandom(int index) {
        return getRandom(index, false);
    }

    private double getRandom(int index, boolean keepArray) {
        if (!keepArray && Math.random() > 0.96) {
            randomDoubles[index] = Math.random();
        }
        return randomDoubles[index];
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setBounds(int x, int y, int height) {
        setBounds(x, y, (int) (40d / 100d * ((double) height)), height);
    }

    @Override
    public int getTimerDelay() {
        return 25;
    }

}
