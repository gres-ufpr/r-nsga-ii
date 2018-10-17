package thiagodnf.rnsgaii;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.algorithm.RNSGAII;
import thiagodnf.rnsgaii.comparator.PreferenceDistanceComparator;
import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;
import thiagodnf.rnsgaii.problem.OneZeroProblem;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class OneZeroRunner {
	
	public static boolean gui = true;

	public static void main(String[] args) {
		
		System.out.println("Running...");
		
		OneZeroProblem problem = new OneZeroProblem(100);

		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(-0.9, -0.2));
//		referencePoints.add(PointSolutionUtils.createSolution(-0.2, -0.9));
		
//		referencePoints.add(PointSolutionUtils.createSolution(-0.5, -0.6));
		
		double epsilon = 0.001;
		
		int populationSize = 100;
		int maxEvaluations = 10000 * populationSize;
		
		CrossoverOperator<BinarySolution> crossover = new SinglePointCrossover(0.9) ;
	    MutationOperator<BinarySolution> mutation = new BitFlipMutation(0.001) ;
	    SelectionOperator<List<BinarySolution>, BinarySolution> selection = new BinaryTournamentSelection<BinarySolution>(new PreferenceDistanceComparator<>()) ;
		
	    NSGAII<BinarySolution> algorithm = new RNSGAII<BinarySolution>(
    		problem, 
    		maxEvaluations, 
    		populationSize, 
    		crossover,
    		mutation, 
    		selection,
    		referencePoints,
    		epsilon
	    );
	    
//	    NSGAII<BinarySolution> algorithm = new NSGAII<BinarySolution>(
//    		problem, 
//    		maxEvaluations, 
//    		populationSize, 
//    		crossover,
//    		mutation, 
//    		selection,
//    		new SequentialSolutionListEvaluator<BinarySolution>()
//	    );
	    
	    new AlgorithmRunner.Executor(algorithm).execute() ;
		
	    List<BinarySolution> population = algorithm.getResult() ;
	    
	    new SolutionListOutput(population)
	        .setSeparator("\t")
	        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
	        .print();
	    
		if (gui) {
			
			List<DataSet> datasets = new ArrayList<>();
			
			datasets.add(new DataSet("Reference Points", referencePoints));
			
			ScatterPlot.show(
					datasets, new double[] {-1.0, 0.0}, new double[] {-1.0, 0.0});
		}
	    
		System.out.println("Done");
	}
}
