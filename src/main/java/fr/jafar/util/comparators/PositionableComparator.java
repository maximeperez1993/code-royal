package fr.jafar.util.comparators;

import java.util.Comparator;

import fr.jafar.structure.Positionable;

public class PositionableComparator implements Comparator<Positionable> {

	private final Positionable origin;

	public PositionableComparator(Positionable origin) {
		this.origin = origin;
	}

	@Override
	public int compare(Positionable p1, Positionable p2) {
		return (int)(origin.getDistance(p1) - origin.getDistance(p2));
	}
}