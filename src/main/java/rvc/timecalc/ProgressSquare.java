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

public class ProgressSquare extends JPanel {

    private static final Color FOREGROUND_COLOR = new Color(220, 220, 220);
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    private int side = 0;
    private int square;
    private double donePercent = 0;

    private boolean highlight = false;

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

    public ProgressSquare() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(BACKGROUND_COLOR);
        new Timer(100, e -> repaint()).start();
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
        if (side == 0) {
            this.side = Math.min(getWidth(), getHeight());
            this.square = side * side;
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //        System.out.println("square=" + square);
        int dotNumber = (int) (donePercent * square);
        int y = dotNumber / side;
        int yOrig = y;
        int x = dotNumber - y * side;

        //        System.out.println("dotNumber=" + dotNumber);
        //        System.out.println("x=" + x);
        //        System.out.println("y=" + y);
        if (y > 1) {
            if(highlight) g2d.setColor(Color.GRAY);
            g2d.fillRect(side - 4, side - 4, 4, 4);
            g2d.fillRect(1, side - 4, 4, 4);

            g2d.setColor(FOREGROUND_COLOR);
            g2d.fillRect(1, 1, side, y - 1);
            if (x > 1) {
                if(highlight) g2d.setColor(Color.GRAY);
                g2d.drawRect(1, y, x - 1, 1);
            }
            if(highlight) g2d.setColor(Color.GRAY);
            g2d.fillRect(side - 4, 1, 4, 4);
            g2d.fillRect(1, 1, 4, 4);

            if(highlight) g2d.setColor(Color.GRAY);
            g2d.drawLine(1, 1, x, y);
            //            g2d.drawLine(1+1, 1+1, x+1, y+1);
            g2d.drawLine(1, 1 + 1, x, y + 1);
            g2d.drawLine(1, 1 + 1, x, y + 1);
            if(highlight) {
                g2d.setColor(Color.BLUE);
                g2d.drawLine(x - 10, y - 10, x + 10, y + 10);
                g2d.drawLine(x + 10, y - 10, x - 10, y + 10);
            }
            g2d.setColor(FOREGROUND_COLOR);
        }
//        int nextX = (int) (Math.random() * 200);
//        int nextY = (int) (Math.random() * (yOrig- 1));
//        for(int i = 0;i< yOrig / 8;i++) {
//            g2d.setColor(Color.GRAY/*Utils.getRandomColor()*/);
//            g2d.drawLine(x, y, nextX, nextY);
//            x = nextX;
//            y = nextY;
//            nextX = (int) (Math.random() * 200);
//            nextY = (int) (Math.random() * (yOrig - 1));
//        }

    }

}