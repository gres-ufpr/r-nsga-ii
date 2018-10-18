package thiagodnf.rnsgaii.comparator;

import java.util.Comparator;

import org.uma.jmetal.solution.Solution;

public class DistanceToRPComparator<S extends Solution<?>> implements Comparator<S> {

	private int rpIndex;
	
	public final static String KEY = "distance_to_rp_";

	public DistanceToRPComparator(int rpIndex) {
		this.rpIndex = rpIndex;
	}

	@Override
	public int compare(S o1, S o2) {

		double v1 = (double) o1.getAttribute(KEY + rpIndex);
		double v2 = (double) o2.getAttribute(KEY + rpIndex);

		return Double.compare(v1, v2);
	}

}
