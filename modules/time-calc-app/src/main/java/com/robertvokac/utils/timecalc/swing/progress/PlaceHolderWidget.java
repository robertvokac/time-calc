package com.robertvokac.utils.timecalc.swing.progress;

import com.robertvokac.utils.timecalc.entity.Visibility;
import com.robertvokac.utils.timecalc.swing.common.Widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PlaceHolderWidget extends Widget {

    public PlaceHolderWidget() {
        setPreferredSize(new Dimension(50, 50));
    }

    @Override
    public void paintWidget(Graphics g) {

        Graphics2D brush = (Graphics2D) g;
        brush.setColor(mouseOver ? Color.LIGHT_GRAY : FOREGROUND_COLOR);

        Visibility visibility = Visibility.ofProperty(visibilityProperty);

            brush.fillRect(1, 1, 48, 48);
            brush.setColor(Color.LIGHT_GRAY);
            brush.drawRect(1, 1, 48, 48);

            brush.setColor(FOREGROUND_COLOR);

    }

}
