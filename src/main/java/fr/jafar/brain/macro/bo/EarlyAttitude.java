package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.brain.micro.PathFinder;
import fr.jafar.info.Manager;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EarlyAttitude implements Attitude {


    private static final List<BuildRequest> HIGH_HP_BO = Arrays.asList(
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT)
    );

    private static final List<BuildRequest> LOW_HP_BO = Arrays.asList(
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT),
            new BuildRequest().a(StructureType.TOWER)
    );


    private final Manager manager;
    private final Escaper escaper;
    private final PathFinder pathFinder;
    private final List<BuildRequest> buildOrder;
    private boolean isFinish;
    private List<BuildRequest> buildOrderModel;

    public EarlyAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
        this.pathFinder = new PathFinder(manager);
        this.buildOrderModel = HIGH_HP_BO;
        this.buildOrder = new ArrayList<>();
    }

    @Override
    public String order(StateInfo i) {
        this.buildOrderModel = manager.getStartHp() <= 40 ? LOW_HP_BO : HIGH_HP_BO;
        if (i.isTouchSiteUpdatable()) {
            return pathFinder.goBuild(upgrade(i.getTouchedSite()));
        }

        if (shouldBuildNext()) {
            prepareNext(i);
        }

        return pathFinder.goBuild(this.build(i.getClosestSafeFreeSite()).log("Build closest"));
    }

    @Override
    public BuildRequest build(Site site) {
        return this.buildOrder.get(this.buildOrder.size() - 1);
    }


    private void prepareNext(StateInfo i) {
        BuildRequest currentBuildOrder = buildOrderModel.get(this.buildOrder.size());
        currentBuildOrder = currentBuildOrder.at(i.getClosestSafeFreeSite()).log("Build order #" + this.buildOrder.size());
        this.buildOrder.add(currentBuildOrder);
    }

    public boolean isFinish() {
        if (!isFinish) {
            isFinish = buildOrder.size() == buildOrderModel.size() && shouldBuildNext();
        }
        return isFinish;
    }

    private boolean shouldBuildNext() {
        return buildOrder.isEmpty() || !buildOrder.get(buildOrder.size() - 1).getSite().isNeutral();
    }

    private boolean hasBarracks() {
        return manager.my().barracks().findAny().isPresent();
    }
}
