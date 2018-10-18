package thiagodnf.rnsgaii.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

import thiagodnf.rnsgaii.selection.RankingAndPreferenceSelection;

public class RNSGAII<S extends Solution<?>> extends NSGAII<S>{

	private static final long serialVersionUID = -2060417835574892395L;

	private double epsilon;
	
	private List<PointSolution> referencePoints;
	
	public RNSGAII(
			Problem<S> problem, 
			int maxEvaluations, 
			int populationSize, 
			CrossoverOperator<S> crossover,
			MutationOperator<S> mutation, 
			SelectionOperator<List<S>, S> selection,
			List<PointSolution> referencePoints,
			double epsilon) {
		super(
			problem, 
			maxEvaluations, 
			populationSize, 
			crossover, 
			mutation, 
			selection,
			new SequentialSolutionListEvaluator<S>());
		
		Preconditions.checkNotNull(referencePoints, "The reference point list should not be null");
		Preconditions.checkArgument(!referencePoints.isEmpty(), "The reference point list should not be empty");
		
		this.referencePoints = referencePoints;
		this.epsilon = epsilon;
	}
	
	@Override
	protected List<S> replacement(List<S> population, List<S> offspringPopulation) {

		List<S> jointPopulation = new ArrayList<>();

		jointPopulation.addAll(population);
		jointPopulation.addAll(offspringPopulation);

		int solutionToSelect = getMaxPopulationSize();
		
		return new RankingAndPreferenceSelection<S>(referencePoints, epsilon, solutionToSelect).execute(jointPopulation);
	}
}
