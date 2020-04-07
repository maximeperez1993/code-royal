package fr.jafar.structure.unit;

import fr.jafar.structure.Position;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class UnitTest {

    @Test
    void isBetweenShouldReturnTrue() {
        // Given
        Unit queen = new Unit(new Position(230, 0), Team.FRIENDLY, UnitType.QUEEN, 100);
        Unit soldier = new Unit(new Position(180, 0), Team.FRIENDLY, UnitType.KNIGHT, 100);
        Site site = new Site(0, new Position(200, 0), 60);

        // When
        boolean result = site.isBetween(soldier, queen, 0);

        // Then
        Assert.assertTrue(result);
    }

    @Test
    void isBetweenShouldReturnFalse() {
        // Given
        Unit queen = new Unit(new Position(170, 0), Team.FRIENDLY, UnitType.QUEEN, 100);
        Unit soldier = new Unit(new Position(120, 0), Team.FRIENDLY, UnitType.KNIGHT, 100);
        Site site = new Site(0, new Position(200, 0), 60);

        // When
        boolean result = site.isBetween(soldier, queen, 0);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    void isInCollisionShouldReturnTrue() {
        // Given
        Unit unit = new Unit(new Position(100, 100), Team.FRIENDLY, UnitType.QUEEN, 100);
        Site site = new Site(0, new Position(160, 160), 60);

        // When
        boolean result = unit.isInCollision(site);

		// Then
		Assert.assertEquals(result, site.isInCollision(unit));
		Assert.assertTrue(result);
	}

	@Test
	void isInCollisionShouldReturnFalse() {
		// Given
		Unit unit = new Unit(new Position(100, 100), Team.FRIENDLY, UnitType.QUEEN, 100);
		Site site = new Site(0, new Position(160, 160), 30);

		// When
		boolean result = unit.isInCollision(site);

		// Then
		Assert.assertEquals(result, site.isInCollision(unit));
		Assert.assertFalse(result);
	}

	@Test
	void getDistance() {
		// Given
		Unit unit = new Unit(new Position(100, 100), Team.FRIENDLY, UnitType.QUEEN, 100);
		Position position = new Position(200, 100);
		// When
		double distance = unit.getDistance(position);

		// Then
		Assert.assertEquals(70, distance, 0.1);
	}

	@Test
	void getDistance2() {
		// Given
		Unit unit = new Unit(new Position(100, 100), Team.FRIENDLY, UnitType.QUEEN, 100);
		Unit unit2 = new Unit(new Position(200, 100), Team.FRIENDLY, UnitType.QUEEN, 100);
		// When
		double distance = unit.getDistance(unit2);

		// Then
		Assert.assertEquals(40, distance, 0.1);
	}
}