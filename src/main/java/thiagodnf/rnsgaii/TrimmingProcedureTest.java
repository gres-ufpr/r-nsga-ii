package thiagodnf.rnsgaii;

import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.qualityattribute.RHypervolume;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class TrimmingProcedureTest {

	public static void main(String[] args) {
		
		PointSolution zr = PointSolutionUtils.createSolution(0.22, 0.18);
		double[] weights = new double[] {1.0, 1.0};
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(0.2, 0.58), 
			PointSolutionUtils.createSolution(0.22, 0.5), 
			PointSolutionUtils.createSolution(0.3, 0.45),  
			PointSolutionUtils.createSolution(0.38, 0.4) ,
			PointSolutionUtils.createSolution(0.4, 0.38) ,
			PointSolutionUtils.createSolution(0.42, 0.36) ,
			PointSolutionUtils.createSolution(0.5, 0.34), 
			PointSolutionUtils.createSolution(0.58, 0.22),
			PointSolutionUtils.createSolution(0.62, 0.2), 
			PointSolutionUtils.createSolution(0.7, 0.18),
			PointSolutionUtils.createSolution(0.78, 0.15) 
		);
		
		
		new RHypervolume<>(zr, weights, Arrays.asList(S1)).evaluate(S1);
	}
}
