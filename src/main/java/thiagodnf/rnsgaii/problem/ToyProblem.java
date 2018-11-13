package thiagodnf.rnsgaii.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class ToyProblem extends AbstractDoubleProblem {

	private static final long serialVersionUID = 3750846353792807137L;

	public ToyProblem() {
		this(1);
	}
	
	public ToyProblem(Integer numberOfVariables) {
		
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(2);
		setName("Toy Example");

		List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(0.0);
			upperLimit.add(1.0);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	@Override
	public void evaluate(DoubleSolution solution) {

		double f0 = solution.getVariableValue(0);
		double f1 = 1.0 - f0;

		solution.setObjective(0, f0);
		solution.setObjective(1, f1);
	}
}
