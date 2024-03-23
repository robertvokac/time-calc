package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressCircle extends Widget {

    public ProgressCircle() {
        setPreferredSize(new Dimension(200, 200));
    }

    @Override
    public void paintWidget(Graphics g) {
        if (side == 0) {
            this.side = Math.min(getWidth(), getHeight());
        }
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ? Color.darkGray
                : FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        double angleDouble = donePercent() * 360;
        double angleDouble2 = (angleDouble - (int) (angleDouble)) * 360;
        //        System.out.println("remainingAngle=" + angleDouble2);

        brush.fillArc(0, 0, side, side, 90, -(int) angleDouble);
        int side2 = side / 2;
        brush.setColor(visibility.isStronglyColored() || mouseOver
                ? new Color(105, 175, 236) : FOREGROUND_COLOR2);
        brush.fillArc(0 + (side2 / 2), 0 + (side2 / 2), side2, side2, 90,
                -(int) angleDouble2);
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.blue
                : FOREGROUND_COLOR);

        brush.drawString(
                NumberFormats.FORMATTER_ZERO_DECIMAL_PLACES
                        .format(donePercent() * 100) + "%",
                (int) (side / 8d * 0d), (int) (side / 8d * 7.5d));
        paintSmiley(visibility, brush, (int) (side / 8d * 0d) + 30,
                (int) (side / 8d * 7.5d - 16d));

    }

}
