package org.nanoboot.utils.timecalc.gui.progress;

import lombok.Getter;
import org.nanoboot.utils.timecalc.gui.common.Widget;
import org.nanoboot.utils.timecalc.main.TimeCalcConf;
import org.nanoboot.utils.timecalc.utils.BooleanHolder;
import org.nanoboot.utils.timecalc.utils.NumberFormats;
import org.nanoboot.utils.timecalc.utils.Utils;

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
    public static boolean wavesOff = false;
    private static final Font bigFont = new Font("sans", Font.BOLD, 24);
    private BooleanHolder blinking = new BooleanHolder();
    private long tmpNanoTime = 0l;

    @Getter
    private final String name;

    private int totalHeight = 0;

    private int totalWidth;
    private String label = null;
    private final double[] randomDoubles = new double[] {1d, 1d, 1d, 1d, 1d, 1d, 1};

    protected Battery(String name) {
        this.name = name;
        setPreferredSize(new Dimension(40, 100));
    }

    protected Battery(String name, int i, int y, int height) {
        this(name);
        setBounds(i, y, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (totalHeight == 0) {
            this.totalHeight = (int) (this.getHeight() / 10d * 7d);
            this.totalWidth = this.getWidth();
        }
        if(donePercent > 0 && donePercent < CRITICAL_LOW_ENERGY && (System.nanoTime() - tmpNanoTime) > (500000000l) / 2l) {
            blinking.flip();
            tmpNanoTime = System.nanoTime();
        }
        if(donePercent <= 0 && blinking.get()){
            blinking.set(false);
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.YELLOW :
                FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!Utils.ultraLight.get()) {
            g2d.fillRect(1, 1, totalWidth, totalHeight);
        }

        if (Utils.highlighted.get() || mouseOver) {
            g2d.setColor(
                    donePercent < LOW_ENERGY ? LOW_HIGHLIGHTED : (donePercent < HIGH_ENERGY ?
                            MEDIUM_HIGHLIGHTED :
                            (donePercent < VERY_HIGH_ENERGY ? HIGH_HIGHLIGHTED :
                                    HIGHEST_HIGHLIGHTED)));
        } else {
            g2d.setColor(donePercent < LOW_ENERGY ? LOW : (donePercent < HIGH_ENERGY ?
                    MEDIUM : (donePercent < VERY_HIGH_ENERGY ? HIGH : HIGHEST)));
        }
        if (Utils.ultraLight.get()) {
            g2d.setColor(Utils.ULTRA_LIGHT_GRAY);
        }
        if(blinking.get()) {
            g2d.setColor(BACKGROUND_COLOR);
        }
        int doneHeight = (int) (totalHeight * donePercent);
        int intX = 1;
        int todoHeight = totalHeight - doneHeight;
        double surfacePower =
                1;//donePercent < 0.5 ? 0.5 : donePercent;// (donePercent * 100 - ((int)(donePercent * 100)));
        int waterSurfaceHeight =
                (int) (4 * surfacePower);//2 + (int) (Math.random() * 3);
        if (waterSurfaceHeight <= 2 || !TimeCalcConf.getInstance()
                .areBatteryWavesEnabled() || wavesOff) {
            waterSurfaceHeight = 0;
        }

        g2d.fillRect(intX+1, doneHeight < waterSurfaceHeight || donePercent >= 1 ?
                        todoHeight : todoHeight + waterSurfaceHeight,
                totalWidth - 3, doneHeight < waterSurfaceHeight || donePercent >= 1 ?
                        doneHeight : doneHeight - waterSurfaceHeight + 1);
        int pointCount = 8;
        if (doneHeight >= waterSurfaceHeight
            && donePercent < 1) {// && todoHeight > waterSurfaceHeight) {
            //g2d.fillArc(intX, intY, width_, intHeight - waterSurfaceHeight, 30, 60);

            g2d.fillPolygon(
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

            g2d.setColor((Utils.ultraLight.get() || !Utils.highlighted.get()) && !mouseOver ? Utils.ULTRA_LIGHT_GRAY : Color.DARK_GRAY);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.drawPolyline(
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
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);

        Font currentFont = g2d.getFont();
        g2d.setFont(bigFont);
        g2d.drawString("⚡", ((int) (totalWidth * 0.45)),(donePercent < 0.5 ? totalHeight / 4 * 3 :
                totalHeight / 4 * 1) + 10);
        g2d.setFont(currentFont);

        g2d.drawString(
                NumberFormats.FORMATTER_THREE_DECIMAL_PLACES.format(donePercent * 100) + "%",
                ((int) (totalWidth * 0.15)),
                donePercent > 0.5 ? totalHeight / 4 * 3 : totalHeight / 4 * 1);

        if (label != null && !label.isEmpty()) {
            g2d.drawString(
                    label,
                    ((int) (totalWidth * 0.15)),
                    (donePercent > 0.5 ? totalHeight / 4 * 3 :
                            totalHeight / 4 * 1) + 20);
        }
        if (name != null && !name.isEmpty()) {
            g2d.drawString(
                    name,
                    ((int) (totalWidth * 0.15)),
                    (totalHeight / 4 * 3) + 20 + 20);
        }
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);
        g2d.drawRect(1, 1, totalWidth - 2, totalHeight);

    }

    private double getRandom(int index) {
        return getRandom(index, false);
    }
    private double getRandom(int index, boolean keepArray) {
        if (!keepArray && Math.random() > 0.95) {
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
        setBounds(x, y, (int) (40d / 100d * ((double)height)), height);
    }
    public int getTimerDelay() {
        return 25;
    }
}