package thiagodnf.rnsgaii.util;

import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.rnsgaii.comparator.DistanceToReferencePointComparator;
import thiagodnf.rnsgaii.distance.PreferenceDistance;

public class Converter {
	
	
	public static<S extends Solution<?>> String print(List<S> solutionList) {
		
		StringBuffer buffer = new StringBuffer();
		
		for(S s : solutionList) {
			
			buffer.append(Arrays.toString(s.getObjectives()));
			buffer.append(", ").append(s.getAttribute(DistanceToReferencePointComparator.KEY+0));
			buffer.append(", ").append(s.getAttribute(DistanceToReferencePointComparator.KEY+1));
			buffer.append(", ").append(s.getAttribute("ranking_to_rp_0"));
			buffer.append(", ").append(s.getAttribute("ranking_to_rp_1"));
			buffer.append(", ").append(s.getAttribute(PreferenceDistance.KEY));
			buffer.append("\n");
			
		}
		
		return buffer.toString();
	}

}
