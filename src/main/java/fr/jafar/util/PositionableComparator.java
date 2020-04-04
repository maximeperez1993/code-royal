package fr.jafar.util;

import fr.jafar.structure.Position;
import fr.jafar.structure.Positionable;

import java.util.Comparator;

public class PositionableComparator implements Comparator<Positionable> {

	private final PositionComparator positionComparator;

	public PositionableComparator(Positionable origin) {
		this.positionComparator = new PositionComparator(origin.getPosition());
	}

	public PositionableComparator(Position position) {
		this.positionComparator = new PositionComparator(position);
	}


	@Override
	public int compare(Positionable p1, Positionable p2) {
		return positionComparator.compare(p1.getPosition(), p2.getPosition());
	}
}