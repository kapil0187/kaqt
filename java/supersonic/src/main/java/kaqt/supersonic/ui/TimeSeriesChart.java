package kaqt.supersonic.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import kaqt.supersonic.ui.utils.UiColors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class TimeSeriesChart extends ApplicationFrame {
	private TimeSeries series;	
	private double lastValue;
	
	@SuppressWarnings("deprecation")
	public TimeSeriesChart(JPanel panel, String title, double initialEquity) {
		super(title);
		
		this.series = new TimeSeries(title, Second.class);
		final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset);
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, "cell 0 0");
	}

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "EQUITY CURVE", 
            "Time", 
            "P/L",
            dataset, 
            true, 
            true, 
            false
        );
        
        result.setBackgroundPaint(UiColors.HIFI_THEME_DARK_COLOR); 
        result.getTitle().setBackgroundPaint(UiColors.HIFI_THEME_DARK_COLOR);
        result.getTitle().setPaint(UiColors.GOLDEN_TEXT);
        result.getTitle().setFont(new Font("Cambria Math", Font.BOLD, 14));
        
        result.setBorderPaint(UiColors.GOLDEN_TEXT);
        result.getLegend().setBackgroundPaint(UiColors.HIFI_THEME_DARK_COLOR);
        result.getLegend().setItemPaint(UiColors.GOLDEN_TEXT);
        result.getLegend().setItemFont(new Font("Cambria Math", Font.BOLD, 11));
        
        final XYPlot plot = result.getXYPlot();
        plot.setBackgroundPaint(UiColors.HIFI_THEME_DARK_COLOR);
        plot.setRangeGridlinePaint(UiColors.GOLDEN_TEXT);
        plot.getRenderer().setSeriesPaint(0, Color.WHITE);
        
        
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        //axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis.setTickLabelPaint(UiColors.GOLDEN_TEXT);
        axis.setTickLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelPaint(UiColors.GOLDEN_TEXT);
        
        
        axis = plot.getRangeAxis();
        axis.setTickLabelPaint(UiColors.GOLDEN_TEXT);
        axis.setTickLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelPaint(UiColors.GOLDEN_TEXT);
        
        return result;
    }
	
    public void update(double newValue) {
    	lastValue = newValue;
    	final Second now = new Second();
    	this.series.add(now, this.lastValue);
    }
    
}
