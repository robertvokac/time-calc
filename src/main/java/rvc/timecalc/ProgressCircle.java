package rvc.timecalc;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class ProgressCircle extends JPanel {

    private static final Color FOREGROUND_COLOR = new Color(220,220,220);
    private static final Color FOREGROUND_COLOR2 = new Color(210,210,210);
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    private int side = 0;
    private double donePercent = 0;

    private boolean highlight = false;

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        if (highlight && !Utils.highlightTxt.exists()) {
            try {
                Utils.highlightTxt.createNewFile();
            } catch (IOException ioException) {
                System.out.println(ioException);
            }
        }
        if (!highlight && Utils.highlightTxt.exists()) {
            Utils.highlightTxt.delete();
        }
    }

    public ProgressCircle() {
        setPreferredSize(new Dimension(200, 200));
        setBackground(BACKGROUND_COLOR);
        new Timer(100, e -> repaint()).start();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                highlight = !highlight;
                if (highlight && !Utils.highlightTxt.exists()) {
                    try {
                        Utils.highlightTxt.createNewFile();
                    } catch (IOException ioException) {
                        System.out.println(e);
                    }
                }
                if (!highlight && Utils.highlightTxt.exists()) {
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
        highlight = Utils.highlightTxt.exists();
        if (side == 0) {
            this.side = Math.min(getWidth(), getHeight());
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(highlight ? Color.darkGray : FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
//        if (highlight) {
//            g2d.setColor(Color.BLUE);
//        }

        double angleDouble = donePercent * 360;
//        System.out.println("angleDouble=" + angleDouble);
        ////

        double angleDouble2 = (angleDouble - (int)(angleDouble)) * 360;
//        System.out.println("remainingAngle=" + angleDouble2);

        g2d.fillArc(0,0,side,side,90, -(int) angleDouble);
        int side2 = ((int)(side/2));
        g2d.setColor(highlight ? new Color(105, 175, 236) : FOREGROUND_COLOR2);
        g2d.fillArc(0+(side2/2),0+(side2/2),side2, side2,90, -(int) angleDouble2);
    }

}