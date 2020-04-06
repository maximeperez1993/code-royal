package fr.jafar.brain.macro.bo;

import fr.jafar.Manager;
import fr.jafar.brain.macro.BuildRequest;
import fr.jafar.brain.micro.Escaper;
import fr.jafar.structure.Positionable;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.UnitType;

import java.util.ArrayList;
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
        Optional<Site> hisBarrack = manager.getSiteManager().getHisTrainingKnightBarracks().stream().findFirst();
        if (hisBarrack.isPresent()) {
            danger = hisBarrack.get();
        } else {
            danger = manager.getUnitManager().getHisQueen();
        }

        List<Site> mySitesAndTarget = new ArrayList<>(manager.getSiteManager().getMySites());
        mySitesAndTarget.add(site);
        mySitesAndTarget = mySitesAndTarget.stream()
                .sorted((s1, s2) -> (int) (s1.getDistance(danger) - s2.getDistance(danger)))
                .collect(Collectors.toList());
        System.err.println(mySitesAndTarget);
        if (mySitesAndTarget.size() < 2) {
            return true;
        }
        return mySitesAndTarget.get(0) == site || mySitesAndTarget.get(1) == site;

    }

    private boolean hasBarracks() {
        return manager.getSiteManager().getMySites().stream().anyMatch(Site::isBarrack);
    }

    private boolean isEnemyTrainingSoldiers() {
        return manager.getSiteManager().getHisTrainingKnightBarracks().stream()
                .anyMatch(Site::isTraining);
    }
}
