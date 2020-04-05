package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;
import fr.jafar.util.Finder;
import fr.jafar.util.MapInfos;

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
    private boolean isFinish;

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

        if (shouldBuildNext()) {
            BuildRequest currentBuildOrder = BUILD_ORDER.get(this.buildOrder.size());
            currentBuildOrder = currentBuildOrder.at(getSite(currentBuildOrder, i)).log("Build order #" + this.buildOrder.size());
            this.buildOrder.add(currentBuildOrder);
        }

        if (i.isClosestFreeSiteAtRange()) {
            return this.build(i.getClosestFreeSite()).log("Build closest").build();
        }
        //TODO : Should optimize movement to target
        return this.build(i.getClosestFreeSite()).log("Move to closest to build").build();
    }

    private Site getSite(BuildRequest buildRequest, StateInfo i) {
        if (buildRequest.getStructureType() == StructureType.TOWER) {
            buildRequest.log("Aggressive early tower");
            return new Finder<>(manager.getSiteManager().getNeutralSites()).sortByClosestFrom(MapInfos.MIDDLE).get();
        }
        return i.getClosestFreeSite();
    }

    @Override
    public BuildRequest build(Site site) {
        return this.buildOrder.get(this.buildOrder.size() - 1);
    }


    public boolean isFinish() {
        if (!isFinish) {
            isFinish = buildOrder.size() == BUILD_ORDER.size() && shouldBuildNext();
        }
        return isFinish;
    }

    private boolean shouldBuildNext() {
        return buildOrder.isEmpty() || !buildOrder.get(buildOrder.size() - 1).getSite().isNeutral();
    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }
}
