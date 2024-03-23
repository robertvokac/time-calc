package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

public class ProgressDot extends Widget {

    private int square;

    @AllArgsConstructor
    class Dot {

        private int x, y;
    }
    private List<Dot> enabledDots = new ArrayList<>();
    private List<Dot> disabledDots = new ArrayList<>();

    public ProgressDot() {
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    public void paintWidget(Graphics g) {
        int dotSize = 1;
        if (side == 0) {
            this.side = Math.min(getWidth(), getHeight()) / dotSize;
            this.square = side * side;
            for (int y = 1; y <= side; y++) {
                for (int x = 1; x <= side; x++) {
                    disabledDots.add(new Dot(x, y));
                }
            }
        }
        Double done = donePercent();
        int enabledDotsExpectedSize = (int) (done * square);
        int disabledDotsExpectedSize = square - enabledDotsExpectedSize;
        System.out.println("enabledDots.size()=" + enabledDots.size());
        System.out.println("disabledDots.size()=" + disabledDots.size());
        while (enabledDots.size() > enabledDotsExpectedSize) {
            int randomIndex = (int) (enabledDots.size() * Math.random());
            Dot randomDot = enabledDots.remove(randomIndex);
            disabledDots.add(randomDot);
        }
        while (enabledDots.size() < enabledDotsExpectedSize) {
            int randomIndex = (int) (disabledDots.size() * Math.random());
            Dot randomDot = disabledDots.remove(randomIndex);
            enabledDots.add(randomDot);
        }

        Graphics2D brush = (Graphics2D) g;
        brush.setColor(FOREGROUND_COLOR);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int numberOfDots = (int) (donePercent() * square);

        Visibility visibility = Visibility.ofProperty(visibilityProperty);
        {
            if (visibility.isStronglyColored() || mouseOver) {
                brush.setColor(Color.GRAY);
            }

            for (Dot d : enabledDots) {
                brush.fillRect(((d.x - 1) * dotSize) + d.x, ((d.y - 1) * dotSize) + d.y, dotSize+(dotSize > 1 ? 1 : 1), dotSize+(dotSize > 1 ? 1 : 1));
            }
            if(visibility.isStronglyColored()){
            //Color currentColor = brush.getColor();
            brush.setColor(Battery.LOW_WEAKLY_COLORED);
            for (Dot d : disabledDots) {
                brush.fillRect(((d.x - 1) * dotSize) + d.x, ((d.y - 1) * dotSize) + d.y, dotSize+(dotSize > 1 ? 1 : 1), dotSize+(dotSize > 1 ? 1 : 1));
            }
            //brush.setColor(currentColor);
            }
            brush.setColor(FOREGROUND_COLOR);
        }
        brush.setColor(visibility.isStronglyColored() || mouseOver ? Color.BLACK
                : BACKGROUND_COLOR);

        brush.drawString(NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                .format(donePercent() * 100) + "%",
                (int) (side / 8d * 3d),
                (int) (side / 8d * (donePercent() > 0.5 ? 3d : 5d)));
        paintSmiley(visibility, brush, (int) (side / 8d * 3d),
                (int) ((side / 8d * (donePercent() > 0.5 ? 3d : 5d)) - 32d));

    }

}
