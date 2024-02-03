package org.nanoboot.utils.timecalc.gui.progress;

import org.nanoboot.utils.timecalc.gui.common.Widget;
import org.nanoboot.utils.timecalc.utils.Utils;

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
    public void paintComponent(Graphics g) {
        if (side == 0) {
            this.side = Math.min(getWidth(), getHeight());
            this.square = side * side;
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int dotNumber = (int) (donePercent * square);
        int y = dotNumber / side;
        int yOrig = y;
        int x = dotNumber - y * side;

        //        System.out.println("dotNumber=" + dotNumber);
        //        System.out.println("x=" + x);
        //        System.out.println("y=" + y);
        if (y > 1) {
            if (Utils.highlighted.get() || mouseOver) {
                g2d.setColor(Color.GRAY);
            }
            g2d.fillRect(side - 4, side - 4, 4, 4);
            g2d.fillRect(1, side - 4, 4, 4);

            g2d.setColor(FOREGROUND_COLOR);
            g2d.fillRect(1, 1, side, y - 1);
            if (x > 1) {
                if (Utils.highlighted.get() || mouseOver) {
                    g2d.setColor(Color.GRAY);
                }
                g2d.drawRect(1, y, x - 1, 1);
            }
            if (Utils.highlighted.get() || mouseOver) {
                g2d.setColor(Color.GRAY);
            }
            g2d.fillRect(side - 4, 1, 4, 4);
            g2d.fillRect(1, 1, 4, 4);

            if (Utils.highlighted.get() || mouseOver) {
                g2d.setColor(Color.GRAY);
            }
            g2d.drawLine(1, 1, x, y);
            //            g2d.drawLine(1+1, 1+1, x+1, y+1);
            g2d.drawLine(1, 1 + 1, x, y + 1);
            g2d.drawLine(1, 1 + 1, x, y + 1);
            if (Utils.highlighted.get() || mouseOver) {
                g2d.setColor(Color.BLUE);
                g2d.drawLine(x - 10, y - 10, x + 10, y + 10);
                g2d.drawLine(x + 10, y - 10, x - 10, y + 10);
            }
            g2d.setColor(FOREGROUND_COLOR);
        }

    }

}