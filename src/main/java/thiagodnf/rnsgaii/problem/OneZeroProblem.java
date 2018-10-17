package thiagodnf.rnsgaii.problem;

import java.util.BitSet;

import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;

import com.google.common.base.Preconditions;

public class OneZeroProblem extends AbstractBinaryProblem {

	private static final long serialVersionUID = -4848206373510353218L;

	private int numberOfBits;

	/**
	 * Constructor
	 */
	public OneZeroProblem() {
		this(512);
	}

	/**
	 * Constructor
	 */
	public OneZeroProblem(int numberOfBits) {

		Preconditions.checkArgument(numberOfBits > 0, "The number of bits should be > 0");

		setNumberOfVariables(1);
		setNumberOfObjectives(2);
		setName(OneZeroProblem.class.getSimpleName());

		this.numberOfBits = numberOfBits;
	}

	@Override
	protected int getBitsPerVariable(int index) {
		
		Preconditions.checkArgument(index == 0, "This problem has only one variable");
		
		return numberOfBits;
	}

	@Override
	public BinarySolution createSolution() {
		return new DefaultBinarySolution(this);
	}

	public void evaluate(BinarySolution solution) {

		Preconditions.checkNotNull(solution, "The solution should not be null");

		int counterOnes = 0;
		int counterZeroes = 0;

		BitSet bitset = solution.getVariableValue(0);

		for (int i = 0; i < bitset.length(); i++) {
			if (bitset.get(i)) {
				counterOnes++;
			} else {
				counterZeroes++;
			}
		}
		
		double objctiveOne = (double) counterOnes / (double) numberOfBits;
		double objctiveTwo = (double) counterZeroes / (double) numberOfBits;

		// OneZeroMax is a maximization problem. Then you have to multiply by -1 to
		// minimize
		solution.setObjective(0, -1.0 * (objctiveOne));
		solution.setObjective(1, -1.0 * (objctiveTwo));
	}
}
