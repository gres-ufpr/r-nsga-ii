package thiagodnf.rnsgaii;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.comparator.PreferenceDistanceComparator;
import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;
import thiagodnf.rnsgaii.problem.KnapsackProblem;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class KPRunner extends AbstractRunner{
	
	public static void main(String[] args) {
		
		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(1.0, 0.1));
		referencePoints.add(PointSolutionUtils.createSolution(0.1, 0.9));
		
		List<DataSet> datasets = new KPRunner().run(referencePoints, 0.000001);
		
		ScatterPlot.show(datasets, new double[] {-0.1,1.1}, new double[] {-0.1, 1.1});
	}
	
	public List<DataSet> run(List<PointSolution> referencePoints, double epsilon) {

		System.out.println("Running " + KPRunner.class.getSimpleName() + " w/ " + epsilon);
		
		List<DataSet> datasets = new ArrayList<>();
		
		BinaryProblem problem = new KnapsackProblem("src/main/resources/kp/p50.kp");

		datasets.add(new DataSet("Reference Points", referencePoints));
		
		int populationSize = 100;
		int maxEvaluations = 500 * populationSize;
		
		CrossoverOperator<BinarySolution> crossover = new SinglePointCrossover(0.9);
	    MutationOperator<BinarySolution> mutation = new BitFlipMutation(0.005);
	    SelectionOperator<List<BinarySolution>, BinarySolution> selection = new BinaryTournamentSelection<BinarySolution>(new PreferenceDistanceComparator<>()) ;
	    
	    List<BinarySolution> populationForRNSGAII = runRNSGAII(problem, populationSize, maxEvaluations, crossover, mutation, selection, referencePoints, epsilon);
	    
	    datasets.add(new DataSet("R-NSGA-II w/ Epsilon=" + epsilon, PointSolutionUtils.convert(populationForRNSGAII)));
	    
	    System.out.println("Done");
	    
	    return datasets;
	}
}
