package rvc.timecalc;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Battery extends JPanel {

    public static final Color LOW = new Color(253, 130, 130);
    public static final Color MEDIUM = new Color(255, 204, 153);
    public static final Color HIGH = new Color(204, 255, 204);
    public static final Color HIGHEST = new Color(153, 255, 153);
    public static final Color LOW_HIGHLIGHTED = Color.red;
    public static final Color MEDIUM_HIGHLIGHTED = Color.ORANGE;
    public static final Color HIGH_HIGHLIGHTED = new Color(158, 227, 158);
    public static final Color HIGHEST_HIGHLIGHTED = Color.green;
    private static final Color FOREGROUND_COLOR = new Color(220, 220, 220);
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    NumberFormat formatter3 = new DecimalFormat("#0.000");
    private int height_ = 0;
    private double donePercent = 0;
    private boolean mouseOver = false;
    private int width_;

    public Battery() {
        setPreferredSize(new Dimension(40, 100));
        setBackground(BACKGROUND_COLOR);
        new Timer(100, e -> repaint()).start();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Utils.highlighted.flip();
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

    public void setDonePercent(double donePercent) {
        this.donePercent = donePercent;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (height_ == 0) {
            this.height_ = Math.min(getWidth(), getHeight());
            this.width_ = (int) (this.height_ * 0.6);
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.YELLOW :
                FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillRect(width_ / 4, 1, width_, height_ - 2);
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);
        g2d.drawRect(width_ / 4 - 1, 0, width_ + 1, height_ + 0);
        if (Utils.highlighted.get() || mouseOver) {
            g2d.setColor(
                    donePercent < 0.1 ? LOW_HIGHLIGHTED : (donePercent < 0.75 ?
                            MEDIUM_HIGHLIGHTED :
                            (donePercent < 0.9 ? HIGH_HIGHLIGHTED :
                                    HIGHEST_HIGHLIGHTED)));
        } else {
            g2d.setColor(donePercent < 0.1 ? LOW : (donePercent < 0.75 ?
                    MEDIUM : (donePercent < 0.9 ? HIGH : HIGHEST)));
        }
        g2d.fillRect(width_ / 4, height_ - (int) (height_ * donePercent),
                width_, (int) (height_ * donePercent));
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                Color.LIGHT_GRAY);
        g2d.drawString(
                formatter3.format(donePercent * 100) + "%",
                ((int) (width_ * 0.4)), height_ / 2);

    }

}