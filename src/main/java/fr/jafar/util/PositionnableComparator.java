package fr.jafar.util;

import java.util.Comparator;

public class PositionnableComparator implements Comparator<Positionable> {

	private final PositionComparator positionComparator;

	public PositionnableComparator(Positionable origin) {
		this.positionComparator = new PositionComparator(origin.getPosition());
	}

	@Override
	public int compare(Positionable p1, Positionable p2) {
		return positionComparator.compare(p1.getPosition(), p2.getPosition());
	}
}