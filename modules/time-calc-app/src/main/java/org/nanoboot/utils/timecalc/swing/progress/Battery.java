package org.nanoboot.utils.timecalc.swing.progress;

import lombok.Getter;
import org.nanoboot.utils.timecalc.app.TimeCalcProperties;
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

public class Battery extends Widget {
    public static final Color LOW = new Color(253, 130, 130);
    public static final Color MEDIUM = new Color(255, 204, 153);
    public static final Color HIGH = new Color(204, 255, 204);
    public static final Color HIGHEST = new Color(153, 255, 153);
    public static final Color LOW_HIGHLIGHTED = Color.red;
    public static final Color MEDIUM_HIGHLIGHTED = Color.ORANGE;
    public static final Color HIGH_HIGHLIGHTED = new Color(158, 227, 158);
    public static final Color HIGHEST_HIGHLIGHTED = Color.green;
    public static final double CRITICAL_LOW_ENERGY = 0.10;
    public static final double LOW_ENERGY = 0.15;
    public static final double HIGH_ENERGY = 0.75;
    public static final double VERY_HIGH_ENERGY = 0.9;
    public static final Color LIGHT_RED = new Color(
            229, 168, 168);
    public static final Color ULTRA_LIGHT_RED = new Color(
            238, 196, 196);
    public static final String CHARCHING = "⚡";
    @Getter
    private final String name;
    private final double[] randomDoubles =
            new double[] {1d, 1d, 1d, 1d, 1d, 1d, 1};
    public BooleanProperty wavesProperty = new BooleanProperty(TimeCalcProperty.BATTERY_WAVES_VISIBLE
            .getKey(), true);
    private final BooleanProperty blinking = new BooleanProperty("blinking");
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

        Graphics2D brush = (Graphics2D) g;

        Visibility visibility =
                Visibility.valueOf(visibilityProperty.getValue());
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ? Color.YELLOW :
                        FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!visibility.isGray()) {
            brush.fillRect(1, 1, totalWidth, totalHeight);
        }

        if (visibility.isStronglyColored() || mouseOver) {
            brush.setColor(
                    donePercent < LOW_ENERGY ? LOW_HIGHLIGHTED :
                            (donePercent < HIGH_ENERGY ?
                                    MEDIUM_HIGHLIGHTED :
                                    (donePercent < VERY_HIGH_ENERGY ?
                                            HIGH_HIGHLIGHTED :
                                            HIGHEST_HIGHLIGHTED)));
        } else {
            brush.setColor(donePercent < LOW_ENERGY ? LOW :
                    (donePercent < HIGH_ENERGY ?
                            MEDIUM :
                            (donePercent < VERY_HIGH_ENERGY ? HIGH : HIGHEST)));
        }
        if (visibility.isGray()) {
            brush.setColor(Utils.ULTRA_LIGHT_GRAY);
        }
        if (blinking.getValue()) {
            brush.setColor(BACKGROUND_COLOR);
        }
        int doneHeight = (int) (totalHeight * donePercent);
        int intX = 1;
        int todoHeight = totalHeight - doneHeight;
        double surfacePower =
                1;//donePercent < 0.5 ? 0.5 : donePercent;// (donePercent * 100 - ((int)(donePercent * 100)));
        int waterSurfaceHeight =
                (int) (4 * surfacePower);//2 + (int) (Math.random() * 3);
        if (waterSurfaceHeight <= 2 || !TimeCalcProperties.getInstance()
                .getBooleanProperty(TimeCalcProperty.BATTERY_WAVES_VISIBLE) || wavesProperty.isDisabled()) {
            waterSurfaceHeight = 0;
        }

        brush.fillRect(intX + 1,
                doneHeight < waterSurfaceHeight || donePercent >= 1 ?
                        todoHeight : todoHeight + waterSurfaceHeight,
                totalWidth - 3,
                doneHeight < waterSurfaceHeight || donePercent >= 1 ?
                        doneHeight : doneHeight - waterSurfaceHeight + 1);
        int pointCount = 8;
        if (doneHeight >= waterSurfaceHeight
            && donePercent < 1) {// && todoHeight > waterSurfaceHeight) {
            //g2d.fillArc(intX, intY, width_, intHeight - waterSurfaceHeight, 30, 60);

            brush.fillPolygon(
                    new int[] {intX,
                            (int) (intX + totalWidth / pointCount * 0.5),
                            intX + totalWidth / pointCount * 3,
                            intX + totalWidth / pointCount * 4,
                            intX + totalWidth / pointCount * 5,
                            intX + totalWidth / pointCount * 6,
                            intX + totalWidth / pointCount * 7,
                            intX + totalWidth / pointCount * 8},
                    new int[] {todoHeight + (waterSurfaceHeight * 1),
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
                    new int[] {intX,
                            (int) (intX + totalWidth / pointCount * 0.5),
                            intX + totalWidth / pointCount * 3,
                            intX + totalWidth / pointCount * 4,
                            intX + totalWidth / pointCount * 5,
                            intX + totalWidth / pointCount * 6,
                            intX + totalWidth / pointCount * 7,
                            intX + totalWidth / pointCount * 8},
                    new int[] {todoHeight + (waterSurfaceHeight * 1),
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
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);

        if (donePercent < 1 && donePercent > 0) {
            {
                Font currentFont = brush.getFont();
                brush.setFont(BIG_FONT);
                brush.drawString(
                        CHARCHING, ((int) (totalWidth * 0.45)),
                        (donePercent < 0.5 ? totalHeight / 4 * 3 :
                                totalHeight / 4 * 1) + 10
                );

                paintSmiley(visibility, brush, ((int) (totalWidth * 0.45)) + 15,
                        (donePercent < 0.5 ? totalHeight / 4 * 3 :
                                totalHeight / 4 * 1) + 8 - 16);
                brush.setFont(currentFont);

            }
            {
                Color currentColor = brush.getColor();
                brush.setColor(
                        visibility.isStronglyColored() ? HIGH_HIGHLIGHTED : (visibility.isWeaklyColored() ? HIGH : Color.lightGray));


                double angleDouble = donePercent * 360;

                brush.fillArc(((int) (totalWidth * 0.45)) + 15,
                        totalHeight / 4 * 3 + 28,
                        15, 15, 90, -(int) angleDouble);
                brush.setColor(
                        visibility.isStronglyColored() ? LIGHT_RED :
                                visibility.isWeaklyColored() ? ULTRA_LIGHT_RED : BACKGROUND_COLOR);
                brush.fillArc(((int) (totalWidth * 0.45)) + 15,
                        totalHeight / 4 * 3 + 28,
                        15, 15, 90, +(int) (360 - angleDouble));

                brush.setColor(currentColor);
            }
        }

        brush.drawString(
                NumberFormats.FORMATTER_THREE_DECIMAL_PLACES
                        .format(donePercent * 100) + "%",
                ((int) (totalWidth * 0.15)),
                donePercent > 0.5 ? totalHeight / 4 * 3 : totalHeight / 4 * 1);

        if (label != null && !label.isEmpty()) {
            brush.drawString(
                    label,
                    ((int) (totalWidth * 0.15)),
                    (donePercent > 0.5 ? totalHeight / 4 * 3 :
                            totalHeight / 4 * 1) + 20);
        }
        if (name != null && !name.isEmpty()) {
            brush.drawString(
                    name,
                    ((int) (totalWidth * 0.10)),
                    (totalHeight / 4 * 3) + 20 + 20);
        }
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);
        brush.drawRect(1, 1, totalWidth - 2, totalHeight);

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

    public void setBounds(int x, int y, int height) {
        setBounds(x, y, (int) (40d / 100d * ((double) height)), height);
    }

    public int getTimerDelay() {
        return 25;
    }
}