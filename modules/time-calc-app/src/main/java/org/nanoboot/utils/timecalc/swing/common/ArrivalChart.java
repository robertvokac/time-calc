package org.nanoboot.utils.timecalc.swing.common;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.nanoboot.utils.timecalc.utils.common.NumberFormats;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Robert Vokac
 * @since 20.02.2024
 */
public class ArrivalChart extends JPanel {
    private static final Color ORANGE = new Color(237, 125, 49);
    private static final Color BLUE = new Color(68, 114, 196);
    private static final Color BROWN = new Color(128,0, 64);
    private static final Color PURPLE = new Color(128,0, 255);
    public static final Rectangle EMPTY_RECTANGLE = new Rectangle();

    public ArrivalChart(ArrivalChartData data, int width) {
        this(data.getDays(), data.getArrival(), data.getTarget(), data.getMa7(), data.getMa14(), data.getMa28(), data.getMa56(),
        data.getStartDate(), data.getEndDate(), width);
    }
    public ArrivalChart(String[] days, double[] arrival, double target, double[] ma7,
            double[] ma14, double[] ma28, double[] ma56, String startDate, String endDate, int width) {
        this.setLayout(null);

        this.setVisible(true);
        //
        String title = "Arrivals";

        List<TimeSeries> timeSeries=
                createSeries(days, arrival, target, ma7, ma14, ma28, ma56, title, startDate, endDate);
        JFreeChart chart = createChart(timeSeries, title);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.white);
        chartPanel.setDomainZoomable(true);
        chartPanel.setMouseZoomable(true);
        this.add(chartPanel);
        chartPanel.setBounds(10, 10, width, 400);

    }

    public static JFreeChart createChart(List<TimeSeries> timeSeries,
            String title) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                "Date",
                "Value",
                new TimeSeriesCollection(new TimeSeries("Date"))
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        for(int i = 0; i < 6; i++) {
            plot.setDataset(i, new TimeSeriesCollection(timeSeries.get(i)));
        }


        chart.setBorderVisible(false);
        BiConsumer<Integer, Color> setSeries = (i, c) -> {
            XYItemRenderer renderer = new XYLineAndShapeRenderer();

//            renderer.setDefaultPaint(c);
//            renderer.setDefaultStroke(new BasicStroke(timeSeries.get(0).getItemCount() > 180 ? 1.0f : (i == 1 || i == 2 ? 1.5f : 1.25f)));
//            renderer.setDefaultShape(EMPTY_RECTANGLE);
            renderer.setSeriesPaint(0, c);
            renderer.setSeriesStroke(0, new BasicStroke(i == 1 || i == 2 ? 2.5f : 1.5f));
            renderer.setSeriesShape(0, EMPTY_RECTANGLE);
            plot.setRenderer(i, renderer);
        };
        setSeries.accept(0, Color.GRAY);
        setSeries.accept(1, ORANGE);
        setSeries.accept(2, BLUE);
        setSeries.accept(3, Color.GREEN);
        setSeries.accept(4, BROWN);
        setSeries.accept(5, PURPLE);

        plot.setBackgroundPaint(Color.white);

//        plot.setRangeGridlinesVisible(true);
//        plot.setRangeGridlinePaint(Color.BLACK);
//
//        plot.setDomainGridlinesVisible(true);
//        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

//        CategoryAxis domainAxis = plot.getDomainAxis();
//        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
//        domainAxis.setLabelFont(SwingUtils.VERY_SMALL_FONT);
//        domainAxis.setTickLabelFont(SwingUtils.VERY_SMALL_FONT);
//        domainAxis.setCategoryLabelPositionOffset(10);
//        domainAxis.setTickLabelsVisible(true);
        chart.setTitle(new TextTitle(title,
                        new Font("Serif", Font.BOLD, 18)
                )
        );

        return chart;
    }

    private List<TimeSeries> createSeries(String[] days, double[] arrival, double target,
            double[] ma7, double[] ma14, double[] ma28,
            double[] ma56, String title, String startDate, String endDate) {
        if (startDate == null) {
            startDate = "0-0-0";
        }
        if (endDate == null) {
            endDate = "0-0-0";
        }
        List<TimeSeries> result = new ArrayList<>();

        final TimeSeries seriesArrival = new TimeSeries( "Arrival" );
        final TimeSeries seriesTarget = new TimeSeries( "Target (" + NumberFormats.FORMATTER_TWO_DECIMAL_PLACES.format(target) + " h)");
        final TimeSeries seriesMa7 = new TimeSeries( "MA7" );
        final TimeSeries seriesMa14 = new TimeSeries( "MA14" );
        final TimeSeries seriesMa28 = new TimeSeries( "MA28" );
        final TimeSeries seriesMa56 = new TimeSeries( "MA56" );
        result.add(seriesArrival);
        result.add(seriesTarget);
        result.add(seriesMa7);
        result.add(seriesMa14);
        result.add(seriesMa28);
        result.add(seriesMa56);
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
            Day day3 = new Day(day, month, year);

            seriesArrival.add(day3, new Double(arrival[i]));
            seriesTarget.add(day3, new Double(0d));
            seriesMa7.add(day3, new Double(ma7[i]));
            seriesMa14.add(day3, new Double(ma14[i]));
            seriesMa28.add(day3, new Double(ma28[i]));
            seriesMa56.add(day3, new Double(ma56[i]));

        }

        return result;
    }
}
