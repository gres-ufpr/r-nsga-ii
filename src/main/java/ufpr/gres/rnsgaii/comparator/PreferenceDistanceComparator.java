package ufpr.gres.rnsgaii.comparator;

import java.util.Comparator;

import org.uma.jmetal.solution.Solution;

import ufpr.gres.rnsgaii.attribute.PreferenceDistance;

public class PreferenceDistanceComparator<S extends Solution<?>> implements Comparator<S> {

	@Override
	public int compare(S solution1, S solution2) {

		int result;

		if (solution1 == null) {
			if (solution2 == null) {
				result = 0;
			} else {
				result = -1;
			}
		} else if (solution2 == null) {
			result = 1;
		} else {
			
			double distance1 = Double.MIN_VALUE;
			double distance2 = Double.MIN_VALUE;

			if (solution1.getAttribute(PreferenceDistance.KEY) != null) {
				distance1 = (int) solution1.getAttribute(PreferenceDistance.KEY);
			}

			if (solution2.getAttribute(PreferenceDistance.KEY) != null) {
				distance2 = (int) solution2.getAttribute(PreferenceDistance.KEY);
			}

			if (distance1 > distance2) {
				result = 1;
			} else if (distance1 < distance2) {
				result = -1;
			} else {
				result = 0;
			}
		}

		return result;
	}

}
