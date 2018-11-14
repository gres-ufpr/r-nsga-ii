package thiagodnf.rnsgaii.rmetric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;
import thiagodnf.rnsgaii.qualityattribute.asf.ASF;
import thiagodnf.rnsgaii.qualityattribute.asf.WeightedChebyshev;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class RMetric {
	
	private PointSolution zr;

	private double[] weights;
	
	private double delta;

	public RMetric(PointSolution zr, double[] weights, double delta) {
		this.zr = zr;
		this.weights = weights;
		this.delta = delta;
	}

	public RMetric(PointSolution zr, double delta) {
		this(zr, Collections.nCopies(zr.getNumberOfObjectives(), 1.0), delta);
	}

	public RMetric(PointSolution zr, List<Double> weights, double delta) {
		this(zr, weights.stream().mapToDouble(x -> x).toArray(), delta);
	}
	
	public List<PointSolution> execute(List<PointSolution> solutions) {
		
		List<PointSolution> S = PointSolutionUtils.copy(solutions);
		
		// Step 1
		S = prescreeningProcedure(S);
		
		// Step 2
		PointSolution zp = pivotPointIdentification(S);
		
		// Step 3
		S = trimmingProcedure(zp, S);
		
		// Step 4
		S = solutionTransfer(zp, S);
		
		return S;
	}
	
	protected List<PointSolution> prescreeningProcedure(List<PointSolution> S) {
		return PointSolutionUtils.copy(SolutionListUtils.getNondominatedSolutions(S));
	}
	
	protected PointSolution pivotPointIdentification(List<PointSolution> S) {

		ASF<PointSolution> asf = new WeightedChebyshev<>(zr, weights);

		PointSolution zp = null;
		double min = Double.POSITIVE_INFINITY;

		for (PointSolution x : S) {

			double value = asf.calculate(x);

			if (value < min) {
				min = value;
				zp = x.copy();
			}
		}

		return zp;
	}
	
	protected List<PointSolution> trimmingProcedure(PointSolution zp, List<PointSolution> S) {

		Iterator<PointSolution> it = S.iterator();
		
		while (it.hasNext()) {
			
			PointSolution x = it.next();
		
			for (int j = 0; j < x.getNumberOfObjectives(); j++) {

				double value = Math.abs(x.getObjective(j) - zp.getObjective(j));

				if (value > (delta / 2.0)) {
					it.remove();
					break;
				}
			}
		}

		return S;
	}
	
	protected List<PointSolution> solutionTransfer(PointSolution zp, List<PointSolution> S) {
		
		PointSolution zw = getWorstPoint();
		
		PointSolution zl = getIsoASFPoint(zp, zw);
		
		List<PointSolution> shifted = new LinkedList<>();
		
		PointSolution vector = new PointSolution(zp.getNumberOfObjectives());

		for (int i = 0; i < vector.getNumberOfObjectives(); i++) {
			vector.setObjective(i, zl.getObjective(i) - zp.getObjective(i));
		}
		
		for(PointSolution x : S) {
			shifted.add(PointSolutionUtils.shift(x, vector));
		}
		
		return shifted;
	}
	
	protected PointSolution getWorstPoint() {

		PointSolution zw = new PointSolution(zr.getNumberOfObjectives());

		for (int i = 0; i < zw.getNumberOfObjectives(); i++) {
			zw.setObjective(i, zr.getObjective(i) + 2.0 * weights[i]);
		}

		return zw;
	}
	
	protected PointSolution getIsoASFPoint(PointSolution zp, PointSolution zw) {
		
		int k = getObjectiveKThatContributesToTheASFValue(zp, zw);

		PointSolution zl = new PointSolution(zp.getNumberOfObjectives());

		double delta = (zp.getObjective(k) - zr.getObjective(k)) / (zw.getObjective(k) - zr.getObjective(k));

		for (int i = 0; i < zp.getNumberOfObjectives(); i++) {
			zl.setObjective(i, zr.getObjective(i) + delta * (zw.getObjective(i) - zr.getObjective(i)));
		}

		return zl;
	}
	
	protected int getObjectiveKThatContributesToTheASFValue(PointSolution zp, PointSolution zw) {

		int k = Integer.MIN_VALUE;
		double max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < zp.getNumberOfObjectives(); i++) {

			double value = (zp.getObjective(i) - zr.getObjective(i)) / (zw.getObjective(i) - zr.getObjective(i));

			if (value > max) {
				max = value;
				k = i;
			}
		}

		return k;
	}
	
	
	
	
//	protected static List<List<PointSolution>> prescreeningProcedure(List<List<PointSolution>> S) {
//		
//		int L = S.size();
//		
//		for (int i = 0; i < L; i++) {
//
//			List<PointSolution> Si = S.get(i);
//
//			List<PointSolution> Sc = new LinkedList<>();
//
//			for (int l = 0; l < L; l++) {
//				Sc = PointSolutionUtils.union(Sc, PointSolutionUtils.minus(S.get(l), Si));
//			}
//
//			for (int j = 0; j < Si.size(); j++) {
//				
//				for (int k = 0; k < Sc.size(); k++) {
//					
//					if(PointSolutionUtils.dominates(Sc.get(k), Si.get(j))) {
//						Si = PointSolutionUtils.minus(Si, Arrays.asList(Si.get(j)));
//						break;
//					}
//				}
//			}
//		}
//		
//		return S;
//	}
	
	
	/**
	 * Return the worst point Zw
	 * 
	 * @param Zr a reference point
	 * @param weights a weight vector that specifies the relative importance of each objective
	 * @return Zw
	 */
//	protected static PointSolution getWorstPoint(PointSolution Zr, double[] weights) {
//
//		PointSolution Zw = new PointSolution(Zr.getNumberOfObjectives());
//
//		for (int i = 0; i < Zw.getNumberOfObjectives(); i++) {
//			Zw.setObjective(i, Zr.getObjective(i) + 2.0 * weights[i]);
//		}
//
//		return Zw;
//	}
	
//	protected static List<PointSolution> trimmingProcedure(PointSolution zp, List<PointSolution> S, double delta) {
//
//		List<PointSolution> trimmedList = new LinkedList<>(S);
//
//		for (PointSolution x : S) {
//
//			for (int j = 0; j < x.getNumberOfObjectives(); j++) {
//
//				double value = Math.abs(x.getObjective(j) - zp.getObjective(j));
//
//				if (value > (delta / 2.0)) {
//					trimmedList.remove(x);
//					break;
//				}
//			}
//		}
//
//		return trimmedList;
//	}
		
//	protected static List<PointSolution> pivotPointIdentification(PointSolution zr, double[] weights, List<List<PointSolution>> S) {
//
//		ASF<PointSolution> asf = new WeightedChebyshev<>(zr, weights);
//		
//		List<PointSolution> zps = new LinkedList<>();
//
//		for(List<PointSolution> Si : S) {
//			
//			PointSolution zp = null;
//			double min = Double.POSITIVE_INFINITY;
//
//			for (PointSolution x : Si) {
//
//				double value = asf.calculate(x);
//
//				if (value < min) {
//					min = value;
//					zp = x.copy();
//				}
//			}
//
//			zps.add(zp);
//		}
//		
//		return zps;
//	}
	
//	protected static List<PointSolution> solutionTransfer(List<PointSolution> S, PointSolution zp, PointSolution zr, PointSolution zw) {
//		
//		PointSolution zl = getIsoASFPoint(zp, zr, zw);
//		
//		List<PointSolution> shifted = new LinkedList<>();
//		
//		PointSolution vector = new PointSolution(zp.getNumberOfObjectives());
//
//		for (int i = 0; i < vector.getNumberOfObjectives(); i++) {
//			vector.setObjective(i, zl.getObjective(i) - zp.getObjective(i));
//		}
//		
//		for(PointSolution x : S) {
//			shifted.add(shift(x, vector));
//		}
//		
//		return shifted;
//	}
	
//	protected static PointSolution shift(PointSolution s, PointSolution vector) {
//
//		PointSolution newPointSolution = s.copy();
//
//		for (int i = 0; i < newPointSolution.getNumberOfObjectives(); i++) {
//			newPointSolution.setObjective(i, s.getObjective(i) + vector.getObjective(i));
//		}
//
//		return newPointSolution;
//	}
	
//	protected static PointSolution getIsoASFPoint(PointSolution zp, PointSolution zr, PointSolution zw) {
//
//		int k = getObjectiveKThatContributesToTheASFValue(zp, zr, zw);
//
//		PointSolution zl = new PointSolution(zp.getNumberOfObjectives());
//
//		double delta = (zp.getObjective(k) - zr.getObjective(k)) / (zw.getObjective(k) - zr.getObjective(k));
//
//		for (int i = 0; i < zp.getNumberOfObjectives(); i++) {
//			zl.setObjective(i, zr.getObjective(i) + delta * (zw.getObjective(i) - zr.getObjective(i)));
//		}
//
//		return zl;
//	}
	
	/**
	 * Equation 
	 * @param zp
	 * @param zr
	 * @param zw the worst point
	 * @return the objective K that contributes to the ASF value
	 */
//	protected static int getObjectiveKThatContributesToTheASFValue(PointSolution zp, PointSolution zr, PointSolution zw) {
//
//		int k = Integer.MIN_VALUE;
//		double max = Double.NEGATIVE_INFINITY;
//
//		for (int i = 0; i < zp.getNumberOfObjectives(); i++) {
//
//			double value = (zp.getObjective(i) - zr.getObjective(i)) / (zw.getObjective(i) - zr.getObjective(i));
//
//			if (value > max) {
//				max = value;
//				k = i;
//			}
//		}
//
//		return k;
//	}
	
public static void main(String[] args) {
		
		PointSolution zr = PointSolutionUtils.createSolution(0.2, 0.2);
		double delta = 0.3; 
		
		List<List<PointSolution>> S = new LinkedList<>();
		
		List<PointSolution> S1 = new LinkedList<>();
		List<PointSolution> S2 = new LinkedList<>();

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

		S2.add(PointSolutionUtils.createSolution(0.55, 0.25));
		S2.add(PointSolutionUtils.createSolution(0.60, 0.23));
		S2.add(PointSolutionUtils.createSolution(0.65, 0.21));
		S2.add(PointSolutionUtils.createSolution(0.70, 0.19));
		S2.add(PointSolutionUtils.createSolution(0.75, 0.17));
		S2.add(PointSolutionUtils.createSolution(0.80, 0.15));
		S2.add(PointSolutionUtils.createSolution(0.85, 0.13));
		S2.add(PointSolutionUtils.createSolution(0.90, 0.11));
		S2.add(PointSolutionUtils.createSolution(0.95, 0.09));
		
		S.add(S2);
		
		RMetric metric = new RMetric(zr, delta);
		
		List<DataSet> datasets = new ArrayList<>();

		datasets.add(new DataSet("RP", Arrays.asList(zr)));
		
		for (int i = 0; i < S.size(); i++) {
			datasets.add(new DataSet("S" + (i + 1), S.get(i)));
		}
		
		
		for (int i = 0; i < S.size(); i++) {
			datasets.add(new DataSet("V" + (i + 1), metric.execute(S.get(i))));
		}		   
		 
		ScatterPlot.show(datasets, new double[] {0.0,1.0}, new double[] {0.0, 1.0});
		
	}

}
