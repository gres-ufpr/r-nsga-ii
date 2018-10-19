package thiagodnf.rnsgaii.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
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
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.util.PointSolutionUtils;

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
			
			example.setSize(400, 400);
			example.setLocationRelativeTo(null);
			example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			example.setVisible(true);
		});
	}
	
	public static<S extends Solution<?>> void show(List<PointSolution> referencePoints, List<S> solutionList) {
		
		int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
		
		double[] fmin = new double[numberOfObjectives];
		double[] fmax = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			Collections.sort(solutionList, new ObjectiveComparator<Solution<?>>(i));

			fmin[i] = solutionList.get(0).getObjective(i);
			fmax[i] = solutionList.get(solutionList.size() - 1).getObjective(i);
		}
		
		List<DataSet> datasets = new ArrayList<>();
		
		datasets.add(new DataSet("Reference Points", PointSolutionUtils.convert(referencePoints)));
		datasets.add(new DataSet("General", PointSolutionUtils.convert(solutionList)));
		
		fmin = new double[] {-0.1, 1.1};
		fmax = new double[] {-0.1, 1.1};
		
		show(datasets, fmin, fmax);
	}
}
