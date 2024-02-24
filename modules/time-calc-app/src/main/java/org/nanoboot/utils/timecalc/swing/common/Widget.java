package org.nanoboot.utils.timecalc.swing.common;

import org.nanoboot.utils.timecalc.app.GetProperty;
import org.nanoboot.utils.timecalc.entity.Visibility;
import org.nanoboot.utils.timecalc.swing.progress.ProgressSmileyIcon;
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
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class Widget extends JPanel implements
        GetProperty {
    protected static final Color FOREGROUND_COLOR = new Color(220, 220, 220);
    protected static final Color FOREGROUND_COLOR2 = new Color(210, 210, 210);
    protected static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    protected static final Font BIG_FONT = new Font("sans", Font.BOLD, 24);
    protected static final Font MEDIUM_FONT = new Font("sans", Font.BOLD, 16);
    public static final int CLOSE_BUTTON_SIDE = 15;

    public StringProperty visibilityProperty =
            new StringProperty("widget.visibilityProperty",
                    Visibility.STRONGLY_COLORED.name());
    public final BooleanProperty smileysColoredProperty =
            new BooleanProperty("smileysColoredProperty", true);
    public final BooleanProperty visibilitySupportedColoredProperty =
            new BooleanProperty("visibilitySupportedColoredProperty", true);
    public final BooleanProperty visibleProperty =
            new BooleanProperty("visibleProperty", true);

    protected int side = 0;
    protected double donePercent = 0;
    protected boolean mouseOver = false;
    protected boolean mouseOverCloseButton = false;
    protected JLabel smileyIcon;

    public Widget() {
        setBackground(BACKGROUND_COLOR);
        new Timer(getTimerDelay(), e -> repaint()).start();
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

                int x=e.getX();
                int y=e.getY();
                mouseOverCloseButton = x >= getWidth() - CLOSE_BUTTON_SIDE && y <= CLOSE_BUTTON_SIDE;
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(mouseOverCloseButton) {
                    visibleProperty.setValue(false);
                    return;
                }

                if(visibilitySupportedColoredProperty.isDisabled()) {
                    //nothing to do
                    return;
                }
                Visibility visibility =
                        Visibility.valueOf(visibilityProperty.getValue());
                if (visibility.isStronglyColored()) {
                    visibilityProperty
                            .setValue(Visibility.WEAKLY_COLORED.name());
                } else {
                    visibilityProperty
                            .setValue(Visibility.STRONGLY_COLORED.name());
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
    }

    public int getTimerDelay() {
        return 100;
    }

    public final void setDonePercent(double donePercent) {
        if (donePercent > 1) {
            donePercent = 1;
        }
        if (donePercent < 0) {
            donePercent = 0;
        }
        this.donePercent = donePercent;
    }

    public void setBounds(int x, int y, int side) {
        setBounds(x, y, side, side);
    }

    @Override
    public final void paintComponent(Graphics brush) {
        super.paintComponent(brush);
        setVisible(visibleProperty.isEnabled());

        Visibility visibility =
                Visibility.valueOf(visibilityProperty.getValue());
        this.setVisible(visibility != Visibility.NONE);
        paintWidget(brush);

        if (mouseOver && mouseOverCloseButton) {
            brush.setColor(SwingUtils.CLOSE_BUTTON_BACKGROUND_COLOR);
            brush.fillOval(getWidth() - CLOSE_BUTTON_SIDE - 1 , 0 + 1,CLOSE_BUTTON_SIDE,CLOSE_BUTTON_SIDE);
            brush.setColor(Color.LIGHT_GRAY);
            Graphics2D brush2d = (Graphics2D) brush;
            brush2d.setStroke(new BasicStroke(2f));
            brush.drawLine(getWidth() - CLOSE_BUTTON_SIDE - 1 + 2 ,0 + 1 + 2, getWidth() - 0 * CLOSE_BUTTON_SIDE - 1 - 2 ,0 + CLOSE_BUTTON_SIDE + 1 - 2);
            brush.drawLine(getWidth() - CLOSE_BUTTON_SIDE - 1 + 2, 0 + CLOSE_BUTTON_SIDE + 1 - 2, getWidth() - 0 * CLOSE_BUTTON_SIDE - 1 - 2 ,0 + 1 + 2);
        }

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

    protected void paintSmiley(Visibility visibility, Graphics2D brush, int x, int y) {
        if(!mouseOver) {
            if(this.smileyIcon != null) {
                this.remove(smileyIcon);
            }

            //nothing more to do
            return;
        }
        boolean colored = smileysColoredProperty.isEnabled();
        if(visibility.isGray()) {
            colored = false;
        }

        if(!colored){
            if(!visibility.isStronglyColored()) {
                brush.setColor(Color.GRAY);
            }
            if(visibility.isGray()) {
                brush.setColor(Color.LIGHT_GRAY);
            }
            if(visibility.isStronglyColored()) {
                brush.setColor(Color.BLACK);
            }
            Color currentColor= brush.getColor();
            brush.setColor(visibility.isStronglyColored() ? Color.WHITE : BACKGROUND_COLOR);
            brush.fillRect(
                    x,y,
                    20,
                    20
            );
            brush.setColor(currentColor);
            brush.setFont(MEDIUM_FONT);
            brush.drawString(
                    ProgressSmiley.forProgress(donePercent).getCharacter(),
                    x,y + 16
            );
        }
        if(colored) {
            ImageIcon imageIcon = ProgressSmileyIcon.forSmiley(ProgressSmiley.forProgress(donePercent)).getIcon();
            if(this.smileyIcon != null) {
                this.remove(smileyIcon);
            }
            this.smileyIcon = new JLabel(imageIcon);
            smileyIcon.setBounds(x,y,15, 15);
            this.add(smileyIcon);
        }
    }

}
