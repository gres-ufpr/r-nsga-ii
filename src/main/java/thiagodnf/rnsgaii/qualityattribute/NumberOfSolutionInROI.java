package thiagodnf.rnsgaii.qualityattribute;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

import thiagodnf.rnsgaii.rmetric.RMetric;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class NumberOfSolutionInROI extends GenericIndicator<PointSolution> {

	private static final long serialVersionUID = 6292740869882877883L;
	
	protected RMetric rMetric;

	public NumberOfSolutionInROI(PointSolution zr, double delta, Front referenceParetoFront){
		super(referenceParetoFront);
		
		this.rMetric = new RMetric(zr, delta);
	}
	
	public Double evaluate(Front approximation) {
		return evaluate(FrontUtils.convertFrontToSolutionList(approximation));
	}
	
	@Override
	public Double evaluate(List<PointSolution> approximation) {
		
		Preconditions.checkNotNull(approximation, "The pareto front approximation is null");

		List<PointSolution> S = PointSolutionUtils.copy(approximation);
		
		// Step 1
		S = rMetric.prescreeningProcedure(S);
		
		// Step 2
		PointSolution zp = rMetric.pivotPointIdentification(S);
		
		// Step 3
		S = rMetric.trimmingProcedure(zp, S);
		
		return (double) S.size();
	}

	@Override
	public boolean isTheLowerTheIndicatorValueTheBetter() {
		return false;
	}
	
}
