package thiagodnf.rnsgaii;

import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.qualityattribute.RHypervolume;
import thiagodnf.rnsgaii.qualityattribute.asf.ASF;
import thiagodnf.rnsgaii.qualityattribute.asf.IsoASFPoint;
import thiagodnf.rnsgaii.qualityattribute.asf.WeightedChebyshev;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class WeightedChebyshevTest {

	public static void main(String[] args) {
		
		PointSolution zr = PointSolutionUtils.createSolution(0.18, 0.38);
		double[] weights = new double[] {1.0, 1.0};
		
		List<PointSolution> solutions = Arrays.asList(
			PointSolutionUtils.createSolution(0.08, 0.8), // Point a
			PointSolutionUtils.createSolution(0.2, 0.8), // Point b
			PointSolutionUtils.createSolution(0.4, 0.38)  // Point c	
		);
		
		ASF<PointSolution> asf = new WeightedChebyshev<>(zr, weights);
		
		for (PointSolution x : solutions) {
			System.out.println(asf.calculate(x));
		}
		
		IsoASFPoint<PointSolution> isoASFPoint = new IsoASFPoint<>(zr, weights);
		
		for (PointSolution a : solutions) {
			System.out.println(a + " - "+isoASFPoint.calculate(a));
		}
		
		System.out.println("-----");
		
		new RHypervolume<>(zr, weights, Arrays.asList(solutions)).evaluate(solutions);
	}
}
