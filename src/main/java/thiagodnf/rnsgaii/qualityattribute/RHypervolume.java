package thiagodnf.rnsgaii.qualityattribute;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.qualityattribute.asf.ASF;
import thiagodnf.rnsgaii.qualityattribute.asf.WeightedChebyshev;
import thiagodnf.rnsgaii.rmetric.RMetric;

public class RHypervolume<S extends Solution<?>> extends GenericIndicator<S> {

	private static final long serialVersionUID = -4463497277376412680L;

	private List<List<S>> S;
	
	protected S zr;

	protected double[] weights;
	
	protected double delta = 0.3;
	
	public RHypervolume(S zr, double[] weights, List<List<S>> S) {
		this.zr = zr;
		this.weights = weights;
		this.S = S;
	}
	
	@Override
	public Double evaluate(List<S> evaluate) {
		
		// Step 1) Prescreening Procedure:
		// We at first merge these L preferred solution sets into a composite set Sc.
		// only the non-dominated solutions, are retained for the R-metric calculation
		
		List<S> jointPopulation = new ArrayList<>();
	    
		for(List<S> si : S) {
			jointPopulation.addAll(si);
		}
		
		List<S> Sc = SolutionListUtils.getNondominatedSolutions(jointPopulation);
		
		// Step 2) Pivot Point Identification:
		// the pivot point (denoted as zp) of a given set of preferred solutions
		// (denoted as S) is used as the representative that reflects the overall
		// satisfaction of S with respect to the DM supplied preference information.
		
		//S zp = RMetric.getPivotPointIdentification(Sc);
		
		// Step 3) Trimming Procedure:
		// Instead of the whole PF, the ROI is a bounded region, i.e., a part of the PF,
		// given the DM’s preference information. Only solutions located in the ROI are
		// of interest to the DM.
		
//		List<S> trimmedList = RMetric.trimmingProcedure(zp, Sc, delta);
//		
//		S zw = RMetric.getWorstPoint(zp, weights);
		
		// Step 4) Solution Transfer:
		// This step is the main crux of our R-metric by which the trimmed points are
		// transferred to a virtual position.
		
		//solutionTransferProcedure(zp, zr, zw);

		return null;
	}
	
	protected void solutionTransferProcedure(S zp, S zr, S zw) {
		
		S zl = getIsoASFPoint(zp, zr, zw);
		
		System.out.println(zl);
		
	}
	
	protected S getIsoASFPoint(S zp, S zr, S zw) {

		int k = getObjectiveKThatContributesToTheASFValue(zp, zr, zw);

		int m = zp.getNumberOfObjectives();

		S zl = (S) new PointSolution(zp.getNumberOfObjectives());

		double delta = (zp.getObjective(k) - zr.getObjective(k)) / (zw.getObjective(k) - zr.getObjective(k));

		for (int i = 0; i < m; i++) {
			zl.setObjective(i, zr.getObjective(i) + delta * (zw.getObjective(i) - zr.getObjective(i)));
		}

		return zl;
	}
	
	/**
	 * Equation 
	 * @param zp
	 * @param zr
	 * @param zw the worst point
	 * @return the objective K that contributes to the ASF value
	 */
	protected int getObjectiveKThatContributesToTheASFValue(S zp, S zr, S zw) {

		int k = Integer.MIN_VALUE;
		double max = Double.NEGATIVE_INFINITY;

		int m = zp.getNumberOfObjectives();

		for (int i = 0; i < m; i++) {

			double value = (zp.getObjective(i) - zr.getObjective(i)) / (zw.getObjective(i) - zr.getObjective(i));

			if (value > max) {
				max = value;
				k = i;
			}
		}

		return k;
	}
	
	@Override
	public boolean isTheLowerTheIndicatorValueTheBetter() {
		return false;
	}

}
