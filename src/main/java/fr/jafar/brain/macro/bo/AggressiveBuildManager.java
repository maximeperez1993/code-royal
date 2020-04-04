package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

public class AggressiveBuildManager implements BuildManager {

    private final Manager manager;

    public AggressiveBuildManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public BuildRequest build(Site site) {
        if (site.hasRemainingGold() && site.getMaxMineSize() >= 2) {
            return new BuildRequest().a(StructureType.MINE);
        }
        if (!hasBarracks()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT);
        }
        return new BuildRequest().a(StructureType.TOWER);
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }
}
