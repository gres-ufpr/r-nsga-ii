package thiagodnf.rnsgaii.util;

import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

public class EuclideanDistanceUtils {
	
	private EuclideanDistanceUtils() throws InstantiationException {
		throw new InstantiationException("Instances of this type are forbidden.");
	}
	
	public static double calculate(PointSolution x, PointSolution r, double[] fmin, double[] fmax) {

		Preconditions.checkNotNull(x, "The solution x should not be null");
		Preconditions.checkNotNull(r, "The solution x should not be null");
		Preconditions.checkArgument(x.getNumberOfObjectives() == r.getNumberOfObjectives(), "The x and r points should be the same number of objectives");
		Preconditions.checkArgument(fmin.length > 0, "The fmin length should be > 0");
		Preconditions.checkArgument(fmax.length > 0, "The fmax length should be > 0");
		Preconditions.checkArgument(fmin.length == x.getNumberOfObjectives(), "The fmin should have the same size of x");
		Preconditions.checkArgument(fmax.length == x.getNumberOfObjectives(), "The fmax should have the same size of x");
		
		int nObj = x.getNumberOfObjectives();

		double sum = 0.0;

		for (int i = 0; i < nObj; i++) {
			sum += Math.pow((x.getObjective(i) - r.getObjective(i)) / (fmax[i] - fmin[i]), 2);
		}

		return Math.sqrt(sum);
	}	
}
