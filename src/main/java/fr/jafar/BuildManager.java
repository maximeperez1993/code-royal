package fr.jafar;

import fr.jafar.site.Site;
import fr.jafar.site.SiteManager;
import fr.jafar.unit.Unit;
import fr.jafar.unit.UnitManager;
import fr.jafar.util.Position;
import fr.jafar.util.Positionable;
import fr.jafar.util.PositionnableComparator;

public class BuildManager {

	private final SiteManager siteManager;
	private final UnitManager unitManager;
	private final Unit myQueen;

	public BuildManager(SiteManager siteManager, UnitManager unitManager) {
		this.siteManager = siteManager;
		this.unitManager = unitManager;
		this.myQueen = this.unitManager.getMyQueen();
	}

	public void build() {
		if (siteManager.getMySites().isEmpty()) {
			this.buildClosest(unitManager.getMyQueen());
		} else if (siteManager.getMySites().size() == 1) {

			if (!siteManager.getHisTrainingSites().isEmpty()) {
				this.move(siteManager.getHisTrainingSites().stream()
					.min(new PositionnableComparator(myQueen))
					.orElseThrow(IllegalArgumentException::new));
			} else {
				this.buildClosest(unitManager.getHisQueen());
			}

		} else {
			this.escape();
		}
	}

	private void buildClosest(Positionable positionable) {
		this.build(siteManager.getNeutralSites().stream()
			.min(new PositionnableComparator(positionable))
			.orElseThrow(IllegalArgumentException::new));
	}

	private void escape() {
		this.build(siteManager.getNeutralSites().stream()
			.max(new PositionnableComparator(this.myQueen))
			.orElseThrow(IllegalArgumentException::new));
	}

	private void build(Site site) {
		System.out.println(String.format("BUILD %d BARRACKS-KNIGHT", site.getId()));
	}

	private void move(Positionable positionable) {
		this.move(positionable.getPosition());
	}

	private void move(Position position) {
		System.out.println(String.format("MOVE %d %d", position.getX(), position.getY()));
	}
}
