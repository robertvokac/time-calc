package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressSquare extends Widget {

    private int square;

    public ProgressSquare() {
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    public void paintWidget(Graphics g) {
        if (side == 0) {
            this.side = Math.min(getWidth(), getHeight());
            this.square = side * side;
        }

        Graphics2D brush = (Graphics2D) g;
        brush.setColor(FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int dotNumber = (int) (donePercent * square);
        int y = dotNumber / side;
        int yOrig = y;
        int x = dotNumber - y * side;

        //        System.out.println("dotNumber=" + dotNumber);
        //        System.out.println("x=" + x);
        //        System.out.println("y=" + y);
        Visibility visibility = Visibility.ofProperty(visibilityProperty);
        if (y > 1) {
            if (visibility.isStronglyColored() || mouseOver) {
                brush.setColor(Color.GRAY);
            }
            brush.fillRect(side - 4, side - 4, 4, 4);
            brush.fillRect(1, side - 4, 4, 4);

            brush.setColor(FOREGROUND_COLOR);
            brush.fillRect(1, 1, side, y - 1);
            if (x > 1) {
                if (visibility.isStronglyColored() || mouseOver) {
                    brush.setColor(Color.GRAY);
                }
                brush.drawRect(1, y, x - 1, 1);
            }
            if (visibility.isStronglyColored() || mouseOver) {
                brush.setColor(Color.GRAY);
            }
            brush.fillRect(side - 4, 1, 4, 4);
            brush.fillRect(1, 1, 4, 4);

            if (visibility.isStronglyColored() || mouseOver) {
                brush.setColor(Color.GRAY);
            }
            brush.drawLine(1, 1, x, y);
            //            g2d.drawLine(1+1, 1+1, x+1, y+1);
            brush.drawLine(1, 1 + 1, x, y + 1);
            brush.drawLine(1, 1 + 1, x, y + 1);
            if (visibility.isStronglyColored() || mouseOver) {
                brush.setColor(Color.BLUE);
                brush.drawLine(x - 10, y - 10, x + 10, y + 10);
                brush.drawLine(x + 10, y - 10, x - 10, y + 10);
            }
            brush.setColor(FOREGROUND_COLOR);
        }
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK
                : BACKGROUND_COLOR);

        brush.drawString(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                .format(donePercent * 100) + "%",
                (int) (side / 8d * 3d),
                (int) (side / 8d * (donePercent > 0.5 ? 3d : 5d)));
        paintSmiley(visibility, brush, (int) (side / 8d * 3d) + 65,
                (int) ((side / 8d * (donePercent > 0.5 ? 3d : 5d)) - 16d));

    }

}
