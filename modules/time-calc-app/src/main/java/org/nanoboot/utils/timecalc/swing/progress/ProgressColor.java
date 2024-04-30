package org.nanoboot.utils.timecalc.swing.progress;

import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.common.Widget;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;
import org.nanoboot.utils.timecalc.utils.property.IntegerProperty;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ProgressColor extends Widget {

    public IntegerProperty heightProperty = new IntegerProperty("heightProperty", 50);
    public ProgressColor() {
        setPreferredSize(new Dimension(600, 50));
    }

    private final Map<Integer, Color> colorMap = new HashMap<>();

    @Override
    public void paintWidget(Graphics g) {
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Graphics2D brush = (Graphics2D) g;
        boolean stronglyColored = visibility.isStronglyColored() || mouseOver;
        int numberStart = 255;
        int numberEnd = stronglyColored ? 0 : (visibility.isWeaklyColored() ? 128 : 192) ;
        int numberDiff = numberStart - numberEnd;
        int number = (int) (numberStart - donePercent() * numberDiff);
        if(!colorMap.containsKey(number)) {
            colorMap.put(number, new Color(number, number, number));
        }
        Function<Integer, Color> colorReturnFunction = (n) -> {
            if(!colorMap.containsKey(n)) {
                colorMap.put(n, new Color(n, n, n));
            }
            return colorMap.get(n);
        };
        Color color = colorReturnFunction.apply(number);
        Color colorStart = colorReturnFunction.apply(numberStart);
        Color colorEnd = colorReturnFunction.apply(numberEnd);
        brush.setColor(color);
        brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int progressWidth = (int) (getWidth() * 0.5);
        //int remainsWidth = getWidth() - progressWidth;

        int h = heightProperty.getValue() > getHeight() ? getHeight() :
                heightProperty.getValue();
//        if(h < 1) {h = 1;}
        brush.fillRect(0, 0, getWidth(), h);
        brush.setColor(colorStart);
        int iii = (int) (getWidth() * 0.05d);
        brush.fillRect(0, 0, iii, h);
        brush.setColor(colorEnd);
        brush.fillRect(getWidth() - iii, 0, iii, h);
        brush.setColor(Color.BLUE);
        brush.setStroke(new BasicStroke(1f));
        brush.drawLine(iii, 0 , iii, h);
        brush.drawLine(getWidth() - iii, 0 , getWidth() - iii, h);
//        brush.setColor(
//                visibility.isStronglyColored() || mouseOver ?
//                        Battery.getColourForProgress(donePercent(), visibility, mouseOver)/*Color.darkGray*/
//                        : FOREGROUND_COLOR);
//        brush.fillRect(0, 0, progressWidth, h);
        brush.setColor(h <= 15 || progressWidth < 40 ? (visibility.isStronglyColored() ? Color.BLACK : Color.GRAY) : Color.WHITE);
        brush.drawString(
                NumberFormats.FORMATTER_ONE_DECIMAL_PLACE
                        .format(number) + " (" + numberStart + "-" + numberEnd + ")",
                (int) (progressWidth * 0.2d),
                h <= 15 ? h + 15 : 15);


    }

}
