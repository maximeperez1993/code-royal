package fr.jafar.structure;

public interface Positionable {

	Position getPosition();

	int getRadius();

	default boolean isBetween(Positionable po1, Positionable po2, int delta) {
		Position p = getPosition();
		Position p1 = po1.getPosition();
		Position p2 = po2.getPosition();
		return p.getX() >= Math.min(p1.getX(), p2.getX()) - delta &&
				p.getX() <= Math.max(p1.getX(), p2.getX()) + delta &&
				p.getY() >= Math.min(p1.getY(), p2.getY()) - delta &&
				p.getY() <= Math.max(p1.getY(), p2.getY()) + delta;
	}

	default double getDistance(Positionable positionable) {
		return getPosition().getDistance(positionable.getPosition()) - (this.getRadius() + positionable.getRadius());
	}

	//  (r1+r2)² >= (x1-x2)² + (y1-y2)²
	default boolean isInCollision(Positionable element) {
		Position p = element.getPosition();
		return (getRadius() + element.getRadius()) * (getRadius() + element.getRadius()) >=
			(p.getX() - getPosition().getX()) * (p.getX() - getPosition().getX()) +
				(p.getY() - getPosition().getY()) * (p.getY() - getPosition().getY());
	}

}
