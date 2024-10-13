package com.robertvokac.utils.timecalc.swing.progress;

import com.robertvokac.utils.timecalc.app.GetProperty;
import com.robertvokac.utils.timecalc.entity.Visibility;
import com.robertvokac.utils.timecalc.swing.windows.MainWindow;
import com.robertvokac.utils.timecalc.swing.common.SwingUtils;
import com.robertvokac.utils.timecalc.swing.common.Toaster;
import com.robertvokac.utils.timecalc.swing.common.Widget;
import com.robertvokac.utils.timecalc.utils.common.Constants;
import com.robertvokac.utils.timecalc.utils.common.NumberFormats;
import com.robertvokac.utils.timecalc.utils.common.Utils;
import com.robertvokac.utils.timecalc.utils.property.Property;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Robert Vokac
 * @since 21.02.2024
 */
public class WalkingHumanProgress extends Widget implements
        GetProperty {

    private static final String WALL = "||";

    private static final int LINE_WHERE_HEAD_IS = 2;

    public WalkingHumanProgress() {
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        setFocusable(false);
        setForeground(Color.GRAY);
        setBackground(MainWindow.BACKGROUND_COLOR);

        new Timer(100, e -> {
            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());
            setForeground(
                    visibility.isStronglyColored()
                    || mouseOver
                            ? Color.BLACK : Color.LIGHT_GRAY);
        }).start();

    }

    private static final String createSpaces(int spaceCount) {
        return createRepeatedString(spaceCount, ' ');
    }

    private static final String createRepeatedString(int count, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    @Override
    public void paintWidget(Graphics brush) {
        String string = createAsciiString();
        if (string.isEmpty()) {
            //nothing to do
        } else {
            String[] lines = string.split("\n");

            Visibility visibility
                    = Visibility.valueOf(visibilityProperty.getValue());

            brush.setColor(visibility.isStronglyColored() ? Color.BLUE
                    : visibility.isWeaklyColored() ? Color.GRAY
                    : Color.LIGHT_GRAY);
//            if(mouseOver) {
//                brush.drawRect(1,1,getWidth() - 2, getHeight() - 2);
//            }
            brush.setFont(SwingUtils.MEDIUM_MONOSPACE_FONT);
            int y = SwingUtils.MARGIN;
            int lineNumber = 0;
            for (String line : lines) {
                ++lineNumber;
                brush.drawString(line, SwingUtils.MARGIN, y);

                if(lineNumber == LINE_WHERE_HEAD_IS) {
                    paintSmiley(visibility,
                            (Graphics2D) brush,
                            //29 309
                            29 + ((int) (280 * donePercent())),
                            y - 4, true);
                }
                y = y + SwingUtils.MARGIN;
            }

        }
    }

    private String createAsciiString() {
        if (visibleProperty.isDisabled()) {
            //nothing to do
            return "";
        }
        boolean bodyEnabled = !shouldBeSmileyPainted();
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        this.setVisible(visibility != Visibility.NONE);

        StringBuilder sb = new StringBuilder();
        int spacesTotal = 40;
        int spacesDone = (int) (donePercent() * spacesTotal);
        if (spacesDone > spacesTotal) {
            spacesDone = spacesTotal;
        }
        int spacesTodo = spacesTotal - (spacesDone < 0 ? 0 : spacesDone);

        sb.append(WALL + createSpaces(spacesTotal + 6 - 2) + (spacesTodo == 0
                ? "          \n" : "|======|\n"));
        sb.append(WALL).append(createSpaces(spacesTotal + 4))
                .append(spacesTodo == 0 ? "" : "|      |").append("\n");

        sb.append(
                WALL + createSpaces(spacesDone) + (bodyEnabled ? HEAD : "    ") + createSpaces(
                spacesTodo) + (spacesTodo == 0
                        ? " \\☼☼☼☼/  "
                        : "|    _ |") + Constants.NEW_LINE
                + WALL + createSpaces(spacesDone) + (bodyEnabled ? BODY : "    ") + createSpaces(
                spacesTodo) + (spacesTodo == 0
                        ? " ☼☼☼☼☼☼ "
                        : "|   |  |") + Constants.NEW_LINE
                + WALL + createSpaces(spacesDone) + (bodyEnabled ? LEGS : "    ") + createSpaces(
                spacesTodo) + (spacesTodo == 0
                        ? " /☼☼☼☼\\  "
                        : "|      |") + Constants.NEW_LINE
                + createRepeatedString(spacesTotal + 14, '=')
                + Constants.NEW_LINE + "Steps: "
                + NumberFormats.FORMATTER_FIVE_DECIMAL_PLACES
                        .format(donePercent() * ((double) spacesTotal)) + "/"
                + spacesTotal + " Done: " + NumberFormats.FORMATTER_EIGHT_DECIMAL_PLACES
                        .format(donePercent() * 100d) + "%"
        );
        return sb.toString();
    }

    @Override
    public Property getVisibilityProperty() {
        return visibilityProperty;
    }

    @Override
    public Property getVisibilitySupportedColoredProperty() {
        return visibilitySupportedColoredProperty;
    }
}
