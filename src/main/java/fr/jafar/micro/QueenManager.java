package fr.jafar.micro;

import fr.jafar.Manager;
import fr.jafar.api.Finder;
import fr.jafar.api.Position;
import fr.jafar.site.SiteManager;
import fr.jafar.site.StructureType;
import fr.jafar.unit.Unit;
import fr.jafar.unit.UnitManager;
import fr.jafar.unit.UnitType;
import fr.jafar.util.MapInfos;

public class QueenManager {

    private final SiteManager siteManager;
    private final UnitManager unitManager;
    private final Builder builder;
    private final Unit myQueen;
    private final Escaper escaper;

    public QueenManager(Manager manager) {
        this.siteManager = manager.getSiteManager();
        this.unitManager = manager.getUnitManager();
        this.builder = new Builder(manager);
        this.myQueen = this.unitManager.getMyQueen();
        this.escaper = new Escaper(myQueen, this.unitManager);
    }

    public void build() {
        if (this.siteManager.getMyBarracks().isEmpty()) {
            System.err.println("Try to build BARRACKS closest my queen");
            this.builder.a(StructureType.BARRACKS)
                    .of(UnitType.KNIGHT)
                    .at(new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(this.myQueen).get())
                    .build();
            return;
        }

        if (this.siteManager.getMyMines().isEmpty()) {
            System.err.println("Try to build MINE closest my first BARRACKS");
            this.builder.a(StructureType.MINE)
                    .at(new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(this.siteManager.getMyBarracks().get(0)).get())
                    .build();
            return;
        }

        if (this.siteManager.getMyBarracks().size() == 1) {
            System.err.println("Try to build next middle");
            this.builder.a(StructureType.BARRACKS)
                    .of(UnitType.KNIGHT)
                    .at(new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(MapInfos.MIDDLE).get())
                    .build();
            return;
        }

        if (!this.siteManager.getNeutralSites().isEmpty()) {
            System.err.println("Try to build tower");
            this.builder.a(StructureType.TOWER)
                    .at(new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(myQueen).get())
                    .build();
            return;
        }

        System.err.println("Try to escape");
        this.move(this.escaper.getEscapePosition());
    }

    private void move(Position position) {
        System.out.println(String.format("MOVE %d %d", position.getX(), position.getY()));
    }
}
