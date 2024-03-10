package org.nanoboot.utils.timecalc.swing.common;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class ArrivalChart extends JPanel {

    public ArrivalChart(ArrivalChartData data) {
        this(data.getDays(), data.getArrival(), data.getMa7(), data.getMa14(), data.getMa28(), data.getMa56(), data.getTarget(),
        data.getStartDate(), data.getEndDate());
    }
    public ArrivalChart(String[] days, double[] arrival, double[] ma7,
            double[] ma14, double[] ma28, double[] ma56, double target, String startDate, String endDate) {
        this.setLayout(null);

        this.setVisible(true);
        //
        String title = "Arrivals";

        CategoryDataset dataset =
                createDataset(days, arrival, ma7, ma14, ma28, ma56, target, title, startDate, endDate);
        JFreeChart chart = createChart(dataset, title);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.white);
        chartPanel.setDomainZoomable(true);
        chartPanel.setMouseZoomable(true);
        this.add(chartPanel);
        chartPanel.setBounds(10, 10, 800, 400);

    }

    public static JFreeChart createChart(CategoryDataset dataset,
            String title) {

        JFreeChart chart = ChartFactory.createLineChart(
                title,
                "Date",
                title,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
//        Plot plot = chart.getPlot();
//
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        chart.setBorderVisible(false);
//        BiConsumer<Integer, Color> setSeries = (i, c) -> {
//            renderer.setSeriesPaint(i, c);
//            renderer.setSeriesStroke(i, new BasicStroke(1.0f));
//            renderer.setSeriesShape(i, new Rectangle());
//        };
//        setSeries.accept(0, Color.GRAY);
//        setSeries.accept(1, Color.BLACK);
//        setSeries.accept(2, Color.RED);
//        setSeries.accept(3, Color.GREEN);
//        setSeries.accept(4, Color.BLUE);
//        setSeries.accept(5, Color.ORANGE);
//
//        plot.setRenderer(renderer);
//        plot.setBackgroundPaint(Color.white);
//
//        plot.setRangeGridlinesVisible(true);
//        plot.setRangeGridlinePaint(Color.BLACK);
//
//        plot.setDomainGridlinesVisible(true);
//        plot.setDomainGridlinePaint(Color.BLACK);

//        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(title,
                        new Font("Serif", Font.BOLD, 18)
                )
        );

        return chart;
    }

    private CategoryDataset createDataset(String[] days, double[] arrival,
            double[] ma7, double[] ma14, double[] ma28,
            double[] ma56, double target, String title, String startDate, String endDate) {
        if (startDate == null) {
            startDate = "0-0-0";
        }
        if (endDate == null) {
            endDate = "0-0-0";
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String seriesArrival = "Arrival";
        String seriesMa7 = "MA7";
        String seriesMa14 = "MA14";
        String seriesMa28 = "MA28";
        String seriesMa56 = "MA56";
        String seriesTarget = "Target";

        String[] dayArray0 = startDate.split("-");
        int year1 = Integer.valueOf(dayArray0[0]);
        int month1 = Integer.valueOf(dayArray0[1]);
        int day1 = Integer.valueOf(dayArray0[2]);
        String[] dayArray1 = endDate.split("-");
        int year2 = Integer.valueOf(dayArray1[0]);
        int month2 = Integer.valueOf(dayArray1[1]);
        int day2 = Integer.valueOf(dayArray1[2]);
        for (int i = 0; i < days.length; i++) {
            String date = days[i];
            String[] dayArray = date.split("-");
            int year = Integer.valueOf(dayArray[0]);
            int month = Integer.valueOf(dayArray[1]);
            int day = Integer.valueOf(dayArray[2]);
            if(year1 != 0) {
                if(month < month1) {
                    continue;
                }
                if(month == month1 && day < day1) {
                    continue;
                }
            }

            if(year2 != 0) {
                if(month > month2) {
                    continue;
                }
                if(month == month2 && day > day2) {
                    continue;
                }
            }
            dataset.addValue(arrival[i], seriesArrival, date);
            dataset.addValue(ma7[i], seriesMa7, date);
            dataset.addValue(ma14[i], seriesMa14, date);
            dataset.addValue(ma28[i], seriesMa28, date);
            dataset.addValue(ma56[i], seriesMa56, date);
            dataset.addValue(target, seriesTarget, date);
        }

        return dataset;
    }
}
