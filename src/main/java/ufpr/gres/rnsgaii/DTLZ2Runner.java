package ufpr.gres.rnsgaii;

import java.io.FileNotFoundException;
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

import ufpr.gres.rnsgaii.comparator.PreferenceDistanceComparator;
import ufpr.gres.rnsgaii.util.PointSolutionUtils;

public class DTLZ2Runner extends AbstractRunner {

	public static void main(String[] args) {
		
		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(0.2, 0.2, 0.6));
		referencePoints.add(PointSolutionUtils.createSolution(0.8, 0.6, 1.0));
		
		new DTLZ2Runner().generate(referencePoints, 0.0001);
	}
	
	public void generate(List<PointSolution> referencePoints, double epsilon) {

		System.out.println("Running " + DTLZ2Runner.class.getSimpleName());

		DoubleProblem problem = new DTLZ2(11, 3);

		new SolutionListOutput(referencePoints)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("RP.tsv"))
	        .print();		

		int populationSize = 100;
		int maxEvaluations = 10000 * populationSize;

		CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(0.9, 10.0);
		MutationOperator<DoubleSolution> mutation = new PolynomialMutation(0.01, 20.0);
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<DoubleSolution>(new PreferenceDistanceComparator<>());

		List<DoubleSolution> populationForRNSGAII = runRNSGAII(problem, populationSize, maxEvaluations, crossover, mutation, selection, referencePoints, epsilon);
		    
		new SolutionListOutput(populationForRNSGAII)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("FUN_"+epsilon+".tsv"))
	        .print();
		
		String referenceParetoFront = "src/main/resources/pareto_fronts/DTLZ2.3D.pf" ;
	    
	    try {
			printQualityIndicators(populationForRNSGAII, referenceParetoFront) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done");
	}
}
