package fr.jafar.brain.micro;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import fr.jafar.info.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathFinderTest {

	private final Position target = new Position(224, 114);

	/**
	 * J'ai fais un graphique ici : https://www.geogebra.org/classic/tyyut7qk
	 */
	@Test
	public void goTo() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(40, 40);
		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		List<Position> expectedPositions = Arrays.asList(
			new Position(43, 99),
			new Position(76, 148),
			new Position(124, 182),
			new Position(181, 167),
			new Position(222, 125),
			new Position(224, 114)
		);
		// When
		for (Position expectedPosition : expectedPositions) {
			Positionable newPosition = pathFinder.goTo(queen, target);
			queen = new Unit.Builder(queen).position(newPosition.getPosition()).build();
			Assert.assertEquals(expectedPosition, queen.getPosition());
		}

		// Then
		Assert.assertEquals(target.getPosition(), queen.getPosition());
	}

	@Test
	public void getCollision() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(40, 40);
		Position target = new Position(200, 120);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Optional<Site> collision = pathFinder.getCollision(queen, target);

		// Then
		Assert.assertTrue(collision.isPresent());
		Assert.assertEquals(obstacle, collision.get());
	}

	@Test
	public void getCollision2() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(124, 182);
		Position target = new Position(200, 120);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Optional<Site> collision = pathFinder.getCollision(queen, target);

		// Then
		Assert.assertTrue(collision.isPresent());
		Assert.assertEquals(obstacle, collision.get());
	}

	@Test
	public void getCollision3() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(222, 125);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Optional<Site> collision = pathFinder.getCollision(queen, target);

		// Then
		Assert.assertFalse(collision.isPresent());
	}

	@Test
	public void getTangent1() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(40, 40);
		Position target = new Position(200, 120);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Positionable tangent = pathFinder.getTangent(queen, obstacle, target);

		// Then
		Assert.assertEquals(new Position(43, 99), tangent.getPosition());
	}

	@Test
	public void getTangent2() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(43, 99);
		Position target = new Position(200, 120);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Positionable tangent = pathFinder.getTangent(queen, obstacle, target);

		// Then
		Assert.assertEquals(new Position(76, 148), tangent.getPosition());
	}

	@Test
	public void getTangent3() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(76, 148);
		Position target = new Position(200, 120);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Positionable tangent = pathFinder.getTangent(queen, obstacle, target);

		// Then
		Assert.assertEquals(new Position(124, 182), tangent.getPosition());
	}

	@Test
	public void getTangent4() {
		// Given
		Site obstacle = buildSite(140, 80, 60);
		Unit queen = buildQueen(124, 182);
		Position target = new Position(200, 120);

		Manager manager = mockManager(Arrays.asList(obstacle));
		PathFinder pathFinder = new PathFinder(manager);

		// When
		Positionable tangent = pathFinder.getTangent(queen, obstacle, target);

		// Then
		Assert.assertEquals(new Position(181, 167), tangent.getPosition());
	}

	private Manager mockManager(List<Site> siteList) {
		Manager manager = mock(Manager.class);
		when(manager.allSites()).thenReturn(siteList);
		return manager;
	}

	private Site buildSite(int x, int y, int radius) {
		return new Site(0, new Position(x, y), radius);
	}

	private Unit buildQueen(int x, int y) {
		return new Unit(new Position(x, y), Team.FRIENDLY, UnitType.QUEEN, 100);
	}
}