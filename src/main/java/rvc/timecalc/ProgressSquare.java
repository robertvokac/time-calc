package rvc.timecalc;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressSquare extends JPanel {

    private static final Color FOREGROUND_COLOR = new Color(220, 220, 220);
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);
    private int side = 0;
    private int square;


    private double donePercent = 0;

    public void setDonePercent(double donePercent) {
        this.donePercent = donePercent;
    }


    public ProgressSquare() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(BACKGROUND_COLOR);
        new Timer(1000, e -> repaint()).start();

    }

    @Override
    public void paintComponent(Graphics g) {

        if(side == 0) {
            this.side = Math.min(getWidth(), getHeight());
            this.square = side * side;
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(FOREGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

//        System.out.println("square=" + square);
        int dotNumber = (int)(donePercent * square);
        int y = dotNumber / side;
        int x = dotNumber - y * side;

//        System.out.println("dotNumber=" + dotNumber);
//        System.out.println("x=" + x);
//        System.out.println("y=" + y);
        if(y > 1) {
            g2d.setColor(Color.GRAY);
        g2d.fillRect(side - 4, side - 4, 4, 4);
        g2d.fillRect(1, side - 4, 4, 4);

            g2d.setColor(FOREGROUND_COLOR);
        g2d.fillRect(1, 1, side, y - 1);
        if(x>1) {
            g2d.drawRect(1, y, x - 1, 1);
        }
            g2d.setColor(Color.GRAY);
            g2d.fillRect(side - 4, 1, 4, 4);
            g2d.fillRect(1, 1, 4, 4);
        g2d.setColor(Color.BLUE);
        g2d.drawLine(x-5, y-5, x+5, y+5);
        g2d.drawLine(x+5, y-5, x-5, y+5);
        g2d.setColor(FOREGROUND_COLOR);
        }
    }

}