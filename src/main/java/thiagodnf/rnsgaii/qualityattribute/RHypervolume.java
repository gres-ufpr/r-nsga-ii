package thiagodnf.rnsgaii.qualityattribute;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

import thiagodnf.rnsgaii.rmetric.RMetric;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class RHypervolume<S extends Solution<?>> extends PISAHypervolume<S> {

	private static final long serialVersionUID = 6046908284567336401L;
	
	protected RMetric rMetric;

	public RHypervolume(PointSolution zr, double delta) {
		this.rMetric = new RMetric(zr, delta);
	}
	
	@Override
	public Double evaluate(List<S> approximation) {
		
		Preconditions.checkNotNull(approximation, "The pareto front approximation is null");

		List<PointSolution> population = PointSolutionUtils.convert(approximation);

		return hypervolume(new ArrayFront(population), rMetric.getZr().getNumberOfObjectives());
	}
	
	private double hypervolume(Front front, int numberOfObjectives) {

		Front invertedFront = FrontUtils.getInvertedFront(front);

		// STEP4. The hypervolume (control is passed to the Java version of Zitzler code)
		return this.calculateHypervolume(FrontUtils.convertFrontToArray(invertedFront), invertedFront.getNumberOfPoints(), numberOfObjectives);
	}
}
