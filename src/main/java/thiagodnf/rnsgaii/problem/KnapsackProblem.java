package thiagodnf.rnsgaii.problem;

import java.io.File;
import java.util.Arrays;
import java.util.BitSet;

import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;

import com.google.common.base.Preconditions;

import thiagodnf.rnsgaii.util.InstanceReaderUtils;

public class KnapsackProblem extends AbstractBinaryProblem {

	private static final long serialVersionUID = 4288759882399626169L;

	protected int numberOfItems;

	protected double[] profits;

	protected double[] weights;

	protected double capacity;

	/** Sum of the profits */
	protected double profitR;

	/** Sum of the weights */
	protected double weightR;

	public KnapsackProblem(String filename) {

		InstanceReaderUtils reader = null;

		try {
			reader = new InstanceReaderUtils(new File(filename));
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}

		this.numberOfItems = reader.readIntegerValue();
		this.weights = reader.readDoubleArray();
		this.profits = reader.readDoubleArray();
		this.capacity = reader.readDoubleValue();

		this.profitR = Arrays.stream(profits).reduce(Double::sum).getAsDouble();
		this.weightR = Arrays.stream(weights).reduce(Double::sum).getAsDouble();

		setNumberOfVariables(1);
		setNumberOfObjectives(2);
		setName(KnapsackProblem.class.getSimpleName());
	}

	@Override
	public void evaluate(BinarySolution solution) {

		Preconditions.checkNotNull(solution, "The solution cannot be null");

		BitSet bitset = solution.getVariableValue(0);

		double sumOfProfits = 0.0;
		double sumOfWeights = 0.0;

		for (int i = 0; i < numberOfItems; i++) {
			if (bitset.get(i)) {
				sumOfProfits += profits[i];
				sumOfWeights += weights[i];
			}
		}

		double rateOfProfit = sumOfProfits / profitR;
		double rateOfWeight = sumOfWeights / weightR;

		solution.setObjective(0, 1.0-rateOfProfit);
		solution.setObjective(1, rateOfWeight);
	}

	@Override
	protected int getBitsPerVariable(int index) {
		return numberOfItems;
	}

}
