package fr.jafar.brain.micro;

import java.util.List;
import java.util.Optional;

import fr.jafar.info.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.util.comparators.MyComparators;

public class PathFinder {

	private final Manager manager;
	private final Escaper escaper;

	public PathFinder(Manager manager) {
		this.manager = manager;
		this.escaper = new Escaper(manager);
	}

	public Positionable goTo(Unit unit, Positionable target) {
		Optional<Site> collisionSite = getCollision(unit, target);
		if (collisionSite.isPresent()) {
			return getTangent(unit, collisionSite.get(), target);
		}
		return target;
	}

	public Optional<Site> getCollision(Unit unit, Positionable target) {
		for (int i = 1; i < unit.getMaxSteps(); i++) {
			Position futurePosition = unit.getPosition().moveExactlyTo(target.getPosition(), i);
			if (futurePosition.equals(target.getPosition())) {
				return Optional.empty();
			}
			Unit futureUnit = new Unit.Builder(unit).position(futurePosition).build();
			Optional<Site> optionalSite = manager.allSites().stream()
				.sorted(MyComparators.distanceFrom(futurePosition))
				.filter(site -> site.isInCollision(futureUnit))
				.findFirst();
			if (optionalSite.isPresent()) {
				return optionalSite;
			}
		}
		return Optional.empty();
	}

	public Unit getTangent(Unit unit, Positionable obstacle, Positionable target) {
		List<Position> positions = escaper.getEscapePositions(unit);
		return positions.stream()
			.map(p -> new Unit.Builder(unit).position(p).build())
			.filter(futureUnit -> !futureUnit.isInCollision(obstacle))
			.min(MyComparators.distanceFrom(target))
			.orElseThrow(IllegalStateException::new);
	}
}
