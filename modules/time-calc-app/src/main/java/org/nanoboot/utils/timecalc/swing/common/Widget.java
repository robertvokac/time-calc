package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.app.TimeCalcProperty;
import org.nanoboot.utils.timecalc.entity.Progress;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.entity.WidgetType;
import org.nanoboot.utils.timecalc.swing.progress.Battery;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSmileyIcon;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSwing;
import org.nanoboot.utils.timecalc.utils.common.ProgressSmiley;
import org.nanoboot.utils.timecalc.utils.property.BooleanProperty;
import org.nanoboot.utils.timecalc.utils.property.Property;
import org.nanoboot.utils.timecalc.utils.property.StringProperty;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class Widget extends JPanel implements
        GetProperty {

    private static final int CLOSE_BUTTON_SIDE = 25;
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
    public static final Color CLOSE_BUTTON_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public static final Color CLOSE_BUTTON_BACKGROUND_COLOR_MOUSE_OVER_CLOSE_ICON = new Color(255, 153, 153);
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
    protected int side = 0;
    protected Progress progress = null;
    protected boolean mouseOver = false;
    private boolean mouseOverCloseButton = false;
    protected JLabel smileyIcon;
    protected JLabel smileyIcon2;
    private long lastUpdate = System.nanoTime();
    private static final Color PURPLE_STRONGLY_COLORED = new Color(153,51,255);
    private static final Color PURPLE_WEAKLY_COLORED = new Color(204,153,255);

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
                mouseOverCloseButton = x >= getWidth() - CLOSE_BUTTON_SIDE
                        && y <= CLOSE_BUTTON_SIDE;
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (mouseOverCloseButton) {
                    visibleProperty.setValue(false);
                    return;
                }

                if (visibilitySupportedColoredProperty.isDisabled()) {
                    //nothing to do
                    return;
                }
//                if (visibleProperty.isEnabled()) {
//                    Visibility visibility
//                            = Visibility.valueOf(visibilityProperty.getValue());
//                    if (visibility.isStronglyColored()) {
//                        visibilityProperty
//                                .setValue(Visibility.WEAKLY_COLORED.name());
//                    } else {
//                        visibilityProperty
//                                .setValue(Visibility.STRONGLY_COLORED.name());
//                    }
//                }
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
                    List<JMenu> additionalMenus = createAdditionalMenus();
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
    protected List<JMenu> createAdditionalMenus() {
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

        double oldDonePercent = this.progress == null ? 0 : this.progress.get(WidgetType.DAY);
        int oldDonePercentInt1000Mil = (int) (oldDonePercent * 1000000000);

        int newDonePercentInt1000Mil = (int) (newProgress.get(WidgetType.DAY) * 1000000000);
        if (newDonePercentInt1000Mil != oldDonePercentInt1000Mil) {
            lastUpdate = System.nanoTime();
        }
        this.progress = newProgress;
    }

    protected double donePercent() {
        if(progress == null) {
            return 0;
        }
        return progress.get(WidgetType.valueOf(typeProperty.getValue().toUpperCase(
                Locale.ROOT)));
    }

    public void setBounds(int x, int y, int side) {
        setBounds(x, y, side, side);
    }

    @Override
    public final void paintComponent(Graphics brush) {
        super.paintComponent(brush);

        setVisible(visibleProperty.isEnabled());

        if (visibleProperty.isDisabled()) {
            //nothing to do
            return;
        }
        Visibility visibility
                = Visibility.valueOf(visibilityProperty.getValue());
        super.setVisible(
                visibility != Visibility.NONE && visibleProperty.isEnabled());
        paintWidget(brush);

        paintCloseIcon(brush, getWidth(), mouseOver, mouseOverCloseButton);

        if (mouseOver) {
            brush.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
        }
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

        brush.setColor(mouseOverCloseButton ? CLOSE_BUTTON_BACKGROUND_COLOR_MOUSE_OVER_CLOSE_ICON : CLOSE_BUTTON_BACKGROUND_COLOR);

//        if(!mouseOverCloseButton) {
//            brush.drawRect(width - CLOSE_BUTTON_SIDE - 1, 0 + 1, CLOSE_BUTTON_SIDE,
//                    CLOSE_BUTTON_SIDE);
//            brush.drawRect(width - CLOSE_BUTTON_SIDE - 1+1, 0 + 1 +1, CLOSE_BUTTON_SIDE - 2,
//                    CLOSE_BUTTON_SIDE - 2);
//            return;
//        }
        brush.fillOval(width - CLOSE_BUTTON_SIDE - 1, 0 + 1, CLOSE_BUTTON_SIDE,
                CLOSE_BUTTON_SIDE);
        brush.setColor(CLOSE_BUTTON_FOREGROUND_COLOR);
        Graphics2D brush2d = (Graphics2D) brush;
        brush2d.setStroke(new BasicStroke(2f));
        int offset = 6;
        brush.drawLine(width - CLOSE_BUTTON_SIDE - 1 + offset, 0 + 1 + offset,
                width - 0 * CLOSE_BUTTON_SIDE - 1 - offset,
                0 + CLOSE_BUTTON_SIDE + 1 - offset);
        brush.drawLine(width - CLOSE_BUTTON_SIDE - 1 + offset,
                0 + CLOSE_BUTTON_SIDE + 1 - offset,
                width - 0 * CLOSE_BUTTON_SIDE - 1 - offset, 0 + 1 + offset);
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
            brush.drawString(BODY, x - 5, y + 26);
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
}
