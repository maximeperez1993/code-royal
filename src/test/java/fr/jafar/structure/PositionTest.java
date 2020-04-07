package fr.jafar.structure;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class PositionTest {

	@Test
	void moveExactlyTo() {
		// Given
		Position unit = new Position(40, 40);
		Position target = new Position(100, 40);

		// When
		Position result = unit.moveExactlyTo(target, 40);

		// Then
		Assert.assertEquals(new Position(80, 40), result);
	}

	@Test
	void moveExactlyTo2() {
		// Given
		Position unit = new Position(40, 40);
		Position target = new Position(200, 120);

		// When
		Position result = unit.moveExactlyTo(target, 60);

		// Then
		Assert.assertEquals(new Position(93, 66), result);
	}
}