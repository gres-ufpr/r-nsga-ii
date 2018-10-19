package thiagodnf.rnsgaii.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.KPRunner;
import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;

public class ExperimentUtils {
	
	public static double[] epsilons = new double[] {
		1.0,
		0.1,
		0.01,
		0.001,
		0.0001,
		0.00001,
		0.000001
	};
	
	public static void main(String[] args) {

		
		for (int i = 0; i < epsilons.length; i++) {

			List<PointSolution> referencePoints = new ArrayList<>();

			referencePoints.add(PointSolutionUtils.createSolution(0.8, 0.3));
			referencePoints.add(PointSolutionUtils.createSolution(0.1, 0.8));

			double epsilon = epsilons[i];

			List<DataSet> datasets = new KPRunner().run(referencePoints, epsilon);

			String filename = "experiment/r-nsga-ii-" + epsilon + ".png";

			ScatterPlot.exportAsPNG(filename, datasets, new double[] {-0.1,1.1}, new double[] {-0.1, 1.1});
		}
	    
	}

}
