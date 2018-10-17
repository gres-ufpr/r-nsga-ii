package thiagodnf.rnsgaii.gui;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.uma.jmetal.util.point.PointSolution;

public class ScatterPlot extends JFrame {

	private static final long serialVersionUID = -5222514150598155420L;

	public ScatterPlot(List<DataSet> datasets, double[] xRange, double[] yRange) {
		super("Population");

		// Create dataset
		XYDataset dataset = createDataset(datasets);

		// Create chart
		JFreeChart chart = ChartFactory
				.createScatterPlot("", "Objective 0", "Objective 1", dataset);

		// Changes background color
		XYPlot plot = (XYPlot) chart.getPlot();
		
		((NumberAxis) plot.getDomainAxis()).setRange(xRange[0], xRange[1]);
		((NumberAxis) plot.getRangeAxis()).setRange(yRange[0], yRange[1]);
		
		plot.setBackgroundPaint(new Color(255, 228, 196));

		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		
		setContentPane(panel);
	}

	private XYDataset createDataset(List<DataSet> datasets) {

		XYSeriesCollection collection = new XYSeriesCollection();

		for (DataSet dataset : datasets) {

			XYSeries serie = new XYSeries(dataset.getName());

			for (PointSolution point : dataset.getSolutions()) {
				serie.add(point.getObjective(0), point.getObjective(1));
			}

			collection.addSeries(serie);
		}

		return collection;
	}

	public static void show(List<DataSet> datasets, double[] xRange, double[] yRange) {
		
		SwingUtilities.invokeLater(() -> {
			
			ScatterPlot example = new ScatterPlot(datasets, xRange, yRange);
			
			example.setSize(800, 400);
			example.setLocationRelativeTo(null);
			example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			example.setVisible(true);
		});
	}
}
