package ufpr.gres.rnsgaii;

import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.point.PointSolution;

import ufpr.gres.rnsgaii.algorithm.RNSGAII;
import ufpr.gres.rnsgaii.gui.DataSet;

public abstract class AbstractRunner extends AbstractAlgorithmRunner {

	
	public<S extends Solution<?>> List<S> runNSGAII(
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
    		populationSize,
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
	
	public List<DataSet> run(List<PointSolution> referencePoints, double epsilon) {
		return null;
	}
}
