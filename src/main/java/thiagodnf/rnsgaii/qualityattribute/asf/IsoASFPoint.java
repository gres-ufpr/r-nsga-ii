package thiagodnf.rnsgaii.qualityattribute.asf;

import java.util.Collections;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

public class IsoASFPoint<S extends Solution<?>> {
	
	protected S zr;

	protected double[] weights;
	
	public IsoASFPoint(S zr) {
		this(zr, Collections.nCopies(zr.getNumberOfObjectives(), 1.0));
	}

	public IsoASFPoint(S zr, List<Double> weights) {
		this(zr, weights.stream().mapToDouble(x -> x).toArray());
	}

	public IsoASFPoint(S zr, double[] weights) {
		this.zr = zr;
		this.weights = weights;
	}
	
	public PointSolution calculate(S a) {

		ASF<S> asf = new WeightedChebyshev<>(zr, weights);

		double delta = asf.calculate(a);

		PointSolution al = new PointSolution(a.getNumberOfObjectives());

		for (int i = 0; i < a.getNumberOfObjectives(); i++) {
			al.setObjective(i, zr.getObjective(i) + delta * weights[i]);
		}

		return al;
	}
}
