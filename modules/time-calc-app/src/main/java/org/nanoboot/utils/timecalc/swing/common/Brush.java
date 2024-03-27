package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.entity.Visibility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author pc00289
 * @since 27.03.2024
 */
public class Brush {
    private final Graphics2D brush;

    public Brush (Graphics2D graphics2D) {
        this.brush = graphics2D;
    }
    public void drawOval(int centreX, int centreY, int width, int height) {
        brush.drawOval((int) (centreX - width / 2d), (int) (centreY - height / 2d), width, height);
    }
    public void drawArc(int centreX, int centreY, int width, int height, int startAngle, int angle) {
        brush.drawArc((int) (centreX - width / 2d), (int) (centreY - height / 2d), width, height, startAngle + 90, angle);
    }
    public void drawBorder(int centreX, int centreY, int length, int startLength, int angle,
            float stroke, Color color) {
        double angleRadian = Math.PI * 2 * ((angle - 90d) / 360d);
        Color currentColor = brush.getColor();
        brush.setColor(color);
        int startX
                = (int) (centreX + startLength * Math.cos(angleRadian));
        int startY
                = (int) (centreY + startLength * Math.sin(angleRadian));
        int endX = (int) (centreX + (startLength + length) * Math.cos(angleRadian));
        int endY = (int) (centreY + (startLength + length) * Math.sin(angleRadian));

        brush.setStroke(new BasicStroke(stroke));
        brush.drawLine(startX, startY, endX, endY);
        brush.setColor(currentColor);
    }
}
