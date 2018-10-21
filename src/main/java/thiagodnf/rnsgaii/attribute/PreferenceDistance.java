package thiagodnf.rnsgaii.attribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.solutionattribute.DensityEstimator;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;

import com.google.common.base.Preconditions;

import thiagodnf.rnsgaii.comparator.DistanceToRPComparator;
import thiagodnf.rnsgaii.util.EuclideanDistanceUtils;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class PreferenceDistance<S extends Solution<?>> extends GenericSolutionAttribute<S, Double> implements DensityEstimator<S> {
	
	private static final long serialVersionUID = -7384332658172205041L;
	
	public static final String KEY = "preference_distance";
	
	private List<PointSolution> referencePoints;
	
	private double[] fmin;
	
	private double[] fmax;
	
	public PreferenceDistance(List<PointSolution> referencePoints, double[] fmin, double[] fmax) {
		
		Preconditions.checkNotNull(fmin, "The fmin should not be null");
		Preconditions.checkNotNull(fmin, "The fmax should not be null");
		Preconditions.checkNotNull(referencePoints, "The reference point list should not be null");
		Preconditions.checkArgument(!referencePoints.isEmpty(), "The reference point list should not be empty");
		Preconditions.checkArgument(fmin.length >= 2, "The fmin length should be >= 2");
		Preconditions.checkArgument(fmax.length >= 2, "The fmax length should be >= 2");
		
		this.referencePoints = referencePoints;
		this.fmin = fmin;
		this.fmax = fmax;
	}

	@Override
	public void computeDensityEstimator(List<S> solutionSet) {
		
		Preconditions.checkNotNull(solutionSet, "The solution set should not be null");
		Preconditions.checkArgument(!solutionSet.isEmpty(), "The solution set should not be empty");
		
		for (int i = 0; i < referencePoints.size(); i++) {

			PointSolution r = referencePoints.get(i);

			for (S s : solutionSet) {

				PointSolution x = PointSolutionUtils.createSolution(s.getObjectives());
				
				double distance = EuclideanDistanceUtils.calculate(x, r, fmin, fmax);
				
				s.setAttribute(DistanceToRPComparator.KEY + i, distance);
			}
			
			Collections.sort(solutionSet, new DistanceToRPComparator<S>(i));
			
			Map<Double, Integer> positions = new HashMap<>();
			
			int rankingPos = 1;
			
			for (S s : solutionSet) {

				double  value = (double) s.getAttribute(DistanceToRPComparator.KEY + i);
				
				if(!positions.containsKey(value)) {
					positions.put(value, rankingPos++);
				}
			}
			
			for (S s : solutionSet) {
				
				double value = (double) s.getAttribute(DistanceToRPComparator.KEY + i);
				
				int pos = positions.get(value);
				
				s.setAttribute("ranking_to_rp_" + i, pos);
			}
		}
		
		for (S s : solutionSet) {
			
			int minPos = Integer.MAX_VALUE;
			
			for (int i = 0; i < referencePoints.size(); i++) {
				
				int pos = (int) s.getAttribute("ranking_to_rp_" + i);
				
				if(pos < minPos) {
					minPos = pos;
				}
			}

			s.setAttribute(KEY, minPos);
		}
	}

}
