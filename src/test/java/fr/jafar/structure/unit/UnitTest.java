package fr.jafar.structure.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import fr.jafar.structure.Position;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;

class UnitTest {

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
}