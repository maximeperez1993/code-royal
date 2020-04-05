package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

public class AggressiveAttitude implements Attitude {

    private final Manager manager;
    private final Escaper escaper;

    public AggressiveAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
    }

    @Override
    public String order(StateInfo i) {
        if (i.isTouchSiteUpdatable()) {
            return upgrade(i.getTouchedSite());
        }
        if (i.isClosestFreeSiteAtRange()) {
            return this.build(i.getClosestFreeSite()).log("Build closest").build();
        }
        //TODO : Should optimize movement to target
        return this.build(i.getClosestFreeSite()).log("Move to closest to build").build();
    }

    @Override
    public BuildRequest build(Site site) {
        if (!hasBarracks()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT).at(site);
        }

        if (isEnemyTrainingSoldiers()) {
            return new BuildRequest().a(StructureType.TOWER).at(site).log("Enemy is training soldiers");
        }

        // TODO : if is in our safe zone, maybe it's better to build a mine
        if (site.hasRemainingGold() && site.getMaxMineSize() >= 2) {
            return new BuildRequest().a(StructureType.MINE).at(site);
        }

        return new BuildRequest().a(StructureType.TOWER).at(site);
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }

    private boolean isEnemyTrainingSoldiers() {
        return manager.getSiteManager().getHisTrainingSites().stream()
                .filter(Site::isBarrack).filter(site -> site.getState().getUnitType() == UnitType.KNIGHT)
                .anyMatch(Site::isTraining);
    }
}
