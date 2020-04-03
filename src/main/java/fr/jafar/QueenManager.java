package fr.jafar;

import fr.jafar.site.Site;
import fr.jafar.site.SiteManager;
import fr.jafar.unit.Unit;
import fr.jafar.unit.UnitManager;
import fr.jafar.util.Position;
import fr.jafar.util.Positionable;
import fr.jafar.util.PositionableComparator;

public class QueenManager {

	private final SiteManager siteManager;
	private final UnitManager unitManager;
	private final Unit myQueen;
	private final Escaper escaper;

	public QueenManager(SiteManager siteManager, UnitManager unitManager) {
		this.siteManager = siteManager;
		this.unitManager = unitManager;
		this.myQueen = this.unitManager.getMyQueen();
		this.escaper = new Escaper(myQueen, this.unitManager);
	}

	public void build() {
		if (siteManager.getMySites().isEmpty()) {
			System.err.println("Try to build closest my queen");
			this.buildClosest(unitManager.getMyQueen());
		}else if(this.siteManager.getMySites().size() == 1){
			System.err.println("Try to build next middle");
			this.buildClosest(MapInfos.MIDDLE);
		} else {
			System.err.println("Try to escape");
			this.escape();
		}
	}

	private void buildClosest(Positionable positionable) {
		this.buildClosest(positionable.getPosition());
	}

	private void buildClosest(Position position) {
		this.build(siteManager.getNeutralSites().stream()
				.min(new PositionableComparator(position))
				.orElseThrow(IllegalArgumentException::new));
	}

	private void escape() {
		this.move(this.escaper.getFarthestPosition(MapInfos.CARDINALS));
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
