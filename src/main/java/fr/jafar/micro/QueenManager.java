package fr.jafar.micro;

import fr.jafar.api.Position;
import fr.jafar.api.Positionable;
import fr.jafar.site.Site;
import fr.jafar.site.SiteManager;
import fr.jafar.site.StructureType;
import fr.jafar.unit.Unit;
import fr.jafar.unit.UnitManager;
import fr.jafar.util.MapInfos;
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
        if (this.siteManager.getMyBarracks().isEmpty()) {
            System.err.println("Try to build closest my queen");
            this.buildClosest(StructureType.BARRACKS, unitManager.getMyQueen());
        } else if (this.siteManager.getMyBarracks().size() == 1) {
            System.err.println("Try to build next middle");
            this.buildClosest(StructureType.BARRACKS, MapInfos.MIDDLE);
        } else {
            if (!this.siteManager.getNeutralSites().isEmpty()) {
                System.err.println("Try to build tower");
                this.buildClosest(StructureType.TOWER, unitManager.getMyQueen());
            } else {
                System.err.println("Try to escape");
                escape();
            }
        }
    }

    private void buildClosest(StructureType structureType, Positionable positionable) {
        this.buildClosest(structureType, positionable.getPosition());
    }

    private void buildClosest(StructureType structureType, Position position) {
        this.build(structureType, siteManager.getNeutralSites().stream()
                .min(new PositionableComparator(position))
                .orElseThrow(IllegalArgumentException::new));
    }

    private void escape() {
        this.move(this.escaper.getFarthestPosition(MapInfos.CARDINALS));
    }


    private void build(StructureType structureType, Site site) {
        System.out.println(String.format("BUILD %d %s-KNIGHT", site.getId(), structureType.name()));
    }

    private void move(Positionable positionable) {
        this.move(positionable.getPosition());
    }

    private void move(Position position) {
        System.out.println(String.format("MOVE %d %d", position.getX(), position.getY()));
    }
}
