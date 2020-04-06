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


    private static final List<BuildRequest> HIGH_HP_BO = Arrays.asList(
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT),
            new BuildRequest().a(StructureType.TOWER),
            new BuildRequest().a(StructureType.TOWER)
    );

    private static final List<BuildRequest> LOW_HP_BO = Arrays.asList(
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.MINE),
            new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT),
            new BuildRequest().a(StructureType.TOWER),
            new BuildRequest().a(StructureType.TOWER),
            new BuildRequest().a(StructureType.MINE)

    );


    private final Manager manager;
    private final Escaper escaper;
    private final List<BuildRequest> buildOrder;
    private boolean isFinish;
    private List<BuildRequest> buildOrderModel;

    public EarlyAttitude(Manager manager, Escaper escaper) {
        this.manager = manager;
        this.escaper = escaper;
        this.buildOrderModel = HIGH_HP_BO;
        this.buildOrder = new ArrayList<>();
    }

    @Override
    public String order(StateInfo i) {
        this.buildOrderModel = manager.getStartHp() <= 35 ? LOW_HP_BO : HIGH_HP_BO;
        if (i.isTouchSiteUpdatable()) {
            return upgrade(i.getTouchedSite());
        }

        if (shouldBuildNext()) {
            prepareNext(i);
        }

        return this.build(i.getClosestFreeSite()).log("Build closest").build();
    }

    @Override
    public BuildRequest build(Site site) {
        return this.buildOrder.get(this.buildOrder.size() - 1);
    }


    private void prepareNext(StateInfo i) {
        BuildRequest currentBuildOrder = buildOrderModel.get(this.buildOrder.size());
        currentBuildOrder = currentBuildOrder.at(i.getClosestFreeSite()).log("Build order #" + this.buildOrder.size());
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
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }
}
