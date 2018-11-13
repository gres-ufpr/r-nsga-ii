package thiagodnf.rnsgaii.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

public class PointSolutionUtils {

	public static PointSolution createSolution(double... objectives) {

		Preconditions.checkArgument(objectives.length > 0, "the objective array should be > 0");

		PointSolution solution = new PointSolution(objectives.length);

		for (int i = 0; i < objectives.length; i++) {
			solution.setObjective(i, objectives[i]);
		}

		return solution;
	}

	public static <S extends Solution<?>> List<PointSolution> convert(List<S> population) {
		
		Preconditions.checkNotNull(population, "The population should not be null");
		
		List<PointSolution> points = new ArrayList<>();

		for (S s : population) {
			points.add(createSolution(s.getObjectives()));
		}

		return points;
	}
	
	public static boolean equals(PointSolution s1, PointSolution s2) {

		if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
			return false;
		}

		for (int i = 0; i < s1.getNumberOfObjectives(); i++) {

			if (s1.getObjective(i) != s2.getObjective(i)) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean contains(List<PointSolution> solutions, PointSolution s2) {

		for (PointSolution s1 : solutions) {

			if (equals(s1, s2)) {
				return true;
			}
		}

		return false;
	}
	
	public static List<PointSolution> union(List<PointSolution> S1, List<PointSolution> S2) {

		Set<PointSolution> union = new HashSet<>();

		for (PointSolution s : S1) {
			union.add(s.copy());
		}

		for (PointSolution s : S2) {
			union.add(s.copy());
		}

		return new ArrayList<>(union);
	}
	
	
	/**
	 * Minus or Relative Complement. That means objects that belong to A and not to B
	 *
	 * @param A the set A
	 * @param B the set B
	 * @return the relative complement to A
	 */
	public static List<PointSolution> minus(List<PointSolution> A, List<PointSolution> B) {

		List<PointSolution> set = new ArrayList<>();

		for (PointSolution sl : A) {

			if (!PointSolutionUtils.contains(B, sl)) {
				set.add(sl.copy());
			}
		}

		return set;
	}
}
