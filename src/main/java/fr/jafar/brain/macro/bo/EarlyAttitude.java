package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EarlyAttitude implements Attitude {

    private static final List<BuildRequest> BUILD_ORDER = Arrays.asList(
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT),
            new BuildRequest().a(StructureType.TOWER),
            new BuildRequest().a(StructureType.TOWER)
    );

    private final Manager manager;
    private final Escaper escaper;
    private final List<BuildRequest> buildOrder;

    public EarlyAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
        this.buildOrder = new ArrayList<>();
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
        if (shouldBuildNext()) {
            this.buildOrder.add(BUILD_ORDER.get(this.buildOrder.size()).at(site).log("Build order #" + this.buildOrder.size()));
            return this.buildOrder.get(this.buildOrder.size() - 1);
        }
        return this.buildOrder.get(this.buildOrder.size() - 1);
    }

    public boolean isFinish() {
        return buildOrder.size() == BUILD_ORDER.size() && shouldBuildNext();
    }

    private boolean shouldBuildNext() {
        return buildOrder.isEmpty() || !buildOrder.get(buildOrder.size() - 1).getSite().isNeutral();
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }
}
