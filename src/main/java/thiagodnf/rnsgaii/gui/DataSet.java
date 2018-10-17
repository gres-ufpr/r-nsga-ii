package thiagodnf.rnsgaii.gui;

import java.util.List;

import org.uma.jmetal.util.point.PointSolution;

import com.google.common.base.Preconditions;

public class DataSet {

	private String name;
	
	private List<PointSolution> solutions;
	
	public DataSet(String name, List<PointSolution> solutions) {
		
		Preconditions.checkNotNull(name, "The dataset name should not be null");
		Preconditions.checkArgument(!name.isEmpty(), "The dataset name should not be empty");
		Preconditions.checkNotNull(solutions, "The solutions should not be null");
		
		this.name = name;
		this.solutions = solutions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PointSolution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<PointSolution> solutions) {
		this.solutions = solutions;
	}
}
