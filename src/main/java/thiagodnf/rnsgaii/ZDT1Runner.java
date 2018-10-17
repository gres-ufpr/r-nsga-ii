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
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.algorithm.RNSGAII;
import thiagodnf.rnsgaii.comparator.PreferenceDistanceComparator;
import thiagodnf.rnsgaii.gui.DataSet;
import thiagodnf.rnsgaii.gui.ScatterPlot;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class ZDT1Runner extends AbstractRunner{
	
	public static boolean gui = true;
	
	public static void main(String[] args) {
		
		System.out.println("Running "+ZDT1Runner.class.getSimpleName());
		
		List<DataSet> datasets = new ArrayList<>();
		
		DoubleProblem problem = new ZDT1(30);

		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(0.2, 0.4));
		referencePoints.add(PointSolutionUtils.createSolution(0.8, 0.4));
		
		datasets.add(new DataSet("Reference Points", referencePoints));
		
		double epsilon = 0.0001;
		
		int populationSize = 100;
		int maxEvaluations = 10000 * populationSize;
		
		CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(0.9, 10.0);
	    MutationOperator<DoubleSolution> mutation = new PolynomialMutation(0.01, 20.0);
	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<DoubleSolution>(new PreferenceDistanceComparator<>()) ;
	    
	    //List<DoubleSolution> populationForNSGAII = runNSGAII(problem, populationSize, maxEvaluations, crossover, mutation, selection);
	    List<DoubleSolution> populationForRNSGAII = runRNSGAII(problem, populationSize, maxEvaluations, crossover, mutation, selection, referencePoints, epsilon);
	    
	    //datasets.add(new DataSet("NSGA-II", PointSolutionUtils.convert(populationForNSGAII)));
	    datasets.add(new DataSet("R-NSGA-II", PointSolutionUtils.convert(populationForRNSGAII)));
	    
	    
		if (gui) {
			
			
			
			ScatterPlot.show(
					datasets, 
					new double[] {0.0,1.0}, 
					new double[] {0.0,1.0});
		}
	    
		System.out.println("Done");
	}
}
