package thiagodnf.rnsgaii;

import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.algorithm.RNSGAII;

public abstract class AbstractRunner {

	
	public static<S extends Solution<?>> List<S> runNSGAII(
			Problem<S> problem, 
			int populationSize, 
			int maxEvaluations, 
			CrossoverOperator<S> crossover,
			MutationOperator<S> mutation,
			SelectionOperator<List<S>,S> selection) {
	    
	    NSGAII<S> algorithm = new NSGAII<S>(
    		problem, 
    		maxEvaluations, 
    		populationSize, 
    		crossover,
    		mutation, 
    		selection,
    		new SequentialSolutionListEvaluator<S>()
	    );
	    
	    new AlgorithmRunner.Executor(algorithm).execute() ;
		
	   return algorithm.getResult() ;
	}
	
	public static<S extends Solution<?>> List<S> runRNSGAII(
			Problem<S> problem, 
			int populationSize, 
			int maxEvaluations, 
			CrossoverOperator<S> crossover,
			MutationOperator<S> mutation,
			SelectionOperator<List<S>,S> selection,
			List<PointSolution> referencePoints,
			double epsilon) {
	    
		NSGAII<S> algorithm = new RNSGAII<S>(
    		problem, 
    		maxEvaluations, 
    		populationSize, 
    		crossover,
    		mutation, 
    		selection,
    		referencePoints,
    		epsilon
	    );
	    
	    new AlgorithmRunner.Executor(algorithm).execute() ;
		
	   return algorithm.getResult() ;
	}
}
