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
	
	public static Path example = Paths.get("example-1");
	
	public static PointSolution zr = PointSolutionUtils.createSolution(0.16, 0.9);
	
	public static double delta = 0.2; 

	public static void main(String[] args) throws IOException {
		
		RMetric rMetric = new RMetric(zr, delta);

		List<List<PointSolution>> s = new LinkedList<>();
		List<List<PointSolution>> virtualS = new LinkedList<>();

		for (int i = 1; i <= 10; i++) {
			s.add(PointSolutionUtils.readFile(base.resolve(example).resolve("s" + i + ".txt")));
		}

		for (int i = 0; i < s.size(); i++) {
			virtualS.add(rMetric.execute(s.get(i)));
		}

		showScatterPlot(s, virtualS);
		
		
//		for (int i = 0; i < virtualS.size(); i++) {
//			printQualityIndicators(virtualS.get(i));
//		}
		
		for (int i = 0; i < virtualS.size(); i++) {
			printQualityIndicators(virtualS.get(i));
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

		ScatterPlot.show(datasets, new double[] { 0.0, 1.0 }, new double[] { 0.0, 2.0 });
	}
	
	
	
	public static <S extends Solution<?>> void printQualityIndicators(List<S> population) throws FileNotFoundException {
		
		Front referenceFront = new ArrayFront(base.resolve("nadir-point.txt").toString());
		FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);

		Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
		
		List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
		
		System.out.println(new RHypervolume<PointSolution>(zr, delta).evaluate(normalizedPopulation));
	}
	
	public static <S extends Solution<?>> void printRIGD(List<S> population) throws FileNotFoundException {
		
		Front referenceFront = new ArrayFront(base.resolve(example).resolve("pareto-front.txt").toString());
		
		Front nadirPoint = new ArrayFront(base.resolve("nadir-point.txt").toString());
		FrontNormalizer frontNormalizer = new FrontNormalizer(nadirPoint);

		Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
		Front normalizedReferenceFront = frontNormalizer.normalize(new ArrayFront(referenceFront));
		
		List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
		
		System.out.println(new RInvertedGenerationalDistance(zr, delta, normalizedReferenceFront).evaluate(normalizedPopulation));
	}
	
}
