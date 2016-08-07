package kaqt.edutils.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import kaqt.edutils.model.Quote;
import kaqt.edutils.utils.Constants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class CurveChart extends ApplicationFrame {
	private XYSeries series;
	public CurveChart(JPanel panel, final String title, ConcurrentHashMap<String, Quote> prices) {
		super(title);
		
		this.series = new XYSeries(title);
		final XYSeriesCollection dataset = new XYSeriesCollection(this.series);
		
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		panel.add(chartPanel, "cell 0 0");
	}
	
	public void updatePrices(ConcurrentHashMap<String, Quote> prices) {
		this.series.clear();
		for (String key : prices.keySet()) {
			Quote q = prices.get(key);
			this.series.add((double)Constants.CONTRACT_ROWS.get(key), 100 - ((q.getAsk_price() + q.getBid_price())/2));
		}
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createXYLineChart(
                "Eurodollar Curve",      // chart title
                "Leg",                      // x axis label
                "rate",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
            );
        
        result.setBackgroundPaint(Color.DARK_GRAY); 
        result.getTitle().setBackgroundPaint(Color.DARK_GRAY);
        result.getTitle().setPaint(Color.WHITE);
        result.getTitle().setFont(new Font("Cambria Math", Font.BOLD, 14));
        
        result.setBorderPaint(Color.DARK_GRAY);
        result.getLegend().setBackgroundPaint(Color.DARK_GRAY);
        result.getLegend().setItemPaint(Color.WHITE);
        result.getLegend().setItemFont(new Font("Cambria Math", Font.BOLD, 11));
        
        final XYPlot plot = result.getXYPlot();
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.getRenderer().setSeriesPaint(0, Color.WHITE);
        
        
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        //axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis.setTickLabelPaint(Color.WHITE);
        axis.setTickLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelPaint(Color.WHITE);
        
        axis = plot.getRangeAxis();
        axis.setTickLabelPaint(Color.WHITE);
        axis.setTickLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelFont(new Font("Cambria Math", Font.BOLD, 11));
        axis.setLabelPaint(Color.WHITE);
        
        return result;
	}
}
