package thiagodnf.rnsgaii;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateFriedmanTestTables;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.algorithm.RNSGAII;
import thiagodnf.rnsgaii.comparator.PreferenceDistanceComparator;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class ExperimentRunner {
	
	private static final int INDEPENDENT_RUNS = 30;

	public static void main(String[] args) throws IOException {
	   
		List<PointSolution> referencePoints = new ArrayList<>();

		referencePoints.add(PointSolutionUtils.createSolution(0.2, 0.4));
		referencePoints.add(PointSolutionUtils.createSolution(0.8, 0.4));
		
		double epsilon = 0.0001;
		
		int populationSize = 100;
		int maxEvaluations = 10000 * populationSize;
		
		CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(0.9, 10.0);
	    MutationOperator<DoubleSolution> mutation = new PolynomialMutation(0.01, 20.0);
	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<DoubleSolution>(new PreferenceDistanceComparator<>()) ;
	   
	    
		
		List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
	    
	    problemList.add(new ExperimentProblem<>(new ZDT1()));
	    

	    List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList = new ArrayList<>();
	    
		for (int run = 0; run < INDEPENDENT_RUNS; run++) {

			for (int i = 0; i < problemList.size(); i++) {
				
				NSGAII<DoubleSolution> algorithm = new RNSGAII<DoubleSolution>(
					problemList.get(i).getProblem(), 
		    		maxEvaluations, 
		    		populationSize, 
		    		crossover,
		    		mutation, 
		    		selection,
		    		referencePoints,
		    		epsilon
			    );
				
				algorithmList.add(new ExperimentAlgorithm<>(algorithm, "R-NSGA-II", problemList.get(i), run));
				
				Algorithm<List<DoubleSolution>> nsgaii = new NSGAIIBuilder<>(
	                  problemList.get(i).getProblem(),
	                  crossover,
	                  mutation)
	                  .setMaxEvaluations(maxEvaluations)
	                  .setPopulationSize(populationSize)
	                  .build();
				
				algorithmList.add(new ExperimentAlgorithm<>(nsgaii, "NSGA-II", problemList.get(i), run));
			}
		}
	    
	    Experiment<DoubleSolution, List<DoubleSolution>> experiment =
	            new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("NSGAII-and-R-NSGAII-Study")
	                    .setAlgorithmList(algorithmList)
	                    .setProblemList(problemList)
	                    .setExperimentBaseDirectory("output")
	                    .setOutputParetoFrontFileName("FUN")
	                    .setOutputParetoSetFileName("VAR")
	                    .setReferenceFrontDirectory("src/main/resources/pareto_fronts")
	                    .setIndicatorList(Arrays.asList(
	                            new Epsilon<DoubleSolution>(),
	                            new Spread<DoubleSolution>(),
	                            new GenerationalDistance<DoubleSolution>(),
	                            new PISAHypervolume<DoubleSolution>(),
	                            new InvertedGenerationalDistance<DoubleSolution>(),
	                            new InvertedGenerationalDistancePlus<DoubleSolution>()))
	                    .setIndependentRuns(INDEPENDENT_RUNS)
	                    .setNumberOfCores(8)
	                    .build();

	    new ExecuteAlgorithms<>(experiment).run();
	    new ComputeQualityIndicators<>(experiment).run();
	    new GenerateLatexTablesWithStatistics(experiment).run();
	    new GenerateWilcoxonTestTablesWithR<>(experiment).run();
	    new GenerateFriedmanTestTables<>(experiment).run();
	    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();
	  }
}
