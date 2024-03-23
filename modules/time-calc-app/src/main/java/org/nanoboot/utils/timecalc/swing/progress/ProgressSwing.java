package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressSwing extends Widget {

    public final BooleanProperty quarterIconVisibleProperty
            = new BooleanProperty(
            TimeCalcProperty.SWING_QUARTER_ICON_VISIBLE
                    .getKey(), true);

    public ProgressSwing() {
        setPreferredSize(new Dimension(200, 100));
    }

    @Override
    public void paintWidget(Graphics g) {
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        brush.setColor(
                visibility.isStronglyColored() || mouseOver ? Color.darkGray
                : FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        brush.setStroke(new BasicStroke(0.5f));

        //brush.drawRect(1,1, getWidth() - 2, getHeight() - 2);
        brush.setStroke(new BasicStroke(1.5f));
        brush.drawLine(getWidth() /2, getHeight(), getWidth()/2,
                (int) (getHeight() * 0.66) - 10);
        brush.fillOval(getWidth()/2 - 3,
                (int) (getHeight() * 0.66) - 10 - 3, 6, 6);
        brush.drawLine(1, getHeight() - 2, getWidth(), getHeight() - 2);
        int startX = (int) (getWidth() * 0.10);
        int startY = (int) (getHeight() - getHeight() * 0.66 * (1 - donePercent)) - 10;
        int endX = (int) (getWidth() * 0.90);
        int endY = (int) (getHeight() - getHeight() * 0.66 * donePercent) - 10;

        brush.drawLine(startX, startY, endX, endY);
//        brush.drawLine(startX, startY, getWidth()/2,
//                (int) (getHeight() * 0.6));
//        brush.drawLine(endX, endY, getWidth()/2,
//                (int) (getHeight() * 0.6));


        paintSmiley(visibility, brush, startX, startY - 25, true);
        paintSmiley(visibility, brush, endX, endY - 25, true, 1.0 - donePercent, 2);

        brush.drawString(
                NumberFormats.FORMATTER_ZERO_DECIMAL_PLACES
                        .format(donePercent * 100) + "%",
                (int) (getWidth() * 0.45), (int) (getHeight() * 0.9));
        if (quarterIconVisibleProperty.isEnabled()) {
            paintQuarterIcon(brush, visibility, getWidth(), getHeight(),
                    (int) (getWidth() * 0.45), (int) (getHeight() * 0.25));
        }

    }

}
