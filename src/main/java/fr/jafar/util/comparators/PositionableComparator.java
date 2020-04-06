package fr.jafar.util.comparators;

import fr.jafar.structure.Positionable;

import java.util.Comparator;

public class PositionableComparator implements Comparator<Positionable> {

	private final PositionComparator positionComparator;

	public PositionableComparator(Positionable origin) {
		this.positionComparator = new PositionComparator(origin.getPosition());
	}

	@Override
	public int compare(Positionable p1, Positionable p2) {
		return positionComparator.compare(p1.getPosition(), p2.getPosition());
	}
}