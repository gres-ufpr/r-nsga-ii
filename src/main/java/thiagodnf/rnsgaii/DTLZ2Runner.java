package thiagodnf.rnsgaii;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.comparator.PreferenceDistanceComparator;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class DTLZ2Runner extends AbstractRunner {

	public static boolean gui = false;

	public static void main(String[] args) {
		new DTLZ2Runner().run();
	}
	
	public void run() {

		System.out.println("Running " + DTLZ2Runner.class.getSimpleName());

		DoubleProblem problem = new DTLZ2(11, 3);

		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(0.2, 0.2, 0.6));
		referencePoints.add(PointSolutionUtils.createSolution(0.8, 0.6, 1.0));
		
		new SolutionListOutput(referencePoints)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("RP.tsv"))
	        .print();		

		double epsilon = 0.001;

		int populationSize = 500;
		int maxEvaluations = 500 * populationSize;

		CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(0.9, 10.0);
		MutationOperator<DoubleSolution> mutation = new PolynomialMutation(0.01, 20.0);
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<DoubleSolution>(new PreferenceDistanceComparator<>());

		List<DoubleSolution> populationForRNSGAII = runRNSGAII(problem, populationSize, maxEvaluations, crossover, mutation, selection, referencePoints, epsilon);
		    
		new SolutionListOutput(populationForRNSGAII)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("FUN_"+epsilon+".tsv"))
	        .print();
		
		System.out.println("Done");
	}
}
