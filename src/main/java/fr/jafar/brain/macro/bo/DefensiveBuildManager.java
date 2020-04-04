package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

public class DefensiveBuildManager implements BuildManager {

    private final Manager manager;

    public DefensiveBuildManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public BuildRequest build(Site site) {
        if (!hasBarracks()) {
            return new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT);
        }
        return new BuildRequest().a(StructureType.TOWER);
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }
}
