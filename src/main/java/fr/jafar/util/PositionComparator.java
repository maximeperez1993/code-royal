package fr.jafar.util;

import java.util.Comparator;

public class PositionComparator implements Comparator<Position> {

	private final Position origin;

	public PositionComparator(Position origin) {this.origin = origin;}

	@Override
	public int compare(Position p1, Position p2) {
		return (int)(origin.getDistance(p1) - origin.getDistance(p2));
	}
}

