package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressBar extends Widget {

    public IntegerProperty heightProperty = new IntegerProperty("heightProperty", 25);
    public ProgressBar() {
        setPreferredSize(new Dimension(600, 25));
    }

    @Override
    public void paintWidget(Graphics g) {
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ?
                        Battery.getColourForProgress(donePercent(), visibility, mouseOver)/*Color.darkGray*/
                : FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int progressWidth = (int) (getWidth() * donePercent());
        int remainsWidth = getWidth() - progressWidth;

        brush.setColor(
                visibility.isStronglyColored() || mouseOver ?
                        Color.WHITE
                        : BACKGROUND_COLOR);
        int h = heightProperty.getValue() > getHeight() ? getHeight() :
                heightProperty.getValue();
        if(h < 1) {h = 1;}
        brush.fillRect(0, 0, getWidth(), h);
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ?
                        Battery.getColourForProgress(donePercent(), visibility, mouseOver)/*Color.darkGray*/
                        : FOREGROUND_COLOR);
        brush.fillRect(0, 0, progressWidth, h);
        brush.setColor(h <= 15 || progressWidth < 40 ? (visibility.isStronglyColored() ? Color.BLACK : Color.GRAY) : Color.WHITE);
        brush.drawString(
                NumberFormats.FORMATTER_ONE_DECIMAL_PLACE
                        .format(donePercent() * 100) + "%",
                progressWidth < 40 ? 40 : progressWidth - 40,
                h <= 15 ? h + 15 : 15);


    }

}
