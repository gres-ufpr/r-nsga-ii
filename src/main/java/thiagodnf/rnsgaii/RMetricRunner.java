package thiagodnf.rnsgaii;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;
import thiagodnf.rnsgaii.qualityattribute.RHypervolume;
import thiagodnf.rnsgaii.qualityattribute.RInvertedGenerationalDistance;
import thiagodnf.rnsgaii.rmetric.RMetric;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class RMetricRunner extends AbstractRunner {
	
	public static Path base = Paths.get("src").resolve("main").resolve("resources").resolve("r-metric");
	
	public static Path example = Paths.get("figure-8-rp-3");
	
	public static PointSolution zr;
	
	public static double delta = 0.2; 
	
	public static RMetric rMetric;

	public static void main(String[] args) throws IOException {
		
		zr = PointSolutionUtils.readFile(base.resolve(example).resolve("reference-point.txt")).get(0);
		
		rMetric = new RMetric(zr, delta);

		List<List<PointSolution>> s = new LinkedList<>();
		List<List<PointSolution>> virtualS = new LinkedList<>();

		for (int i = 1; i <= 10; i++) {
			s.add(PointSolutionUtils.readFile(base.resolve(example).resolve("s" + i + ".txt")));
		}

		for (int i = 0; i < s.size(); i++) {
			virtualS.add(rMetric.execute(s.get(i)));
		}

		showScatterPlot(s, virtualS);
		
		for (int i = 0; i < virtualS.size(); i++) {
			printQualityIndicators(i + 1, virtualS.get(i));
		} 
	}
	
	public static void showScatterPlot(List<List<PointSolution>> s, List<List<PointSolution>> virtualS) {
		
		List<DataSet> datasets = new ArrayList<>();

		datasets.add(new DataSet("RP", Arrays.asList(zr)));

		for (int i = 0; i < s.size(); i++) {
			datasets.add(new DataSet("S" + (i + 1), s.get(i)));
		}

		for (int i = 0; i < virtualS.size(); i++) {
			datasets.add(new DataSet("V" + (i + 1), virtualS.get(i)));
		}

		ScatterPlot.show(datasets, new double[] { 0.0, 2.4 }, new double[] { -0.5, 1.5 });
	}
	
	public static <S extends Solution<?>> void printQualityIndicators(int index, List<S> population) throws FileNotFoundException {
		
		
		// Read values 
		
		Front referenceFront = new ArrayFront(base.resolve(example).resolve("pareto-front.txt").toString());
		
		Front nadirPoint = new ArrayFront(base.resolve(example).resolve("nadir-point.txt").toString());
		
		FrontNormalizer frontNormalizer = new FrontNormalizer(nadirPoint);

		
		// Trim the pareto-front values
		
		List<PointSolution> paretoFront = FrontUtils.convertFrontToSolutionList(referenceFront);
		
		PointSolution zp = rMetric.pivotPointIdentification(paretoFront);
		
		List<PointSolution> trimmedParetoFront = rMetric.trimmingProcedure(zp, paretoFront);
		
		
		
		// Normalize the values
		
		Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
		
		Front normalizedReferenceFront = frontNormalizer.normalize(new ArrayFront(trimmedParetoFront));
		
		List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
		
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("S"+index);
		buffer.append(" ");
		buffer.append(new RHypervolume(zr, delta, normalizedFront).evaluate(normalizedPopulation));
		buffer.append(" ");
		buffer.append(new RInvertedGenerationalDistance(zr, delta, normalizedReferenceFront).evaluate(normalizedPopulation));
		
		System.out.println(buffer.toString());
	}
	
}
