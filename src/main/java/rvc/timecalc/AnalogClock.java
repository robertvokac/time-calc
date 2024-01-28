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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//https://kodejava.org/how-do-i-write-a-simple-analog-clock-using-java-2d/
public class AnalogClock extends Widget {

    public AnalogClock() {
        setPreferredSize(new Dimension(400, 300));

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Analog Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AnalogClock());
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.side = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        GregorianCalendar time = new GregorianCalendar();
        int second = time.get(Calendar.SECOND);
        int minute = time.get(Calendar.MINUTE);
        int hour = time.get(Calendar.HOUR_OF_DAY);

        // Draw clock numbers and circle
        drawClockFace(g2d, centerX, centerY, side / 2 - 40);

        drawHand(g2d, side / 2 - 10, second / 60.0, 0.5f, Color.RED);

        if(TimeCalcConf.getInstance().areClockHandsLong()) drawHand(g2d, (side / 2 - 10) / 4,
                (second > 30 ? second - 30 : second + 30) / 60.0, 0.5f,
                Color.RED);
        //
        double minutes = minute / 60.0 + second / 60.0 / 60.0;
        drawHand(g2d, side / 2 - 20, minutes, 2.0f,
                Color.BLUE);
        if(TimeCalcConf.getInstance().areClockHandsLong()) drawHand(g2d, (side / 2 - 20) / 4,
                minutes + minutes > 0.5 ? minutes - 0.5 :
                        minutes + (minutes > 0.5 ? (-1) : 1) * 0.5, 2.0f,
                Color.BLUE);
        //
        double hours = hour / 12.0 + minute / 60.0 / 12 + second / 60 / 60 / 12;
        drawHand(g2d, side / 2 - 40,
                hours, 4.0f,
                Color.BLACK);
        if(TimeCalcConf.getInstance().areClockHandsLong()) drawHand(g2d, (side / 2 - 40) / 4, hours + hours > 0.5 ? hours - 0.5 :
                        hours + (hours > 0.5 ? (-1) : 1) * 0.5, 4.0f,
                Color.BLACK);
        drawCentre(g2d, centerX, centerY);

    }

    private void drawCentre(Graphics2D g2d, int centerX, int centerY) {
        Color currentColor = g2d.getColor();
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.RED :
                FOREGROUND_COLOR);
        g2d.fillOval(centerX - 3, centerY - 3, 8, 8);
        g2d.setColor(currentColor);
    }

    private void drawHand(Graphics2D g2d, int length, double value,
            float stroke, Color color) {
        length = length - 4;
        double angle = Math.PI * 2 * (value - 0.25);
        int endX = (int) (getWidth() / 2 + length * Math.cos(angle));
        int endY = (int) (getHeight() / 2 + length * Math.sin(angle));

        g2d.setColor((Utils.highlighted.get() || mouseOver) ? color :
                FOREGROUND_COLOR);
        g2d.setStroke(new BasicStroke(stroke));
        g2d.drawLine(getWidth() / 2, getHeight() / 2, endX, endY);
    }

    private void drawClockFace(Graphics2D g2d, int centerX, int centerY,
            int radius) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(Utils.highlighted.get() || mouseOver ? Color.BLACK :
                FOREGROUND_COLOR);
        //        System.out.println("centerX=" + centerX);
        //        System.out.println("centerY=" + centerY);
        //        System.out.println("radius=" + radius);
        g2d.drawOval(1, 1, centerX * 2 - 4, centerY * 2 - 4);
        g2d.drawOval(2, 2, centerX * 2 - 4, centerY * 2 - 4);

        //        g2d.drawOval(3, 3, centerX * 2 - 6, centerY * 2 - 6);
        //        g2d.drawOval(4, 4, centerX * 2 - 8, centerY * 2 - 8);

        DateFormat formatter2 =
                new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String now = formatter2.format(new Date());

        for (int i = 1; i <= 12; i++) {
            double angle = Math.PI * 2 * (i / 12.0 - 0.25);
            int dx = centerX + (int) ((radius + 20) * Math.cos(angle)) - 4;
            int dy = centerY + (int) ((radius + 20) * Math.sin(angle)) + 4;

            int seconds = Integer.valueOf(now.split(":")[2]);


            g2d.setFont(new Font("sans", Font.BOLD, 16));
            g2d.drawString(Integer.toString(i), dx, dy);
        }

    }
    public int getTimerDelay() {
        return 20;
    }
}