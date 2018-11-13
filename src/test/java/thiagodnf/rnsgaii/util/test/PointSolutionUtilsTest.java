package thiagodnf.rnsgaii.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class PointSolutionUtilsTest {

	@Test
	public void shouldReturnFalseWhenComparesSolutionsWithDifferentNumberOfObjectives() {
		
		PointSolution s1 = PointSolutionUtils.createSolution(0.0, 1.0);
		PointSolution s2 = PointSolutionUtils.createSolution(0.0, 1.0, 0.2);
		
		assertFalse(PointSolutionUtils.equals(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenComparesSolutionsWithDifferentObjectivesValue() {
		
		PointSolution s1 = PointSolutionUtils.createSolution(0.0, 1.0);
		PointSolution s2 = PointSolutionUtils.createSolution(0.0, 2.0);
		
		assertFalse(PointSolutionUtils.equals(s1, s2));
	}
	
	@Test
	public void shouldReturnTrueWhenComparesSolutionsWithSameObjectivesValue() {
		
		PointSolution s1 = PointSolutionUtils.createSolution(2.0, 2.0);
		PointSolution s2 = PointSolutionUtils.createSolution(2.0, 2.0);
		
		assertTrue(PointSolutionUtils.equals(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenTheSolutionIsNotInTheList() {
		
		List<PointSolution> solutions = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0)
		);
		
		PointSolution s = PointSolutionUtils.createSolution(2.0, 2.0);
		
		assertFalse(PointSolutionUtils.contains(solutions, s));
	}
	
	@Test
	public void shouldReturnTrueWhenTheSolutionIsNotInTheList() {
		
		List<PointSolution> solutions = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0)
		);
		
		PointSolution s = PointSolutionUtils.createSolution(1.0, 2.0);
		
		assertTrue(PointSolutionUtils.contains(solutions, s));
	}
	
	@Test
	public void shouldReturnEmptyWhenUnionTwoEmptySolutionList() {
		
		List<PointSolution> S1 = new ArrayList<>();
		List<PointSolution> S2 = new ArrayList<>();
		
		List<PointSolution> union = PointSolutionUtils.union(S1, S2);
		
		assertTrue(union.isEmpty());
	}
	
	@Test
	public void shouldReturnCorrectSetWhenUnionTwoSolutionListNotSimiliarElements() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(2.0, 3.0)
		);
		
		List<PointSolution> union = PointSolutionUtils.union(S1, S2);
		
		assertFalse(union.isEmpty());
		assertEquals(union.size(), 2);
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(1.0, 2.0)));
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(2.0, 3.0)));
	}
	
	@Test
	public void shouldReturnCorrectSetWhenUnionTwoSolutionListSimiliarElements() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0)
		);
		
		List<PointSolution> union = PointSolutionUtils.union(S1, S2);
		
		assertFalse(union.isEmpty());
		assertEquals(union.size(), 1);
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(1.0, 2.0)));
	}
	
	@Test
	public void shouldReturnCorrectSetWhenUnionTwoSolutionListMoreElements() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(3.0, 4.0),
			PointSolutionUtils.createSolution(4.0, 5.0)
		);
		
		List<PointSolution> union = PointSolutionUtils.union(S1, S2);
		
		assertFalse(union.isEmpty());
		assertEquals(union.size(), 4);
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(1.0, 2.0)));
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(2.0, 3.0)));
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(3.0, 4.0)));
		assertTrue(PointSolutionUtils.contains(union, PointSolutionUtils.createSolution(4.0, 5.0)));
	}	
	
	@Test
	public void shouldReturnCorrectRelativeComplementToAExample1() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(4.0, 5.0),
			PointSolutionUtils.createSolution(5.0, 6.0),
			PointSolutionUtils.createSolution(1.0, 2.0)
		);
		
		List<PointSolution> minus = PointSolutionUtils.minus(S1, S2);
		
		assertEquals(minus.size(), 2);
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(2.0, 3.0)));
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(3.0, 4.0)));
	}
	
	@Test
	public void shouldReturnCorrectRelativeComplementToAExample2() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0),
			PointSolutionUtils.createSolution(4.0, 5.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(3.0, 4.0),
			PointSolutionUtils.createSolution(4.0, 5.0),
			PointSolutionUtils.createSolution(5.0, 6.0),
			PointSolutionUtils.createSolution(6.0, 7.0)
		);
		
		List<PointSolution> minus = PointSolutionUtils.minus(S1, S2);
		
		assertEquals(minus.size(), 2);
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(1.0, 2.0)));
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(2.0, 3.0)));
	}
	
	@Test
	public void shouldReturnEmptyRelativeComplementToAWithTheSameSet() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0),
			PointSolutionUtils.createSolution(4.0, 5.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0),
			PointSolutionUtils.createSolution(4.0, 5.0)
		);
		
		List<PointSolution> minus = PointSolutionUtils.minus(S1, S2);
		
		assertTrue(minus.isEmpty());
	}
	
	@Test
	public void shouldReturnTheSetARelativeComplementToAWithTheSameSet() {
		
		List<PointSolution> S1 = Arrays.asList(
			PointSolutionUtils.createSolution(1.0, 2.0),
			PointSolutionUtils.createSolution(2.0, 3.0),
			PointSolutionUtils.createSolution(3.0, 4.0)
		);
		
		List<PointSolution> S2 = Arrays.asList(
			PointSolutionUtils.createSolution(5.0, 6.0)
		);
		
		List<PointSolution> minus = PointSolutionUtils.minus(S1, S2);
		
		assertEquals(minus.size(), 3);
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(1.0, 2.0)));
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(2.0, 3.0)));
		assertTrue(PointSolutionUtils.contains(minus, PointSolutionUtils.createSolution(3.0, 4.0)));
	}
}
