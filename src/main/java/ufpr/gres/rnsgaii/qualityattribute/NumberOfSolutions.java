package ufpr.gres.rnsgaii.qualityattribute;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

public class NumberOfSolutions extends GenericIndicator<PointSolution> {

	private static final long serialVersionUID = 6046908284567336401L;
	
	public NumberOfSolutions(PointSolution zr, double delta, Front referenceParetoFront) {
		super(referenceParetoFront);
	}
	
	public Double evaluate(Front approximation) {
		return evaluate(FrontUtils.convertFrontToSolutionList(approximation));
	}
	
	@Override
	public Double evaluate(List<PointSolution> approximation) {
		
		Preconditions.checkNotNull(approximation, "The pareto front approximation is null");

		return (double) approximation.size();
	}
	
	@Override
	public boolean isTheLowerTheIndicatorValueTheBetter() {
		return false;
	}
}
