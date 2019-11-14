package ufpr.gres.rnsgaii.qualityattribute;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

import ufpr.gres.rnsgaii.rmetric.RMetric;
import ufpr.gres.rnsgaii.util.PointSolutionUtils;

public class RHypervolume extends PISAHypervolume<PointSolution> {

	private static final long serialVersionUID = 6046908284567336401L;
	
	protected RMetric rMetric;

	public RHypervolume(PointSolution zr, double delta, Front referenceParetoFront) {
		super(referenceParetoFront);
		
		this.rMetric = new RMetric(zr, delta);
	}
	
	public Double evaluate(Front approximation) {
		return evaluate(FrontUtils.convertFrontToSolutionList(approximation));
	}
	
	@Override
	public Double evaluate(List<PointSolution> approximation) {
		
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
