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
import java.io.IOException;

public class Battery extends JPanel {

    private static final Color FOREGROUND_COLOR = new Color(220, 220, 220);
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    public static final Color LOW = new Color(255, 102, 102);
    public static final Color MEDIUM = new Color(255, 204, 153);
    public static final Color HIGH = new Color(204, 255, 204);
    public static final Color HIGHEST = new Color(153, 255, 153);
    private int height_ = 0;
    private int square;
    private double donePercent = 0;

    private boolean highlight = false;
    private int width_;

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        if(highlight && !Utils.highlightTxt.exists()) {
            try {
                Utils.highlightTxt.createNewFile();
            } catch (IOException ioException) {
                System.out.println(ioException);
            }
        }
        if(!highlight && Utils.highlightTxt.exists()) {
            Utils.highlightTxt.delete();
        }
    }

    public Battery() {
        setPreferredSize(new Dimension(40, 100));
        setBackground(BACKGROUND_COLOR);
        new Timer(1000, e -> repaint()).start();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                highlight = !highlight;
                if(highlight && !Utils.highlightTxt.exists()) {
                    try {
                        Utils.highlightTxt.createNewFile();
                    } catch (IOException ioException) {
                        System.out.println(e);
                    }
                }
                if(!highlight && Utils.highlightTxt.exists()) {
                    Utils.highlightTxt.delete();
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

            }

            @Override
            public void mouseExited(MouseEvent e) {

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
            this.width_= (int)(this.height_* 0.6);
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillRect(width_/4,0,width_, height_);
        g2d.setColor(donePercent < 0.1 ? LOW : (donePercent < 0.75 ?
                MEDIUM : (donePercent < 0.9 ? HIGH : HIGHEST)));
        g2d.fillRect(width_/4,height_ - (int)(height_ * donePercent),width_, (int)(height_ * donePercent));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(String.valueOf((int)(donePercent * 100)) + "%",width_/2, height_/2);

    }

}