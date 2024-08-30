package ufpr.gres.rnsgaii.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

public class PointSolutionUtils {

	public static PointSolution createSolution(List<Double> objectives) {
		
		PointSolution solution = new PointSolution(objectives.size());

		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i));
		}

		return solution;
	}
	
	public static PointSolution createSolution(double... objectives) {
		return createSolution(Arrays.stream(objectives).boxed().collect(Collectors.toList()));
	}

	public static <S extends Solution<?>> PointSolution convert(S s) {
		return createSolution(s.getObjectives());
	}
	
	public static <S extends Solution<?>> List<PointSolution> convert(List<S> population) {
		
		Preconditions.checkNotNull(population, "The population should not be null");
		
		List<PointSolution> points = new ArrayList<>();

		for (S s : population) {
			points.add(convert(s));
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

		return new LinkedList<>(union);
	}
	
	
	/**
	 * Minus or Relative Complement. That means objects that belong to A and not to B
	 *
	 * @param A the set A
	 * @param B the set B
	 * @return the relative complement to A
	 */
	public static List<PointSolution> minus(List<PointSolution> A, List<PointSolution> B) {

		List<PointSolution> set = new LinkedList<>();

		for (PointSolution sl : A) {

			if (!PointSolutionUtils.contains(B, sl)) {
				set.add(sl.copy());
			}
		}

		return set;
	}

	public static boolean dominates(PointSolution s1, PointSolution s2) {
		return new DominanceComparator<>().compare(s1, s2) == -1;
	}
	
	public static PointSolution shift(PointSolution s, PointSolution vector) {

		PointSolution newPointSolution = s.copy();

		for (int i = 0; i < newPointSolution.getNumberOfObjectives(); i++) {
			newPointSolution.setObjective(i, s.getObjective(i) + vector.getObjective(i));
		}

		return newPointSolution;
	}

	public static List<PointSolution> copy(List<PointSolution> solutions) {

		List<PointSolution> list = new LinkedList<>();

		for (PointSolution solution : solutions) {
			list.add(solution.copy());
		}

		return list;
	}
	
	public static List<PointSolution> readFile(Path path) throws IOException{
		
		List<PointSolution> solutions = new LinkedList<>();

		List<String> lines = Files.readAllLines(path);

		for (String line : lines) {

			String[] strings = line.split("\\s+");

			PointSolution sol = new PointSolution(strings.length);

			for (int i = 0; i < strings.length; i++) {
				sol.setObjective(i, Double.valueOf(strings[i]));
			}

			solutions.add(sol);
		}

		return solutions;
	}
	
	
	public static double[] getMinimumValues(List<PointSolution> solutions) {

		int numberOfObjectives = solutions.get(0).getNumberOfObjectives();

		double[] fmin = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			Collections.sort(solutions, new ObjectiveComparator<PointSolution>(i));

			fmin[i] = solutions.get(0).getObjective(i);
		}

		return fmin;
	}
	
	public static double[] getMaximumValues(List<PointSolution> solutions) {

		int numberOfObjectives = solutions.get(0).getNumberOfObjectives();

		double[] fmax = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			Collections.sort(solutions, new ObjectiveComparator<PointSolution>(i));

			fmax[i] = solutions.get(solutions.size() - 1).getObjective(i);
		}

		return fmax;
	}

	public static List<? extends Solution<?>> getNadirPoint(int numberOfObjectives, double min, double max) {
		
		List<PointSolution> solutions = new ArrayList<>();
		
		for (int i = 0; i < numberOfObjectives; i++) {

			PointSolution solution = new PointSolution(numberOfObjectives);

			for (int j = 0; j < numberOfObjectives; j++) {

				if (i == j) {
					solution.setObjective(j, max);
				} else {
					solution.setObjective(j, min);
				}
			}

			solutions.add(solution);
		}
		
		return solutions;
	}
}
