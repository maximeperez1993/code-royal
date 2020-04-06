package fr.jafar.brain.macro.bo;

import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.info.Manager;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;
import fr.jafar.util.comparators.MyComparators;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        return this.build(i.getClosestFreeSite()).log("Build closest").build();
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
        if (site.hasRemainingGold() && site.getMaxMineSize() >= 2 && !isAtFrontLine(site)) {
            return new BuildRequest().a(StructureType.MINE).at(site);
        }

        return new BuildRequest().a(StructureType.TOWER).at(site);
    }

    private boolean isAtFrontLine(Site site) {
        Positionable danger;
        Optional<Site> hisBarrack = manager.his().trainingKnightBarracks().findFirst();
        if (hisBarrack.isPresent()) {
            danger = hisBarrack.get();
        } else {
            danger = manager.his().queen();
        }

        List<Site> mySitesAndTarget = manager.my().sites().collect(Collectors.toList());
        mySitesAndTarget.add(site);
        mySitesAndTarget.sort(MyComparators.distanceFrom(danger));
        System.err.println(mySitesAndTarget);
        if (mySitesAndTarget.size() < 2) {
            return true;
        }
        return mySitesAndTarget.get(0) == site || mySitesAndTarget.get(1) == site;

    }

    private boolean hasBarracks() {
        return manager.my().barracks().findAny().isPresent();
    }

    private boolean isEnemyTrainingSoldiers() {
        return manager.his().trainingKnightBarracks().findAny().isPresent();
    }
}
