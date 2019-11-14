package ufpr.gres.rnsgaii.replacement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import com.google.common.base.Preconditions;

import ufpr.gres.rnsgaii.attribute.PreferenceDistance;
import ufpr.gres.rnsgaii.comparator.PreferenceDistanceComparator;
import ufpr.gres.rnsgaii.util.EuclideanDistanceUtils;
import ufpr.gres.rnsgaii.util.PointSolutionUtils;

public class RankingAndPreferenceReplacement<S extends Solution<?>> extends RankingAndCrowdingSelection<S> {

	private static final long serialVersionUID = 2840694213029065690L;

	private int solutionsToSelect;
	
	private Comparator<S> dominanceComparator ;
	
	private List<PointSolution> referencePoints;
	
	private double epsilon;
	
	public RankingAndPreferenceReplacement(List<PointSolution> referencePoints, double epsilon, int solutionsToSelect) {
		this(referencePoints, epsilon, solutionsToSelect, new DominanceComparator<S>());
	}

	public RankingAndPreferenceReplacement(List<PointSolution> referencePoints, double epsilon, int solutionsToSelect, Comparator<S> dominanceComparator) {
		super(solutionsToSelect, dominanceComparator);

		this.epsilon = epsilon;
		this.referencePoints = referencePoints;
		this.dominanceComparator = dominanceComparator;
		this.solutionsToSelect = solutionsToSelect;
	}

	/** Execute() method */
	public List<S> execute(List<S> solutionList) throws JMetalException {
		
		Preconditions.checkNotNull(solutionList, "The solution list should not be null");
		Preconditions.checkArgument(solutionList.size() >= solutionsToSelect, "The solution list should be >= "+ solutionsToSelect);
		
		Ranking<S> ranking = new DominanceRanking<S>(dominanceComparator);
		ranking.computeRanking(solutionList);

		return preferenceDistanceSelection(solutionList, ranking);
	}
	
	protected List<S> preferenceDistanceSelection(List<S> solutionList, Ranking<S> ranking) {
		
		Preconditions.checkNotNull(ranking, "The ranking should not be null");
		Preconditions.checkNotNull(solutionList, "The solution list should not be null");
		Preconditions.checkArgument(solutionList.size() >= solutionsToSelect, "The solution list should be >= "+ solutionsToSelect);
		
		int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
		
		double[] fmin = new double[numberOfObjectives];
		double[] fmax = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			Collections.sort(solutionList, new ObjectiveComparator<S>(i));

			fmin[i] = solutionList.get(0).getObjective(i);
			fmax[i] = solutionList.get(solutionList.size() - 1).getObjective(i);
		}
		
		PreferenceDistance<S> preferenceDistance = new PreferenceDistance<S>(referencePoints, fmin, fmax);
		
		//calculate  preference  distance  of  each  fronts' individual using nitching strategy specified in Fig. 2
		preferenceDistance.computeDensityEstimator(solutionList);
		
		solutionList = clearEpsilon(solutionList, fmin, fmax);
		
		List<S> population = new ArrayList<>(solutionsToSelect);
		
		int rankingIndex = 0;
		
		while (population.size() < solutionsToSelect) {
			
			if (subfrontFillsIntoThePopulation(ranking, rankingIndex, population)) {
				addRankedSolutionsToPopulation(ranking, rankingIndex, population);
				rankingIndex++;
			} else {
				addLastRankedSolutionsToPopulation(ranking, rankingIndex, population, fmin, fmax);
			}
		}
		
		return population;
	}
	
	protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population, double[] fmin, double[] fmax) {
		
		List<S> currentRankedFront = ranking.getSubfront(rank);
		
		Collections.sort(currentRankedFront, new PreferenceDistanceComparator<S>());
		
		int i = 0;
		
		while (population.size() < solutionsToSelect) {
			population.add(currentRankedFront.get(i));
			i++;
		}
	}
	
	protected List<S> clearEpsilon(List<S> population, double[] fmin, double[] fmax) {
		
		List<S> nextPopulation = new ArrayList<>();
		
		List<S> temporalList = new LinkedList<S>(population);
		
		while (!temporalList.isEmpty()) {
			
			int indexRandom = JMetalRandom.getInstance().nextInt(0, temporalList.size() - 1);

			S randomSolution = temporalList.get(indexRandom);
			
			nextPopulation.add(randomSolution);
			temporalList.remove(indexRandom);
			
			PointSolution p = PointSolutionUtils.createSolution(randomSolution.getObjectives());
			
			List<S> group = new ArrayList<>();

			for (int i = 0; i < temporalList.size(); i++) {

				PointSolution q = PointSolutionUtils.createSolution(temporalList.get(i).getObjectives());

				double sum = 0.0;

				for (int j = 0; j < q.getNumberOfObjectives(); j++) {
					sum += (Math.abs(p.getObjective(j) - q.getObjective(j)) / (fmax[j] - fmin[j]));
				}
				
				if (sum <= epsilon) {
					group.add(temporalList.get(i));
				}
			}

			for (S s : group) {
				
				int value = (int) s.getAttribute(PreferenceDistance.KEY);
				
				s.setAttribute(PreferenceDistance.KEY, value+population.size());
				
				nextPopulation.add(s);
				
				temporalList.remove(s);
			}
		}
		
		return nextPopulation;
	}
}
