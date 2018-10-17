package thiagodnf.rnsgaii.util;

import java.util.ArrayList;
import java.util.List;

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

		List<PointSolution> points = new ArrayList<>();

		for (S s : population) {
			points.add(createSolution(s.getObjectives()));
		}

		return points;
	}
}
