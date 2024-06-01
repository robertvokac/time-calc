package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Progress;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.progress.ProgressFuelGauge;
import org.nanoboot.utils.timecalc.swing.progress.battery.Battery;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSmileyIcon;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSwing;
import org.nanoboot.utils.timecalc.utils.common.ProgressSmiley;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.JMenuItem;
import org.nanoboot.utils.timecalc.app.TimeCalcApp;
import static org.nanoboot.utils.timecalc.swing.progress.battery.Battery.HIGH_STRONGLY_COLORED;
import static org.nanoboot.utils.timecalc.swing.progress.battery.Battery.HIGH_WEAKLY_COLORED;
import static org.nanoboot.utils.timecalc.swing.progress.battery.Battery.LIGHT_RED;
import static org.nanoboot.utils.timecalc.swing.progress.battery.Battery.ULTRA_LIGHT_RED;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class Widget extends JPanel implements
        GetProperty {

    private static final int CLOSE_OR_MINIMIZE_BUTTON_SIDE = 25;
    protected static final Color FOREGROUND_COLOR = new Color(220, 220, 220);
    protected static final Color FOREGROUND_COLOR2 = new Color(210, 210, 210);
    protected static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    protected static final Font BIG_FONT = new Font("sans", Font.BOLD, 24);
    protected static final Font MEDIUM_FONT = new Font("sans", Font.BOLD, 16);
    protected static final String HEAD = " () ";
    protected static final String BODY = "/||\\";
    protected static final String LEGS = " /\\ ";
    public static final Color CLOSE_BUTTON_FOREGROUND_COLOR
            = new Color(127, 127, 127);
    public static final Color CLOSE_OR_MINIMIZE_BUTTON_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public static final Color CLOSE_BUTTON_BACKGROUND_COLOR_MOUSE_OVER_CLOSE_ICON = new Color(255, 153, 153);
    public static final Color MINIMIZE_BUTTON_BACKGROUND_COLOR_MOUSE_OVER_CLOSE_ICON = new Color(
            126, 179, 227);
    private static final Color VERY_LIGHT_GRAY = new Color(220, 220, 220);
    private static final Font FONT = new Font("sans", Font.PLAIN, 12);
    public static final Color WIDGET_BACKGROUND_COLOR = ((Supplier<Color>) () ->{int i = 232;return new Color(i,i,i);}).get();

    public final BooleanProperty visibilitySupportedColoredProperty
            = new BooleanProperty("visibilitySupportedColoredProperty", true);
    public final BooleanProperty visibleProperty
            = new BooleanProperty("visibleProperty", true);
    public final BooleanProperty smileysVisibleProperty
            = new BooleanProperty(TimeCalcProperty.SMILEYS_VISIBLE.getKey());
    public final BooleanProperty smileysVisibleOnlyIfMouseMovingOverProperty
            = new BooleanProperty(
                    TimeCalcProperty.SMILEYS_VISIBLE_ONLY_IF_MOUSE_MOVING_OVER
                            .getKey());
    public final BooleanProperty smileysColoredProperty
            = new BooleanProperty("smileysColoredProperty", true);
    public StringProperty visibilityProperty
            = new StringProperty("widget.visibilityProperty",
                    Visibility.STRONGLY_COLORED.name());
    public StringProperty typeProperty
            = new StringProperty("widget.typeProperty", WidgetType.DAY.name().toLowerCase());
    public final BooleanProperty typeVisibleProperty = new BooleanProperty(TimeCalcProperty.TYPE_VISIBLE
            .getKey(), false);
    protected int side = 0;
    protected Progress progress = null;
    protected boolean mouseOver = false;
    private boolean mouseOverCloseButton = false;
    private boolean mouseOverMinimizeButton = false;
    protected JLabel smileyIcon;
    protected JLabel smileyIcon2;
    private long lastUpdate = System.nanoTime();
    private static final Color PURPLE_STRONGLY_COLORED = new Color(153,51,255);
    private static final Color PURPLE_WEAKLY_COLORED = new Color(204,153,255);

    public final BooleanProperty hiddenProperty
            = new BooleanProperty("hiddenProperty", false);

    private WidgetMenu widgetMenu = null;
    public Widget() {
        setBackground(BACKGROUND_COLOR);
        new Timer(getTimerDelay(), e -> repaint()).start();
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();
                mouseOverCloseButton = x >= getWidth() - CLOSE_OR_MINIMIZE_BUTTON_SIDE
                        && y <= CLOSE_OR_MINIMIZE_BUTTON_SIDE;
                mouseOverMinimizeButton = x < getWidth() - CLOSE_OR_MINIMIZE_BUTTON_SIDE
                                       && x > getWidth() - 2 * CLOSE_OR_MINIMIZE_BUTTON_SIDE
                                       && y <= CLOSE_OR_MINIMIZE_BUTTON_SIDE;
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(hiddenProperty.isEnabled()) {
                    hiddenProperty.disable();
                }

                if (mouseOverCloseButton) {
                    visibleProperty.setValue(false);
                    return;
                }

                if(mouseOverMinimizeButton) {
                    hiddenProperty.enable();
                    return;
                }

                if (visibilitySupportedColoredProperty.isDisabled()) {
                    //nothing to do
                    return;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
            }
        });

        Widget widget = this;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if(widgetMenu == null) {
                    widgetMenu = new WidgetMenu(widget, createRefreshConsumer());
                    List<JMenuItem> additionalMenus = createAdditionalMenus();
                    if(additionalMenus != null) {
                        additionalMenus.forEach(m-> widgetMenu.add(m));
                    }
                }
                widgetMenu.refresh();
                widgetMenu.markAsSelected(WidgetType.valueOf(typeProperty.getValue().toUpperCase(
        Locale.ROOT)));
                if (e.isPopupTrigger()) {
                    widgetMenu.show(e.getComponent(),
                            e.getX(), e.getY());

                }
            }


        });
    }
    protected Consumer<Object> createRefreshConsumer() {
        return null;
    }
    protected List<JMenuItem> createAdditionalMenus() {
        return null;
    }

    public int getTimerDelay() {
        return 100;
    }

    //    @Override
    //    public void setVisible(boolean aFlag) {
    //        if(visibleProperty.isEnabled() && !aFlag) {
    //            super.setVisible(false);
    //        }
    //        if(visibleProperty.isDisabled() && aFlag) {
    //            super.setVisible(false);
    //        }
    //
    //    }
    public final void setProgress(Progress newProgress) {

        double oldDonePercent = this.progress == null ? 0 : this.progress.getDonePercent(WidgetType.DAY);
        int oldDonePercentInt1000Mil = (int) (oldDonePercent * 1000000000);

        int newDonePercentInt1000Mil = (int) (newProgress.getDonePercent(WidgetType.DAY) * 1000000000);
        if (newDonePercentInt1000Mil != oldDonePercentInt1000Mil) {
            lastUpdate = System.nanoTime();
        }
        this.progress = newProgress;
    }

    protected double donePercent() {
        if(progress == null) {
            return 0d;
        }
        if(typeProperty.getValue().equals(WidgetType.PRESENTATION.name())) {
            long currentTime = new Date().getTime() / 1000l;
            long l = currentTime % 35;
            if (l >= 0 && l < 5) {
                return getDonePercentForWidgetType(WidgetType.MINUTE);
            }
            if (l >= 5 && l < 10) {
                return getDonePercentForWidgetType(WidgetType.HOUR);
            }
            if (l >= 10 && l < 15) {
                return getDonePercentForWidgetType(WidgetType.DAY);
            }
            if (l >= 15 && l < 20) {
                return getDonePercentForWidgetType(WidgetType.WEEK);
            }
            if (l >= 20 && l < 25) {
                return getDonePercentForWidgetType(WidgetType.MONTH);
            }
            if (l >= 25 && l < 30) {
                return getDonePercentForWidgetType(WidgetType.YEAR);
            }
            if (l >= 30 && l < 35) {
                return getDonePercentForWidgetType(WidgetType.LIFE);
            }
            return getDonePercentForWidgetType(WidgetType.DAY);
        }
        return getDonePercentForWidgetType(WidgetType.valueOf(typeProperty.getValue().toUpperCase(
                Locale.ROOT)));
    }

    private double getDonePercentForWidgetType(WidgetType widgetType) {
        return progress
                .getDonePercent(widgetType);
    }

    public void setBounds(int x, int y, int side) {
        setBounds(x, y, side, side);
    }

    @Override
    public final void paintComponent(Graphics brush) {
        super.paintComponent(brush);

        ((Graphics2D)brush).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        setVisible(visibleProperty.isEnabled());
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        Color currentColor = brush.getColor();
        brush.setColor(WIDGET_BACKGROUND_COLOR);
        brush.fillRect(1, 1, getWidth(), getHeight());
        brush.setColor(currentColor);

        if (visibleProperty.isDisabled() || hiddenProperty.isEnabled()) {
            if(hiddenProperty.isEnabled()) {
                if(this.smileyIcon != null) {
                    this.remove(this.smileyIcon);
                    this.smileyIcon = null;
                }
                if(this.smileyIcon2 != null) {
                    this.remove(this.smileyIcon2);
                    this.smileyIcon2 = null;
                }
                if (mouseOver) {
                    currentColor = brush.getColor();
                    brush.setColor(VERY_LIGHT_GRAY);
                    brush.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
                    brush.setColor(currentColor);
                }
                if(!visibility.isStronglyColored()) {
                    brush.setColor(Color.LIGHT_GRAY);
                }
                int row = 10;
                int x = (int)(getWidth() > 100 ? (int) (getWidth() * 0.4) : (int) (getWidth() * 0.1));
                if(getHeight() <= 50) {
                    brush.drawString("Show" + ' ' + getHumanName(), x, row);
                } else {
                    brush.drawString("Show", x, row);
                    row = row + 20;
                    String[] nameArray = getHumanName().split(" ");
                    for(int i = 0; i< nameArray.length; i++) {
                        brush.drawString(
                                nameArray[i],
                                x
                                , row);
                        row = row + 12;
                    }
                }

                
            }
            //nothing to do
            return;
        }

        super.setVisible(
                visibility != Visibility.NONE && visibleProperty.isEnabled());
        paintWidget(brush);

        paintCloseIcon(brush, getWidth(), mouseOver, mouseOverCloseButton);
        paintMinimizeIcon(brush, getWidth(), mouseOver, mouseOverMinimizeButton);

        if (mouseOver) {
            currentColor = brush.getColor();
            if(visibility.isStronglyColored()) {
                brush.setColor(Color.BLUE);
            }
            brush.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
            brush.setColor(currentColor);
        }

        if (typeVisibleProperty.isEnabled() || typeProperty.getValue().equals(WidgetType.PRESENTATION.name().toLowerCase())) {
            paintTypeName(brush, visibility);

        }
    }

    private void paintTypeName(Graphics brush, Visibility visibility) {
        brush.setColor(visibility.isStronglyColored() ? Color.BLUE : Color.GRAY);
        if(visibility.isStronglyColored() && (getClass() == ProgressFuelGauge.class)) {
            brush.setColor(Color.GRAY);
        }
        if(visibility.isWeaklyColored() && (getClass() == ProgressFuelGauge.class)) {
            brush.setColor(Color.LIGHT_GRAY);
        }
        brush.setFont(FONT);
        brush.drawString(progress.getWidgetType(WidgetType.valueOf(typeProperty.getValue().toUpperCase())).name(),
                (int) (getWidth() * 0.5d - 20d), 15);
    }

    private static void paintCloseIcon(Graphics brush, int width,
            boolean mouseOver, boolean mouseOverCloseButton) {

        if (!mouseOver) {
            //nothing to do
            return;
        }
//        if(!mouseOverCloseButton) {
//            //nothing to do
//            return;
//        }

        brush.setColor(mouseOverCloseButton ? CLOSE_BUTTON_BACKGROUND_COLOR_MOUSE_OVER_CLOSE_ICON :
                CLOSE_OR_MINIMIZE_BUTTON_BACKGROUND_COLOR);

//        if(!mouseOverCloseButton) {
//            brush.drawRect(width - CLOSE_BUTTON_SIDE - 1, 0 + 1, CLOSE_BUTTON_SIDE,
//                    CLOSE_BUTTON_SIDE);
//            brush.drawRect(width - CLOSE_BUTTON_SIDE - 1+1, 0 + 1 +1, CLOSE_BUTTON_SIDE - 2,
//                    CLOSE_BUTTON_SIDE - 2);
//            return;
//        }
        brush.fillOval(width - CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1, 0 + 1,
                CLOSE_OR_MINIMIZE_BUTTON_SIDE,
                CLOSE_OR_MINIMIZE_BUTTON_SIDE);
        brush.setColor(CLOSE_BUTTON_FOREGROUND_COLOR);
        Graphics2D brush2d = (Graphics2D) brush;
        brush2d.setStroke(new BasicStroke(2f));
        int offset = 6;
        brush.drawLine(width - CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 + offset, 0 + 1 + offset,
                width - 0 * CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 - offset,
                0 + CLOSE_OR_MINIMIZE_BUTTON_SIDE + 1 - offset);
        brush.drawLine(width - CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 + offset,
                0 + CLOSE_OR_MINIMIZE_BUTTON_SIDE + 1 - offset,
                width - 0 * CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 - offset, 0 + 1 + offset);
    }

    private static void paintMinimizeIcon(Graphics brush, int width,
            boolean mouseOver, boolean mouseOverMinimizeButton) {

        if (!mouseOver) {
            //nothing to do
            return;
        }

        brush.setColor(mouseOverMinimizeButton ? MINIMIZE_BUTTON_BACKGROUND_COLOR_MOUSE_OVER_CLOSE_ICON :
                CLOSE_OR_MINIMIZE_BUTTON_BACKGROUND_COLOR);

        brush.fillOval(width - CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 - CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1, 0 + 1,
                CLOSE_OR_MINIMIZE_BUTTON_SIDE,
                CLOSE_OR_MINIMIZE_BUTTON_SIDE);
        brush.setColor(CLOSE_BUTTON_FOREGROUND_COLOR);
        Graphics2D brush2d = (Graphics2D) brush;
        brush2d.setStroke(new BasicStroke(2f));
        int offset = 6;
        int y = ((int)(0 + 1 + CLOSE_OR_MINIMIZE_BUTTON_SIDE / 2d)) + 2;
        brush.drawLine(width - CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 - CLOSE_OR_MINIMIZE_BUTTON_SIDE
                       - 1 + offset, y,
                width - 0 * CLOSE_OR_MINIMIZE_BUTTON_SIDE - 1 - offset - CLOSE_OR_MINIMIZE_BUTTON_SIDE
                - 1,
                y);
    }

    protected void paintWidget(Graphics g) {
    }

    @Override
    public Property getVisibilityProperty() {
        return visibilityProperty;
    }

    @Override
    public Property getVisibilitySupportedColoredProperty() {
        return visibilitySupportedColoredProperty;
    }
    protected void paintSmiley(Visibility visibility, Graphics2D brush, int x,
            int y) {
        paintSmiley(visibility, brush, x, y, false);
    }
    protected void paintSmiley(Visibility visibility, Graphics2D brush, int x,
            int y, boolean paintBody) {
        paintSmiley(visibility, brush, x, y, paintBody, -1, 1);
    }
    protected void paintSmiley(Visibility visibility, Graphics2D brush, int x,
            int y, boolean paintBody, double customDonePercent, int smileyNumber) {
        if (!shouldBeSmileyPainted()) {
            if (this.smileyIcon != null) {
                this.remove(smileyIcon);
                this.smileyIcon = null;
            }
            if (this.smileyIcon2 != null) {
                this.remove(smileyIcon2);
                this.smileyIcon2 = null;
            }

            //nothing more to do
            return;
        }
        boolean colored = smileysColoredProperty.isEnabled();
        if (visibility.isGray()) {
            colored = false;
        }

        if (!colored) {
            y = y - 2;
            if (this.smileyIcon != null) {
                this.remove(smileyIcon);
                this.smileyIcon = null;
            }
            if (this.smileyIcon2 != null) {
                this.remove(smileyIcon2);
                this.smileyIcon2 = null;
            }
            Color originalColor = brush.getColor();
            if (!visibility.isStronglyColored()) {
                brush.setColor(Color.GRAY);
            }
            if (visibility.isGray()) {
                brush.setColor(Color.LIGHT_GRAY);
            }
            if (visibility.isStronglyColored()) {
                brush.setColor(Color.BLACK);
            }
            Color currentColor = brush.getColor();
            Font currentFont = brush.getFont();
            brush.setColor(visibility.isStronglyColored() ? Color.WHITE
                    : BACKGROUND_COLOR);
            brush.fillRect(
                    x, y,
                    20,
                    20
            );
            brush.setColor(currentColor);
            brush.setFont(MEDIUM_FONT);
            brush.drawString(
                    ProgressSmiley.forProgress(customDonePercent >= 0 ? customDonePercent : donePercent()).getCharacter(),
                    x + 1 + (getClass() == ProgressSwing.class ? -8 : 0), y + 16
            );
            brush.setFont(currentFont);
            brush.setColor(originalColor);
        }
        if (colored) {
            x = x + 2;
            ImageIcon imageIcon = ProgressSmileyIcon
                    .forSmiley(ProgressSmiley.forProgress(customDonePercent >= 0 ? customDonePercent : donePercent()))
                    .getIcon();
            if(smileyNumber < 2) {
                if (this.smileyIcon != null) {
                    this.remove(smileyIcon);
                    this.smileyIcon = null;
                }
                this.smileyIcon = new JLabel(imageIcon);
                smileyIcon.setBounds(x + (getClass() == ProgressSwing.class ? - 8 : 0), y, 15, 15);
                this.add(smileyIcon);
            } else {
                if (this.smileyIcon2 != null) {
                    this.remove(smileyIcon2);
                    this.smileyIcon2 = null;
                }
                this.smileyIcon2 = new JLabel(imageIcon);
                smileyIcon2.setBounds(x + (getClass() == ProgressSwing.class ? - 8 : 0), y, 15, 15);
                this.add(smileyIcon2);
            }
        }
        if(colored) {
            x = x - 2;
            y = y - 2;
        }
        if(paintBody) {
            brush.drawString(BODY, x - 5 + (TimeCalcApp.IS_RUNNING_ON_LINUX && getClass() == ProgressSwing.class ? - 4 : 0), y + 26);
            brush.drawString(LEGS, x - 5, y + 36);
        }
    }

    protected boolean shouldBeSmileyPainted() {
        return smileysVisibleProperty.isEnabled() && (mouseOver
                                                       || !smileysVisibleOnlyIfMouseMovingOverProperty
                                                               .isEnabled());
    }

    protected boolean changedInTheLastXMilliseconds(int milliseconds) {
        return (System.nanoTime() - lastUpdate) < milliseconds * 1000000;
    }

    protected void paintQuarterIcon(Graphics2D brush,
            Visibility visibility, int totalWidth, int totalHeight) {
        paintQuarterIcon(brush, visibility, totalWidth, totalHeight, -1, -1);
    }
    protected void paintQuarterIcon(Graphics2D brush,
            Visibility visibility, int totalWidth, int totalHeight, int x, int y) {
        Color currentColor = brush.getColor();
        //Color currentBackgroundColor = brush.getBackground();
        Font currentFont = brush.getFont();
        brush.setFont(BIG_FONT);
        int q = donePercent() < 0.25 ? 0 : (donePercent() < 0.5 ? 1 :
                (donePercent() < 0.75 ? 2 : (donePercent() < 1.0 ? 3 : 4)));
        Color color;
        Color backgroundColor;
        switch (visibility) {
            case STRONGLY_COLORED:
                backgroundColor = Color.WHITE;
                break;
            case WEAKLY_COLORED:
                backgroundColor = Color.LIGHT_GRAY;
                break;
            default:
                backgroundColor = Color.LIGHT_GRAY;
        }

        switch (q) {
            case 0:
                color = Battery.getColourForProgress(0.05, visibility,
                        mouseOver);
                break;
            case 1:
                color = Battery.getColourForProgress(0.25, visibility,
                        mouseOver);
                break;
            case 2:
                color = Battery.getColourForProgress(0.85, visibility,
                        mouseOver);
                break;
            case 3:
                color = Battery.getColourForProgress(0.95, visibility,
                        mouseOver);
                break;
            case 4:
                color = visibility.isStronglyColored() ? PURPLE_STRONGLY_COLORED : (visibility.isWeaklyColored() ? PURPLE_WEAKLY_COLORED : Color.GRAY);
                break;
            default:
                color = Color.LIGHT_GRAY;
        }
        brush.setColor(backgroundColor);
        if(x< 0 || y < 0) {
            brush.fillRect(((int) (totalWidth * 0.08)),
                    (donePercent() < 0.5 ? totalHeight / 4 * 3
                            : (totalHeight / 4 * 1) + 10) + -8, 20, 20);
        } else {
            brush.fillRect(x, y, 20, 20);
        }
        brush.setColor(color);
        if(x< 0 || y < 0) {
            brush.drawString(
                    String.valueOf(q), ((int) (totalWidth * 0.13)),
                    (donePercent() < 0.5 ? totalHeight / 4 * 3
                            : (totalHeight / 4 * 1) + 10) + 10
            );
        } else {
            brush.drawString(String.valueOf(q), x + 4, y + 18);
        }
        brush.setColor(currentColor);
        //brush.setBackground(currentBackgroundColor);
        brush.setFont(currentFont);
    }
    
    protected void paintCircleProgress(Graphics2D brush, Visibility visibility, int totalWidth, int totalHeight) {
        Color currentColor = brush.getColor();
        brush.setColor(
                visibility.isStronglyColored() ? HIGH_STRONGLY_COLORED
                : (visibility.isWeaklyColored() ? HIGH_WEAKLY_COLORED
                : Color.lightGray));

        double angleDouble = donePercent() * 360;

        brush.fillArc(((int) (totalWidth * 1)) - 15,
                totalHeight / 4 * 3 + 28,
                15, 15, 90, -(int) angleDouble);
        brush.setColor(
                visibility.isStronglyColored() ? LIGHT_RED
                : visibility.isWeaklyColored() ? ULTRA_LIGHT_RED
                : BACKGROUND_COLOR);
        brush.fillArc(((int) (totalWidth * 1)) - 15,
                totalHeight / 4 * 3 + 28,
                15, 15, 90, +(int) (360 - angleDouble));

        brush.setColor(currentColor);
    }

    public void hideWidget() {
        this.hiddenProperty.enable();
    }

    private String getHumanName() {
        String name = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        for (char ch : name.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append(' ').append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }

        }
        String result = sb.toString().trim();
        return result.substring(0, 1).toUpperCase() + result.substring(1, result.length());
    }
}
