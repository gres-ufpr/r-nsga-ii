package thiagodnf.rnsgaii.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.rnsgaii.util.EuclideanDistanceUtils;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

public class EuclideanDistanceUtilsTest {

	@Test
	public void shouldThrowExceptionWhenInitiateThisClass() throws IllegalAccessException, InstantiationException {
		
		final Class<?> cls = EuclideanDistanceUtils.class;
		
		final Constructor<?> c = cls.getDeclaredConstructors()[0];
		
		c.setAccessible(true);

		assertThrows(InvocationTargetException.class, () -> {
			c.newInstance((Object[])null);
		});
	}

	@Test
	public void shouldThrowNullExceptionWithNullParameters() {

		PointSolution p = PointSolutionUtils.createSolution(0.0, 0.0);
		PointSolution q = PointSolutionUtils.createSolution(0.0, 0.0);

		assertThrows(NullPointerException.class, () -> {
			EuclideanDistanceUtils.calculate(null, q, new double[2], new double[2]);
		});

		assertThrows(NullPointerException.class, () -> {
			EuclideanDistanceUtils.calculate(p, null, new double[2], new double[2]);
		});

		assertThrows(NullPointerException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, null, new double[2]);
		});

		assertThrows(NullPointerException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, new double[2], null);
		});
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWithDifferentObjectives() {

		PointSolution p = PointSolutionUtils.createSolution(0.0, 0.0);
		PointSolution q = PointSolutionUtils.createSolution(0.0, 0.0, 0.0);

		assertThrows(IllegalArgumentException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, new double[2], new double[2]);
		});
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWithFminAndFmaxSize() {

		PointSolution p = PointSolutionUtils.createSolution(0.0, 0.0);
		PointSolution q = PointSolutionUtils.createSolution(0.0, 0.0);

		assertThrows(IllegalArgumentException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, new double[2], new double[0]);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, new double[0], new double[2]);
		});
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWithFminAndFmaxDifferentFromSolutions() {

		PointSolution p = PointSolutionUtils.createSolution(0.0, 0.0);
		PointSolution q = PointSolutionUtils.createSolution(0.0, 0.0);

		assertThrows(IllegalArgumentException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, new double[2], new double[3]);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			EuclideanDistanceUtils.calculate(p, q, new double[3], new double[2]);
		});
	}

	@Test
	public void shouldReturnZeroWithTwoEqualPoints() {

		PointSolution p = PointSolutionUtils.createSolution(0.0, 1.0);
		PointSolution q = PointSolutionUtils.createSolution(0.0, 1.0);

		assertEquals(0.0, EuclideanDistanceUtils.calculate(p, q, new double[] { 0.0, 1.0 }, new double[] { 1.0, 2.0 }));
	}
}
