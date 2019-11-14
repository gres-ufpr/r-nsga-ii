package ufpr.gres.rnsgaii.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.uma.jmetal.util.point.PointSolution;

public class LinePlot extends JFrame {

	private static final long serialVersionUID = -5222514150598155420L;

	public LinePlot(List<DataSet> datasets, double[] yRange) {
		super("Populations");
		
		ChartPanel panel = new ChartPanel(createChart(datasets, yRange));
		
		setContentPane(panel);
	}
	
	public static JFreeChart createChart(List<DataSet> datasets, double[] yRange) {
		
		// Create dataset
		DefaultCategoryDataset dataset = createDataset(datasets);

		// Create chart
		JFreeChart chart = ChartFactory.createLineChart(
		         null,
		         "Objectives","Values",
		         dataset,
		         PlotOrientation.VERTICAL,
		         false,	// Legend 
		         true, false);
		
		// Changes background color
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		
		((NumberAxis) plot.getRangeAxis()).setRange(yRange[0], yRange[1]);
		
		plot.setBackgroundPaint(new Color(255, 228, 196));
		
		return chart;
	}
	
	public static void exportAsPNG(String filename, List<DataSet> datasets, double[] xRange, double[] yRange) {

		JFreeChart chart  = createChart(datasets, yRange);
		
		BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();

		g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
		Rectangle r = new Rectangle(0, 0, 400, 400);
		chart.draw(g2, r);
		
		File f = new File(filename);

		BufferedImage chartImage = chart.createBufferedImage(400, 400, null);
		
		try {
			ImageIO.write(chartImage, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static DefaultCategoryDataset createDataset(List<DataSet> datasets) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (DataSet d : datasets) {
		
			for (int i = 0; i < d.getSolutions().size(); i++) {

				PointSolution point = d.getSolutions().get(i);
				
				for (int j = 0; j < point.getNumberOfObjectives(); j++) {

					dataset.addValue(point.getObjective(j), String.valueOf(i), String.valueOf(j));
				}
			}
		}

		return dataset;
	}

	public static void show(List<DataSet> datasets, double[] yRange) {
		
		SwingUtilities.invokeLater(() -> {
			
			LinePlot example = new LinePlot(datasets, yRange);
			
			example.setSize(400, 400);
			example.setLocationRelativeTo(null);
			example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			example.setVisible(true);
		});
	}
}
