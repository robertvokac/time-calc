package org.nanoboot.utils.timecalc.gui.progress;

import org.nanoboot.utils.timecalc.gui.common.Widget;
import org.nanoboot.utils.timecalc.main.TimeCalcConf;
import org.nanoboot.utils.timecalc.utils.Utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Battery extends Widget {
    public static final Color LOW = new Color(253, 130, 130);
    public static final Color MEDIUM = new Color(255, 204, 153);
    public static final Color HIGH = new Color(204, 255, 204);
    public static final Color HIGHEST = new Color(153, 255, 153);
    public static final Color LOW_HIGHLIGHTED = Color.red;
    public static final Color MEDIUM_HIGHLIGHTED = Color.ORANGE;
    public static final Color HIGH_HIGHLIGHTED = new Color(158, 227, 158);
    public static final Color HIGHEST_HIGHLIGHTED = Color.green;
    public static boolean wavesOff = false;
    NumberFormat formatter3 = new DecimalFormat("#0.000");
    private int totalHeight = 0;

    private int width_;
    private String label = null;
    private final double[] randomDoubles = new double[] {1d, 1d, 1d, 1d, 1d, 1d, 1};

    public Battery() {
        setPreferredSize(new Dimension(40, 100));
    }

    public Battery(int i, int y, int height) {
        this();
        setBounds(i, y, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (totalHeight == 0) {
            this.totalHeight = Math.min(getWidth(), getHeight());
            this.width_ = (int) (this.totalHeight * 0.6);
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.YELLOW :
                FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!Utils.ultraLight.get()) {
            g2d.fillRect(width_ / 4, 1, width_, totalHeight - 2);
        }
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);
        g2d.drawRect(width_ / 4 - 1, 0, width_ + 1, totalHeight + 0);
        if (Utils.highlighted.get() || mouseOver) {
            g2d.setColor(
                    donePercent < 0.1 ? LOW_HIGHLIGHTED : (donePercent < 0.75 ?
                            MEDIUM_HIGHLIGHTED :
                            (donePercent < 0.9 ? HIGH_HIGHLIGHTED :
                                    HIGHEST_HIGHLIGHTED)));
        } else {
            g2d.setColor(donePercent < 0.1 ? LOW : (donePercent < 0.75 ?
                    MEDIUM : (donePercent < 0.9 ? HIGH : HIGHEST)));
        }
        if (Utils.ultraLight.get()) {
            g2d.setColor(Utils.ULTRA_LIGHT_GRAY);
        }
        int doneHeight = (int) (totalHeight * donePercent);
        int intX = width_ / 4;
        int todoHeight = totalHeight - doneHeight;
        double surfacePower =
                1;//donePercent < 0.5 ? 0.5 : donePercent;// (donePercent * 100 - ((int)(donePercent * 100)));
        int waterSurfaceHeight =
                (int) (4 * surfacePower);//2 + (int) (Math.random() * 3);
        if (waterSurfaceHeight <= 2 || !TimeCalcConf.getInstance()
                .areBatteryWavesEnabled() || wavesOff) {
            waterSurfaceHeight = 0;
        }

        g2d.fillRect(intX, doneHeight < waterSurfaceHeight || donePercent >= 1 ?
                        todoHeight : todoHeight + waterSurfaceHeight,
                width_, doneHeight < waterSurfaceHeight || donePercent >= 1 ?
                        doneHeight : doneHeight - waterSurfaceHeight);
        int pointCount = 8;
        if (doneHeight >= waterSurfaceHeight
            && donePercent < 1) {// && todoHeight > waterSurfaceHeight) {
            //g2d.fillArc(intX, intY, width_, intHeight - waterSurfaceHeight, 30, 60);

            g2d.fillPolygon(
                    new int[] {intX,
                            (int) (intX + width_ / pointCount * 0.5),
                            intX + width_ / pointCount * 3,
                            intX + width_ / pointCount * 4,
                            intX + width_ / pointCount * 5,
                            intX + width_ / pointCount * 6,
                            intX + width_ / pointCount * 7,
                            intX + width_ / pointCount * 8},
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
        }
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);
        g2d.drawString(
                formatter3.format(donePercent * 100) + "%",
                ((int) (width_ * 0.4)),
                donePercent > 0.5 ? totalHeight / 4 * 3 : totalHeight / 4 * 1);

        if (label != null && !label.isEmpty()) {
            g2d.drawString(
                    label,
                    ((int) (width_ * 0.4)),
                    (donePercent > 0.5 ? totalHeight / 4 * 3 :
                            totalHeight / 4 * 1) + 20);
        }

    }

    private double getRandom(int index) {
        if (Math.random() > 0.7) {
            randomDoubles[index] = Math.random();
        }
        return randomDoubles[index];
    }

    public int getTimerDelay() {
        return 250;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setBounds(int x, int y, int height) {
        setBounds(x, y, (int) (90d / 140d * height), height);
    }
}