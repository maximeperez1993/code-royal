package fr.jafar.brain.micro;

import java.util.List;
import java.util.Optional;

import fr.jafar.brain.macro.BuildRequest;
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

	public String goBuild(BuildRequest buildRequest) {
		Site target = buildRequest.getSite();
		Position position = goTo(manager.my().queen(), target).getPosition();
		if (position == target.getPosition()) {
			return buildRequest.build();
		}
		System.err.println(String.format("[%s] : %s", buildRequest.getLog(), buildRequest));
		return String.format("MOVE %d %d", position.getX(), position.getY());
	}

	public String goTo(Positionable target) {
		Position position = goTo(manager.my().queen(), target).getPosition();
		return String.format("MOVE %d %d", position.getX(), position.getY());
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
			Unit futureUnit = new Unit.Builder(unit).position(futurePosition).build();
			if (target.isInCollision(futureUnit)) {
				return Optional.empty();
			}
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
