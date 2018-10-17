package thiagodnf.rnsgaii.util;

import java.util.Arrays;
import java.util.Collections;

import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

public class EuclideanDistanceUtils {

	public static double calculate(PointSolution x, PointSolution r, double[] fmin, double[] fmax) {

		Preconditions.checkNotNull(x, "The solution x should not be null");
		Preconditions.checkNotNull(r, "The solution x should not be null");
		Preconditions.checkArgument(x.getNumberOfObjectives() == r.getNumberOfObjectives(), "The x and r points should be the same number of objectives");
		Preconditions.checkArgument(fmin.length > 0, "The fmin length should be > 0");
		Preconditions.checkArgument(fmax.length > 0, "The fmax length should be > 0");
		Preconditions.checkArgument(fmin.length == x.getNumberOfObjectives(), "The fmin should have the same size of x");
		Preconditions.checkArgument(fmax.length == x.getNumberOfObjectives(), "The fmax should have the same size of x");
		Preconditions.checkArgument(fmin.length == r.getNumberOfObjectives(), "The fmin should have the same size of x");
		Preconditions.checkArgument(fmax.length == r.getNumberOfObjectives(), "The fmax should have the same size of x");
		
		int nObj = x.getNumberOfObjectives();

		double sum = 0.0;

		for (int i = 0; i < nObj; i++) {
			sum += Math.pow((x.getObjective(i) - r.getObjective(i)) / (fmax[i] - fmin[i]), 2);
		}

		return Math.sqrt(sum);
	}
	
	public static double calculate(PointSolution x, PointSolution r) {
		
		int nObj = x.getNumberOfObjectives();
		
		double[] fmin = new double[nObj];
		double[] fmax = new double[nObj];
		
		Arrays.fill(fmin, 1.0);
		Arrays.fill(fmax, 2.0);
		
		return calculate(x, r, fmin, new double[x.getNumberOfObjectives()]);
	}
		
}
