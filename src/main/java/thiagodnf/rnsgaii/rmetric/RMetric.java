package thiagodnf.rnsgaii.rmetric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class RMetric {
	
	public static void main(String[] args) {
		
		List<List<PointSolution>> S = new LinkedList<>();
		
		List<PointSolution> S1 = new LinkedList<>();

		S1.add(PointSolutionUtils.createSolution(0.2, 0.6));
		S1.add(PointSolutionUtils.createSolution(0.25, 0.55));
		S1.add(PointSolutionUtils.createSolution(0.3, 0.5));
		S1.add(PointSolutionUtils.createSolution(0.35, 0.45));
		S1.add(PointSolutionUtils.createSolution(0.4, 0.4));
		S1.add(PointSolutionUtils.createSolution(0.45, 0.35));
		S1.add(PointSolutionUtils.createSolution(0.5, 0.3));
		S1.add(PointSolutionUtils.createSolution(0.55, 0.25));
		S1.add(PointSolutionUtils.createSolution(0.6, 0.2));
	
		S.add(S1);
		
		S = prescreeningProcedure(S);
		
	}
	
	protected static List<List<PointSolution>> prescreeningProcedure(List<List<PointSolution>> S) {
		
		int L = S.size();
		
		for (int i = 0; i < L; i++) {

			List<PointSolution> Si = new LinkedList<>(S.get(i));

			List<PointSolution> Sc = new ArrayList<>();

			for (int l = 0; l < L; l++) {
				Sc = PointSolutionUtils.union(Sc, PointSolutionUtils.minus(S.get(l), Si));
			}

			for (int j = 0; j < Si.size(); j++) {

				for (int k = 0; k < Sc.size(); k++) {
					
					if(PointSolutionUtils.dominates(Sc.get(k), Si.get(j))) {
						Si = PointSolutionUtils.minus(Si, Arrays.asList(Si.get(j)));
						break;
					}
				}
			}
		}
		
		return S;
	}
	
	
	/**
	 * Return the worst point Zw
	 * 
	 * @param Zr a reference point
	 * @param weights a weight vector that specifies the relative importance of each objective
	 * @return Zw
	 */
	protected static PointSolution getWorstPoint(PointSolution Zr, double[] weights) {

		PointSolution Zw = new PointSolution(Zr.getNumberOfObjectives());

		for (int i = 0; i < Zw.getNumberOfObjectives(); i++) {
			Zw.setObjective(i, Zr.getObjective(i) + 2.0 * weights[i]);
		}

		return Zw;
	}
	
	protected static List<PointSolution> trimmingProcedure(PointSolution zp, List<PointSolution> Sc, double delta) {

		List<PointSolution> trimmedList = new ArrayList<>(Sc);

		for (int i = 0; i < Sc.size(); i++) {

			PointSolution s = Sc.get(i);

			for (int j = 0; j < s.getNumberOfObjectives(); j++) {

				double value = Math.abs(s.getObjective(j) - zp.getObjective(j));

				if (value > (delta / 2.0)) {
					trimmedList.remove(s);
					break;
				}
			}
		}

		return trimmedList;
	}
	
	
	
	protected static PointSolution getPivotPointIdentification(List<PointSolution> Sc) {

//		ASF<PointSolution> asf = new WeightedChebyshev<>(zr, weights);
//
		PointSolution rp = null;
//		double min = Double.POSITIVE_INFINITY;
//
//		for (PointSolution s : Sc) {
//
//			double value = asf.calculate(s);
//
//			if (value < min) {
//				min = value;
//				rp = s;
//			}
//		}

		return rp;
	}

}
