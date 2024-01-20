package rvc.timecalc;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Calendar;
import java.util.GregorianCalendar;

//https://kodejava.org/how-do-i-write-a-simple-analog-clock-using-java-2d/
public class AnalogClock extends JPanel {

    private static final Color FOREGROUND_COLOR  = new Color(230, 230, 230);
    private static final Color BACKGROUND_COLOR  = new Color(240,240,240);

    public AnalogClock() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(BACKGROUND_COLOR);
        new Timer(1000, e -> repaint()).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int side = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        GregorianCalendar time = new GregorianCalendar();
        int second = time.get(Calendar.SECOND);
        int minute = time.get(Calendar.MINUTE);
        int hour = time.get(Calendar.HOUR_OF_DAY);

        drawHand(g2d, side / 2 - 10, second / 60.0, 0.5f, Color.RED);
        drawHand(g2d, side / 2 - 20, minute / 60.0 + second / 60.0 / 60.0, 2.0f, Color.BLUE);
        drawHand(g2d, side / 2 - 40, hour / 12.0 + minute / 60.0 / 12 + second / 60 / 60 / 12, 4.0f, Color.BLACK);

        // Draw clock numbers and circle
        drawClockFace(g2d, centerX, centerY, side / 2 - 40);
    }

    private void drawHand(Graphics2D g2d, int length, double value,
            float stroke, Color color) {
        length= length - 4;
        double angle = Math.PI * 2 * (value - 0.25);
        int endX = (int) (getWidth() / 2 + length * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + length * Math.sin(angle));

        g2d.setColor(FOREGROUND_COLOR);
        g2d.setStroke(new BasicStroke(stroke));
        g2d.drawLine(getWidth() / 2, getHeight() / 2, endX, endY);
    }

    private void drawClockFace(Graphics2D g2d, int centerX, int centerY,
            int radius) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(FOREGROUND_COLOR);
        System.out.println("centerX=" + centerX);
        System.out.println("centerY=" + centerY);
        System.out.println("radius=" + radius);
        g2d.drawOval(1, 1, centerX * 2 - 4, centerY * 2 - 4);
        g2d.drawOval(2, 2, centerX * 2 - 4, centerY * 2 - 4);
//        g2d.drawOval(3, 3, centerX * 2 - 6, centerY * 2 - 6);
//        g2d.drawOval(4, 4, centerX * 2 - 8, centerY * 2 - 8);

        for (int i = 1; i <= 12; i++) {
            double angle = Math.PI * 2 * (i / 12.0 - 0.25);
            int dx = centerX + (int) ((radius + 20) * Math.cos(angle)) - 4;
            int dy = centerY + (int) ((radius + 20) * Math.sin(angle)) + 4;

            g2d.setFont(new Font("sans", Font.BOLD, 16));
            g2d.drawString(Integer.toString(i), dx, dy);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Analog Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AnalogClock());
        frame.pack();
        frame.setVisible(true);
    }
}