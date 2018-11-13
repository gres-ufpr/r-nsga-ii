package thiagodnf.rnsgaii.rmetric;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class RMetric {
	
	protected static void prescreeningProcedure(List<List<PointSolution>> S) {
		
		int L = S.size();
		
		for (int i = 0; i < L; i++) {

			List<PointSolution> Si = new LinkedList<>(S.get(i));

			List<PointSolution> Sc = new ArrayList<>();

			for (int l = 0; l < L; l++) {
				Sc = PointSolutionUtils.union(Sc, PointSolutionUtils.minus(S.get(l), Si));
			}

			for (int j = 0; j < Si.size(); j++) {

				for (int k = 0; k < Sc.size(); k++) {
//					if() {
//						break;
//					}
				}
			}
		}
//		
//		List<PointSolution> jointPopulation = new ArrayList<>();
//	    
//		for(List<PointSolution> si : S) {
//			jointPopulation.addAll(si);
//		}
//		
//		List<PointSolution> Sc = SolutionListUtils.getNondominatedSolutions(jointPopulation);
//		
		
//		Ranking<S> ranking = new DominanceRanking<S>() ;
//	    return ranking.computeRanking(solutionList).getSubfront(0);
		
	}
	
//	protected static List<PointSolution> union(List<List<PointSolution>> S, List<PointSolution> Si) {
//		
//		int L = S.size();
//		
//		Set<PointSolution> Sc = new HashSet<>();
//		
//		for (int l = 0; l < L; l++) {
//			
//			
//		}
//			
//		return null;
//	}
	

	
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
