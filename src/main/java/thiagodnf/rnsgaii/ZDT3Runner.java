package thiagodnf.rnsgaii;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT3;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.algorithm.RNSGAII;
import thiagodnf.rnsgaii.comparator.PreferenceDistanceComparator;
import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class ZDT3Runner {
	
	public static boolean gui = true;
	
	public static void main(String[] args) {
		
		System.out.println("Running...");
		
		DoubleProblem problem = new ZDT3(30);

		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(0.1, 0.6));
		referencePoints.add(PointSolutionUtils.createSolution(0.3, 0.2));
		referencePoints.add(PointSolutionUtils.createSolution(0.7, -0.2));
		
		double epsilon = 0.001;
		
		int populationSize = 100;
		int maxEvaluations = 10000 * populationSize;
		
		CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(0.9, 10.0);
	    MutationOperator<DoubleSolution> mutation = new PolynomialMutation(0.01, 20.0);
	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<DoubleSolution>(new PreferenceDistanceComparator<>()) ;
		
//	    
	    
	    NSGAII<DoubleSolution> algorithm = new NSGAII<DoubleSolution>(
    		problem, 
    		maxEvaluations, 
    		populationSize, 
    		crossover,
    		mutation, 
    		selection,
    		new SequentialSolutionListEvaluator<DoubleSolution>()
	    );
	    
	    new AlgorithmRunner.Executor(algorithm).execute() ;
		
	    List<DoubleSolution> population = algorithm.getResult() ;
	    
	    new SolutionListOutput(population)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
	        .print();
	    
		if (gui) {
			
			List<DataSet> datasets = new ArrayList<>();
			
			datasets.add(new DataSet("Reference Points", referencePoints));
			
			ScatterPlot.show(
					datasets, 
					new double[] {0.0, 1.0}, 
					new double[] {-1.0, 1.0});
		}
	    
		System.out.println("Done");
	}
}
