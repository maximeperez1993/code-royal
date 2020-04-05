package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

public class DefensiveAttitude implements Attitude {

    private final Manager manager;
    private final Escaper escaper;

    public DefensiveAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
    }

    @Override
    public String order(StateInfo i) {
        if (i.isTouchSiteTowerLowHp()) {
            return upgrade(i.getTouchedSite());
        }
        if (i.isCloserSoldierUnder(1000)) {
            System.err.println("Escape !!");
            return this.move(escaper.getEscapePosition());
        }
        return this.build(i.getClosestFreeSite()).log("Soldiers far away, we can try to build").build();
    }

    @Override
    public BuildRequest build(Site site) {
        if (!hasBarracks() && hasTower()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT).at(site);
        }
        return new BuildRequest().a(StructureType.TOWER).at(site);
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }

    private boolean hasTower() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isTower);
    }
}
