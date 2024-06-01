package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressWater extends Widget {

    public ProgressWater() {
        System.out.println("Starting ProgressWater");
        setPreferredSize(new Dimension(100, 400));
    }

    public final BooleanProperty coloredProperty = new BooleanProperty("coloredProperty", false);
    private static final Color WATER_COLOR = new Color(128, 197, 222);

    @Override
    public void paintWidget(Graphics g) {

        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ?
                        (coloredProperty.isEnabled() ? Battery.getColourForProgress(donePercent(), Visibility.WEAKLY_COLORED, mouseOver): WATER_COLOR)
                        : FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int tankHeight = (int) (getHeight() * 0.48);

        int doneWaterHeight = (int) (((double)tankHeight) * (donePercent()));

        int h = getHeight() - 1;
        int w = getWidth() - 1;
        int w48 = (int) (w * 0.48);
        int w52 = (int) (w * 0.52);
        int h48 = (int) (h * 0.48) - 1;
        int h52 = (int) (h * 0.52);

        int w4 = (int) (w * 0.04);
        int h4 = (int) (h * 0.04);

        brush.fillRect(2, doneWaterHeight, w - 1,
                (int) (tankHeight * (1d - donePercent())));
        brush.fillRect(2, h - doneWaterHeight, w - 1,
                (int) (tankHeight * (donePercent())));
        if(donePercent() < 1d) {
            brush.fillRect(w48, h48, w4 + 1, h4 + tankHeight);
        }
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ?
                        Color.BLACK : FOREGROUND_COLOR);
        
        brush.drawPolyline(
                new int[] {w48, 1, 1, w, w, w52},
                new int[] {h48, h48, 1, 1,
                        h48, h48},
                6);

        brush.drawPolyline(
                new int[] {w52, w, w, 1, 1, w48},
                new int[] {h52, h52, h, h,
                        h52, h52},
                6);
        brush.drawLine(w48, h48, w48, h52);
        brush.drawLine(w52, h48, w52, h52);
    }

}
